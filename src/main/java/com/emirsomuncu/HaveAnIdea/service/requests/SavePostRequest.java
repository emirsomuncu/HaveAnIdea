package com.emirsomuncu.HaveAnIdea.service.requests;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavePostRequest {

    private int id ;

    @Size(min = 1 , message = "You have to fill blank")
    private String title ;

    @Size(min = 1 , message = "You have to fill blank")
    private String text ;
    private Long userId;

}
