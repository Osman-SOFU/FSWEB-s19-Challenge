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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tweet")
@CrossOrigin(origins = "http://localhost:3200") // Sadece React için izin ver
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
    public TweetResponse create(@Validated @RequestBody TweetRequest tweetRequest,
                                @AuthenticationPrincipal UserDetails userDetails) {
        // Kullanıcıyı email ile veritabanından al
        User user = userService.findByEmail(userDetails.getUsername());

        if (user == null) {
            throw new TwitterException("Kullanıcı bulunamadı.", HttpStatus.NOT_FOUND);
        }

        // Tweet nesnesini oluştur ve kullanıcısını belirle
        Tweet tweet = new Tweet();
        tweet.setText(tweetRequest.getText());
        tweet.setUser(user);

        tweetService.save(tweet);

        return new TweetResponse(tweet);
    }


    @GetMapping("/findByUserId")
    public List<TweetResponse> findByUserId(@RequestParam("userId") Long userId) {
        List<Tweet> tweets = tweetService.findByUserId(userId);
        return tweets.stream().map(TweetResponse::new).collect(Collectors.toList());
    }

    @GetMapping("/findById/{id}")
    public TweetResponse getById(@PathVariable("id") Long id) {
        Tweet tweet = tweetService.findById(id);
        return new TweetResponse(tweet); // ✅ Doğru dönüşüm
    }

    @PutMapping("/{id}")
    public TweetResponse update(@PathVariable("id") Long id,
                                @RequestBody TweetRequest tweetRequest,
                                @AuthenticationPrincipal UserDetails userDetails) {
        // Giriş yapan kullanıcıyı al
        User user = userService.findByEmail(userDetails.getUsername());

        Tweet tweet = tweetService.findById(id);

        // Kullanıcı tweetin sahibi mi?
        if (!tweet.getUser().getId().equals(user.getId())) {
            throw new TwitterException("Bu tweeti güncelleme yetkiniz yok.", HttpStatus.FORBIDDEN);
        }

        // Güncelleme işlemi
        tweet.setText(tweetRequest.getText());
        tweetService.update(tweet);

        return new TweetResponse(tweet);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") Long id,
                           @AuthenticationPrincipal UserDetails userDetails) {
        // Kullanıcı bilgilerini al
        User user = userService.findByEmail(userDetails.getUsername());

        // Tweeti getir
        Tweet tweet = tweetService.findById(id);

        // Kullanıcı tweetin sahibi mi?
        if (!tweet.getUser().getId().equals(user.getId())) {
            throw new TwitterException("Bu tweeti silme yetkiniz yok.", HttpStatus.FORBIDDEN);
        }

        // Silme işlemi
        tweetService.delete(id);
    }
}
