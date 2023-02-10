package com.lingfei;

import com.lingfei.domain.entity.Article;
import com.lingfei.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UpdateTest {
    @Autowired
    ArticleService articleService;

    @Test
    public void main() {
        Article article = new Article(9L, 999L);
        articleService.updateById(article);
    }

}
