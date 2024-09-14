package com.emirsomuncu.HaveAnIdea.service.concretes;

import com.emirsomuncu.HaveAnIdea.core.utilites.mappers.ModelMapperService;
import com.emirsomuncu.HaveAnIdea.dao.LikeDao;
import com.emirsomuncu.HaveAnIdea.entities.Like;
import com.emirsomuncu.HaveAnIdea.service.abstracts.LikeService;
import com.emirsomuncu.HaveAnIdea.service.requests.SaveLikeRequest;
import com.emirsomuncu.HaveAnIdea.service.responses.GetLikesByPostIdResponse;
import com.emirsomuncu.HaveAnIdea.service.rules.LikeServiceImplRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeDao likeDao ;

    @Autowired
    private ModelMapperService modelMapperService ;

    @Autowired
    private LikeServiceImplRules likeServiceImplRules;


    @Override
    public List<GetLikesByPostIdResponse> getLikesByPostId(Long postId) {

        this.likeServiceImplRules.checkUserToShowLikes(postId);
        List<Like> likeList = this.likeDao.findLikeByPostId(postId);
        List<GetLikesByPostIdResponse> getLikesByPostIdResponses = likeList.stream().map(like -> this.modelMapperService
                .forResponse().map(like , GetLikesByPostIdResponse.class)).toList();
        return getLikesByPostIdResponses ;
    }

    @Override
    public void saveLike(Long postId, Long userId) {

        Optional<Like> existingLike = this.likeDao.findLikeByPostIdAndUserId(postId , userId);
        if (existingLike.isPresent()) {
            this.likeDao.deleteById(existingLike.get().getId());
        }else {
            SaveLikeRequest saveLikeRequest = new SaveLikeRequest();
            saveLikeRequest.setUserId(userId);
            saveLikeRequest.setPostId(postId);
            Like like = this.modelMapperService.forRequest().map(saveLikeRequest , Like.class);
            this.likeDao.save(like);
        }
    }

    @Override
    public Long countLikes(Long postId) {

        Long numberOfPostLikes = this.likeDao.countLikeByPostId(postId);
        return numberOfPostLikes;
    }
}
