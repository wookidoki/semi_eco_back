package com.kh.eco.configuration.filter;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.eco.auth.model.vo.CustomUserDetails;
import com.kh.eco.token.utill.JwtUtill;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/*
 * 추상클래스 | 인터페이스
 * 단일상속     다중상속
 * 
 * 추상클래스
 * 필드의 공유가 필요할 때, 생성자가 필요할 때, 공통 구현 로직이 많을 때
 * =>
 * 다형성을 적용해야 한다.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtill jwtUtill;
	private final UserDetailsService userDetailsService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String uri = request.getRequestURI();
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

		if(authorization == null || uri.equals("/auth/login") ) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authorization.split(" ")[1];

		try {
			Claims claims = jwtUtill.parseJwt(token);
			String username = claims.getSubject();
			CustomUserDetails user = (CustomUserDetails)userDetailsService.loadUserByUsername(username);

            if ("N".equals(user.getStatus())) {
                log.warn("정지된 계정 접근 시도: {}", username);
                SecurityContextHolder.clearContext();
                sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "정지된 계정입니다.");
                return;
            }

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch(ExpiredJwtException e) {
			log.info("토큰의 유효기간 만료");
			sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "토큰이 만료되었습니다. 다시 로그인해주세요.");
			return;
		} catch(JwtException e) {
			log.info("유효하지 않은 토큰: {}", e.getMessage());
			sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "유효하지 않은 토큰입니다.");
			return;
		}
		filterChain.doFilter(request, response);
	}

	/**
	 * JSON 형식의 에러 응답 전송
	 */
	private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
		response.setStatus(status);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("success", false);
		errorResponse.put("message", message);
		errorResponse.put("data", null);

		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}
