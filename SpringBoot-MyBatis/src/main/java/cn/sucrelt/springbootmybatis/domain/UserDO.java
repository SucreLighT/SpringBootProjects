package cn.sucrelt.springbootmybatis.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @description:
 * @author: sucre
 * @date: 2020/12/14
 * @time: 16:20
 */

@Setter
@Getter
@ToString
public class UserDO {
    private Integer id;
    private String username;
    private String password;
    private Date createTime;
}
