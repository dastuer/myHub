package com.diao.myhub.dto;

import lombok.Data;

import java.util.List;
@Data
public class TagDTO {
    int categoryId;
    private String categoryName;
    private List<String> tags;
}
