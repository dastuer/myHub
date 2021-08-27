package com.diao.myhub.exception;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.OnError;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ModelAndView myExceptionHandler(Throwable e, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ModelAndView mv = new ModelAndView();
//        System.out.println(req.getContentType());
        if ("application/json".equals(req.getContentType())) {
            HashMap<String, Integer> code = new HashMap<>();
            if (e instanceof CustomizeException) {
                code.put("code",((CustomizeException) e).getCode());
            } else {
                code.put("code",new CustomizeException(CustomizeError.SYS_ERROR).getCode());
            }
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSON.toJSONString(code));
            writer.flush();
            writer.close();
        } else {
            if (e instanceof CustomizeException) {
                String eMessage = e.getMessage();
                mv.addObject("message", eMessage);
            } else {
                mv.addObject("message", CustomizeError.SYS_ERROR.getMsg());
            }
            mv.setViewName("error");
        }
        return mv;
    }
}
