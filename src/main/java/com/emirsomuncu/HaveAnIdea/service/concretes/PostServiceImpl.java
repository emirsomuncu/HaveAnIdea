package com.emirsomuncu.HaveAnIdea.service.concretes;

import com.emirsomuncu.HaveAnIdea.core.utilites.mappers.ModelMapperService;
import com.emirsomuncu.HaveAnIdea.repository.PostRepository;
import com.emirsomuncu.HaveAnIdea.entities.Post;
import com.emirsomuncu.HaveAnIdea.service.abstracts.PostService;
import com.emirsomuncu.HaveAnIdea.service.requests.SavePostRequest;
import com.emirsomuncu.HaveAnIdea.service.responses.post.GetAllPostsAccordingToTopic;
import com.emirsomuncu.HaveAnIdea.service.responses.post.GetAllPostsResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.user.GetDesiredUserPostsByUserIdResponse;
import com.emirsomuncu.HaveAnIdea.service.responses.post.GetPostByIdResponse;
import com.emirsomuncu.HaveAnIdea.service.rules.PostServiceImplRules;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    public final PostRepository postRepository;
    public final ModelMapperService modelMapperService ;
    public final PostServiceImplRules postServiceImplRules ;

    @Override
    public List<Post> getPostByUserId(Long id) {

        List<Post> postList = this.postRepository.findPostByUserIdOrderByCreatedAtDesc(id);
        return postList ;

    }

    @Override
    public List<GetDesiredUserPostsByUserIdResponse> getDesiredUserPostsByUserId(Long id) {

        List<Post> postList = this.postRepository.findPostByUserId(id);
        List<GetDesiredUserPostsByUserIdResponse> getDesiredUserPostsByUserIdResponse = postList.stream().map(post -> this.modelMapperService
                .forResponse().map(post , GetDesiredUserPostsByUserIdResponse.class)).toList();

        return getDesiredUserPostsByUserIdResponse;
    }

    @Override
    public GetPostByIdResponse getPostById(Long id) {

        Optional<Post> post = this.postRepository.findById(id);
        GetPostByIdResponse getPostByIdResponse = this.modelMapperService.forResponse().map(post , GetPostByIdResponse.class);

        return getPostByIdResponse;
    }

    @Override
    public List<GetAllPostsResponse> getAllPosts() {
        List<Post> posts  = this.postRepository.findAllByOrderByCreatedAtDesc();
        List<GetAllPostsResponse> getAllPostsResponses = posts.stream().map(post -> this.modelMapperService
                .forResponse().map(post , GetAllPostsResponse.class)).toList();
        return getAllPostsResponses ;
    }

    @Override
    public List<GetAllPostsAccordingToTopic> getAllPostsAccordingToTopic(String topic) {

        List<Post> postList = this.postRepository.findPostByTitleOrderByCreatedAtDesc(topic);
        List<GetAllPostsAccordingToTopic> getAllPostsAccordingToTopics = postList.stream().map(post -> this.modelMapperService.forResponse().map(post , GetAllPostsAccordingToTopic.class)).collect(Collectors.toList());
        return getAllPostsAccordingToTopics;
    }

    @Override
    public void deletePost(Long id) {
        this.postServiceImplRules.checkUserToDeletePost(id);
        this.postRepository.deleteById(id);
    }

    @Override
    public void savePost(SavePostRequest savePostRequest) {

        Post post = this.modelMapperService.forRequest().map(savePostRequest, Post.class);
        this.postRepository.save(post);
    }

    @Override
    public Long countPosts() {
        Long postCount = this.postRepository.count();
        return postCount;
    }

    @Override
    public List<String> topicList() {

        List<String> topicList = new ArrayList<>();
        List<Post> postList = this.postRepository.findAll();
        for(Post postListRunner : postList ) {
            String postTopic = postListRunner.getTitle();
            topicList.add(postTopic);
        }

        return topicList;
    }

    @Override
    public List<String> searchTopic(String topic) {
        List<String> topicList = topicList();
        List<String> result = new ArrayList<>();

        for (String t : topicList) {
            if (t.toLowerCase().contains(topic.toLowerCase()) && !result.contains(t)) {
                result.add(t);
            }
        }
        return result;
    }

    @Override
    public Map<String, Long> popularTopics() {
        List<String> topicList = topicList();

        return topicList.stream()
                .collect(Collectors.groupingBy(topic -> topic, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }


}
