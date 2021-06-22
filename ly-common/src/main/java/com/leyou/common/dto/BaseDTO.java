package com.leyou.common.dto;

import com.leyou.common.entity.BaseEntity;
import com.leyou.common.utils.BeanHelper;
import org.springframework.beans.BeanUtils;

/**
 * 基本的DTO，提供了DTO和Entity之间的互相转换功能
 * PO: persistent object与数据库表衣衣对应
 * BO：business object用来组装PO，在业务层传递
 * DTO：data transfer object数据转移对象，用于服务端和客户端间的数据传输，比如服务器端返回数据到浏览器
 * @author Huang Mingwang
 * @create 2021-05-22 2:57 下午
 */
public abstract class BaseDTO {
    /**
     * DTO(Data Transfer Object)转PO(Persistent Object)
     * @param entityClass PO对象的字节码
     * @param <T> PO对象的类型
     * @return PO对象
     */
    public <T> T toEntity(Class<T> entityClass) {
        return BeanHelper.copyProperties(this, entityClass);
    }

    /**
     * 从Entity转为DTO
     * @param entity 任意实体
     */
    public BaseDTO(BaseEntity entity) {
        if(entity != null){
            BeanUtils.copyProperties(entity, this);
        }
    }

    public BaseDTO() {
    }
}
