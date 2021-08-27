package com.diao.myhub.mapper;

import com.diao.myhub.model.Question;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PublishMapper {
    int addQuestion(Question question);
}
