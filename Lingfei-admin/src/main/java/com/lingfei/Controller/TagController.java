package com.lingfei.Controller;

import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Category;
import com.lingfei.domain.entity.Tag;
import com.lingfei.domain.vo.PageVo;
import com.lingfei.dto.EditTagDto;
import com.lingfei.dto.TagListDto;
import com.lingfei.service.TagService;
import com.lingfei.utils.BeanCopyUtils;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/7 10:57
 * @Decription:
 */

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    @GetMapping("/listAllTag")
    public ResponseResult<PageVo> listAllTag(){
        List<Tag> list = tagService.list();
        return ResponseResult.okResult(list);
    }

    //添加标签,用Dto类接收，只有name和remark字段
    @PostMapping
    public ResponseResult addTag(@RequestBody TagListDto tagListDto){
        return tagService.addTag(tagListDto);
    }

    @DeleteMapping("{id}")
    public ResponseResult delTag(@PathVariable Long id){
        tagService.removeById(id);
        return ResponseResult.okResult();
    }

    @PutMapping
    public ResponseResult update(@RequestBody EditTagDto editTagDto){
        Tag tag = BeanCopyUtils.copyBean(editTagDto, Tag.class);
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }
    @GetMapping(value = "/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        Tag tag = tagService.getById(id);
        return ResponseResult.okResult(tag);
    }

}
