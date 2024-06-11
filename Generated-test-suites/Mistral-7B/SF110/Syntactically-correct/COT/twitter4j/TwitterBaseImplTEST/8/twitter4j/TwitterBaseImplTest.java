package twitter4j; // replace with the actual package name if needed

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import twitter4j.auth.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;
import twitter4j.*;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private Configuration conf;
    private Authorization auth;

    @Before
    public void setup() {
        conf = new Configuration();
        auth = Mockito.mock(Authorization.class);
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        when(auth.isEnabled()).thenReturn(true);
        assertEquals("testScreenName", twitterBaseImpl.getScreenName());
        verify(auth).isEnabled();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenName_noAuthCredentials() throws TwitterException, IllegalStateException {
        when(auth.isEnabled()).thenReturn(false);
        twitterBaseImpl.getScreenName();
        verify(auth, times(1)).isEnabled();
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        when(auth.isEnabled()).thenReturn(true);
        assertEquals(1L, twitterBaseImpl.getId());
        verify(auth).isEnabled();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetId_noAuthCredentials() throws TwitterException, IllegalStateException {
        when(auth.isEnabled()).thenReturn(false);
        twitterBaseImpl.getId();
        verify(auth, times(1)).isEnabled();
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());
        verify(listener).onRateLimitStatus(any(RateLimitStatusEvent.class));
    }

    @Test
    public void testHttpResponseReceived_rateLimitReached() {
        HttpResponseEvent event = mock(HttpResponseEvent.class);
        RateLimitStatus eventRateLimitStatus = new RateLimitStatus(15, 17);
        RateLimitStatusEvent statusEvent = new RateLimitStatusEvent(twitterBaseImpl, eventRateLimitStatus, true);

        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.onRateLimitReached(new Consumer<RateLimitStatusEvent>() {
            public void accept(RateLimitStatusEvent event) {
                assertEquals(statusEvent, event);
                listener.onRateLimitStatus(event);
            }
        });

        twitterBaseImpl.httpResponseReceived(event);
        verify(listener).onRateLimitStatus(statusEvent);
        verify(listener).onRateLimitReached(statusEvent);
    }

    @Test
    public void testHttpResponseReceived_notRateLimitReached() {
        HttpResponseEvent event = mock(HttpResponseEvent.class);
        RateLimitStatus eventRateLimitStatus = null;
        RateLimitStatusEvent statusEvent = new RateLimitStatusEvent(twitterBaseImpl, eventRateLimitStatus, true);

        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.onRateLimitStatus(new Consumer<RateLimitStatusEvent>() {
            public void accept(RateLimitStatusEvent event) {
                assertEquals(statusEvent, event);
                listener.onRateLimitStatus(event);
            }
        });

        twitterBaseImpl.httpResponseReceived(event);
        verify(listener).onRateLimitStatus(statusEvent);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testSetOAuthConsumer() throws TwitterException {
        String consumerKey = "testConsumerKey";
        String consumerSecret = "testConsumerSecret";

        twitterBaseImpl.setOAuthConsumer(consumerKey, consumerSecret);
        assertEquals(consumerKey, twitterBaseImpl.getAuthorization().getConsumerKey());
        assertEquals(consumerSecret, twitterBaseImpl.getAuthorization().getConsumerSecret());

        assertEquals("TestConsumer", twitterBaseImpl.getScreenName());
        assertEquals(1L, twitterBaseImpl.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetOAuthConsumer_alreadySet() throws TwitterException {
        String consumerKey = "testConsumerKey";
        String consumerSecret = "testConsumerSecret";

        twitterBaseImpl.setOAuthConsumer(consumerKey, consumerSecret);
        twitterBaseImpl.setOAuthConsumer(consumerKey, consumerSecret);

        verify(auth, times(1)).setOAuthConsumer(consumerKey, consumerSecret);

        assertEquals("TestConsumer", twitterBaseImpl.getScreenName());
        assertEquals(1L, twitterBaseImpl.getId());
    }
}