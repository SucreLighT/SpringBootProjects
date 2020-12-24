package cn.sucrelt.springbootcheckcode.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @description:
 * @author: sucre
 * @date: 2020/12/24
 * @time: 10:03
 */

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
