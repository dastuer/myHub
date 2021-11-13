package com.diao.myhub.controller;

import com.alibaba.fastjson.JSON;
import com.diao.myhub.cache.HotTagCache;
import com.diao.myhub.cache.TagCache;
import com.diao.myhub.dto.CommentDTO;
import com.diao.myhub.dto.QuestionDTO;
import com.diao.myhub.enums.CommentTypeEnum;
import com.diao.myhub.enums.LikeStatusEnum;
import com.diao.myhub.enums.NotifyStatusEnum;
import com.diao.myhub.exception.CustomizeError;
import com.diao.myhub.exception.CustomizeException;
import com.diao.myhub.model.Question;
import com.diao.myhub.model.User;
import com.diao.myhub.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PublishService publishService;
    @Autowired
    private TagCache tagCache;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private UserService userService;
    @Autowired
    private HotTagCache hotTagCache;
    @Autowired
    private GreatService greatService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id, Long notify, Model model, HttpSession session){
        questionService.updateView(id);
        User user = (User) session.getAttribute("user");
        if (notify!=null){
            if (user==null){throw new CustomizeException(CustomizeError.NO_LOGIN);}
            Short status = notifyService.getNotifyById(notify).getStatus();
            if (status.equals(NotifyStatusEnum.UNREAD.getStatus())){
                notifyService.readOne(notify);
                userService.updateUnreadDe(user.getId());
            }
        }
        QuestionDTO question = questionService.getQuestionDTO(user,id);
        List<CommentDTO> comments = commentService.getCommentsByPid(user,id,CommentTypeEnum.COMMENT_TYPE_QUESTION);
        List<QuestionDTO> related = questionService.getQuestionsRelated(id);
        model.addAttribute("comments",comments);
        model.addAttribute("question",question);
        model.addAttribute("related",related);
        model.addAttribute("hotTags",hotTagCache.getHotTags());
        return "question";
    }
    @GetMapping("/question/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model,HttpServletRequest req){

        User user = (User)req.getSession().getAttribute("user");
        Question question = questionService.getQuestion(id);
        // 返回所有分组
        model.addAttribute("tagsList",tagCache.getTagCache());
        // 返回标签标签
        model.addAttribute("allTags", JSON.toJSON(tagCache.getAllTags()));
        model.addAttribute("question",question);
        return "question/edit";
    }
    @PostMapping("/question/edit/{id}")
    public String doEdit(@PathVariable("id")Long id, Question question, Model model, HttpServletRequest req){

        // =========参数为空判断==========
        if (question.getTag()==null||question.getTag().trim().length()==0||
            question.getTitle()==null||question.getTitle().trim().length()==0||
            question.getDescription()==null||question.getDescription().trim().length()==0){
            model.addAttribute("msg","内容不能为空");
            model.addAttribute("question",question);
            return "question/edit";
        }
        // =========用户权限判读=========
        User user = (User) req.getSession().getAttribute("user");
        Question localQuestion = questionService.getQuestion(id);
        if (!localQuestion.getCreator().equals(user.getId())){
            model.addAttribute("msg","权限不足,无法修改");
            model.addAttribute("question",question);
            return "question/edit";
        }
        //============标签验证==========
        if (!publishService.validate(question.getTag())){
            model.addAttribute("msg","标签错误,请检查标签是否重复或存在");
            model.addAttribute("question",question);
            return "question/edit";
        }
        //===========更新验证===========
        question.setId(id);
        question.setGmtModify(System.currentTimeMillis());
        if(questionService.updateQuestion(question)==0){
            model.addAttribute("msg","更新失败,服务器异常");
            model.addAttribute("question",question);
            return "question/edit";
        }
        model.addAttribute("msg","修改成功");
        return "success/success";
    }
    @GetMapping("/question/delete/{id}")
    public String deleteQuestion(@PathVariable("id") Long id,HttpSession session,Model model){
        Question question = questionService.getQuestion(id);
        User user=(User)session.getAttribute("user");
        if (question==null){
            throw new CustomizeException(CustomizeError.QUESTION_NOT_FOUND);
        }else if (user==null||!question.getCreator().equals(user.getId())){
            throw new CustomizeException(CustomizeError.NOT_ALLOWED);
        }else {
           int res =  questionService.delQuestionById(id);
           if (res==0){
               throw new CustomizeException(CustomizeError.SYS_ERROR);
           }
        }
        model.addAttribute("msg","删除成功!");
         return "success/success";
    }
}
