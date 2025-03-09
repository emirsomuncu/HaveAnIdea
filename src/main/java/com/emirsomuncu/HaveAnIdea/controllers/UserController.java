package com.emirsomuncu.HaveAnIdea.controllers;

import com.emirsomuncu.HaveAnIdea.dao.UserDao;
import com.emirsomuncu.HaveAnIdea.entities.Comment;
import com.emirsomuncu.HaveAnIdea.entities.Post;
import com.emirsomuncu.HaveAnIdea.entities.User;
import com.emirsomuncu.HaveAnIdea.service.abstracts.CommentService;
import com.emirsomuncu.HaveAnIdea.service.abstracts.LikeService;
import com.emirsomuncu.HaveAnIdea.service.abstracts.PostService;
import com.emirsomuncu.HaveAnIdea.service.abstracts.UserService;
import com.emirsomuncu.HaveAnIdea.service.requests.AddCommentRequest;
import com.emirsomuncu.HaveAnIdea.service.requests.SavePostRequest;
import com.emirsomuncu.HaveAnIdea.service.requests.SaveUserRequest;
import com.emirsomuncu.HaveAnIdea.service.requests.UpdateUserRequest;
import com.emirsomuncu.HaveAnIdea.service.responses.*;
import com.emirsomuncu.HaveAnIdea.service.rules.LikeServiceImplRules;
import com.emirsomuncu.HaveAnIdea.service.rules.UserServiceImplRules;
import jakarta.validation.Valid;
import org.hibernate.engine.jdbc.mutation.spi.BindingGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.model.IModel;

import java.security.Principal;
import java.util.*;
import java.util.function.Predicate;

