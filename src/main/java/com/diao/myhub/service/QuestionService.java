package com.diao.myhub.service;

import com.diao.myhub.dto.PaginationDTO;
import com.diao.myhub.dto.QuestionDTO;
import com.diao.myhub.model.Question;
import com.diao.myhub.model.User;

import java.util.List;

public interface QuestionService {
    PaginationDTO getPaginationDTO(Integer qCount, Integer page, List<Question> quez);
    PaginationDTO getQuestions(String tag, String[] keys, Integer page);
    PaginationDTO getQuestionsById(Integer id, Integer page);
    QuestionDTO getQuestionDTO(User user,Long id);
    Question getQuestion(Long id);
    Question getQuestionWithNull(Long id);
    int updateQuestion(Question question);
    int updateView(Long id);
    int updateComment(Long id);
    int updateCommentDe(Long id);
    List<QuestionDTO> getQuestionsRelated(Long id);
    List<Question> getAllQuestions();
    List<Question> getRecentQuestions(Long time,int offset,int limit);
    int addQuestion(Question question);

    int updateLikeInc(Long likeId);

    int updateLikeDec(Long likeId);

    int refreshLikeCount(Long id);

    int delQuestionById(Long id);
}
