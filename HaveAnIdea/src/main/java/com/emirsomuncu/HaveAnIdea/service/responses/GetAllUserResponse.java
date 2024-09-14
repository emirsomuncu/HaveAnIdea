package com.emirsomuncu.HaveAnIdea.service.responses;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllUserResponse {

    private Long id ;

    private String username;

    private String email;

    private String role ;


}
