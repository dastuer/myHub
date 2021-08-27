package com.diao.myhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class ErrorController {
    @GetMapping("/error/loginError")
    public String loginError(){
        return "loginError";
    }
    @GetMapping("/error/innerError")
    public String publishError(){
        return "innerError";
    }
}
