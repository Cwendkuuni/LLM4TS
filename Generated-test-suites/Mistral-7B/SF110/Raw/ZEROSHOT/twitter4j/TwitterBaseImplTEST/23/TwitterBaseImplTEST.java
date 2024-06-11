import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.ConfigurationImpl;
import twitter4j.util.function.Consumer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private ConfigurationImpl config;
    private OAuthAuthorization oAuthAuthorization;
    private OAuth2Authorization oAuth2Authorization;

    @Before
    public void setup() {
        config = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();

        oAuthAuthorization = new OAuthAuthorization(config);
        oAuth2Authorization = new OAuth2Authorization(config);

        twitterBaseImpl = new TwitterBaseImpl(config, oAuthAuthorization);
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        assertEquals("consumerKey", twitterBaseImpl.getScreenName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithNoCredentials() throws TwitterException, IllegalStateException {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(new ConfigurationImpl(configurationBuilder.build()), null);
        twitterBaseImpl1.getScreenName();
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        assertEquals(1L, twitterBaseImpl.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithNoCredentials() throws TwitterException, IllegalStateException {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(new ConfigurationImpl(configurationBuilder.build()), null);
        twitterBaseImpl1.getId();
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        User user = twitterBaseImpl.fillInIDAndScreenName();
        assertEquals("consumerKey", user.getScreenName());
        assertEquals(1L, user.getId());
    }

    @Test(expected = TwitterException.class)
    public void testFillInIDAndScreenNameWithHttpError() throws TwitterException {
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(new ConfigurationImpl(new ConfigurationBuilder().build()), null);
        twitterBaseImpl1.setHttp(httpClient);
        Mockito.when(httpClient.get(Mockito.anyString(), Mockito.any(HttpParameter[].class), Mockito.eq(null), Mockito.eq(null)))
                .thenReturn(new HttpResponse(404, ""));
        twitterBaseImpl1.fillInIDAndScreenName();
    }

    @Test
    public void testAddRateLimitStatusListener() {
        twitterBaseImpl.addRateLimitStatusListener((Consumer<RateLimitStatusEvent>) Mockito.mock(Consumer.class));
        List<RateLimitStatusListener> rateLimitStatusListeners = twitterBaseImpl.getRateLimitStatusListeners();
        assertEquals(1, rateLimitStatusListeners.size());
    }

    @Test
    public void testOnRateLimitStatus() {
        Consumer<RateLimitStatusEvent> consumer = Mockito.mock(Consumer.class);
        twitterBaseImpl.onRateLimitStatus((Consumer<RateLimitStatusEvent>) consumer);
        ArgumentCaptor<RateLimitStatusListener> rateLimitStatusListenerArgumentCaptor = ArgumentCaptor.forClass(RateLimitStatusListener.class);
        verify(consumer).accept(Mockito.capture(rateLimitStatusListenerArgumentCaptor));
        RateLimitStatusListener rateLimitStatusListener = rateLimitStatusListenerArgumentCaptor.getValue();
        assertEquals(twitterBaseImpl, rateLimitStatusListener.getSource());
    }

    @Test
    public void testOnRateLimitReached() {
        Consumer<RateLimitStatusEvent> consumer = Mockito.mock(Consumer.class);
        twitterBaseImpl.onRateLimitReached((Consumer<RateLimitStatusEvent>) consumer);
        ArgumentCaptor<RateLimitStatusListener> rateLimitStatusListenerArgumentCaptor = ArgumentCaptor.forClass(RateLimitStatusListener.class);
        verify(consumer, Mockito.never()).accept(Mockito.any(RateLimitStatusEvent.class));
    }

    @Test
    public void testHttpResponseReceived() {
        HttpResponseEvent httpResponseEvent = Mockito.mock(HttpResponseEvent.class);
        RateLimitStatus rateLimitStatus = Mockito.mock(RateLimitStatus.class);
        Mockito.when(httpResponseEvent.getResponse()).thenReturn(new HttpResponse(200, null));
        Mockito.when(httpResponseEvent.isAuthenticated()).thenReturn(true);
        Mockito.when(httpResponseEvent.getTwitterException()).thenReturn(null);
        Mockito.when(httpResponseEvent.getRateLimitStatus()).thenReturn(rateLimitStatus);
        twitterBaseImpl.httpResponseReceived(httpResponseEvent);
        ArgumentCaptor<RateLimitStatusEvent> rateLimitStatusEventArgumentCaptor = ArgumentCaptor.forClass(RateLimitStatusEvent.class);
        verify(twitterBaseImpl.getRateLimitStatusListeners()).forEach(rateLimitStatusListener -> {
            rateLimitStatusListener.onRateLimitStatus(rateLimitStatusEventArgumentCaptor.capture());
            rateLimitStatusListener.onRateLimitReached(rateLimitStatusEventArgumentCaptor.getValue());
        });
        RateLimitStatusEvent rateLimitStatusEvent = rateLimitStatusEventArgumentCaptor.getValue();
        assertEquals(twitterBaseImpl, rateLimitStatusEvent.getSource());
        assertEquals(rateLimitStatus, rateLimitStatusEvent.getRateLimitStatus());
    }

    @Test
    public void testSetOAuthConsumer() {
        twitterBaseImpl.setOAuthConsumer("consumerKey2", "consumerSecret2");
        assertEquals("consumerKey2", twitterBaseImpl.getAuthorization().getConsumerKey());
        assertEquals("consumerSecret2", twitterBaseImpl.getAuthorization().getConsumerSecret());
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        assertEquals("oauth_token", requestToken.getToken());
        assertEquals("oauth_token_secret", requestToken.getTokenSecret());
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
        assertEquals("accessToken", accessToken.getToken());
        assertEquals("accessTokenSecret", accessToken.getTokenSecret());
        assertEquals("consumerKey", accessToken.getScreenName());
        assertEquals(1L, accessToken.getUserId());
    }

    @Test
    public void testGetOAuthAccessTokenWithOAuthToken() throws TwitterException {
        RequestToken requestToken = Mockito.mock(RequestToken.class);
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken);
        assertEquals("accessToken", accessToken.getToken());
        assertEquals("accessTokenSecret", accessToken.getTokenSecret());
        assertEquals("consumerKey", accessToken.getScreenName());
        assertEquals(1L, accessToken.getUserId());
    }

    @Test
    public void testGetOAuthAccessTokenWithOAuthVerifier() throws TwitterException {
        RequestToken requestToken = Mockito.mock(RequestToken.class);
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, "oauth_verifier");
        assertEquals("accessToken", accessToken.getToken());
        assertEquals("accessTokenSecret", accessToken.getTokenSecret());
        assertEquals("consumerKey", accessToken.getScreenName());
        assertEquals(1L, accessToken.getUserId());
    }

    @Test
    public void testSetOAuthAccessToken() throws TwitterException {
        AccessToken accessToken = Mockito.mock(AccessToken.class);
        twitterBaseImpl.setOAuthAccessToken(accessToken);
        assertEquals(accessToken, twitterBaseImpl.getAuthorization().getAccessToken());
    }

    @Test
    public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("screenName", "password");
        assertEquals("screenName", accessToken.getScreenName());
        assertEquals(1L, accessToken.getUserId());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOAuthAccessTokenWithNoCredentials() throws TwitterException {
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
    }

    @Test
    public void testGetOAuth2Token() throws TwitterException {
        OAuth2Token oAuth2Token = twitterBaseImpl.getOAuth2Token();
        assertEquals("access_token", oAuth2Token.getTokenType());
        assertEquals("access_token", oAuth2Token.getAccessToken());
    }

    @Test
    public void testSetOAuth2Token() throws TwitterException {
        OAuth2Token oAuth2Token = Mockito.mock(OAuth2Token.class);
        twitterBaseImpl.setOAuth2Token(oAuth2Token);
        assertEquals(oAuth2Token, twitterBaseImpl.getAuthorization().getOAuth2Token());
    }

    @Test
    public void testInvalidateOAuth2Token() throws TwitterException {
        twitterBaseImpl.invalidateOAuth2Token();
        assertNull(twitterBaseImpl.getAuthorization().getOAuth2Token());
    }

    @Test
    public void testEquals() {
        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(new ConfigurationImpl(new ConfigurationBuilder().build()), null);
        assertFalse(twitterBaseImpl.equals(null));
        assertFalse(twitterBaseImpl.equals(""));
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(new ConfigurationImpl(new ConfigurationBuilder().build()), null);
        assertFalse(twitterBaseImpl.equals(twitterBaseImpl2));
        TwitterBaseImpl twitterBaseImpl3 = new TwitterBaseImpl(new ConfigurationImpl(new ConfigurationBuilder().build()), null);
        twitterBaseImpl3.setAuthorization(twitterBaseImpl.getAuthorization());
        assertFalse(twitterBaseImpl.equals(twitterBaseImpl3));
        TwitterBaseImpl twitterBaseImpl4 = new TwitterBaseImpl(new ConfigurationImpl(new ConfigurationBuilder().build()), oAuthAuthorization);
        assertTrue(twitterBaseImpl.equals(twitterBaseImpl4));
    }

    @Test
    public void testHashCode() {
        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(new ConfigurationImpl(new ConfigurationBuilder().build()), null);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(new ConfigurationImpl(new ConfigurationBuilder().build()), null);
        assertEquals(twitterBaseImpl1.hashCode(), twitterBaseImpl2.hashCode());
        TwitterBaseImpl twitterBaseImpl3 = new TwitterBaseImpl(new ConfigurationImpl(new ConfigurationBuilder().build()), oAuthAuthorization);
        assertNotEquals(twitterBaseImpl1.hashCode(), twitterBaseImpl3.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("TwitterBase{conf=Configuration{...}, http=null, rateLimitStatusListeners=[], auth=OAuthAuthorization{...}}", twitterBaseImpl.toString());
    }
}