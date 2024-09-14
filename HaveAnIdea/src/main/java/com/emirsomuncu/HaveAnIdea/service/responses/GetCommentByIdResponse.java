package com.emirsomuncu.HaveAnIdea.service.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCommentByIdResponse {

    private Long id ;
    private String text ;
    private Long postId;

}
