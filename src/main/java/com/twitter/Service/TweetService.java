package com.twitter.Service;

import com.twitter.Dto.TweetResponse;
import com.twitter.Entity.Tweet;

import java.util.List;

public interface TweetService {
    List<Tweet> getAll();
    Tweet findById(Long id);
    List<TweetResponse> findByUserId(Long id);
    Tweet save(Tweet tweet);
    Tweet update(Tweet tweet);
    void delete(Long id);
    List<Tweet> getAllSortedByNewest();
}
