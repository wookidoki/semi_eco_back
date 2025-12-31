package com.kh.eco.bookmark.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.eco.bookmark.model.dto.BookmarkDTO;

@Mapper
public interface BookmarkMapper {
	
	/**
	 * 즐겨찾기 등록
	 * @param bookmarkDTO
	 * @return
	 */
	int insertBookmark(BookmarkDTO bookmarkDTO);
	
	/**
	 * 즐겨찾기 행 존재 여부
	 * @param bookmarkDTO
	 * @return
	 */
	int existsBookmark(BookmarkDTO bookmarkDTO);
	
	/**
	 * 즐겨찾기 삭제
	 * @param bookmarkDTO
	 * @return
	 */
	int cancleBookmark(BookmarkDTO bookmarkDTO);
}
