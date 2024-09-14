package com.emirsomuncu.HaveAnIdea.service.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLikesByPostIdResponse {

    private Long id ;
    private String userUsername;
    private Long userId;


}
