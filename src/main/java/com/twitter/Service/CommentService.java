package com.twitter.Service;

import com.twitter.Entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getAll();
    Comment findById(Long id);
    List<Comment> findByTweetId(Long id);
    Comment save(Comment comment);
    Comment update(Long id, Comment comment);
    void delete(Long id);
}
