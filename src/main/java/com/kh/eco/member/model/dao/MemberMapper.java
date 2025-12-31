package com.kh.eco.member.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.eco.board.model.dto.FeedBoardDTO;
import com.kh.eco.comment.model.dto.CommentDTO;
import com.kh.eco.member.model.dto.MemberLoginDTO;
import com.kh.eco.member.model.dto.UpdateProfileDTO;
import com.kh.eco.member.model.vo.MemberVO;

@Mapper
public interface MemberMapper {
	
	int signUp(MemberVO member);
	
	int countByMemberId(String memberId);
	int countByPhone(String phone);
	int countByEmail(String email);
	
	MemberLoginDTO loadUser(String memberId);
	void changePassword(Map<String, String> changeRequest);

	MemberVO findById(Long memberId);

	MemberVO findByEmail(String newEmail);

	void updateEmail(Map<String, String> changeRequest);
	
	long getActiveMemberCount();
	
	List<Map<String, Object>> getMemberRank();

	void updatePhone(Map<String, String> changeRequest);

	MemberVO findByPhone(String phone);

	int updateProfile(UpdateProfileDTO profile);
	
	void updateRegion(Map<String, Object> changeRequest);

	List<FeedBoardDTO> selectMyPosts(@Param("memberNo") long memberNo,
									 @Param("offset") int offset, 
									 @Param("limit") int limit);

	int countMyPosts(@Param("memberNo") long memberNo);

	List<CommentDTO> selectMyComments(@Param("memberNo")long memberNo, 
									  @Param("offset")int offset, 
									  @Param("limit")int limit);
	

	int countMyComments(@Param("memberNo") long memberNo);


	List<FeedBoardDTO> selectMyLikes(@Param("memberNo") long memberNo, 
								     @Param("offset") int offset, 
								     @Param("limit") int boardLimit);

	int countMyLikes(@Param("memberNo") long memberNo);

	List<FeedBoardDTO> selectMyBookmarks(@Param("memberNo") long memberNo, 
										 @Param("offset") int offset,
										 @Param("limit") int boardLimit);

	int countMyBookmarks(@Param("memberNo") long memberNo);
	
	

		
}
