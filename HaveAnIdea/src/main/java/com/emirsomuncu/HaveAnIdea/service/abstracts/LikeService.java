package com.emirsomuncu.HaveAnIdea.service.abstracts;

import com.emirsomuncu.HaveAnIdea.service.responses.GetLikesByPostIdResponse;

import java.util.List;

public interface LikeService {

    public List<GetLikesByPostIdResponse> getLikesByPostId(Long postId);
    public void saveLike(Long postId , Long userId);
    public Long countLikes(Long postId);
}
