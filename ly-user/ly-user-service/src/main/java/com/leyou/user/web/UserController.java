package com.leyou.user.web;

import com.leyou.common.exception.LyException;
import com.leyou.user.dto.UserDTO;
import com.leyou.user.entity.User;
import com.leyou.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * @author Huang Mingwang
 * @create 2021-06-05 5:46 下午
 */
@RestController
@RequestMapping("/info")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 发送短信验证码
     * @param phone 手机号 yy
     * @return 无
     */
    @PostMapping("/code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone")String phone) {

        userService.sendVerifyCode(phone);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    /**
     * 检验数据是否存在
     * @param data 数据
     * @param type 类型
     * @return 布尔，true：存在，false：不存在
     */
    @GetMapping("/exists/{data}/{type}")
    public ResponseEntity<Boolean> infoExists(@PathVariable("data") String data,
                                             @PathVariable("type")Integer type){
        Boolean flag = userService.infoExists(data, type);
        return ResponseEntity.ok(flag);
    }

    /**
     * 用户注册
     * @param user 用户信息
     * @param result result
     * @param code 验证码
     * @return 无
     */
    @PostMapping
    public ResponseEntity<Void> register(@Valid User user, BindingResult result, @RequestParam("code")String code) {
        if (result.hasErrors()) {
            String errorMsg = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
            System.out.println(errorMsg);
            throw new LyException(400, "请求参数错误");
        }
        userService.register(user, code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    /**
     * 根据用户名和密码查询用户
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     */
    @GetMapping
    public ResponseEntity<UserDTO> queryUserByNameAndPassword(@RequestParam("username")String username,
                                                              @RequestParam("password")String password) {

        UserDTO userDTO = userService.queryUserByNameAndPassword(username, password);
        return ResponseEntity.ok(userDTO);
    }
}
