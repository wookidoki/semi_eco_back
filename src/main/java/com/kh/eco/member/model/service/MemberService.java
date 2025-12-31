package com.kh.eco.member.model.service;


import com.kh.eco.board.model.dto.FeedBoardDTO;
import com.kh.eco.member.model.dto.ChangePasswordDTO;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;


import com.kh.eco.member.model.dto.MemberSignUpDTO;
import com.kh.eco.member.model.dto.UpdateEmailDTO;
import com.kh.eco.member.model.dto.UpdatePhoneDTO;
import com.kh.eco.member.model.dto.UpdateProfileDTO;
import com.kh.eco.member.model.dto.UpdateRegionDTO;


public interface MemberService {
	

	void signUp(MemberSignUpDTO member, MultipartFile profileImg );
	
	void changePassword(ChangePasswordDTO password);

	void updateMemberEmail(UpdateEmailDTO email);
	
	long getActiveMemberCount();

	List<Map<String, Object>> getMemberRank();

	void updateMemberPhone(UpdatePhoneDTO phone);

	String updateProfile(UpdateProfileDTO profile);

	void updateMemberRegion(UpdateRegionDTO region);

	Map<String, Object> getMyPosts(long memberNo, int currentPage);

	Map<String, Object> getMyComments(long memberNo, int currentPage);

	Map<String, Object> getMyLikes(long memberNo, int currentPage);

	Map<String, Object> getMyBookmarks(long memberNo, int currentPage);
	
	


	
}
