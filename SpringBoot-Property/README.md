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

**@Value**方式读取配置文件在Spring中是类型不安全的，在SpringBoot中不被推荐使用，使用**@Component**注解进行类型安全的属性注入，该注解会自动将Spring容器中的对应数据注入到对象对应的属性中。

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



## 3.使用@PropertySource

一般来说，application.properties中存放的是系统变量，对于自定义的配置一般在resources目录下新建配置文件，采用**@PropertySource**注解读取。

### 3.1 配置类

在配置类上增加注解**@PropertySource**，value属性用于设置读取的文件位置。

```java
@Component
@ConfigurationProperties(prefix = "students")
@PropertySource(value = "classpath:students.properties", encoding = "UTF-8")
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

### 3.2 配置文件

在resources目录下新建students.properties文件并编写内容。

**由于@PropertySource注解只支持读取properties类型的配置文件，这里没法使用yml类型文件，想要实现该注解读取yml类型文件，分析源码可知@PropertySource注解是通过继承DefaultPropertySourceFactory类并实现createPropertySource方法来读取配置文件，因此需要自定义继承此类，并重写其createPropertySource方法，完成读取yml文件的功能。**

```properties
students.stu[0].age=20
students.stu[0].city=北京
students.stu[0].money=2000.0
students.stu[0].username=张三
students.stu[1].age=18
students.stu[1].city=上海
students.stu[1].money=1000.0
students.stu[1].username=李四
students.type=IT
```



## 4.配置文件的位置

在创建一个SpringBoot工程时，默认的resources目录下会有一个application.properties文件，但这个文件的位置不是唯一的，在SpringBoot中一个有以下四个地方可以存放application.properties文件。

+ Project
  + .idea
  + .mvn
  + config
    + **application.properties** //1.项目根目录下的config目录下
  + src
    + main
      + java
      + resources
        + config
          + **application.properties** //3.resources目录下的config目录下
        + **application.properties** //4.resources目录下
    + test
  + **application.properties** //2.项目根目录下

以上1234处位置的配置文件优先级依次降低，同一属性在以上四个配置文件中均出现时，以优先级最高的文件中的为准。