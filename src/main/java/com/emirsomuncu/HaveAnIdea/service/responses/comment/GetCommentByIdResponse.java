package com.emirsomuncu.HaveAnIdea.service.responses.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCommentByIdResponse {

    private Long id ;
    private String text ;
    private Date createdAt;
    private Long postId;
    private Long userId;

}
