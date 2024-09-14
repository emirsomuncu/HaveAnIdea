package com.emirsomuncu.HaveAnIdea.service.requests;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveUserRequest {

    int id ;

    @Size(min = 1 , message = "You have to fill blank")
    private String username;

    @Size(min = 1 , message = "You have to fill blank")
    private String email;

    @Size(min = 6 , message = "Minimum password length is 6 characters")
    private String password ;

    private String role ;

    @Size(min = 1 , message = "You have to fill blank")
    private String confirmPassword ;

}
