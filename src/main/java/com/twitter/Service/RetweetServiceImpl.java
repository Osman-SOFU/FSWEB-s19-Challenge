package com.twitter.Service;

import com.twitter.Entity.Retweet;
import com.twitter.Entity.Tweet;
import com.twitter.Entity.User;
import com.twitter.Exceptions.TwitterException;
import com.twitter.Repository.RetweetRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class RetweetServiceImpl implements RetweetService{

    private final RetweetRepository retweetRepository;

    @Override
    public List<Retweet> getAll() {
        return retweetRepository.findAll();
    }

    @Override
    public Retweet findById(Long id) {
        return retweetRepository.findById(id)
                .orElseThrow(() -> new TwitterException(id + "'li retweet bulunamadı", HttpStatus.NOT_FOUND));
    }

    @Override
    public int countByTweetId(Long id) {
        return retweetRepository.countByTweetId(id);
    }

    @Override
    public Retweet save(Retweet retweet) {
        boolean alreadyRetweeted = retweetRepository.existsByUserIdAndTweetId(retweet.getUser().getId(), retweet.getTweet().getId());
        if (alreadyRetweeted) {
            throw new TwitterException("Bu tweet zaten retweet edildi!", HttpStatus.BAD_REQUEST);
        }
        return retweetRepository.save(retweet);
    }

    @Override
    public void delete(Long id) {
        retweetRepository.deleteById(id);
    }

    @Override
    public void deleteByUserAndTweet(User user, Tweet tweet) {
        Retweet retweet = retweetRepository.findByUserIdAndTweetId(user.getId(), tweet.getId());
        if (retweet == null) {
            throw new TwitterException("Retweet bulunamadı!", HttpStatus.NOT_FOUND);
        }
        retweetRepository.delete(retweet);
    }

    @Override
    public boolean existsByUserIdAndTweetId(Long userId, Long tweetId) {
        return retweetRepository.existsByUserIdAndTweetId(userId, tweetId);
    }
}
