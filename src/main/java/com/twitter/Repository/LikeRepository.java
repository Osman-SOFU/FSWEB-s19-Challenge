package com.twitter.Repository;

import com.twitter.Entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    //Bir tweet'in toplam beğeni sayısını getirir.
    @Query("SELECT COUNT(l) FROM Like l WHERE l.tweet.id = :tweetId")
    int countByTweetId(@Param("tweetId") Long tweetId);

    //Bir kullanıcının belirli bir tweet'i beğenip beğenmediğini kontrol eder.
    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END FROM Like l WHERE l.user.id = :userId AND l.tweet.id = :tweetId")
    boolean existsByUserIdAndTweetId(@Param("userId") Long userId, @Param("tweetId") Long tweetId);

    // ✅ Kullanıcının belirli bir tweet'e yaptığı like'ı getirir.
    @Query("SELECT l FROM Like l WHERE l.user.id = :userId AND l.tweet.id = :tweetId")
    Like findByUserIdAndTweetId(@Param("userId") Long userId, @Param("tweetId") Long tweetId);
}
