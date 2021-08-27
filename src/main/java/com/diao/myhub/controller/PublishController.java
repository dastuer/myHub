package com.diao.myhub.controller;

import com.alibaba.fastjson.JSON;
import com.diao.myhub.cache.TagCache;
import com.diao.myhub.exception.CustomizeError;
import com.diao.myhub.exception.CustomizeException;
import com.diao.myhub.model.Question;
import com.diao.myhub.model.User;
import com.diao.myhub.service.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class PublishController {

    @Autowired
    public void setPublishService(PublishService publishService) {
        this.publishService = publishService;
    }
    private PublishService publishService;
    @Autowired
    private TagCache tagCache;
    @GetMapping("/publish")
    public String publish(HttpServletRequest req, Model model){
        User user = (User)req.getSession().getAttribute("user");
        if (user==null){
            throw new CustomizeException(CustomizeError.NO_LOGIN);
        }
        // 返回所有分组
        model.addAttribute("tagsList",tagCache.getTagCache());
        // 返回标签标签
        model.addAttribute("allTags", JSON.toJSON(tagCache.getAllTags()));
        model.addAttribute("creator",user.getId());
        return "publish";
    }
    @PostMapping("/publish")
    public String doPublish(Question question, Model model,HttpServletRequest req){
        User user = (User) req.getSession().getAttribute("user");
        if (user==null) {
            throw new CustomizeException(CustomizeError.NO_LOGIN);
        }
        question.setCreator(user.getId());
        model.addAttribute("question",question);

        if (question.getCreator()==null||question.getTitle()==null
                ||question.getDescription()==null) {
            model.addAttribute("msg","不能有空");
            return "publish";
        }
        if (!publishService.validate(question.getTag())){
            model.addAttribute("msg","标签错误,请检查标签是否重复或存在");
            return "publish";
        }
        question.setGmtCreate(new Date().getTime());
        question.setGmtModify(question.getGmtCreate());
        int res = publishService.addQuestion(question);
        if (res==0){
            model.addAttribute("msg","发布失败,服务器异常");
            return "publish";
        }
        model.addAttribute("msg","发布成功");
        return "success/success";
    }

}
