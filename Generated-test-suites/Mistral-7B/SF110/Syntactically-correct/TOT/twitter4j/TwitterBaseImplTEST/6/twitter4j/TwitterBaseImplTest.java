package twitter4j;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import twitter4j.Configuration;
import twitter4j.OAuthAuthorization;
import twitter4j.OAuthConsumer;
import twitter4j.TwitterException;
import twitter4j.auth.NullAuthorization;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBase;
    private Configuration conf;
    private OAuthAuthorization auth;

    @Before
    public void setup() {
        conf = new ConfigurationBuilder().build();
        auth = mock(OAuthAuthorization.class);
        twitterBase = new TwitterBaseImpl(conf, auth);
    }

    @Test
    public void testGetScreenName_NoAuthorization_ThrowIllegalStateException() {
        when(auth.isEnabled()).thenReturn(false);
        try {
            twitterBase.getScreenName();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }
    }

    @Test
    public void testGetScreenName_BasicAuthorization_ReturnScreenName() {
        when(auth.isEnabled()).thenReturn(true);
        when(((BasicAuthorization) auth).getUserId()).thenReturn("testUser");
        assertEquals("testUser", twitterBase.getScreenName());
    }

    @Test
    public void testGetScreenName_OAuthAuthorization_ReturnScreenName() {
        when(auth.isEnabled()).thenReturn(true);
        when(((OAuthAuthorization) auth).getAccessToken().getScreenName()).thenReturn("testUser");
        assertEquals("testUser", twitterBase.getScreenName());
    }

    @Test
    public void testGetId_NoAuthorization_ThrowIllegalStateException() {
        when(auth.isEnabled()).thenReturn(false);
        try {
            twitterBase.getId();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }
    }

    @Test
    public void testGetId_BasicAuthorization_ReturnId() {
        when(auth.isEnabled()).thenReturn(true);
        when(((BasicAuthorization) auth).getUserId()).thenReturn(1L);
        assertEquals(1L, twitterBase.getId());
    }

    @Test
    public void testGetId_OAuthAuthorization_ReturnId() {
        when(auth.isEnabled()).thenReturn(true);
        when(((OAuthAuthorization) auth).getAccessToken().getUserId()).thenReturn(1L);
        assertEquals(1L, twitterBase.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        List<RateLimitStatusListener> rateLimitStatusListeners = twitterBase.getRateLimitStatusListeners();
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBase.addRateLimitStatusListener(listener);
        assertNotNull(rateLimitStatusListeners);
        assertTrue(rateLimitStatusListeners.contains(listener));
    }

    @Test
    public void testHttpResponseReceived_RateLimitExceeded_NotifyListeners() {
        List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<>();
        RateLimitStatusListener listener1 = mock(RateLimitStatusListener.class);
        RateLimitStatusListener listener2 = mock(RateLimitStatusListener.class);
        twitterBase.onRateLimitStatus(rateLimitEvent -> {
            listener1.onRateLimitStatus(rateLimitEvent);
            listener2.onRateLimitStatus(rateLimitEvent);
        });
        RateLimitStatus rateLimitStatus = new RateLimitStatus(1, 1, 60);
        RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBase, rateLimitStatus, true);
        twitterBase.httpResponseReceived(new HttpResponseEvent(new byte[0], 429, null, null));
        verify(listener1).onRateLimitStatus(rateLimitStatusEvent);
        verify(listener1).onRateLimitReached(rateLimitStatusEvent);
        verify(listener2).onRateLimitStatus(rateLimitStatusEvent);
        verify(listener2).onRateLimitReached(rateLimitStatusEvent);
    }

    @Test
    public void testGetAuthorization_NullAuthorization_ReturnNullAuthorization() {
        assertNotNull(twitterBase.getAuthorization());
        twitterBase.setAuthorization(null);
        assertNull(twitterBase.getAuthorization());
    }

    @Test
    public void testGetConfiguration_ReturnConfiguration() {
        assertNotNull(twitterBase.getConfiguration());
    }

    @Test
    public void testSetOAuthConsumer_ConsumerKeyAndSecret_SetAuthorization() {
        String consumerKey = "testConsumerKey";
        String consumerSecret = "testConsumerSecret";
        twitterBase.setOAuthConsumer(consumerKey, consumerSecret);
        assertNotNull(twitterBase.getAuthorization());
        assertEquals(consumerKey, twitterBase.getAuthorization().getConsumerKey());
        assertEquals(consumerSecret, twitterBase.getAuthorization().getConsumerSecret());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOAuthConsumer_ConsumerKeyNull() {
        String consumerSecret = "testConsumerSecret";
        twitterBase.setOAuthConsumer(null, consumerSecret);
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOAuthConsumer_ConsumerSecretNull() {
        String consumerKey = "testConsumerKey";
        twitterBase.setOAuthConsumer(consumerKey, null);
        fail("IllegalArgumentException expected");
    }
}