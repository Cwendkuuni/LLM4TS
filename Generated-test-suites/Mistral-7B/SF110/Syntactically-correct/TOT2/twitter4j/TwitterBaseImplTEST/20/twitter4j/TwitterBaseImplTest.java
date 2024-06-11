import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.auth.Authorization;
import twitter4j.TwitterBase;
import twitter4j.impl.TwitterBaseImpl;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertEquals("consumerKey", twitterBaseImpl.getScreenName());

        twitterBaseImpl = new TwitterBaseImpl(conf, null);
        assertNull(twitterBaseImpl.getScreenName());

        twitterBaseImpl = new TwitterBaseImpl(null, auth);
        assertNull(twitterBaseImpl.getScreenName());

        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.ensureAuthorizationEnabled();
        twitterBaseImpl.fillInIDAndScreenName();
        assertEquals("userScreenName", twitterBaseImpl.getScreenName());
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.ensureAuthorizationEnabled();
        twitterBaseImpl.fillInIDAndScreenName();
        assertEquals(12345678L, twitterBaseImpl.getId());

        twitterBaseImpl = new TwitterBaseImpl(conf, null);
        assertNull(twitterBaseImpl.getId());

        twitterBaseImpl = new TwitterBaseImpl(null, auth);
        assertNull(twitterBaseImpl.getId());

        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.ensureOAuthEnabled();
        User user = twitterBaseImpl.fillInIDAndScreenName();
        assertEquals(user.getId(), twitterBaseImpl.getId());
    }

    @Test(expected = TwitterException.class)
    public void testGetScreenNameWithNoAuthorization() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.getScreenName();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithNoUserID() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.ensureAuthorizationEnabled();
        twitterBaseImpl.fillInIDAndScreenName(new HttpParameter[] { new HttpParameter("foo", "bar") });
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testAddRateLimitStatusListener() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new BasicAuthorization("consumerKey", "consumerSecret"));
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        assertTrue(twitterBaseImpl.rateLimitStatusListeners.contains(listener));
    }

    @Test
    public void testHttpResponseReceived() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new BasicAuthorization("consumerKey", "consumerSecret"));
        HttpResponseEvent event = mock(HttpResponseEvent.class);
        twitterBaseImpl.httpResponseReceived(event);
        verify(event).isAuthenticated();
    }
}