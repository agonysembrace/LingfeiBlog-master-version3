package com.lingfei.Controller;

import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Article;
import com.lingfei.domain.vo.ArticleVo;
import com.lingfei.domain.vo.PageVo;
import com.lingfei.dto.AddArticleDto;
import com.lingfei.dto.ArticleDto;
import com.lingfei.service.ArticleService;
import com.lingfei.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/8 12:23
 * @Decription:
 */
@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }


    @GetMapping("/list")
    public ResponseResult list(Article article, Integer pageNum, Integer pageSize)
    {
        PageVo pageVo = articleService.selectArticlePage(article,pageNum,pageSize);
        return ResponseResult.okResult(pageVo);
    }

    @GetMapping(value = "/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        ArticleVo article = articleService.getInfo(id);
        return ResponseResult.okResult(article);
    }

    @PutMapping
    public ResponseResult edit(@RequestBody ArticleDto article){
        articleService.edit(article);
        return ResponseResult.okResult();
    }
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        articleService.removeById(id);
        return ResponseResult.okResult();
    }
    //上传文件




}