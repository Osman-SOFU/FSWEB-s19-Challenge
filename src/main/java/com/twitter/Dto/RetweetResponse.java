package com.twitter.Dto;

import lombok.Data;

@Data
public class RetweetResponse {
    private Long tweetId;
    private String username;
    private String message;
    private int retweetCount;

    public RetweetResponse(Long tweetId, String username, String message, int retweetCount) {
        this.tweetId = tweetId;
        this.username = username;
        this.message = message;
        this.retweetCount = retweetCount;
    }
}
