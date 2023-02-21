package com.lingfei.Controller;

import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Article;

import com.lingfei.service.ArticleService;
import io.swagger.annotations.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/1 16:49
 * @Decription:
 */

@RestController
//@Scope(name = ConfigurableBeanFactory.SCOPE_PROTOTYPE, description = "")
@RequestMapping("/article")

public class ArticleController {

    @Autowired
    private ArticleService articleService;
//
//    @GetMapping("/list")
//    public List<Article> test(){
//        return articleService.list();
//    }

    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
//        articleService.updateViewCountToDB();
        ResponseResult result = articleService.hotArticle();
        return result;
    }

    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
//        articleService.updateViewCountToDB();
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    @GetMapping("{id}")
    public ResponseResult getArticleDetail(@PathVariable("id")Long id){
//        articleService.updateViewCountToDB();
        return articleService.getArticleDetail(id);
    }

    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }

}
