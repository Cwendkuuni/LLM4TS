import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.HttpClient;
import twitter4j.http.HttpResponse;
import twitter4j.util.function.Consumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private Configuration configuration;
    private Authorization auth;
    private HttpClient httpClient;

    @Before
    public void setup() {
        configuration = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("test-key")
                .setOAuthConsumerSecret("test-secret")
                .build();
        auth = new BasicAuthorization("test-username", "test-password");
        httpClient = Mockito.mock(HttpClient.class);
        twitterBaseImpl = new TwitterBaseImpl(configuration, auth);
        twitterBaseImpl.http = httpClient;
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        twitterBaseImpl.ensureAuthorizationEnabled();
        Mockito.when(httpClient.get(Mockito.anyString(), Mockito.any(HttpParameter[].class), eq(auth), eq(twitterBaseImpl)))
                .thenReturn(new HttpResponse(200, new ByteArrayInputStream("{\"screen_name\":\"test-username\"}".getBytes())));

        String screenName = twitterBaseImpl.getScreenName();
        Mockito.verify(httpClient).get(eq(twitterBaseImpl.conf.getRestBaseURL() + "account/verify_credentials.json"), eq(new HttpParameter[0]), eq(auth), eq(twitterBaseImpl));
        ArgumentCaptor<Consumer<RateLimitStatusEvent>> rateLimitStatusListenerCaptor = ArgumentCaptor.forClass(Consumer.class);
        ArgumentCaptor<Consumer<RateLimitStatusEvent>> rateLimitReachedListenerCaptor = ArgumentCaptor.forClass(Consumer.class);

        twitterBaseImpl.onRateLimitReached(rateLimitStatusListenerCaptor.capture());
        twitterBaseImpl.onRateLimitStatus(rateLimitReachedListenerCaptor.capture());

        twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(new HttpResponse(429, null), new TwitterException(429, null), false));
        rateLimitStatusListenerCaptor.getValue().onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, null, false));
        rateLimitReachedListenerCaptor.getValue().onRateLimitReached(new RateLimitStatusEvent(twitterBaseImpl, null, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithoutAuthorization() throws TwitterException, IllegalStateException {
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        twitterBaseImpl.ensureAuthorizationEnabled();
        Mockito.when(httpClient.get(Mockito.anyString(), Mockito.any(HttpParameter[].class), eq(auth), eq(twitterBaseImpl)))
                .thenReturn(new HttpResponse(200, new ByteArrayInputStream("{\"id\":1234567890}".getBytes())));

        long id = twitterBaseImpl.getId();
        Mockito.verify(httpClient).get(eq(twitterBaseImpl.conf.getRestBaseURL() + "account/verify_credentials.json"), eq(new HttpParameter[0]), eq(auth), eq(twitterBaseImpl));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithoutAuthorization() throws TwitterException, IllegalStateException {
        twitterBaseImpl.getId();
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = Mockito.mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);

        List<RateLimitStatusListener> rateLimitStatusListeners = twitterBaseImpl.rateLimitStatusListeners;
        assertEquals(1, rateLimitStatusListeners.size());
        assertEquals(listener, rateLimitStatusListeners.get(0));
    }

    @Test
    public void testOnRateLimitStatus() {
        Consumer<RateLimitStatusEvent> consumer = Mockito.mock(Consumer.class);
        RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, null, false);

        twitterBaseImpl.onRateLimitStatus(consumer);
        Mockito.verify(consumer).accept(eq(event));
    }

    @Test
    public void testOnRateLimitReached() {
        Consumer<RateLimitStatusEvent> consumer = Mockito.mock(Consumer.class);
        RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, null, false);

        twitterBaseImpl.onRateLimitReached(consumer);
        Mockito.verify(consumer).accept(eq(event));
    }

    @Test
    public void testHttpResponseReceived() {
        HttpResponseEvent event = new HttpResponseEvent(new HttpResponse(200, null), null, false);
        RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 15, 60);
        RateLimitStatusEvent statusEvent = new RateLimitStatusEvent(twitterBaseImpl, rateLimitStatus, false);

        twitterBaseImpl.onRateLimitStatus(consumer);
        twitterBaseImpl.onRateLimitReached(consumer);
        twitterBaseImpl.httpResponseReceived(event);

        List<RateLimitStatusListener> rateLimitStatusListeners = twitterBaseImpl.rateLimitStatusListeners;
        assertEquals(2, rateLimitStatusListeners.size());

        Consumer<RateLimitStatusEvent> statusListener = rateLimitStatusListeners.get(0);
        assertEquals(statusEvent, statusListener.getClass().cast(statusListener).getEvent());

        Consumer<RateLimitStatusEvent> reachedListener = rateLimitStatusListeners.get(1);
        assertEquals(statusEvent, reachedListener.getClass().cast(reachedListener).getEvent());
    }
}