package cn.sucrelt.springbootmybatis.controller;

import cn.sucrelt.springbootmybatis.domain.UserDO;
import cn.sucrelt.springbootmybatis.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 * @author: sucre
 * @date: 2020/12/23
 * @time: 16:04
 */

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public PageInfo<UserDO> testPageHelper1() {
        PageInfo<UserDO> queryResult = userService.findAllByPage(1, 5);
        return queryResult;
    }
}
