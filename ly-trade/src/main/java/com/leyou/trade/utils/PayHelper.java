package com.leyou.trade.utils;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfigImpl;
import com.leyou.trade.constants.BasePayConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Huang Mingwang
 * @create 2021-06-11 6:21 上午
 */
@Component
public class PayHelper {

    private final WXPay wxPay;

    private final WXPayConfigImpl payConfig;

    public PayHelper(WXPay wxPay, WXPayConfigImpl payConfig) {
        this.wxPay = wxPay;
        this.payConfig = payConfig;
    }

    public String unifiedOrder(Long orderId, Long totalFee){
        // 1.准备请求参数：
        Map<String, String> data = new HashMap<String, String>();
        // 1.1.商品有关信息
        data.put("body", BasePayConstants.ORDER_DESC);
        data.put("out_trade_no", orderId.toString());
        data.put("total_fee", totalFee.toString());
        // 1.2.交易的固定参数
        data.put("spbill_create_ip", payConfig.getSpbillCreateIp());
        data.put("trade_type", BasePayConstants.UNIFIED_ORDER_TRADE_TYPE);

        try {
            // 2.统一下单
            Map<String, String> resp = wxPay.unifiedOrder(data);

            // 3.数据校验
            // 3.1.returnCode校验
            checkReturnCode(resp);
            // 3.2.resultCode校验
            checkResultCode(resp);
            // 3.3.签名校验
            checkSignature(data);

            // 4.获取支付url，并返回
            String url = resp.get(BasePayConstants.PAY_URL_KEY);
            if (StringUtils.isBlank(url)) {
                throw new RuntimeException("支付链接为空！");
            }
            return url;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void checkSignature(Map<String, String> data) throws RuntimeException {
        try {
            boolean signatureValid = wxPay.isResponseSignatureValid(data);
            if(!signatureValid){
                // 签名有误
                throw new RuntimeException("签名有误！");
            }
        } catch (Exception e) {
            throw new RuntimeException("签名有误！");
        }
    }

    public void checkResultCode(Map<String, String> resp) {
        String resultCode = resp.get(BasePayConstants.RESULT_CODE_KEY);
        if(BasePayConstants.FAIL.equals(resultCode)){
            // returnCode错误
            throw new RuntimeException(resp.get(BasePayConstants.ERROR_CODE_KEY));
        }
    }

    public void checkReturnCode(Map<String, String> resp) {
        String returnCode = resp.get(BasePayConstants.RETURN_CODE_KEY);
        if(BasePayConstants.FAIL.equals(returnCode)){
            // returnCode错误
            throw new RuntimeException(resp.get(BasePayConstants.RETURN_MESSAGE_KEY));
        }
    }
}
