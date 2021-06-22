package com.leyou.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leyou.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

import static com.leyou.common.constants.BaseRegexPatterns.*;

/**
 * @author Huang Mingwang
 * @create 2021-06-05 5:37 下午
 */

@TableName(value = "tb_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity{
    @TableId
    private Long id;
    @Pattern(regexp = USERNAME_REGEX, message = "用户名输入不正确")
    private String username;
    @Pattern(regexp = PASSWORD_REGEX, message = "密码输入格式不正确")
    private String password;
    @Pattern(regexp = PHONE_REGEX, message = "电话输入格式不正确")
    private String phone;
}
