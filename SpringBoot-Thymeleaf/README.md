# Thymeleaf

## 1. 快速入门

### 1.1 配置

Spring Boot针对 Thymeleaf 提供了一整套的自动化配置方案，这一套配置类的属性在`org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties` 中，部分源码如下：

```java
@ConfigurationProperties(prefix = "spring.thymeleaf")
public class ThymeleafProperties {
        private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8; //默认编码方式
        public static final String DEFAULT_PREFIX = "classpath:/templates/";	//默认视图解析器的前缀
        public static final String DEFAULT_SUFFIX = ".html";					//默认视图解析器的后缀
        private boolean checkTemplate = true;									
        private boolean checkTemplateLocation = true;
        private String prefix = DEFAULT_PREFIX;
        private String suffix = DEFAULT_SUFFIX;
        private String mode = "HTML";											//默认视图解析的模版模式为HTML，推荐使用
        private Charset encoding = DEFAULT_ENCODING;
        private boolean cache = true;											//默认使用cache，可以使用false关闭
        //...
}
```

1. 首先通过 `@ConfigurationProperties` 注解，将读取 `application.properties`中前缀为 `spring.thymeleaf` 的配置。
2. `Thymeleaf` 模板的默认位置在 `resources/templates` 目录下，默认的后缀是 `html` 。
3. 这些配置，如果开发者不提供，则使用默认的，如果提供，则在 `application.properties` 中以 `spring.thymeleaf` 开始相关的配置。

### 1.2 简单案例

+ 创建 Controller

```java
@Controller
public class IndexController {
    @GetMapping("/index")
    public String index(Model model) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User u = new User();
            u.setId((long) i);
            u.setName("javaboy:" + i);
            u.setAddress("深圳:" + i);
            users.add(u);
        }
        model.addAttribute("users", users);
        return "index";
    }
}

public class User {
    private Long id;
    private String name;
    private String address;
    //省略 getter/setter
}
```

在 `IndexController` 中返回逻辑视图名+数据，逻辑视图名为 `index` ，所以需要在 `resources/templates` 目录下提供一个名为 `index.html` 的 `Thymeleaf` 模板文件。

+ 创建 Thymeleaf模板index.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<table border="1">
    <tr>
        <td>编号</td>
        <td>用户名</td>
        <td>地址</td>
    </tr>
    <tr th:each="user : ${users}">
        <td th:text="${user.id}"></td>
        <td th:text="${user.name}"></td>
        <td th:text="${user.address}"></td>
    </tr>
</table>
</body>
</html>
```

上述案例表示通过 `th:each` 指令来遍历一个集合，数据的展示通过 `th:text` 指令来实现，配置完成后，就可以启动项目了，访问 /index 接口，就能看到集合中的数据了。



## 2. 语法

### 2.1 标准表达式语法

#### 2.1.1 简单表达式

**变量表达式`${...}`**

+ 直接使用 `th:xx = "${}"` 获取model返回的对象属性。

**选择表达式`*{...}`**

+ 与`${...}`用法相同，但是可以与`th:object`标签结合使用，`th:object`首先获取对象，然后使用选择表达式直接获取其中的属性即可。
+ 适用于需要获取某一对象大量属性的情况，可以提高效率，简写风格极为清爽。

```html
<table border="1" th:object="${user}">
<tr>
    <td>用户名</td>
    <td th:text="*{username}"></td>
</tr>
<tr>
    <td>地址</td>
    <td th:text="*{address}"></td>
</tr>
</table>
```

**消息表达式`#{...}`**

+ 用于获取国际化语言翻译值。

+ 例如在有两个配置文件：messages.properties 和 messages_zh_CN.properties时，其内容分别为中英文，在使用消息表达式引用 message时，系统会根据浏览器的语言环境显示不同的语言值：

```html
<div th:text="#{message}"></div>
```

**链接表达式`@{...}`**

+ 用于引用URL，配合`th:src`标签使用。

+ 绝对 URL：

```html
<script type="text/javascript" th:src="@{http://localhost:8080/hello.js}"></script>
```

等价于：

```html
<script type="text/javascript" src="http://localhost:8080/hello.js"></script>
```

+ 上下文相关的 URL：

如在 application.properties 中配置 Spring Boot 的上下文：

```properties 
server.servlet.context-path=/myapp
```

引用路径：

