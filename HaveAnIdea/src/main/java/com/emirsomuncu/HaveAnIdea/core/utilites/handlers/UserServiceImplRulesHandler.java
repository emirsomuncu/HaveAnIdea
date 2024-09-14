package com.emirsomuncu.HaveAnIdea.core.utilites.handlers;

import com.emirsomuncu.HaveAnIdea.core.utilites.exceptions.UserUpdatePermissionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserServiceImplRulesHandler {


    @ExceptionHandler
    public String handleUserUpdatePermissionException(UserUpdatePermissionException userUpdatePermissionException) {
        return "/error/handle_check_user_to_update_profile";
    }


}
