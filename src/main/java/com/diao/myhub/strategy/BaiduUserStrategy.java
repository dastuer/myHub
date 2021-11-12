package com.diao.myhub.strategy;

import com.diao.myhub.dto.AccessTokenDTO;
import com.diao.myhub.dto.BaiduUser;
import com.diao.myhub.dto.GiteeUser;
import com.diao.myhub.provider.BaiduProvider;
import com.diao.myhub.provider.GiteeProvider;
import com.diao.myhub.strategy.DTO.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Huah
 */
@Component
@PropertySource("classpath:private.properties")
public class BaiduUserStrategy implements UserStrategy{
    private final String type = "baidu";
    @Autowired
    private BaiduProvider provider;
    @Value("${baidu.client.grant_type}")
    private String grant_type;
    @Value("${baidu.client.client_id}")
    private String client_id;
    @Value("${baidu.client.client_secret}")
    private String client_secret;
    @Value("${baidu.client.redirect_uri}")
    private String redirect_uri;
    @Value("${baidu.client.get_code}")
    private String get_code;
    @Value("${baidu.client.response_type}")
    private String response_type;
    @Value("${baidu.client.header_prefix}")
    private String header_prefix;
    @Override
    public LoginUser getLoginUser(AccessTokenDTO accessTokenDTO) throws Exception {
        accessTokenDTO.setClient_secret(client_secret);
        accessTokenDTO.setGrant_type(grant_type);
        accessTokenDTO.setRedirect_uri(redirect_uri);
        accessTokenDTO.setClient_id(client_id);
        String accessToken = provider.getAccessToken(accessTokenDTO);
        BaiduUser user = provider.getUser(accessToken);
        LoginUser loginUser = new LoginUser();
        loginUser.setName(user.getUsername());
        loginUser.setId(user.getOpenid());
        loginUser.setBio(user.getBirthday());
        loginUser.setAvatar_url(header_prefix+user.getPortrait());
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
