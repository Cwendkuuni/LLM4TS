package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import twitter4j.Configuration;
import twitter4j.OAuthAccessToken;
import twitter4j.OAuthAuthorization;
import twitter4j.OAuthConsumer;
import twitter4j.OAuthRequestToken;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.BasicAuthorization;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.XAuthAuthorization;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.HttpClientFactory;
import twitter4j.http.HttpResponseEvent;
import twitter4j.http.HttpResponseListener;

public class TwitterBaseImplTest {

    private Configuration conf;
    private Authorization auth;
    private TwitterBaseImpl twitterBaseImpl;
    private HttpClient http;
    private JSONImplFactory factory;
    private List<RateLimitStatusListener> rateLimitStatusListeners;

    @Before
    public void setup() {
        conf = new ConfigurationBuilder().build();
        auth = mock(Authorization.class);
        http = mock(HttpClient.class);
        factory = mock(JSONImplFactory.class);
        rateLimitStatusListeners = new ArrayList<>(2);
        twitterBaseImpl = new TwitterBaseImpl(conf, auth) {
            @Override
            HttpClient getHttpClient() {
                return http;
            }

            @Override
            JSONImplFactory getFactory() {
                return factory;
            }

            @Override
            public void addRateLimitStatusListener(RateLimitStatusListener listener) {
                rateLimitStatusListeners.add(listener);
            }
        };
        twitterBaseImpl.init();
        twitterBaseImpl.setFactory();
    }

    @Test
    public void testBasicConstructor() {
        assertNull(twitterBaseImpl.getScreenName());
        assertNull(twitterBaseImpl.getId());
        assertEquals(conf, twitterBaseImpl.getConfiguration());
        assertEquals(auth, twitterBaseImpl.getAuthorization());
    }

    @Test
    public void testOAuthConsumerConstructor() {
        OAuthConsumer consumer = new OAuthConsumer("consumerKey", "consumerSecret");
        when(auth.isEnabled()).thenReturn(true);
        when(auth.isApplicationOnlyAuthEnabled()).thenReturn(false);
        when(auth.getInstance()).thenReturn(consumer);

        assertNull(twitterBaseImpl.getScreenName());
        assertNull(twitterBaseImpl.getId());
        assertEquals(conf, twitterBaseImpl.getConfiguration());
        assertEquals(consumer, twitterBaseImpl.getAuthorization());

        try {
            twitterBaseImpl.getScreenName();
            fail("Expected IllegalStateException when neither user ID/password combination nor OAuth consumer key/secret combination supplied.");
        } catch (IllegalStateException e) {
            // Expected
        }

        when(auth.getInstance()).thenReturn(null);
        assertNull(twitterBaseImpl.getAuthorization());
    }

    @Test
    public void testOAuth2ConsumerConstructor() {
        OAuth2Authorization oauth2 = new OAuth2Authorization(conf);
        when(auth.isEnabled()).thenReturn(true);
        when(auth.isApplicationOnlyAuthEnabled()).thenReturn(false);
        when(auth.getInstance()).thenReturn(oauth2);

        assertNull(twitterBaseImpl.getScreenName());
        assertNull(twitterBaseImpl.getId());
        assertEquals(conf, twitterBaseImpl.getConfiguration());
        assertEquals(oauth2, twitterBaseImpl.getAuthorization());

        try {
            twitterBaseImpl.getScreenName();
            fail("Expected IllegalStateException when neither user ID/password combination nor OAuth consumer key/secret combination supplied.");
        } catch (IllegalStateException e) {
            // Expected
        }

        when(auth.getInstance()).thenReturn(null);
        assertNull(twitterBaseImpl.getAuthorization());
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        User user = mock(User.class);
        when(user.getScreenName()).thenReturn("testUser");
        when(user.getId()).thenReturn(1L);

        when(auth.isEnabled()).thenReturn(true);
        when(auth.getInstance()).thenReturn(new BasicAuthorization("testUserId", "testPassword"));
        when(auth.getUserId()).thenReturn("testUserId");
        when(this.factory.getObject("UserJSONImpl", user)).thenReturn(user);
        when(http.get(anyString(), any(HttpRequest.class), eq(auth), any(HttpResponseListener.class))).thenReturn(new HttpResponseEvent());

        assertEquals("testUser", twitterBaseImpl.getScreenName());
        assertEquals(1L, twitterBaseImpl.getId());
    }

