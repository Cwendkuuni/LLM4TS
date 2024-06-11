package twitter4j; // replace with your package name if needed

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import twitter4j.Configuration;
import twitter4j.Configuration.NetworkConnectionType;
import twitter4j.HttpClient;
import twitter4j.OAuth;
import twitter4j.OAuthAccessToken;
import twitter4j.OAuthAuthorization;
import twitter4j.OAuthRequestToken;
import twitter4j.OAuthSupport;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.HttpClientFactory;
import twitter4j.http.HttpResponseEvent;
import twitter4j.http.HttpResponseListener;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private Configuration conf;
    private OAuthAuthorization auth;
    private HttpClient http;
    private List<RateLimitStatusListener> rateLimitStatusListeners;

    @Before
    public void setUp() {
        conf = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("test-consumer-key")
                .setOAuthConsumerSecret("test-consumer-secret")
                .setOAuthAccessToken("test-access-token")
                .setOAuthAccessTokenSecret("test-access-token-secret")
                .setHttpConnectionTimeout(10 * 1000)
                .build();
        auth = mock(OAuthAuthorization.class);
        http = HttpClientFactory.getInstance(conf.getHttpClientConfiguration());
        rateLimitStatusListeners = new ArrayList<>();
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        when(auth.isEnabled()).thenReturn(true);
        String screenName = twitterBaseImpl.getScreenName();
        assertSame(screenName, twitterBaseImpl.screenName);
        assertTrue(screenName != null);
    }

    @Test(expected = TwitterException.class)
    public void testGetScreenNameNoAuth() throws TwitterException, IllegalStateException {
        when(auth.isEnabled()).thenReturn(false);
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        when(auth.isEnabled()).thenReturn(true);
        long id = twitterBaseImpl.getId();
        assertSame(id, twitterBaseImpl.id);
        assertTrue(id != 0);
    }

    @Test(expected = TwitterException.class)
    public void testGetIdNoAuth() throws TwitterException, IllegalStateException {
        when(auth.isEnabled()).thenReturn(false);
        twitterBaseImpl.getId();
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        when(auth.isEnabled()).thenReturn(true);
        User user = twitterBaseImpl.fillInIDAndScreenName();
        assertSame(user, twitterBaseImpl.user);
        assertTrue(user != null);
        assertTrue(user.getScreenName() != null);
        assertTrue(user.getId() != 0);
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        assertTrue(rateLimitStatusListeners.contains(listener));
    }

    @Test
    public void testHttpResponseReceivedWithRateLimit() {
        HttpResponseEvent httpResponseEvent = mock(HttpResponseEvent.class);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        when(rateLimitStatus.getLimit().getRemaining())
                .thenReturn(1L);
        when(rateLimitStatus.getLimit().getLimit())
                .thenReturn(10L);
        when(rateLimitStatus.getResetWindowSeconds())
                .thenReturn(60L);
        when(httpResponseEvent.getResponse()).thenReturn(mock(HttpResponse.class));
        when(httpResponseEvent.getTwitterException()).thenReturn(null);
        when(httpResponseEvent.isAuthenticated()).thenReturn(true);
        ArgumentCaptor<RateLimitStatusEvent> rateLimitStatusEventCaptor = ArgumentCaptor.forClass(RateLimitStatusEvent.class);

        twitterBaseImpl.onRateLimitStatus(rateLimitEvent -> {
            // any action here
        });
        twitterBaseImpl.onRateLimitStatus(rateLimitEvent -> {
            // any action here
        });
        twitterBaseImpl.onRateLimitReached(rateLimitEvent -> {
            // any action here
        });
        twitterBaseImpl.onRateLimitReached(rateLimitEvent -> {
            // any action here
        });
        twitterBaseImpl.httpResponseReceived(httpResponseEvent);

        verify(rateLimitStatusListeners, times(2)).onRateLimitStatus(rateLimitStatusEventCaptor.capture());
        RateLimitStatusEvent rateLimitStatusEvent1 = rateLimitStatusEventCaptor.getAllValues().get(0);
        RateLimitStatusEvent rateLimitStatusEvent2 = rateLimitStatusEventCaptor.getAllValues().get(1);
        assertTrue(rateLimitStatusEvent1 != null);
        assertTrue(rateLimitStatusEvent2 != null);
        assertEquals(rateLimitStatus, rateLimitStatusEvent1.getRateLimitStatus());
        assertEquals(rateLimitStatus, rateLimitStatusEvent2.getRateLimitStatus());
        assertTrue(rateLimitStatusEvent1.isAuthenticated());
        assertTrue(rateLimitStatusEvent2.isAuthenticated());
        assertTrue(rateLimitStatusEvent1.getResetTime() != 0);
        assertTrue(rateLimitStatusEvent2.getResetTime() != 0);

        assertEquals(rateLimitStatus.getLimit().getRemaining(), rateLimitStatusEvent1.getRemainingCount());
        assertEquals(rateLimitStatus.getLimit().getLimit(), rateLimitStatusEvent1.getLimit());
        assertEquals(rateLimitStatus.getResetWindowSeconds(), rateLimitStatusEvent1.getResetWindowSeconds());
        assertEquals(rateLimitStatus.getResetWindowSeconds(), rateLimitStatusEvent2.getResetWindowSeconds());
    }

    @Test
    public void testHttpResponseReceivedWithoutRateLimit() {
        HttpResponseEvent httpResponseEvent = mock(HttpResponseEvent.class);
        when(httpResponseEvent.getResponse()).thenReturn(mock(HttpResponse.class));
        when(httpResponseEvent.getTwitterException()).thenReturn(null);
        when(httpResponseEvent.isAuthenticated()).thenReturn(true);

        twitterBaseImpl.httpResponseReceived(httpResponseEvent);

        verify(rateLimitStatusListeners, never()).onRateLimitStatus(any(RateLimitStatusEvent.class));
        verify(rateLimitStatusListeners, never()).onRateLimitReached(any(RateLimitStatusEvent.class));
    }

    @Test
    public void testGetAuthorization() {
        assertSame(auth, twitterBaseImpl.getAuthorization());
    }

    @Test
    public void testGetConfiguration() {
        assertSame(conf, twitterBaseImpl.getConfiguration());
    }

    @Test
    public void testEnsureAuthorizationEnabled() {
        twitterBaseImpl.ensureAuthorizationEnabled();
        // no exception thrown
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureAuthorizationEnabledWithNoAuth() {
        twitterBaseImpl.ensureAuthorizationEnabled();
        fail("IllegalStateException expected");
    }

    @Test
    public void testEnsureOAuthEnabled() {
        twitterBaseImpl.ensureOAuthEnabled();
        // no exception thrown
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureOAuthEnabledWithNonOAuthAuth() {
        Configuration confOAuthDisabled = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .build();
        Configuration confOAuthEnabled = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("test-consumer-key")
                .setOAuthConsumerSecret("test-consumer-secret")
                .build();
        OAuthAuthorization oauthDisabled = mock(OAuthAuthorization.class);
        OAuthAuthorization oauthEnabled = mock(OAuthAuthorization.class);
        when(oauthDisabled.isEnabled()).thenReturn(false);
        when(oauthEnabled.isEnabled()).thenReturn(true);

        TwitterBaseImpl twitterBaseImplDisabled = new TwitterBaseImpl(confOAuthDisabled, oauthDisabled);
        TwitterBaseImpl twitterBaseImplEnabled = new TwitterBaseImpl(confOAuthEnabled, oauthEnabled);

        twitterBaseImplDisabled.ensureOAuthEnabled();
        twitterBaseImplEnabled.ensureOAuthEnabled();

        twitterBaseImplDisabled.ensureAuthorizationEnabled();
        twitterBaseImplEnabled.ensureAuthorizationEnabled();

        twitterBaseImplDisabled.setOAuthConsumer("test-consumer-key", "test-consumer-secret");
        twitterBaseImplEnabled.setOAuthConsumer("test-consumer-key", "test-consumer-secret");

        twitterBaseImplDisabled.getOAuthRequestToken();
        twitterBaseImplEnabled.getOAuthRequestToken();

        twitterBaseImplDisabled.getOAuthAccessToken();
        twitterBaseImplEnabled.getOAuthAccessToken();

        twitterBaseImplDisabled.getOAuthAccessToken(TwitterBaseImplTest.mockRateToken());
        twitterBaseImplEnabled.getOAuthAccessToken(TwitterBaseImplTest.mockRateToken());

        twitterBaseImplDisabled.getOAuthAccessToken(TwitterBaseImplTest.mockAccessToken());
        twitterBaseImplEnabled.getOAuthAccessToken(TwitterBaseImplTest.mockAccessToken());

        twitterBaseImplDisabled.getOAuthAccessToken(TwitterBaseImplTest.mockOAuthVerifier());
        twitterBaseImplEnabled.getOAuthAccessToken(TwitterBaseImplTest.mockOAuthVerifier());

        twitterBaseImplDisabled.getOAuthAccessToken(TwitterBaseImplTest.mockAccessToken(), TwitterBaseImplTest.mockOAuthVerifier());
        twitterBaseImplEnabled.getOAuthAccessToken(TwitterBaseImplTest.mockAccessToken(), TwitterBaseImplTest.mockOAuthVerifier());

        twitterBaseImplDisabled.setOAuthAccessToken(TwitterBaseImplTest.mockAccessToken());
        twitterBaseImplEnabled.setOAuthAccessToken(TwitterBaseImplTest.mockAccessToken());

        twitterBaseImplDisabled.getOAuthAccessToken(TwitterBaseImplTest.mockScreenName(), TwitterBaseImplTest.mockPassword());
        twitterBaseImplEnabled.getOAuthAccessToken(TwitterBaseImplTest.mockScreenName(), TwitterBaseImplTest.mockPassword());

        twitterBaseImplDisabled.getOAuth2Token();
        twitterBaseImplEnabled.getOAuth2Token();

        twitterBaseImplDisabled.setOAuth2Token(TwitterBaseImplTest.mockOAuth2Token());
        twitterBaseImplEnabled.setOAuth2Token(TwitterBaseImplTest.mockOAuth2Token());

        twitterBaseImplDisabled.invalidateOAuth2Token();
        twitterBaseImplEnabled.invalidateOAuth2Token();
    }

    private static OAuth2Token mockOAuth2Token() {
        return mock(OAuth2Token.class);
    }

    private static AccessToken mockAccessToken() {
        return mock(AccessToken.class);
    }

    private static RequestToken mockRateToken() {
        return mock(RequestToken.class);
    }

    private static OAuthVerifier mockOAuthVerifier() {
        return mock(OAuthVerifier.class);
    }

    private static String mockScreenName() {
        return "test-screen-name";
    }

    private static String mockPassword() {
        return "test-password";
    }
}