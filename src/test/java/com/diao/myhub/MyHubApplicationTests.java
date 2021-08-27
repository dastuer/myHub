package com.diao.myhub;

import com.diao.myhub.mapper.PublishMapper;
import com.diao.myhub.mapper.QuestionMapper;
import com.diao.myhub.service.CommentService;
import com.diao.myhub.service.NotifyService;
import com.diao.myhub.service.QuestionService;
import com.diao.myhub.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Scanner;

@SpringBootTest
class MyHubApplicationTests {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private PublishMapper publishMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private NotifyService notifyService;

    @Test
    void contextLoads() {
//        System.out.println(commentService.getMyCommentDTOSByCommenterId(23).size());
//        List<Question> questions = questionMapper.getQuestions(1,5);
//        for (int i = 0; i < 5; i++) {
//            for (Question question : questions) {
//                publishMapper.addQuestion(question);
//            }
//        }
//        List<CommentDTO> commentsByQuestion = commentService.getCommentsByQuestion((long) 41);
//        System.out.println(commentsByQuestion);
        System.out.println(notifyService.getNotifyDTOS(23).size());

    }





//        String tag = new Scanner(System.in).nextLine();
//        String tags = "\""+tag
//                +"\"";
//        System.out.println(tags.replace(" ", "\",\""));
    }


