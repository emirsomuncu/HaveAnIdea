package com.emirsomuncu.HaveAnIdea.core.utilites.handlers;

import com.emirsomuncu.HaveAnIdea.core.utilites.exceptions.PostDeletePermissionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class PostServiceImplRulesHandler {


    @ExceptionHandler
    public String handleCheckUserToDeletePost(PostDeletePermissionException postDeletePermissionException) {
        return "/error/handle_check_user_to_delete_post_error";
    }

}
