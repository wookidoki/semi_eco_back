package com.kh.eco.api.news.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.eco.api.news.model.dto.NewsDTO;
import com.kh.eco.api.news.model.service.NewsService;
import com.kh.eco.common.dto.ResponseData;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/news")
    public ResponseEntity<ResponseData<List<NewsDTO>>> getNews(
            @RequestParam(value = "query", required = false, defaultValue = "환경") String query) {

        List<NewsDTO> newsList = newsService.findAllNews(query);
        return ResponseData.ok(newsList);
    }
}
