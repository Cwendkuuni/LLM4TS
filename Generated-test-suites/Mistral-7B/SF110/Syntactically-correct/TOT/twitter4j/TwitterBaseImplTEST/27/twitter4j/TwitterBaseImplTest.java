import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.util.function.Function;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private Configuration conf;
    private Authorization auth;
    @Captor
    private ArgumentCaptor<RateLimitStatusListener> rateLimitStatusListenerCaptor;

    @Before
    public void setup() {
        conf = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("testConsumerKey")
                .setOAuthConsumerSecret("testConsumerSecret")
                .setOAuthAccessToken("testAccessToken")
                .setOAuthAccessTokenSecret("testAccessTokenSecret")
                .build();
        auth = new OAuthAuthorization(conf);
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        twitterBaseImpl.getScreenName();
        assertNotNull(twitterBaseImpl.screenName);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithoutAuthentication() throws TwitterException, IllegalStateException {
        twitterBaseImpl.auth = null;
        twitterBaseImpl.getScreenName();
        fail("An IllegalStateException should have been thrown.");
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        twitterBaseImpl.getId();
        assertNotNull(twitterBaseImpl.id);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithoutAuthentication() throws TwitterException, IllegalStateException {
        twitterBaseImpl.auth = null;
        twitterBaseImpl.getId();
        fail("An IllegalStateException should have been thrown.");
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        User user = twitterBaseImpl.fillInIDAndScreenName();
        assertNotNull(user.getScreenName());
        assertNotNull(user.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        twitterBaseImpl.addRateLimitStatusListener(mockListener());
        assertEquals(1, twitterBaseImpl.rateLimitStatusListeners.size());
    }

    @Test
    public void testOnRateLimitStatus() {
        Consumer<RateLimitStatusEvent> action = new Consumer<RateLimitStatusEvent>() {
            @Override
            public void accept(RateLimitStatusEvent event) {
                assertEquals("Too many requests.", event.getLimited().getRemaining());
            }
        };
        twitterBaseImpl.onRateLimitStatus(action);
        Mockito.verify(twitterBaseImpl).onRateLimitStatus(rateLimitStatusListenerCaptor.capture());
        rateLimitStatusListenerCaptor.getValue().onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, null, true));
        action.accept(new RateLimitStatusEvent(twitterBaseImpl, null, true));
    }

    @Test
    public void testOnRateLimitReached() {
        Consumer<RateLimitStatusEvent> action = new Consumer<RateLimitStatusEvent>() {
            @Override
            public void accept(RateLimitStatusEvent event) {
                assertEquals("You have reached your rate limit.", event.getLimited().getMessage());
            }
        };
        twitterBaseImpl.onRateLimitReached(action);
        Mockito.verify(twitterBaseImpl).onRateLimitStatus(rateLimitStatusListenerCaptor.capture());
        rateLimitStatusListenerCaptor.getValue().onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, null, true));
        rateLimitStatusListenerCaptor.getValue().onRateLimitReached(new RateLimitStatusEvent(twitterBaseImpl, null, true));
        action.accept(new RateLimitStatusEvent(twitterBaseImpl, null, true));
    }

    @Test
    public void testHttpResponseReceived() {
        HttpResponseEvent httpResponseEvent = Mockito.mock(HttpResponseEvent.class);
        RateLimitStatus rateLimitStatus = new RateLimitStatus(10, 20, 30, 40, 50, 60, 70, 80, 90);
        Mockito.when(httpResponseEvent.getRateLimitStatus()).thenReturn(rateLimitStatus);

        twitterBaseImpl.httpResponseReceived(httpResponseEvent);
        Mockito.verify(twitterBaseImpl).onRateLimitStatus(rateLimitStatusListenerCaptor.capture());
        rateLimitStatusListenerCaptor.getValue().onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, rateLimitStatus, true));

        Mockito.when(httpResponseEvent.getRateLimitStatus()).thenReturn(null);
        twitterBaseImpl.httpResponseReceived(httpResponseEvent);
        Mockito.verify(twitterBaseImpl).onRateLimitStatus(rateLimitStatusListenerCaptor.capture());
        rateLimitStatusListenerCaptor.getValue().onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, null, true));
    }

    @Test
    public void testGetAuthorization() {
        assertEquals(auth, twitterBaseImpl.getAuthorization());
    }

    @Test
    public void testGetConfiguration() {
        assertEquals(conf, twitterBaseImpl.getConfiguration());
    }

    @Test
    public void testEnsureAuthorizationEnabled() {
        twitterBaseImpl.ensureAuthorizationEnabled();
        assertFalse(twitterBaseImpl.auth.isEnabled());
        twitterBaseImpl.auth = new OAuthAuthorization(conf);
        twitterBaseImpl.ensureAuthorizationEnabled();
        assertTrue(twitterBaseImpl.auth.isEnabled());
    }

    @Test
    public void testEnsureOAuthEnabled() {
        twitterBaseImpl.ensureOAuthEnabled();
        assertFalse(twitterBaseImpl.auth.isOAuthEnabled());
        twitterBaseImpl.auth = new OAuthAuthorization(conf);
        twitterBaseImpl.ensureOAuthEnabled();
        assertTrue(twitterBaseImpl.auth.isOAuthEnabled());
    }

    @Test
    public void testSetOAuthConsumer() {
        twitterBaseImpl.setOAuthConsumer("newConsumerKey", "newConsumerSecret");
        assertEquals("newConsumerKey", twitterBaseImpl.auth.getConsumerKey());
        assertEquals("newConsumerSecret", twitterBaseImpl.auth.getConsumerSecret());
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        assertNotNull(requestToken.getToken());
        assertNotNull(requestToken.getTokenSecret());
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
        assertNotNull(accessToken.getScreenName());
        assertNotNull(accessToken.getId());
    }

    @Test
    public void testSetOAuthAccessToken() {
        AccessToken accessToken = new AccessToken("testScreenName", 1L);
        twitterBaseImpl.setOAuthAccessToken(accessToken);
        assertEquals(accessToken, twitterBaseImpl.getOAuthAccessToken());
    }

    @Test
    public void testGetOAuthAccessTokenWithOAuthVerifier() throws TwitterException {
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("OAuthVerifier");
        assertNotNull(accessToken.getScreenName());
        assertNotNull(accessToken.getId());
    }

    @Test
    public void testGetOAuthAccessTokenWithRequestToken() throws TwitterException {
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken);
        assertNotNull(accessToken.getScreenName());
        assertNotNull(accessToken.getId());
    }

    @Test
    public void testGetOAuthAccessTokenWithRequestTokenAndOAuthVerifier() throws TwitterException {
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, "OAuthVerifier");
        assertNotNull(accessToken.getScreenName());
        assertNotNull(accessToken.getId());
    }

    private RateLimitStatusListener mockListener() {
        return new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {

            }
        };
    }
}