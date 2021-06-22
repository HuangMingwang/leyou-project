package com.leyou.auth.service.impl;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.service.UserAuthService;
import com.leyou.common.constants.BaseRedisConstants;
import com.leyou.common.entity.Payload;
import com.leyou.common.entity.UserInfo;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import com.leyou.user.client.UserClient;
import com.leyou.user.dto.UserDTO;
import feign.FeignException;
import io.jsonwebtoken.JwtException;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

import static com.leyou.common.constants.BaseTokenConstants.COOKIE_NAME;
import static com.leyou.common.constants.BaseTokenConstants.DOMAIN;

/**
 * @author Huang Mingwang
 * @create 2021-06-06 9:05 下午
 */
@Service
public class UserAuthServiceImpl implements UserAuthService {
    private final UserClient userClient;
    private final JwtProperties jwtProperties;
    private final StringRedisTemplate redisTemplate;

    public UserAuthServiceImpl(UserClient userClient, JwtProperties jwtProperties, StringRedisTemplate redisTemplate) {
        this.userClient = userClient;
        this.jwtProperties = jwtProperties;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void login(String username, String password, HttpServletResponse response) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new LyException(400, "用户名或密码不能为空");
        }

        // 1.查询用户
        UserDTO userDTO = null;

        try {
            userDTO = userClient.queryUserByUsernameAndPassword(username, password);
        } catch (FeignException e) {
            // 捕捉feign的异常，并获取feign调用状态码和异常信息
            throw new LyException(e.status(), e.contentUTF8(), e);
        }
        // 2.判断查询结果
        if (userDTO == null) {
            throw new LyException(400, "用户名或密码错误");
        }
        // 3.生成token
        String jti = JwtUtils.createJTI();
        String token = JwtUtils.generateTokenWithJTI(new UserInfo(userDTO.getId(), username, null)
                , jti, jwtProperties.getPrivateKey());
        // 4.将jti存入redis
        redisTemplate.opsForValue().set(BaseRedisConstants.JTI_KEY_PREFIX + userDTO.getId(), jti,
                BaseRedisConstants.TOKEN_EXPIRE_MINUTES, TimeUnit.MINUTES);
        // 5.写入cookie
        // "/"代表一切路径都有效
        CookieUtils.builder(response)
                .domain(DOMAIN)
                .maxAge(-1)
                .path("/")
                .httpOnly(true)
                .build(COOKIE_NAME, token);


    }

    @Override
    public String verify(HttpServletRequest request) {
        // 1.获取cookie
        String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
        if (StringUtils.isEmpty(token)) {
            throw new LyException(401, "用户未登录或者超时");
        }
        // 2.验证cookie的正确性
        Payload<UserInfo> payload = null;
        try {
            payload = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey(), UserInfo.class);
        } catch (JwtException e) {
            throw new LyException(401, "用户未登录或者不存在");
        }
        //3.获取cookie中的username
        UserInfo userInfo = payload.getUserInfo();
        if (userInfo == null) {
            throw new LyException(401, "用户未登录或者不存在");
        }
        String jti = redisTemplate.opsForValue().get(BaseRedisConstants.JTI_KEY_PREFIX + userInfo.getId());
        if (!StringUtils.equals(jti, payload.getId())) {
            throw new LyException(401, "登录失效");
        }
        return userInfo.getUsername();
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            //将redis中token删除
            String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
            if (StringUtils.isEmpty(token)) {
                throw new LyException(400, "未知错误,未登录不该点到这个按钮");
            }
            Payload<UserInfo> payload = null;
            try {
                payload = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey(), UserInfo.class);
            } catch (JwtException e) {
                throw new LyException(400, "不存在token?");
            }
            //3.获取cookie中的username
            UserInfo userInfo = payload.getUserInfo();
            if (userInfo == null) {
                throw new LyException(400, "未知错误,未登录不该点到这个按钮");
            }
            redisTemplate.delete(BaseRedisConstants.JTI_KEY_PREFIX + userInfo.getId());
            //将cookie时间设置为0
            CookieUtils.deleteCookie(COOKIE_NAME, DOMAIN, response);
        } catch (LyException e) {
            throw new LyException(400, "未知错误,未登录不该点到这个按钮");
        } catch (Exception e) {
            throw new LyException(500, "退出登录失败");
        }
    }
}
