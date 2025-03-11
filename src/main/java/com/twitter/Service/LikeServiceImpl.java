package com.twitter.Service.Impl;

import com.twitter.Entity.Like;
import com.twitter.Entity.Tweet;
import com.twitter.Entity.User;
import com.twitter.Exceptions.TwitterException;
import com.twitter.Repository.LikeRepository;
import com.twitter.Service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public Like save(User user, Tweet tweet) {
        if (hasUserLikedTweet(user, tweet)) {
            throw new TwitterException("Bu tweeti zaten beğendiniz!", HttpStatus.BAD_REQUEST);
        }
        Like like = new Like();
        like.setUser(user);
        like.setTweet(tweet);
        return likeRepository.save(like);
    }

    @Override
    public boolean hasUserLikedTweet(User user, Tweet tweet) {
        return likeRepository.existsByUserAndTweet(user, tweet);
    }

    @Override
    @Transactional
    public void removeLike(User user, Tweet tweet) {
        likeRepository.deleteByUserAndTweet(user, tweet);
    }

    @Override
    public int countByTweetId(Long tweetId) {
        return likeRepository.countByTweetId(tweetId);
    }
}
