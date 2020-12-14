package cn.sucrelt.springbootmybatis.mapper;

import cn.sucrelt.springbootmybatis.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author: sucre
 * @date: 2020/12/14
 * @time: 16:22
 */

@Mapper
public interface UserMapper {
    public List<User> queryUserList();
}
