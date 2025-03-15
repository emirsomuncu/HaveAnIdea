package com.emirsomuncu.HaveAnIdea.service.rules;

import com.emirsomuncu.HaveAnIdea.core.utilites.exceptions.user.UserUpdatePermissionException;
import com.emirsomuncu.HaveAnIdea.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImplRules {

    @Autowired
    private UserRepository userRepository;

    public Boolean checkUserIsExistsByUsername(String username) {
        com.emirsomuncu.HaveAnIdea.entities.User user = this.userRepository.findUserByUsername(username);
        if( user == null) {
            return false;
        }
        else{
            return true;
        }
    }
    public void checkUserToUpdateProfile(Long userId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        Optional<com.emirsomuncu.HaveAnIdea.entities.User> currentUser = this.userRepository.findByEmail(email);

        if(!userId.equals(currentUser.get().getId())) {
            throw new UserUpdatePermissionException("You cannot update another users' profile");
        }


    }



}
