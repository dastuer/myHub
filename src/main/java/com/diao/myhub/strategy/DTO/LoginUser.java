package com.diao.myhub.strategy.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Huah
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {
    private String name;
    private String id;
    private String bio;
    private String avatar_url;
}
