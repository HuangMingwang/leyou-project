package com.leyou.auth.controller;

import com.leyou.auth.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Huang Mingwang
 * @create 2021-06-06 9:31 下午
 */
@RestController
@RequestMapping("/user")
public class UserAuthController {

    private final UserAuthService service;

    public UserAuthController(UserAuthService service) {
        this.service = service;
    }

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return 无
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      HttpServletResponse response) {
        service.login(username, password, response);
        return ResponseEntity.noContent().build();
    }

    /**
     * 验证用户信息
     * @return 用户信息
     */
    @GetMapping("/verify")
    public ResponseEntity<String> verify(HttpServletRequest request) {
        String username = service.verify(request);

        return ResponseEntity.ok(username);
    }

    /**
     *
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        service.logout(request, response);
        return ResponseEntity.noContent().build();

    }


}
