package twitter4j;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private Configuration configuration;
    private Authorization auth;
    private HttpClient httpClient;
    private List<RateLimitStatusListener> rateLimitStatusListeners;

    @Before
    public void setUp() {
        configuration = new Configuration();
        auth = Mockito.mock(Authorization.class);
        httpClient = Mockito.mock(HttpClient.class);
        rateLimitStatusListeners = new ArrayList<>();
        twitterBaseImpl = new TwitterBaseImpl(configuration, auth);
        twitterBaseImpl.init();
        twitterBaseImpl.setFactory();
    }

    @Test
    public void testGetScreenNameSuccess() {
        when(auth.isEnabled()).thenReturn(true);
        String screenName = twitterBaseImpl.getScreenName();
        assertEquals(screenName, "expected_screen_name");
        verify(auth, times(1)).isEnabled();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameFailure() {
        when(auth.isEnabled()).thenReturn(false);
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testAddRateLimitStatusListener() {
        twitterBaseImpl.addRateLimitStatusListener(mock(RateLimitStatusListener.class));
        ArgumentCaptor<RateLimitStatusListener> listenerCaptor = ArgumentCaptor.forClass(RateLimitStatusListener.class);
        verify(rateLimitStatusListeners, times(1)).add(listenerCaptor.capture());
        RateLimitStatusListener listener = listenerCaptor.getValue();
        assertEquals(listener, twitterBaseImpl.getRateLimitStatusListeners().get(0));
    }

    @Test
    public void testOnRateLimitStatus() {
        RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(100, 150), true);
        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
        twitterBaseImpl.onRateLimitStatus(action);
        verify(action, times(1)).accept(event);
    }

    @Test
    public void testOnRateLimitReached() {
        RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(100, 150), false);
        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
        twitterBaseImpl.onRateLimitReached(action);
        verify(action, times(1)).accept(event);
    }

    @Test
    public void testHttpResponseReceived() {
        HttpResponseEvent event = new HttpResponseEvent(null, null, null, null, null, null, null, null, false);
        RateLimitStatus rateLimitStatus = new RateLimitStatus(100, 150);
        twitterBaseImpl.onRateLimitStatus(mock(Consumer.class));
        twitterBaseImpl.onRateLimitReached(mock(Consumer.class));
        twitterBaseImpl.httpResponseReceived(event);
        verify(rateLimitStatusListeners, times(1)).onRateLimitStatus(eq(event));
        verify(rateLimitStatusListeners, times(1)).onRateLimitReached(eq(event));
    }

    @Test
    public void testGetAuthorization() {
        assertEquals(auth, twitterBaseImpl.getAuthorization());
    }

    @Test
    public void testGetConfiguration() {
        assertEquals(configuration, twitterBaseImpl.getConfiguration());
    }
}