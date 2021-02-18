package com.wql.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * 自定义realm类，在该类中可以声明自定义的登录认证和授权方法
 */
public class MyRealm extends AuthenticatingRealm {

    //自定义的登录方法: shiro login的底层调用这里
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("认证开始...");
        //获取用户的登录身份
        String uname = token.getPrincipal().toString();
        //获取用户登录的凭证
        String pwd = new String((char[]) token.getCredentials());

        System.out.println("用户的登录信息：" + uname + "   " + pwd);

        //用户登录信息判断
        if (uname.equals("admin")) {
            String sysPwd = "9aa2e5743fb03b26d7a3936431f62089";
            //使用shiro完成凭证的校验:
            //shiro在此之前已经拿到登录的用户和密码
            //这里再将数据库中查找到的用户，密码放入，shiro会帮我们验证用户密码是否正确
            AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(token.getPrincipal(), sysPwd, ByteSource.Util.bytes("wql"), "realmName");
            return authenticationInfo;
        }
        return null;
    }
}
