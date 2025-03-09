package com.emirsomuncu.HaveAnIdea.security;

import com.emirsomuncu.HaveAnIdea.dao.UserDao;
import com.emirsomuncu.HaveAnIdea.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao ;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

       Optional<User> user = this.userDao.findByEmail(email);

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
