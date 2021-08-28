package com.diao.myhub.service;

import com.diao.myhub.dto.PaginationDTO;
import com.diao.myhub.dto.QuestionDTO;
import com.diao.myhub.model.Question;
import com.diao.myhub.model.User;

import java.util.List;

public interface QuestionService {
    public PaginationDTO getQuestions(String tag ,String[] keys,Integer page);
    public PaginationDTO getQuestionsById(Integer id,Integer page);
    QuestionDTO getQuestionDTO(User user,Long id);
    Question getQuestion(Long id);
    int updateQuestion(Question question);
    int updateView(Long id);
    int updateComment(Long id);
    int updateCommentDe(Long id);
    List<QuestionDTO> getQuestionsRelated(Long id);
    List<Question> getAllQuestions();
    List<Question> getRecentQuestions(Long time,int offset,int limit);

    int updateLikeInc(Long likeId);

    int updateLikeDec(Long likeId);

}
