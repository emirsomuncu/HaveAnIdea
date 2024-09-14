package com.emirsomuncu.HaveAnIdea.service.concretes;

import com.emirsomuncu.HaveAnIdea.core.utilites.mappers.ModelMapperService;
import com.emirsomuncu.HaveAnIdea.dao.UserDao;
import com.emirsomuncu.HaveAnIdea.entities.User;
import com.emirsomuncu.HaveAnIdea.service.abstracts.UserService;
import com.emirsomuncu.HaveAnIdea.service.requests.SaveUserRequest;
import com.emirsomuncu.HaveAnIdea.service.requests.UpdateUserRequest;
import com.emirsomuncu.HaveAnIdea.service.responses.GetAllUserResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.GetUserByIdResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.GetUserByRoleResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.GetUserByUsernameResponse;
import com.emirsomuncu.HaveAnIdea.service.rules.UserServiceImplRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao ;

    @Autowired
    private ModelMapperService modelMapperService ;


    @Override
    public void saveUser(SaveUserRequest saveUserRequest) {

        User user = this.modelMapperService.forRequest().map(saveUserRequest, User.class);
        userDao.save(user);

    }

    @Override
    public void updateUser(UpdateUserRequest updateUserRequest) {

        User user = this.modelMapperService.forRequest().map(updateUserRequest , User.class);
        this.userDao.save(user);

    }

    @Override
    public List<GetAllUserResponse> getAllUser() {

        List<User> userList = this.userDao.findAll();
        List<GetAllUserResponse> getAllUserResponses = userList.stream().map(allUser->this.modelMapperService
                .forResponse().map(allUser , GetAllUserResponse.class)).toList();

        return getAllUserResponses;
    }

    @Override
    public void deleteUser(Long id) {
        this.userDao.deleteById(id);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return this.userDao.findByEmail(email);
    }

    @Override
    public GetUserByIdResponse getUserById(Long id) {

        Optional<User> user = this.userDao.findById(id);
        GetUserByIdResponse getUserByIdResponse = this.modelMapperService.forResponse().map(user,GetUserByIdResponse.class);

        return getUserByIdResponse;
    }

    @Override
    public List<GetUserByUsernameResponse> getUserByUsername(String username) {
        List<User> userList = this.userDao.findUserByUsernameContainingIgnoreCase(username);
        List<GetUserByUsernameResponse> getUserByUsernameResponses = userList.stream().map(user -> this.modelMapperService
                .forResponse().map(user , GetUserByUsernameResponse.class)).toList();
        return getUserByUsernameResponses;
    }

    @Override
    public List<GetUserByRoleResponse> getUserByRole(String role) {
        List<User> userList = this.userDao.findUserByRole(role);
        List<GetUserByRoleResponse> getUserByRoleResponses = userList.stream().map(user -> this.modelMapperService
                .forResponse().map(user , GetUserByRoleResponse.class )).toList();

        return getUserByRoleResponses ;
    }

    @Override
    public Long countUsers() {

        Long userCount = this.userDao.count();
        return userCount;
    }

    @Override
    public Long countAdmins() {
        String role = "ADMIN,USER";
        Long adminCount = this.userDao.countByRole(role);
        return adminCount;
    }
}
