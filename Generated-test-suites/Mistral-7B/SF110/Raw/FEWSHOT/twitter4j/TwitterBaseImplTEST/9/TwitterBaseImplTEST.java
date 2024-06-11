package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

import twitter4j.auth.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() {
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        String screenName = twitterBaseImpl.getScreenName();
        assertNull(screenName);

        twitterBaseImpl.fillInIDAndScreenName();
        screenName = twitterBaseImpl.getScreenName();
        assertNotNull(screenName);

        twitterBaseImpl = new TwitterBaseImpl(conf, null);
        try {
            twitterBaseImpl.getScreenName();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            //expected
        }
    }

    @Test
    public void testGetId() {
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        long id = twitterBaseImpl.getId();
        assertEquals(0, id);

        twitterBaseImpl.fillInIDAndScreenName();
        id = twitterBaseImpl.getId();
        assertNotEquals(0, id);

        twitterBaseImpl = new TwitterBaseImpl(conf, null);
        try {
            twitterBaseImpl.getId();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            //expected
        }
    }

    @Test
    public void testAddRateLimitStatusListener() {
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        List<RateLimitStatusListener> listeners = twitterBaseImpl.rateLimitStatusListeners;
        assertEquals(0, listeners.size());

        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        listeners = twitterBaseImpl.rateLimitStatusListeners;
        assertEquals(1, listeners.size());
        verify(listener).onRateLimitStatus(any(RateLimitStatusEvent.class));
        verify(listener).onRateLimitReached(any(RateLimitStatusEvent.class));

        twitterBaseImpl.onRateLimitStatus(mock(Consumer.class));
        verify(listener).onRateLimitStatus(any(RateLimitStatusEvent.class));
        verifyZeroInteractions(listener);

        twitterBaseImpl.onRateLimitReached(mock(Consumer.class));
        verify(listener).onRateLimitReached(any(RateLimitStatusEvent.class));
        verifyZeroInteractions(listener);
    }

    @Test
    public void testOnRateLimitStatus() {
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        List<RateLimitStatusListener> listeners = twitterBaseImpl.rateLimitStatusListeners;
        assertEquals(0, listeners.size());

        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.onRateLimitStatus(listener);
        listeners = twitterBaseImpl.rateLimitStatusListeners;
        assertEquals(1, listeners.size());
        verify(listener).onRateLimitStatus(any(RateLimitStatusEvent.class));
        verifyZeroInteractions(listener);

        RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
        twitterBaseImpl.onRateLimitStatus(event);
        verify(listener).onRateLimitStatus(event);
    }

    @Test
    public void testSetOAuthConsumer() {
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        assertNull(twitterBaseImpl.auth);

        twitterBaseImpl.setOAuthConsumer("newConsumerKey", "newConsumerSecret");
        assertNotNull(twitterBaseImpl.auth);
        assertTrue(twitterBaseImpl.auth instanceof OAuthAuthorization);

        OAuthAuthorization oauthAuthorization = (OAuthAuthorization) twitterBaseImpl.auth;
        assertEquals("newConsumerKey", oauthAuthorization.getConsumerKey());
        assertEquals("newConsumerSecret", oauthAuthorization.getConsumerSecret());

        twitterBaseImpl.setOAuthConsumer("newConsumerKey2", "newConsumerSecret2");
        OAuthAuthorization newOAuthAuthorization = (OAuthAuthorization) twitterBaseImpl.auth;
        assertEquals("newConsumerKey2", newOAuthAuthorization.getConsumerKey());
        assertEquals("newConsumerSecret2", newOAuthAuthorization.getConsumerSecret());
    }
}