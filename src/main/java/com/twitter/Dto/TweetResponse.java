package com.twitter.Dto;


import com.twitter.Entity.Tweet;

public record TweetResponse(String text) {

    public TweetResponse(Tweet tweet) {
        this(tweet.getText());
    }
}
