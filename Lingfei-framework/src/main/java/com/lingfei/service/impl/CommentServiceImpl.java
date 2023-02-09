package com.lingfei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingfei.constants.SystemConstants;
import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.vo.CommentVo;
import com.lingfei.domain.vo.PageVo;
import com.lingfei.enums.AppHttpCodeEnum;
import com.lingfei.exception.SystemException;
import com.lingfei.mapper.CommentMapper;
import com.lingfei.service.CommentService;
import com.lingfei.service.UserService;
import com.lingfei.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lingfei.domain.entity.Comment;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-12-02 12:42:02
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    UserService userService;
    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //对articleId进行判断
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment ::getArticleId,articleId );
        //根评论rootId为-1
        queryWrapper.eq(Comment ::getRootId,-1);
        //评论类型
        queryWrapper.eq(Comment::getType,commentType);
        //分页查询
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page,queryWrapper);
        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());
        //查询所有子评论
        for (CommentVo commentVo : commentVoList) {
            //查询
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
//        comment.setCreateBy(SecurityUtils.getUserId());
        //评论内容不能为空
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    private List<CommentVo> getChildren(Long id) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment :: getRootId,id );
        queryWrapper.orderByAsc(Comment ::getCreateTime);
        List<Comment> comments = list(queryWrapper);
        List<CommentVo> children = toCommentVoList(comments);
        return children;
    }

    private List<CommentVo>toCommentVoList(List<Comment>list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历Vo集合
        for(CommentVo commentVo : commentVos){
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            String avatar = userService.getById(commentVo.getCreateBy()).getAvatar();
            commentVo.setUserName(nickName);
            commentVo.setAvatar(avatar);
            if(commentVo.getToCommentUserId()!=-1){
                String name = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(name);
            }
        }
        return commentVos;
    }
}
