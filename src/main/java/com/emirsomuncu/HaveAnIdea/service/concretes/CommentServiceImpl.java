package com.emirsomuncu.HaveAnIdea.service.concretes;

import com.emirsomuncu.HaveAnIdea.core.utilites.mappers.ModelMapperService;
import com.emirsomuncu.HaveAnIdea.repository.CommentRepository;
import com.emirsomuncu.HaveAnIdea.entities.Comment;
import com.emirsomuncu.HaveAnIdea.service.abstracts.CommentService;
import com.emirsomuncu.HaveAnIdea.service.requests.AddCommentRequest;
import com.emirsomuncu.HaveAnIdea.service.responses.comment.GetCommentByIdResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.comment.GetCommentsByPostIdResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.user.GetDesiredUserCommentsByUserIdResponse;
import com.emirsomuncu.HaveAnIdea.service.rules.CommentServiceImplRules;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapperService modelMapperService;
    private final CommentServiceImplRules commentServiceImplRules ;


    @Override
    public List<Comment> getCommentsByUserId(Long id) {

        return this.commentRepository.findCommentByUserId(id);

    }

    @Override
    public List<GetDesiredUserCommentsByUserIdResponse> getDesiredUserCommentsByUserId(Long id) {

        List<Comment> commentList = this.commentRepository.findCommentByUserId(id);
        List<GetDesiredUserCommentsByUserIdResponse> getDesiredUserCommentsByUserIdResponses = commentList.stream().map(comment -> this.modelMapperService
                .forResponse().map(comment , GetDesiredUserCommentsByUserIdResponse.class)).toList();
        return getDesiredUserCommentsByUserIdResponses;
    }

    @Override
    public GetCommentByIdResponse getCommentById(Long id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        GetCommentByIdResponse getCommentByIdResponse = this.modelMapperService.forResponse().map(comment , GetCommentByIdResponse.class);
        return getCommentByIdResponse;
    }


    @Override
    public void deleteComment(Long id) {
        this.commentServiceImplRules.checkUserToDeleteComments(id);
        this.commentRepository.deleteById(id);
    }

    @Override
    public List<GetCommentsByPostIdResponse> getCommentsByPostId(Long id) {

        List<Comment> commentList = this.commentRepository.findCommentByPostId(id);
        List<GetCommentsByPostIdResponse> getCommentsByPostIdResponses = commentList.stream().map(comment -> this.modelMapperService
                .forResponse().map(comment , GetCommentsByPostIdResponse.class)).toList();

        return getCommentsByPostIdResponses;
    }

    @Override
    public void saveComment(AddCommentRequest addCommentRequest) {

        Comment comment = this.modelMapperService.forRequest().map(addCommentRequest,Comment.class);
        this.commentRepository.save(comment);
    }

    @Override
    public Long countComments() {
        Long commentCount = this.commentRepository.count();
        return commentCount;
    }
}
