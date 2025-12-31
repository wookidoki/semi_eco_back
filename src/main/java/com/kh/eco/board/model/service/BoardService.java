package com.kh.eco.board.model.service;

import java.util.Map;
import org.springframework.web.multipart.MultipartFile;
import com.kh.eco.board.model.dto.BoardDTO;
import com.kh.eco.board.model.dto.BoardDetailDTO;
import jakarta.validation.Valid;

public interface BoardService {


    Map<String, Object> selectBoardList(Map<String, Object> map);

    BoardDetailDTO selectBoardDetail(Long boardNo);

    /**
     * 게시글 작성
     * @return 업로드된 파일 경로 (없으면 null)
     */
    String insertBoard(@Valid BoardDTO board, MultipartFile file, String userId);

    /**
     * 게시글 수정
     * @return 업로드된 파일 경로 (없으면 null)
     */
    String updateBoard(@Valid BoardDTO board, MultipartFile file, String userId);

    int deleteBoard(Long boardNo, String userId);

    long getBoardCountForParticipation();
}