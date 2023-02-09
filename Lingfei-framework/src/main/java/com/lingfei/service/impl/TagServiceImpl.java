package com.lingfei.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.vo.PageVo;
import com.lingfei.dto.TagListDto;
import com.lingfei.mapper.TagMapper;
import com.lingfei.service.TagService;
import com.lingfei.utils.BeanCopyUtils;
import com.lingfei.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lingfei.domain.entity.Tag;
import org.springframework.util.StringUtils;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-12-02 12:42:02
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {


    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询
        LambdaQueryWrapper<Tag> queryWrapper= new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());

        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);

        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(TagListDto tagListDto) {
        save(BeanCopyUtils.copyBean(tagListDto,Tag.class));
        return ResponseResult.okResult();
    }
}
