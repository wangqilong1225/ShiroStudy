package com.wql.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {
    //声明单元方法，完成登录
    @RequestMapping("userLogin")
    public String userLogin(String uname, String pwd) {
        // 1.获取SecurityManager对象，将配置信息读取到 SecurityManager中
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();

        // 2.获取Subject对象
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();

        // 3.调用登录认证方法
        //  创建对象存储认证信息
        AuthenticationToken token = new UsernamePasswordToken(uname, pwd);
        //  完成登录认证
        try {
            //login()没有返回值，只能通过是否有异常判断是否登录成功
            subject.login(token);
            //return "登录成功";
            return "redirect:/userSq";
        } catch (UnknownAccountException e) {
            return "账号不存在";
        } catch (IncorrectCredentialsException e) {
            return "密码错误";
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            return "权限不足";
        }
    }

    @RequestMapping("userSq")
    @ResponseBody
    public String userSq() {
        Subject subject = SecurityUtils.getSubject();
        //授权认证
        // 角色判断
        boolean role1 = subject.hasRole("role1");
        System.out.println("判断是否具有角色Role1：" + role1);
        // 权限判断
        //subject.checkPermission("sys:user:insert"); //如果认证成功的用户有权限则正常执行，没有则报错
        boolean permit = subject.isPermitted("sys:user:insert");//权限判断，返回boolean
        System.out.println("权限判断结果：" + permit);
        return "授权的角色判断结果：" + role1 + "权限判断结果：" + permit;
    }
}
