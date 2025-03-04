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
    public CommentResponse create(@Validated @RequestBody CommentRequest commentRequest) {
        User user = userService.findById(commentRequest.getUserId());
        if (user == null) {
            throw new TwitterException("Kullanıcı bulunamadı.", HttpStatus.NOT_FOUND);
        }

        Comment comment = new Comment();
        comment.setText(commentRequest.getText());
        comment.setUser(user);

        commentService.save(comment);

        return new CommentResponse(comment);
    }

    @PutMapping("/{id}")
    public CommentResponse update(@PathVariable("id") Long id, @RequestBody CommentRequest commentRequest) {
        Comment comment = commentService.findById(id);
        if (!comment.getUser().getId().equals(commentRequest.getUserId())) {
            throw new TwitterException("Bu yorumu güncelleme yetkiniz yok.", HttpStatus.FORBIDDEN);
        }

        comment.setText(commentRequest.getText());
        commentService.update(comment);

        return new CommentResponse(comment);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
        Comment comment = commentService.findById(id);
        Tweet tweet = tweetService.findById(comment.getTweet().getId());

        if (!comment.getUser().getId().equals(userId) && !tweet.getUser().getId().equals(userId)) {
            throw new TwitterException("Bu yorumu silme yetkiniz yok.", HttpStatus.FORBIDDEN);
        }

        commentService.delete(id);
    }
}
