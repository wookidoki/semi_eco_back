package com.kh.eco.auth.model.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kh.eco.auth.model.vo.CustomUserDetails;
import com.kh.eco.exception.UsenameNotFoundException;
import com.kh.eco.member.model.dao.MemberMapper;
import com.kh.eco.member.model.dto.MemberLoginDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberMapper mapper;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberLoginDTO user = mapper.loadUser(username);
		// log.info("이거 오나요 : {}", user);
		
		if(user == null) {
			throw new UsenameNotFoundException("아이디가 존재하지 않습니다.");
		}
		
		if ("N".equals(user.getStatus())) {
		    throw new UsenameNotFoundException("정지된 계정입니다.");
		}
		
		return CustomUserDetails.builder().username(user.getMemberId())
				  						  .password(user.getMemberPwd())
				  						  .memberName(user.getMemberName())
				  						  .memberNo(user.getMemberNo())
				  						  .phone(user.getPhone())
				  						  .email(user.getEmail())
				  						  .refRno(user.getRefRno())
				  						  .memberImage(user.getMemberImage())
				  						  .memberPoint(user.getMemberPoint())
				  						  .enrollDate(user.getEnrollDate())
				  						  .authorities(Collections.singletonList(new SimpleGrantedAuthority(user.getRole())))
				  						  .status(user.getStatus())
				  						  .build();
	}

}