package com.kh.eco.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 웹 MVC 설정
 * S3 파일 업로드 사용으로 로컬 리소스 핸들러 불필요
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // S3 사용으로 로컬 파일 핸들러 제거됨
    // 필요시 CORS 등 추가 설정 가능
}
