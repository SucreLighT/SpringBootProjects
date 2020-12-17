package cn.sucrelt.springboothymeleaf.controller;

import cn.sucrelt.springboothymeleaf.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: sucre
 * @date: 2020/12/17
 * @time: 11:12
 */

@Controller
public class UserController {

    @GetMapping("/user")
    public String TestUser(Model model) {
        User user = new User();
        user.setUsername("张三");
        user.setPassword("123456");

        List<String> hobbies = new ArrayList();
        hobbies.add("singing");
        hobbies.add("dancing");
        hobbies.add("gaming");
        user.setHobbies(hobbies);

        Map<String, String> secrets = new HashMap<>();
        secrets.put("1", "a");
        secrets.put("2", "b");
        secrets.put("3", "c");
        secrets.put("4", "d");
        user.setSecrets(secrets);

        model.addAttribute("user", user);

        return "user";
    }
}
