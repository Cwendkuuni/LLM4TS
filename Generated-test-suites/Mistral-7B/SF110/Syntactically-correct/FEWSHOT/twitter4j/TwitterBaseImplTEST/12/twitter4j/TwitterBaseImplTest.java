package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;
import twitter4j.conf.*;
import twitter4j.util.function.*;

public class TwitterBaseImplTest {

    @Test
    public void testInit() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertNotNull(twitterBaseImpl.getHttp());
        assertEquals(conf, twitterBaseImpl.getConfiguration());
        assertNull(twitterBaseImpl.getScreenName());
        assertNull(twitterBaseImpl.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testInitWithoutAuth() {
        Configuration conf = new Configuration();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        throw new IllegalStateException("Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
    }

    @Test
    public void testSetFactory() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.setFactory();
        assertNotNull(twitterBaseImpl.getFactory());
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertNull(twitterBaseImpl.getScreenName());
        twitterBaseImpl.init();
        assertNotNull(twitterBaseImpl.getScreenName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithoutInit() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertNull(twitterBaseImpl.getScreenName());
        twitterBaseImpl.setFactory();
        throw new IllegalStateException("Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertNull(twitterBaseImpl.getId());
        twitterBaseImpl.init();
        assertNotNull(twitterBaseImpl.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithoutInit() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertNull(twitterBaseImpl.getId());
        twitterBaseImpl.setFactory();
        throw new IllegalStateException("Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        Configuration conf = new Configuration();
        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        User user = twitterBaseImpl.fillInIDAndScreenName();
        assertNotNull(user);
        assertNotNull(user.getScreenName());
        assertNotNull(user.getId());
    }

    @Test(expected = TwitterException.class)
    public void testFillInIDAndScreenNameWithoutInit() throws TwitterException {
        Configuration conf = new Configuration();
        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.setFactory();
        twitterBaseImpl.fillInIDAndScreenName();
        throw new TwitterException("Authentication credentials are missing.");
    }

    @Test
    public void testAddRateLimitStatusListener() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        assertTrue(twitterBaseImpl.getRateLimitStatusListeners().contains(listener));
    }

    @Test
    public void testHttpResponseReceived() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        HttpResponseEvent event = new HttpResponseEvent(new HttpResponse(200, ""), null);
        twitterBaseImpl.httpResponseReceived(event);
        verifyNoMoreInteractions(twitterBaseImpl.getRateLimitStatusListeners());

        RateLimitStatus rateLimitStatus = new RateLimitStatus(100, 15, 200);
        HttpResponseEvent eventWithRateLimitStatus = new HttpResponseEvent(new HttpResponse(200, ""), rateLimitStatus, true);
        twitterBaseImpl.httpResponseReceived(eventWithRateLimitStatus);
        for (RateLimitStatusListener listener : twitterBaseImpl.getRateLimitStatusListeners()) {
            verify(listener).onRateLimitStatus(eq(new RateLimitStatusEvent(twitterBaseImpl, rateLimitStatus, true)));
            verify(listener).onRateLimitReached(eq(new RateLimitStatusEvent(twitterBaseImpl, rateLimitStatus, true)));
        }
    }

    @Test
    public void testSetOAuthConsumer() {
        Configuration conf = new Configuration();
        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.setOAuthConsumer("newConsumerKey", "newConsumerSecret");
        assertTrue(twitterBaseImpl.getAuthorization() instanceof OAuthAuthorization);
        OAuthAuthorization oauthAuthorization = (OAuthAuthorization) twitterBaseImpl.getAuthorization();
        assertEquals("newConsumerKey", oauthAuthorization.getConsumerKey());
        assertEquals("newConsumerSecret", oauthAuthorization.getConsumerSecret());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOAuthConsumerWithNullConsumerKey() {
        Configuration conf = new Configuration();
        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.setOAuthConsumer(null, "consumerSecret");
        throw new IllegalArgumentException("consumer key is null");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOAuthConsumerWithNullConsumerSecret() {
        Configuration conf = new Configuration();
        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.setOAuthConsumer("consumerKey", null);
        throw new IllegalArgumentException("consumer secret is null");
    }

    @Test(expected = IllegalStateException.class)
    public void testSetOAuthConsumerWithEnabledAuthorization() {
        Configuration conf = new Configuration();
        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.setOAuthConsumer("newConsumerKey", "newConsumerSecret");
        throw new IllegalStateException("consumer key/secret pair already set.");
    }
}