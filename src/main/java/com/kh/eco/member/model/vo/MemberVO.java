package com.kh.eco.member.model.vo;

import java.sql.Date;
import java.time.LocalDate;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Builder
@Value
@ToString
public class MemberVO {

	private Long memberNo;
	private String memberName;
	private String memberId;
	private String memberPwd;
	private String phone;
	private String email;
	private int refRno;
    private String memberImageUrl; 
    private Long memberPoint;
    private Date enrollDate;
    private char status;
    private String role;
    


}