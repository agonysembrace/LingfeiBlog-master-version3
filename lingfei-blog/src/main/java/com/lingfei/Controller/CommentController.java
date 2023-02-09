package com.lingfei.Controller;

import com.lingfei.constants.SystemConstants;
import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Comment;
import com.lingfei.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/4 18:03
 * @Decription:
 */

@RestController
@RequestMapping("/comment")
@Api(tags="评论",description = "评论相关接口")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId, pageNum, pageSize);
    }


    @PostMapping
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }

    @GetMapping("/linkCommentList")
    @ApiOperation(value = "友链评论列表",notes = "获取一列友链评论")
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
}
