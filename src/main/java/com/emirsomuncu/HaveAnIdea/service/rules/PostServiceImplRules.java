package com.emirsomuncu.HaveAnIdea.service.rules;

import com.emirsomuncu.HaveAnIdea.core.utilites.exceptions.post.PostDeletePermissionException;
import com.emirsomuncu.HaveAnIdea.repository.PostRepository;
import com.emirsomuncu.HaveAnIdea.repository.UserRepository;
import com.emirsomuncu.HaveAnIdea.entities.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostServiceImplRules {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;
    public void checkUserToDeletePost(Long id) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = currentUser.getUsername();

        Optional<com.emirsomuncu.HaveAnIdea.entities.User> user = this.userRepository.findByEmail(email);
        Optional<Post> post = this.postRepository.findById(id);


        if( (!post.get().getUser().getId().equals(user.get().getId()) ) && (!user.get().getRole().equals("ADMIN,USER") ) ) {
            throw new PostDeletePermissionException("You have no permission to delete this post");
        }
    }







}
