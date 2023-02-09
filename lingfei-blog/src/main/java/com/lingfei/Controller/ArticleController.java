package com.lingfei.Controller;

import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Article;

import com.lingfei.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/1 16:49
 * @Decription:
 */

@RestController
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
        ResponseResult result = articleService.hotArticle();
        return result;
    }

    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    @GetMapping("{id}")
    public ResponseResult getArticleDetail(@PathVariable("id")Long id){
        return articleService.getArticleDetail(id);
    }

    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }

}
