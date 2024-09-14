package com.emirsomuncu.HaveAnIdea.service.requests;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    private Long id ;
    @Size(min = 1 , message = "Fill this blank")
    private String username;
    @Size(min = 1 , message = "Fill this blank")
    private String email;
    @Size(min = 1 , message = "Fill this blank")
    private String password;
    private String role;
    @Size(min = 1 , message = "Fill this blank")
    private String oldPassword;
    @Size(min = 1 , message = "Fill this blank")
    private String confirmPassword;
}
