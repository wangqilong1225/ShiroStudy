package com.wql.realm;

import com.wql.entity.User;
import com.wql.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.CachingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    /**
     * 登录认证自定义逻辑方法
     * 自定义登录认证的逻辑，配置生效后，当shiro的login方法被执行时，底层会
     * 自动的调用该方法完成登录认证，而不是使用shiro自己内置的登录认证逻辑
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("自定义登录认证开始...");
        //获取用户的登录名
        String uname = token.getPrincipal().toString();
        //根据用户名从数据库获取用户信息
        User user = userService.selUserInfoService(uname);
        if (user != null) {
            //效验密码
            String salt = "wql";  //盐
            ByteSource bs = ByteSource.Util.bytes(salt);
            AuthenticationInfo info = new SimpleAuthenticationInfo(token.getPrincipal(), user.getPwd(), bs, token.getPrincipal().toString());
            return info;
        }
        return null;
    }

    /**
     * 自定义授权方法
     * 每一个授权都会走到这里验证
     * 该方法是有shiro自动触发，调用该方法获取当前认证成功的用户权限信息，
     * 然后shiro获取当前方法的返回值后，自动的进行权限效验，如果效验成功
     * 则正常执行对应的资源；效验失败则报错。
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("自定义授权开始...");
        String uname = (String) principalCollection.getPrimaryPrincipal();
        // 1.查询当前用户具备的权限信息
        List<String> list = userService.selPowerInfoService(uname);
        // 查询当前用户的角色信息
        List<String> list2 = userService.selRoleInfoService(uname);

        // 2.将当前用户具备的权限信息给shiro
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(list);
        simpleAuthorizationInfo.addRoles(list2);

        // 3.返回
        return simpleAuthorizationInfo;
    }
}
