package twitter4j;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import twitter4j.auth.*;
import twitter4j.conf.*;
import twitter4j.http.*;
import twitter4j.util.function.*;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        // Test when auth is not enabled
        twitterBaseImpl.setAuthorizationEnabled();
        assertThrows(IllegalStateException.class, twitterBaseImpl::getScreenName);

        // Test when screenName is null
        when(auth.isEnabled()).thenReturn(true);
        when(auth.getUserId()).thenReturn(null);
        when(auth.getScreenName()).thenReturn(null);
        assertNull(twitterBaseImpl.getScreenName());

        // Test when screenName is present in BasicAuthorization
        when(auth.isEnabled()).thenReturn(true);
        when(auth.getUserId()).thenReturn("testUserId");
        when(auth.getScreenName()).thenReturn("testScreenName");
        assertEquals("testScreenName", twitterBaseImpl.getScreenName());

        // Test when screenName is present in XAuthAuthorization
        when(auth.isEnabled()).thenReturn(true);
        when(auth.getUserId()).thenReturn("testUserId");
        when(auth.getScreenName()).thenReturn(null);
        when(auth.getOAuthConsumerKey()).thenReturn("testConsumerKey");
        when(auth.getOAuthConsumerSecret()).thenReturn("testConsumerSecret");
        when(auth.getOAuthAccessToken()).thenReturn(new AccessToken("testAccessToken", "testAccessTokenSecret"));
        when(auth.getOAuthAccessTokenSecret()).thenReturn("testAccessTokenSecret");
        assertEquals("testScreenName", twitterBaseImpl.getScreenName());
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        // Test when auth is not enabled
        twitterBaseImpl.setAuthorizationEnabled();
        assertThrows(IllegalStateException.class, twitterBaseImpl::getId);

        // Test when id is 0
        when(auth.isEnabled()).thenReturn(true);
        when(auth.getUserId()).thenReturn(null);
        when(auth.getScreenName()).thenReturn("testScreenName");
        twitterBaseImpl.fillInIDAndScreenName();
        assertEquals(0, twitterBaseImpl.getId());

        // Test when id is present
        when(auth.isEnabled()).thenReturn(true);
        when(auth.getUserId()).thenReturn(1234567890L);
        twitterBaseImpl.fillInIDAndScreenName();
        assertEquals(1234567890L, twitterBaseImpl.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);

        twitterBaseImpl.addRateLimitStatusListener(listener);
        verify(listener, times(1)).onRateLimitStatus(any(RateLimitStatusEvent.class));
    }

    @Test
    public void testOnRateLimitStatus() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);
        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                action.accept(event);
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
            }
        };

        twitterBaseImpl.onRateLimitStatus(listener);
        verify(listener, times(1)).onRateLimitStatus(any(RateLimitStatusEvent.class));
    }

    @Test
    public void testOnRateLimitReached() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);
        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
                action.accept(event);
            }
        };

        twitterBaseImpl.onRateLimitReached(listener);
        verify(listener, times(1)).onRateLimitReached(any(RateLimitStatusEvent.class));
    }

    @Test
    public void testHttpResponseReceived() throws TwitterException, IOException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);
        HttpResponseEvent event = mock(HttpResponseEvent.class);
        RateLimitStatus status = mock(RateLimitStatus.class);
        RateLimitStatusEvent statusEvent = new RateLimitStatusEvent(twitterBaseImpl, status, true);

        RateLimitStatusListener listener1 = mock(RateLimitStatusListener.class);
        RateLimitStatusListener listener2 = mock(RateLimitStatusListener.class);
        twitterBaseImpl.onRateLimitStatus(listener1);
        twitterBaseImpl.onRateLimitReached(listener2);
        twitterBaseImpl.httpResponseReceived(event);

        // Test no rate limit reached
        verify(listener1, times(1)).onRateLimitStatus(statusEvent);
        verify(listener2, times(0)).onRateLimitReached(statusEvent);

        // Test rate limit reached
        twitterBaseImpl.setRateLimitStatus(status);
        twitterBaseImpl.httpResponseReceived(event);
        verify(listener1, times(1)).onRateLimitStatus(statusEvent);
        verify(listener2, times(1)).onRateLimitReached(statusEvent);
    }

    @Test
    public void testGetAuthorization() {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        assertEquals(auth, twitterBaseImpl.getAuthorization());
    }

    @Test
    public void testGetConfiguration() {
        Configuration conf = mock(Configuration.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);

        assertEquals(conf, twitterBaseImpl.getConfiguration());
    }

    @Test
    public void testEnsureAuthorizationEnabled() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);

        assertThrows(IllegalStateException.class, twitterBaseImpl::ensureAuthorizationEnabled);
    }

    @Test
    public void testEnsureOAuthEnabled() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);

        assertThrows(IllegalStateException.class, twitterBaseImpl::ensureOAuthEnabled);
    }
}