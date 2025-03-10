package com.emirsomuncu.HaveAnIdea.service.concretes;

import com.emirsomuncu.HaveAnIdea.core.utilites.mappers.ModelMapperService;
import com.emirsomuncu.HaveAnIdea.repository.LikeRepository;
import com.emirsomuncu.HaveAnIdea.entities.Like;
import com.emirsomuncu.HaveAnIdea.service.abstracts.LikeService;
import com.emirsomuncu.HaveAnIdea.service.requests.SaveLikeRequest;
import com.emirsomuncu.HaveAnIdea.service.responses.like.GetLikesByPostIdResponse;
import com.emirsomuncu.HaveAnIdea.service.rules.LikeServiceImplRules;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final ModelMapperService modelMapperService ;
    private final LikeServiceImplRules likeServiceImplRules;

    @Override
    public List<GetLikesByPostIdResponse> getLikesByPostId(Long postId) {

        this.likeServiceImplRules.checkUserToShowLikes(postId);
        List<Like> likeList = this.likeRepository.findLikeByPostId(postId);
        List<GetLikesByPostIdResponse> getLikesByPostIdResponses = likeList.stream().map(like -> this.modelMapperService
                .forResponse().map(like , GetLikesByPostIdResponse.class)).toList();
        return getLikesByPostIdResponses ;
    }

    @Override
    public void saveLike(Long postId, Long userId) {

        Optional<Like> existingLike = this.likeRepository.findLikeByPostIdAndUserId(postId , userId);
        if (existingLike.isPresent()) {
            this.likeRepository.deleteById(existingLike.get().getId());
        }else {
            SaveLikeRequest saveLikeRequest = new SaveLikeRequest();
            saveLikeRequest.setUserId(userId);
            saveLikeRequest.setPostId(postId);
            Like like = this.modelMapperService.forRequest().map(saveLikeRequest , Like.class);
            this.likeRepository.save(like);
        }
    }

    @Override
    public Long countLikes(Long postId) {

        Long numberOfPostLikes = this.likeRepository.countLikeByPostId(postId);
        return numberOfPostLikes;
    }
}
