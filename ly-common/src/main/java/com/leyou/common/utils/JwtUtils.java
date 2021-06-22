package com.leyou.common.utils;

import com.leyou.common.entity.Payload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author  HuYi.Zhang
 **/
public class JwtUtils {

    private static final String JWT_PAYLOAD_USER_KEY = "user";

    /**
     * 私钥加密生成token，永久有效
     *
     * @param userInfo   载荷中的数据
     * @param privateKey 私钥
     * @return JWT
     */
    public static String generateToken(Object userInfo, PrivateKey privateKey) {
        return Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, JsonUtils.toJson(userInfo))
                .setId(createJTI())
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * 私钥加密生成token，永久有效
     *
     * @param userInfo   载荷中的数据
     * @param privateKey 私钥
     * @return JWT
     */
    public static String generateTokenWithJTI(Object userInfo, String jti, PrivateKey privateKey) {
        return Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, JsonUtils.toJson(userInfo))
                .setId(jti)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * 私钥加密生成token，指定过期时间
     *
     * @param userInfo   载荷中的数据
     * @param privateKey 私钥
     * @param expire     过期时间，单位秒
     * @return JWT
     */
    public static String generateTokenExpireInSeconds(Object userInfo, PrivateKey privateKey, int expire) {
        return Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, JsonUtils.toJson(userInfo))
                .setId(createJTI())
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * 公钥解析token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return Jws<Claims>
     */
    private static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    public static String createJTI() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    }

    /**
     * 获取token中的用户信息，并且验证token是否有效、是否过期
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @param userType 载荷中的用户数据的类型
     * @return 用户信息
     */
    public static <T> Payload<T> getInfoFromToken(String token, PublicKey publicKey, Class<T> userType) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        Payload<T> claims = new Payload<>();
        claims.setId(body.getId());
        if(userType == String.class){
            claims.setUserInfo((T) body.get(JWT_PAYLOAD_USER_KEY).toString());
        }else {
            claims.setUserInfo(JsonUtils.toBean(body.get(JWT_PAYLOAD_USER_KEY).toString(), userType));
        }
        claims.setExpiration(body.getExpiration());
        return claims;
    }

    /**
     * 获取token中的ID信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     */
    public static String getIdFromToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        return claimsJws.getBody().getId();
    }

    private static final Decoder<String, byte[]> stringDecoder = Decoders.BASE64URL;
    /**
     * 获取token中的载荷信息
     *
     * @param token     用户请求中的令牌
     * @return 用户信息
     */
    public static <T> Payload<T> getInfoFromToken(String token, Class<T> userType) throws UnsupportedEncodingException {
        String payloadStr = StringUtils.substringBetween(token, ".");
        byte[] bytes = stringDecoder.decode(payloadStr);
        String json = new String(bytes, "UTF-8");
        Map<String, String> map = JsonUtils.toMap(json, String.class, String.class);
        Payload<T> claims = new Payload<>();
        claims.setId(map.get("jti"));
        claims.setExpiration(new Date(Long.valueOf(map.getOrDefault("exp", "0"))));

        if(userType == String.class){
            claims.setUserInfo((T) map.get("user"));
        }else {
            claims.setUserInfo(JsonUtils.toBean(map.get("user"), userType));
        }
        return claims;
    }
}