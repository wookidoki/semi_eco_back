package com.kh.eco.api.news.model.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.eco.api.news.model.dto.NewsDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {

    // application.properties에 설정된 키 값을 가져옵니다.
    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    public List<NewsDTO> findAllNews(String query) {
        // 1. 네이버 API 호출 URI 생성
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/news.json")
                .queryParam("query", query == null ? "환경" : query)
                .queryParam("display", 10) // 가져올 개수
                .queryParam("start", 1)
                .queryParam("sort", "date") // 날짜순 (date) 또는 정확도순 (sim)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        // 2. 헤더에 Client ID와 Secret 추가 (네이버 필수)
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .build();

        // 3. API 요청 및 응답 수신 (NaverNewsResponse 없이 String으로 바로 받음)
        RestTemplate restTemplate = new RestTemplate();
        String responseBody = restTemplate.exchange(req, String.class).getBody();

        // 4. JSON 파싱 및 데이터 가공 후 반환
        return parseJson(responseBody, query);
    }

    // JSON 파싱 메서드 (Jackson 사용)
    private List<NewsDTO> parseJson(String jsonResponse, String query) {
        List<NewsDTO> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            // JSON 구조: { "items": [ ... ] }
            JsonNode root = mapper.readTree(jsonResponse);
            JsonNode items = root.path("items");
            long id = 1;

            for (JsonNode item : items) {
                // 데이터 가공 (HTML 태그 제거 등)
                String title = removeHtmlTags(item.path("title").asText());
                String description = removeHtmlTags(item.path("description").asText());
                String link = item.path("link").asText();
                String pubDate = item.path("pubDate").asText();

                // 프론트엔드 출력을 위한 DTO 생성
                NewsDTO news = NewsDTO.builder()
                        .id(id++)
                        .title(title)
                        .link(link)
                        .description(description)
                        .date(pubDate) // 필요시 포맷팅 추가 가능
                        .category("#" + (query == null ? "환경" : query)) // 검색어를 카테고리로
                        .imageUrl("https://placehold.co/600x400/34d399/ffffff?text=News+Image") // 대체 이미지
                        .views(ThreadLocalRandom.current().nextInt(100, 3001)) // 랜덤 조회수
                        .build();

                list.add(news);
            }
        } catch (Exception e) {
            log.error("뉴스 JSON 파싱 실패: {}", e.getMessage(), e);
        }
        return list;
    }

    // HTML 태그 제거 유틸리티
    private String removeHtmlTags(String text) {
        if (text == null) return "";
        return text.replaceAll("<[^>]*>", "")
                   .replaceAll("&quot;", "\"")
                   .replaceAll("&amp;", "&")
                   .replaceAll("&lt;", "<")
                   .replaceAll("&gt;", ">");
    }
}