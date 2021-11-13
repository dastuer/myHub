package com.diao.myhub.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author Huah
 */
@Data
public class GiteeUser {
    private String name;
    private String id;
    private String bio;
    private String avatar_url;
}
