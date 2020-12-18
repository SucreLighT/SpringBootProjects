# SpringBoot+MyBatis

SpringBoot整合Mybatis可以使用xml方式，也可以使用全注解的方式，在SQL语句比较简单时，使用全注解方式比较方便，SQL语句复杂时则不适合写在代码中，使用xml配置比较清晰简洁。

## 一 准备工作

### 1.1 建表

```sql
CREATE TABLE `user` (
  `id` int(13) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(33) DEFAULT NULL COMMENT '姓名',
  `age` int(3) DEFAULT NULL COMMENT '年龄',
  `money` double DEFAULT NULL COMMENT '账户余额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8
```

### 1.2 配置相关依赖

主要依赖包括`mybatis-spring-boot-starter`和`mysql-connector-java`。

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>1.3.2</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### 1.3 配置 application.properties

由于我使用的是比较新的Mysql连接驱动，所以配置文件可能和之前有一点不同。

```properties
#数据库配置
spring.datasource.url=jdbc:mysql://127.0.0\
  .1:3306/springboot?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

#使用xml方式加载Mybatis映射文件的相关配置
mybatis.config-location=classpath:mybatis-config.xml	//配置Mybatis映射文件的路径
mybatis.mapper-locations=classpath:mapper/*Mapper.xml	//配置Mapper文件的路径
mybatis.type-aliases-package=com.sucrelt.项目名.domain	  //配置实体类的目录，默认扫描domain下所有实体类
```

### 1.4 创建用户类 Bean

User.java

```java
public class User {
    private int id;
    private String name;
    private int age;
    private double money;
    ...
    省略getter、setter以及toString方法
}
```



## 二 全注解的方式

### 2.1 Dao 层

项目目录下建立dao包，新建dao层接口UserDao.java

```java
@Mapper
@Repository
public interface UserDao {
    /**
     * 通过名字查询用户信息
     */
    @Select("select * from user where name = #{name}")
    User findUserByName(@Param("name") String name);

    /**
     * 查询所有用户
     */
    @Select("select * from user")
    List<User> findAllUser();

    /**
     * 插入用户信息
     */
    @Insert("insert into user(name, age, money) values (#{name}, #{age}, #{money})")
    void insertUser(@Param("name") String name, @Param("age") Integer age, @Param("money") Double money);

    /**
     * 根据id更新用户信息
     */
    @Update("UPDATE  user SET name = #{name},age = #{age},money= #{money} WHERE id = #{id}")
    void updateUser(@Param("name") String name, @Param("age") Integer age, @Param("money") Double money,
                    @Param("id") int id);

    /**
     * 根据id删除用户信息
     */
    @Delete("DELETE from user WHERE id = #{id}")
    void deleteUser(@Param("id") int id);
}
```

:star:**注解说明**：

1. 在**dao**层的类需要加上 **@Mapper**的注解，这个注解是**mybatis**提供的，标识这个类是一个数据访问层的bean，并交给spring容器管理，并且可以省去之前的xml映射文件。在**编译**的时候，添加了这个类也会相应的生成这个类的实现类daoImpl。
2. 使用IDEA时，在service中注入bean的时候，会显示报错，但是不影响运行，这是因为 **@mapper**不是spring提供的，当需要自动注入这个bean的时候idea不能 **预检测**到这个bean是否可以注入到容器中。如果要消除这个报错，可以在dao层的类上面加上一个 **@Repository**，这个注解是spring提供的，这样就可以预检测到mapper的bean是可以注册到spring容器里面的。
3. 如果接口有**两个及其以上的参数**时，就需要用 **@Param**这个注解了，不然在对应的xml文件，它分辨不出来这个参数是哪一个就会报错，用这个注解的意思就是说**标识这个参数的名称，以便让接受参数的一方更好的找到并利用这个值。**

### 2.2 Service 层

```java
@Service
public class UserService {
    @Autowired
    private UserDao userDao;//上面注意中说了这里可能会报错哦！

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
     * 模拟事务。如果转账中途出了意外，更新操作不会生效。
     */
    @Transactional
    public void changemoney() {
        userDao.updateUser("zhangsan", 22, 2000.0, 7);
        // 模拟转账过程中可能遇到的意外状况
        // int temp = 1 / 0;
        userDao.updateUser("lisi", 19, 4000.0, 8);
    }
}
```

### 2.3 Controller 层

```java
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/query")
    public User testQuery() {
        return userService.selectUserByName("lisi");
    }

    @RequestMapping("/insert")
    public List<User> testInsert() {
        userService.insertService();
        return userService.selectAllUser();
    }

    @RequestMapping("/changemoney")
    public List<User> testchangemoney() {
        userService.changemoney();
        return userService.selectAllUser();
    }

    @RequestMapping("/delete")
    public String testDelete() {
        userService.deleteService(3);
        return "OK";
    }
}
```

