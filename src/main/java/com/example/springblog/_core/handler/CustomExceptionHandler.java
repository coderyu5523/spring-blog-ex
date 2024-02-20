package com.example.springblog._core.handler;

import com.example.springblog._core.util.Script;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice // 응답 에러 컨트롤러.(view==파일 리턴, 모든 쓰로우를 처리)
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class) // 모든 에러를 다 받음.
    public @ResponseBody String error1(Exception e){

        return Script.back(e.getMessage());
    }
}
