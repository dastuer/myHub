package com.diao.myhub.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.diao.myhub.dto.AccessTokenDTO;
import com.diao.myhub.dto.BaiduUser;
import com.diao.myhub.dto.GiteeUser;
import com.diao.myhub.exception.CustomizeError;
import com.diao.myhub.exception.CustomizeException;
import okhttp3.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

/**
 * @author Huah
 */
@Component
@PropertySource("classpath:private.properties")
public class BaiduProvider {
    @Value("${baidu.client.get_token}")
    private String getToken;
    @Value("${baidu.client.get_user}")
    private String getUser;
// ================= 获取token ================
    public String getAccessToken(AccessTokenDTO accessTokenDTO) throws Exception {
        OkHttpClient client = new OkHttpClient();

        String uri = getToken+
                "?code="+accessTokenDTO.getCode()+
                "&client_id="+accessTokenDTO.getClient_id()+
                "&client_secret="+accessTokenDTO.getClient_secret()+
                "&grant_type="+accessTokenDTO.getGrant_type()+
                "&redirect_uri="+accessTokenDTO.getRedirect_uri();
        // 百度获取access_token方式为get
        Request request = new Request.Builder()
                .url(uri)
                .get()
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
    public BaiduUser getUser(String accessToken) throws IOException {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getUser + accessToken)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()){
                throw new CustomizeException(CustomizeError.SYS_ERROR);
            }
            String resp = response.body().string();
            return JSON.parseObject(resp, BaiduUser.class);
        }
    }
}


