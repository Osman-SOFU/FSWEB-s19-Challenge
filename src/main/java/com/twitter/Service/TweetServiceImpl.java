package com.twitter.Service;

import com.twitter.Entity.Tweet;
import com.twitter.Exceptions.TwitterException;
import com.twitter.Repository.TweetRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TweetServiceImpl implements TweetService{

    private final TweetRepository tweetRepository;

    @Override
    public List<Tweet> getAll() {
        return tweetRepository.findAll();
    }

    @Override
    public Tweet findById(Long id) {
        Optional<Tweet> optionalTweet = tweetRepository.findById(id);

        if (optionalTweet.isPresent()){
            return optionalTweet.get();
        }

        throw new TwitterException(id +"'li tweet bulunamadı", HttpStatus.NOT_FOUND);
    }

    @Override
    public List<Tweet> findByUserId(Long userId) {
        List<Tweet> optionalTweet = tweetRepository.findByUserId(userId);

        if (optionalTweet.isEmpty()){
            throw new TwitterException(userId +"'li kullanıcı tweeti bulunamadı", HttpStatus.NOT_FOUND);
        }
        return optionalTweet;
    }

    @Override
    public Tweet save(Tweet tweet) {
        return tweetRepository.save(tweet);
    }

    @Override
    public Tweet update(Tweet tweet) {
        Tweet optionalTweet = tweetRepository
                .findById(tweet.getId())
                .orElseThrow(() -> new TwitterException(tweet.getId() +"'li tweet bulunamadı", HttpStatus.NOT_FOUND));

        if (tweet.getText() != null) {
            optionalTweet.setText(tweet.getText());
        }

        return tweetRepository.save(optionalTweet);
    }

    @Override
    public void delete(Long id) {
        Tweet tweet = findById(id);
        tweetRepository.deleteById(tweet.getId());
    }

    @Override
    public List<Tweet> getAllSortedByNewest() {
        return tweetRepository.findAllByOrderByIdDesc();
    }
}
