package com.emirsomuncu.HaveAnIdea.service.abstracts;

import com.emirsomuncu.HaveAnIdea.entities.Post;
import com.emirsomuncu.HaveAnIdea.service.requests.SavePostRequest;
import com.emirsomuncu.HaveAnIdea.service.responses.GetAllPostsResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.GetDesiredUserPostsByUserIdResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.GetPostByIdResponse;

import java.util.List;

public interface PostService {

    public List<Post> getPostByUserId(Long id);
    public List<GetDesiredUserPostsByUserIdResponse> getDesiredUserPostsByUserId(Long id);
    public GetPostByIdResponse getPostById(Long id);
    public List<GetAllPostsResponse> getAllPosts();
    public void deletePost(Long id);
    public void savePost(SavePostRequest savePostRequest);
    public Long countPosts();

}
