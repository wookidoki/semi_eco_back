package com.kh.eco.member.model.service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.eco.auth.model.vo.CustomUserDetails;
import com.kh.eco.board.model.dto.FeedBoardDTO;
import com.kh.eco.comment.model.dto.CommentDTO;
import com.kh.eco.common.PageInfo;
import com.kh.eco.common.Pagination;
import com.kh.eco.exception.CustomAuthenticationException;
import com.kh.eco.file.S3Service;
import com.kh.eco.member.model.dao.MemberMapper;
import com.kh.eco.member.model.dto.ChangePasswordDTO;
import com.kh.eco.member.model.dto.MemberSignUpDTO;
import com.kh.eco.member.model.dto.UpdateEmailDTO;
import com.kh.eco.member.model.dto.UpdatePhoneDTO;
import com.kh.eco.member.model.dto.UpdateProfileDTO;
import com.kh.eco.member.model.dto.UpdateRegionDTO;
import com.kh.eco.member.model.vo.MemberVO;

import org.springframework.beans.factory.annotation.Value;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final MemberInfoDuplicateCheck midc;
    private final S3Service s3Service;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    private String getDefaultProfileImage() {
        return String.format("https://%s.s3.%s.amazonaws.com/profile/default-profile.png", bucketName, region);
    }

    @Override
    public void signUp(MemberSignUpDTO member, MultipartFile profileImg) {
        midc.idDuplicateCheck(member.getMemberId());
        midc.phoneDuplicateCheck(member.getPhone());
        midc.emailDuplicateCheck(member.getEmail());

        String profileImgUrl = getDefaultProfileImage();

        // 프로필 이미지 S3 업로드
        if (profileImg != null && !profileImg.isEmpty()) {
            profileImgUrl = s3Service.upload(profileImg, "profile");
            log.info("프로필 이미지 S3 업로드 완료: {}", profileImgUrl);
        }

        Date currentDate = new Date(System.currentTimeMillis());

        MemberVO signUpMember = MemberVO.builder()
                .memberName(member.getMemberName())
                .memberId(member.getMemberId())
                .memberPwd(passwordEncoder.encode(member.getMemberPwd()))
                .phone(member.getPhone())
                .email(member.getEmail())
                .refRno(member.getRefRno())
                .memberImageUrl(profileImgUrl)
                .memberPoint(0L)
                .enrollDate(currentDate)
                .status('Y')
                .role("ROLE_USER")
                .build();

        memberMapper.signUp(signUpMember);
        log.info("사용자등록 성공: {}", signUpMember.getMemberId());
    }

    @Override
    public void changePassword(ChangePasswordDTO password) {
        CustomUserDetails user = validatePassword(password.getCurrentPassword());
        String newPassword = passwordEncoder.encode(password.getNewPassword());

        Map<String, String> changeRequest = Map.of(
                "memberId", user.getUsername(),
                "newPassword", newPassword
        );

        memberMapper.changePassword(changeRequest);
    }

    private CustomUserDetails validatePassword(String password) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomAuthenticationException("비밀번호가 일치하지 않습니다");
        }

        return user;
    }

    @Override
    public void updateMemberEmail(UpdateEmailDTO email) {
        CustomUserDetails user = validateEmail(email.getNewEmail());
        log.debug("이메일 변경 - user: {}, newEmail: {}", user.getUsername(), email.getNewEmail());

        Map<String, String> changeRequest = Map.of(
                "memberId", user.getUsername(),
                "newEmail", email.getNewEmail()
        );

        memberMapper.updateEmail(changeRequest);
    }

    private CustomUserDetails validateEmail(String newEmail) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        MemberVO duplicated = memberMapper.findByEmail(newEmail);
        if (duplicated != null && !duplicated.getEmail().equals(newEmail)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다");
        }

        return user;
    }

    @Override
    public void updateMemberPhone(UpdatePhoneDTO phone) {
        CustomUserDetails user = validatePhone(phone.getNewPhone());

        Map<String, String> changeRequest = Map.of(
                "memberId", user.getUsername(),
                "newPhone", phone.getNewPhone()
        );

        memberMapper.updatePhone(changeRequest);
    }

    private CustomUserDetails validatePhone(String phone) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("번호는 비어 있을 수 없습니다");
        }

        if (!phone.matches("^0\\d{1,2}-\\d{3,4}-\\d{4}$")) {
            throw new IllegalArgumentException("올바른 번호 형식이 아닙니다");
        }

        MemberVO duplicated = memberMapper.findByPhone(phone);
        if (duplicated != null && !duplicated.getPhone().equals(phone)) {
            throw new IllegalArgumentException("이미 사용중인 번호입니다");
        }

        return user;
    }

    @Override
    public void updateMemberRegion(UpdateRegionDTO region) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        int newRegion = region.getNewRegion();
        log.debug("지역 변경 - user: {}, newRegion: {}", user.getUsername(), newRegion);

        Map<String, Object> changeRequest = Map.of(
                "memberId", user.getUsername(),
                "newRegion", newRegion
        );

        memberMapper.updateRegion(changeRequest);
    }

    @Override
    public String updateProfile(UpdateProfileDTO profile) {
        log.debug("프로필 업데이트 시작 - memberId: {}", profile.getMemberId());

        MultipartFile file = profile.getNewImage();

        // 새 이미지가 있으면 S3 업로드
        if (file != null && !file.isEmpty()) {
            String imageUrl = s3Service.upload(file, "profile");
            profile.setImagePath(imageUrl);
            log.info("프로필 이미지 S3 업로드 완료: {}", imageUrl);
        }

        int result = memberMapper.updateProfile(profile);
        if (result == 0) {
            throw new RuntimeException("프로필 업데이트 실패");
        }

        return profile.getImagePath();
    }

    @Override
    public Map<String, Object> getMyPosts(long memberNo, int currentPage) {
        int boardLimit = 10;
        int pageLimit = 5;

        int listCount = memberMapper.countMyPosts(memberNo);
        PageInfo pageInfo = Pagination.getPageInfo(listCount, currentPage, pageLimit, boardLimit);

        List<FeedBoardDTO> posts = memberMapper.selectMyPosts(memberNo, pageInfo.getOffset(), boardLimit);

        List<FeedBoardDTO> postDTOs = posts.stream()
                .map(board -> new FeedBoardDTO(
                        board.getBoardNo(),
                        board.getBoardTitle(),
                        board.getBoardContent(),
                        board.getBoardCategory(),
                        board.getCategoryName(),
                        board.getBoardAuthor(),
                        board.getMemberId(),
                        board.getMemberImage(),
                        board.getAttachmentPath(),
                        board.getRegionNo(),
                        board.getRegionName(),
                        board.getRegDate(),
                        board.getLikeCount(),
                        board.getCommentCount()
                ))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("pageInfo", pageInfo);
        response.put("list", posts);

        return response;
    }

    @Override
    public Map<String, Object> getMyComments(long memberNo, int currentPage) {
        int boardLimit = 10;
        int pageLimit = 5;

        int listCount = memberMapper.countMyComments(memberNo);
        PageInfo pageInfo = Pagination.getPageInfo(listCount, currentPage, pageLimit, boardLimit);

        List<CommentDTO> comment = memberMapper.selectMyComments(memberNo, pageInfo.getOffset(), boardLimit);

        Map<String, Object> response = new HashMap<>();
        response.put("pageInfo", pageInfo);
        response.put("list", comment);

        return response;
    }

    @Override
    public Map<String, Object> getMyLikes(long memberNo, int currentPage) {
        int boardLimit = 10;
        int pageLimit = 5;

        int listCount = memberMapper.countMyLikes(memberNo);
        PageInfo pageInfo = Pagination.getPageInfo(listCount, currentPage, pageLimit, boardLimit);

        List<FeedBoardDTO> likes = memberMapper.selectMyLikes(memberNo, pageInfo.getOffset(), boardLimit);

        Map<String, Object> response = new HashMap<>();
        response.put("pageInfo", pageInfo);
        response.put("list", likes);

        return response;
    }

    @Override
    public Map<String, Object> getMyBookmarks(long memberNo, int currentPage) {
        int boardLimit = 10;
        int pageLimit = 5;

        int listCount = memberMapper.countMyBookmarks(memberNo);
        PageInfo pageInfo = Pagination.getPageInfo(listCount, currentPage, pageLimit, boardLimit);

        List<FeedBoardDTO> bookmarks = memberMapper.selectMyBookmarks(memberNo, pageInfo.getOffset(), boardLimit);

        Map<String, Object> response = new HashMap<>();
        response.put("pageInfo", pageInfo);
        response.put("list", bookmarks);

        return response;
    }

    @Override
    public long getActiveMemberCount() {
        try {
            return memberMapper.getActiveMemberCount();
        } catch (Exception e) {
            log.error("활성 회원 수 조회 중 오류 발생", e);
            return 0L;
        }
    }

    @Override
    public List<Map<String, Object>> getMemberRank() {
        return memberMapper.getMemberRank();
    }
}
