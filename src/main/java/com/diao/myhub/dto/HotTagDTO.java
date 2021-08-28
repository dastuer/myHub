package com.diao.myhub.dto;

import lombok.Data;

@Data
public class HotTagDTO implements Comparable{
    private String tagName;
    private Integer priority;
    private Integer questionCount;
    private Integer replies;

    @Override
    public int compareTo(Object o) {
        return this.priority-((HotTagDTO)o).getPriority();
    }
}