@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserServiceImplRules userServiceImplRules;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {

        SaveUserRequest saveUserRequest = new SaveUserRequest();
        model.addAttribute(saveUserRequest);

        return "/user/register_form";

    }

    @PostMapping("/register/save-user")
    public String saveRegisterForm(@Valid @ModelAttribute("saveUserRequest") SaveUserRequest saveUserRequest, BindingResult bindingResult, Model model) {


        Optional<User> user = this.userService.findUserByEmail(saveUserRequest.getEmail());
        if (user.isPresent()) {
            bindingResult.addError(
                    new FieldError("saveUserRequest", "email", "Email is already used")
            );
        }

        if (!saveUserRequest.getEmail().endsWith("@gmail.com")) {
            bindingResult.addError(new FieldError("saveUserRequest", "email", "Email must end with @gmail.com "));
        }

        if (!saveUserRequest.getPassword().equals(saveUserRequest.getConfirmPassword())) {
            bindingResult.addError(
                    new FieldError("saveUserRequest", "confirmPassword", "Password and Confirm Password do not match"));
        }

        if (bindingResult.hasErrors()) {
            return "/user/register_form";
        } else {
            saveUserRequest.setPassword(passwordEncoder.encode(saveUserRequest.getPassword()));
            saveUserRequest.setRole("USER");
            userService.saveUser(saveUserRequest);

            model.addAttribute("succes", true);
            return "/user/register_form";
        }

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
    public String userHomePage(Model model) {

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


        return "/user/user_home";
    }

    @GetMapping("/user/view-comments")
    public String viewComments(@RequestParam Long id, Model model) {

        GetPostByIdResponse getPostByIdResponse = this.postService.getPostById(id);
        model.addAttribute("getPostByIdResponse", getPostByIdResponse);

        Long postId = getPostByIdResponse.getId();
        List<GetCommentsByPostIdResponse> getCommentsByPostIdResponses = this.commentService.getCommentsByPostId(postId);
        model.addAttribute("getCommentsByPostIdResponses", getCommentsByPostIdResponses);

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        Optional<User> currentUser = this.userService.findUserByEmail(email);
        model.addAttribute("currentUser", currentUser);

        return "/user/user_view_comments";
    }

    @RequestMapping("/user/add-comment")
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

    @PostMapping("/user/save-comment")
    public String saveComment(@Valid @ModelAttribute("addCommentRequest") AddCommentRequest addCommentRequest, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "/user/user_add_comment";
        } else {
            this.commentService.saveComment(addCommentRequest);
        }
        Long postId = addCommentRequest.getPostId();
        return "redirect:/user/view-comments?id=" + postId;
    }

    @RequestMapping("/user/delete-post")
    public String deletePost(@RequestParam Long id) {
        this.postService.deletePost(id);
        return "redirect:/user/home";
    }

    @RequestMapping("/user/delete-comment")
    public String deleteComment(@RequestParam Long id) {
        GetCommentByIdResponse getCommentByIdResponse = this.commentService.getCommentById(id);
        Long postId = getCommentByIdResponse.getPostId();
        this.commentService.deleteComment(id);
        return "redirect:/user/view-comments?id=" + postId;
    }


    @RequestMapping("/user/post-likes")
    public String postLikes(@RequestParam Long postId , Model model ) {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        Optional<User> currentUser = this.userService.findUserByEmail(email);

        this.likeService.saveLike(postId , currentUser.get().getId() );
        return "redirect:/user/home";
    }

    @GetMapping("/user/{postId}/likes")
    public String usersPostLikes(@PathVariable Long postId , Model model) {

        List<GetLikesByPostIdResponse> getLikesByPostIdResponses = this.likeService.getLikesByPostId(postId);
        model.addAttribute("likeList" , getLikesByPostIdResponses);

        return "/user/user_post_likes";
    }

    @GetMapping("/user/create-post")
    public String createPost(Model model) {

        SavePostRequest savePostRequest = new SavePostRequest();
        model.addAttribute("savePostRequest", savePostRequest);

        return "/user/user_create_post_form";
    }

    @PostMapping("/user/save-post")
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

    @GetMapping("/user/profile")
    public String userProfile(@RequestParam("userId") Long userId, Model model) {

        GetUserByIdResponse getUserByIdResponse = this.userService.getUserById(userId);
        model.addAttribute("getUserByIdResponse", getUserByIdResponse);

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        Optional<User> currentUser = this.userService.findUserByEmail(email);
        model.addAttribute("currentUser", currentUser);


        return "/user/user_profile";
    }

    @GetMapping("/user/user-posts")
    public String usersPost(@RequestParam Long userId, Model model) {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        Optional<User> currentUser = this.userService.findUserByEmail(email);
        model.addAttribute("currentUser", currentUser);

        List<GetDesiredUserPostsByUserIdResponse> postList = this.postService.getDesiredUserPostsByUserId(userId);
        model.addAttribute("postList", postList);

        return "/user/user_desired_user_post";
    }


    @GetMapping("/user/user-comments")
    public String usersComments(@RequestParam Long userId, Model model) {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        Optional<User> currentUser = this.userService.findUserByEmail(email);
        model.addAttribute("currentUser", currentUser);

        List<GetDesiredUserCommentsByUserIdResponse> commentList = this.commentService.getDesiredUserCommentsByUserId(userId);
        model.addAttribute("commentList", commentList);

        return "/user/user_desired_user_comments";
    }

    @RequestMapping("/user/delete-account")
    public String deleteUser(@RequestParam Long userId) {

        this.userService.deleteUser(userId);
        return "redirect:/";
    }

    @GetMapping("/user/update-profile")
    public String updateProfileForm(@RequestParam Long userId, Model model) {

        //Normally I had to use this rule method in service layer but my app structure does not allow to use in service so ı used here
        this.userServiceImplRules.checkUserToUpdateProfile(userId);
        GetUserByIdResponse getUserByIdResponse = this.userService.getUserById(userId);

        //I mapped this class to be able to perform operations such as password verification in other controller. Then I realized that it would be okay to use it here.
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setId(getUserByIdResponse.getId());
        updateUserRequest.setUsername(getUserByIdResponse.getUsername());
        updateUserRequest.setEmail(getUserByIdResponse.getEmail());
        updateUserRequest.setPassword(getUserByIdResponse.getPassword());
        updateUserRequest.setRole(getUserByIdResponse.getRole());

        model.addAttribute("updateUserRequest", updateUserRequest);
        return "/user/user_update_profile";
    }

    @RequestMapping("/user/save-profile")
    public String saveProfileChanges(@Valid @ModelAttribute("updateUserRequest") UpdateUserRequest updateUserRequest, BindingResult bindingResult) {


        if (!updateUserRequest.getEmail().endsWith("@gmail.com")) {
            bindingResult.addError(new FieldError("UpdateUserRequest", "email", "Email must end with @gmail.com"));
        }

        if (bindingResult.hasErrors()) {
            return "/user/user_update_profile";
        } else {
            this.userService.updateUser(updateUserRequest);
        }

        return "redirect:/custom-login-page?logout";
    }

    @GetMapping("/user/update-password")
    public String updatePasswordForm(Model model) {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        Optional<User> currentUser = this.userService.findUserByEmail(email);

        //I mapped this class to be able to perform operations such as password verification.
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setId(currentUser.get().getId());
        updateUserRequest.setUsername(currentUser.get().getUsername());
        updateUserRequest.setEmail(currentUser.get().getEmail());
        updateUserRequest.setRole(currentUser.get().getRole());

        model.addAttribute("updateUserRequest", updateUserRequest);

        return "/user/user_update_password";
    }


    @PostMapping("/user/save-password")
    public String savePasswordChanges(@Valid @ModelAttribute("updateUserRequest") UpdateUserRequest updateUserRequest, BindingResult bindingResult) {


        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        Optional<User> currentUser = this.userService.findUserByEmail(email);
        updateUserRequest.setOldPassword(this.passwordEncoder.encode(updateUserRequest.getOldPassword()));
        if (this.passwordEncoder.matches(updateUserRequest.getOldPassword(), currentUser.get().getPassword())) {
            bindingResult.addError(new FieldError("UpdateUserRequest", "oldPassword", "Password is not true"));
        }

        if (!updateUserRequest.getPassword().equals(updateUserRequest.getConfirmPassword())) {
            bindingResult.addError(new FieldError("UpdateUserRequest", "confirmPassword", "New Password And Confirm Password Do Not Match"));
        }

        if (bindingResult.hasErrors()) {
            return "/user/user_update_password";
        } else {

            updateUserRequest.setPassword(this.passwordEncoder.encode(updateUserRequest.getPassword()));
            this.userService.updateUser(updateUserRequest);

        }

        return "redirect:/custom-login-page?logout";
    }

    @GetMapping("/user/search-user")
    public String searchUser(@RequestParam(value = "name", required = false) String name, Model model) {

        if(name != null) {
            List<GetUserByUsernameResponse> userList = this.userService.getUserByUsername(name);
            model.addAttribute("userList", userList);
            model.addAttribute("name", name);
        }

        return "/user/user_search_user";
    }

    @GetMapping("/user/contacts")
    public String contactsInfo(Model model) {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        Optional<User> currentUser = this.userService.findUserByEmail(email);
        model.addAttribute("currentUser" ,currentUser);

        String role = "ADMIN,USER";
        List<GetUserByRoleResponse> adminList = this.userService.getUserByRole(role);
        model.addAttribute("adminList", adminList);

        return "/user/user_contacts";
    }

}