### 2.4 启动类

```java
@SpringBootApplication
// 此注解表示动态扫描DAO接口所在包，实际上不加下面这条语句也可以找到
@MapperScan("cn.sucrelt.springbootmybatis.dao")
public class SpringBootMyBatisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMyBatisApplication.class, args);
	}
}
```

### 2.5 测试

运行SpringBoot启动类，访问浏览器：`http://localhost:8080/user/insert`查看结果。



## 三 XML方式:star::star::star:

### 1. 推荐使用XML方式

**使用注解的方式，首先SQL不容易排版，其次会导致Mapper接口冗长混乱。**

### 2. UserDO与数据库表

UserDO

```java
@Setter
@Getter
@ToString
public class UserDO {
    private Integer id;
    private String username;
    private String password;
    private Date createTime;
}
```

sql

```sql
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `username` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '账号',
  `password` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '密码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
```

### 3. Dao层接口UserMapper

```java
@Repository
public interface UserMapper {
    int insert(UserDO user);
    int updateById(UserDO user);
    int deleteById(Integer id);
    UserDO selectById(Integer id);
    UserDO selectByUsername(String username);
    List<UserDO> selectByIds(@Param("ids") List<Integer> ids); //注意这里需要使用@Param注解
}
```

+ **@Repository**注解，作用是标记数据访问 Bean 对象。在 MyBatis 的接口，实际**非必须**，只是为了避免在 Service 中，`@Autowired` 注入时报错（但不影响执行）。
+ **@Param**注解：声明变量名。
  + 在方法为单参数时，**非必须**。
  + 在方法为多参数时，用于指定参数列表中的参数和对应sql中的占位符的匹配关系。
  + **:lemon:注意**：当我们传递一个List实例或者数组作为参数对象传给MyBatis时，MyBatis会自动将它包装在一个Map中，List 实例将会以“list”作为键名，而数组实例将会以“array”作为键名。
    +  案例方法：`List<UserDO> selectByIds(List<Integer> ids);` 
    + 这样我们在编写SQL时，设置参数类型`parameterType="java.util.List"`，具体见下面的UserMapper.xml，测试时也传入一个List类型的变量，看似没有任何问题，但实际上再执行时Mybatis会报错说明找不到变量ids。
    + 这是因为如上所说：MyBatis将集合类型的参数包装在一个Map中，此处为List实例，故该Map中的键名为list，而非设定的ids，从而导致MyBatis无法在容器中找到ids参数并注入。
    + **解决方法：**
      + 如上，在方法定义时使用@Param注解，将该变量名声明为ids，这样即使MyBatis内部将List转成了Map，通过注解指定其参数名仍然为ids。**这也是在只有一个参数时仍要使用@Param注解的情况。**
      + 另一种方法是，这里不使用@Param注解，保留参数名为list，在后面UserMapper.xml中，参数注入到collection中，将collection中参数改为list，即`<foreach item="id" collection="list" separator="," open="(" close=")" index="">`。但这种方法不通用，如果是数组集合就要改成array，需要程序员自己去判断，不如第一种方法省事。
      + 还有一种方法就是不要传递list和数组类型，将list手动包装成Map再传参。

### 4. 实体类映射文件UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.sucrelt.springbootmybatis.dao.UserMapper">

    <sql id="FIELDS">
        id, username, password, create_time
    </sql>

    <insert id="insert" parameterType="UserDO" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO users(username, password, create_time)
        VALUES (#{username}, #{password}, #{createTime})
    </insert>

    <update id="updateById" parameterType="UserDO">
        UPDATE users
        <set>
            <if test="username != null">
                username = #{username},
            </if>
            <if test="password != null">
                password = #{password},
            </if>
        </set>
        WHERE id = #{id}
    </update>

    <delete id="deleteById" parameterType="Integer">
        DELETE
        FROM users
        WHERE id = #{id}
    </delete>

    <select id="selectById" parameterType="Integer" resultType="UserDO">
        SELECT
        <include refid="FIELDS"/>
        FROM users
        WHERE id = #{id}
    </select>

    <select id="selectByUsername" parameterType="String" resultType="UserDO">
        SELECT
        <include refid="FIELDS"/>
        FROM users
        WHERE username = #{username}
        LIMIT 1
    </select>

    <select id="selectByIds" resultType="UserDO" parameterType="java.util.List">
        SELECT
        <include refid="FIELDS"/>
        FROM users
        WHERE id IN
        <foreach item="id" collection="ids" separator="," open="(" close=")" index="">
            #{id}
        </foreach>
    </select>
</mapper>
```

+ 对于绝大多数查询，我们是返回统一字段，所以可以使用 `<sql />` 标签，定义 SQL 段。对于性能或者查询字段比较大的查询，按需要的字段查询。
+ 对于数据库的关键字，使用大写。例如说，`SELECT`、`WHERE` 等等。