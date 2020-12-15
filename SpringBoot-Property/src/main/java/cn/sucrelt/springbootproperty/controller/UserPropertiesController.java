package cn.sucrelt.springbootproperty.controller;

import cn.sucrelt.springbootproperty.property.UserProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: sucre
 * @date: 2020/12/15
 * @time: 15:30
 */

@RestController
@RequestMapping()
public class UserPropertiesController {

    @Autowired
    private final UserProperties userProperties;

    public UserPropertiesController(UserProperties userProperties) {
        this.userProperties = userProperties;
    }

    @RequestMapping("property")
    public String ReadProperties() {
        return userProperties.toString();
    }
}
