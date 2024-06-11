package twitter4j.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.HttpClient;
import twitter4j.http.HttpResponse;
import twitter4j.util.function.RateLimitStatusListener;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private Configuration configuration;
    private Authorization authorization;
    private HttpClient httpClient;
    private OAuthSupport oAuthSupport;
    private RateLimitStatusListener rateLimitStatusListener;

    @Before
    public void setup() {
        configuration = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("testConsumerKey")
                .setOAuthConsumerSecret("testConsumerSecret")
                .build();

        authorization = Mockito.mock(Authorization.class);
        httpClient = Mockito.mock(HttpClient.class);
        oAuthSupport = Mockito.mock(OAuthSupport.class);
        twitterBaseImpl = new TwitterBaseImpl(configuration, authorization);
        twitterBaseImpl.init();
        twitterBaseImpl.setFactory();
        rateLimitStatusListener = Mockito.mock(RateLimitStatusListener.class);
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        when(authorization.isEnabled()).thenReturn(true);
        when(authorization.getInstance(OAuthAuthorization.class)).thenReturn(oAuthSupport);
        when(oAuthSupport.getOAuthAccessToken(anyString(), anyString())).thenReturn(new AccessToken("testScreenName", 1L, "testAccessToken", "testAccessTokenSecret"));

        assertEquals("testScreenName", twitterBaseImpl.getScreenName());

        verify(authorization).isEnabled();
        verify(oAuthSupport).getOAuthAccessToken(anyString(), anyString());
    }

    @Test
    public void testGetScreenNameWithoutAuthorization() throws TwitterException, IllegalStateException {
        when(authorization.isEnabled()).thenReturn(false);

        try {
            twitterBaseImpl.getScreenName();
            fail("Exception expected");
        } catch (IllegalStateException e) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }

        verify(authorization, never()).getInstance(OAuthAuthorization.class);
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        when(authorization.isEnabled()).thenReturn(true);
        when(authorization.getInstance(OAuthAuthorization.class)).thenReturn(oAuthSupport);
        when(oAuthSupport.getOAuthAccessToken(anyString(), anyString())).thenReturn(new AccessToken("testScreenName", 1L, "testAccessToken", "testAccessTokenSecret"));

        assertEquals(1L, twitterBaseImpl.getId());

        verify(authorization).isEnabled();
        verify(oAuthSupport).getOAuthAccessToken(anyString(), anyString());
    }

    @Test
    public void testGetIdWithoutAuthorization() throws TwitterException, IllegalStateException {
        when(authorization.isEnabled()).thenReturn(false);

        try {
            twitterBaseImpl.getId();
            fail("Exception expected");
        } catch (IllegalStateException e) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }

        verify(authorization, never()).getInstance(OAuthAuthorization.class);
    }

    @Test
    public void testAddRateLimitStatusListener() {
        twitterBaseImpl.addRateLimitStatusListener(rateLimitStatusListener);

        verify(twitterBaseImpl).rateLimitStatusListeners.add(rateLimitStatusListener);
    }

    @Test
    public void testOnRateLimitStatus() {
        when(authorization.isEnabled()).thenReturn(true);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponseListener.class))).thenAnswer(i -> null);

        twitterBaseImpl.addRateLimitStatusListener(rateLimitStatusListener);

        ArgumentCaptor<RateLimitStatusEvent> statusEventCaptor = ArgumentCaptor.forClass(RateLimitStatusEvent.class);

        twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(new HttpResponse(200, ""), null, true));

        twitterBaseImpl.onRateLimitStatus(statusEventCaptor.capture());

        RateLimitStatusEvent statusEvent = statusEventCaptor.getValue();
        assertEquals(twitterBaseImpl, statusEvent.getSource());
        assertNotNull(statusEvent.getRateLimitStatus());
        assertEquals(twitterBaseImpl, statusEvent.getSource());
        assertTrue(statusEvent.isAuthenticated());

        verify(rateLimitStatusListener).onRateLimitStatus(statusEvent);
    }

    @Test
    public void testOnRateLimitReached() {
        when(authorization.isEnabled()).thenReturn(true);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponseListener.class))).thenAnswer(i -> null);

        twitterBaseImpl.addRateLimitStatusListener(rateLimitStatusListener);

        ArgumentCaptor<RateLimitStatusEvent> statusEventCaptor = ArgumentCaptor.forClass(RateLimitStatusEvent.class);

        twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(new HttpResponse(429, ""), null, true));

        twitterBaseImpl.onRateLimitReached(statusEventCaptor.capture());

        RateLimitStatusEvent statusEvent = statusEventCaptor.getValue();
        assertEquals(twitterBaseImpl, statusEvent.getSource());
        assertNotNull(statusEvent.getRateLimitStatus());
        assertEquals(twitterBaseImpl, statusEvent.getSource());
        assertTrue(statusEvent.isAuthenticated());

        verify(rateLimitStatusListener).onRateLimitReached(statusEvent);
    }

    @Test
    public void testHttpResponseReceived() {
        when(authorization.isEnabled()).thenReturn(true);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponseListener.class))).thenAnswer(i -> null);

        twitterBaseImpl.addRateLimitStatusListener(rateLimitStatusListener);

        ArgumentCaptor<RateLimitStatusEvent> statusEventCaptor = ArgumentCaptor.forClass(RateLimitStatusEvent.class);

        HttpResponseEvent httpResponseEvent = new HttpResponseEvent(new HttpResponse(200, ""), null, true);
        twitterBaseImpl.httpResponseReceived(httpResponseEvent);

        twitterBaseImpl.onRateLimitStatus(statusEventCaptor.capture());

        RateLimitStatusEvent statusEvent = statusEventCaptor.getValue();
        assertEquals(twitterBaseImpl, statusEvent.getSource());
        assertNotNull(statusEvent.getRateLimitStatus());
        assertEquals(twitterBaseImpl, statusEvent.getSource());
        assertTrue(statusEvent.isAuthenticated());

        verifyZeroInteractions(httpClient);

        twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(new HttpResponse(429, ""), null, true));

        twitterBaseImpl.onRateLimitReached(statusEventCaptor.capture());

        RateLimitStatusEvent statusEvent2 = statusEventCaptor.getValue();
        assertEquals(twitterBaseImpl, statusEvent2.getSource());
        assertNotNull(statusEvent2.getRateLimitStatus());
        assertEquals(twitterBaseImpl, statusEvent2.getSource());
        assertTrue(statusEvent2.isAuthenticated());

        verifyZeroInteractions(httpClient);
    }
}