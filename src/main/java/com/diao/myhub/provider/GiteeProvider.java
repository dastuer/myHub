package com.diao.myhub.provider;

import com.alibaba.fastjson.JSON;
import com.diao.myhub.dto.AccessTokenDTO;
import com.diao.myhub.dto.GiteeToken;
import com.diao.myhub.dto.GiteeUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GiteeProvider {

// ================= 获取token ================
    public String getAccessToken(AccessTokenDTO accessTokenDTO) throws Exception {
        final MediaType json = MediaType.get("application/json; charset=utf-8");
        String params = JSON.toJSONString(accessTokenDTO);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, params);
        Request request = new Request.Builder()
                .url("https://gitee.com/oauth/token")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String s = response.body().string();
        GiteeToken giteeToken = JSON.parseObject(s, GiteeToken.class);
        String token = giteeToken.getAccess_token();
        return token;
    }
// ================= 获取用户信息 ================
    public GiteeUser getUser(String accessToken) throws IOException {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://gitee.com/api/v5/user?access_token=" + accessToken)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            String body = response.body().string();
            GiteeUser user = JSON.parseObject(body, GiteeUser.class);
            return user;
        }
    }
}


