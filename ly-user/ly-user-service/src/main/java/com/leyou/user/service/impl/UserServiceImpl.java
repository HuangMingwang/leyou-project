package com.leyou.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.RegexUtils;
import com.leyou.user.dto.UserDTO;
import com.leyou.user.entity.User;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.service.UserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.leyou.common.constants.BaseMQConstants.ExchangeConstants.SMS_EXCHANGE_NAME;
import static com.leyou.common.constants.BaseMQConstants.RoutingKeyConstants.VERIFY_CODE_KEY;
import static com.leyou.common.constants.BaseRedisConstants.KEY_PREFIX;

/**
 * @author Huang Mingwang
 * @create 2021-06-05 5:32 下午
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final RabbitTemplate rabbitTemplate;
    private final StringRedisTemplate redisTemplate;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(RabbitTemplate rabbitTemplate, StringRedisTemplate redisTemplate, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.rabbitTemplate = rabbitTemplate;
        this.redisTemplate = redisTemplate;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * 发送验证码,通过给rabbitMQ发送消息
     * @param phone 电话号码
     */
    @Override
    public void sendVerifyCode(String phone) {
        if (!RegexUtils.isPhone(phone)) {
            throw new LyException(400, "输入的电话号码错误");
        }
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        String code = RandomStringUtils.randomNumeric(6);
        map.put("code", code);

        rabbitTemplate.convertAndSend(SMS_EXCHANGE_NAME, VERIFY_CODE_KEY, map);
        redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5L, TimeUnit.MINUTES);
    }

    @Override
    public Boolean infoExists(String data, Integer type) {
        Integer count = query().eq(type == 1, "username", data)
                .eq(type == 2, "phone", data)
                .count();
        return count > 0;

    }

    /**
     * 注册
     * @param user 用户信息
     * @param code 验证码
     */
    @Override
    @Transactional
    public void register(User user, String code) {
        if (!RegexUtils.isCodeValid(code)) {
            throw new LyException(400, "验证码格式错误");
        }
        String codeRedis = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if (!StringUtils.isEmpty(codeRedis)) {
            if (codeRedis.equals(code)) {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                boolean flag = save(user);
                if (!flag) {
                    throw new LyException(500, "注册失败");
                }
            }
        }
    }

    @Override
    public UserDTO queryUserByNameAndPassword(String username, String password) {
        User user = getOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            throw new LyException(400, "用户不存在");
        }
        boolean flag = bCryptPasswordEncoder.matches(password, user.getPassword());
        if (!flag) {
            throw new LyException(400, "密码不正确");
        }
        return new UserDTO(user);
    }
}
