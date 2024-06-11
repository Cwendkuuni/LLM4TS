import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.auth.Authorization;
import twitter4j.auth.BasicAuthorization;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.NullAuthorization;
import twitter4j.auth.OAuth2Authorization;
import twitter4j.http.HttpClient;
import twitter4j.http.HttpClientFactory;
import twitter4j.http.HttpResponseEvent;
import twitter4j.http.HttpResponseListener;
import twitter4j.http.ResponseStatus;
import twitter4j.util.function.RateLimitStatusListener;
import twitter4j.util.function.Consumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private Configuration configuration;
    private Authorization authorization;
    private HttpClient httpClient;
    private RateLimitStatusListener listener;

    @Before
    public void setUp() throws Exception {
        configuration = new Configuration();
        authorization = new BasicAuthorization("testConsumerKey", "testConsumerSecret");
        httpClient = HttpClientFactory.getInstance(configuration.getHttpClientConfiguration());
        listener = Mockito.mock(RateLimitStatusListener.class);
        twitterBaseImpl = new TwitterBaseImpl(configuration, authorization);
        twitterBaseImpl.init();
        twitterBaseImpl.setFactory();
    }

    @Test
    public void testGetScreenName() {
        twitterBaseImpl.addRateLimitStatusListener(listener);
        Mockito.when(authorization.isEnabled()).thenReturn(true);
        Mockito.when(authorization.getInstanceOfType(BasicAuthorization.class)).thenReturn(authorization);
        String screenName = twitterBaseImpl.getScreenName();
        verify(listener).onRateLimitStatus(Mockito.any(RateLimitStatusEvent.class));
        verify(listener).onRateLimitReached(Mockito.any(RateLimitStatusEvent.class));
        assertNotNull(screenName);
        assertEquals("testConsumerKey", twitterBaseImpl.getScreenName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWhenAuthorizationIsNotEnabled() {
        twitterBaseImpl.addRateLimitStatusListener(listener);
        Mockito.when(authorization.isEnabled()).thenReturn(false);
        twitterBaseImpl.getScreenName();
        verify(listener).onRateLimitStatus(Mockito.any(RateLimitStatusEvent.class));
        verify(listener).onRateLimitReached(Mockito.any(RateLimitStatusEvent.class));
    }

    @Test
    public void testGetId() {
        twitterBaseImpl.addRateLimitStatusListener(listener);
        Mockito.when(authorization.isEnabled()).thenReturn(true);
        User user = new User();
        user.setScreenName("testScreenName");
        user.setId(1L);
        Mockito.when(httpClient.get(Mockito.anyString(), Mockito.any(HttpParameter[].class), Mockito.eq(authorization), Mockito.eq(configuration)))
                .thenReturn(new HttpResponseEvent(new HttpResponse(new ByteArrayInputStream(new byte[0]), new ResponseStatus(200, "OK"))));
        Mockito.when(factory.createUserFromJson(Mockito.any(JSONObject.class)))
                .thenReturn(user);
        long id = twitterBaseImpl.getId();
        verify(listener).onRateLimitStatus(Mockito.any(RateLimitStatusEvent.class));
        verify(listener).onRateLimitReached(Mockito.any(RateLimitStatusEvent.class));
        assertNotNull(id);
        assertEquals(1L, id);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWhenAuthorizationIsNotEnabled() {
        twitterBaseImpl.addRateLimitStatusListener(listener);
        Mockito.when(authorization.isEnabled()).thenReturn(false);
        twitterBaseImpl.getId();
        verify(listener).onRateLimitStatus(Mockito.any(RateLimitStatusEvent.class));
        verify(listener).onRateLimitReached(Mockito.any(RateLimitStatusEvent.class));
    }

    @Test
    public void testGetIdWhenUserIsNotFound() {
        twitterBaseImpl.addRateLimitStatusListener(listener);
        Mockito.when(authorization.isEnabled()).thenReturn(true);
        Mockito.when(httpClient.get(Mockito.anyString(), Mockito.any(HttpParameter[].class), Mockito.eq(authorization), Mockito.eq(configuration)))
                .thenReturn(new HttpResponseEvent(new HttpResponse(new ByteArrayInputStream(new byte[0]), new ResponseStatus(404, "Not Found"))));
        twitterBaseImpl.getId();
        verify(listener).onRateLimitStatus(Mockito.any(RateLimitStatusEvent.class));
        verify(listener).onRateLimitReached(Mockito.any(RateLimitStatusEvent.class));
    }

    @Test
    public void testAddRateLimitStatusListener() {
        twitterBaseImpl.setFactory(new JSONImplFactory(configuration));
        twitterBaseImpl.addRateLimitStatusListener(listener);
        List<RateLimitStatusListener> rateLimitStatusListeners = twitterBaseImpl.getRateLimitStatusListeners();
        assertEquals(1, rateLimitStatusListeners.size());
        assertTrue(rateLimitStatusListeners.contains(listener));
    }

    @Test
    public void testOnRateLimitStatus() {
        twitterBaseImpl.onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, null, false));
        verify(listener).onRateLimitStatus(Mockito.any(RateLimitStatusEvent.class));
    }

    @Test
    public void testOnRateLimitReached() {
        twitterBaseImpl.onRateLimitReached(new RateLimitStatusEvent(twitterBaseImpl, null, false));
        verify(listener).onRateLimitReached(Mockito.any(RateLimitStatusEvent.class));
    }

    @Test
    public void testHttpResponseReceived() {
        twitterBaseImpl.onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, null, false));
        HttpResponseEvent httpResponseEvent = new HttpResponseEvent(new HttpResponse(new ByteArrayInputStream(new byte[0]), new ResponseStatus(200, "OK")), null);
        twitterBaseImpl.httpResponseReceived(httpResponseEvent);
        verify(listener).onRateLimitStatus(Mockito.any(RateLimitStatusEvent.class));
        verify(listener).onRateLimitReached(Mockito.any(RateLimitStatusEvent.class));
    }

    @Test
    public void testHttpResponseReceivedWithRateLimitStatus() {
        twitterBaseImpl.onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, null, false));
        HttpResponseEvent httpResponseEvent = new HttpResponseEvent(new HttpResponse(new ByteArrayInputStream(new byte[0]), new ResponseStatus(429, "Rate Limit Exceeded")), null);
        twitterBaseImpl.httpResponseReceived(httpResponseEvent);
        verify(listener).onRateLimitStatus(Mockito.any(RateLimitStatusEvent.class));
        verify(listener).onRateLimitReached(Mockito.any(RateLimitStatusEvent.class));
    }

    @Test
    public void testGetAuthorization() {
        assertEquals(authorization, twitterBaseImpl.getAuthorization());
    }

    @Test
    public void testGetConfiguration() {
        assertEquals(configuration, twitterBaseImpl.getConfiguration());
    }

    @Test
    public void testEnsureAuthorizationEnabled() {
        twitterBaseImpl = new TwitterBaseImpl(configuration, null);
        try {
            twitterBaseImpl.ensureAuthorizationEnabled();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Authentication credentials are missing. See http://twitter4j.org/en/configuration.html for details. See and register at http://apps.twitter.com/", e.getMessage());
        }
    }

    @Test
    public void testEnsureOAuthEnabled() {
        Authorization oauthAuthorization = new OAuthAuthorization("testConsumerKey", "testConsumerSecret", "testAccessToken", "testAccessTokenSecret");
        twitterBaseImpl = new TwitterBaseImpl(configuration, oauthAuthorization);
        try {
            twitterBaseImpl.ensureOAuthEnabled();
        } catch (IllegalStateException e) {
            fail();
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testSetOAuthConsumerWithEmptyConsumerKey() {
        twitterBaseImpl.setOAuthConsumer("", "");
    }

    @Test(expected = IllegalStateException.class)
    public void testSetOAuthConsumerWithEmptyConsumerSecret() {
        twitterBaseImpl.setOAuthConsumer("testConsumerKey", "");
    }

    @Test
    public void testSetOAuthConsumer() {
        twitterBaseImpl.setOAuthConsumer("testConsumerKey", "testConsumerSecret");
        assertEquals("testConsumerKey", authorization.getConsumerKey());
        assertEquals("testConsumerSecret", authorization.getConsumerSecret());
    }

    @Test
    public void testGetOAuthRequestToken() {
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        assertNotNull(requestToken);
        assertEquals("oauth_token", requestToken.getToken());
        assertEquals("oauth_token_secret", requestToken.getTokenSecret());
    }

    @Test
    public void testGetOAuthAccessToken() throws Exception {
        RequestToken requestToken = Mockito.mock(RequestToken.class);
        String oauthVerifier = "oauth_verifier";
        Mockito.when(requestToken.getToken()).thenReturn("oauth_token");
        Mockito.when(requestToken.getTokenSecret()).thenReturn("oauth_token_secret");
        Mockito.when(getOAuth().getOAuthAccessToken(requestToken)).thenReturn(new AccessToken("testScreenName", "testUserId"));
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(oauthVerifier);
        assertEquals("testScreenName", accessToken.getScreenName());
        assertEquals("testUserId", accessToken.getUserId());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOAuthAccessTokenWithNullVerifier() {
        RequestToken requestToken = Mockito.mock(RequestToken.class);
        twitterBaseImpl.getOAuthAccessToken(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOAuthAccessTokenWithInvalidVerifier() {
        RequestToken requestToken = Mockito.mock(RequestToken.class);
        String oauthVerifier = "invalid_verifier";
        twitterBaseImpl.getOAuthAccessToken(oauthVerifier);
    }

    @Test
    public void testEquals() {
        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(configuration, authorization);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(configuration, authorization);
        assertTrue(twitterBaseImpl1.equals(twitterBaseImpl2));
        TwitterBaseImpl twitterBaseImpl3 = new TwitterBaseImpl(configuration, new OAuthAuthorization("testConsumerKey", "testConsumerSecret"));
        assertFalse(twitterBaseImpl1.equals(twitterBaseImpl3));
        TwitterBaseImpl twitterBaseImpl4 = new TwitterBaseImpl(new Configuration(), authorization);
        assertFalse(twitterBaseImpl1.equals(twitterBaseImpl4));
        TwitterBaseImpl twitterBaseImpl5 = new TwitterBaseImpl(configuration, authorization);
        twitterBaseImpl5.setFactory(new JSONImplFactory(new Configuration()));
        assertFalse(twitterBaseImpl1.equals(twitterBaseImpl5));
    }

    @Test
    public void testHashCode() {
        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(configuration, authorization);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(configuration, authorization);
        assertEquals(twitterBaseImpl1.hashCode(), twitterBaseImpl2.hashCode());
        TwitterBaseImpl twitterBaseImpl3 = new TwitterBaseImpl(configuration, new OAuthAuthorization("testConsumerKey", "testConsumerSecret"));
        assertNotEquals(twitterBaseImpl1.hashCode(), twitterBaseImpl3.hashCode());
        TwitterBaseImpl twitterBaseImpl4 = new TwitterBaseImpl(new Configuration(), authorization);
        assertNotEquals(twitterBaseImpl1.hashCode(), twitterBaseImpl4.hashCode());
        TwitterBaseImpl twitterBaseImpl5 = new TwitterBaseImpl(configuration, authorization);
        twitterBaseImpl5.setFactory(new JSONImplFactory(new Configuration()));
        assertNotEquals(twitterBaseImpl1.hashCode(), twitterBaseImpl5.hashCode());
    }

    @Test
    public void testToString() {
        twitterBaseImpl.toString();
    }
}