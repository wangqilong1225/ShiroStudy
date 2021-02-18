package com.wql.matcher;


import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;

import java.util.concurrent.atomic.AtomicInteger;


public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {
    //声明缓存对象
    private Cache passwordRetryEhcache;
    //声明构造方法
    public RetryLimitHashedCredentialsMatcher(EhCacheManager ehCacheManager){
        passwordRetryEhcache=ehCacheManager.getCacheManager().getCache("passwordRetryEhcache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info){
        //1.获取缓存中的错误次数
        //获取用户身份信息
        String uname=token.getPrincipal().toString();
        //获取缓存对象
        Element element=passwordRetryEhcache.get(uname);
        //判断是否存在缓存
        if(element==null){
            Element ele=new Element(uname,new AtomicInteger(0));
        }


        //2.判断缓存次数

        return super.doCredentialsMatch(token,info);
    }

}
