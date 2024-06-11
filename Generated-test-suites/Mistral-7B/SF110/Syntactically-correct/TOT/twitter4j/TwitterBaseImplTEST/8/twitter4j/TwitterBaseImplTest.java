package twitter4j;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import twitter4j.auth.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;

import java.io.*;
import java.util.*;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private Configuration configuration;
    private Authorization authorization;
    private HttpClient httpClient;
    private OAuthSupport oAuthSupport;

    @Before
    public void setUp() {
        configuration = new Configuration();
        authorization = Mockito.mock(Authorization.class);
        httpClient = Mockito.mock(HttpClient.class);
        oAuthSupport = Mockito.mock(OAuthSupport.class);

        twitterBaseImpl = new TwitterBaseImpl(configuration, authorization) {
            @Override
            public HttpClient getHttpClient() {
                return httpClient;
            }

            @Override
            public OAuthSupport getOAuth() {
                return oAuthSupport;
            }
        };
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        // BasicAuthorization
        Mockito.when(authorization.isEnabled()).thenReturn(true);
        Mockito.when(authorization.getUserId()).thenReturn("username1");
        Assert.assertEquals("username1", twitterBaseImpl.getScreenName());

        // OAuthAuthorization
        Mockito.when(authorization.isEnabled()).thenReturn(true);
        Mockito.when(authorization.getAccessToken()).thenReturn(new AccessToken("token1", "tokenSecret1"));
        Assert.assertEquals("token1", twitterBaseImpl.getScreenName());

        // NullAuthorization
        Mockito.when(authorization.isEnabled()).thenReturn(false);
        Assert.assertNull(twitterBaseImpl.getScreenName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWhenAuthorizationNotEnabled() throws TwitterException, IllegalStateException {
        Mockito.when(authorization.isEnabled()).thenReturn(false);
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        // BasicAuthorization
        Mockito.when(authorization.isEnabled()).thenReturn(true);
        Mockito.when(authorization.getUserId()).thenReturn("userId1");
        Assert.assertEquals(1L, twitterBaseImpl.getId());

        // OAuthAuthorization
        Mockito.when(authorization.isEnabled()).thenReturn(true);
        Mockito.when(authorization.getAccessToken()).thenReturn(new AccessToken("token1", "tokenSecret1"));
        Assert.assertEquals(1234567L, twitterBaseImpl.getId());

        // NullAuthorization
        Mockito.when(authorization.isEnabled()).thenReturn(false);
        Assert.assertNull(twitterBaseImpl.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWhenAuthorizationNotEnabled() throws TwitterException, IllegalStateException {
        Mockito.when(authorization.isEnabled()).thenReturn(false);
        twitterBaseImpl.getId();
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = Mockito.mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        Mockito.verify(listener).onRateLimitStatus(ArgumentMatchers.any(RateLimitStatusEvent.class));
        Mockito.verify(listener).onRateLimitReached(ArgumentMatchers.any(RateLimitStatusEvent.class));
    }

    @Test
    public void testOnRateLimitStatus() {
        RateLimitStatusEvent event = Mockito.mock(RateLimitStatusEvent.class);
        RateLimitStatusListener listener = Mockito.mock(RateLimitStatusListener.class);
        twitterBaseImpl.onRateLimitStatus(event);
        Mockito.verify(listener).onRateLimitStatus(event);
    }

    @Test
    public void testOnRateLimitReached() {
        RateLimitStatusEvent event = Mockito.mock(RateLimitStatusEvent.class);
        RateLimitStatusListener listener = Mockito.mock(RateLimitStatusListener.class);
        twitterBaseImpl.onRateLimitReached(event);
        Mockito.verify(listener).onRateLimitReached(event);
    }

    @Test
    public void testHttpResponseReceivedWithRateLimitStatus() {
        HttpResponseEvent httpResponseEvent = Mockito.mock(HttpResponseEvent.class);
        RateLimitStatusEvent rateLimitStatusEvent = Mockito.mock(RateLimitStatusEvent.class);
        RateLimitStatus rateLimitStatus = Mockito.mock(RateLimitStatus.class);
        Mockito.when(rateLimitStatusEvent.getRateLimitStatus()).thenReturn(rateLimitStatus);
        Mockito.when(rateLimitStatus.getRemaining()).thenReturn(1);
        Mockito.when(rateLimitStatus.getLimit()).thenReturn(100);

        twitterBaseImpl.httpResponseReceived(httpResponseEvent);
        Mockito.verify(rateLimitStatusListeners).size();
        Mockito.verify(rateLimitStatusListeners.get(0)).onRateLimitStatus(rateLimitStatusEvent);
        Mockito.verify(rateLimitStatusListeners.get(0)).onRateLimitReached(rateLimitStatusEvent);
    }

    @Test
    public void testHttpResponseReceivedWithNoRateLimitStatus() {
        HttpResponseEvent httpResponseEvent = Mockito.mock(HttpResponseEvent.class);
        RateLimitStatus rateLimitStatus = Mockito.mock(RateLimitStatus.class);
        Mockito.when(rateLimitStatus.getRemaining()).thenReturn(100);
        Mockito.when(rateLimitStatus.getLimit()).thenReturn(100);
        Mockito.when(httpResponseEvent.getRateLimitStatus()).thenReturn(rateLimitStatus);

        twitterBaseImpl.httpResponseReceived(httpResponseEvent);
        Mockito.verify(rateLimitStatusListeners).size();
        Mockito.verify(rateLimitStatusListeners.get(0)).onRateLimitStatus(ArgumentMatchers.any(RateLimitStatusEvent.class));
    }
}