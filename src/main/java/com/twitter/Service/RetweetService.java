package com.twitter.Service;

import com.twitter.Entity.Retweet;
import com.twitter.Entity.Tweet;
import com.twitter.Entity.User;

import java.util.List;

public interface RetweetService {
    List<Retweet> getAll();
    Retweet findById(Long id);
    int countByTweetId(Long id);
    Retweet save(Retweet retweet);
    void delete(Long id);
    void deleteByUserAndTweet(User user, Tweet tweet);
    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);


}
