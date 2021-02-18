package com.wql.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {
    //跳转登录页面
    @RequestMapping("login")
    public String loginPage() {
        return "login";
    }

    //跳转主页面
    @RequestMapping("main")
    public String getMainPage() {
        return "main";
    }

    @RequestMapping("userLogin")
    public String userLogin(String uname, String pwd, @RequestParam(defaultValue = "false") Boolean remember) {
        //获取subject对象
        Subject subject = SecurityUtils.getSubject();
        //创建对象存储认证信息
        AuthenticationToken token = new UsernamePasswordToken(uname, pwd, remember);
        //调用shiro的login方法完成登录认证
        try {
            //login()没有返回值，只能通过是否有异常判断是否登录成功
            subject.login(token);
            return "redirect:/main";
        } catch (UnknownAccountException e) {
            return "账号不存在";
        } catch (IncorrectCredentialsException e) {
            return "密码错误";
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            return "权限不足";
        }
    }
}
