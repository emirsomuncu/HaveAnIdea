package com.emirsomuncu.HaveAnIdea.security;

import com.emirsomuncu.HaveAnIdea.repository.UserRepository;
import com.emirsomuncu.HaveAnIdea.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

       Optional<User> user = this.userRepository.findByEmail(email);

        if (user.isPresent()) {
            var userObj = user.get();

            return org.springframework.security.core.userdetails.User.builder()
                    .username(userObj.getEmail())
                    .password(userObj.getPassword())
                    .roles(getRoles(userObj))
                    .build();
        }else {
            throw new UsernameNotFoundException("User Not Found");
        }

    }

        public String[] getRoles(User user) {
            if(user.getRole() == null) {
                return new String[]{"USER"} ;
            }
            return user.getRole().split(",");
        }

}
