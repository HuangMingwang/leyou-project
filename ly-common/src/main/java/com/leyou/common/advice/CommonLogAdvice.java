package com.leyou.common.advice;

import com.leyou.common.exception.LyException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Huang Mingwang
 * @create 2021-05-20 2:07 下午
 */
@Slf4j
@Component
@Aspect
public class CommonLogAdvice {

    //拦截所有被Service注解的方法或者IService的实现类
    @Pointcut("within(@org.springframework.stereotype.Service *) || within(com.baomidou.mybatisplus.extension.service.IService+)")
    public void pt(){}


    @Around("pt()")
    public Object handleExceptionLog(ProceedingJoinPoint jp) throws Throwable {

        try {
            // 记录方法进入日志
            log.debug("{}方法准备调用，参数: {}", jp.getSignature(), Arrays.toString(jp.getArgs()));
            long a = System.currentTimeMillis();
            // 调用切点方法
            Object result = jp.proceed();
            // 记录方法结束日志
            log.debug("{}方法调用成功，执行耗时{}", jp.getSignature(), System.currentTimeMillis() - a);
            return result;
        } catch (Throwable throwable) {
            log.error("{}方法执行失败，原因：{}", jp.getSignature(), throwable.getMessage(), throwable);
            // 判断异常是否是LyException
            if(throwable instanceof LyException){
                // 如果是，不处理，直接抛
                throw throwable;
            }else{
                // 如果不是，转为LyException
                throw new LyException(500, throwable);
            }
        }


    }

}
