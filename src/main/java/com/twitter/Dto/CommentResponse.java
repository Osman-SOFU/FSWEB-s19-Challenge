package com.twitter.Dto;

import com.twitter.Entity.Comment;
import com.twitter.Entity.Tweet;

public record CommentResponse(String text) {

    public CommentResponse(Comment comment) {
        this(comment.getText());
    }
}
