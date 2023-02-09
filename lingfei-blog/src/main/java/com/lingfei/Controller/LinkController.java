package com.lingfei.Controller;

import com.lingfei.domain.ResponseResult;
import com.lingfei.service.LinkService;
import com.lingfei.service.impl.LinkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/2 20:43
 * @Decription:
 */

@RestController
@RequestMapping("/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @GetMapping("/getAllLink")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }

}
