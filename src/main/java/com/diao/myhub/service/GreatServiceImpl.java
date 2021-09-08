package com.diao.myhub.service;

import com.diao.myhub.enums.LikeStatusEnum;
import com.diao.myhub.enums.LikeTypeEnum;
import com.diao.myhub.mapper.GreatMapper;
import com.diao.myhub.model.Great;
import com.diao.myhub.model.GreatExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author Morty
 */
@Service
public  class GreatServiceImpl implements GreatService{
    @Autowired
    private GreatMapper greatMapper;

    @Override
    public LikeStatusEnum getIsLikeStatus(int userId, Long likeId, LikeTypeEnum typeEnum) {
        GreatExample greatExample = new GreatExample();
        greatExample.createCriteria().andUserIdEqualTo(userId).andLikeIdEqualTo(likeId).
                andTypeEqualTo(typeEnum.getType());
        List<Great> greats = greatMapper.selectByExample(greatExample);
        if (greats==null||greats.size()==0){
            return LikeStatusEnum.LIKE_STATUS_UNLIKE;
        }
        else {
            return greats.get(0).getStatus().equals(LikeStatusEnum.LIKE_STATUS_LIKED.getStatus())?
                    LikeStatusEnum.LIKE_STATUS_LIKED:LikeStatusEnum.LIKE_STATUS_UNLIKE;
        }


    }

    @Override
    public List<Great> getLikeRecord(Integer id, Short type, Long likeId) {
        GreatExample greatExample = new GreatExample();
        greatExample.createCriteria().andUserIdEqualTo(id).andLikeIdEqualTo(likeId).
                andTypeEqualTo(type);
        return greatMapper.selectByExample(greatExample);
    }

    @Override
    public int createRecord(Great great) {
       return greatMapper.insert(great);
    }

    @Override
    public int updateRecord(Great great) {
        GreatExample greatExample = new GreatExample();
        greatExample.createCriteria().andIdEqualTo(great.getId());
        return   greatMapper.updateByExample(great,greatExample);
    }

    @Override
    public long countByQuestionId(Long id) {
        GreatExample greatExample = new GreatExample();
        greatExample.createCriteria().andLikeIdEqualTo(id).andTypeEqualTo(LikeTypeEnum.LIKE_TYPE_QUESTION.getType()).
                andStatusEqualTo(LikeStatusEnum.LIKE_STATUS_LIKED.getStatus());
        return greatMapper.countByExample(greatExample);

    }
    @Override
    public long countByCommentId(Long id) {
        GreatExample greatExample = new GreatExample();
        greatExample.createCriteria().andLikeIdEqualTo(id).andTypeEqualTo(LikeTypeEnum.LIKE_TYPE_COMMENT.getType()).
                andStatusEqualTo(LikeStatusEnum.LIKE_STATUS_LIKED.getStatus());
        return greatMapper.countByExample(greatExample);
    }
}
