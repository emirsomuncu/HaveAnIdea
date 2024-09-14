package com.emirsomuncu.HaveAnIdea.service.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDesiredUserCommentsByUserIdResponse {

    private Long id ;
    private String text;
    private String userUsername;
    private Long userId;
    private Long postId;

}
