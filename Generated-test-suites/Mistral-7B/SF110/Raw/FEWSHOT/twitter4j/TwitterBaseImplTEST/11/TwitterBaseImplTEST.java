package twitter4j;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.Test;
import org.mockito.Mockito;
import twitter4j.auth.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import twitter4j.HttpClient;
import twitter4j.TwitterException;
import twitter4j.Json.*;
import twitter4j.http.HttpClientFactory;
import twitter4j.http.HttpResponse;
import twitter4j.http.HttpResponseEvent;
import twitter4j.oauth.*;
import twitter4j.oauth2.*;
import twitter4j.util.RateLimitStatusEvent;

public class TwitterBaseImplTest {

    @Test
    public void testInit() throws TwitterException {
        Configuration conf = Mockito.mock(Configuration.class);
        Authorization auth = Mockito.mock(Authorization.class);

        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        verify(auth, times(1)).isEnabled();
        assertEquals(HttpClientFactory.getInstance(conf.getHttpClientConfiguration()), twitterBaseImpl.http);
        assertEquals(conf, twitterBaseImpl.conf);
        assertEquals(auth, twitterBaseImpl.auth);
        assertEquals(new JSONImplFactory(conf), twitterBaseImpl.factory);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitWithoutAuth() throws TwitterException {
        Configuration conf = Mockito.mock(Configuration.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.init();
    }

    @Test
    public void testGetScreenName() throws TwitterException {
        Configuration conf = Mockito.mock(Configuration.class);
        Authorization auth = Mockito.mock(Authorization.class);

        when(auth.isEnabled()).thenReturn(true);
        when(auth.getUserId()).thenReturn("test_user_id");

        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        String screenName = twitterBaseImpl.getScreenName();
        assertEquals("test_user_id", screenName);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithoutAuth() throws TwitterException {
        Configuration conf = Mockito.mock(Configuration.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.init();
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws TwitterException {
        Configuration conf = Mockito.mock(Configuration.class);
        Authorization auth = Mockito.mock(Authorization.class);

        when(auth.isEnabled()).thenReturn(true);
        when(auth.getUserId()).thenReturn(1L);

        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        long id = twitterBaseImpl.getId();
        assertEquals(1L, id);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithoutAuth() throws TwitterException {
        Configuration conf = Mockito.mock(Configuration.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.init();
        twitterBaseImpl.getId();
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        Configuration conf = Mockito.mock(Configuration.class);
        Authorization auth = Mockito.mock(Authorization.class);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        ObjectFactory factory = Mockito.mock(ObjectFactory.class);
        UserJsonImpl userJsonImpl = Mockito.mock(UserJsonImpl.class);

        when(auth.isEnabled()).thenReturn(true);
        when(auth.getUserId()).thenReturn("test_user_id");
        when(auth.getInstance(User.class)).thenReturn(userJsonImpl);
        when(userJsonImpl.getId()).thenReturn(1L);
        when(userJsonImpl.getScreenName()).thenReturn("test_screen_name");
        when(conf.getRestBaseURL()).thenReturn("test_rest_base_url");
        when(httpClient.get(eq("test_rest_base_url") + "/account/verify_credentials.json", any(HttpParameter[].class), eq(auth), eq(conf)))
                .thenReturn(new HttpResponse(200, new ByteArrayInputStream("{}".getBytes())));
        when(factory.getInstance(JSONObject.class)).thenReturn(new JSONObject("{}"));

        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.http = httpClient;
        twitterBaseImpl.setFactory(factory);

        User user = twitterBaseImpl.fillInIDAndScreenName();

        assertEquals(1L, user.getId());
        assertEquals("test_screen_name", user.getScreenName());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);

        twitterBaseImpl.addRateLimitStatusListener(listener);

        List<RateLimitStatusListener> rateLimitStatusListeners = twitterBaseImpl.rateLimitStatusListeners;
        assertEquals(1, rateLimitStatusListeners.size());
        assertTrue(rateLimitStatusListeners.contains(listener));
    }

    @Test
    public void testOnRateLimitStatus() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);
        Consumer<RateLimitStatusEvent> consumer = mock(Consumer.class);

        RateLimitStatusEvent rateLimitStatusEvent = mock(RateLimitStatusEvent.class);

        twitterBaseImpl.onRateLimitStatus(consumer);

        for (RateLimitStatusListener listener : twitterBaseImpl.rateLimitStatusListeners) {
            listener.onRateLimitStatus(rateLimitStatusEvent);
        }

        verify(consumer, times(1)).accept(rateLimitStatusEvent);
    }

    @Test
    public void testOnRateLimitReached() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);
        Consumer<RateLimitStatusEvent> consumer = mock(Consumer.class);

        RateLimitStatusEvent rateLimitStatusEvent = mock(RateLimitStatusEvent.class);

        twitterBaseImpl.onRateLimitReached(consumer);

        for (RateLimitStatusListener listener : twitterBaseImpl.rateLimitStatusListeners) {
            listener.onRateLimitReached(rateLimitStatusEvent);
        }

        // No calls to consumer since onRateLimitReached method does nothing
        verifyNoMoreInteractions(consumer);
    }

    @Test
    public void testHttpResponseReceived() throws TwitterException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);
        Consumer<RateLimitStatusEvent> consumer = mock(Consumer.class);

        RateLimitStatusEvent rateLimitStatusEvent = mock(RateLimitStatusEvent.class);

        HttpResponseEvent httpResponseEvent = mock(HttpResponseEvent.class);
        HttpResponse httpResponse = mock(HttpResponse.class);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        int statusCode = 200;

        when(httpResponse.getStatusCode()).thenReturn(statusCode);
        when(httpResponseEvent.getResponse()).thenReturn(httpResponse);
        when(httpResponseEvent.getTwitterException()).thenReturn(null);

        when(rateLimitStatus.getRemainingBuckets()).thenReturn(new int[0]);
        when(rateLimitStatus.getLimit()).thenReturn(new int[0]);
        when(rateLimitStatus.getCost()).thenReturn(new int[0]);
        when(rateLimitStatus.getResetTime()).thenReturn(System.currentTimeMillis());

        when(twitterBaseImpl.getFactory().createRateLimitStatusFromResponseHeader(httpResponse)).thenReturn(rateLimitStatus);

        twitterBaseImpl.rateLimitStatusListeners.add(new RateLimitStatusListener() {
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                consumer.accept(event);
            }

            public void onRateLimitReached(RateLimitStatusEvent event) {
            }
        });

