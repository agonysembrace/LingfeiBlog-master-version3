package com.lingfei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Category;
import com.lingfei.domain.vo.CategoryVo;
import com.lingfei.domain.vo.PageVo;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-12-02 12:42:02
 */
public interface CategoryService extends IService<Category> {
    ResponseResult getCategoryList();

    PageVo selectCategoryPage(Category category, Integer pageNum, Integer pageSize);

    List<CategoryVo> listAllCategory();
}

