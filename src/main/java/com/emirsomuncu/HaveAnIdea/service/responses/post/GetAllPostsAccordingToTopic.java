package com.emirsomuncu.HaveAnIdea.service.responses.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllPostsAccordingToTopic {

    private Long id ;
    private String title;
    private String text ;
    private String createdAt ;
    private String userUsername;
    private Long userId;

}
