package com.twitter.Repository;

import com.twitter.Entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RetweetRepository extends JpaRepository<Retweet, Long> {

    //Bir tweet'in kaç kez retweet edildiğini getirir.
    @Query("SELECT COUNT(r) FROM Retweet r WHERE r.tweet.id = :tweetId")
    int countByTweetId(@Param("tweetId") Long tweetId);

    //Bir kullanıcının belirli bir tweet'i retweet edip etmediğini kontrol eder.
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END FROM Retweet r WHERE r.user.id = :userId AND r.tweet.id = :tweetId")
    boolean existsByUserIdAndTweetId(@Param("userId") Long userId, @Param("tweetId") Long tweetId);

    // ✅ Kullanıcının belirli bir tweet'e yaptığı retweet'i getirir.
    @Query("SELECT r FROM Retweet r WHERE r.user.id = :userId AND r.tweet.id = :tweetId")
    Retweet findByUserIdAndTweetId(@Param("userId") Long userId, @Param("tweetId") Long tweetId);
}
