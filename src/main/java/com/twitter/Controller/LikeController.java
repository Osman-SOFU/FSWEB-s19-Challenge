package com.twitter.Controller;

import com.twitter.Dto.LikeRequest;
import com.twitter.Entity.Like;
import com.twitter.Entity.Tweet;
import com.twitter.Entity.User;
import com.twitter.Exceptions.TwitterException;
import com.twitter.Service.LikeService;
import com.twitter.Service.TweetService;
import com.twitter.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class LikeController {

    private final LikeService likeService;
    private final TweetService tweetService;
    private final UserService userService;

    @Autowired
    public LikeController(LikeService likeService, TweetService tweetService, UserService userService) {
        this.likeService = likeService;
        this.tweetService = tweetService;
        this.userService = userService;
    }

    @PostMapping("/like")
    @ResponseStatus(HttpStatus.CREATED)
    public Like likeTweet(@RequestBody LikeRequest likeRequest) {
        User user = userService.findById(likeRequest.getUserId());
        Tweet tweet = tweetService.findById(likeRequest.getTweetId());

        if (likeService.existsByUserIdAndTweetId(user.getId(), tweet.getId())) {
            throw new TwitterException("Bu tweeti zaten beğendiniz.", HttpStatus.BAD_REQUEST);
        }

        Like like = new Like();
        like.setUser(user);
        like.setTweet(tweet);

        return likeService.save(like);
    }

    @PostMapping("/dislike")
    @ResponseStatus(HttpStatus.OK)
    public void dislikeTweet(@RequestBody LikeRequest likeRequest) {
        User user = userService.findById(likeRequest.getUserId());
        Tweet tweet = tweetService.findById(likeRequest.getTweetId());

        if (!likeService.existsByUserIdAndTweetId(user.getId(), tweet.getId())) {
            throw new TwitterException("Bu tweeti daha önce beğenmediniz.", HttpStatus.BAD_REQUEST);
        }

        likeService.deleteByUserAndTweet(user, tweet);
    }
}
