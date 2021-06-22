package com.leyou.gateway.filter;

import com.leyou.common.constants.BaseRedisConstants;
import com.leyou.common.entity.Payload;
import com.leyou.common.entity.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.gateway.config.FilterProperties;
import com.leyou.gateway.config.JwtProperties;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.SignatureException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.leyou.common.constants.BaseTokenConstants.COOKIE_NAME;

/**
 * @author Huang Mingwang
 * @create 2021-06-07 10:05 下午
 */
@Component
@Slf4j
public class LoginGlobalFilter implements GlobalFilter, Ordered {

    private final JwtProperties jwtProperties;

    private final StringRedisTemplate redisTemplate;
    private final FilterProperties filterProperties;

    public LoginGlobalFilter(JwtProperties jwtProperties, StringRedisTemplate redisTemplate, FilterProperties filterProperties) {
        this.jwtProperties = jwtProperties;
        this.redisTemplate = redisTemplate;
        this.filterProperties = filterProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1。获取request对象
        ServerHttpRequest request = exchange.getRequest();
        // 2.获取cookie
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        HttpCookie cookie = cookies.getFirst(COOKIE_NAME);
        try {
            if (cookie == null) {
                //结束
                throw new SignatureException("未登录或状态无效");
            }
            // 3. 检验token是否有效
            String token = cookie.getValue();
            Payload<UserInfo> payload;
            payload = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey(), UserInfo.class);

            /**
             * 获取redis中的token,与这个token进行比较
             * 刷新token时间
             */
            UserInfo userInfo = payload.getUserInfo();
            if (userInfo == null) {
                throw new SignatureException("未登录或状态无效");
            }
            String jti = redisTemplate.opsForValue().get(BaseRedisConstants.JTI_KEY_PREFIX + userInfo.getId());
            if (!org.apache.commons.lang.StringUtils.equals(jti, payload.getId())) {
                throw new SignatureException("未登录或已经过期");
            }
            redisTemplate.expire(BaseRedisConstants.JTI_KEY_PREFIX + userInfo.getId(),
                    BaseRedisConstants.TOKEN_EXPIRE_MINUTES, TimeUnit.MINUTES);
            log.info("用户{}正在访问资源{}", userInfo.getUsername(), request.getPath().toString());
            // 4.有效放行
            return chain.filter(exchange);
        } catch (ExpiredJwtException | SignatureException e) {
            // 没有登录，判断是不是白名单中的请求
            if (isAllowRequest(exchange)) {
                // 如果是，那么放行
                return chain.filter(exchange);
            }
            log.error("用户非法访问资源{}", request.getPath().toString());
            // 不是的话，解析不通过，返回401
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        } catch (Exception e) {
            log.info("未知错误,可能是解析失败");
            // 内部异常，返回500
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }
    }

    private boolean isAllowRequest(ServerWebExchange exchange) {
        // 1.获取当前请求Request
        ServerHttpRequest request = exchange.getRequest();
        // 2.获取请求方式和请求路径
        String path = request.getPath().toString();
        String method = Objects.requireNonNull(request.getMethod()).toString();
        // 3.遍历白名单
        for (Map.Entry<String, Set<String>> entry : filterProperties.getAllowRequests().entrySet()) {
            // 白名单前缀
            String key = entry.getKey();
            // 白名单的method集合
            Set<String> value = entry.getValue();
            // 判断是否符合
            if (StringUtils.startsWithIgnoreCase(path, key) && value.contains(method)) {
                // 符合白名单
                return true;
            }
        }
        // 不符合白名单
        return false;
    }

    @Override
    public int getOrder() {
        // 登录拦截，可以采用最高优先级
        return HIGHEST_PRECEDENCE;
    }
}
