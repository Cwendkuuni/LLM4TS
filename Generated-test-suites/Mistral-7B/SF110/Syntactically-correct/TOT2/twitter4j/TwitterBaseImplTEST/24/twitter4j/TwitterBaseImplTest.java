import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import twitter4j.*;
import java.util.*;

public class TwitterBaseImplTest {
    private TwitterBaseImpl twitterBaseImpl;
    private RateLimitStatusListener listener;

    @Before
    public void setup() {
        Configuration config = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .setOAuthAccessToken("accessToken")
                .setOAuthAccessTokenSecret("accessTokenSecret")
                .build();
        Authorization auth = new OAuthAuthorization(config);
        twitterBaseImpl = new TwitterBaseImpl(config, auth);
        listener = mock(RateLimitStatusListener.class);
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        assertEquals("testUserName", twitterBaseImpl.getScreenName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithoutAuth() throws TwitterException, IllegalStateException {
        twitterBaseImpl.auth = null;
        twitterBaseImpl.init();
        twitterBaseImpl.setFactory();
        twitterBaseImpl.getScreenName();
    }

    @Test(expected = TwitterException.class)
    public void testGetScreenNameWithTwitterException() throws TwitterException, IllegalStateException {
        Configuration config = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .build();
        Authorization auth = null;
        twitterBaseImpl = new TwitterBaseImpl(config, auth);
        twitterBaseImpl.http = null;
        twitterBaseImpl.init();
        twitterBaseImpl.setFactory();
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        assertEquals(12345678L, twitterBaseImpl.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithoutAuth() throws TwitterException, IllegalStateException {
        twitterBaseImpl.auth = null;
        twitterBaseImpl.init();
        twitterBaseImpl.setFactory();
        twitterBaseImpl.getId();
    }

    @Test(expected = TwitterException.class)
    public void testGetIdWithTwitterException() throws TwitterException, IllegalStateException {
        twitterBaseImpl.http = null;
        twitterBaseImpl.init();
        twitterBaseImpl.setFactory();
        twitterBaseImpl.getId();
    }

    @Test
    public void testAddRateLimitStatusListener() {
        twitterBaseImpl.addRateLimitStatusListener(listener);
        verify(listener).onRateLimitStatus(any(RateLimitStatusEvent.class));
    }

    @Test
    public void testAddNullRateLimitStatusListener() {
        RateLimitStatusListener listener = null;
        twitterBaseImpl.addRateLimitStatusListener(listener);
        // No exception expected
    }
}