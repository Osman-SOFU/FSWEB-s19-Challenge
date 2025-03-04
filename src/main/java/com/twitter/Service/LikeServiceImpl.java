package com.twitter.Service;

import com.twitter.Entity.Like;
import com.twitter.Entity.Tweet;
import com.twitter.Entity.User;
import com.twitter.Exceptions.TwitterException;
import com.twitter.Repository.LikeRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class LikeServiceImpl implements LikeService{

    private final LikeRepository likeRepository;

    @Override
    public List<Like> getAll() {
        return likeRepository.findAll();
    }

    @Override
    public Like findById(Long id) {
        Optional<Like> optionalLike = likeRepository.findById(id);

        if (optionalLike.isPresent()){
            return optionalLike.get();
        }

        throw new TwitterException(id +"'li beğenme bulunamadı", HttpStatus.NOT_FOUND);
    }

    @Override
    public int countByTweetId(Long id) {
       return likeRepository.countByTweetId(id);
    }

    @Override
    public Like save(Like like) {
        boolean alreadyLiked = likeRepository.existsByUserIdAndTweetId(like.getUser().getId(), like.getTweet().getId());
        if (alreadyLiked) {
            throw new TwitterException("Bu tweet zaten beğenildi!", HttpStatus.BAD_REQUEST);
        }
        return likeRepository.save(like);
    }

    @Override
    public Like update(Long id, Like like) {
        Like existingLike = likeRepository
                .findById(id)
                .orElseThrow(() -> new TwitterException(id + "'li beğeni bulunamadı", HttpStatus.NOT_FOUND));

        existingLike.setTweet(like.getTweet());
        existingLike.setUser(like.getUser());

        return likeRepository.save(existingLike);
    }

    @Override
    public void delete(Long id) {
        likeRepository.deleteById(id);
    }

    @Override
    public boolean existsByUserIdAndTweetId(Long userId, Long tweetId) {
        return likeRepository.existsByUserIdAndTweetId(userId,tweetId);
    }

    @Override
    public void deleteByUserAndTweet(User user, Tweet tweet) {
        Like like = likeRepository.findByUserIdAndTweetId(user.getId(), tweet.getId());
        if (like == null) {
            throw new TwitterException("Beğeni bulunamadı!", HttpStatus.NOT_FOUND);
        }
        likeRepository.delete(like);
    }
}
