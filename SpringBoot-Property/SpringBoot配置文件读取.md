# Spring Boot配置文件读取

自定义的一些配置需要卸载配置文件`application.yml` 中。

`application.yml`文件内容如下：

```yml
username: zhangsan
students:
  type: IT
  stu:
    - username: 张三
      age: 20
      city: 北京
      money: 2000.00
    - username: 李四
      age: 18
      city: 上海
      money: 1000.00
```

## 1.通过 `@value` 读取

在需要使用配置时，使用 `@Value("${property}")` 读取比较简单的配置信息：

```
@Value("${username}")
String username;
```

**此方式在SpringBoot中是不被推荐的。**

## 2.通过`@ConfigurationProperties`读取并与 bean 绑定

### 2.1 创建一个配置类

创建用户配置类UserProperties，注解**@Component**表示该类也会注册到Spring容器中，在后面使用时就可以自动注入。

```java
@Component
@ConfigurationProperties(prefix = "students")
@Setter
@Getter
@ToString
public class UserProperties {
    private String type;
    private List<Stu> stu;

    @Setter
    @Getter
    @ToString
    static class Stu {
        private String username;
        private int age;
        private String city;
        private double money;
    }
}
```

### 2.2 调用配置类执行相应业务

```java
@RestController
@RequestMapping()
public class UserPropertiesController {

    @Autowired
    private final UserProperties userProperties;

    public UserPropertiesController(UserProperties userProperties) {
        this.userProperties = userProperties;
    }

    @RequestMapping("property")
    public String ReadProperties() {
        return userProperties.toString();
    }
}
```

