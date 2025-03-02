package com.twitter.Service;

import com.twitter.Entity.Comment;
import com.twitter.Entity.Like;

import java.util.List;

public interface LikeService {
    List<Like> getAll();
    Like findById(Long id);
    int countByTweetId(Long id);
    Like save(Like like);
    Like update(Long id, Like like);
    void delete(Long id);
}
