package com.leyou.common.entity;

import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 因为所有的字段都会有create_time和update_time属性，因此我们会先定义一个通用的实体类，因为是通用的
 * 因此放到ly-common这个项目的com.leyou.common.entity中。
 * @author Huang Mingwang
 * @create 2021-05-22 2:29 下午
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BaseEntity {
    private Date createTime;
    private Date updateTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
