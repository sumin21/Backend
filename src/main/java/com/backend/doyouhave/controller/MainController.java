package com.backend.doyouhave.controller;

import com.backend.doyouhave.domain.main.MainInfoDto;
import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.domain.post.dto.PostListResponseDto;
import com.backend.doyouhave.service.PostFilterService;
import com.backend.doyouhave.service.PostService;
import com.backend.doyouhave.service.result.MultiplePageResult;
import com.backend.doyouhave.service.result.MultipleResult;
import com.backend.doyouhave.service.result.ResponseService;
import com.backend.doyouhave.service.result.SingleResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users/{userId}")
@RequiredArgsConstructor
public class MainController {

    private final PostService postService;
    private final PostFilterService postFilterService;
    private final ResponseService responseService;

    @GetMapping
    public ResponseEntity<SingleResult> countOfPost() {

        Map<String, Integer> countPost = postService.findCountPost();

        return ResponseEntity.ok(responseService.getSingleResult(new MainInfoDto(countPost.get("allCount"), countPost.get("todayCount"))));
    }

    /* 전단지 조회 API (+ 검색) */
    @GetMapping("/list")
    @ApiOperation(value = "전단지 리스트", notes = "선택한 카테고리 및 정렬순에 따라 전단지를 보여준다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "권한이 없는 사용자입니다."),
            @ApiResponse(code = 404, message = "잘못된 요청입니다.")
    })
    public ResponseEntity<MultiplePageResult> postListUp(
            @PathVariable Long userId,
            @RequestParam(name="category", required = false) String category,
            @RequestParam(name="tag", required = false) String tag,
            @RequestParam(name="search", required = false) String search,
            @RequestParam(name="sort", required = false) String sort,
            @PageableDefault(size=20) Pageable pageable) {

        // 검색했을 때와 검색하지 않았을 때를 구분
        Page<PostListResponseDto> response = search == null ? postFilterService.findPostByCategoryOrTags(category, tag, sort, pageable) :
                                                                postFilterService.findPostByCategoryAndSearch(category, search, sort, pageable);
        return ResponseEntity.ok(responseService.getMultiplePageResult(response));
    }

    /* 인기 태그 조회 API */
    @GetMapping("/list/top")
    public ResponseEntity<MultipleResult> postTop5Tag(
            @PathVariable Long userId,
            @RequestParam(name="category", required = false) String category) {
        List<String> topTags = postService.findTopTags(category);
        return ResponseEntity.ok(responseService.getMultipleResult(topTags));
    }
}
