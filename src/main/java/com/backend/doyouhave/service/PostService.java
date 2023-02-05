package com.backend.doyouhave.service;

import com.backend.doyouhave.domain.post.Category;
import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.repository.post.CategoryRepository;
import com.backend.doyouhave.repository.post.PostRepository;
import com.backend.doyouhave.util.CloudManager;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final CloudManager cloudManager;

    /* 전단지 작성 */
    public Long savePost(Post savedPost, MultipartFile imageFile, MultipartFile imageFileSecond) throws IOException {

        // savedPost.setUser(user);
        // 카테고리 별로 태그 저장. 카테고리가 기존에 존재하는 키워드라면 해당 카테고리에 속하는 태그들을 추가하는 방식
        Category categoryResult = categoryRepository.findByKeyword(savedPost.getCategory());
        if(categoryResult == null) {
            List<String> tagList =  Arrays.stream(savedPost.getTags().split(",")).toList();
            categoryResult = Category.builder()
                    .keyword(savedPost.getCategory())
                    .count(0)
                    .tags(tagList)
                    .build();
        } else {
            List<String> tags = categoryResult.getTags();
            List<String> postTags =  Arrays.stream(savedPost.getTags().split(",")).toList();
            for(String tag : postTags) {
                tags.add(tag);
            }
        }
        categoryResult.setCount(categoryResult.getCount() + 1);
        categoryRepository.save(categoryResult);

        // 클라우디너리에 이미지 저장 및 글 작성 처리
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
