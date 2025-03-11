package com.twitter.Dto;

import lombok.Data;

@Data
public class LikeResponse {
    private Long tweetId;
    private String username;
    private String message;
    private int likeCount; // Beğeni sayısını da ekledik

    public LikeResponse(Long tweetId, String username, String message, int likeCount) {
        this.tweetId = tweetId;
        this.username = username;
        this.message = message;
        this.likeCount = likeCount;
    }
}
