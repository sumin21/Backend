package com.backend.doyouhave.service.sorttype;

import com.backend.doyouhave.domain.post.dto.PostListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SortWay {
    public Page<PostListResponseDto> findPostBySortType(String category, String tag, Pageable pageable);
}
