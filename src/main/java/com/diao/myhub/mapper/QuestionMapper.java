package com.diao.myhub.mapper;

import com.diao.myhub.model.Question;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface QuestionMapper {
    List<Question> getQuestions(Integer offset, Integer size);
    List<Question> searchQuestions(String[] keys,Integer offset, Integer size);
    int searchQuestionsCount(String[] keys);
    @Select("select count(1) from myhub.question")
    Integer count();
    @Select("select count(1) from myhub.question where creator=#{id}")
    Integer countById(Integer id);
    List<Question> getQuestionsById(Integer id, Integer offset, Integer size);
    @Select("select * from myhub.question where id=#{id}")
    Question getQuestion(Long id);
    int updateQuestion(Question question);
    @Update("update myhub.question set view_count = view_count + 1 where id=#{id}")
    int updateView(Long id);
    @Update("update myhub.question set comment_count = comment_count + 1 where id=#{id}")
    int updateComment(Long id);
    @Update("update myhub.question set comment_count = comment_count - 1 where id=#{id}")
    int updateCommentDe(Long id);
    List<Question> getQuestionsRelated(long id,String[] tags);
    @Select("select * from myhub.question")
    List<Question> getAllQuestions();
    @Select("select * from myhub.question where gmt_modify >= #{time} limit #{offset},#{limit}")
    List<Question> getRecentQuestions(Long time, int offset, int limit);
    List<Question> getQuestionsByTag(String tag, Integer offset, Integer size);
    Integer questionTagCount(String tag);
    @Update(("update myhub.question set like_count = like_count+1 where id=#{likeId}"))
    int updateLikeInc(Long likeId);
    @Update(("update myhub.question set like_count = like_count-1 where id=#{likeId}"))
    int updateLikeDec(Long likeId);
    @Update("update myhub.question set like_count = #{questionLikeCount} where id = #{id}")
    int updateLikeCount(Long id, long questionLikeCount);
    @Delete("delete  from myhub.question where id = #{id}")
    int delQuestionById(Long id);
    int addQuestion(Question question);
}
