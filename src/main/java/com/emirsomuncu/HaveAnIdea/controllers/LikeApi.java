package com.emirsomuncu.HaveAnIdea.controllers;

import com.emirsomuncu.HaveAnIdea.entities.User;
import com.emirsomuncu.HaveAnIdea.service.abstracts.LikeService;
import com.emirsomuncu.HaveAnIdea.service.abstracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class LikeApi {

    private final LikeService likeService;
    private final UserService userService;

    @PostMapping("like-post")
    @ResponseBody
    public String likePost(@RequestParam Long postId) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        Optional<User> currentUser = this.userService.findUserByEmail(email);
        Long userId = currentUser.get().getId();
        likeService.saveLike(postId , userId);
        Long updatedLikeCount = likeService.countLikes(postId);
        return updatedLikeCount.toString();
    }
}
