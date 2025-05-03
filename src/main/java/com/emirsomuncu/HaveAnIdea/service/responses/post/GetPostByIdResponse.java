package com.emirsomuncu.HaveAnIdea.service.responses.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPostByIdResponse {

    private Long id ;
    private String title;
    private String text ;
    private Date createdAt;
    private String userUsername;
    private String userId;
    private String userRole;

}
