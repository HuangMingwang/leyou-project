package com.leyou.trade.interceptors;

import com.leyou.common.constants.BaseTokenConstants;
import com.leyou.common.entity.Payload;
import com.leyou.common.entity.UserInfo;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import com.leyou.trade.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @author Huang Mingwang
 * @create 2021-06-08 9:58 上午
 */
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取cookie中的token
        String token = CookieUtils.getCookieValue(request, BaseTokenConstants.COOKIE_NAME);
        // 获取token中的用户, 不需要公钥或私钥，直接解析载荷
        try {
            Payload<UserInfo> payload = JwtUtils.getInfoFromToken(token, UserInfo.class);

            UserInfo userInfo = payload.getUserInfo();

            // 保存用户
            UserHolder.setUser(userInfo.getId());
            // 放行
            return true;
        } catch (UnsupportedEncodingException e) {
            // 获取用户失败，无需向后走了
            throw new LyException(500, "解析用户信息失败！");
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 业务结束后，移除ThreadLocal中的用户
        UserHolder.removeUser();
    }
}

