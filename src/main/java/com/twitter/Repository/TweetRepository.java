package com.twitter.Repository;

import com.twitter.Entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    //Bir kullanıcının tüm tweet'lerini getirir.
    @Query("SELECT t FROM Tweet t WHERE t.user.id = :userId")
    List<Tweet> findByUserId(@Param("userId") Long userId);

    //Tüm tweet'leri en yeni olandan sıralayarak getirir.
    @Query("SELECT t FROM Tweet t ORDER BY t.id DESC")
    List<Tweet> findAllByOrderByIdDesc();
}
