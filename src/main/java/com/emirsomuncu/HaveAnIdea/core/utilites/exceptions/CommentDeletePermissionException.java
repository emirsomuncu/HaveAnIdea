package com.emirsomuncu.HaveAnIdea.core.utilites.exceptions;

import com.emirsomuncu.HaveAnIdea.entities.Comment;

public class CommentDeletePermissionException extends RuntimeException{

    public CommentDeletePermissionException(String message) {
        super(message);
    }
}
