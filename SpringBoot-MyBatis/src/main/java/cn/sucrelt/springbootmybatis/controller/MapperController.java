package cn.sucrelt.springbootmybatis.controller;

import cn.sucrelt.springbootmybatis.domain.User;
import cn.sucrelt.springbootmybatis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @description:
 * @author: sucre
 * @date: 2020/12/14
 * @time: 16:25
 */

@Controller
public class MapperController {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/queryUser")
    @ResponseBody
    public List<User> queryUser() {
        List<User> users = userMapper.queryUserList();
        return users;
    }
}
