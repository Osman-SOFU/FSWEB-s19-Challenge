package com.twitter.Service;

import com.twitter.Dto.TweetResponse;
import com.twitter.Entity.Tweet;
import com.twitter.Exceptions.TwitterException;
import com.twitter.Repository.TweetRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new TwitterException(id + "'li tweet bulunamadı", HttpStatus.NOT_FOUND));

        // ✅ Eğer null ise boş liste ata
        if (tweet.getComments() == null) {
            tweet.setComments(new ArrayList<>());
        }

        return tweet;
    }

    @Override
    public List<TweetResponse> findByUserId(Long userId) {
        List<Tweet> tweets = tweetRepository.findByUserId(userId);

        if (tweets.isEmpty()) {
            throw new TwitterException(userId + "'li kullanıcı tweeti bulunamadı", HttpStatus.NOT_FOUND);
        }

        return tweets.stream()
                .map(TweetResponse::new) // ✅ Tweet yanında yorumlar da eklenecek!
                .collect(Collectors.toList());
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
