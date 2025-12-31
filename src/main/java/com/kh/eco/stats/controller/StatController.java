package com.kh.eco.stats.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.eco.common.dto.ResponseData;
import com.kh.eco.member.model.service.MemberService;
import com.kh.eco.stats.model.service.StatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/stats")
public class StatController {

    private final StatService statService;
    private final MemberService memberService;

    @GetMapping("/landing")
    public ResponseEntity<ResponseData<Map<String, Object>>> getLandingStats() {
        Map<String, Object> stats = statService.getLandingStats();
        return ResponseData.ok(stats);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ResponseData<Map<String, Object>>> getDashboardStats() {
        Map<String, Object> stats = statService.getDashboardStats();
        return ResponseData.ok(stats);
    }

    @GetMapping("/mainpage")
    public ResponseEntity<ResponseData<Map<String, Object>>> getMainpage() {
        Map<String, Object> main = statService.getMainpage();
        return ResponseData.ok(main);
    }

    @GetMapping("/ranking")
    public ResponseEntity<ResponseData<List<Map<String, Object>>>> getMemberRankList() {
        List<Map<String, Object>> rankList = memberService.getMemberRank();
        return ResponseData.ok(rankList);
    }

    /**
     * 오늘의 참여태그 수
     */
    @GetMapping("/today")
    public ResponseEntity<ResponseData<Map<String, Object>>> todayParticipants(
            @RequestParam(name = "category") String category) {

        int count = statService.todayParticipants(category);

        Map<String, Object> result = new HashMap<>();
        result.put("category", category);
        result.put("todayParticipants", count);

        return ResponseData.ok(result);
    }

    /**
     * 오늘의 새 글
     */
    @GetMapping("/todayPost")
    public ResponseEntity<ResponseData<Map<String, Object>>> todayPost(
            @RequestParam(name = "category") String category) {

        int count = statService.todayPost(category);

        Map<String, Object> result = new HashMap<>();
        result.put("category", category);
        result.put("todayPost", count);

        return ResponseData.ok(result);
    }
}
