package com.emirsomuncu.HaveAnIdea.controllers;

import com.emirsomuncu.HaveAnIdea.entities.Comment;
import com.emirsomuncu.HaveAnIdea.entities.User;
import com.emirsomuncu.HaveAnIdea.service.abstracts.CommentService;
import com.emirsomuncu.HaveAnIdea.service.abstracts.PostService;
import com.emirsomuncu.HaveAnIdea.service.abstracts.UserService;
import com.emirsomuncu.HaveAnIdea.service.requests.AddCommentRequest;
import com.emirsomuncu.HaveAnIdea.service.responses.comment.GetCommentByIdResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.comment.GetCommentsByPostIdResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.post.GetAllPostsResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.post.GetPostByIdResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class CommentController {

    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;

    @GetMapping("/view-comments")
    public String viewComments(@RequestParam Long id, @RequestParam(value = "highlightedPostId", required = false) Long highlightedPostId, Model model) {

        GetPostByIdResponse getPostByIdResponse = this.postService.getPostById(id);
        model.addAttribute("getPostByIdResponse", getPostByIdResponse);

        Long postId = getPostByIdResponse.getId();
        List<GetCommentsByPostIdResponse> getCommentsByPostIdResponses = this.commentService.getCommentsByPostId(postId);
        model.addAttribute("getCommentsByPostIdResponses", getCommentsByPostIdResponses);

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        Optional<User> currentUser = this.userService.findUserByEmail(email);
        model.addAttribute("currentUser", currentUser);

        model.addAttribute("highlightedPostId", highlightedPostId);

        return "/user/user_view_comments";
    }

    @RequestMapping("/add-comment")
    public String addComment(@RequestParam Long postId, Model model) {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();

        Optional<User> currentUser = this.userService.findUserByEmail(email);
        Long userId = currentUser.get().getId();

        AddCommentRequest addCommentRequest = new AddCommentRequest();
        addCommentRequest.setPostId(postId);
        addCommentRequest.setUserId(userId);

        model.addAttribute("addCommentRequest", addCommentRequest);

        return "/user/user_add_comment";
    }

    @PostMapping("/save-comment")
    public String saveComment(@Valid @ModelAttribute("addCommentRequest") AddCommentRequest addCommentRequest, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "/user/user_add_comment";
        } else {
            this.commentService.saveComment(addCommentRequest);
        }
        Long postId = addCommentRequest.getPostId();

        return "redirect:/user/home?highlightedPostId=" + postId;
    }

    @RequestMapping("/delete-comment")
    public String deleteComment(@RequestParam Long id) {
        GetCommentByIdResponse getCommentByIdResponse = this.commentService.getCommentById(id);
        Long postId = getCommentByIdResponse.getPostId();
        this.commentService.deleteComment(id);
        return "redirect:/user/home?highlightedPostId=" + postId;
    }

    @RequestMapping("/delete-profile-comment")
    public String deleteCommentFromProfile(@RequestParam Long id) {

        GetCommentByIdResponse comment = this.commentService.getCommentById(id);
        Long userId = comment.getUserId();
        Date deletedCommentCreatedAt = comment.getCreatedAt();

        List<Comment> allComments = this.commentService.getCommentsByUserId(userId);  // yorum göre düzenle
        Comment closestComment = null;
        long minDiff = Long.MAX_VALUE;

        for (Comment comments : allComments) {
            if (comments.getId().equals(id)) continue;

            long diff = Math.abs(comments.getCreatedAt().getTime() - deletedCommentCreatedAt.getTime());
            if (diff < minDiff) {
                minDiff = diff;
                closestComment = comments;
            }
        }

        this.commentService.deleteComment(id);

        if (closestComment != null) {
            return "redirect:/user/user-comments?highlightedCommentId=" + closestComment.getId() + "&userId=" + userId;
        }

        return "redirect:/user/user-comments?userId=" + userId ;
    }

}
