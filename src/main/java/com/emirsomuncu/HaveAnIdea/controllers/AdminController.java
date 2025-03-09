package com.emirsomuncu.HaveAnIdea.controllers;

import com.emirsomuncu.HaveAnIdea.entities.Comment;
import com.emirsomuncu.HaveAnIdea.entities.Post;
import com.emirsomuncu.HaveAnIdea.entities.User;
import com.emirsomuncu.HaveAnIdea.service.abstracts.CommentService;
import com.emirsomuncu.HaveAnIdea.service.abstracts.PostService;
import com.emirsomuncu.HaveAnIdea.service.abstracts.UserService;
import com.emirsomuncu.HaveAnIdea.service.requests.SaveUserRequest;
import com.emirsomuncu.HaveAnIdea.service.responses.GetAllUserResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.GetUserByUsernameResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    private UserService userService ;

    @Autowired
    private PostService postService ;

    @Autowired
    private CommentService commentService ;

    @Autowired
    private PasswordEncoder passwordEncoder ;

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


    @GetMapping("/admin/manage")
    public String adminManagePage(@RequestParam(value = "name" , required = false) String name , Model model) {

        if( name != null ) {

            List<GetUserByUsernameResponse> getUserByUsernameResponse = this.userService.getUserByUsername(name);
            model.addAttribute("name" , name);
            model.addAttribute("getUserByUsernameResponse" , getUserByUsernameResponse);
            return "/admin/admin_manage_search";
        }

        else{
            List<GetAllUserResponse> getAllUserResponses = this.userService.getAllUser();
            model.addAttribute("getAllUserResponses" , getAllUserResponses) ;

        }

        return "/admin/admin_manage";
    }

    @GetMapping("/admin/delete-user")
    public String deleteUser(@RequestParam Long id) {

        this.userService.deleteUser(id);

        return "redirect:/admin/manage";
    }

    @GetMapping("/admin/view-desired-user-post")
    public String viewDesiredUserPost(@RequestParam Long id , Model model) {

        List<Post> postList = this.postService.getPostByUserId(id);
        model.addAttribute("postList", postList) ;

        return "/admin/admin_desired_user_post";
    }

    @RequestMapping("/admin/delete-post")
    public String deletePost(Long id) {

        this.postService.deletePost(id);
        return "redirect:/admin/manage";
    }


    @GetMapping("/admin/view-desired-user-comment")
    public String viewDesiredUserComments(@RequestParam Long id , Model model) {

        List<Comment> commentList = this.commentService.getCommentsByUserId(id);
        model.addAttribute("commentList", commentList) ;

        return "/admin/admin_desired_user_comments";
    }

    @RequestMapping("/admin/delete-comment")
    public String deleteComment(@RequestParam Long id) {

        this.commentService.deleteComment(id);
        return "redirect:/admin/manage";
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



}
