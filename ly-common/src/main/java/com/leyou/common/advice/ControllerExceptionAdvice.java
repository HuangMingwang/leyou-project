package com.leyou.common.advice;

import com.leyou.common.exception.LyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 拦截controller层下的运行时异常
 * @author Huang Mingwang
 * @create 2021-05-20 1:56 下午
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionAdvice {
    @ExceptionHandler(LyException.class)
    public ResponseEntity<String> handleLyException(LyException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }
}
