package com.emirsomuncu.HaveAnIdea.service.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveLikeRequest {

    private Long id ;

    private Long userId;

    private Long postId;

}
