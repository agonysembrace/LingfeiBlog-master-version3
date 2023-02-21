package com.lingfei.Controller;


import com.lingfei.config.FastJsonRedisSerializer;
import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Article;
import com.lingfei.domain.vo.ArticleDetailVo;
import com.lingfei.service.ArticleService;
import com.lingfei.utils.BeanCopyUtils;
import com.lingfei.utils.CompressRedis;
import com.lingfei.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.nio.charset.StandardCharsets;

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
    public String fun2(){
        Article article = articleService.getById(10);

        return "访问成功";

    }
    @GetMapping("/jemter3")
    public ResponseResult fun3() throws IOException, ClassNotFoundException {
        String key ="test:Jedis";
        int id = 12;
        Article article =articleService.getById(id);
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        byte[] bytes = new CompressRedis().serialize(articleDetailVo);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(bytes);

        //写入 Redis
        Jedis jedis = new Jedis("43.143.94.164",6379);
        jedis.auth("990313wlf");
//        jedis.set(key.getBytes(), baos.toByteArray());
        jedis.set(key.getBytes(), baos.toByteArray());
        //关闭流
        oos.close();

        // 读取 Byte格式 存入的数据
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jedis.get(key.getBytes()));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        byte[] o = (byte[]) objectInputStream.readObject();
        //解压数据
        ArticleDetailVo vo = (ArticleDetailVo) new CompressRedis().deserialize(o);
        return ResponseResult.okResult(vo);
    }
}
