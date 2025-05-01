package com.emirsomuncu.HaveAnIdea.service.concretes;

import com.emirsomuncu.HaveAnIdea.core.utilites.mappers.ModelMapperService;
import com.emirsomuncu.HaveAnIdea.repository.UserRepository;
import com.emirsomuncu.HaveAnIdea.entities.User;
import com.emirsomuncu.HaveAnIdea.service.abstracts.UserService;
import com.emirsomuncu.HaveAnIdea.service.requests.SaveUserRequest;
import com.emirsomuncu.HaveAnIdea.service.requests.UpdateUserRequest;
import com.emirsomuncu.HaveAnIdea.service.responses.user.GetAllUserResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.user.GetUserByIdResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.user.GetUserByRoleResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.user.GetUserByUsernameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapperService modelMapperService ;

    @Override
    public void saveUser(SaveUserRequest saveUserRequest) {

        User user = this.modelMapperService.forRequest().map(saveUserRequest, User.class);
        userRepository.save(user);

    }

    @Override
    public void updateUser(UpdateUserRequest updateUserRequest) {

        User user = this.modelMapperService.forRequest().map(updateUserRequest , User.class);
        this.userRepository.save(user);

    }

    @Override
    public List<GetAllUserResponse> getAllUser() {

        List<User> userList = this.userRepository.findAll();
        List<GetAllUserResponse> getAllUserResponses = userList.stream().map(allUser->this.modelMapperService
                .forResponse().map(allUser , GetAllUserResponse.class)).toList();

        return getAllUserResponses;
    }

    @Override
    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public GetUserByIdResponse getUserById(Long id) {

        Optional<User> user = this.userRepository.findById(id);
        GetUserByIdResponse getUserByIdResponse = this.modelMapperService.forResponse().map(user,GetUserByIdResponse.class);

        return getUserByIdResponse;
    }

    @Override
    public List<GetUserByUsernameResponse> getUserByUsername(String username) {
        List<User> userList = this.userRepository.findUserByUsernameContainingIgnoreCase(username);
        List<GetUserByUsernameResponse> getUserByUsernameResponses = userList.stream().map(user -> this.modelMapperService
                .forResponse().map(user , GetUserByUsernameResponse.class)).toList();
        return getUserByUsernameResponses;
    }

    @Override
    public List<GetUserByRoleResponse> getUserByRole(String role) {
        List<User> userList = this.userRepository.findUserByRole(role);
        List<GetUserByRoleResponse> getUserByRoleResponses = userList.stream().map(user -> this.modelMapperService
                .forResponse().map(user , GetUserByRoleResponse.class )).toList();

        return getUserByRoleResponses ;
    }

    @Override
    public Long countUsers() {

        Long userCount = this.userRepository.count();
        return userCount;
    }

    @Override
    public Long countAdmins() {
        String role = "ADMIN,USER";
        Long adminCount = this.userRepository.countByRole(role);
        return adminCount;
    }

    @Override
    public List<User> getRandomFiveUser() {
        return this.userRepository.getRandomFiveUser();
    }

}
