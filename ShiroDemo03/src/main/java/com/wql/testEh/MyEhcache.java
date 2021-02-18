package com.wql.testEh;


import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.net.URL;

public class MyEhcache {
    public static void main(String[] args){
        // 1.创建缓存管理对象CacheManager
        //D:\MyWorkSpace\ShiroDemo03\target\classes
        URL resource=MyEhcache.class.getClassLoader().getResource("cache.xml");
        CacheManager cacheManager=new CacheManager(resource);
        //2.根据配置文件中的缓存策略获取缓存对象
        Cache cache= cacheManager.getCache("helloWorldCache");
        //3.创建对象存储缓存数据
        Element element=new Element("str","sxt");
        //4.将对象缓存到cache中
        cache.put(element);
        //5.获取缓存数据
        Element element1= cache.get("str");

        System.out.println(element1.getObjectValue());

    }
}
