package twitter4j;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import twitter4j.conf.*;
import twitter4j.auth.*;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;

public class TwitterBaseImplTest {

    private Configuration conf;
    private Authorization auth;
    private TwitterBaseImpl twitterBaseImpl;

    @Before
    public void setup() {
        conf = new ConfigurationBuilder()
                .setDebugEnabled(false)
                .setOAuthConsumerKey("testConsumerKey")
                .setOAuthConsumerSecret("testConsumerSecret")
                .setOAuthAccessToken("testAccessToken")
                .setOAuthAccessTokenSecret("testAccessTokenSecret")
                .build();

        auth = mock(Authorization.class);
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
    }

    @Test
    public void testGetScreenName() {
        when(auth.isEnabled()).thenReturn(true);
        assertEquals("testUserName", twitterBaseImpl.getScreenName());

        when(auth.isEnabled()).thenReturn(false);
        assertNull(twitterBaseImpl.getScreenName());

        twitterBaseImpl.fillInIDAndScreenName();
        assertEquals("testUserName", twitterBaseImpl.getScreenName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameNoAuth() {
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() {
        when(auth.isEnabled()).thenReturn(true);
        assertEquals(123L, twitterBaseImpl.getId());

        when(auth.isEnabled()).thenReturn(false);
        assertNull(twitterBaseImpl.getId());

        twitterBaseImpl.fillInIDAndScreenName();
        assertEquals(123L, twitterBaseImpl.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdNoAuth() {
        twitterBaseImpl.getId();
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        User user = twitterBaseImpl.fillInIDAndScreenName();
        assertEquals("testUserName", user.getScreenName());
        assertEquals(123L, user.getId());
    }

    @Test
    public void testFillInIDAndScreenNameWithParameters() throws TwitterException {
        HttpParameter[] parameters = new HttpParameter[0];
        User user = twitterBaseImpl.fillInIDAndScreenName(parameters);
        assertEquals("testUserName", user.getScreenName());
        assertEquals(123L, user.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        verify(listener, times(1)).onRateLimitStatus(any(RateLimitStatusEvent.class));
    }

    @Test
    public void testHttpResponseReceived() {
        HttpResponseEvent event = new HttpResponseEvent(null, null, 200, null, null, null);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        when(event.getTwitterException()).thenReturn(null);
        when(event.isAuthenticated()).thenReturn(true);
        when(JSONImplFactory.createRateLimitStatusFromResponseHeader(event.getResponse())).thenReturn(rateLimitStatus);

        twitterBaseImpl.httpResponseReceived(event);
        verify(rateLimitStatusListeners, times(1)).onRateLimitStatus(any(RateLimitStatusEvent.class));
    }

    @Test
    public void testHttpResponseReceivedWithRateLimitReached() {
        HttpResponseEvent event = new HttpResponseEvent(null, null, 429, null, null, null);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        when(event.getTwitterException()).thenReturn(null);
        when(event.isAuthenticated()).thenReturn(true);
        when(JSONImplFactory.createRateLimitStatusFromResponseHeader(event.getResponse())).thenReturn(rateLimitStatus);

        twitterBaseImpl.httpResponseReceived(event);
        verify(rateLimitStatusListeners, times(2)).onRateLimitStatus(any(RateLimitStatusEvent.class));
        verify(rateLimitStatusListeners, times(2)).onRateLimitReached(any(RateLimitStatusEvent.class));
    }

    @Test
    public void testHttpResponseReceivedWithoutAuthentication() {
        HttpResponseEvent event = new HttpResponseEvent(null, null, 200, null, null, null);
        twitterBaseImpl.httpResponseReceived(event);
        verify(rateLimitStatusListeners, never()).onRateLimitStatus(any(RateLimitStatusEvent.class));
    }
}