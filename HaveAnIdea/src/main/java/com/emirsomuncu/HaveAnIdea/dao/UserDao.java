package com.emirsomuncu.HaveAnIdea.dao;

import com.emirsomuncu.HaveAnIdea.entities.User;
import com.emirsomuncu.HaveAnIdea.service.abstracts.LikeService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User , Long> {

    public Optional<User> findByEmail(String email);
    public List<User> findUserByRole(String role);
    public List<User> findUserByUsernameContainingIgnoreCase(String username);
    public Long countByRole(String role);
}
