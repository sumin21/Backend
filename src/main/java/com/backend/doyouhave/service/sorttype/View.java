package com.backend.doyouhave.service.sorttype;

import com.backend.doyouhave.domain.post.dto.PostListResponseDto;
import com.backend.doyouhave.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class View implements SortWay {
    private final PostRepository postRepository;
    @Override
    public Page<PostListResponseDto> findPostBySortType(String category, String tag, Pageable pageable) {
        PageRequest request = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "viewCount"));
        pageable = request;
        Page<PostListResponseDto> pageList = null;
        if(category==null && tag == null) {
            pageList = postRepository.findAllByOrderByViewCountDesc(pageable).map(PostListResponseDto::new);
        } else if(category!=null && tag != null) {
            pageList  = postRepository.findByCategoryAndTagsContainingOrderByViewCountDesc(category, tag, pageable).map(PostListResponseDto::new);
        } else {
            pageList = postRepository.findByCategoryOrTagsContainingOrderByViewCountDesc(category, tag, pageable).map(PostListResponseDto::new);
        }
        return pageList;
    }
}
