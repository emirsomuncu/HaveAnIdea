package com.emirsomuncu.HaveAnIdea.service.concretes;

import com.emirsomuncu.HaveAnIdea.core.utilites.mappers.ModelMapperService;
import com.emirsomuncu.HaveAnIdea.dao.CommentDao;
import com.emirsomuncu.HaveAnIdea.entities.Comment;
import com.emirsomuncu.HaveAnIdea.service.abstracts.CommentService;
import com.emirsomuncu.HaveAnIdea.service.requests.AddCommentRequest;
import com.emirsomuncu.HaveAnIdea.service.responses.GetCommentByIdResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.GetCommentsByPostIdResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.GetDesiredUserCommentsByUserIdResponse;
import com.emirsomuncu.HaveAnIdea.service.rules.CommentServiceImplRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao ;

    @Autowired
    private ModelMapperService modelMapperService;

    @Autowired
    private CommentServiceImplRules commentServiceImplRules ;


    @Override
    public List<Comment> getCommentsByUserId(Long id) {

        return this.commentDao.findCommentByUserId(id);

    }

    @Override
    public List<GetDesiredUserCommentsByUserIdResponse> getDesiredUserCommentsByUserId(Long id) {

        List<Comment> commentList = this.commentDao.findCommentByUserId(id);
        List<GetDesiredUserCommentsByUserIdResponse> getDesiredUserCommentsByUserIdResponses = commentList.stream().map(comment -> this.modelMapperService
                .forResponse().map(comment , GetDesiredUserCommentsByUserIdResponse.class)).toList();
        return getDesiredUserCommentsByUserIdResponses;
    }

    @Override
    public GetCommentByIdResponse getCommentById(Long id) {
        Optional<Comment> comment = this.commentDao.findById(id);
        GetCommentByIdResponse getCommentByIdResponse = this.modelMapperService.forResponse().map(comment , GetCommentByIdResponse.class);
        return getCommentByIdResponse;
    }


    @Override
    public void deleteComment(Long id) {
        this.commentServiceImplRules.checkUserToDeleteComments(id);
        this.commentDao.deleteById(id);
    }

    @Override
    public List<GetCommentsByPostIdResponse> getCommentsByPostId(Long id) {

        List<Comment> commentList = this.commentDao.findCommentByPostId(id);
        List<GetCommentsByPostIdResponse> getCommentsByPostIdResponses = commentList.stream().map(comment -> this.modelMapperService
                .forResponse().map(comment , GetCommentsByPostIdResponse.class)).toList();

        return getCommentsByPostIdResponses;
    }

    @Override
    public void saveComment(AddCommentRequest addCommentRequest) {

        Comment comment = this.modelMapperService.forRequest().map(addCommentRequest,Comment.class);
        this.commentDao.save(comment);
    }

    @Override
    public Long countComments() {
        Long commentCount = this.commentDao.count();
        return commentCount;
    }
}
