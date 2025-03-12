package com.twitter.Controller;

import com.twitter.Dto.CommentRequest;
import com.twitter.Dto.CommentResponse;
import com.twitter.Entity.Comment;
import com.twitter.Entity.Tweet;
import com.twitter.Entity.User;
import com.twitter.Exceptions.TwitterException;
import com.twitter.Service.CommentService;
import com.twitter.Service.TweetService;
import com.twitter.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final TweetService tweetService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService, TweetService tweetService) {
        this.commentService = commentService;
        this.userService = userService;
        this.tweetService = tweetService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse create(@Validated @RequestBody CommentRequest commentRequest,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        // Kullanıcıyı email ile veritabanından al
        User user = userService.findByEmail(userDetails.getUsername());

        if (user == null) {
            throw new TwitterException("Kullanıcı bulunamadı.", HttpStatus.NOT_FOUND);
        }

        // Tweet'i al
        Tweet tweet = tweetService.findById(commentRequest.getTweetId());

        // Yorum oluştur
        Comment comment = new Comment();
        comment.setText(commentRequest.getText());
        comment.setUser(user);
        comment.setTweet(tweet); // ✅ Tweet'i set et!

        commentService.save(comment);

        return new CommentResponse(comment);
    }

    @PutMapping("/{id}")
    public CommentResponse update(@PathVariable("id") Long id,
                                  @RequestBody CommentRequest commentRequest,
                                  @AuthenticationPrincipal UserDetails userDetails) {

        // Giriş yapan kullanıcıyı al
        User user = userService.findByEmail(userDetails.getUsername());

        Tweet tweet = tweetService.findById(id);

        Comment comment = commentService.findById(id);

        // Kullanıcı tweetin sahibi mi?
        if (!tweet.getUser().getId().equals(user.getId()) && !comment.getUser().getId().equals(user.getId())) {
            throw new TwitterException("Bu yorumu güncelleme yetkiniz yok.", HttpStatus.FORBIDDEN);
        }

        comment.setText(commentRequest.getText());
        comment.setTweet(tweet); // ✅ Tweet'i set et!
        commentService.update(comment);

        return new CommentResponse(comment);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") Long id,
                           @AuthenticationPrincipal UserDetails userDetails) {
        // Kullanıcı bilgilerini al
        User user = userService.findByEmail(userDetails.getUsername());

        Comment comment = commentService.findById(id);

        Tweet tweet = tweetService.findById(comment.getTweet().getId());

        // Kullanıcı tweetin sahibi mi?
        if (!tweet.getUser().getId().equals(user.getId()) && !comment.getUser().getId().equals(user.getId())) {
            throw new TwitterException("Bu yorumu silme yetkiniz yok.", HttpStatus.FORBIDDEN);
        }

        commentService.delete(id);
    }
}
