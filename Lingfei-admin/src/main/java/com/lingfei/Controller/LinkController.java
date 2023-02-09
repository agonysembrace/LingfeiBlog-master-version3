package com.lingfei.Controller;

import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Link;
import com.lingfei.domain.vo.LinkVo;
import com.lingfei.domain.vo.PageVo;
import com.lingfei.dto.TagListDto;
import com.lingfei.service.LinkService;
import com.lingfei.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    LinkService linkService;

    @GetMapping("/list")
    public ResponseResult list(Link link, Integer pageNum, Integer pageSize) {
        return linkService.selectLinkPage(link,pageNum,pageSize);
    }

    @PostMapping
    public ResponseResult add(@RequestBody Link link) {
        linkService.save(link);
        return ResponseResult.okResult();
    }

    @GetMapping("{id}")
    public ResponseResult info(@PathVariable Integer id) {
        Link link = linkService.getById(id);
        return ResponseResult.okResult(link);
    }

    @PutMapping
    public ResponseResult update(@RequestBody  Link link) {
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    @DeleteMapping("{id}")
    public ResponseResult delete(@PathVariable Integer id) {
        linkService.removeById(id);
        return ResponseResult.okResult();
    }
}
