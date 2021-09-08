package com.diao.myhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * @author Morty
 */
@Controller
public class ErrorController {
    @GetMapping("/loginError")
    public String loginError(){
        return "loginError";
    }
    @GetMapping("/innerError")
    public String publishError(){
        return "innerError";
    }
}
