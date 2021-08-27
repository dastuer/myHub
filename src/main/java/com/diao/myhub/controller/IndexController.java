package com.diao.myhub.controller;

import com.diao.myhub.dto.PaginationDTO;
import com.diao.myhub.model.User;
import com.diao.myhub.service.NotifyService;
import com.diao.myhub.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    @Autowired
    private NotifyService notifyService;
    private QuestionService questionService;
    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }
    @GetMapping({"/","/index/{page}","/main","/index"})
    public String index(Model model, @RequestParam("search") @Nullable String search, @PathVariable("page") @Nullable Integer page, HttpSession session){
        if (page==null||page<0){
            page = 1;
        }
        PaginationDTO paginationDTO = null;
        if (search==null||search.trim().length()==0){
            paginationDTO = questionService.getQuestions(null,page);
        }else {
            // 搜索关键字
            String[] keys = search.trim().split(" ");
            paginationDTO = questionService.getQuestions(keys,page);
        }
        model.addAttribute("quez",paginationDTO);
        return "index";
    }
}
