package cn.sucrelt.springbootmybatis.service;

import cn.sucrelt.springbootmybatis.domain.UserDO;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: sucre
 * @date: 2020/12/22
 * @time: 14:52
 */

public interface UserService {
    public int insertUser(UserDO userDO);
}
