package com.emirsomuncu.HaveAnIdea.service.rules;

import com.emirsomuncu.HaveAnIdea.core.utilites.exceptions.CommentDeletePermissionException;
import com.emirsomuncu.HaveAnIdea.dao.CommentDao;
import com.emirsomuncu.HaveAnIdea.dao.UserDao;
import com.emirsomuncu.HaveAnIdea.entities.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImplRules {

    @Autowired
    private CommentDao commentDao ;

    @Autowired
    private UserDao userDao ;

    public void checkUserToDeleteComments(Long id) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = currentUser.getUsername();

        Optional<com.emirsomuncu.HaveAnIdea.entities.User> user = this.userDao.findByEmail(email);
        Optional<Comment> comment = this.commentDao.findById(id);

        if( (!comment.get().getUser().getId().equals(user.get().getId())) && (!user.get().getRole().equals("ADMIN,USER") ) ) {
            throw new CommentDeletePermissionException("You have no permission to delete this comment");
        }
    }


}
