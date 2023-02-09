package com.lingfei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingfei.constants.SystemConstants;
import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.vo.LinkVo;
import com.lingfei.domain.vo.PageVo;
import com.lingfei.mapper.LinkMapper;
import com.lingfei.service.LinkService;
import com.lingfei.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import com.lingfei.domain.entity.Link;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-12-02 12:42:02
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper();
        //只能显示审核被通过的友链
        queryWrapper.eq(Link ::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        //获取符合wrapper查询条件的所有link组成List
        List<Link> links  = list(queryWrapper);
        //转换为Vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult selectLinkPage(Link link, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(link.getName()),Link::getName,link.getName());
        wrapper.eq(StringUtils.hasText(link.getStatus()),Link::getStatus,link.getStatus());
        Page<Link> linkPage = new Page<Link>();
        linkPage.setCurrent(pageNum);
        linkPage.setSize(pageSize);
        page(linkPage,wrapper);

        List<Link> linkList = linkPage.getRecords();
//        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(linkList, LinkVo.class);
        PageVo pageVo = new PageVo();
        pageVo.setRows(linkList);
        pageVo.setTotal(linkPage.getTotal());

        return ResponseResult.okResult(pageVo);

    }
}
