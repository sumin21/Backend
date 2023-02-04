package com.backend.doyouhave.service;

import com.backend.doyouhave.domain.comment.Comment;
import com.backend.doyouhave.domain.comment.dto.CommentRequestDto;
import com.backend.doyouhave.domain.comment.dto.CommentResponseDto;
import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;

    /*
     * 댓글 생성
     */
    public Long save(CommentRequestDto commentRequestDto) {

        return commentRepository.save(commentRequestDto.toEntity()).getId();
    }

    /*
     * 댓글 수정
     */
    public Long update(Long commentId, CommentRequestDto commentRequestDto) {
        // 수정하려는 댓글이 없을 경우 IllegalArgumentException 발생
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + commentId));

        comment.update(commentRequestDto.getContent());

        return commentId;
    }

    /*
     * 사용자가 작성한 댓글
     */
    public Page<CommentResponseDto> findByUser(User user, Pageable pageable) {

        return commentRepository.findByUser(user, pageable).map(CommentResponseDto::new);
    }

    /*
     * 전단지에 작성된 댓글
     */
    public Page<CommentResponseDto> findByPost(Post post, Pageable pageable, User writer, User user) {
        List<CommentResponseDto> comments = new ArrayList<>();

        for (Comment comment : commentRepository.findByPost(post)) {
            if (comment.getUser() == writer || comment.getUser() == user) {
                // 전단지 작성자, 혹은 댓글 작성자일 경우 실제 댓글 내용 조회 가능
                comments.add(new CommentResponseDto(comment));
            } else {
                // 전단지 작성자, 댓글 작성자 둘 다 아니면 실제 댓글 내용이 아닌 "비밀 댓글입니다."로 조회되도록 함
                Comment newComment = new Comment();
                newComment.create(comment.getUser(), "비밀 댓글입니다.");
                comments.add(new CommentResponseDto(newComment));
            }
        }

        return new PageImpl<>(comments, pageable, comments.size());
    }
}