    @Test
    public void testGetScreenNameWhenAuthIsNull() throws TwitterException, IllegalStateException {
        assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getScreenName());
    }

    @Test
    public void testGetScreenNameWhenAuthorizationIsDisabled() throws TwitterException, IllegalStateException {
        when(auth.isEnabled()).thenReturn(false);

        try {
            twitterBaseImpl.getScreenName();
            fail("Expected IllegalStateException when neither user ID/password combination nor OAuth consumer key/secret combination supplied.");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());
    }

    @Test
    public void testOnRateLimitStatus() {
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);

        RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
        twitterBaseImpl.httpResponseReceived(new HttpResponseEvent());

        verify(listener).onRateLimitStatus(event);
    }

    @Test
    public void testOnRateLimitReached() {
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);

        RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
        twitterBaseImpl.httpResponseReceived(new HttpResponseEvent());

        verify(listener).onRateLimitReached(event);
    }

    @Test
    public void testGetConfiguration() {
        assertEquals(conf, twitterBaseImpl.getConfiguration());
    }

    @Test
    public void testGetAuthorization() {
        assertEquals(auth, twitterBaseImpl.getAuthorization());
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        OAuthAuthorization oauth = mock(OAuthAuthorization.class);
        when(auth.getInstance()).thenReturn(oauth);

        OAuthRequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        assertEquals(oauth, requestToken.getAuthorization());
    }

    @Test
    public void testGetOAuthRequestTokenWithCallbackUrl() throws TwitterException {
        OAuthAuthorization oauth = mock(OAuthAuthorization.class);
        when(auth.getInstance()).thenReturn(oauth);

        String callbackUrl = "http://test.com";
        OAuthRequestToken requestToken = twitterBaseImpl.getOAuthRequestToken(callbackUrl);
        assertEquals(oauth, requestToken.getAuthorization());
        assertEquals(callbackUrl, requestToken.getCallbackUrl());
    }

    @Test
    public void testGetOAuthRequestTokenWithXAuthAccessType() throws TwitterException {
        OAuthAuthorization oauth = mock(OAuthAuthorization.class);
        when(auth.getInstance()).thenReturn(oauth);

        String xAuthAccessType = "read";
        OAuthRequestToken requestToken = twitterBaseImpl.getOAuthRequestToken("http://test.com", xAuthAccessType);
        assertEquals(oauth, requestToken.getAuthorization());
        assertEquals("http://test.com", requestToken.getCallbackUrl());
        assertEquals(xAuthAccessType, requestToken.getXAuthAccessType());
    }

    @Test
    public void testGetOAuthRequestTokenWithXAuthAccessTypeAndXAuthMode() throws TwitterException {
        OAuthAuthorization oauth = mock(OAuthAuthorization.class);
        when(auth.getInstance()).thenReturn(oauth);

        String xAuthAccessType = "read";
        String xAuthMode = "client_side";
        OAuthRequestToken requestToken = twitterBaseImpl.getOAuthRequestToken("http://test.com", xAuthAccessType, xAuthMode);
        assertEquals(oauth, requestToken.getAuthorization());
        assertEquals("http://test.com", requestToken.getCallbackUrl());
        assertEquals(xAuthAccessType, requestToken.getXAuthAccessType());
        assertEquals(xAuthMode, requestToken.getXAuthMode());
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        OAuthAuthorization oauth = mock(OAuthAuthorization.class);
        when(auth.getInstance()).thenReturn(oauth);

        OAuthAccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
        assertEquals(oauth, accessToken.getAuthorization());
        assertNotNull(accessToken.getScreenName());
        assertNotNull(accessToken.getId());
    }

    @Test
    public void testGetOAuthAccessTokenWithOAuthVerifier() throws TwitterException {
        OAuthAuthorization oauth = mock(OAuthAuthorization.class);
        when(auth.getInstance()).thenReturn(oauth);

        AccessToken accessToken = mock(AccessToken.class);
        when(oauth.getOAuthAccessToken(anyString(), anyString())).thenReturn(accessToken);

        OAuthAccessToken accessTokenFromTwitterBaseImpl = twitterBaseImpl.getOAuthAccessToken("oauth_token", "oauth_token_secret");
        assertEquals(accessToken, accessTokenFromTwitterBaseImpl);
    }

    @Test
    public void testGetOAuthAccessTokenWithRequestToken() throws TwitterException {
        OAuthAuthorization oauth = mock(OAuthAuthorization.class);
        when(auth.getInstance()).thenReturn(oauth);

        RequestToken requestToken = mock(RequestToken.class);
        when(oauth.getOAuthAccessToken(requestToken)).thenReturn(mock(AccessToken.class));

        AccessToken accessTokenFromTwitterBaseImpl = twitterBaseImpl.getOAuthAccessToken(requestToken);
        assertNotNull(accessTokenFromTwitterBaseImpl);
    }

    @Test
    public void testGetOAuthAccessTokenWithRequestTokenAndOAuthVerifier() throws TwitterException {
        OAuthAuthorization oauth = mock(OAuthAuthorization.class);
        when(auth.getInstance()).thenReturn(oauth);

        RequestToken requestToken = mock(RequestToken.class);
        AccessToken accessToken = mock(AccessToken.class);
        when(oauth.getOAuthAccessToken(requestToken, "oauth_verifier")).thenReturn(accessToken);

        AccessToken accessTokenFromTwitterBaseImpl = twitterBaseImpl.getOAuthAccessToken(requestToken, "oauth_verifier");
        assertEquals(accessToken, accessTokenFromTwitterBaseImpl);
    }

    @Test
    public void testSetOAuthAccessToken() throws TwitterException {
        AccessToken accessToken = mock(AccessToken.class);
        twitterBaseImpl.setOAuthAccessToken(accessToken);
        assertEquals(accessToken, twitterBaseImpl.getOAuthAccessToken());
    }

    @Test
    public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
        OAuthAuthorization oauth = mock(OAuthAuthorization.class);
        when(auth.getInstance()).thenReturn(oauth);
        when(auth.getUserId()).thenReturn("testUserId");

        AccessToken accessToken = mock(AccessToken.class);
        when(oauth.getOAuthAccessToken(eq("testUserId"), eq("testPassword"))).thenReturn(accessToken);

        AccessToken accessTokenFromTwitterBaseImpl = twitterBaseImpl.getOAuthAccessToken("testUserId", "testPassword");
        assertEquals(accessToken, accessTokenFromTwitterBaseImpl);
    }

    @Test
    public void testGetOAuth2Token() throws TwitterException {
        OAuth2Authorization oauth2 = mock(OAuth2Authorization.class);
        when(auth.getInstance()).thenReturn(oauth2);

        OAuth2Token oauth2Token = twitterBaseImpl.getOAuth2Token();
        assertEquals(oauth2, oauth2Token.getAuthorization());
        assertNotNull(oauth2Token.getAccessToken());
        assertNotNull(oauth2Token.getTokenType());
        assertNotNull(oauth2Token.getRefreshToken());
        assertNotNull(oauth2Token.getRefreshTokenSecret());
    }

    @Test
    public void testSetOAuth2Token() throws TwitterException {
        OAuth2Token oauth2Token = new OAuth2Token("token_type", "access_token", "refresh_token", "refresh_token_secret");
        twitterBaseImpl.setOAuth2Token(oauth2Token);
        assertEquals(oauth2Token, twitterBaseImpl.getOAuth2Token());
    }

    @Test
    public void testInvalidateOAuth2Token() throws TwitterException {
        OAuth2Authorization oauth2 = mock(OAuth2Authorization.class);
        when(auth.getInstance()).thenReturn(oauth2);
        when(oauth2.isValidToken()).thenReturn(true);
        when(oauth2.invalidateToken()).thenReturn(true);

        twitterBaseImpl.invalidateOAuth2Token();
        verify(oauth2).invalidateToken();

        when(oauth2.isValidToken()).thenReturn(false);
        twitterBaseImpl.invalidateOAuth2Token();
        // No-op
    }

    @Test
    public void testEquals() {
        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf, auth);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);

        assertEquals(twitterBaseImpl1, twitterBaseImpl2);
    }

    @Test
    public void testHashCode() {
        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf, auth);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);

        assertEquals(twitterBaseImpl1.hashCode(), twitterBaseImpl2.hashCode());
    }

    @Test
    public void testToString() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        String expected = "TwitterBase{conf=" + conf.toString() + ", http=" + http.toString() + ", rateLimitStatusListeners=" + rateLimitStatusListeners.toString() + ", auth=" + auth.toString() + '}';
        assertEquals(expected, twitterBaseImpl.toString());
    }
}