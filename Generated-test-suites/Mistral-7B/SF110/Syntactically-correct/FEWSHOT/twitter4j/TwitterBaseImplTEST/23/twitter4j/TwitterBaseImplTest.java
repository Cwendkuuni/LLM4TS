package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;
import twitter4j.auth.*;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertEquals(null, twitterBaseImpl.getScreenName());

        User user = new UserJSONImpl(new HttpResponse(HttpResponseStatus.OK, "{}"), conf);
        twitterBaseImpl.fillInIDAndScreenName(new HttpParameter[0]);
        assertEquals("screenName", twitterBaseImpl.getScreenName());
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertEquals(0, twitterBaseImpl.getId());

        User user = new UserJSONImpl(new HttpResponse(HttpResponseStatus.OK, "{\"id\":123456789}"), conf);
        twitterBaseImpl.fillInIDAndScreenName(new HttpParameter[0]);
        assertEquals(123456789, twitterBaseImpl.getId());
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl.ensureAuthorizationEnabled();

        User user = new UserJSONImpl(new HttpResponse(HttpResponseStatus.OK, "{\"id\":123456789,\"screen_name\":\"screenName\"}"), conf);
        twitterBaseImpl.fillInIDAndScreenName();
        assertEquals("screenName", twitterBaseImpl.getScreenName());
        assertEquals(123456789, twitterBaseImpl.getId());
    }

    @Test
    public void testFillInIDAndScreenNameWithParameters() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl.ensureAuthorizationEnabled();

        HttpParameter[] parameters = new HttpParameter[0];
        User user = new UserJSONImpl(new HttpResponse(HttpResponseStatus.OK, "{\"id\":123456789,\"screen_name\":\"screenName\"}"), conf);
        twitterBaseImpl.fillInIDAndScreenName(parameters);
        assertEquals("screenName", twitterBaseImpl.getScreenName());
        assertEquals(123456789, twitterBaseImpl.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        assertTrue(twitterBaseImpl.rateLimitStatusListeners.contains(listener));
    }

    @Test
    public void testOnRateLimitStatus() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

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
        twitterBaseImpl.onRateLimitStatus(action);
        twitterBaseImpl.addRateLimitStatusListener(listener);

        RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
        when(event.getRateLimitStatus()).thenReturn(new RateLimitStatus(100, 200, 300));
        when(event.isAuthenticated()).thenReturn(true);
        action.accept(event);
        verify(listener, times(1)).onRateLimitStatus(event);
        verify(listener, times(1)).onRateLimitReached(event);
    }

    @Test
    public void testOnRateLimitReached() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

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
        twitterBaseImpl.onRateLimitReached(action);
        twitterBaseImpl.addRateLimitStatusListener(listener);

        RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
        when(event.getRateLimitStatus()).thenReturn(new RateLimitStatus(100, 200, 300));
        when(event.isAuthenticated()).thenReturn(true);
        action.accept(event);
        verify(listener, times(1)).onRateLimitStatus(event);
        verify(listener, times(1)).onRateLimitReached(event);
    }

    @Test
    public void testHttpResponseReceived() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.onRateLimitStatus(action -> {
            listener.onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(100, 200, 300), true));
            listener.onRateLimitReached(new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(100, 200, 300), true));
        });

        HttpResponseEvent event = new HttpResponseEvent(new HttpResponse(HttpResponseStatus.OK, "{}"), null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        twitterBaseImpl.httpResponseReceived(event);

        verify(listener, times(1)).onRateLimitStatus(isA(RateLimitStatusEvent.class));
        verify(listener, times(1)).onRateLimitReached(isA(RateLimitStatusEvent.class));
    }

    @Test
    public void testGetAuthorization() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertEquals(auth, twitterBaseImpl.getAuthorization());
    }

    @Test
    public void testGetConfiguration() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertEquals(conf, twitterBaseImpl.getConfiguration());
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureAuthorizationEnabled() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.ensureAuthorizationEnabled();
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureOAuthEnabled() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.ensureOAuthEnabled();
    }
}