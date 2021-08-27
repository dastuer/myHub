package com.diao.myhub.controller;

import com.diao.myhub.dto.MyCommentsDTO;
import com.diao.myhub.dto.NotifyDTO;
import com.diao.myhub.dto.PaginationDTO;
import com.diao.myhub.enums.NotifyStatusEnum;
import com.diao.myhub.exception.CustomizeError;
import com.diao.myhub.exception.CustomizeException;
import com.diao.myhub.model.Comment;
import com.diao.myhub.model.User;
import com.diao.myhub.service.CommentService;
import com.diao.myhub.service.NotifyService;
import com.diao.myhub.service.QuestionService;
import com.diao.myhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private UserService userService;


    @GetMapping("/profile/{actions}")
    public String profile(@PathVariable(name = "actions") String action,String readAll, Model model,  HttpSession session,Integer page){
        User user = (User) session.getAttribute("user");
        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
            if (page==null||page<0){
                page=1;
            }
            PaginationDTO paginationDTO = questionService.getQuestionsById(user.getId(), page);
            model.addAttribute("quez",paginationDTO);
        }
        if ("replies".equals(action)){
            List<MyCommentsDTO> myCommentDTOSByCommenterId = commentService.getMyCommentDTOSByCommenterId(user.getId());
            model.addAttribute("myComments",myCommentDTOSByCommenterId);
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","我的回复");
        }
        if ("message".equals(action)){
            List<NotifyDTO> notifyDTOS = notifyService.getNotifyDTOS(user.getId());
            model.addAttribute("notifies",notifyDTOS);
            model.addAttribute("section","message");
            model.addAttribute("sectionName","我的消息");
            if (readAll!=null){
                notifyService.readMsg(user.getId());
                userService.readAll(user.getId());
            }
        }
        return "profile";
    }
    @GetMapping("/delReplies/{id}")
    public String delReplies(@PathVariable("id") String id){
        commentService.delCommentById(Long.valueOf(id));
        return "redirect:/profile/replies";
    }
    @GetMapping("/delMessage/{id}")
    public String delMessage(@PathVariable("id")Long id,HttpSession session){
        if(notifyService.getNotifyById(id).
                getStatus().equals(NotifyStatusEnum.UNREAD.getStatus())){
            User user = (User)session.getAttribute("user");
            if (user==null){throw  new CustomizeException(CustomizeError.NO_LOGIN); }
            userService.updateUnreadDe(user.getId());
        }
        notifyService.delNotify(id);
        return "redirect:/profile/message";
    }
}
