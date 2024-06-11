package twitter4j;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.io.*;
import twitter4j.*;
import twitter4j.auth.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        when(auth.isEnabled()).thenReturn(true);

        String screenName = twitterBaseImpl.getScreenName();
        assertEquals("", screenName);

        twitterBaseImpl.setScreenName("testScreenName");
        assertEquals("testScreenName", twitterBaseImpl.getScreenName());

        twitterBaseImpl.setId(12345L);
        assertEquals("testScreenName", twitterBaseImpl.getScreenName());

        when(auth.isEnabled()).thenReturn(false);
        try {
            twitterBaseImpl.getScreenName();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        when(auth.isEnabled()).thenReturn(true);

        when(auth.getInstance(User.class)).thenReturn(new UserJSONImpl(new Object(), conf));

        long id = twitterBaseImpl.getId();
        assertEquals(0L, id);

        twitterBaseImpl.fillInIDAndScreenName();
        assertEquals(12345L, twitterBaseImpl.getId());

        when(auth.isEnabled()).thenReturn(false);
        try {
            twitterBaseImpl.getId();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        when(auth.isEnabled()).thenReturn(true);

        when(auth.getInstance(User.class)).thenReturn(new UserJSONImpl(new Object(), conf));

        assertNull(twitterBaseImpl.getScreenName());
        assertNull(twitterBaseImpl.getId());

        twitterBaseImpl.fillInIDAndScreenName();
        assertNotNull(twitterBaseImpl.getScreenName());
        assertNotNull(twitterBaseImpl.getId());

        HttpParameter[] parameters = new HttpParameter[1];
        parameters[0] = new HttpParameter("key", "value");
        twitterBaseImpl.fillInIDAndScreenName(parameters);
        assertNotNull(twitterBaseImpl.getScreenName());
        assertNotNull(twitterBaseImpl.getId());

        when(auth.isEnabled()).thenReturn(false);
        try {
            twitterBaseImpl.fillInIDAndScreenName();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }
    }

    @Test
    public void testAddRateLimitStatusListener() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);

        twitterBaseImpl.addRateLimitStatusListener(listener);
        assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());
        verify(listener).onRateLimitStatus(any(RateLimitStatusEvent.class));
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
        assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());
        verify(action).accept(any(RateLimitStatusEvent.class));
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
        assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());
        verify(action).accept(any(RateLimitStatusEvent.class));
    }

    @Test
    public void testHttpResponseReceived() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);
        HttpClient httpClient = mock(HttpClient.class);
        HttpResponseEvent httpResponseEvent = mock(HttpResponseEvent.class);
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);

        when(httpClient.get(anyString(), any(HttpParameter[].class), eq(twitterBaseImpl.getAuthorization()), eq(twitterBaseImpl.getConfiguration()), eq(twitterBaseImpl), anyInt(), anyInt()))
                .thenReturn(httpResponseEvent);

        twitterBaseImpl.setHttpClient(httpClient);
        twitterBaseImpl.onRateLimitStatus(listener);
        verify(listener).onRateLimitStatus(any(RateLimitStatusEvent.class));

        when(httpResponseEvent.getTwitterException()).thenReturn(new TwitterException("Exception"));
        twitterBaseImpl.onRateLimitStatus(listener);
        verify(listener).onRateLimitStatus(any(RateLimitStatusEvent.class));

        when(rateLimitStatus.getRateLimitStatus()).thenReturn(RateLimitStatus.LIMIT_NONE);
        when(httpResponseEvent.getResponse()).thenReturn(new ByteArrayInputStream("status: 200 OK".getBytes()));
        twitterBaseImpl.onRateLimitStatus(listener);
        verify(listener, never()).onRateLimitReached(any(RateLimitStatusEvent.class));

        when(rateLimitStatus.getRateLimitStatus()).thenReturn(RateLimitStatus.LIMIT_RATE_LIMIT);
        when(httpResponseEvent.getResponse()).thenReturn(new ByteArrayInputStream("status: 429 Too Many Requests".getBytes()));
        twitterBaseImpl.onRateLimitStatus(listener);
        verify(listener).onRateLimitReached(any(RateLimitStatusEvent.class));
    }

}