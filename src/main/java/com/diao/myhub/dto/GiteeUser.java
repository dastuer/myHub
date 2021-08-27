package com.diao.myhub.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class GiteeUser {
    private String name;
    private Long id;
    private String bio;
    private String avatar_url;
}
