package com.backend.doyouhave.controller;

import com.backend.doyouhave.domain.post.dto.ReportedPostDto;
import com.backend.doyouhave.domain.user.dto.UserInfoDto;
import com.backend.doyouhave.service.PostService;
import com.backend.doyouhave.service.UserService;
import com.backend.doyouhave.service.result.MultiplePageResult;
import com.backend.doyouhave.service.result.MultipleResult;
import com.backend.doyouhave.service.result.ResponseService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/admins")
@RestController
public class ManagerController {

    private final UserService userService;
    private final PostService postService;
    private final ResponseService responseService;

    @GetMapping("/users")
    @ApiOperation(value = "회원 목록 API", notes = "관리자 페이지에서 회원 목록을 보여준다. (회원정보, 접속일자)")
    public ResponseEntity<MultiplePageResult<UserInfoDto>> usersInfo(
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserInfoDto> userInfoDtos = userService.findUsersInfo(pageable);
        return ResponseEntity.ok(responseService.getMultiplePageResult(userInfoDtos));
    }

    @GetMapping("/posts/report")
    @ApiOperation(value = "신고 글 목록 API", notes = "관리자 페이지에서 신고당한 글 목록을 보여준다. ")
    public ResponseEntity<MultiplePageResult<ReportedPostDto>> reportedPostInfo(
            @PageableDefault(sort = "created_date", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ReportedPostDto> reportedPostDtos = postService.findReportedPosts(pageable);
        return ResponseEntity.ok(responseService.getMultiplePageResult(reportedPostDtos));
    }
}

