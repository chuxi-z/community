package com.firstpro.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> data;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;

    private Integer page;
    private Integer totalPage;

    private List<Integer> pages = new ArrayList<>();

//    public Integer getTotalPage() {
//        return totalPage;
//    }
//
//    public void setTotalPage(Integer totalPage) {
//        this.totalPage = totalPage;
//    }
//
//    public List<QuestionDTO> getQuestions() {
//        return questions;
//    }
//
//    public void setQuestions(List<QuestionDTO> questions) {
//        this.questions = questions;
//    }
//
//    public boolean isShowPrevious() {
//        return showPrevious;
//    }
//
//    public void setShowPrevious(boolean showPrevious) {
//        this.showPrevious = showPrevious;
//    }
//
//    public boolean isShowFirstPage() {
//        return showFirstPage;
//    }
//
//    public void setShowFirstPage(boolean showFirstPage) {
//        this.showFirstPage = showFirstPage;
//    }
//
//    public boolean isShowNext() {
//        return showNext;
//    }
//
//    public void setShowNext(boolean showNext) {
//        this.showNext = showNext;
//    }
//
//    public boolean isShowEndPage() {
//        return showEndPage;
//    }
//
//    public void setShowEndPage(boolean showEndPage) {
//        this.showEndPage = showEndPage;
//    }
//
//    public Integer getPage() {
//        return page;
//    }
//
//    public void setPage(Integer page) {
//        this.page = page;
//    }
//
//    public List<Integer> getPages() {
//        return pages;
//    }
//
//    public void setPages(List<Integer> pages) {
//        this.pages = pages;
//    }

    public void setPagination(Integer totalPage, Integer page) {
        this.totalPage = totalPage;
        this.page = page;
        this.pages.add(page);
        for(int i = 1; i <= 3; i++){
            if(page-i > 0){
                this.pages.add(0, page-i);
            }
            if(page+i <= totalPage){
                this.pages.add(page+i);
            }
        }

        if(page == 1){
            showPrevious = false;
        }
        else{
            showPrevious = true;
        }
        if(page == totalPage){
            showNext = false;
        }
        else{
            showNext = true;
        }

        if(pages.contains(1)){
            showFirstPage = false;
        }
        else{
            showFirstPage = true;
        }
        if(pages.contains(totalPage)){
            showEndPage = false;
        }
        else{
            showEndPage = true;
        }

    }
}
