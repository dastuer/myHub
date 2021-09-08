package com.diao.myhub.service;

import com.diao.myhub.cache.TagCache;
import com.diao.myhub.mapper.PublishMapper;
import com.diao.myhub.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PublishServiceImpl implements PublishService{
    @Autowired
    public void setMapper(PublishMapper mapper) {
        this.mapper = mapper;
    }
    @Autowired
    private TagCache tagCache;
    private PublishMapper mapper;
    @Override
    public int addQuestion(Question question) {
     return mapper.addQuestion(question);
    }
    @Override
    public boolean validate(String tags){
        String[] tag = tags.split(",");
        if (tag.length>4){
            return false;
        }
        Set<String> allTags = tagCache.getAllTags();
        HashSet<String> tagSet = new HashSet<>();
        for (String t : tag) {
            if (tagSet.contains(t)||!allTags.contains(t)){
                return false;
            }else {
                tagSet.add(t);
            }
        }
        return true;
    }
}
