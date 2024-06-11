package twitter4j;

import static org.junit.Assert.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import org.junit.Test;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        try {
            assertEquals(null, twitterBaseImpl.getScreenName());
            twitterBaseImpl.init();
            assertNull(twitterBaseImpl.getScreenName());
            twitterBaseImpl.fillInIDAndScreenName();
            assertEquals("screenName", twitterBaseImpl.getScreenName());
        } catch (TwitterException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        } catch (IllegalStateException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetId() {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        try {
            assertEquals(0, twitterBaseImpl.getId());
            twitterBaseImpl.init();
            assertEquals(0, twitterBaseImpl.getId());
            twitterBaseImpl.fillInIDAndScreenName();
            assertEquals(1234567890L, twitterBaseImpl.getId());
        } catch (TwitterException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        } catch (IllegalStateException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        try {
            twitterBaseImpl.init();
            User user = twitterBaseImpl.fillInIDAndScreenName();
            assertEquals("screenName", user.getScreenName());
            assertEquals(1234567890L, user.getId());
        } catch (TwitterException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        } catch (IllegalStateException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetAuthorization() {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertEquals(auth, twitterBaseImpl.getAuthorization());
    }

    @Test
    public void testGetRateLimitStatusListenersSize() {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertEquals(0, twitterBaseImpl.getRateLimitStatusListeners().size());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());
    }

    @Test
    public void testHttpResponseReceived() {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        HttpResponseEvent event = mock(HttpResponseEvent.class);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        when(event.getTwitterException()).thenReturn(null);
        when(event.isAuthenticated()).thenReturn(true);
        when(rateLimitStatus.getRateLimitStatus()).thenReturn("rateLimitStatus");
        when(rateLimitStatus.getRemainingHits()).thenReturn(10);
        when(rateLimitStatus.getResetTime()).thenReturn(System.currentTimeMillis() + 60000);

        twitterBaseImpl.onRateLimitStatus(event);
        verify(event).getTwitterException();
        verify(event).isAuthenticated();
        verify(event).getResponse();
        verifyZeroInteractions(rateLimitStatus);

        twitterBaseImpl.onRateLimitReached(event);
        verify(event).getTwitterException();
        verify(event).isAuthenticated();
        verify(event).getResponse();
        verify(rateLimitStatus).getRateLimitStatus();
        verify(rateLimitStatus).getRemainingHits();
        verify(rateLimitStatus).getResetTime();
    }
}