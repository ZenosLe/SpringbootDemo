package com.zeniusLe.demo1.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandle {
    @ExceptionHandler(value = RuntimeException.class) // khi có 1 kiểu runtime exception xảy ra thì nó sẽ tập trung về đây
    ResponseEntity<String> handleException(RuntimeException exception){
        return ResponseEntity.badRequest().body(exception.getMessage()); // trả về nội dung muốn cần
    }
}
