package com.diao.myhub.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.diao.myhub.dto.AccessTokenDTO;
import com.diao.myhub.dto.GiteeToken;
import com.diao.myhub.dto.GiteeUser;
import com.diao.myhub.exception.CustomizeError;
import com.diao.myhub.exception.CustomizeException;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Huah
 */
@Component
@PropertySource("classpath:private.properties")
public class GiteeProvider {
    @Value("${gitee.client.get_token}")
    private String getToken;
    @Value("${gitee.client.get_user}")
    private String getUser;
// ================= 获取token ================
    public String getAccessToken(AccessTokenDTO accessTokenDTO) throws Exception {
        final MediaType json = MediaType.get("application/json; charset=utf-8");
        String params = JSON.toJSONString(accessTokenDTO);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, params);
        Request request = new Request.Builder()
                .url(getToken)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()){
            if (!response.isSuccessful()){
                throw new CustomizeException(CustomizeError.SYS_ERROR);
            }
            String s = response.body().string();
            JSONObject jsonObject = JSON.parseObject(s);
            return jsonObject.getString("access_token");
        }
    }
// ================= 获取用户信息 ================
    public GiteeUser getUser(String accessToken) throws IOException {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getUser + accessToken)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()){
                throw new CustomizeException(CustomizeError.SYS_ERROR);
            }
            String resp = response.body().string();
            return JSON.parseObject(resp, GiteeUser.class);
        }
    }
}


