package com.twitter.Repository;

import com.twitter.Entity.Like;
import com.twitter.Entity.Tweet;
import com.twitter.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    // Bir tweet'in toplam beğeni sayısını getirir
    @Query("SELECT COUNT(l) FROM Like l WHERE l.tweet.id = :tweetId")
    int countByTweetId(@Param("tweetId") Long tweetId);

    // Kullanıcının belirli bir tweet'i beğenip beğenmediğini kontrol eder
    boolean existsByUserAndTweet(User user, Tweet tweet);

    // Kullanıcının belirli bir tweet'e yaptığı like'ı getirir
    Optional<Like> findByUserAndTweet(User user, Tweet tweet);

    // Kullanıcının beğenisini kaldırmak için
    void deleteByUserAndTweet(User user, Tweet tweet);
}

