package com.backend.doyouhave.controller;

import com.backend.doyouhave.domain.main.MainInfoDto;
import com.backend.doyouhave.service.PostService;
import com.backend.doyouhave.service.result.ResponseService;
import com.backend.doyouhave.service.result.SingleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users/{userId}")
@RequiredArgsConstructor
public class MainController {

    private final PostService postService;
    private final ResponseService responseService;

    @GetMapping
    public ResponseEntity<SingleResult> countOfPost() {

        Map<String, Integer> countPost = postService.findCountPost();

        return ResponseEntity.ok(responseService.getSingleResult(new MainInfoDto(countPost.get("allCount"), countPost.get("todayCount"))));
    }
}
