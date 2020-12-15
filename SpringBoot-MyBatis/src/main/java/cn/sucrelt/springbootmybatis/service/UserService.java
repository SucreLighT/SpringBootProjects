package cn.sucrelt.springbootmybatis.service;

import cn.sucrelt.springbootmybatis.dao.UserDao;
import cn.sucrelt.springbootmybatis.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:
 * @author: sucre
 * @date: 2020/12/14
 * @time: 19:22
 */

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 根据名字查找用户
     */
    public User selectUserByName(String name) {
        return userDao.findUserByName(name);
    }

    /**
     * 查找所有用户
     */
    public List<User> selectAllUser() {
        return userDao.findAllUser();
    }

    /**
     * 插入两个用户
     */
    public void insertService() {
        userDao.insertUser("zhangsan", 22, 3000.0);
        userDao.insertUser("lisi", 19, 3000.0);
    }

    /**
     * 根据id 删除用户
     */

    public void deleteService(int id) {
        userDao.deleteUser(id);
    }

    /**
     * 模拟事务。
     */
    @Transactional
    public void changemoney() {
        userDao.updateUser("zhangsan", 22, 2000.0, 7);
        // 模拟转账过程中可能遇到的意外状况
        // int temp = 1 / 0;
        userDao.updateUser("lisi", 19, 4000.0, 8);
    }
}
