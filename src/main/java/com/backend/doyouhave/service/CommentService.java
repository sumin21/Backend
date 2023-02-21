package com.backend.doyouhave.service;

import com.backend.doyouhave.domain.comment.Comment;
import com.backend.doyouhave.domain.comment.dto.ChildCommentDto;
import com.backend.doyouhave.domain.comment.dto.CommentRequestDto;
import com.backend.doyouhave.domain.comment.dto.CommentInfoDto;
import com.backend.doyouhave.domain.comment.dto.MyInfoCommentResponseDto;
import com.backend.doyouhave.domain.notification.Notification;
import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.exception.BusinessException;
import com.backend.doyouhave.exception.ExceptionCode;
import com.backend.doyouhave.exception.NotFoundException;
import com.backend.doyouhave.repository.comment.CommentRepository;
import com.backend.doyouhave.repository.notification.NotificationRepository;
import com.backend.doyouhave.repository.post.PostRepository;
import com.backend.doyouhave.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final PostRepository postRepository;

    /*
     * 댓글 생성
     */
    public Long save(CommentRequestDto commentRequestDto, Long userId, Long postId) {

        // 알림 로직
        setNotification(postId, commentRequestDto);

        return commentRepository.save(commentRequestDto.toEntity(userRepository.findById(userId).orElseThrow(NotFoundException::new),
                postRepository.findById(postId).orElseThrow(NotFoundException::new))).getId();
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
    public Page<CommentInfoDto> getCommentsByPost(Long postId, Long userId, Pageable pageable) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundException::new);
        List<Comment> commentList = commentRepository.findByPostOrderByCreatedDate(post);
        // 댓글 작성자 목록 (userId, 별명) <- 익명 순서 지정하기 위해
        Map<Long, String> writeCommentUserIdAndNameMap = new HashMap<>();
        // 댓글 목록 (commentId, comment) <- 원댓글, 대댓글 관계 매핑 위해
        Map<Long, CommentInfoDto> commentIdAndDtoMap = new LinkedHashMap<>();
        
        for (Comment comment : commentList) {
            User commentWriter = comment.getUser();
            boolean isWriter = Objects.equals(commentWriter, comment.getPost().getUser());
            String name = isWriter ? "글쓴이" : writeCommentUserIdAndNameMap.computeIfAbsent(
                    commentWriter.getId(),
                    key -> "익명"+String.valueOf(writeCommentUserIdAndNameMap.size()+1)
            );

            if (comment.getParentId() != null) {
                // 대댓글인 경우
                CommentInfoDto parentComment = commentIdAndDtoMap.getOrDefault(comment.getParentId(), null);
                if (parentComment == null) throw new BusinessException(ExceptionCode.INTERNAL_SERVER_ERROR);
                parentComment.getChildComments().add(ChildCommentDto.from(comment, userId, name, parentComment.getIsCommentWriter()));
            } else {
                // 원댓글인 경우
                commentIdAndDtoMap.put(comment.getId(), CommentInfoDto.from(comment, userId, name));
            }
        }
        List<CommentInfoDto> result = new ArrayList<>(commentIdAndDtoMap.values());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), result.size());
        return new PageImpl<>(result.subList(start, end), pageable, result.size());
    }

    /*
     * 사용자가 해당 게시글 작성자인지 유무 확인
     */
    public boolean checkUserIsWriter(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundException::new);
        return Objects.equals(post.getUser().getId(), userId);
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

    /*
     * 알림 추가 로직
     */
    private void setNotification(Long postId, CommentRequestDto commentRequestDto) {
        Notification notification = new Notification();
        notification.create(postRepository.findById(postId).orElseThrow(NotFoundException::new).getTitle(), commentRequestDto.getContent());
        postRepository.findById(postId).orElseThrow(NotFoundException::new).getUser().setNotification(notification);
        notificationRepository.save(notification);
        userRepository.save(postRepository.findById(postId).orElseThrow(NotFoundException::new).getUser());
    }
}