```html
<script type="text/javascript" th:src="@{/hello.js}"></script>
```

等价于：

```html
<script type="text/javascript" src="/myapp/hello.js"></script>
```

+ 相对 URL：

这个相对是指相对于服务器的 URL，例如如下引用：

```html
<script type="text/javascript" th:src="@{~/hello.js}"></script>
```

等价于：

```html
<script type="text/javascript" src="/hello.js"></script>
```

应用程序的上下文 **/myapp** 将被忽略，这表示允许在同一服务器中的其他上下文调用该URL。

+ 协议相对 URL：

```html
<script type="text/javascript" th:src="@{//localhost:8080/hello.js}"></script>
```

等价于：

```html
<script type="text/javascript" src="//localhost:8080/hello.js"></script>
```

**片段表达式`~{...}`**

与`th:fragment`、`th:replace`标签搭配使用，片段表达式拥有三种语法：

+ `~{ viewName }`：表示引入完整页面。
+ `~{ viewName ::selector}`：表示在指定页面寻找片段，其中 selector 可为片段名、jquery选择器等。
+ `~{ ::selector}`：表示在当前页寻找。

在 resources/templates 目录下新建 my_fragment.html 文件，内容如下：

```html
<div th:fragment="javaboy_link"><a href="http://www.javaboy.org">www.javaboy</a></div>
<div th:fragment="itboyhub_link"><a href="http://www.itboyhub.com">www.itboyhub.com</a></div>
```

这里有两个 div，通过 `th:fragment` 来定义片段，两个 div 分别具有不同的名字。

然后在**另外一个页面**中引用该片段：

```html
<table border="1" th:object="${user}" th:fragment="aaa">
<tr>
    <td>用户名</td>
    <td th:text="*{username}"></td>
</tr>
<tr>
    <td>地址</td>
    <td th:text="*{address}"></td>
</tr>
</table>
<hr>
<div th:replace="my_fragment.html"></div> //引入完整界面
<hr>
<div th:replace="~{my_fragment.html::javaboy_link}"></div> //引入名指定页面中为javaboy_link的片段
<hr>
<div th:replace="~{::aaa}"></div> //引入当前页面名为aaa的片段
```

#### 2.1.2 字面量

Thymeleaf中可以直接写在表达式中的字符，主要有如下几种：

+ 文本字面量：'one text', 'Another one!',…
+ 数字字面量：0, 34, 3.0, 12.3,…
+ 布尔字面量：true, false
+ Null字面量：null
+ 字面量标记：one, sometext, main,…

#### 2.1.3 文本运算

文本可以类似于String使用 `+` 进行拼接。

如果字符串中包含变量，也可以使用另一种简单的方式，叫做**字面量置换**，用 `|`代替 `'...' + '...'`，如下：

```html
<div th:text="|hello ${user.username}|"></div>
等价于：
<div th:text="'hello '+${user.username}+' '+|Go ${user.address}|"></div>
```

#### 2.1.4 算术运算与布尔运算

+ 算术运算有：`+`, `-`, `*`, `/` 和 `%`。

+ 二元运算符：and, or
+ 布尔非（一元运算符）：!, not

#### 2.1.5 比较和相等

表达式里的值可以使用 `>`, `<`, `>=` 和 `<=` 符号比较。`==` 和 `!=` 运算符用于检查相等（或者不相等）。

**注意** `XML`规定 `<` 和 `>` 标签不能用于属性值，所以应当把它们转义为 `<` 和 `>`，如果不想转义，也可以使用别名：gt (>)；lt (<)；ge (>=)；le (<=)；not (!)。还有 eq (==), neq/ne (!=)。

#### 2.1.6 条件运算符

类似于 Java 中的三目运算符。

```html
<div th:with="age=(99*99/99+99-1)">
    <div th:text="(${age} ne 197)?'yes':'no'"></div>
</div>
```

#### 2.1.7 内置对象

+ \#request：（仅在 Web 上下文中）HttpServletRequest 对象。
+ \#response：（仅在 Web 上下文中）HttpServletResponse 对象。
+ \#session：（仅在 Web 上下文中）HttpSession 对象。
+ \#servletContext：（仅在 Web 上下文中）ServletContext 对象。
+ \#dates：java.util.Date对象的方法：格式化，组件提取等。
+ \#calendars：类似于#dates但是java.util.Calendar对象。
+ \#numbers：用于格式化数字对象的方法。
+ \#strings：String对象的方法：contains，startsWith，prepending / appending等。
+ \#arrays：数组方法。
+ \#lists：列表的方法。
+ \#sets：集合的方法。
+ \#maps：地图方法。

