package com.twitter.Controller;

import com.twitter.Dto.RetweetRequest;
import com.twitter.Entity.Retweet;
import com.twitter.Entity.Tweet;
import com.twitter.Entity.User;
import com.twitter.Exceptions.TwitterException;
import com.twitter.Service.RetweetService;
import com.twitter.Service.TweetService;
import com.twitter.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Retweet retweet(@RequestBody RetweetRequest retweetRequest) {
        User user = userService.findById(retweetRequest.getUserId());
        Tweet tweet = tweetService.findById(retweetRequest.getTweetId());

        if (retweetService.existsByUserIdAndTweetId(user.getId(), tweet.getId())) {
            throw new TwitterException("Bu tweet zaten retweet edildi!", HttpStatus.BAD_REQUEST);
        }

        Retweet retweet = new Retweet();
        retweet.setUser(user);
        retweet.setTweet(tweet);

        return retweetService.save(retweet);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRetweet(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
        Retweet retweet = retweetService.findById(id);

        if (!retweet.getUser().getId().equals(userId)) {
            throw new TwitterException("Bu retweeti silme yetkiniz yok!", HttpStatus.FORBIDDEN);
        }

        retweetService.delete(id);
    }
}
