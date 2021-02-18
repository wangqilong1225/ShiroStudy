package com.wql.config;

import com.wql.realm.MyRealm;
import com.wql.realm.MyRealm2;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class ShiroConfig {
    //声明自定义MyRealm的属性
    @Autowired
    private MyRealm myRealm;

    @Autowired
    private MyRealm2 myRealm2;

    /**
     * SecurityManager的bean配置方法
     * 注意：
     * SpringBoot项目启动时会自动加载配置类的资源，
     * 并将bean方法配置的资源完成初始化设置，然后放到Spring容器中，
     * 相当于bean标签配置
     */
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //设置登录登录认证开启MD5加密
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        //加密迭代次数
        matcher.setHashIterations(2);
        myRealm.setCredentialsMatcher(matcher);

        //关闭身份验证缓存
        myRealm.setAuthenticationCachingEnabled(false);
        //是否开启权限认证缓存
        myRealm.setAuthorizationCachingEnabled(true);
        //缓存策略
        myRealm.setAuthorizationCacheName("passwordRetryEhcache");

        /**********
         //认证策略：多个realm认证组合策略； 认证策略需要放在realm集成前
         ModularRealmAuthenticator modularRealmAuthenticator=new ModularRealmAuthenticator();
         //AllSuccessfulStrategy 所有的realm都认证成功
         //AtLeastOneSuccessfulStrategy 任意一个realm认证成功即可
         //FirstSuccessfulStrategy 第一个realm认证成功
         modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
         defaultWebSecurityManager.setAuthenticator(modularRealmAuthenticator);
         **********/

        //将自定义的realm集成到DefaultWebSecurityManager对象中，单个realm认证
        defaultWebSecurityManager.setRealm(myRealm);

        /*************
         //创建集合存储自定义realm对象
         ArrayList<Realm> list=new ArrayList<Realm>();
         list.add(myRealm);
         list.add(myRealm2);
         defaultWebSecurityManager.setRealms(list);
         ***************/

        //设置shiro的remember me功能
        defaultWebSecurityManager.setRememberMeManager(rememberMeManager());

        //设置Ehcache的缓存
        defaultWebSecurityManager.setCacheManager(getEhcacheManager());
        return defaultWebSecurityManager;
    }


    /**
     * 设置RememberMeManager对象
     */
    public CookieRememberMeManager rememberMeManager() {
        //创建CookieRememberMeManager对象
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        //1.设置Cookie的参数
        cookieRememberMeManager.setCookie(simpleCookie());
        //2.对cookie信息进行加密设置
        cookieRememberMeManager.setCipherKey("1234567890987654".getBytes());
        //返回
        return cookieRememberMeManager;
    }


    /**
     * 设置cookie
     */
    public SimpleCookie simpleCookie() {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(30 * 24 * 60 * 60);
        return cookie;
    }


    /**
     * 配置shiro内置过滤器
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
        //放行登录页面请求
        definition.addPathDefinition("/login", "anon");
        definition.addPathDefinition("/userLogin", "anon");
        definition.addPathDefinition("/logout", "logout");
        definition.addPathDefinition("/**", "user");
        return definition;
    }

    /**
     * 配置shiro整合Ehcache的CacheManager
     */
    public EhCacheManager getEhcacheManager() {
        //1.创建shiro EhCacheManager对象
        EhCacheManager cacheManager = new EhCacheManager();
        //2.创建InputStream对象读取缓存策略
        InputStream is = null;
        try {
            is = ResourceUtils.getInputStreamForPath("classpath:ehcache/ehcache-shiro.xml");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        net.sf.ehcache.CacheManager cm = new net.sf.ehcache.CacheManager(is);
        //3.将Ehcache的CacheManager对象存储到shiro的CacheManager对象中
        cacheManager.setCacheManager(cm);
        //4.返回
        return cacheManager;
    }
}
