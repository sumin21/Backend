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

@Transactional
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final CloudManager cloudManager;

    /* 전단지 작성 */
    public Post savePost(Long userId, Post savedPost, MultipartFile imageFile) throws IOException {
        // User user = findUser(userId); post.setUser(user); 토큰으로 가져오는 경우 authToken 사용
        /* 최대 사진 2개 업로드 가능
        List<Object> multipartFiles = new ArrayList<>();
        for(MultipartFile imageFile : imageFiles) {
            multipartFiles.add(imageFile.getBytes());
        } */
        Map uploadResult = cloudManager.uploadFile(savedPost.getId(), imageFile,
                ObjectUtils.asMap("resourcetype", "auto"));
        // url
        if(uploadResult!=null) {
            savedPost.setImg(uploadResult.get("url").toString());
        }
        return postRepository.save(savedPost);
    }

//    private User findUser(Long id) {
//        return userRepository.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다."));
//    }
}
