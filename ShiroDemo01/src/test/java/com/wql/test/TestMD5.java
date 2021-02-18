package com.wql.test;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

public class TestMD5 {
    //测试MD5加密
    @Test
    public void testMD() {
        // 1.声明测试的密码
        String password = "456";
        // 2.使用MD5加密
        Md5Hash md5Hash = new Md5Hash(password);
        //5d41402abc4b2a76b9719d911017c592
        System.out.println("一次加密的结果：" + md5Hash.toHex());
        // 3.带盐加密
        Md5Hash md5Hash1=new Md5Hash(password,"wql");
        //43b0bf53b49f4ac6042d5975f5e5e448
        System.out.println("带盐加密的结果：" + md5Hash1.toHex());
        // 4.带盐迭代加密
        Md5Hash md5Hash2=new Md5Hash(password,"wql",2);
        //9aa2e5743fb03b26d7a3936431f62089
        System.out.println("带盐迭代加密的结果：" + md5Hash2.toHex());
    }
}
