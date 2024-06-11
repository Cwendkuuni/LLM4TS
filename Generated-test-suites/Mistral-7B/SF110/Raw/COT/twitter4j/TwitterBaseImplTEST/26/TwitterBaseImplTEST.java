package twitter4j; // replace with the actual package name if needed

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.auth.*;
import twitter4j.conf.*;
import twitter4j.http.*;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private Configuration conf;
    private Authorization auth;

    @Before
    public void setup() {
        conf = new Configuration();
        auth = new BasicAuthorization("consumerKey", "consumerSecret");
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        String screenName = twitterBaseImpl.getScreenName();
        assertNotNull(screenName);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithoutAuth() throws TwitterException, IllegalStateException {
        twitterBaseImpl.auth = null;
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        long id = twitterBaseImpl.getId();
        assertNotNull(id);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithoutAuth() throws TwitterException, IllegalStateException {
        twitterBaseImpl.auth = null;
        twitterBaseImpl.getId();
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        assertTrue(twitterBaseImpl.rateLimitStatusListeners.contains(listener));
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullRateLimitStatusListener() {
        twitterBaseImpl.addRateLimitStatusListener(null);
    }

    @Test
    public void testOnRateLimitStatus() {
        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
        RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);

        twitterBaseImpl.onRateLimitStatus((RateLimitStatusListener) action);
        twitterBaseImpl.onRateLimitStatus(event);
        action.accept(event);
    }

    @Test
    public void testOnRateLimitReached() {
        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
        RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);

        twitterBaseImpl.onRateLimitReached((RateLimitStatusListener) action);
        twitterBaseImpl.onRateLimitReached(event);
        action.accept(event);
    }

    @Test
    public void testHttpResponseReceived() throws TwitterException {
        HttpResponseEvent event = mock(HttpResponseEvent.class);
        RateLimitStatus rateLimitStatus = new RateLimitStatus(100, 150, 60);
        twitterBaseImpl.rateLimitStatusListeners.add(new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                assertEquals(rateLimitStatus, event.getRateLimitStatus());
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
                // not tested here
            }
        });
        twitterBaseImpl.httpResponseReceived(event);
    }

    @Test
    public void testSetOAuthConsumer() {
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
        assertTrue(twitterBaseImpl.auth instanceof OAuthAuthorization || twitterBaseImpl.auth instanceof OAuth2Authorization);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullOAuthConsumer() {
        twitterBaseImpl.setOAuthConsumer(null, null);
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        assertNotNull(requestToken);
    }

    @Test(expected = TwitterException.class)
    public void testGetOAuthRequestTokenWithoutAuth() throws TwitterException {
        twitterBaseImpl.auth = null;
        twitterBaseImpl.getOAuth();
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
        assertNotNull(accessToken);
    }

    @Test(expected = TwitterException.class)
    public void testGetOAuthAccessTokenWithoutAuth() throws TwitterException {
        twitterBaseImpl.auth = null;
        twitterBaseImpl.getOAuthAccessToken();
    }

    @Test
    public void testGetOAuthAccessTokenWithVerifier() throws TwitterException {
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("verifier");
        assertNotNull(accessToken);
    }

    @Test
    public void testSetOAuthAccessToken() {
        AccessToken accessToken = twitterBaseImpl.getOAuth().getOAuthAccessToken();
        twitterBaseImpl.setOAuthAccessToken(accessToken);
        assertEquals(accessToken, twitterBaseImpl.getOAuth().getOAuthAccessToken());
    }

    @Test
    public void testEquals() {
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
        assertTrue(twitterBaseImpl.equals(twitterBaseImpl2));
    }

    @Test
    public void testNotEquals() {
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, null);
        assertFalse(twitterBaseImpl.equals(twitterBaseImpl2));
    }

    @Test
    public void testHashCode() {
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
        assertEquals(twitterBaseImpl.hashCode(), twitterBaseImpl2.hashCode());
    }

    @Test
    public void testToString() {
        String toString = twitterBaseImpl.toString();
        assertTrue(toString.contains("TwitterBase{conf="));
        assertTrue(toString.contains("auth="));
    }
}