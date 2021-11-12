package com.diao.myhub.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Huah
 */
@Component
public class UserLoginFactory {
    @Autowired
    private List<UserStrategy> userStrategies;
    public UserStrategy getLoginStrategy(String type){
        for (UserStrategy strategy : userStrategies) {
            if (type!=null){
                if (type.equals(strategy.getType())){
                    return strategy;
                }
            }
        }
        return null;
    }
}
