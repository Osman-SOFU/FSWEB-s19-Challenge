package com.twitter.Service;

import com.twitter.Entity.Like;
import com.twitter.Entity.Tweet;
import com.twitter.Entity.User;
import org.springframework.data.repository.query.Param;

public interface LikeService {
    Like save(User user, Tweet tweet); // Save işlemi
    boolean hasUserLikedTweet(User user, Tweet tweet);
    void removeLike(User user, Tweet tweet);
    int countByTweetId(Long tweetId);
}
