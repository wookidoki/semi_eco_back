package com.kh.eco.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.eco.board.model.dao.BoardMapper;
import com.kh.eco.board.model.dto.BoardDTO;
import com.kh.eco.board.model.dto.BoardDetailDTO;
import com.kh.eco.common.PageInfo;
import com.kh.eco.file.S3Service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;
    private final S3Service s3Service;

    @Override
    public Map<String, Object> selectBoardList(Map<String, Object> map) {
        int listCount = boardMapper.selectListCount(map);

        int currentPage = 1;
        if (map.get("currentPage") != null) {
            currentPage = Integer.parseInt(String.valueOf(map.get("currentPage")));
        }

        int pageLimit = 10;
        int boardLimit = 10;
        int maxPage = (int) Math.ceil((double) listCount / boardLimit);
        int startPage = (currentPage - 1) / pageLimit * pageLimit + 1;
        int endPage = startPage + pageLimit - 1;
        if (endPage > maxPage) endPage = maxPage;
        int offset = (currentPage - 1) * boardLimit;

        PageInfo pi = new PageInfo(listCount, currentPage, pageLimit, boardLimit, startPage, endPage, maxPage, offset);

        int startRow = (pi.getCurrentPage() - 1) * pi.getBoardLimit() + 1;
        int endRow = startRow + pi.getBoardLimit() - 1;

        map.put("startRow", startRow);
        map.put("endRow", endRow);

        List<BoardDTO> list = boardMapper.selectBoardList(map);
        List<BoardDTO> topPosts = boardMapper.selectTopBoardList(map);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("topPosts", topPosts);
        result.put("pi", pi);

        return result;
    }

    @Override
    @Transactional
    public BoardDetailDTO selectBoardDetail(Long boardNo) {
        boardMapper.increaseViewCount(boardNo);
        return boardMapper.selectBoardDetail(boardNo);
    }

    @Override
    @Transactional
    public String insertBoard(BoardDTO board, MultipartFile file, String userId) {
        board.setBoardWriter(userId);
        boardMapper.insertBoard(board);

        String filePath = null;
        if (file != null && !file.isEmpty()) {
            filePath = saveAttachment(board.getBoardNo(), file);
        }

        return filePath;
    }

    @Transactional
    @Override
    public String updateBoard(@Valid BoardDTO board, MultipartFile file, String userId) {
        board.setBoardWriter(userId);
        boardMapper.updateBoard(board);

        String filePath = null;
        if (file != null && !file.isEmpty()) {
            filePath = s3Service.upload(file, "board");
            String originalName = file.getOriginalFilename();

            Map<String, Object> fileMap = new HashMap<>();
            fileMap.put("refBno", board.getBoardNo());
            fileMap.put("originName", originalName);
            fileMap.put("changeName", extractFileName(filePath));
            fileMap.put("attachmentPath", filePath);

            int updateCount = boardMapper.updateAttachment(fileMap);

            if (updateCount == 0) {
                boardMapper.insertAttachment(fileMap);
            }
        }

        return filePath;
    }

    @Override
    @Transactional
    public int deleteBoard(Long boardNo, String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("boardNo", boardNo);
        map.put("userId", userId);
        return boardMapper.deleteBoard(map);
    }

    @Override
    public long getBoardCountForParticipation() {
        return boardMapper.getBoardCountForParticipation();
    }

    /**
     * 파일 첨부 저장 (S3)
     * @return 업로드된 파일 경로
     */
    private String saveAttachment(Long boardNo, MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        String fileUrl = s3Service.upload(file, "board");
        String originalName = file.getOriginalFilename();

        Map<String, Object> fileMap = new HashMap<>();
        fileMap.put("refBno", boardNo);
        fileMap.put("originName", originalName);
        fileMap.put("changeName", extractFileName(fileUrl));
        fileMap.put("attachmentPath", fileUrl);

        boardMapper.insertAttachment(fileMap);
        return fileUrl;
    }

    /**
     * S3 URL에서 파일명 추출
     */
    private String extractFileName(String fileUrl) {
        if (fileUrl == null) return null;
        int lastSlash = fileUrl.lastIndexOf('/');
        return lastSlash >= 0 ? fileUrl.substring(lastSlash + 1) : fileUrl;
    }
}
