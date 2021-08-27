package com.diao.myhub.service;

import com.diao.myhub.model.Question;

public interface PublishService {
    int addQuestion(Question question);
    boolean validate(String tags);
}
