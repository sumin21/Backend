package com.backend.doyouhave.service;

import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.repository.post.PostRepository;
import com.backend.doyouhave.util.CloudManager;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final CloudManager cloudManager;

    /* 전단지 작성 */
    public Long savePost(Post savedPost, MultipartFile imageFile, MultipartFile imageFileSecond) throws IOException {
        // User user = findUser(userId); post.setUser(user); 토큰으로 가져오는 경우 authToken 사용

        postRepository.save(savedPost);
        Map<Object, Map> uploadResultFirst = cloudManager.uploadFile(savedPost.getId(), imageFile);
        if(uploadResultFirst!=null) {
            savedPost.setImg(uploadResultFirst.get("url").toString());
        }

        Map<Object, Map> uploadResultSecond = cloudManager.uploadFile(savedPost.getId(), imageFileSecond);
        if(uploadResultSecond!=null) {
            savedPost.setImgSecond(uploadResultSecond.get("url").toString());
        }

        return savedPost.getId();
    }

//    private User findUser(Long id) {
//        return userRepository.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다."));
//    }
}
