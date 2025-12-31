package com.kh.eco.admin.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageResponse {
    private List<AdminBoardDTO> content;
    private int totalCount;
}
