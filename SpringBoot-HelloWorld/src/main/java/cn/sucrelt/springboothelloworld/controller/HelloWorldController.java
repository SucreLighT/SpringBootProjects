package cn.sucrelt.springboothelloworld.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: sucre
 * @date: 2020/12/13
 * @time: 17:24
 */

@RestController
@RequestMapping("test")
@ConfigurationProperties(prefix = "helloworld")
public class HelloWorldController {

    // @Value("${helloworld.username}")
    private String username;
    // @Value("${helloworld.info}")
    private String info;

    @RequestMapping("hello")
    public String sayHello() {
        return username + "：" + info;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
