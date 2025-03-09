package com.emirsomuncu.HaveAnIdea.service.concretes;

import com.emirsomuncu.HaveAnIdea.core.utilites.mappers.ModelMapperService;
import com.emirsomuncu.HaveAnIdea.dao.PostDao;
import com.emirsomuncu.HaveAnIdea.dao.UserDao;
import com.emirsomuncu.HaveAnIdea.entities.Post;
import com.emirsomuncu.HaveAnIdea.service.abstracts.PostService;
import com.emirsomuncu.HaveAnIdea.service.requests.SavePostRequest;
import com.emirsomuncu.HaveAnIdea.service.responses.GetAllPostsResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.GetDesiredUserPostsByUserIdResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.GetPostByIdResponse;
import com.emirsomuncu.HaveAnIdea.service.rules.PostServiceImplRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    public PostDao postDao ;

    @Autowired
    public ModelMapperService modelMapperService ;

    @Autowired
    public PostServiceImplRules postServiceImplRules ;


    @Override
    public List<Post> getPostByUserId(Long id) {

        List<Post> postList = this.postDao.findPostByUserIdOrderByCreatedAtDesc(id);
        return postList ;

    }

    @Override
    public List<GetDesiredUserPostsByUserIdResponse> getDesiredUserPostsByUserId(Long id) {

        List<Post> postList = this.postDao.findPostByUserId(id);
        List<GetDesiredUserPostsByUserIdResponse> getDesiredUserPostsByUserIdResponse = postList.stream().map(post -> this.modelMapperService
                .forResponse().map(post , GetDesiredUserPostsByUserIdResponse.class)).toList();

        return getDesiredUserPostsByUserIdResponse;
    }

    @Override
    public GetPostByIdResponse getPostById(Long id) {

        Optional<Post> post = this.postDao.findById(id);
        GetPostByIdResponse getPostByIdResponse = this.modelMapperService.forResponse().map(post , GetPostByIdResponse.class);

        return getPostByIdResponse;
    }

    @Override
    public List<GetAllPostsResponse> getAllPosts() {
        List<Post> posts  = this.postDao.findAllByOrderByCreatedAtDesc();
        List<GetAllPostsResponse> getAllPostsResponses = posts.stream().map(post -> this.modelMapperService
                .forResponse().map(post , GetAllPostsResponse.class)).toList();
        return getAllPostsResponses ;
    }

    @Override
    public void deletePost(Long id) {
        this.postServiceImplRules.checkUserToDeletePost(id);
        this.postDao.deleteById(id);
    }

    @Override
    public void savePost(SavePostRequest savePostRequest) {

        Post post = this.modelMapperService.forRequest().map(savePostRequest, Post.class);
        this.postDao.save(post);
    }

    @Override
    public Long countPosts() {
        Long postCount = this.postDao.count();
        return postCount;
    }

}
