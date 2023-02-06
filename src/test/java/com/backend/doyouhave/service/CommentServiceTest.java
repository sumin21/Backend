package com.backend.doyouhave.service;

import com.backend.doyouhave.domain.comment.Comment;
import com.backend.doyouhave.domain.comment.dto.CommentRequestDto;
import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.domain.user.Role;
import com.backend.doyouhave.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Test
    @DisplayName("댓글 작성")
    void create() {

    }

    @Test
    @DisplayName("댓글 수정")
    void update() {

    }

    @Test
    @DisplayName("사용자가 작성한 댓글 조회")
    void findByUser() {

    }

    @Test
    @DisplayName("전단지에 작성된 댓글 조회")
    void findByPost() {

    }

    @Test
    @DisplayName("비밀 댓글 등록")
    void convert() {

    }

    @Test
    @DisplayName("댓글 삭제")
    void delete() {

    }
}