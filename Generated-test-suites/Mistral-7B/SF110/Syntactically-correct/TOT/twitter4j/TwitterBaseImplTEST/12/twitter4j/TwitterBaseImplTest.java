package twitter4j.test;

import static org.junit.Assert.*;
import static twitter4j.conf.Configuration.Builder.*;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.impl.TwitterBaseImpl;
import twitter4j.util.function.*;

import java.util.*;
import java.io.*;

public class TwitterBaseImplTest {

    @Test
    public void testInitializeTwitterBaseImpl() {
        Configuration conf = new Configuration.Builder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .setOAuthAccessToken("accessToken")
                .setOAuthAccessTokenSecret("accessTokenSecret")
                .build();

        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        assertNotNull(twitterBaseImpl.getScreenName());
        assertNotNull(twitterBaseImpl.getId());
        assertNotNull(twitterBaseImpl.getAuthorization());
        assertNotNull(twitterBaseImpl.getConfiguration());
        assertNotNull(twitterBaseImpl.getHttp());
    }

    @Test
    public void testInitializeTwitterBaseImplWithoutCredentials() {
        Configuration conf = new Configuration.Builder()
                .setDebugEnabled(true)
                .build();

        Authorization auth = null;
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        assertNull(twitterBaseImpl.getScreenName());
        assertEquals(0, twitterBaseImpl.getId());
        assertNull(twitterBaseImpl.getAuthorization());
        assertNotNull(twitterBaseImpl.getConfiguration());
        assertNotNull(twitterBaseImpl.getHttp());
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeTwitterBaseImplWithInvalidCredentials() {
        Configuration conf = new Configuration.Builder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("")
                .setOAuthConsumerSecret("")
                .build();

        Authorization auth = null;
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        fail("Should have thrown an IllegalStateException");
    }

    @Test
    public void testInitializeTwitterBaseImplWithApplicationOnlyAuth() {
        Configuration conf = new Configuration.Builder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .setApplicationOnlyAuthEnabled(true)
                .build();

        Authorization auth = new OAuth2Authorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        assertNotNull(twitterBaseImpl.getScreenName());
        assertNotNull(twitterBaseImpl.getId());
        assertNotNull(twitterBaseImpl.getAuthorization());
        assertNotNull(twitterBaseImpl.getConfiguration());
        assertNotNull(twitterBaseImpl.getHttp());
    }

    @Test
    public void testInitializeTwitterBaseImplWithOAuth() {
        Configuration conf = new Configuration.Builder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .setOAuthAccessToken("accessToken")
                .setOAuthAccessTokenSecret("accessTokenSecret")
                .build();

        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        assertNotNull(twitterBaseImpl.getScreenName());
        assertNotNull(twitterBaseImpl.getId());
        assertNotNull(twitterBaseImpl.getAuthorization());
        assertNotNull(twitterBaseImpl.getConfiguration());
        assertNotNull(twitterBaseImpl.getHttp());
    }

    @Test
    public void testInitializeTwitterBaseImplWithNullAuthorization() {
        Configuration conf = new Configuration.Builder()
                .setDebugEnabled(true)
                .build();

        Authorization auth = null;
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        assertNull(twitterBaseImpl.getScreenName());
        assertEquals(0, twitterBaseImpl.getId());
        assertNull(twitterBaseImpl.getAuthorization());
        assertNotNull(twitterBaseImpl.getConfiguration());
        assertNotNull(twitterBaseImpl.getHttp());
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration.Builder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .setOAuthAccessToken("accessToken")
                .setOAuthAccessTokenSecret("accessTokenSecret")
                .build();

        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        String screenName = twitterBaseImpl.getScreenName();
        assertNotNull(screenName);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithoutCredentials() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration.Builder()
                .setDebugEnabled(true)
                .build();

        Authorization auth = null;
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl.getScreenName();

        fail("Should have thrown an IllegalStateException");
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration.Builder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .setOAuthAccessToken("accessToken")
                .setOAuthAccessTokenSecret("accessTokenSecret")
                .build();

        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        long id = twitterBaseImpl.getId();
        assertNotNull(id);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithoutCredentials() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration.Builder()
                .setDebugEnabled(true)
                .build();

        Authorization auth = null;
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl.getId();

        fail("Should have thrown an IllegalStateException");
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
    public void testOnRateLimitStatus() {
        Configuration conf = new Configuration.Builder()
                .setDebugEnabled(true)
                .build();

        Authorization auth = null;
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);

        RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
        when(event.getRateLimitStatus()).thenReturn(new RateLimitStatus(15, 100, 60));

        twitterBaseImpl.onRateLimitStatus(action);
        verify(event).getMessage();
        verify(event).getRateLimitStatus();
    }

    @Test
    public void testOnRateLimitReached() {
        Configuration conf = new Configuration.Builder()
                .setDebugEnabled(true)
                .build();

        Authorization auth = null;
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);

        RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
        when(event.getRateLimitStatus()).thenReturn(new RateLimitStatus(15, 100, 60));

        twitterBaseImpl.onRateLimitReached(action);
        verify(event).getMessage();
        verify(event).getRateLimitStatus();
    }
}