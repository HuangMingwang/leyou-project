package com.leyou.common.utils;

import com.leyou.common.constants.BaseRegexPatterns;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 虎哥
 */
public class RegexUtils {
    /**
     * 是否符合手机格式
     * @param phone 要校验的手机号
     * @return true:符合，false：不符合
     */
    public static boolean isPhone(String phone){
        return matches(phone, BaseRegexPatterns.PHONE_REGEX);
    }
    /**
     * 是否符合邮箱格式
     * @param email 要校验的邮箱
     * @return true:符合，false：不符合
     */
    public static boolean isEmail(String email){
        return matches(email, BaseRegexPatterns.EMAIL_REGEX);
    }

    /**
     * 是否符合验证码格式
     * @param code 要校验的验证码
     * @return true:符合，false：不符合
     */
    public static boolean isCodeValid(String code){
        return matches(code, BaseRegexPatterns.VERIFY_CODE_REGEX);
    }
	
	/**
     * 是否符合订单编号格式
     * @param orderId 要校验的订单编号
     * @return true:符合，false：不符合
     */
    public static boolean isOrderId(String orderId){
        return matches(orderId, BaseRegexPatterns.ORDER_ID_REGEX);
    }
	/**
     * 是否符合数字格式
     * @param num 要校验的数字字符串
     * @return true:符合，false：不符合
     */
    public static boolean isNumber(String num){
        return matches(num, BaseRegexPatterns.ORDER_FEE_REGEX);
    }

    private static boolean matches(String str, String regex){
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return str.matches(regex);
    }
}
