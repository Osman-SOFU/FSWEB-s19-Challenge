/*
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.twitter.Entity.*;
import com.twitter.Exceptions.TwitterException;
import com.twitter.Repository.*;

import com.twitter.Service.CommentServiceImpl;
import com.twitter.Service.RetweetServiceImpl;
import com.twitter.Service.TweetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MainTest {

    @Nested
    @DisplayName("Comment Service Tests")
    class CommentServiceTests {
        @Mock private CommentRepository commentRepository;
        @InjectMocks private CommentServiceImpl commentService;

        private Comment comment;

        @BeforeEach
        void setUp() {
            comment = new Comment();
            comment.setId(1L);
            comment.setText("Test Comment");
        }

        @Test
        @DisplayName("Find Comment By Id - Exists")
        void testFindCommentById_WhenExists() {
            when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

            Comment foundComment = commentService.findById(1L);
            assertNotNull(foundComment);
            assertEquals(1L, foundComment.getId());
            assertEquals("Test Comment", foundComment.getText());

            System.out.println("Bulunan Yorum: " + foundComment.getText());
        }

        @Test
        @DisplayName("Find Comment By Id - Not Exists")
        void testFindCommentById_WhenNotExists() {
            when(commentRepository.findById(2L)).thenReturn(Optional.empty());

            TwitterException exception = assertThrows(TwitterException.class, () -> commentService.findById(2L));
            assertEquals("2'li yorum bulunamadı", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Like Service Tests")
    class LikeServiceTests {
        @Mock private LikeRepository likeRepository;
        @InjectMocks private com.twitter.Service.Impl.LikeServiceImpl likeService;

        private Like like;

        @BeforeEach
        void setUp() {
            like = new Like();
            like.setId(1L);
        }

        @Test
        @DisplayName("Find Like By Id - Exists")
        void testFindLikeById_WhenExists() {
            when(likeRepository.findById(1L)).thenReturn(Optional.of(like));

            Like foundLike = likeService.findById(1L);
            assertNotNull(foundLike);
            assertEquals(1L, foundLike.getId());

            System.out.println("Bulunan Beğeni: ID = " + foundLike.getId());
        }

        @Test
        @DisplayName("Find Like By Id - Not Exists")
        void testFindLikeById_WhenNotExists() {
            when(likeRepository.findById(2L)).thenReturn(Optional.empty());

            TwitterException exception = assertThrows(TwitterException.class, () -> likeService.findById(2L));
            assertEquals("2 numaralı beğeni bulunamadı", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Retweet Service Tests")
    class RetweetServiceTests {
        @Mock private RetweetRepository retweetRepository;
        @InjectMocks private RetweetServiceImpl retweetService;

        private Retweet retweet;

        @BeforeEach
        void setUp() {
            retweet = new Retweet();
            retweet.setId(1L);
        }

        @Test
        @DisplayName("Find Retweet By Id - Exists")
        void testFindRetweetById_WhenExists() {
            when(retweetRepository.findById(1L)).thenReturn(Optional.of(retweet));

            Retweet foundRetweet = retweetService.findById(1L);
            assertNotNull(foundRetweet);
            assertEquals(1L, foundRetweet.getId());

            System.out.println("Bulunan Retweet: ID = " + foundRetweet.getId());
        }

        @Test
        @DisplayName("Find Retweet By Id - Not Exists")
        void testFindRetweetById_WhenNotExists() {
            when(retweetRepository.findById(2L)).thenReturn(Optional.empty());

            TwitterException exception = assertThrows(TwitterException.class, () -> retweetService.findById(2L));
            assertEquals("2'li retweet bulunamadı", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Tweet Service Tests")
    class TweetServiceTests {
        @Mock private TweetRepository tweetRepository;
        @InjectMocks private TweetServiceImpl tweetService;

        private Tweet tweet;

        @BeforeEach
        void setUp() {
            tweet = new Tweet();
            tweet.setId(1L);
            tweet.setText("Hello Twitter!");
        }

        @Test
        @DisplayName("Find Tweet By Id - Exists")
        void testFindTweetById_WhenExists() {
            when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));

            Tweet foundTweet = tweetService.findById(1L);
            assertNotNull(foundTweet);
            assertEquals(1L, foundTweet.getId());
            assertEquals("Hello Twitter!", foundTweet.getText());

            System.out.println("Bulunan Tweet: " + foundTweet.getText());
        }

        @Test
        @DisplayName("Find Tweet By Id - Not Exists")
        void testFindTweetById_WhenNotExists() {
            when(tweetRepository.findById(2L)).thenReturn(Optional.empty());

            TwitterException exception = assertThrows(TwitterException.class, () -> tweetService.findById(2L));
            assertEquals("2'li tweet bulunamadı", exception.getMessage());
        }
    }
}
 */
