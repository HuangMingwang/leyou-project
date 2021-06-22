package com.leyou.sms.constants;

/**
 * @author Huang Mingwang
 * @create 2021-06-04 4:25 下午
 */
public final class SmsConstants {
    /**
     * 请求参数
     */
    public static final String SMS_PARAM_KEY_PHONE = "PhoneNumbers";
    public static final String SMS_PARAM_KEY_SIGN_NAME = "SignName";
    public static final String SMS_PARAM_KEY_TEMPLATE_CODE = "TemplateCode";
    public static final String SMS_PARAM_KEY_TEMPLATE_PARAM= "TemplateParam";

    /**
     * 响应结果
     */
    public static final String SMS_RESPONSE_KEY_CODE = "Code";
    public static final String SMS_RESPONSE_KEY_MESSAGE = "Message";

    /**
     * 状态
     */
    public static final String OK = "OK";

    /**
     * 短信验证码的参数的模板
     */
    public static final String VERIFY_CODE_PARAM_TEMPLATE = "{\"code\":\"%s\"}";
}