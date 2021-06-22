package com.leyou.common.utils;

import org.junit.Test;

/**
 * @author Huang Mingwang
 * @create 2021-06-05 7:59 下午
 */
public class RsaUtilsTest {
    public static void main(String[] args) {
      /*  try {
            RsaUtils.generateKey("d:/develop/ssh/id_rsa.pub","d:/develop/ssh/id_rsa", "hahahah");
            // 获取私钥
            PrivateKey privateKey = RsaUtils.getPrivateKey("d:/develop/ssh/id_rsa");
            System.out.println("privateKey = " + privateKey);
            // 获取公钥
            PublicKey publicKey = RsaUtils.getPublicKey("d:/develop/ssh/id_rsa.pub");
            System.out.println("publicKey = " + publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Test
    public void name() throws Exception {
       /* // 获取私钥
        PrivateKey privateKey = RsaUtils.getPrivateKey("d:/develop/ssh/id_rsa");
        // 生成token
        String token = JwtUtils.generateTokenExpireInSeconds(new UserInfo(1L, "Jack", Collections.singletonList("guest")), privateKey, 5);
        System.out.println("token = " + token);

        // 获取公钥
        PublicKey publicKey = RsaUtils.getPublicKey("d:/develop/ssh/id_rsa.pub");
        // 解析token
        Payload<UserInfo> info = JwtUtils.getInfoFromToken(token, publicKey, UserInfo.class);

        System.out.println("info.getExpiration() = " + info.getExpiration());
        System.out.println("info.getUserInfo() = " + info.getUserInfo());
        System.out.println("info.getId() = " + info.getId());*/
    }
}
