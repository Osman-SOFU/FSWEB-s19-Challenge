package com.twitter.Service;

import com.twitter.Entity.Retweet;

import java.util.List;

public interface RetweetService {
    List<Retweet> getAll();
    Retweet findById(Long id);
    int countByTweetId(Long id);
    Retweet save(Retweet retweet);
    void delete(Long id);
}
