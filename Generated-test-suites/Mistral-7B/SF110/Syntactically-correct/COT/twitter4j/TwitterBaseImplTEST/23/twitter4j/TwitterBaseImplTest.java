package twitter4j; // replace with your package name if necessary

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import twitter4j.Configuration;
import twitter4j.OAuthAuthorization;
import twitter4j.OAuthSupport;
import twitter4j.OAuth2Authorization;
import twitter4j.OAuth2Support;
import twitter4j.RateLimitStatusListener;
import twitter4j.RateLimitStatusEvent;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.BasicAuthorization;
import twitter4j.auth.XAuthAuthorization;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.HttpClient;
import twitter4j.http.HttpClientFactory;
import twitter4j.http.HttpResponse;
import twitter4j.http.HttpResponseEvent;
import twitter4j.util.function.Consumer;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private Configuration conf;
    private Authorization auth;
    private HttpClient http;
    private List<RateLimitStatusListener> rateLimitStatusListeners;

    @Before
    public void setup() {
        conf = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .setOAuthAccessToken("accessToken")
                .setOAuthAccessTokenSecret("accessTokenSecret")
                .build();

        auth = mock(Authorization.class);
        when(auth.isEnabled()).thenReturn(true);

        http = mock(HttpClient.class);

        rateLimitStatusListeners = new ArrayList<>();

        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        MockitoAnnotations.initMocks(this);
        twitterBaseImpl.init();
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        String expectedScreenName = "expectedScreenName";
        when(auth.getUserId()).thenReturn(expectedScreenName);
        String actualScreenName = twitterBaseImpl.getScreenName();
        assertEquals(expectedScreenName, actualScreenName);

        // Test when screenName is null
        twitterBaseImpl.setScreenName(null);
        assertNull(twitterBaseImpl.getScreenName());

        // Test when auth is disabled
        twitterBaseImpl.setAuth(null);
        try {
            twitterBaseImpl.getScreenName();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        long expectedId = 123;
        when(auth.getUserId()).thenReturn("userID");
        when(twitterBaseImpl.fillInIDAndScreenName(any(HttpParameter[].class))).thenReturn(new User());
        User user = twitterBaseImpl.fillInIDAndScreenName();
        long actualId = twitterBaseImpl.getId();
        assertEquals(expectedId, actualId);

        // Test when id is 0
        twitterBaseImpl.setId(0);
        assertNotNull(twitterBaseImpl.getId());
        assertEquals(0, twitterBaseImpl.getId().longValue());

        // Test when auth is disabled
        twitterBaseImpl.setAuth(null);
        try {
            twitterBaseImpl.getId();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        verify(rateLimitStatusListeners, atLeast(1)).add(listener);

        // Test when listener is already added
        twitterBaseImpl.addRateLimitStatusListener(listener);
        verify(rateLimitStatusListeners, times(1)).add(listener);
    }

    @Test
    public void testOnRateLimitStatus() {
        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
        RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
        twitterBaseImpl.onRateLimitStatus(action);
        verify(rateLimitStatusListeners, atLeast(1)).onRateLimitStatus(event);
        verify(rateLimitStatusListeners, times(1)).onRateLimitReached(event);

        // Test when status code is not a rate limit status
        HttpResponse response = mock(HttpResponse.class);
        when(response.getStatusCode()).thenReturn(200);
        HttpResponseEvent eventWithResponse = new HttpResponseEvent(http, response, null, null, null, null);
        twitterBaseImpl.httpResponseReceived(eventWithResponse);
        verifyNoMoreInteractions(rateLimitStatusListeners);
    }

    @Test(expected = NullPointerException.class)
    public void testSetOAuthConsumerWithNullConsumerKeyOrConsumerSecret() {
        twitterBaseImpl.setOAuthConsumer(null, null);
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        String callbackUrl = "callbackUrl";
        RequestToken expectedRequestToken = mock(RequestToken.class);
        when(twitterBaseImpl.getOAuth().getOAuthRequestToken(callbackUrl)).thenReturn(expectedRequestToken);
        RequestToken actualRequestToken = twitterBaseImpl.getOAuthRequestToken(callbackUrl);
        assertEquals(expectedRequestToken, actualRequestToken);
    }

    @Test
    public void testGetOAuthAccessTokenWithoutOAuthVerifier() throws TwitterException {
        AccessToken expectedAccessToken = mock(AccessToken.class);
        when(twitterBaseImpl.getOAuth().getOAuthAccessToken()).thenReturn(expectedAccessToken);
        AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken();
        assertEquals(expectedAccessToken, actualAccessToken);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOAuthAccessTokenWithNullOAuthVerifier() throws TwitterException {
        AccessToken expectedAccessToken = mock(AccessToken.class);
        when(twitterBaseImpl.getOAuth().getOAuthAccessToken(any(RequestToken.class))).thenReturn(expectedAccessToken);
        try {
            twitterBaseImpl.getOAuthAccessToken(null);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("OAuth verifier is missing", e.getMessage());
        }
    }

    @Test
    public void testGetOAuthAccessTokenWithOAuthVerifier() throws TwitterException {
        AccessToken expectedAccessToken = mock(AccessToken.class);
        RequestToken requestToken = mock(RequestToken.class);
        AccessToken oauthAccessToken = mock(AccessToken.class);
        when(twitterBaseImpl.getOAuth().getOAuthAccessToken(requestToken)).thenReturn(oauthAccessToken);
        when(oauthAccessToken.getScreenName()).thenReturn("screenName");
        AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken(requestToken);
        assertEquals(expectedAccessToken, actualAccessToken);
        assertEquals("screenName", actualAccessToken.getScreenName());
    }

    @Test
    public void testSetOAuthAccessToken() {
        AccessToken expectedAccessToken = mock(AccessToken.class);
        twitterBaseImpl.setOAuthAccessToken(expectedAccessToken);
        assertEquals(expectedAccessToken, twitterBaseImpl.getOAuthAccessToken());
    }

    @Test
    public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
        AccessToken expectedAccessToken = mock(AccessToken.class);
        when(twitterBaseImpl.getOAuth().getOAuthAccessToken("screenName", "password")).thenReturn(expectedAccessToken);
        AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken("screenName", "password");
        assertEquals(expectedAccessToken, actualAccessToken);
    }

    @Test
    public void testGetOAuthAccessTokenWithXAuthAccessType() throws TwitterException {
        AccessToken expectedAccessToken = mock(AccessToken.class);
        when(twitterBaseImpl.getOAuth().getOAuthAccessToken("screenName", "password", "xAuthAccessType")).thenReturn(expectedAccessToken);
        AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken("screenName", "password", "xAuthAccessType");
        assertEquals(expectedAccessToken, actualAccessToken);
    }

    @Test
    public void testGetOAuthAccessTokenWithXAuthAccessTypeAndXAuthMode() throws TwitterException {
        AccessToken expectedAccessToken = mock(AccessToken.class);
        when(twitterBaseImpl.getOAuth().getOAuthAccessToken("screenName", "password", "xAuthAccessType", "xAuthMode")).thenReturn(expectedAccessToken);
        AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken("screenName", "password", "xAuthAccessType", "xAuthMode");
        assertEquals(expectedAccessToken, actualAccessToken);
    }
}