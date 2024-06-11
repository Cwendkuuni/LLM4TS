package twitter4j.test;

import twitter4j.*;
import twitter4j.auth.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;
import org.junit.Assert.*;
import org.junit.Test;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() {
        Configuration conf = new Configuration.Builder().build();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        try {
            Assert.assertNull(twitterBaseImpl.getScreenName());
        } catch (IllegalStateException e) {
            Assert.assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }

        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        auth = new OAuthAuthorization(conf);
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.init();
        try {
            Assert.assertNull(twitterBaseImpl.getScreenName());
            twitterBaseImpl.fillInIDAndScreenName();
            Assert.assertNotNull(twitterBaseImpl.getScreenName());
        } catch (TwitterException | IllegalStateException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetId() {
        Configuration conf = new Configuration.Builder().build();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        try {
            Assert.assertEquals(0L, twitterBaseImpl.getId());
            twitterBaseImpl.fillInIDAndScreenName();
            Assert.assertNotEquals(0L, twitterBaseImpl.getId());
        } catch (TwitterException | IllegalStateException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFillInIDAndScreenName() {
        Configuration conf = new Configuration.Builder().build();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        User user = null;
        try {
            user = twitterBaseImpl.fillInIDAndScreenName();
            Assert.assertNull(user);
        } catch (TwitterException | IllegalStateException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        auth = new OAuthAuthorization(conf);
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.init();

        HttpParameter[] parameters = new HttpParameter[0];
        try {
            user = twitterBaseImpl.fillInIDAndScreenName(parameters);
            Assert.assertNotNull(user);
            Assert.assertNotNull(user.getScreenName());
            Assert.assertNotEquals(0L, user.getId());
        } catch (TwitterException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAddRateLimitStatusListener() {
        Configuration conf = new Configuration.Builder().build();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<>();
        twitterBaseImpl.addRateLimitStatusListener(rateLimitStatus -> {
            Assert.assertTrue(rateLimitStatus.getLimit() > 0);
            Assert.assertTrue(rateLimitStatus.getRemaining() > 0);
        });

        HttpResponseEvent event = new HttpResponseEvent(null, null, null, null);
        twitterBaseImpl.httpResponseReceived(event);
    }

    @Test
    public void testOnRateLimitStatus() {
        Configuration conf = new Configuration.Builder().build();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<>();

        twitterBaseImpl.onRateLimitStatus(event -> {
            Assert.assertNotNull(event);
            Assert.assertNotNull(event.getRateLimitStatus());
            Assert.assertNotNull(event.getTwitterException());
            Assert.assertTrue(event.isAuthenticated());
        });

        HttpResponseEvent event = new HttpResponseEvent(null, null, null, null);
        twitterBaseImpl.httpResponseReceived(event);
    }

    @Test
    public void testOnRateLimitReached() {
        Configuration conf = new Configuration.Builder().build();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<>();

        twitterBaseImpl.onRateLimitReached(event -> {
            Assert.assertNotNull(event);
            Assert.assertNotNull(event.getRateLimitStatus());
            Assert.assertNull(event.getTwitterException());
            Assert.assertTrue(event.isAuthenticated());
        });

        HttpResponseEvent event = new HttpResponseEvent(null, null, null, null);
        event.setStatusCode(420);
        twitterBaseImpl.httpResponseReceived(event);
    }

    @Test
    public void testSetAuthorization() {
        Configuration conf = new Configuration.Builder().build();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl.setAuthorization(new BasicAuthorization("userID", "password"));
        Authorization auth1 = twitterBaseImpl.getAuthorization();
        Assert.assertTrue(auth1 instanceof BasicAuthorization);
        Assert.assertEquals("userID", auth1.getUserId());

        twitterBaseImpl.setAuthorization(new OAuthAuthorization(conf));
        Authorization auth2 = twitterBaseImpl.getAuthorization();
        Assert.assertTrue(auth2 instanceof OAuthAuthorization);
    }

    @Test
    public void testGetOAuthRequestToken() {
        Configuration conf = new Configuration.Builder().build();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        Assert.assertNotNull(requestToken);
        Assert.assertNotNull(requestToken.getToken());
        Assert.assertNotNull(requestToken.getTokenSecret());
    }

    @Test
    public void testGetOAuthAccessToken() {
        Configuration conf = new Configuration.Builder().build();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
        Assert.assertNull(accessToken);

        twitterBaseImpl.setAuthorization(new OAuthAuthorization(conf));
        twitterBaseImpl.init();

        AccessToken accessToken1 = twitterBaseImpl.getOAuthAccessToken();
        Assert.assertNotNull(accessToken1);
        Assert.assertNotNull(accessToken1.getScreenName());
        Assert.assertNotEquals(0L, accessToken1.getId());

        twitterBaseImpl.setAuthorization(new XAuthAuthorization(new BasicAuthorization("userID", "password"), conf));
        twitterBaseImpl.init();

        AccessToken accessToken2 = twitterBaseImpl.getOAuthAccessToken();
        Assert.assertNotNull(accessToken2);
        Assert.assertNotNull(accessToken2.getScreenName());
        Assert.assertNotEquals(0L, accessToken2.getId());
    }
}