package com.emirsomuncu.HaveAnIdea.controllers;

import com.emirsomuncu.HaveAnIdea.entities.Post;
import com.emirsomuncu.HaveAnIdea.entities.User;
import com.emirsomuncu.HaveAnIdea.repository.UserRepository;
import com.emirsomuncu.HaveAnIdea.service.abstracts.LikeService;
import com.emirsomuncu.HaveAnIdea.service.abstracts.PostService;
import com.emirsomuncu.HaveAnIdea.service.abstracts.UserService;
import com.emirsomuncu.HaveAnIdea.service.requests.SavePostRequest;
import com.emirsomuncu.HaveAnIdea.service.requests.SaveUserRequest;
import com.emirsomuncu.HaveAnIdea.service.responses.post.GetAllPostsAccordingToTopic;
import com.emirsomuncu.HaveAnIdea.service.responses.post.GetAllPostsResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.post.GetPostByIdResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.user.GetUserByUsernameResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequiredArgsConstructor()
@RequestMapping("/user")
public class PostController {

    private final UserService userService;
    private final PostService postService;
    private final UserRepository userRepository;
    private final LikeService likeService;

    @GetMapping("/create-post")
    public String createPost(Model model) {

        SavePostRequest savePostRequest = new SavePostRequest();
        model.addAttribute("savePostRequest", savePostRequest);

        return "/user/user_create_post_form";
    }

    @PostMapping("/save-post")
    public String savePost(@Valid @ModelAttribute("savePostRequest") SavePostRequest savePostRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/user/user_create_post_form";
        }

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        Optional<User> user1 = this.userService.findUserByEmail(email);
        Long userId = user1.get().getId();

        savePostRequest.setUserId(userId);
        this.postService.savePost(savePostRequest);
        return "redirect:/user/home";
    }

    @RequestMapping("/delete-post")
    public String deleteHomePost(@RequestParam Long id) {

        GetPostByIdResponse postToDelete = this.postService.getPostById(id);

        if (postToDelete == null) {
            return "redirect:/user/home";
        }

        Date deletedPostCreatedAt = postToDelete.getCreatedAt();

        List<GetAllPostsResponse> allPosts = this.postService.getAllPosts();
        GetAllPostsResponse closestPost = null;
        long minDiff = Long.MAX_VALUE;

        for (GetAllPostsResponse post : allPosts) {
            if (post.getId().equals(id)) continue;

            long diff = Math.abs(post.getCreatedAt().getTime() - deletedPostCreatedAt.getTime());
            if (diff < minDiff) {
                minDiff = diff;
                closestPost = post;
            }
        }

        this.postService.deletePost(id);

        if (closestPost != null) {
            return "redirect:/user/home?highlightedPostId=" + closestPost.getId();
        }

        return "redirect:/user/home";
    }

    @RequestMapping("/delete-profile-post")
    public String deletePostFromProfile(@RequestParam Long id) {

        GetPostByIdResponse postToDelete = this.postService.getPostById(id);
        Long userId = Long.valueOf(postToDelete.getUserId());

        Date deletedPostCreatedAt = postToDelete.getCreatedAt();

        List<Post> allPosts = this.postService.getPostByUserId(userId);
        Post closestPost = null;
        long minDiff = Long.MAX_VALUE;

        for (Post post : allPosts) {
            if (post.getId().equals(id)) continue;

            long diff = Math.abs(post.getCreatedAt().getTime() - deletedPostCreatedAt.getTime());
            if (diff < minDiff) {
                minDiff = diff;
                closestPost = post;
            }
        }

        this.postService.deletePost(id);

        if (closestPost != null) {
            return "redirect:/user/user-posts?highlightedPostId=" + closestPost.getId() + "&userId=" + userId;

        }

        return "redirect:/user/user-posts?userId=" + userId;
    }

    @RequestMapping("/delete-topic-post")
    public String deleteTopicPost(@RequestParam Long id) {

        GetPostByIdResponse postToDelete = this.postService.getPostById(id);

        if (postToDelete == null) {
            return "redirect:/user/view-comments";
        }

        Date deletedPostCreatedAt = postToDelete.getCreatedAt();

        List<GetAllPostsResponse> allPosts = this.postService.getAllPosts();
        GetAllPostsResponse closestPost = null;
        long minDiff = Long.MAX_VALUE;

        for (GetAllPostsResponse post : allPosts) {
            if (post.getId().equals(id)) continue;

            long diff = Math.abs(post.getCreatedAt().getTime() - deletedPostCreatedAt.getTime());
            if (diff < minDiff) {
                minDiff = diff;
                closestPost = post;
            }
        }

        Long userId = Long.valueOf(postToDelete.getUserId());
        String topic = postToDelete.getTitle();
        String encodedTopic = URLEncoder.encode(topic, StandardCharsets.UTF_8);
        this.postService.deletePost(id);

        if (closestPost != null) {
            return "redirect:/user/topic/" + encodedTopic + "/posts?highlightedPostId=" + closestPost.getId();
        }

        return "redirect:/user/view-comments";
    }

    @GetMapping("/topic/{topicName}/posts")
    public String getAllPostAccordingToTopic(@PathVariable String topicName , Model model) {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        User currentUser = this.userRepository.findByEmail(email).get();

        List<GetAllPostsAccordingToTopic> posts = this.postService.getAllPostsAccordingToTopic(topicName);

        Map<Long, Long> postLikeCounts = new HashMap<>();
        for (GetAllPostsAccordingToTopic post : posts) {
            Long numberOfLikes = this.likeService.countLikes(post.getId());
            postLikeCounts.put(post.getId(), numberOfLikes);
        }

        List<GetAllPostsAccordingToTopic> topicPosts = this.postService.getAllPostsAccordingToTopic(topicName);
        Long count = 0L;
        for (GetAllPostsAccordingToTopic topicPostsRunner : topicPosts) {
            count++;
        }

        model.addAttribute("postCount", count);
        model.addAttribute("postLikeCounts", postLikeCounts);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("posts", posts);
        return "/user/user_topics_posts";
    }

    @GetMapping("/search-topic")
    public String searchTopic(@RequestParam(value = "topicName", required = false) String topicName, Model model) {

        Map<String, Long> popularTopics = this.postService.popularTopics();
        model.addAttribute("popularTopics", popularTopics);

        if(topicName != null) {

            List<String> topicList = this.postService.searchTopic(topicName);

            model.addAttribute("topicList", topicList );
            model.addAttribute("topicName", topicName);
        }

        return "/user/user_search_topic";
    }

}
