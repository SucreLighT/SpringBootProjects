package cn.sucrelt.springboothymeleaf.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: sucre
 * @date: 2020/12/17
 * @time: 11:11
 */

@Setter
@Getter
public class User {
    private String username;
    private String password;
    List<String> hobbies;
    Map<String, String> secrets;
}
