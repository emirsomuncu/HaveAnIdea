package com.emirsomuncu.HaveAnIdea.service.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserByIdResponse {

    private Long id ;
    private String username;
    private String email;
    private String password ;
    private String role ;
}
