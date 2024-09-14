package com.emirsomuncu.HaveAnIdea.service.abstracts;

import com.emirsomuncu.HaveAnIdea.entities.User;
import com.emirsomuncu.HaveAnIdea.service.requests.SaveUserRequest;
import com.emirsomuncu.HaveAnIdea.service.requests.UpdateUserRequest;
import com.emirsomuncu.HaveAnIdea.service.responses.GetAllUserResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.GetUserByIdResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.GetUserByRoleResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.GetUserByUsernameResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public void saveUser(SaveUserRequest saveUserRequest) ;
    public void updateUser(UpdateUserRequest updateUserRequest);
    public List<GetAllUserResponse> getAllUser() ;
    public void deleteUser(Long id);
    public Optional<User> findUserByEmail(String email);
    public GetUserByIdResponse getUserById(Long id);
    public List<GetUserByUsernameResponse> getUserByUsername(String username);
    public List<GetUserByRoleResponse> getUserByRole(String role);
    public Long countUsers();
    public Long countAdmins();


}
