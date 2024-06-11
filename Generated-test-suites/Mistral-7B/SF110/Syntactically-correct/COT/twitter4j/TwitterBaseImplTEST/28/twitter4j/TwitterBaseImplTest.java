package twitter4j.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.test.util.TestAuthorization;
import twitter4j.util.function.Function;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private Configuration conf;
    private Authorization auth;

    @Before
    public void setup() {
        conf = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        auth = new TestAuthorization();
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
    }

    @Test
    public void testGetScreenName() {
        Assert.assertNull(twitterBaseImpl.getScreenName());
        twitterBaseImpl.setFactory();
        twitterBaseImpl.fillInIDAndScreenName();
        Assert.assertNotNull(twitterBaseImpl.getScreenName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithDisabledAuth() {
        twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() {
        Assert.assertZero(twitterBaseImpl.getId());
        twitterBaseImpl.setFactory();
        twitterBaseImpl.fillInIDAndScreenName();
        Assert.assertNotNull(twitterBaseImpl.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithDisabledAuth() {
        twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.getId();
    }

    @Test
    public void testFillInIDAndScreenName() {
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.fillInIDAndScreenName();
        Assert.assertNotNull(twitterBaseImpl.getScreenName());
        Assert.assertNotNull(twitterBaseImpl.getId());
    }

    @Test(expected = TwitterException.class)
    public void testFillInIDAndScreenNameWithException() {
        when(auth.isEnabled()).thenReturn(false);
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.fillInIDAndScreenName();
    }

    @Test
    public void testAddRateLimitStatusListener() {
        twitterBaseImpl.addRateLimitStatusListener(mock(RateLimitStatusListener.class));
        Assert.assertFalse(twitterBaseImpl.rateLimitStatusListeners.isEmpty());
    }

    @Test
    public void testOnRateLimitStatus() {
        Function<RateLimitStatusEvent, Void> action = event -> {
            Assert.assertEquals(twitterBaseImpl, event.getSource());
            Assert.assertNotNull(event.getRateLimitStatus());
            return null;
        };
        twitterBaseImpl.onRateLimitStatus(action);
        RateLimitStatus rateLimitStatus = new RateLimitStatus(300, 600);
        RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, rateLimitStatus, true);
        action.apply(event);
    }

    @Test
    public void testOnRateLimitReached() {
        Function<RateLimitStatusEvent, Void> action = event -> {
            Assert.assertEquals(twitterBaseImpl, event.getSource());
            Assert.assertNotNull(event.getRateLimitStatus());
            return null;
        };
        twitterBaseImpl.onRateLimitReached(action);
        RateLimitStatus rateLimitStatus = new RateLimitStatus(300, 600);
        RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, rateLimitStatus, true);
        action.apply(event);
    }

    // Add more test cases as needed
}