package com.emirsomuncu.HaveAnIdea.repository;

import com.emirsomuncu.HaveAnIdea.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment , Long> {

    public List<Comment> findCommentByUserId(Long id);

    public List<Comment> findCommentByPostId(Long id);
}
