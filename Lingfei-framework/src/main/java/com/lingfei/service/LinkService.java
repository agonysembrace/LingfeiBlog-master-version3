package com.lingfei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-12-02 12:42:02
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult selectLinkPage(Link link, Integer pageNum, Integer pageSize);
}

