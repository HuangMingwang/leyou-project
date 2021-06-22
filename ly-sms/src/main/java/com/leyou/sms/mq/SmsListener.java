package com.leyou.sms.mq;

import com.leyou.common.utils.RegexUtils;
import com.leyou.sms.utils.SmsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

import static com.leyou.common.constants.BaseMQConstants.*;


/**
 * @author Huang Mingwang
 * @create 2021-06-04 4:33 下午
 */
@Slf4j
@Component
public class SmsListener {

    private final SmsUtils smsUtils;

    public SmsListener(SmsUtils smsUtils) {
        this.smsUtils = smsUtils;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = QueueConstants.SMS_VERIFY_CODE_QUEUE, durable = "true"),
            exchange = @Exchange(name = ExchangeConstants.SMS_EXCHANGE_NAME, type = ExchangeTypes.DIRECT),
            key = RoutingKeyConstants.VERIFY_CODE_KEY
    ))
    public void listenerVerifyCodeMessage(Map<String, String> msg){
        // 获取参数
        if (CollectionUtils.isEmpty(msg)) {
            // 如果消息为空，不处理
            return ;
        }
        // 手机号
        String phone = msg.get("phone");
        if (!RegexUtils.isPhone(phone)) {
            // 手机号有误，不处理
            return ;
        }
        // 验证码
        String code = msg.get("code");
        if (!RegexUtils.isCodeValid(code)) {
            // 验证码有误，不处理
            return ;

        }
        // 发送短信
        try {
            smsUtils.sendVerifyCode(phone, code);
        } catch (Exception e) {
            log.error("短信发送失败, 原因{}", e.getMessage() );
        }
    }
}
