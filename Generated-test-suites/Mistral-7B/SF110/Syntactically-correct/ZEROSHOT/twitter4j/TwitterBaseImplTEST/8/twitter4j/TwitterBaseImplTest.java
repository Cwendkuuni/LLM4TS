import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.ConfigurationImpl;
import twitter4j.util.function.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private ConfigurationImpl configuration;
    private Authorization authorization;

    @Before
    public void setUp() {
        configuration = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .build();
        authorization = Mockito.mock(Authorization.class);
        twitterBaseImpl = new TwitterBaseImpl(configuration, authorization);
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Mockito.when(authorization.isEnabled()).thenReturn(true);
        String screenName = twitterBaseImpl.getScreenName();
        Assert.assertNotNull(screenName);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithDisabledAuthorization() throws TwitterException, IllegalStateException {
        Mockito.when(authorization.isEnabled()).thenReturn(false);
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Mockito.when(authorization.isEnabled()).thenReturn(true);
        long id = twitterBaseImpl.getId();
        Assert.assertNotEquals(0, id);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithDisabledAuthorization() throws TwitterException, IllegalStateException {
        Mockito.when(authorization.isEnabled()).thenReturn(false);
        twitterBaseImpl.getId();
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = Mockito.mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        List<RateLimitStatusListener> rateLimitStatusListeners = twitterBaseImpl.getRateLimitStatusListeners();
        Assert.assertTrue(rateLimitStatusListeners.contains(listener));
    }

    @Test
    public void testOnRateLimitStatus() {
        Consumer<RateLimitStatusEvent> action = Mockito.mock(Consumer.class);
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                action.accept(event);
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {

            }
        };
        twitterBaseImpl.onRateLimitStatus(action);
        List<RateLimitStatusListener> rateLimitStatusListeners = twitterBaseImpl.getRateLimitStatusListeners();
        Assert.assertEquals(1, rateLimitStatusListeners.size());
        Assert.assertTrue(rateLimitStatusListeners.contains(listener));
    }

    @Test
    public void testHttpResponseReceived() throws TwitterException {
        HttpResponseEvent httpResponseEvent = Mockito.mock(HttpResponseEvent.class);
        RateLimitStatus rateLimitStatus = Mockito.mock(RateLimitStatus.class);
        Mockito.when(httpResponseEvent.getRateLimitStatus()).thenReturn(rateLimitStatus);
        RateLimitStatusEvent statusEvent = new RateLimitStatusEvent(twitterBaseImpl, rateLimitStatus, true);
        twitterBaseImpl.onRateLimitStatus(statusEvent);
        List<RateLimitStatusListener> rateLimitStatusListeners = twitterBaseImpl.getRateLimitStatusListeners();
        for (RateLimitStatusListener listener : rateLimitStatusListeners) {
            listener.onRateLimitStatus(statusEvent);
            listener.onRateLimitReached(statusEvent);
        }
    }

    @Test
    public void testGetAuthorization() {
        Assert.assertEquals(authorization, twitterBaseImpl.getAuthorization());
    }

    @Test
    public void testGetConfiguration() {
        Assert.assertEquals(configuration, twitterBaseImpl.getConfiguration());
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        Mockito.when(authorization.isEnabled()).thenReturn(true);
        User user = twitterBaseImpl.fillInIDAndScreenName();
        Assert.assertNotNull(user);
    }

    @Test(expected = TwitterException.class)
    public void testFillInIDAndScreenNameWithDisabledAuthorization() throws TwitterException {
        Mockito.when(authorization.isEnabled()).thenReturn(false);
        twitterBaseImpl.fillInIDAndScreenName();
    }

    @Test
    public void testAddRateLimitStatusListeners() {
        RateLimitStatusListener listener1 = Mockito.mock(RateLimitStatusListener.class);
        RateLimitStatusListener listener2 = Mockito.mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener1);
        twitterBaseImpl.addRateLimitStatusListener(listener2);
        List<RateLimitStatusListener> rateLimitStatusListeners = twitterBaseImpl.getRateLimitStatusListeners();
        Assert.assertEquals(2, rateLimitStatusListeners.size());
        Assert.assertTrue(rateLimitStatusListeners.contains(listener1));
        Assert.assertTrue(rateLimitStatusListeners.contains(listener2));
    }

    @Test
    public void testSetOAuthConsumer() {
        String consumerKey = "consumerKey";
        String consumerSecret = "consumerSecret";
        twitterBaseImpl.setOAuthConsumer(consumerKey, consumerSecret);
        Authorization auth = twitterBaseImpl.getAuthorization();
        OAuthAuthorization oAuthAuthorization = (OAuthAuthorization) auth;
        Assert.assertEquals(consumerKey, oAuthAuthorization.getConsumerKey());
        Assert.assertEquals(consumerSecret, oAuthAuthorization.getConsumerSecret());
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        String callbackUrl = "callbackUrl";
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken(callbackUrl);
        Assert.assertNotNull(requestToken);
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
        Assert.assertNotNull(accessToken);
    }

    @Test
    public void testSetOAuthAccessToken() {
        AccessToken accessToken = Mockito.mock(AccessToken.class);
        twitterBaseImpl.setOAuthAccessToken(accessToken);
        AccessToken storedAccessToken = twitterBaseImpl.getOAuthAccessToken();
        Assert.assertEquals(accessToken, storedAccessToken);
    }
}