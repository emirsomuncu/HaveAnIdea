package com.emirsomuncu.HaveAnIdea.dao;

import com.emirsomuncu.HaveAnIdea.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeDao extends JpaRepository<Like , Long> {

    public Long countLikeByPostId(Long postId);
    public Optional<Like> findLikeByPostIdAndUserId(Long postId , Long userId);
    public List<Like> findLikeByPostId(Long postId);
}
