package com.leyou.user.dto;

import com.leyou.common.dto.BaseDTO;
import com.leyou.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Huang Mingwang
 * @create 2021-06-05 5:56 下午
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AddressDTO extends BaseDTO {
    private Long id;
    private Long userId;
    private String addressee;// 收件人姓名
    private String phone;// 电话
    private String province;// 省份
    private String city;// 城市
    private String district;// 区
    private String street;// 街道地址
    private String  postcode;// 邮编
    private Boolean isDefault;

    public AddressDTO(BaseEntity entity) {
        super(entity);
    }
}
