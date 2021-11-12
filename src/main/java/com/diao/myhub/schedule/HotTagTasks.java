package com.diao.myhub.schedule;

import com.diao.myhub.cache.HotTagCache;
import com.diao.myhub.cache.TagCache;
import com.diao.myhub.dto.HotTagDTO;
import com.diao.myhub.model.Question;
import com.diao.myhub.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
public class HotTagTasks {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private HotTagCache hotTagCache;

    @Scheduled(fixedRate = 1800000)
    public void hotTagSchedule(){
        log.info("hot tag flush task start {}",new Date());
        // 获取20年之内的所有问题
        long recentTime = 630_720_000_000L;
        long now = System.currentTimeMillis();
        List<Question> recentQuestions = new ArrayList<>();
        HashMap<String, Integer[]> hotTagMap = new HashMap<>();
        int offset = 0;
        int limit = 20;
        while (offset==0|| limit ==recentQuestions.size()) {
            recentQuestions = questionService.getRecentQuestions(now - recentTime,offset, limit);
            for (Question recentQuestion : recentQuestions) {
                String[] tag = recentQuestion.getTag().trim().split(",");
                for (String t : tag) {
                    if (hotTagMap.get(t) != null) {
                        // 标签热度表不为空,更新该标签热度
                        // 热度计算:5*出现的标签数+标签所在问题的评论数
                        Integer[] res = {(hotTagMap.get(t)[0] + 5 + recentQuestion.getCommentCount()),
                                hotTagMap.get(t)[1]+1,hotTagMap.get(t)[2]+recentQuestion.getCommentCount()};
                        hotTagMap.put(t,res);
                    } else {
                        Integer[] res = {5 + recentQuestion.getCommentCount(),1,recentQuestion.getCommentCount()};
                        hotTagMap.put(t, res);
                    }
                }
            }
            offset = offset + limit;
        }
        List<HotTagDTO> hotTagDTOList = new ArrayList<>();
        PriorityQueue<HotTagDTO> hotTagDTOS = hotTagCache.updateTags(hotTagMap);
        while (hotTagDTOS.peek()!=null){
            HotTagDTO poll = hotTagDTOS.poll();
            hotTagDTOList.add(poll);
        }
        Collections.reverse(hotTagDTOList);
        hotTagCache.setHotTags(hotTagDTOList);
        log.info("hot tag flush task end {}",new Date());
        log.info("hot tag list {}",hotTagDTOList);
    }

}
