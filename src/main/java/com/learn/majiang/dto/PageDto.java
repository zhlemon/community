package com.learn.majiang.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDto {
    private List<QuestionDto> questions;
    //向前按钮
    private boolean showPrevious;
    //第一页按钮
    private boolean showFirstPage;
    //后一个
    private boolean showNext;
    //最后一页按钮
    private boolean showEndPage;
    //当前页面
    private Integer currentPage;
    //页面展示哪几页
    private List<Integer> pages = new ArrayList<>();

    private Integer totalPage;

    /**
     * @param totalCount 记录总数
     * @param page       请求的页码 /page
     * @param size       每页大小
     */
    public void setPageInfo(Integer totalCount, Integer page, Integer size) {

        Integer totalPage;
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        this.totalPage = totalPage;
        this.currentPage = page;

        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                //往前是头部插入
                pages.add(0, page - i);
            }

            if (page + i <= totalPage) {
                //往后是尾部插入
                pages.add(page + i);
            }

        }
        //  是否展示上一页
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }

        //是否展示下一页
        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }

        //是否展示第一页
        //如果下面的页码有1 就不展示第一页的按钮 如果没有第一页按钮就展示
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }

        //是否展示最后一页 如果包含最后一页就不展示最后一页的按钮 如果没有就展示
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }


}
