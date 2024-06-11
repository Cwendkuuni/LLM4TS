package twitter4j;

import static org.junit.Assert.*;
import twitter4j.conf.*;
import org.junit.Before;
import org.junit.Test;
import twitter4j.auth.*;
import java.util.*;

public class TwitterBaseImplTest {

    private Configuration conf;
    private Authorization auth;
    private TwitterBaseImpl twitterBaseImpl;

    @Before
    public void setUp() {
        conf = new Configuration();
        auth = new BasicAuthorization();
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        assertNull(twitterBaseImpl.getScreenName());

        twitterBaseImpl.fillInIDAndScreenName();
        assertNotNull(twitterBaseImpl.getScreenName());

        assertEquals("expectedScreenName", twitterBaseImpl.getScreenName());
        twitterBaseImpl.setScreenName("expectedScreenName");
        assertEquals("expectedScreenName", twitterBaseImpl.getScreenName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithUninitializedAuth() throws TwitterException, IllegalStateException {
        twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        assertNull(twitterBaseImpl.getId());

        twitterBaseImpl.fillInIDAndScreenName();
        assertNotNull(twitterBaseImpl.getId());

        assertEquals(1L, twitterBaseImpl.getId());
        twitterBaseImpl.setId(1L);
        assertEquals(1L, twitterBaseImpl.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithUninitializedAuth() throws TwitterException, IllegalStateException {
        twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.getId();
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        assertTrue(twitterBaseImpl.rateLimitStatusListeners.contains(listener));
    }

    @Test
    public void testOnRateLimitStatus() {
        Consumer<RateLimitStatusEvent> action = event -> assertEquals(RateLimitStatus.LIMIT_NONE, event.getRateLimitStatus().getLimitStatus());
        twitterBaseImpl.onRateLimitStatus(action);

        HttpResponseEvent httpResponseEvent = mock(HttpResponseEvent.class);
        RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, null, true);
        RateLimitStatus rateLimitStatus = new RateLimitStatus(RateLimitStatus.LIMIT_NONE, 0, 15, 60);
        httpResponseEvent.setRateLimitStatus(rateLimitStatus);
        twitterBaseImpl.httpResponseReceived(httpResponseEvent);
        action.accept(rateLimitStatusEvent);
    }

    @Test
    public void testOnRateLimitReached() {
        Consumer<RateLimitStatusEvent> action = event -> assertEquals(RateLimitStatus.LIMIT_NONE, event.getRateLimitStatus().getLimitStatus());
        twitterBaseImpl.onRateLimitReached(action);

        HttpResponseEvent httpResponseEvent = mock(HttpResponseEvent.class);
        RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, null, true);
        RateLimitStatus rateLimitStatus = new RateLimitStatus(RateLimitStatus.LIMIT_REACHED, 0, 15, 60);
        httpResponseEvent.setRateLimitStatus(rateLimitStatus);
        twitterBaseImpl.httpResponseReceived(httpResponseEvent);
        action.accept(rateLimitStatusEvent);
    }

    @Test
    public void testSetOAuthConsumer() {
        String consumerKey = "consumerKey";
        String consumerSecret = "consumerSecret";
        twitterBaseImpl.setOAuthConsumer(consumerKey, consumerSecret);

        assertEquals(consumerKey, twitterBaseImpl.getAuthorization().getConsumerKey());
        assertEquals(consumerSecret, twitterBaseImpl.getAuthorization().getConsumerSecret());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetOAuthConsumerWithUninitializedAuth() {
        twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        assertNotNull(requestToken);
        assertEquals("oauth_token", requestToken.getToken());
        assertEquals("oauth_token_secret", requestToken.getTokenSecret());
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
        assertNotNull(accessToken);
        assertEquals("accessToken", accessToken.getToken());
        assertEquals("accessTokenSecret", accessToken.getTokenSecret());
        assertEquals("screenName", accessToken.getScreenName());
        assertEquals(1L, accessToken.getUserId());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOAuthAccessTokenWithUninitializedAuth() throws TwitterException {
        twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.getOAuthAccessToken();
    }

    @Test
    public void testSetOAuthAccessToken() {
        AccessToken accessToken = new AccessToken(1L, "accessToken", "accessTokenSecret", "screenName");
        twitterBaseImpl.setOAuthAccessToken(accessToken);

        assertEquals("screenName", twitterBaseImpl.getScreenName());
        assertEquals(1L, twitterBaseImpl.getId());
    }
}