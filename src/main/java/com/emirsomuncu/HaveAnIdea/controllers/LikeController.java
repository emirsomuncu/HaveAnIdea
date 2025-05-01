package com.emirsomuncu.HaveAnIdea.controllers;

import com.emirsomuncu.HaveAnIdea.core.utilites.redirecthelper.RedirectHelper;
import com.emirsomuncu.HaveAnIdea.entities.Post;
import com.emirsomuncu.HaveAnIdea.entities.User;
import com.emirsomuncu.HaveAnIdea.service.abstracts.LikeService;
import com.emirsomuncu.HaveAnIdea.service.abstracts.PostService;
import com.emirsomuncu.HaveAnIdea.service.abstracts.UserService;
import com.emirsomuncu.HaveAnIdea.service.responses.like.GetLikesByPostIdResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.post.GetPostByIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class LikeController {

    private final UserService userService;
    private final LikeService likeService;
    private final PostService postService;

    @RequestMapping("/user/post-likes")
    public String postLikes(@RequestParam Long postId) {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        Optional<User> currentUser = this.userService.findUserByEmail(email);

        this.likeService.saveLike(postId , currentUser.get().getId() );
        return "redirect:/user/home";
    }

    @RequestMapping("/user/posts/post-likes")
    public String profilePostLikes(@RequestParam Long postId) {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        Optional<User> currentUser = this.userService.findUserByEmail(email);
        this.likeService.saveLike(postId , currentUser.get().getId() );
        GetPostByIdResponse post = this.postService.getPostById(postId);
        String userId = post.getUserId();
        return "redirect:/user/user-posts?userId=" + userId;
    }

    @RequestMapping("/user/topics-post-likes")
    public String postLikeForFromTopicPage( Long postId  , String topicName) {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        Optional<User> currentUser = this.userService.findUserByEmail(email);
        this.likeService.saveLike(postId , currentUser.get().getId() );
        String encodedTopicName = RedirectHelper.encodeUrlPathSegment(topicName);

        return "redirect:/user/topic/" + encodedTopicName + "/posts";
    }

    @GetMapping("/user/{postId}/likes")
    public String usersPostLikes(@PathVariable Long postId , Model model) {

        List<GetLikesByPostIdResponse> getLikesByPostIdResponses = this.likeService.getLikesByPostId(postId);
        model.addAttribute("likeList" , getLikesByPostIdResponses);

        return "/user/user_post_likes";
    }

}
