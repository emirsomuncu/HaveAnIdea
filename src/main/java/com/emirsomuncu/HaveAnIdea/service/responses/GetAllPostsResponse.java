package com.emirsomuncu.HaveAnIdea.service.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllPostsResponse {

    private Long id ;

    private String title;

    private String text ;

    private String createdAt ;

    private String userUsername;

    private Long userId;

}
