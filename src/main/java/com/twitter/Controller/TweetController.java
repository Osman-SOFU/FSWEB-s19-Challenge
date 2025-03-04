package com.twitter.Controller;

import com.twitter.Dto.TweetRequest;
import com.twitter.Dto.TweetResponse;
import com.twitter.Entity.Tweet;
import com.twitter.Entity.User;
import com.twitter.Exceptions.TwitterException;
import com.twitter.Service.TweetService;
import com.twitter.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tweet")
public class TweetController {

    private final TweetService tweetService;
    private final UserService userService;

    @Autowired
    public TweetController(@Qualifier("tweetServiceImpl") TweetService tweetService, UserService userService) {
        this.tweetService = tweetService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TweetResponse create(@Validated @RequestBody TweetRequest tweetRequest) {
        User user = userService.findById(tweetRequest.getUserId());
        if (user == null) {
            throw new TwitterException("Kullanıcı bulunamadı.", HttpStatus.NOT_FOUND);
        }

        Tweet tweet = new Tweet();
        tweet.setText(tweetRequest.getText());
        tweet.setUser(user);

        tweetService.save(tweet);

        return new TweetResponse(tweet); // ✅ Doğru dönüşüm
    }

    @GetMapping("/findByUserId/{id}")
    public List<TweetResponse> getByUserId(@PathVariable("id") Long id) {
        List<Tweet> tweets = tweetService.findByUserId(id);
        return tweets.stream().map(TweetResponse::new).toList();
    }

    @GetMapping("/findById/{id}")
    public TweetResponse getById(@PathVariable("id") Long id) {
        Tweet tweet = tweetService.findById(id);
        return new TweetResponse(tweet); // ✅ Doğru dönüşüm
    }

    @PutMapping("/{id}")
    public TweetResponse update(@PathVariable("id") Long id, @RequestBody TweetRequest tweetRequest) {
        Tweet tweet = tweetService.findById(id);
        if (!tweet.getUser().getId().equals(tweetRequest.getUserId())) {
            throw new TwitterException("Bu tweeti güncelleme yetkiniz yok.", HttpStatus.FORBIDDEN);
        }

        tweet.setText(tweetRequest.getText());
        tweetService.update(tweet);

        return new TweetResponse(tweet); // ✅ Doğru dönüşüm
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
        Tweet tweet = tweetService.findById(id);

        if (!tweet.getUser().getId().equals(userId)) {
            throw new TwitterException("Bu tweeti silme yetkiniz yok.", HttpStatus.FORBIDDEN);
        }

        tweetService.delete(id);
    }
}
