package com.emirsomuncu.HaveAnIdea.controllers;

import com.emirsomuncu.HaveAnIdea.entities.User;
import com.emirsomuncu.HaveAnIdea.repository.UserRepository;
import com.emirsomuncu.HaveAnIdea.service.abstracts.LikeService;
import com.emirsomuncu.HaveAnIdea.service.abstracts.PostService;
import com.emirsomuncu.HaveAnIdea.service.abstracts.UserService;
import com.emirsomuncu.HaveAnIdea.service.requests.SavePostRequest;
import com.emirsomuncu.HaveAnIdea.service.requests.SaveUserRequest;
import com.emirsomuncu.HaveAnIdea.service.responses.post.GetAllPostsAccordingToTopic;
import com.emirsomuncu.HaveAnIdea.service.responses.post.GetAllPostsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public String deletePost(@RequestParam Long id) {
        this.postService.deletePost(id);
        return "redirect:/user/home";
    }

    @GetMapping("/topic/{topicName}/posts")
    public String getAllPostAccordingToTopic(@PathVariable String topicName , Model model) {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        User currentUser = this.userRepository.findByEmail(email).get();

        List<GetAllPostsAccordingToTopic> posts = this.postService.getAllPostsAccordingToTopic(topicName);

        Map<Long , Long> postLikeCounts = new HashMap<>();
        for(GetAllPostsAccordingToTopic post : posts) {
            Long numberOfLikes = this.likeService.countLikes(post.getId());
            postLikeCounts.put(post.getId() , numberOfLikes );
        }

        model.addAttribute("postLikeCounts" , postLikeCounts);
        model.addAttribute("currentUser" , currentUser);
        model.addAttribute("posts" , posts);
        return "/user/user_topics_posts";
    }

}
