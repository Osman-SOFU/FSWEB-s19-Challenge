package com.twitter.Service;

import com.twitter.Entity.Comment;
import com.twitter.Exceptions.TwitterException;
import com.twitter.Repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment findById(Long id) {
        System.out.println("findById çağrıldı, id: " + id);  // Debug Log
        return commentRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("Hata: " + id + " numaralı yorum bulunamadı!");
                    return new TwitterException(id + "'li yorum bulunamadı", HttpStatus.NOT_FOUND);
                });
    }

    @Override
    public List<Comment> findByTweetId(Long id) {
        List<Comment> comments = commentRepository.findByTweetId(id);

        if (comments.isEmpty()) {
            throw new TwitterException(id + "'li tweet için yorum bulunamadı", HttpStatus.NOT_FOUND);
        }

        return comments;
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(Comment comment) {
        Comment existingComment = commentRepository
                .findById(comment.getId())
                .orElseThrow(() -> new TwitterException(comment.getId() + "'li yorum bulunamadı", HttpStatus.NOT_FOUND));

        System.out.println("Yorum güncelleniyor, mevcut text: " + existingComment.getText());

        // Eğer yeni bir text gönderildiyse güncelle
        if (comment.getText() != null) {
            existingComment.setText(comment.getText());
        }

        return commentRepository.save(existingComment);
    }

    @Override
    public void delete(Long id) {
        Comment comment = findById(id);
        commentRepository.deleteById(comment.getId());
    }
}
