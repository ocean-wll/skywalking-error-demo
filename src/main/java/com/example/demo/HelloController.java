package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author ocean_wll
 * @Date 2021/8/4 2:27 下午
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    @ResponseBody
    public String test() {
        try {
            DemoApplication.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "test";
    }
}
