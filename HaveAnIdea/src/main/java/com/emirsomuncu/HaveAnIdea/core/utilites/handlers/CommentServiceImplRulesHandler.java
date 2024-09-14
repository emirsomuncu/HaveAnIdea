package com.emirsomuncu.HaveAnIdea.core.utilites.handlers;

import com.emirsomuncu.HaveAnIdea.core.utilites.exceptions.CommentDeletePermissionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommentServiceImplRulesHandler {


    @ExceptionHandler
    public String handleCheckUserToDeleteComment(CommentDeletePermissionException commentDeletePermissionException) {
        return "/error/handle_check_user_to_delete_comment_error";
    }




}
