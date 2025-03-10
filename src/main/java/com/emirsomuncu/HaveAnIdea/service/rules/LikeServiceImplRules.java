package com.emirsomuncu.HaveAnIdea.service.rules;

import com.emirsomuncu.HaveAnIdea.core.utilites.exceptions.ShowPostLikesPermissionException;
import com.emirsomuncu.HaveAnIdea.repository.PostRepository;
import com.emirsomuncu.HaveAnIdea.repository.UserRepository;
import com.emirsomuncu.HaveAnIdea.entities.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeServiceImplRules {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public void checkUserToShowLikes(Long postId) {

       Optional<Post> post = this.postRepository.findById(postId);
       Long userId = post.get().getUser().getId();

       User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       String email = user.getUsername();
       Optional<com.emirsomuncu.HaveAnIdea.entities.User> currentUser = this.userRepository.findByEmail(email);

       if(!currentUser.get().getId().equals(userId)) {
           throw new ShowPostLikesPermissionException("You can only see those who liked your posts");
       }


    }

}
