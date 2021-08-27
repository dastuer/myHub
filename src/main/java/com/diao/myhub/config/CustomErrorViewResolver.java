package com.diao.myhub.config;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
@Component
public class CustomErrorViewResolver implements ErrorViewResolver {
    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        ModelAndView mv = new ModelAndView();
        String message = null;
        if (status.is4xxClientError()){
            message  = "你这个请求错了吧，要不然换个姿势？";
        }
        else {
            message  = "服务器错误,请稍后再试!";
        }
        mv.addObject("message",message );
        mv.setViewName("error");
        return mv;
    }
}
