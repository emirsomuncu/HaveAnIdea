package com.emirsomuncu.HaveAnIdea.service.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPostByIdResponse {

    private Long id ;

    private String title;

    private String text ;

    private String userUsername;

    private String userId;
}
