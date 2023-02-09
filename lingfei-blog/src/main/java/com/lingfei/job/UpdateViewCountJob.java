package com.lingfei.job;

import com.lingfei.domain.entity.Article;
import com.lingfei.service.ArticleService;
import com.lingfei.utils.RedisCache;
import com.lingfei.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/6 20:17
 * @Decription:
 */
@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ArticleService articleService;

//    @Scheduled(cron="0/55 * * * * ?")
//    public void testJob(){
//        Object value = redisCache.getCacheMapValue("article:viewCount", "1");
//        System.out.println(value);
////        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");
//        Map<String, Object> viewCountMap = redisUtil.hmget("article:viewCount");
//        List<Article> articles = viewCountMap.entrySet()
//                .stream()
//                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
//                .collect(Collectors.toList());
//        articleService.updateBatchById(articles);
//
//    }
@Scheduled(cron = "0 0/2 * * * ?")
public void updateViewCount(){
    //获取redis中的浏览量:id为Long，viewCount为Long
    Map<Object, Object> viewCountMap = redisUtil.hmget("article:viewCount");

    //stream流返回Article，因对应要有相应的构造器！
    List<Article> articles = viewCountMap.entrySet()
            .stream()
            .map(entry -> new Article(Long.valueOf((String)entry.getKey()), ((Integer) entry.getValue()).longValue()))
            .collect(Collectors.toList());
    //更新到数据库中
    articleService.updateBatchById(articles);

}
}
