package cn.sucrelt.springbootmybatis;

import cn.sucrelt.springbootmybatis.dao.UserMapper;
import cn.sucrelt.springbootmybatis.domain.UserDO;
import cn.sucrelt.springbootmybatis.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootMyBatisApplication.class)
class SpringBootMyBatisApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsert() {
        //批量插入数据，用作分页展示的数据
        for (int i = 0; i < 20; i++) {
            UserDO user = new UserDO();
            user.setUsername(UUID.randomUUID().toString());
            user.setPassword("nicai");
            user.setCreateTime(new Date());
            userMapper.insert(user);
        }
    }

    @Test
    public void testUpdateById() {
        UserDO user = new UserDO();
        user.setId(1);
        user.setPassword("bucai");
        userMapper.updateById(user);
    }

    @Test
    public void testDeleteById() {
        userMapper.deleteById(6);
    }

    @Test
    public void testSelectById() {
        System.out.println(userMapper.selectById(3));
    }

    @Test
    public void testSelectByUsername() {
        System.out.println(userMapper.selectByUsername("sucre"));
    }

    @Test
    public void testSelectByIds() {
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(1);
        ids.add(2);

        List<UserDO> users = userMapper.selectByIds(ids);
        for (UserDO user : users) {
            System.out.println("user：" + user.toString());
        }
    }

    @Autowired
    private UserService userService;

    @Test
    public void testTransaction() {
        UserDO user = new UserDO();
        user.setUsername(UUID.randomUUID().toString());
        user.setPassword("nicai");
        user.setCreateTime(new Date());
        userService.insertUser(user);
    }

    @Test
    public void testPage(){
        PageInfo<UserDO> pageInfo = userService.findAllByPage(3, 10);
        System.out.println("page:" + pageInfo);
        for(UserDO user : pageInfo.getList()) {
            System.out.println(user);
        }
    }
}
