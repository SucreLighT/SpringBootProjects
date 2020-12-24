# SpringBoot+kaptcha实现验证码

## 1. Kaptcha介绍

验证码（CAPTCHA）是“Completely Automated Public Turing test to tell Computers and Humans Apart”（全自动区分计算机和人类的图灵测试）的缩写，是一种区分用户是计算机还是人的公共全自动程序。可以防止：恶意破解密码、刷票、论坛灌水，有效防止某个黑客对某一个特定注册用户用特定程序暴力破解方式进行不断的登陆尝试，实际上用验证码是现在很多网站通行的方式。**谷歌的Kaptcha验证码生成工具是常用的验证码插件。**

验证码的功能逻辑主要包括：

+ 验证码的生成
+ 验证码的显示
+ 验证码的比对



## 2. 使用案例

### 2.1 导入依赖

```xml
 <!--导入kaptcha依赖-->
<dependency>
    <groupId>com.github.penggle</groupId>
    <artifactId>kaptcha</artifactId>
    <version>2.3.2</version>
</dependency>
```

### 2.2 创建配置类

新建验证码配置类并将其注册到IOC容器中，新建 config 包，在其中新建KaptchaConfig类

```java
@Component
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha getDefaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();

        // 设置属性
        // 图片边框
        properties.setProperty("kaptcha.border", "yes");
        // 边框颜色
        properties.setProperty("kaptcha.border.color", "105,179,90");
        // 字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        // 图片宽
        properties.setProperty("kaptcha.image.width", "120");
        // 图片高
        properties.setProperty("kaptcha.image.height", "50");
        // 字体大小
        properties.setProperty("kaptcha.textproducer.font.size", "40");
        // session key
        properties.setProperty("kaptcha.session.key", "code");
        // 验证码长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        // 字体
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}
```

+ 配置类中设置Kaptcha的常用属性，将属性封装到Properties对象中，然后调用DefaultKaptcha类进行设置。

### 2.3 Controller层

在controller包中新建KaptchaControlle，自动注入DefaultKaptcha类，然后进行验证码的生成、显示和校验。

```java
@Controller
public class KaptchaController {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @RequestMapping("/kaptcha")
    public void defaultKaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        byte[] captcha;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            // 生成验证码文字
            String rightCode = defaultKaptcha.createText();
            // 将生成的验证码保存在session中
            request.getSession().setAttribute("rightCode", rightCode);

            BufferedImage bufferedImage = defaultKaptcha.createImage(rightCode);
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);

            System.out.println("rc:" + rightCode);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 设置返回信息
        captcha = byteArrayOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        ServletOutputStream servletOutputStream = response.getOutputStream();
        servletOutputStream.write(captcha);
        servletOutputStream.flush();
        servletOutputStream.close();
    }

    @GetMapping("/verify")
    @ResponseBody
    public String verify(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");

        if (StringUtils.isEmpty(code)) {
            return "验证码不能为空";
        }
        String rightCode = (String) request.getSession().getAttribute("rightCode");
        System.out.println("rightCode:" + rightCode);
        System.out.println("Code:" + code);

        if (StringUtils.isEmpty(rightCode) || !code.equals(rightCode)) {
            return "验证码错误";
        }

        return "验证成功";
    }
}
```

+ **defaultKaptcha：用于生成验证码。**该方拦截/kaptcha路径，在前端访问该路径后就新建一个方法，在方法里可以生成验证码对象，并以图片流的方式写到前端以供显示。
+ **verify：用于校验验证码。**该方法拦截 /verify路径为，请求参数为 code，即用户输入的验证码，在进行基本的非空验证后，与之前保存在session中的 rightCode值进行比较，不同则返回验证码错误，相同则返回验证成功。



### 2.4 前端显示页面

新建 verify.html。

+ 该页面中显示验证码，验证码图片关联后端验证码访问路径 /kaptcha，同时定义了 `onclick` 方法，在点击该图片时可以动态的切换显示一个新的验证码，点击时访问的路径为 `/kaptcha?d=1565950414611`，即原来的验证码路径后面带上一个时间戳参数，时间戳是会变化的，所以每次点击都会是一个与之前不同的请求。
+ 提供用户输入验证码的输入框以及提交按钮。用户在输入框中输入验证码后可以点击“验证”按钮，点击事件触发后执行 js 方法，该方法会获取到用户输入的验证码的值，并将其作为请求参数，之后进行 Ajax 请求，请求后会在弹框中显示后端返回的处理结果。

```html
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>验证码测试</title>
    </head>
    <body>
        <img src="/kaptcha" onclick="this.src='/kaptcha?d='+new Date()*1" />
        <input type="text" maxlength="5" id="code" placeholder="请输入验证码" />
        <button id="verify">验证</button>
    </body>
    <script src="jquery.js"></script>
    <script type="text/javascript">
        $(function () {
            $('#verify').click(function () {
                var code = $('#code').val();
                $.ajax({
                    type: 'GET', //方法类型
                    url: '/verify?code=' + code,
                    success: function (result) {
                        alert(result);
                    },
                    error: function () {
                        alert('请求失败');
                    },
                });
            });
        });
    </script>
</html>
```