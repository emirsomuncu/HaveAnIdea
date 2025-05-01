package com.emirsomuncu.HaveAnIdea.repository;

import com.emirsomuncu.HaveAnIdea.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User , Long> {

    @Query(value = "SELECT * FROM user ORDER BY RAND() LIMIT 5", nativeQuery = true)
    public List<User> getRandomFiveUser();
    public Optional<User> findByEmail(String email);
    public List<User> findUserByRole(String role);
    public List<User> findUserByUsernameContainingIgnoreCase(String username);
    public User findUserByUsername(String username);
    public Long countByRole(String role);
}
