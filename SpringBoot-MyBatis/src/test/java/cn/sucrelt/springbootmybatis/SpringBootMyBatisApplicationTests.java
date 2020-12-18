package cn.sucrelt.springbootmybatis;

import cn.sucrelt.springbootmybatis.dao.UserMapper;
import cn.sucrelt.springbootmybatis.domain.UserDO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.omg.PortableInterceptor.INACTIVE;
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
        UserDO user = new UserDO();
        user.setUsername(UUID.randomUUID().toString());
        user.setPassword("nicai");
        user.setCreateTime(new Date());
        userMapper.insert(user);
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
}
