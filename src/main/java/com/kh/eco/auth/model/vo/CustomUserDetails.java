package com.kh.eco.auth.model.vo;

import java.sql.Date;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CustomUserDetails implements UserDetails {

	private String username;
	private String password;
	private String role;
	private Collection<? extends GrantedAuthority> authorities;
	
	// 앞단에서 회원정보 조회등에 사용하기 위해 추가로 가져가는 정보들
	private String status;
	private int memberNo;
	private String memberName;
	private String phone;
	private String email;
	private int refRno;
	private String memberImage;
	private int memberPoint;
	private Date enrollDate;
	
	
}