package cn.sucrelt.springbootmybatis.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @description:
 * @author: sucre
 * @date: 2020/12/14
 * @time: 16:20
 */

@Setter
@Getter
@ToString
public class User {
    private int id;
    private String name;
    private int age;
    private double money;
}
