package com.kh.eco.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.kh.eco.configuration.filter.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfigure {

	@Value("${app.server.url}")
	private String serverUrl;
	private final JwtFilter jwtFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		return httpSecurity
				.formLogin(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)
				.cors(Customizer.withDefaults())
				.sessionManagement(manager ->
					manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


				.authorizeHttpRequests(requests -> {
					// [1] 누구나 접근 가능한 기능 (PermitAll)

					// POST: 로그인, 회원가입, 토큰 갱신, 로그아웃
					requests.requestMatchers(HttpMethod.POST,
							"/api/members",
							"/api/auth/login",
							"/api/auth/refresh",
							"/api/auth/logout",
							"/api/members/profile",
							"/api/members/**").permitAll();

					// GET: 게시글/댓글 조회, 통계, 피드 등 비회원 접근 가능
					requests.requestMatchers(HttpMethod.GET,
							"/api/uploads/**",
							"/api/news/**",
							"/api/boards/**",
							"/api/comments/**",
							"/api/feeds/**",
							"/api/stats/**",
							"/api/members/**",
							"/api/admin/notices/**"
					).permitAll();

					// [2] 인증(로그인)이 필요한 기능 (Authenticated)

					// PUT: 수정
					requests.requestMatchers(HttpMethod.PUT,
							"/api/members/**",
							"/api/boards/**").authenticated();

					// POST: 게시글 작성, 댓글 작성, 피드 작성
					requests.requestMatchers(HttpMethod.POST,
							"/api/boards",
							"/api/boards/**",
							"/api/comments",
							"/api/feeds").authenticated();

					// PUT: 회원 정보 수정, 게시글 수정
					requests.requestMatchers(HttpMethod.PUT,
							"/api/members/**",
							"/api/boards/**",
							"/api/feeds/**",
							"/api/comments/**").authenticated();

					// DELETE: 회원 탈퇴, 게시글 삭제
					requests.requestMatchers(HttpMethod.DELETE,
							"/api/members/**",
							"/api/boards/**",
							"/api/feeds/**").authenticated();

					// [3] 관리자 전용
					requests.requestMatchers("/api/admin/**").hasRole("ADMIN");

					// [4] 그 외 모든 요청은 인증 필요
					requests.anyRequest().authenticated();
				})

				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.build();

	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(Arrays.asList(serverUrl, "http://localhost:5173", "http://localhost:3000"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

}
