package com.diao.myhub.strategy;

import com.diao.myhub.dto.AccessTokenDTO;
import com.diao.myhub.dto.GiteeUser;
import com.diao.myhub.provider.GiteeProvider;
import com.diao.myhub.strategy.DTO.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Huah
 */
@Component
@PropertySource("classpath:private.properties")
public class GiteeUserStrategy implements UserStrategy{
    private final String type = "gitee";
    @Autowired
    private GiteeProvider provider;
    @Value("${gitee.client.grant_type}")
    private String grant_type;
    @Value("${gitee.client.client_id}")
    private String client_id;
    @Value("${gitee.client.client_secret}")
    private String client_secret;
    @Value("${gitee.client.redirect_uri}")
    private String redirect_uri;
    @Value("${gitee.client.get_code}")
    private String get_code;
    @Value("${gitee.client.response_type}")
    private String response_type;
    @Override
    public LoginUser getLoginUser(AccessTokenDTO accessTokenDTO) throws Exception {
        accessTokenDTO.setClient_secret(client_secret);
        accessTokenDTO.setGrant_type(grant_type);
        accessTokenDTO.setRedirect_uri(redirect_uri);
        accessTokenDTO.setClient_id(client_id);
        String accessToken = provider.getAccessToken(accessTokenDTO);
        GiteeUser user = provider.getUser(accessToken);
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(user,loginUser);
        return loginUser;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getRedirectUri() {
        return get_code + "?" +"client_id="+
                client_id + "&" +"redirect_uri="+
                redirect_uri + "&" +"response_type="+
                response_type;
    }
}
