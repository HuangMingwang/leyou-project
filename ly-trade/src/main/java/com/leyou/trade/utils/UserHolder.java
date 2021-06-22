package com.leyou.trade.utils;

import org.springframework.stereotype.Component;

/**
 * @author Huang Mingwang
 * @create 2021-06-08 9:52 上午
 */
public class UserHolder {
    private static final ThreadLocal<Long> tl = new ThreadLocal<>();

    public static void setUser(Long id){
        tl.set(id);
    }

    public static Long getUser(){
        Long id = tl.get();
        if(id == null){
            return 0L;
        }
        return id;
    }

    public static void removeUser(){
        tl.remove();
    }
}

