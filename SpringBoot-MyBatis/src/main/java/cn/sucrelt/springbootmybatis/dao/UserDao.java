package cn.sucrelt.springbootmybatis.dao;

import cn.sucrelt.springbootmybatis.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: sucre
 * @date: 2020/12/14
 * @time: 19:21
 */

@Mapper
@Repository
public interface UserDao {

    /**
     * 通过名字查询用户信息
     *
     * @param name
     * @return
     */
    // @Select("select * from user where name = #{name}")
    // User findUserByName(@Param("name") String name);

    //使用xml文件配置
    @Mapper
    User findUserByName(String name);


    /**
     * 查询所有用户
     *
     * @return
     */
    @Select("select * from user")
    List<User> findAllUser();


    /**
     * 插入用户信息
     *
     * @param name
     * @param age
     * @param money
     */
    @Insert("insert into user(name, age, money) values (#{name}, #{age}, #{money})")
    void insertUser(@Param("name") String name, @Param("age") Integer age, @Param("money") Double money);


    /**
     * 根据id更新用户信息
     *
     * @param name
     * @param age
     * @param money
     * @param id
     */
    @Update("UPDATE  user SET name = #{name},age = #{age},money= #{money} WHERE id = #{id}")
    void updateUser(@Param("name") String name, @Param("age") Integer age, @Param("money") Double money,
                    @Param("id") int id);

    /**
     * 根据id删除用户信息
     *
     * @param id
     */
    @Delete("DELETE from user WHERE id = #{id}")
    void deleteUser(@Param("id") int id);
}
