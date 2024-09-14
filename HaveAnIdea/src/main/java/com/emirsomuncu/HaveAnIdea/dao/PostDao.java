package com.emirsomuncu.HaveAnIdea.dao;

import com.emirsomuncu.HaveAnIdea.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostDao extends JpaRepository<Post , Long> {

    public List<Post> findPostByUserId(Long id);
    public List<Post> findAllByOrderByCreatedAtDesc();
    public List<Post> findPostByUserIdOrderByCreatedAtDesc(Long id);


}
