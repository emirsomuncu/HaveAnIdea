package com.emirsomuncu.HaveAnIdea.core.utilites.handlers;

import com.emirsomuncu.HaveAnIdea.core.utilites.exceptions.ShowPostLikesPermissionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LikeServiceImplRulesHandler {


    @ExceptionHandler
    public String handleShowPostLikesPermissionException(ShowPostLikesPermissionException showPostLikesPermissionException) {
        return "/error/handle_check_user_to_show_likes";
    }


}

