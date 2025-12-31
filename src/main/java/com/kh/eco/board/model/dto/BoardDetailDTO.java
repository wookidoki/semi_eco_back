package com.kh.eco.board.model.dto;

import java.util.List;

import com.kh.eco.comment.model.dto.CommentDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class BoardDetailDTO extends BoardDTO {
    
    private List<CommentDTO> commentList;
    
}