在页面可以访问到上面这些内置对象

```html
<div th:text='${#session.getAttribute("name")}'></div>
```

### 2.2 设置属性值

给 HTML 元素设置属性值。可以一次设置多个，多个之间用 `,` 分隔开。

例如：

```html
<img th:attr="src=@{/1.png},title=${user.username},alt=${user.username}">
```

会被渲染成：

```html
<img src="/myapp/1.png" title="javaboy" alt="javaboy">
```

**支持在每一个原生的 HTML 属性前加上 `th:` 前缀的方式来使用动态值**，像下面这样：

```html
<img th:src="@{/1.png}" th:alt="${user.username}" th:title="${user.username}">
```

渲染效果和前面一致。

### 3.3 遍历

Thymeleaf 中通过 `th:each`标签实现迭代器的迭代遍历。

```html
<table th:each="u,state : ${users}">
    <tr>
        <td th:text="${u}"></td>
        <td th:text="${u.username}"></td>
        <td th:text="${u.address}"></td>
        <td th:text="${state.index}"></td>
        <td th:text="${state.count}"></td>
        <td th:text="${state.size}"></td>
    </tr>
</table>
```

users 是要遍历的集合/数组，第一个参数u是集合中的单个元素，第二个参数表示迭代器的状态，提供了状态变量的相关方法可以获得一些属性：

+ index：当前对象在迭代器中的索引；
+ count：同index，从1计数；
+ size：迭代器中对象总数；
+ current：每次遍历的遍历变量。
+ even/odd：当前对象的索引index是否为奇数/偶数；
+ first/last：当前对象是迭代器中第一个/最后一个

### 3.4 分支语句

使用 `th:if`进行条件判断，如：

```html
<table border="1">
    <tr th:each="u,state : ${users}" th:if="${state.odd}">
        <td th:text="${u.username}"></td>
        <td th:text="${u.address}"></td>
        <td th:text="${state.index}"></td>
        <td th:text="${state.count}"></td>
        <td th:text="${state.size}"></td>
        <td th:text="${state.current}"></td>
        <td th:text="${state.even}"></td>
        <td th:text="${state.odd}"></td>
        <td th:text="${state.first}"></td>
        <td th:text="${state.last}"></td>
    </tr>
</table>
```

如下值情况都会判为 true：

+ 布尔值，并且为 true。
+ 数字，且不为 0。
+ 字符，且不为 0。
+ 字符串，且不为 “false”， “off” 或者 “no”。
+ 如果值不是布尔值，数字，字符或者字符串。
+ 但是如果值为 null，th:if 会求值为 false。

`th:unless`标签也可用作条件判断，与`th:if`判断相反。

当可能性比较多的时候，也可以使用`th:switch`，`th:case="*"` 则表示默认选项。

```html
<table border="1">
    <tr th:each="u,state : ${users}">
        <td th:text="${u.username}"></td>
        <td th:text="${u.address}"></td>
        <td th:text="${state.index}"></td>
        <td th:text="${state.count}"></td>
        <td th:text="${state.size}"></td>
        <td th:text="${state.current}"></td>
        <td th:text="${state.even}"></td>
        <td th:text="${state.odd}"></td>
        <td th:text="${state.first}"></td>
        <td th:text="${state.last}"></td>
        <td th:switch="${state.odd}">
            <span th:case="true">odd</span>
            <span th:case="*">even</span>
        </td>
    </tr>
</table>
```

### 3.5 本地变量

这个我们前面已经涉及到了，使用 `th:with` 可以定义一个本地变量。

### 3.6 内联

我们可以使用属性将数据放入页面模版中，但是很多时候，内联的方式看起来更加直观一些，像下面这样：

```html
<div>hello [[${user.username}]]</div>
```

用内联的方式去做拼接也显得更加自然。

+ `[[...]]` 对应于 `th:text` ,结果会是转义的 HTML，即保留标签不执行结果.

+ `[(...)]`对应于 `th:utext`，不会执行任何的 HTML 转义，显示为执行标签后的样式。





