package com.lingfei.Controller;


import com.lingfei.cache.LRUCache;
import com.lingfei.config.FastJsonRedisSerializer;
import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Article;
import com.lingfei.domain.vo.ArticleDetailVo;
import com.lingfei.factory.SingletonFactory;
import com.lingfei.service.ArticleService;
import com.lingfei.utils.BeanCopyUtils;
import com.lingfei.utils.CacheSingletonUtil;
import com.lingfei.utils.CompressRedis;
import com.lingfei.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.lingfei.utils.CacheSingletonUtil.ARTICLE_DETALE_KEY;

@RestController
public class JmeterController {

    @Autowired
    RedisUtil redisUtil;

//    int i = 0;
//    @GetMapping("/jmeter")
//    public void jmeter(){
//        String name = Thread.currentThread().getName();
//        System.out.println("Thread name : "+ name +" num =  "+i++);
//
//    }
@Autowired
ArticleService articleService;

    @GetMapping("/jmeter")
    public ResponseResult jmeter(){
        int id = 10;
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer<>(ArticleDetailVo.class);
        ArticleDetailVo articleDetailVo = new ArticleDetailVo();
        Article article =articleService.getById(id);
        articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        System.out.println(articleDetailVo.getContent());
        byte[] serialize = serializer.serialize(articleDetailVo);
        redisUtil.set("test:1",new String(serialize, StandardCharsets.UTF_8));
        String content = (String) redisUtil.get("test:1");
        articleDetailVo = (ArticleDetailVo)serializer.deserialize(content.getBytes(StandardCharsets.UTF_8));
        return ResponseResult.okResult(articleDetailVo);
    }
    @GetMapping("/jmeter1")
    public String fun(){
        redisUtil.get("article:detail:"+10);
        return "访问成功";
    }

    @GetMapping("/jmeter2")
    public Article fun2(){
        Article article ;
        article  = (Article) CacheSingletonUtil.getInstance().getCacheData(ARTICLE_DETALE_KEY);
        if(article!=null){
            return article;
        }else{
            article = articleService.getById(10);
            CacheSingletonUtil.getInstance().addCacheData(ARTICLE_DETALE_KEY,article);
        }
        return article;
    }
    @GetMapping("/jemter3/{id}")
    public ResponseResult fun3(@PathVariable("id") Long id) throws IOException, ClassNotFoundException {



        Article article = null;
//        LRUCache lruCache = SingletonFactory.getInstance(LRUCache.class);
        LRUCache lruCache = LRUCache.getInstance();
        article = (Article) lruCache.get(ARTICLE_DETALE_KEY+id);
        if(article!=null){
            return ResponseResult.okResult(article);
        }else{
            article = articleService.getById(id);
            lruCache.put(ARTICLE_DETALE_KEY+id,article);
        }
        return ResponseResult.okResult(article);
    }
    @GetMapping("/jmeter4/{id}")
    public ResponseResult fun4(@PathVariable("id") Long id) throws IOException, ClassNotFoundException {

        redisUtil.set("aaa",1);
        System.out.println(redisUtil.get("aaa"));

        redisUtil.set("bbb:aaa",new String("差不多得了"),50);

        redisUtil.get("bbb:aaa");
        redisUtil.expire("aaa",5);
        redisUtil.expire("article:detail:11",99);
        System.out.println(redisUtil.getExpire("aaa"));
        LRUCache lruCache = LRUCache.getInstance();
        lruCache.remove(ARTICLE_DETALE_KEY+id);
        return ResponseResult.okResult(lruCache);
    }
}
