package com.twitter.Controller;

import com.twitter.Dto.RetweetRequest;
import com.twitter.Dto.RetweetResponse;
import com.twitter.Entity.Retweet;
import com.twitter.Entity.Tweet;
import com.twitter.Entity.User;
import com.twitter.Exceptions.TwitterException;
import com.twitter.Service.RetweetService;
import com.twitter.Service.TweetService;
import com.twitter.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retweet")
public class RetweetController {

    private final RetweetService retweetService;
    private final TweetService tweetService;
    private final UserService userService;

    @Autowired
    public RetweetController(RetweetService retweetService, TweetService tweetService, UserService userService) {
        this.retweetService = retweetService;
        this.tweetService = tweetService;
        this.userService = userService;
    }

    // ✅ Retweet yapma işlemi
    @PostMapping
    public ResponseEntity<RetweetResponse> retweet(@RequestBody RetweetRequest retweetRequest,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new TwitterException("Kullanıcı kimliği doğrulanamadı!", HttpStatus.UNAUTHORIZED);
        }

        User user = userService.findByEmail(userDetails.getUsername());
        if (user == null) {
            throw new TwitterException("Kullanıcı bulunamadı!", HttpStatus.NOT_FOUND);
        }

        Tweet tweet = tweetService.findById(retweetRequest.getTweetId());
        if (tweet == null) {
            throw new TwitterException("Tweet bulunamadı!", HttpStatus.NOT_FOUND);
        }

        if (retweetService.existsByUserIdAndTweetId(user.getId(), tweet.getId())) {
            throw new TwitterException("Bu tweet zaten retweet edildi!", HttpStatus.BAD_REQUEST);
        }

        Retweet retweet = new Retweet();
        retweet.setUser(user);
        retweet.setTweet(tweet);
        retweetService.save(retweet);

        int retweetCount = retweetService.countByTweetId(tweet.getId());

        RetweetResponse response = new RetweetResponse(tweet.getId(), user.getEmail(), "Tweet retweet edildi!", retweetCount);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RetweetResponse> deleteRetweet(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new TwitterException("Kullanıcı kimliği doğrulanamadı!", HttpStatus.UNAUTHORIZED);
        }

        User user = userService.findByEmail(userDetails.getUsername());
        if (user == null) {
            throw new TwitterException("Kullanıcı bulunamadı!", HttpStatus.NOT_FOUND);
        }

        Retweet retweet = retweetService.findById(id);
        if (retweet == null) {
            throw new TwitterException("Retweet bulunamadı!", HttpStatus.NOT_FOUND);
        }

        if (!retweet.getUser().getId().equals(user.getId())) {
            throw new TwitterException("Bu retweeti silme yetkiniz yok!", HttpStatus.FORBIDDEN);
        }

        retweetService.delete(id);

        // Güncellenmiş retweet sayısını al
        int updatedRetweetCount = retweetService.countByTweetId(retweet.getTweet().getId());

        // Yanıtı oluştur ve döndür
        RetweetResponse response = new RetweetResponse(retweet.getTweet().getId(), user.getEmail(), "Retweet silindi!", updatedRetweetCount);
        return ResponseEntity.ok(response);
    }
}
