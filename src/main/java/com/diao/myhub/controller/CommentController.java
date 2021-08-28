package com.diao.myhub.controller;

import com.alibaba.fastjson.JSON;
import com.diao.myhub.dto.CommentDTO;
import com.diao.myhub.enums.CommentTypeEnum;
import com.diao.myhub.enums.NotifyStatusEnum;
import com.diao.myhub.exception.CustomizeError;
import com.diao.myhub.exception.CustomizeException;
import com.diao.myhub.model.Comment;
import com.diao.myhub.model.Notify;
import com.diao.myhub.model.User;
import com.diao.myhub.service.CommentService;
import com.diao.myhub.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }
    @Autowired
    public void setNotifyService(NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    private NotifyService notifyService;
    private CommentService commentService;
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    @ResponseBody
    public String comment(@RequestBody Comment comment, HttpServletRequest req){
        System.out.println(comment);
        // 异步评论实现,返回JSON字符串
        HashMap<String, Integer> map = new HashMap<>();
        // 找到评论人
        User user = (User) req.getSession().getAttribute("user");
        if (user==null){
            throw new CustomizeException(CustomizeError.NO_LOGIN);
        }
        // 判断评论内容是否正确
        if (comment.getContent()==null||comment.getType()==null
                ||comment.getParentId()==null){
            throw new CustomizeException(CustomizeError.COMMENT_PARAMS_ERROR);
        }
        if (comment.getContent().trim().length()==0){
            throw new CustomizeException(CustomizeError.COMMENT_CONTENT_NULL);
        }
        // 设置评论时间以及修改时间,以及用户id
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModify(comment.getGmtCreate());
        comment.setCommenter(user.getId());
        // 评论提交数据库,并判断提交是否成功
        int i = commentService.addComment(comment);
        if (i==0){ throw new CustomizeException(CustomizeError.SYS_ERROR); }
        // 新增加通知
        notifyService.addNotify(comment);
        // 返回成功结果,交给前端处理
        map.put("code",200);
        return JSON.toJSONString(map);
    }
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String subComment(@PathVariable("id") Long id){
        List<CommentDTO> commentDTOS = commentService.getCommentsByPid(id, CommentTypeEnum.COMMENT_TYPE_COMMENT);
        return JSON.toJSONString(commentDTOS);
    }

}
