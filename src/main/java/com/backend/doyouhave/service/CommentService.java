package com.backend.doyouhave.service;

import com.backend.doyouhave.domain.comment.Comment;
import com.backend.doyouhave.domain.comment.dto.CommentRequestDto;
import com.backend.doyouhave.domain.comment.dto.CommentResponseDto;
import com.backend.doyouhave.domain.comment.dto.MyInfoCommentResponseDto;
import com.backend.doyouhave.domain.notification.Notification;
import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.exception.NotFoundException;
import com.backend.doyouhave.repository.comment.CommentRepository;
import com.backend.doyouhave.repository.notification.NotificationRepository;
import com.backend.doyouhave.repository.user.UserRepository;
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

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    /*
     * 원 댓글 생성
     */
    public Long saveParent(CommentRequestDto commentRequestDto) {

        // 알림 로직
        Notification notification = new Notification();
        notification.create(commentRequestDto.getPost().getTitle(), commentRequestDto.getContent());
        commentRequestDto.getPost().getUser().setNotification(notification);
        notificationRepository.save(notification);
        userRepository.save(commentRequestDto.getPost().getUser());

        return commentRepository.save(commentRequestDto.toEntityParent()).getId();
    }

    /*
     * 대댓글 생성
     */
    public Long saveChild(CommentRequestDto commentRequestDto) {

        // 알림 로직
        Notification notification = new Notification();
        notification.create(commentRequestDto.getPost().getTitle(), commentRequestDto.getContent());
        commentRequestDto.getPost().getUser().setNotification(notification);
        notificationRepository.save(notification);
        userRepository.save(commentRequestDto.getPost().getUser());

        return commentRepository.save(commentRequestDto.toEntityChild()).getId();
    }

    /*
     * 댓글 수정
     */
    public void update(Long commentId, CommentRequestDto commentRequestDto) {
        // 수정하려는 댓글이 없을 경우 IllegalArgumentException 발생
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NotFoundException::new);

        comment.update(commentRequestDto.getContent());
        commentRepository.save(comment);
    }

    /*
     * 해당 Id를 갖는 댓글
     * (테스트용 메소드)
     */
    public Comment findById(Long commentId) {

        return commentRepository.findById(commentId).orElse(null);
    }

    /*
     * 사용자가 작성한 댓글
     * (테스트용 메소드)
     */
    public List<Comment> findByUser(User user) {

        return commentRepository.findByUser(user);
    }

    /*
     * 사용자가 작성한 댓글
     */
    public Page<MyInfoCommentResponseDto> findByUser(Long userId, Pageable pageable) {

        User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);

        return commentRepository.findByUser(user, pageable).map(MyInfoCommentResponseDto::new);
    }

    /*
     * 전단지에 작성된 댓글
     * (테스트용 메소드)
     */
    public List<Comment> findByPost(Post post) {

        return commentRepository.findByPost(post);
    }

    /*
     * 전단지에 작성된 댓글
     */
    public Page<CommentResponseDto> findByPost(Post post, Pageable pageable, User writer, User user) {
        List<CommentResponseDto> commentDtos = new ArrayList<>();

        for (Comment comment : commentRepository.findByPost(post)) {
            if (comment.getUser() == writer || comment.getUser() == user) {
                // 전단지 작성자, 혹은 댓글 작성자일 경우 실제 댓글 내용 조회 가능
                commentDtos.add(new CommentResponseDto(comment));
            } else {
                // 전단지 작성자, 댓글 작성자 둘 다 아니면 실제 댓글 내용이 아닌 "비밀 댓글입니다."로 조회되도록 함
                Comment newComment = new Comment();
                if (comment.getParent() == null) {
                    newComment.createParent(comment.getUser(), post, comment.getContent(), comment.isSecret());
                } else {
                    newComment.createChild(comment.getUser(), post, comment.getContent(), comment.getParent(), comment.isSecret());
                }
                commentDtos.add(new CommentResponseDto(newComment));
            }
        }

        return new PageImpl<>(commentDtos, pageable, commentDtos.size());
    }

    /*
     * 댓글 삭제
     * DB에서 데이터가 삭제되지 않고 isRemoved 변수만 true 값을 갖도록 한다.
     */
    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NotFoundException::new);
        comment.delete();
        commentRepository.save(comment);
    }
}
