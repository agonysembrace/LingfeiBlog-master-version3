package com.lingfei.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/6 20:12
 * @Decription:
 */

@Component
public class TestRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("程序初始化");
    }
}
