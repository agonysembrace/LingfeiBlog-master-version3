package com.lingfei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Article;
import com.lingfei.domain.vo.ArticleVo;
import com.lingfei.domain.vo.PageVo;
import com.lingfei.dto.AddArticleDto;
import com.lingfei.dto.ArticleDto;


/**
 * 文章表(Article)表服务接口
 *
 * @author makejava
 * @since 2022-12-02 12:41:56
 */
public interface ArticleService extends IService<Article> {
    ResponseResult hotArticle();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);

    PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize);

    ArticleVo getInfo(Long id);

    void edit(ArticleDto article);
}

