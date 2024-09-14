package com.emirsomuncu.HaveAnIdea.service.rules;

import com.emirsomuncu.HaveAnIdea.core.utilites.exceptions.UserUpdatePermissionException;
import com.emirsomuncu.HaveAnIdea.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImplRules {

    @Autowired
    private UserDao userDao ;

    public void checkUserToUpdateProfile(Long userId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        Optional<com.emirsomuncu.HaveAnIdea.entities.User> currentUser = this.userDao.findByEmail(email);

        if(!userId.equals(currentUser.get().getId())) {
            throw new UserUpdatePermissionException("You cannot update another users' profile");
        }


    }



}
