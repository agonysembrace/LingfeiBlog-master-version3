package com.lingfei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingfei.constants.SystemConstants;
import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Article;
import com.lingfei.domain.entity.ArticleTag;
import com.lingfei.domain.entity.Category;
import com.lingfei.domain.vo.*;
import com.lingfei.dto.AddArticleDto;
import com.lingfei.dto.ArticleDto;
import com.lingfei.mapper.ArticleMapper;
import com.lingfei.service.ArticleService;
import com.lingfei.service.ArticleTagService;
import com.lingfei.service.CategoryService;
import com.lingfei.utils.BeanCopyUtils;
import com.lingfei.utils.RedisCache;
import com.lingfei.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2022-12-02 12:42:00
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public ResponseResult hotArticle() {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        //文章不能是草稿    //浏览量来降序排序
        wrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL).orderByDesc(Article::getViewCount);
        //分页展示：第一页的前10条
        Page page = new Page(1,10);
        page(page,wrapper);
        List<Article> articleList = page.getRecords();

        //利用Java反射，我根本不需要自己创建List对象！
        // ArrayList<HotArticleVo> hotArticleList = new ArrayList<>();
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articleList, HotArticleVo.class);
        return ResponseResult.okResult(hotArticleVos);

    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //如果有categoryId 查询和传入的要相同
        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
        //状态是正式发布
        queryWrapper.eq(Article ::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        //对isTop要进行降序,置顶的文章就会被放在最上面了
        queryWrapper.orderByDesc(Article :: getIsTop );

        //进行分页
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        //查询CategoryName
        List<Article> pageRecords = page.getRecords();
        //给查出来的文章赋予分类名称！
        List<Article> articleList = pageRecords.stream()
                .map(article -> {
                    article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
                    return article;
                }).collect(Collectors.toList());
        //封装成Vo
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articleList, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据Id查询文章
        Article article = getById(id);
        //从Redis中查出改文章的viewCount
        Integer viewCount = (Integer) redisUtil.hget("article:viewCount", id.toString());
//        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //转换为Vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据Id获取分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category != null) {
            articleDetailVo.setCategoryName(category.getName());
        }
        return ResponseResult.okResult(articleDetailVo);

    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //封装配置类，使值自增1
//        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        redisUtil.hincr("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult add(AddArticleDto articleDto) {
        //添加 博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(article.getTitle()),Article::getTitle, article.getTitle());
        queryWrapper.like(StringUtils.hasText(article.getSummary()),Article::getSummary, article.getSummary());

        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);

        //转换成VO
        List<Article> articles = page.getRecords();

        //这里偷懒没写VO的转换 应该转换完在设置到最后的pageVo中

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(articles);
        return pageVo;
    }

    @Override
    public ArticleVo getInfo(Long id) {
        Article article = getById(id);
        //获取关联标签
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        List<ArticleTag> articleTags = articleTagService.list(articleTagLambdaQueryWrapper);
        List<Long> tags = articleTags.stream().map(articleTag -> articleTag.getTagId()).collect(Collectors.toList());

        ArticleVo articleVo = BeanCopyUtils.copyBean(article,ArticleVo.class);
        articleVo.setTags(tags);
        return articleVo;
    }

    @Override
    public void edit(ArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        //更新博客信息
        updateById(article);
        //删除原有的 标签和博客的关联
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(articleTagLambdaQueryWrapper);
        //添加新的博客和标签的关联信息
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(articleDto.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
    }
}
