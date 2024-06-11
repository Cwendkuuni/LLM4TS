package twitter4j.test;

import static org.junit.Assert.*;
import static twitter4j.conf.Configuration.Builder.*;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.OAuthAuthorization;
import twitter4j.conf.OAuth2Authorization;
import twitter4j.exceptions.TwitterException;
import twitter4j.http.HttpClient;
import twitter4j.http.HttpClientFactory;
import twitter4j.util.function.RateLimitStatusListener;
import twitter4j.util.function.RateLimitStatusEvent;

import org.junit.Test;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws TwitterException {
        Configuration conf = new Configuration.Builder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("testConsumerKey")
                .setOAuthConsumerSecret("testConsumerSecret")
                .setOAuthAccessToken("testAccessToken")
                .setOAuthAccessTokenSecret("testAccessTokenSecret")
                .build();

        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        assertEquals("testScreenName", twitterBaseImpl.getScreenName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameThrowsExceptionWhenAuthDisabled() throws TwitterException {
        Configuration conf = new Configuration.Builder()
                .setDebugEnabled(true)
                .build();

        Authorization auth = null;
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws TwitterException {
        Configuration conf = new Configuration.Builder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("testConsumerKey")
                .setOAuthConsumerSecret("testConsumerSecret")
                .setOAuthAccessToken("testAccessToken")
                .setOAuthAccessTokenSecret("testAccessTokenSecret")
                .build();

        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        assertEquals(1L, twitterBaseImpl.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdThrowsExceptionWhenAuthDisabled() throws TwitterException {
        Configuration conf = new Configuration.Builder()
                .setDebugEnabled(true)
                .build();

        Authorization auth = null;
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl.getId();
    }

    @Test
    public void testAddRateLimitStatusListener() {
        Configuration conf = new Configuration.Builder()
                .setDebugEnabled(true)
                .build();

        Authorization auth = null;
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);

        assertTrue(twitterBaseImpl.getRateLimitStatusListeners().contains(listener));
    }

    @Test
    public void testOnRateLimitStatus() throws TwitterException {
        Configuration conf = new Configuration.Builder()
                .setDebugEnabled(true)
                .build();

        Authorization auth = null;
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        Consumer<RateLimitStatusEvent> consumer = event -> {
            assertEquals(RateLimitStatus.LIMIT_RATE_LIMIT, event.getRateLimitStatus().getLimitType());
            assertEquals(429, event.getRateLimitStatus().getSecondsRemaining());
        };

        twitterBaseImpl.onRateLimitStatus(consumer);

        // Simulate a rate limit event
        RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(RateLimitStatus.LIMIT_RATE_LIMIT, 429), true);
        consumer.accept(event);
    }
}