        twitterBaseImpl.httpResponseReceived(httpResponseEvent);

        verify(consumer, times(1)).accept(rateLimitStatusEvent);
    }

    @Test
    public void testHttpResponseReceivedWithTwitterException() throws TwitterException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);
        Consumer<RateLimitStatusEvent> consumer = mock(Consumer.class);

        RateLimitStatusEvent rateLimitStatusEvent = mock(RateLimitStatusEvent.class);

        HttpResponseEvent httpResponseEvent = mock(HttpResponseEvent.class);
        HttpResponse httpResponse = mock(HttpResponse.class);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        int statusCode = 429;

        when(httpResponse.getStatusCode()).thenReturn(statusCode);
        when(httpResponseEvent.getResponse()).thenReturn(httpResponse);
        when(httpResponseEvent.getTwitterException()).thenReturn(new TwitterException("Test error message", statusCode, new RateLimitStatus(rateLimitStatus)));

        when(rateLimitStatus.getRemainingBuckets()).thenReturn(new int[0]);
        when(rateLimitStatus.getLimit()).thenReturn(new int[0]);
        when(rateLimitStatus.getCost()).thenReturn(new int[0]);
        when(rateLimitStatus.getResetTime()).thenReturn(System.currentTimeMillis());

        when(twitterBaseImpl.getFactory().createRateLimitStatusFromResponseHeader(httpResponse)).thenReturn(rateLimitStatus);

        twitterBaseImpl.rateLimitStatusListeners.add(new RateLimitStatusListener() {
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                consumer.accept(event);
            }

            public void onRateLimitReached(RateLimitStatusEvent event) {
            }
        });

        twitterBaseImpl.httpResponseReceived(httpResponseEvent);

        verify(consumer, times(1)).accept(rateLimitStatusEvent);

        RateLimitStatusEvent rateLimitStatusEventReceived = rateLimitStatusEvent;
        verify(consumer).accept(argThat(event -> event.equals(rateLimitStatusEventReceived)));
    }
}