package twitter4j;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.*;
import twitter4j.auth.*;
import twitter4j.conf.*;
import twitter4j.http.*;
import twitter4j.util.function.*;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private Configuration conf;
    private Authorization auth;
    private HttpClient http;
    private List<RateLimitStatusListener> rateLimitStatusListeners;
    private ObjectFactory factory;
    
    @Before
    public void setUp() {
        conf = new Configuration();
        auth = mock(Authorization.class);
        http = mock(HttpClient.class);
        rateLimitStatusListeners = new ArrayList<>();
        factory = new JSONImplFactory(conf);
        twitterBaseImpl = new TwitterBaseImpl(conf, auth) {
            @Override
            public void httpResponseReceived(HttpResponseEvent event) {
                super.httpResponseReceived(event);
                verify(http).get(eq(conf.getRestBaseURL() + "account/verify_credentials.json"), any(HttpParameter[].class), eq(auth), eq(this), any(HttpRequest.class));
            }
        };
    }

    @Test
    public void testNullAuthorization() {
        twitterBaseImpl = new TwitterBaseImpl(conf, null);
        assertNull(twitterBaseImpl.getAuthorization());
    }

    @Test
    public void testGetAuthorization() {
        when(auth.isEnabled()).thenReturn(true);
        assertEquals(auth, twitterBaseImpl.getAuthorization());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetAuthorizationWithoutEnabled() {
        when(auth.isEnabled()).thenReturn(false);
        twitterBaseImpl.getAuthorization();
    }

    @Test
    public void testGetConfiguration() {
        assertEquals(conf, twitterBaseImpl.getConfiguration());
    }

    @Test
    public void testSetFactory() {
        twitterBaseImpl.setFactory();
        assertEquals(factory, twitterBaseImpl.getFactory());
    }

    @Test(expected = IllegalStateException.class)
    public void testScreenNameNull() throws TwitterException, IllegalStateException {
        twitterBaseImpl.getScreenName();
    }

    @Test(expected = IllegalStateException.class)
    public void testIdNull() throws TwitterException, IllegalStateException {
        twitterBaseImpl.getId();
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        User user = mock(User.class);
        when(user.getScreenName()).thenReturn("testUser");
        when(user.getId()).thenReturn(1L);
        when(http.get(anyString(), any(HttpParameter[].class), eq(auth), eq(twitterBaseImpl), any(HttpRequest.class))).thenReturn(new HttpResponse(200, "{}"));
        twitterBaseImpl.fillInIDAndScreenName();
        assertEquals("testUser", twitterBaseImpl.getScreenName());
        assertEquals(1L, twitterBaseImpl.getId());
        verify(http).get(eq(conf.getRestBaseURL() + "account/verify_credentials.json"), any(HttpParameter[].class), eq(auth), eq(twitterBaseImpl), any(HttpRequest.class));
    }

    @Test(expected = TwitterException.class)
    public void testFillInIDAndScreenNameWithException() throws TwitterException, IllegalStateException {
        when(http.get(anyString(), any(HttpParameter[].class), eq(auth), eq(twitterBaseImpl), any(HttpRequest.class))).thenThrow(new TwitterException(400, ""));
        twitterBaseImpl.fillInIDAndScreenName();
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        assertTrue(rateLimitStatusListeners.contains(listener));
    }

    @Test
    public void testOnRateLimitStatus() {
        Consumer<RateLimitStatusEvent> action = arg -> {};
        RateLimitStatusEvent statusEvent = mock(RateLimitStatusEvent.class);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        when(rateLimitStatus.getRemainingBuckets()).thenReturn(Collections.singletonList(new RateLimitBucket(1, 1, 1)));
        when(statusEvent.getRateLimitStatus()).thenReturn(rateLimitStatus);
        twitterBaseImpl.onRateLimitStatus(action);
        verify(action).accept(eq(statusEvent));
    }

    @Test
    public void testOnRateLimitReached() {
        Consumer<RateLimitStatusEvent> action = arg -> {};
        RateLimitStatusEvent statusEvent = mock(RateLimitStatusEvent.class);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        when(rateLimitStatus.getRemainingBuckets()).thenReturn(Collections.singletonList(new RateLimitBucket(0, 1, 1)));
        when(statusEvent.getRateLimitStatus()).thenReturn(rateLimitStatus);
        twitterBaseImpl.onRateLimitReached(action);
        verify(action).accept(eq(statusEvent));
    }
}