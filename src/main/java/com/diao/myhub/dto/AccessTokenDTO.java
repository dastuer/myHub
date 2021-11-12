package com.diao.myhub.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Huah
 */
@Data
public class AccessTokenDTO {
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    // 请求传入
    private String code;
// https://gitee.com/oauth/token?grant_type=authorization_code&code=
// {code}&client_id={client_id}&redirect_uri={redirect_uri}&client_secret={client_secret}
}
