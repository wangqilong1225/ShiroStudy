package com.wql.controller;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("userController")
public class UserController {
    @RequestMapping("userDel")
    @RequiresPermissions("user:del")
    @ResponseBody
    public String userDel(){
        System.out.println("用户信息删除方法");
        return "删除成功";
    }

    @RequestMapping("userEdit")
    @RequiresRoles(value={"user","user2"},logical = Logical.OR)
    @ResponseBody
    public String userEdit(){
        System.out.println("用户信息修改方法");
        return "修改成功";
    }

    @RequestMapping("userAdd")
    @RequiresPermissions("user:add")
    @ResponseBody
    public String userAdd(){
        System.out.println("用户信息新增方法");
        return "新增成功";
    }

    @RequestMapping("userSel")
    @RequiresPermissions("user:sel")
    @ResponseBody
    public String userSel(){
        System.out.println("用户信息查询方法");
        return "查询成功";
    }
}
