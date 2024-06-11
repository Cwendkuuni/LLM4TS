package twitter4j;

import static org.junit.Assert.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import org.junit.*;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("user1", "pass1");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        String screenName = twitterBaseImpl.getScreenName();
        assertEquals("user1", screenName);

        twitterBaseImpl.setScreenName("testUser");
        screenName = twitterBaseImpl.getScreenName();
        assertEquals("testUser", screenName);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithoutAuthorization() {
        Configuration conf = new Configuration();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("user1", "pass1");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        long id = twitterBaseImpl.getId();
        fail("getId should throw TwitterException or IllegalStateException when not initialized");
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("user1", "pass1");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        User user = twitterBaseImpl.fillInIDAndScreenName();
        assertEquals("user1", user.getScreenName());
        assertEquals(1L, user.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("user1", "pass1");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        assertTrue(twitterBaseImpl.rateLimitStatusListeners.contains(listener));
    }

    @Test
    public void testOnRateLimitStatus() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("user1", "pass1");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                action.accept(event);
            }

            public void onRateLimitReached(RateLimitStatusEvent event) {
            }
        };
        twitterBaseImpl.onRateLimitStatus(action);
        twitterBaseImpl.addRateLimitStatusListener(listener);

        RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, null, true);
        action.accept(event);
        assertTrue(listener.onRateLimitStatus(event).isAuthenticated());
    }

    @Test
    public void testOnRateLimitReached() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("user1", "pass1");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            public void onRateLimitStatus(RateLimitStatusEvent event) {
            }

            public void onRateLimitReached(RateLimitStatusEvent event) {
                action.accept(event);
            }
        };
        twitterBaseImpl.onRateLimitReached(action);
        twitterBaseImpl.addRateLimitStatusListener(listener);

        RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, null, true);
        action.accept(event);
        assertTrue(listener.onRateLimitReached(event).isAuthenticated());
    }

    @Test
    public void testSetOAuthConsumer() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = null;
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        String consumerKey = "consumerKey";
        String consumerSecret = "consumerSecret";
        twitterBaseImpl.setOAuthConsumer(consumerKey, consumerSecret);
        assertEquals(consumerKey, twitterBaseImpl.getAuthorization().getConsumerKey());
        assertEquals(consumerSecret, twitterBaseImpl.getAuthorization().getConsumerSecret());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetOAuthConsumerWithActiveAuthorization() throws TwitterException {
        Configuration conf = new Configuration();
        String consumerKey = "consumerKey";
        String consumerSecret = "consumerSecret";
        Authorization auth = new BasicAuthorization("user1", "pass1");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl.setOAuthConsumer(consumerKey, consumerSecret);
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("user1", "pass1");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        assertNotNull(requestToken);
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("user1", "pass1");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
        assertNotNull(accessToken);
    }

    @Test
    public void testGetOAuthAccessTokenWithOAuthRequestToken() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("user1", "pass1");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken);
        assertNotNull(accessToken);
    }

    @Test
    public void testGetOAuthAccessTokenWithOAuthVerifier() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("user1", "pass1");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, "oauthVerifier");
        assertNotNull(accessToken);
    }
}