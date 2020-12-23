package cn.sucrelt.springbootmybatis.dao;

import cn.sucrelt.springbootmybatis.domain.UserDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @description:
 * @author: sucre
 * @date: 2020/12/14
 * @time: 19:21
 */

@Repository
public interface UserMapper {
    int insert(UserDO user);

    int updateById(UserDO user);

    int deleteById(Integer id);

    UserDO selectById(Integer id);

    UserDO selectByUsername(String username);

    List<UserDO> selectByIds(@Param("ids") List<Integer> ids);

    //查询所有记录，用于测试分页查询
    List<UserDO> findAllByPage();
}
