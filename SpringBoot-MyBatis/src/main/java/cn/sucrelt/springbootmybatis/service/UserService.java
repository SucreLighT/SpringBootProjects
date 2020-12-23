package cn.sucrelt.springbootmybatis.service;

import cn.sucrelt.springbootmybatis.domain.UserDO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: sucre
 * @date: 2020/12/22
 * @time: 14:52
 */

public interface UserService {
    public int insertUser(UserDO userDO);

    public PageInfo<UserDO> findAllByPage(int pageNum, int pageSize);
}
