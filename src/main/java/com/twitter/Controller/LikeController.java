package com.twitter.Controller;

import com.twitter.Dto.LikeRequest;
import com.twitter.Dto.LikeResponse;
import com.twitter.Entity.Like;
import com.twitter.Entity.Tweet;
import com.twitter.Entity.User;
import com.twitter.Exceptions.TwitterException;
import com.twitter.Service.LikeService;
import com.twitter.Service.TweetService;
import com.twitter.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<LikeResponse> likeTweet(@RequestBody LikeRequest request,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new TwitterException("Kimlik doğrulaması başarısız!", HttpStatus.UNAUTHORIZED);
        }

        User user = userService.findByEmail(userDetails.getUsername());
        if (user == null) {
            throw new TwitterException("Kullanıcı bulunamadı!", HttpStatus.NOT_FOUND);
        }

        Tweet tweet = tweetService.findById(request.getTweetId());
        if (tweet == null) {
            throw new TwitterException("Tweet bulunamadı!", HttpStatus.NOT_FOUND);
        }

        if (likeService.hasUserLikedTweet(user, tweet)) {
            throw new TwitterException("Bu tweeti zaten beğendiniz!", HttpStatus.BAD_REQUEST);
        }

        // Beğeni ekleme işlemi
        Like like = likeService.save(user, tweet);

        // Güncellenmiş beğeni sayısını al
        int likeCount = likeService.countByTweetId(tweet.getId());

        // Yanıtı oluştur ve döndür
        LikeResponse response = new LikeResponse(tweet.getId(), user.getEmail(), "Tweet beğenildi!", likeCount);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/dislike")
    @ResponseStatus(HttpStatus.OK)
    public void dislikeTweet(@RequestBody LikeRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new TwitterException("Kimlik doğrulaması başarısız!", HttpStatus.UNAUTHORIZED);
        }

        User user = userService.findByEmail(userDetails.getUsername());
        if (user == null) {
            throw new TwitterException("Kullanıcı bulunamadı!", HttpStatus.NOT_FOUND);
        }

        Tweet tweet = tweetService.findById(request.getTweetId());
        if (tweet == null) {
            throw new TwitterException("Tweet bulunamadı!", HttpStatus.NOT_FOUND);
        }

        if (!likeService.hasUserLikedTweet(user, tweet)) {
            throw new TwitterException("Bu tweeti daha önce beğenmediniz.", HttpStatus.BAD_REQUEST);
        }

        likeService.removeLike(user, tweet);
    }
}
