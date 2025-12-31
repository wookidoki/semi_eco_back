package com.kh.eco.admin.model.dto;

import lombok.Data;

@Data
public class NoticeRequestDTO {
    private Long boardNo;
    private Long refMno;
    private String boardTitle;
    private String boardContent;
    private String boardCategory;
}