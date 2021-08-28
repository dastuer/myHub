package com.diao.myhub.controller;

import com.diao.myhub.cache.HotTagCache;
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
    @Autowired
    private HotTagCache hotTagCache;
    private QuestionService questionService;
    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }
    @GetMapping({"/","/index/{page}","/main","/index"})
    public String index(Model model, @RequestParam("search") @Nullable String search,@Nullable String tag, @PathVariable("page") @Nullable Integer page, HttpSession session){
        if (page==null||page<0){
            page = 1;
        }
        PaginationDTO paginationDTO = null;
        // 查询参数全部为空
        if (tag==null&&(search==null||search.trim().length()==0)){
            paginationDTO = questionService.getQuestions(null,null,page);
        // 查询参数只有tag为空,搜索关键字不为空
        }else if (tag == null){
            String[] keys = search.trim().split(" ");
            paginationDTO = questionService.getQuestions(null,keys,page);
        // 查询参数都不为空,或只有search为空,按照标签tag查询
        }else {
            paginationDTO=questionService.getQuestions(tag,null,page);
        }
        model.addAttribute("quez",paginationDTO);
        model.addAttribute("hotTags",hotTagCache.getHotTags());
        return "index";
    }
}
