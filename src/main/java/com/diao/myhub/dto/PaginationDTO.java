package com.diao.myhub.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private boolean showPrevious;
    private boolean showFirst;
    private boolean showNext;
    private boolean showLast;
    private Integer page;
    private Integer pageCount;
    private List<Integer> pages = new ArrayList<>();

    public void setPagination(Integer qCount,Integer page,Integer size){
        pageCount = qCount/size;
        if (qCount%size!=0||qCount==0){
            pageCount = pageCount+1;
        }
        // 获取页码列表
        pages = getPageList(pageCount,page);
        // 包含首页时,不展示首页按钮
        showFirst =  (!pages.contains(1));
        // 首页时,不展示前页
        showPrevious = (page!=1);
        // 末页时,不展示后页
        showNext = (!page.equals(pageCount));
        // 包含末页,不展示末页按钮
        showLast = (!pages.contains(pageCount));
    }
    private List<Integer> getPageList(Integer pageCount,Integer page){
        LinkedList<Integer> pageList = new LinkedList<>();
        int index = page;
        for (int i = 0; i < 3; i++) {
            if (index>0){
                pageList.addFirst(index);
                --index;
            }else {
                break;
            }
        }
        index = page+1;
        for (int i = 0; i < 2; i++) {
            if (index<=pageCount){
                pageList.add(index);
                ++index;
            }else {
                break;
            }
        }
        return pageList;
    }
}
