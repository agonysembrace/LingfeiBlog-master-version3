package com.lingfei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingfei.domain.entity.ArticleTag;
import com.lingfei.mapper.ArticleTagMapper;
import com.lingfei.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2022-12-02 12:42:02
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
