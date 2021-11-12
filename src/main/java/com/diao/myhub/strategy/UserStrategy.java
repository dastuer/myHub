package com.diao.myhub.strategy;

import com.diao.myhub.dto.AccessTokenDTO;
import com.diao.myhub.strategy.DTO.LoginUser;

/**
 * @author Huah
 */

public interface UserStrategy {
    LoginUser getLoginUser(AccessTokenDTO accessTokenDTO) throws Exception;
    String getType();
    String getRedirectUri();
}
