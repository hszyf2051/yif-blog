package com.yif.utils;

import com.yif.pojo.SysUser;

/**
 * ThreadLocal保存用户信息
 */
public class UserThreadLocal {

    /**
     *  创建单例线程
     */
    private UserThreadLocal(){}
    // 线程变量隔离
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }
    public static SysUser get(){
        return LOCAL.get();
    }
    public static void remove(){
        LOCAL.remove();
    }
}
