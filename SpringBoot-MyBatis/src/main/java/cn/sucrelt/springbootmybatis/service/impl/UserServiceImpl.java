package cn.sucrelt.springbootmybatis.service.impl;

import cn.sucrelt.springbootmybatis.dao.UserMapper;
import cn.sucrelt.springbootmybatis.domain.UserDO;
import cn.sucrelt.springbootmybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: sucre
 * @date: 2020/12/22
 * @time: 14:54
 */

@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int insertUser(UserDO userDO) {
        int result = userMapper.insert(userDO);

        // int i = 1 / 0;

        return result;
    }
}
