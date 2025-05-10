package com.emirsomuncu.HaveAnIdea.controllers;

import com.emirsomuncu.HaveAnIdea.entities.Comment;
import com.emirsomuncu.HaveAnIdea.entities.Post;
import com.emirsomuncu.HaveAnIdea.entities.User;
import com.emirsomuncu.HaveAnIdea.service.abstracts.CommentService;
import com.emirsomuncu.HaveAnIdea.service.abstracts.PostService;
import com.emirsomuncu.HaveAnIdea.service.abstracts.UserService;
import com.emirsomuncu.HaveAnIdea.service.requests.SaveUserRequest;
import com.emirsomuncu.HaveAnIdea.service.responses.post.GetAllPostsAccordingToTopic;
import com.emirsomuncu.HaveAnIdea.service.responses.post.GetPostByIdResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.user.GetAllUserResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.user.GetUserByRoleResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.user.GetUserByUsernameResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class AdminController {

    private final UserService userService ;
    private final PostService postService ;
    private final CommentService commentService ;
    private final PasswordEncoder passwordEncoder ;

    @GetMapping("/admin/admin-selection-page")
    public String adminSelectionPage() {
        return "/admin/admin_selection_page";
    }

    @GetMapping("/admin/home")
    public String adminHomePage(Model model ) {

        Long userCount = this.userService.countUsers();
        model.addAttribute("userCount" , userCount );

        Long adminCount = this.userService.countAdmins();
        model.addAttribute("adminCount" , adminCount) ;

        Long postCount = this.postService.countPosts();
        model.addAttribute("postCount" , postCount);

        Long commentCount = this.commentService.countComments();
        model.addAttribute("commentCount" , commentCount);



        return "/admin/admin_home";
    }


    @GetMapping("/admin/manage-user")
    public String adminManageUser(@RequestParam(value = "name" , required = false) String name , Model model) {

        if( name != null ) {

            List<GetUserByUsernameResponse> getUserByUsernameResponse = this.userService.getUserByUsername(name);
            model.addAttribute("name" , name);
            model.addAttribute("getUserByUsernameResponse" , getUserByUsernameResponse);
            return "/admin/admin_manage_user_search";
        }

        else{
            List<GetAllUserResponse> getAllUserResponses = this.userService.getAllUser();
            model.addAttribute("getAllUserResponses" , getAllUserResponses) ;

        }

        return "/admin/admin_manage_users";
    }

    @GetMapping("/admin/delete-user")
    public String deleteUser(@RequestParam Long id) {

        this.userService.deleteUser(id);

        return "redirect:/admin/manage-user";
    }

    @GetMapping("/admin/view-desired-user-post")
    public String viewDesiredUserPost(@RequestParam Long id , Model model) {

        List<Post> postList = this.postService.getPostByUserId(id);
        model.addAttribute("postList", postList) ;

        return "/admin/admin_desired_user_post";
    }

    @RequestMapping("/admin/delete-post")
    public String deletePost(@RequestParam Long id , @RequestParam Long userId) {

        this.postService.deletePost(id);
        return "redirect:/admin/view-desired-user-post?id=" + userId;
    }


    @GetMapping("/admin/view-desired-user-comment")
    public String viewDesiredUserComments(@RequestParam Long id , Model model) {

        List<Comment> commentList = this.commentService.getCommentsByUserId(id);
        model.addAttribute("commentList", commentList) ;

        return "/admin/admin_desired_user_comments";
    }

    @RequestMapping("/admin/delete-comment")
    public String deleteComment(@RequestParam Long id , @RequestParam Long userId) {

        this.commentService.deleteComment(id);
        return "redirect:/admin/view-desired-user-comment?id=" + userId;
    }

    @GetMapping("/admin/manage-topic")
    public String adminManageTopic(@RequestParam(value = "topicName" , required = false) String topicName , Model model) {

        if( topicName != null ) {

            List<String> searchedTopicList = this.postService.searchTopic(topicName);
            model.addAttribute("topicName" , topicName);
            model.addAttribute("topicList" , searchedTopicList);
            return "/admin/admin_manage_topic_search";
        }

        else{
            List<String> uniqueTopicList = this.postService.topicList()
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
            model.addAttribute("uniqueTopicList" , uniqueTopicList) ;
        }

        return "/admin/admin_manage_topics";
    }

    @GetMapping("/admin/view-desired-topic-posts")
    public String desiredTopicPosts(@RequestParam String topicName , Model model) {

        List<GetAllPostsAccordingToTopic> topicsPosts = this.postService.getAllPostsAccordingToTopic(topicName);
        model.addAttribute("topicName" , topicName);
        model.addAttribute("posts" , topicsPosts);
        return "/admin/admin_desired_topic_posts";
    }

    @RequestMapping("/admin/delete-topic-post")
    public String deleteDesiredTopicPosts(@RequestParam Long id) {

        GetPostByIdResponse post = this.postService.getPostById(id);
        String topicName = post.getTitle();
        this.postService.deletePost(id);

        return "redirect:/admin/view-desired-topic-posts?topicName=" + topicName;
    }

    @RequestMapping("/admin/delete-topic")
    public String deleteTopicAndTopicsPosts(@RequestParam String topicName) {

        List<GetAllPostsAccordingToTopic> postList = this.postService.getAllPostsAccordingToTopic(topicName);
        for(GetAllPostsAccordingToTopic postListRunner : postList) {
            this.postService.deletePost(postListRunner.getId());
        }

        return "redirect:/admin/manage-topic";
    }


    @GetMapping("/admin/admin-register")
    public String adminRegister(Model model) {

        SaveUserRequest saveUserRequest = new SaveUserRequest();
        model.addAttribute("saveUserRequest",saveUserRequest);
        return "/admin/admin_register_form";
    }

    @PostMapping("/admin/save-admin")
    public String adminSave(@Valid @ModelAttribute("saveUserRequest") SaveUserRequest saveUserRequest, BindingResult bindingResult){

        Optional<User> user=this.userService.findUserByEmail(saveUserRequest.getEmail());

        if(user.isPresent()) {
            bindingResult.addError(new FieldError("saveUserRequest" , "email" , "Email is already used"));
        }

        if(!saveUserRequest.getEmail().endsWith("@gmail.com")) {
            bindingResult.addError(new FieldError("saveUserRequest" , "email" , "Email must end with @gmail.com "));
        }

        if (!saveUserRequest.getPassword().equals(saveUserRequest.getConfirmPassword())) {
            bindingResult.addError(new FieldError("saveUserRequest", "confirmPassword" , "Password and Confirm Password do not match"));
        }

        if (bindingResult.hasErrors()) {
            return "/admin/admin_register_form";
        }

        saveUserRequest.setPassword(this.passwordEncoder.encode(saveUserRequest.getPassword()));
        saveUserRequest.setRole("ADMIN,USER");
        this.userService.saveUser(saveUserRequest);
        return "redirect:/admin/manage";
    }

    @GetMapping("/admin/admin-list")
    public String adminList(Model model) {

        List<GetUserByRoleResponse> adminList = this.userService.getUserByRole("ADMIN,USER");
        model.addAttribute("adminList" , adminList);

        return "/admin/admin_list";
    }


}
