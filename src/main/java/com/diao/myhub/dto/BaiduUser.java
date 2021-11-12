package com.diao.myhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaiduUser {
    private String portrait;
    private String username;
    private String openid;
    private String birthday;
    private Short sex;

}
