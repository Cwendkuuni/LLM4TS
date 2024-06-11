import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.http.HttpClient;
import twitter4j.http.HttpResponse;
import twitter4j.util.function.Consumer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterBaseImplTest {

    @Mock
    private Configuration conf;

    @Mock
    private Authorization auth;

    @Mock
    private HttpClient http;

    @Mock
    private RateLimitStatus rateLimitStatus;

    @Mock
    private HttpResponse httpResponse;

    @Mock
    private RateLimitStatusEvent rateLimitStatusEvent;

    @InjectMocks
    private TwitterBaseImpl twitterBaseImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
    }

    @Test
    public void testGetScreenName() throws Exception {
        when(auth.isEnabled()).thenReturn(true);
        when(auth.getInstance(TwitterBaseImpl.BasicAuthorization.class)).thenReturn(new BasicAuthorization());
        String screenName = twitterBaseImpl.getScreenName();
        verify(auth).isEnabled();
        verify(auth).getInstance(TwitterBaseImpl.BasicAuthorization.class);
        verify(twitterBaseImpl).fillInIDAndScreenName();
        assertEquals("Expected screen name", screenName, twitterBaseImpl.screenName);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWhenAuthNotEnabled() throws Exception {
        when(auth.isEnabled()).thenReturn(false);
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws Exception {
        when(auth.isEnabled()).thenReturn(true);
        when(auth.getInstance(TwitterBaseImpl.BasicAuthorization.class)).thenReturn(new BasicAuthorization());
        User user = new User();
        when(http.get(anyString(), any(HttpParameter[].class), eq(auth), any(Configuration.class))).thenReturn(httpResponse);
        doReturn(user).when(twitterBaseImpl).fillInIDAndScreenName(httpResponse.getResponseHeaders());
        long id = twitterBaseImpl.getId();
        verify(auth).isEnabled();
        verify(auth).getInstance(TwitterBaseImpl.BasicAuthorization.class);
        verify(twitterBaseImpl).fillInIDAndScreenName(httpResponse.getResponseHeaders());
        assertEquals("Expected id", id, twitterBaseImpl.id);
    }

    @Test(expected = TwitterException.class)
    public void testGetIdWhenAuthenticationFailed() throws Exception {
        when(auth.isEnabled()).thenReturn(true);
        when(auth.getInstance(TwitterBaseImpl.BasicAuthorization.class)).thenReturn(new BasicAuthorization());
        when(http.get(anyString(), any(HttpParameter[].class), eq(auth), any(Configuration.class))).thenThrow(new TwitterException(-1, "Authentication failed"));
        twitterBaseImpl.getId();
    }

    @Test
    public void testAddRateLimitStatusListener() {
        verify(rateLimitStatusListeners).add(any(RateLimitStatusListener.class));
    }

    @Test
    public void testOnRateLimitStatus() {
        Consumer<RateLimitStatusEvent> action = argument -> {};
        doReturn(rateLimitStatusEvent).when(twitterBaseImpl).new RateLimitStatusEvent(any(), any(), anyBoolean());
        twitterBaseImpl.onRateLimitStatus(action);
        verify(rateLimitStatusListeners).stream()
                .filter(listener -> listener instanceof RateLimitStatusListener)
                .map(listener -> (RateLimitStatusListener) listener)
                .forEach(listener -> listener.onRateLimitStatus(rateLimitStatusEvent));
        verify(rateLimitStatusListeners).stream()
                .filter(listener -> listener instanceof RateLimitStatusListener)
                .map(listener -> (RateLimitStatusListener) listener)
                .forEach(listener -> listener.onRateLimitReached(rateLimitStatusEvent));
    }

    @Test
    public void testOnRateLimitReached() {
        Consumer<RateLimitStatusEvent> action = argument -> {};
        doReturn(rateLimitStatusEvent).when(twitterBaseImpl).new RateLimitStatusEvent(any(), any(), anyBoolean());
        twitterBaseImpl.onRateLimitReached(action);
        verify(rateLimitStatusListeners).stream()
                .filter(listener -> listener instanceof RateLimitStatusListener)
                .map(listener -> (RateLimitStatusListener) listener)
                .forEach(listener -> listener.onRateLimitStatus(rateLimitStatusEvent));
        verify(rateLimitStatusListeners).stream()
                .filter(listener -> listener instanceof RateLimitStatusListener)
                .map(listener -> (RateLimitStatusListener) listener)
                .forEach(listener -> listener.onRateLimitReached(rateLimitStatusEvent));
    }

    @Test
    public void testHttpResponseReceived() {
        HttpResponseEvent httpResponseEvent = new HttpResponseEvent(httpResponse, null, false, null);
        Consumer<RateLimitStatusEvent> onRateLimitStatusAction = argument -> {};
        Consumer<RateLimitStatusEvent> onRateLimitReachedAction = argument -> {};
        twitterBaseImpl.onRateLimitStatus(onRateLimitStatusAction);
        twitterBaseImpl.onRateLimitReached(onRateLimitReachedAction);
        twitterBaseImpl.httpResponseReceived(httpResponseEvent);
        verify(onRateLimitStatusAction).accept(rateLimitStatusEvent);
        verify(onRateLimitReachedAction).accept(rateLimitStatusEvent);
    }

    @Test
    public void testSetOAuthConsumer() {
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
        verify(auth).setOAuthConsumer("consumerKey", "consumerSecret");
    }

    @Test(expected = IllegalStateException.class)
    public void testSetOAuthConsumerWhenOAuthNotSupported() {
        twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
    }

    @Test(expected = IllegalStateException.class)
    public void testSetOAuthConsumerWhenAuthNotEnabled() {
        when(auth.isEnabled()).thenReturn(false);
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
    }
}