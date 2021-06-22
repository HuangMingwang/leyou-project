package com.leyou.auth.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Huang Mingwang
 * @create 2021-06-06 9:04 下午
 */
public interface UserAuthService {
    void login(String username, String password, HttpServletResponse response);

    String verify(HttpServletRequest request);

    void logout(HttpServletRequest request, HttpServletResponse response);
}
