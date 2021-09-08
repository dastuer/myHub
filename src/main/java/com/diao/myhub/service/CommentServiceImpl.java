package com.diao.myhub.service;


import com.diao.myhub.dto.CommentDTO;
import com.diao.myhub.dto.MyCommentsDTO;
import com.diao.myhub.enums.CommentTypeEnum;
import com.diao.myhub.enums.LikeStatusEnum;
import com.diao.myhub.enums.LikeTypeEnum;
import com.diao.myhub.exception.CustomizeError;
import com.diao.myhub.exception.CustomizeException;
import com.diao.myhub.mapper.CommentMapper;
import com.diao.myhub.mapper.UserMapper;
import com.diao.myhub.model.Comment;
import com.diao.myhub.model.CommentExample;
import com.diao.myhub.model.Question;
import com.diao.myhub.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;
    @Autowired
    private GreatService greatService;



    @Override
    public int addComment(Comment comment) {
        Short type = comment.getType();
        //  根据评论类型,插入评论,并更新问题评论数
        if (CommentTypeEnum.COMMENT_TYPE_QUESTION.getType().equals(type)){
            Question question = questionService.getQuestion(comment.getParentId());
            if (question==null){
                throw new CustomizeException(CustomizeError.QUESTION_NOT_FOUND);
            }
            questionService.updateComment(comment.getParentId());
        }
        else if (CommentTypeEnum.COMMENT_TYPE_COMMENT.getType().equals(type)){
            if(commentMapper.selectByPrimaryKey(comment.getParentId())==null){
                throw new CustomizeException(CustomizeError.COMMENT_NOT_FOUND); }
            Comment parentComment = commentMapper.getCommentById(comment.getParentId());
            questionService.updateComment(parentComment.getParentId());
        }
        else {
            throw new CustomizeException(CustomizeError.COMMENT_TYPE_ERROR);
        }
        int i= commentMapper.insertSelective(comment);
        if (i==0){
            // 评论失败返回1002
            throw new CustomizeException(CustomizeError.COMMENT_FIELD);
        }
        return i;
    }

    @Override
    public int getCommentCount(Long pid) {
        return commentMapper.getCommentCount(pid);
    }

    @Override
    public List<MyCommentsDTO> getMyCommentDTOSByCommenterId(Integer cid) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andCommenterEqualTo(cid);
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        List<MyCommentsDTO> myCommentsDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            MyCommentsDTO myCommentsDTO = new MyCommentsDTO();
            Integer commenter = comment.getCommenter();
            User userById = userService.getUserById(commenter);
            myCommentsDTO.setUser(userById);
            if (comment.getType().equals(CommentTypeEnum.COMMENT_TYPE_QUESTION.getType())) {
                Question question = questionService.getQuestionWithNull(comment.getParentId());
                myCommentsDTO.setQuestion(question);
            } else if (comment.getType().equals(CommentTypeEnum.COMMENT_TYPE_COMMENT.getType())) {
                Comment commentById = commentMapper.getCommentById(comment.getParentId());
                Question question = questionService.getQuestionWithNull(commentById.getParentId());
                myCommentsDTO.setQuestion(question);
            }
            BeanUtils.copyProperties(comment, myCommentsDTO);
            myCommentsDTOS.add(myCommentsDTO);
        }

        return myCommentsDTOS;
    }

    @Override
    public List<CommentDTO> getCommentsByPid(User user,Long qid,CommentTypeEnum typeEnum) {
        ArrayList<CommentDTO> commentDTOS = new ArrayList<>();
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().
                andParentIdEqualTo(qid).
                andTypeEqualTo(typeEnum.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments!=null){
            for (Comment comment : comments) {
                CommentDTO commentDTO = new CommentDTO();
                Integer commenter = comment.getCommenter();
                short likedStatus = LikeStatusEnum.LIKE_STATUS_UNLIKE.getStatus();
                if (user!=null){
                    likedStatus= greatService.getIsLikeStatus(user.getId(),comment.getId(), LikeTypeEnum.LIKE_TYPE_COMMENT).getStatus();
                }
                commentDTO.setLikedStatus(likedStatus);
                User commentator = userService.getUserById(commenter);
                long commentCount = commentMapper.getSubCommentCount(comment.getId());
                BeanUtils.copyProperties(comment,commentDTO);
                commentDTO.setSubCommentCount(commentCount);
                commentDTO.setUser(commentator);
                commentDTOS.add(commentDTO);
            }
        }
        return commentDTOS;
    }
    @Override
    public int getTotalSubCommentCount(List<CommentDTO> commentDTOS){
        int res = 0;
        for (CommentDTO commentDTO : commentDTOS) {
            res += commentDTO.getSubCommentCount();
        }
        return res;
    }

    @Override
    public int updateLikeInc(Long likeId) {
        return commentMapper.updateLikeInc(likeId);
    }

    @Override
    public int updateLikeDec(Long likeId) {
        return commentMapper.updateLikeDec(likeId);
    }

    @Override
    public int refreshLikeCount(Long likeId) {
      long  likeCount =  greatService.countByCommentId(likeId);
        Comment comment = new Comment();
        comment.setId(likeId);
        comment.setLikeCount(likeCount);
        return commentMapper.updateByPrimaryKeySelective(comment);
    }

    @Override
    public int delCommentById(Long id) {
        // 当前评论
        Comment comment = commentMapper.getCommentById(id);
        // 删除当前评论下的次级评论
        if (comment.getType().equals(CommentTypeEnum.COMMENT_TYPE_QUESTION.getType())){
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andTypeEqualTo(CommentTypeEnum.COMMENT_TYPE_COMMENT.getType()).
                andParentIdEqualTo(id);
            List<Comment> comments = commentMapper.selectByExample(commentExample);
            for (Comment comment1 : comments) {
                int n = commentMapper.deleteByPrimaryKey(comment1.getId());
                questionService.updateCommentDe(comment.getParentId());
                if (n==0){
                throw new CustomizeException(CustomizeError.SYS_ERROR);
                }
            }
            int res = commentMapper.deleteByPrimaryKey(id);
            Question questionWithNull = questionService.getQuestionWithNull(comment.getParentId());
            if (questionWithNull!=null){
                questionService.updateCommentDe(comment.getParentId());
            }
            if (res==0){
                throw new CustomizeException(CustomizeError.SYS_ERROR);
            }
            return res;
        }
        else if (comment.getType().equals(CommentTypeEnum.COMMENT_TYPE_COMMENT.getType())){
            // 获得父级评论
            Comment comment2 = commentMapper.selectByPrimaryKey(comment.getParentId());
            Question questionWithNull = questionService.getQuestionWithNull(comment2.getParentId());
            if (questionWithNull!=null){
                questionService.updateCommentDe(comment2.getParentId());
            }
            // 删除当前评论
            return commentMapper.deleteByPrimaryKey(id);
        }
        else {
            throw new CustomizeException(CustomizeError.COMMENT_TYPE_ERROR);
        }
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentMapper.selectByPrimaryKey(id);
    }
}
