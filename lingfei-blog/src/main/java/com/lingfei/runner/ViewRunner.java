package com.lingfei.runner;

import com.lingfei.domain.entity.Article;
import com.lingfei.mapper.ArticleMapper;
import com.lingfei.utils.RedisCache;
import com.lingfei.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/6 20:12
 * @Decription:
 */

@Component
public class ViewRunner implements CommandLineRunner {


    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void run(String... args) throws Exception {
        //查询出所有文章
        List<Article> articles = articleMapper.selectList(null);
        //查询出文章的访问量
        Map<String, Object> viewCountMap = articles.stream().collect(
                Collectors.toMap(
                        article -> article.getId().toString(),
                        article -> article.getViewCount().intValue()
                )
        );
//        redisCache.setCacheMap("article:viewCount",viewCountMap);
        redisUtil.hmset("article:viewCount",viewCountMap);
    }
}
