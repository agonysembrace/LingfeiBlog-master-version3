package com.lingfei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Tag;
import com.lingfei.domain.vo.PageVo;
import com.lingfei.dto.TagListDto;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2022-12-02 12:42:02
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(TagListDto tagListDto);
}

