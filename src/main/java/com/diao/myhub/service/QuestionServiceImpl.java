package com.diao.myhub.service;

import com.diao.myhub.dto.PaginationDTO;
import com.diao.myhub.dto.QuestionDTO;
import com.diao.myhub.enums.LikeStatusEnum;
import com.diao.myhub.enums.LikeTypeEnum;
import com.diao.myhub.exception.CustomizeError;
import com.diao.myhub.exception.CustomizeException;
import com.diao.myhub.mapper.QuestionMapper;
import com.diao.myhub.mapper.UserMapper;
import com.diao.myhub.model.Question;
import com.diao.myhub.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService{
    private UserMapper userMapper;
    private QuestionMapper questionMapper;
    @Autowired
    private CommentService commentService;
    @Autowired
    private GreatService greatService;
    private static final Integer size = 12;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setQuestionMapper(QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }

    @Override
    public PaginationDTO getQuestions(String tag,String[] keys,Integer page) {
        Integer offset = (page-1)*size;
        Integer totalCount = questionMapper.count();
        // 页数超过最大页面数,抛出异常
        if (totalCount<offset){
            throw new CustomizeException(CustomizeError.PAGE_NOT_FOUND);
        }
        // 获取数据库问题列表
        List<Question> quez;
        // 获取问题总数
        Integer qCount;
        if (keys!=null&&keys.length>0){
           quez =  questionMapper.searchQuestions(keys,offset,size);
           qCount = questionMapper.searchQuestionsCount(keys);
        }else if(tag!=null){
            quez =  questionMapper.getQuestionsByTag(tag,offset,size);
            qCount = questionMapper.questionTagCount(tag);
        }
        else {
            quez = questionMapper.getQuestions(offset,size);
            qCount = questionMapper.count();
        }

        return getPaginationDTO(qCount,page,quez);
    }

    @Override
    public PaginationDTO getQuestionsById(Integer id, Integer page) {
        // 获取问题列表
        Integer offset = (page-1)*size;
        Integer totalCount = questionMapper.countById(id);
        // 页数超过最大页面数,抛出异常
        if (totalCount<offset){
            throw new CustomizeException(CustomizeError.PAGE_NOT_FOUND);
        }
        List<Question> quez = questionMapper.getQuestionsById(id,offset,size);
        // 获取问题总数
        Integer qCount = questionMapper.countById(id);
        return getPaginationDTO(qCount,page,quez);

    }

    @Override
    public QuestionDTO getQuestionDTO(User user,Long id) {
        LikeStatusEnum isLikeStatus = LikeStatusEnum.LIKE_STATUS_UNLIKE;
        if (user!=null){
            isLikeStatus = greatService.getIsLikeStatus(user.getId(), id, LikeTypeEnum.LIKE_TYPE_QUESTION);
        }
        Question question = getQuestion(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User creator = userMapper.getUserById(question.getCreator());
        questionDTO.setUser(creator);
        questionDTO.setIsLike(isLikeStatus.getStatus());
        return questionDTO;
    }

    @Override
    public Question getQuestion(Long id) {
        Question question = questionMapper.getQuestion(id);
        if (question==null){
            throw new CustomizeException(CustomizeError.QUESTION_NOT_FOUND);
        }else {
            return question;
        }
    }

    @Override
    public Question getQuestionWithNull(Long id) {
        return questionMapper.getQuestion(id);
    }

    @Override
    public int updateQuestion(Question question) {
        return questionMapper.updateQuestion(question);
    }

    @Override
    public int updateView(Long id) {
        return questionMapper.updateView(id);
    }

    @Override
    public int updateComment(Long id) {
        return questionMapper.updateComment(id);
    }

    @Override
    public int updateCommentDe(Long id) {
        int res = questionMapper.updateCommentDe(id);
        if (res==0){
           throw  new CustomizeException(CustomizeError.SYS_ERROR);
        }
        return res;
    }
    @Override
    public PaginationDTO getPaginationDTO(Integer qCount,Integer page,List<Question> quez){
        // 页面数据,包括问题数,分页等信息
        PaginationDTO paginationDTO = new PaginationDTO();
        // 设置当前页
        paginationDTO.setPage(page);
        List<QuestionDTO> questions = new ArrayList<>();
        for (Question q : quez) {
            QuestionDTO question = new QuestionDTO();
            BeanUtils.copyProperties(q,question);
            User user = userMapper.getUserById(q.getCreator());
            question.setUser(user);
            questions.add(question);
        }
        paginationDTO.setQuestions(questions);
        // 设置分页信息
        paginationDTO.setPagination(qCount,page,size);
        return paginationDTO;
    }

    @Override
    public List<QuestionDTO> getQuestionsRelated(Long id){
        Question question = getQuestion(id);
        String tag = question.getTag();
        String[] tags = tag.trim().split(",");
        List<Question> related = questionMapper.getQuestionsRelated(id, tags);
        return related.stream().map
                (q -> { QuestionDTO questionDTO = new QuestionDTO();
                    BeanUtils.copyProperties(q,questionDTO);
                    return questionDTO; }).
                collect(Collectors.toList());
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionMapper.getAllQuestions();
    }

    @Override
    public List<Question> getRecentQuestions(Long time,int offset,int limit) {
        return questionMapper.getRecentQuestions(time,offset,limit);
    }

    @Override
    public int updateLikeInc(Long likeId) {
        return questionMapper.updateLikeInc(likeId);
    }

    @Override
    public int updateLikeDec(Long likeId) {
        return questionMapper.updateLikeDec(likeId);
    }

    @Override
    public int refreshLikeCount(Long id) {
        long questionLikeCount = greatService.countByQuestionId(id);
        return questionMapper.updateLikeCount(id,questionLikeCount);
    }

    @Override
    public int delQuestionById(Long id) {
       return questionMapper.delQuestionById(id);


    }


}
