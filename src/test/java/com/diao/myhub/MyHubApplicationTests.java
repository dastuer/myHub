package com.diao.myhub;

import com.diao.myhub.mapper.PublishMapper;
import com.diao.myhub.mapper.QuestionMapper;
import com.diao.myhub.schedule.HotTagTasks;
import com.diao.myhub.service.CommentService;
import com.diao.myhub.service.NotifyService;
import com.diao.myhub.service.QuestionService;
import com.diao.myhub.service.UserService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    @Autowired
    private HotTagTasks tagTasks;

    @Test
    void contextLoads() {
        tagTasks.hotTagSchedule();
        HashMap<String, String> hashMap = new HashMap<>();
        val strings = new ArrayList<String>(Arrays.asList("1", "2", "3"));
        System.out.println(strings);

    }





//        String tag = new Scanner(System.in).nextLine();
//        String tags = "\""+tag
//                +"\"";
//        System.out.println(tags.replace(" ", "\",\""));
    }


