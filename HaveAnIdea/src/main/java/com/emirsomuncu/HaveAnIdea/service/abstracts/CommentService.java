package com.emirsomuncu.HaveAnIdea.service.abstracts;

import com.emirsomuncu.HaveAnIdea.entities.Comment;
import com.emirsomuncu.HaveAnIdea.service.requests.AddCommentRequest;
import com.emirsomuncu.HaveAnIdea.service.responses.GetCommentByIdResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.GetCommentsByPostIdResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.GetDesiredUserCommentsByUserIdResponse;

import java.util.List;

public interface CommentService {

    public List<Comment> getCommentsByUserId(Long id);
    public List<GetDesiredUserCommentsByUserIdResponse> getDesiredUserCommentsByUserId(Long id);

    public GetCommentByIdResponse getCommentById(Long id);
    public void deleteComment(Long id);
    public List<GetCommentsByPostIdResponse> getCommentsByPostId(Long id);
    public void saveComment(AddCommentRequest addCommentRequest);
    public Long countComments();
}
