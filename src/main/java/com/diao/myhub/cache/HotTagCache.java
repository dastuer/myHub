package com.diao.myhub.cache;

import com.diao.myhub.dto.HotTagDTO;
import com.diao.myhub.dto.TagDTO;
import com.diao.myhub.model.Question;
import com.diao.myhub.service.QuestionService;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Data
public class HotTagCache {

   // key未标签名,Integer[0]未热度,Integer[1]未问题数量
   private List<HotTagDTO> hotTags = new ArrayList<>();

   public PriorityQueue<HotTagDTO>  updateTags(Map<String,Integer[]> tagMap){
      int maxSize = 10;
      PriorityQueue<HotTagDTO> hotTagDTOS = new PriorityQueue<>(10);
      tagMap.forEach((k,v)->{
         HotTagDTO hotTagDTO = new HotTagDTO();
         hotTagDTO.setTagName(k);
         hotTagDTO.setPriority(v[0]);
         hotTagDTO.setQuestionCount(v[1]);
         hotTagDTO.setReplies(v[2]);
         if (hotTagDTOS.size()<maxSize){
            hotTagDTOS.add(hotTagDTO);
         }else {
            if(hotTagDTOS.peek().compareTo(hotTagDTO)<0){
               hotTagDTOS.poll();
               hotTagDTOS.add(hotTagDTO);
            }
         }
      });
      return hotTagDTOS;
   }

}
