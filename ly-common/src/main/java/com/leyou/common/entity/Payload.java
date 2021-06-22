package com.leyou.common.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Huang Mingwang
 */
@Data
public class Payload<T> {
    /**
     * token的唯一标示,JTI
     */
    private String id;
    /**
     * 过期时间
     */
    private Date expiration;
    /**
     * 用户信息
     */
    private T userInfo;
}
