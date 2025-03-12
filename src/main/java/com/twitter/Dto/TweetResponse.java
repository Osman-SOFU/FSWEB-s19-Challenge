package com.twitter.Dto;

import com.twitter.Entity.Comment;
import com.twitter.Entity.Tweet;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TweetResponse {
    private Long id;
    private String text;
    private String username;
    private int likeCount;
    private int commentCount;
    private int retweetCount;
    private List<String> comments; // ✅ Sadece yorumların metni


    public TweetResponse(Tweet tweet) {
        this.id = tweet.getId();
        this.text = tweet.getText();
        this.username = tweet.getUser().getName(); // Kullanıcı ismi
        this.likeCount = (tweet.getLikes() != null) ? tweet.getLikes().size() : 0;
        this.commentCount = (tweet.getComments() != null) ? tweet.getComments().size() : 0;
        this.retweetCount = (tweet.getRetweets() != null) ? tweet.getRetweets().size() : 0;
        this.comments = tweet.getComments().stream().map(Comment::getText).collect(Collectors.toList()); // ✅ Sadece text al
    }
}
