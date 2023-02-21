package com.lingfei;

import com.lingfei.config.FastJsonRedisSerializer;
import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Article;
import com.lingfei.domain.entity.Category;
import com.lingfei.domain.vo.ArticleDetailVo;
import com.lingfei.service.ArticleService;
import com.lingfei.service.CategoryService;
import com.lingfei.utils.BeanCopyUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
@SpringBootTest
public class RedisTest {

    @Autowired
    ArticleService articleService;

    @Test
    public  void main() {
//        Article article = null;
//        ArticleDetailVo articleDetailVo = null;
//        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer<>(ArticleDetailVo.class);
//
//            byte[] bytes = serializer.serialize(articleDetailVo);
//            redisUtil.set("article:detail:"+id, new String(bytes));
//
//        }
//        String s  = (String) redisUtil.get("article:detail:" + id);
//        articleDetailVo = (ArticleDetailVo) serializer.deserialize(s.getBytes());
//        return ResponseResult.okResult(articleDetailVo);
    }
}
