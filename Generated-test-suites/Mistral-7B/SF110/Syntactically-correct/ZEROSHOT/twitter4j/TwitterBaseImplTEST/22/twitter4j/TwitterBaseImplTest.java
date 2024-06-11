package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import twitter4j.Authorization;
import twitter4j.Configuration;
import twitter4j.OAuthAuthorization;
import twitter4j.OAuthSupport;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private Configuration conf;
    private Authorization auth;
    private List<RateLimitStatusListener> rateLimitStatusListeners;

    @Before
    public void setup() {
        conf = new ConfigurationBuilder().build();
        auth = Mockito.mock(Authorization.class);
        rateLimitStatusListeners = new ArrayList<>();
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        String expectedScreenName = "testUser";
        Mockito.when(auth.isEnabled()).thenReturn(true);
        Mockito.when(auth.getUserId()).thenReturn(expectedScreenName);
        assertEquals(expectedScreenName, twitterBaseImpl.getScreenName());

        Mockito.when(auth.isEnabled()).thenReturn(false);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getScreenName());
        assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", exception.getMessage());
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        long expectedId = 123456789L;
        Mockito.when(auth.isEnabled()).thenReturn(true);
        Mockito.when(auth.getUserId()).thenReturn("testUser");
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(expectedId);
        Mockito.when(twitterBaseImpl.fillInIDAndScreenName(Mockito.any(HttpParameter[].class))).thenReturn(user);
        assertEquals(expectedId, twitterBaseImpl.getId());

        Mockito.when(auth.isEnabled()).thenReturn(false);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getId());
        assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", exception.getMessage());
    }

    @Test
    public void testAddAndRemoveRateLimitStatusListener() {
        RateLimitStatusListener listener = Mockito.mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());
        twitterBaseImpl.removeRateLimitStatusListener(listener);
        assertEquals(0, twitterBaseImpl.getRateLimitStatusListeners().size());
    }

    @Test
    public void testOnRateLimitStatus() throws TwitterException {
        RateLimitStatusEvent event = Mockito.mock(RateLimitStatusEvent.class);
        RateLimitStatusListener listener = Mockito.mock(RateLimitStatusListener.class);
        twitterBaseImpl.onRateLimitStatus(event);
        Mockito.verify(listener).onRateLimitStatus(event);
    }

    @Test
    public void testOnRateLimitReached() throws TwitterException {
        RateLimitStatusEvent event = Mockito.mock(RateLimitStatusEvent.class);
        RateLimitStatusListener listener = Mockito.mock(RateLimitStatusListener.class);
        twitterBaseImpl.onRateLimitReached(event);
        Mockito.verify(listener).onRateLimitReached(event);
    }

    @Test
    public void testHttpResponseReceived() throws TwitterException {
        HttpResponseEvent httpResponseEvent = Mockito.mock(HttpResponseEvent.class);
        RateLimitStatus rateLimitStatus = Mockito.mock(RateLimitStatus.class);
        RateLimitStatusListener listener = Mockito.mock(RateLimitStatusListener.class);
        twitterBaseImpl.onRateLimitStatus(rateLimitStatus);
        Mockito.verify(listener).onRateLimitStatus(Mockito.any(RateLimitStatusEvent.class));

        Mockito.when(httpResponseEvent.getTwitterException()).thenReturn(null);
        Mockito.when(httpResponseEvent.isAuthenticated()).thenReturn(true);
        Mockito.when(httpResponseEvent.getResponse().getStatusCode()).thenReturn(420);
        twitterBaseImpl.httpResponseReceived(httpResponseEvent);
        Mockito.verify(listener).onRateLimitStatus(Mockito.any(RateLimitStatusEvent.class));
        Mockito.verify(listener).onRateLimitReached(Mockito.any(RateLimitStatusEvent.class));

        Mockito.when(httpResponseEvent.getTwitterException()).thenReturn(new TwitterException("Test Exception"));
        twitterBaseImpl.httpResponseReceived(httpResponseEvent);
        Mockito.verify(rateLimitStatusListeners, Mockito.never()).onRateLimitStatus(Mockito.any(RateLimitStatusEvent.class));
        Mockito.verify(rateLimitStatusListeners, Mockito.never()).onRateLimitReached(Mockito.any(RateLimitStatusEvent.class));
    }

    @Test
    public void testGetAuthorization() {
        assertEquals(auth, twitterBaseImpl.getAuthorization());
    }

    @Test
    public void testGetConfiguration() {
        assertEquals(conf, twitterBaseImpl.getConfiguration());
    }

    @Test
    public void testEnsureAuthorizationEnabled() throws TwitterException {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> twitterBaseImpl.ensureAuthorizationEnabled());
        assertEquals("Authentication credentials are missing. See http://twitter4j.org/en/configuration.html for details. See and register at http://apps.twitter.com/", exception.getMessage());
    }

    @Test
    public void testEnsureOAuthEnabled() throws TwitterException {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> twitterBaseImpl.ensureOAuthEnabled());
        assertEquals("OAuth required. Authentication credentials are missing. See http://twitter4j.org/en/configuration.html for details. See and register at http://apps.twitter.com/", exception.getMessage());
    }
}