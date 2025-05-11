package com.emirsomuncu.HaveAnIdea.controllers;

import com.emirsomuncu.HaveAnIdea.entities.User;
import com.emirsomuncu.HaveAnIdea.service.abstracts.LikeService;
import com.emirsomuncu.HaveAnIdea.service.abstracts.PostService;
import com.emirsomuncu.HaveAnIdea.service.abstracts.UserService;
import com.emirsomuncu.HaveAnIdea.service.responses.post.GetAllPostsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;
    private final UserService userService;
    private final LikeService likeService;

    @GetMapping("/")
    public String firstPage() {
        return "common_page";
    }

    @GetMapping("/custom-login-page")
    public String customLoginPage() {
        return "/user/login_page";
    }

    @GetMapping("/access-denied")
    public String customAccessDenied() {
        return "/user/access_denied";
    }

    @GetMapping("/user/home")
    public String userHomePage(@RequestParam(value = "highlightedPostId", required = false) Long highlightedPostId , Model model) {

        List<GetAllPostsResponse> getAllPostsResponses = this.postService.getAllPosts();
        model.addAttribute("getAllPostsResponses", getAllPostsResponses);

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        Optional<User> currentUser = this.userService.findUserByEmail(email);
        model.addAttribute("currentUser", currentUser);

        Map<Long , Long> postLikeCounts = new HashMap<>();
        for(GetAllPostsResponse post : getAllPostsResponses) {
            Long numberOfLikes = this.likeService.countLikes(post.getId());
            postLikeCounts.put(post.getId() , numberOfLikes );
        }

        model.addAttribute("postLikeCounts" , postLikeCounts);

        model.addAttribute("highlightedPostId", highlightedPostId);

        return "/user/user_home";
    }
}
