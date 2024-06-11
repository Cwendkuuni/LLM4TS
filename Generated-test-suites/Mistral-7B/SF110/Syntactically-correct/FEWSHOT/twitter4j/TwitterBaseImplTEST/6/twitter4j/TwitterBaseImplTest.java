package twitter4j;

import static org.junit.Assert.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;
import twitter4j.auth.*;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertEquals(null, twitterBaseImpl.getScreenName());

        twitterBaseImpl.fillInIDAndScreenName();
        assertNotNull(twitterBaseImpl.getScreenName());
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertEquals(0L, twitterBaseImpl.getId());

        twitterBaseImpl.fillInIDAndScreenName();
        assertNotNull(twitterBaseImpl.getId());
    }

    @Test
    public void testGetConfiguration() {
        Configuration configuration = new Configuration();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(configuration, null);
        assertEquals(configuration, twitterBaseImpl.getConfiguration());
    }

    @Test
    public void testSetOAuthConsumer() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
        assertNotNull(twitterBaseImpl.getAuthorization());
        assertTrue(twitterBaseImpl.getAuthorization() instanceof OAuthAuthorization);
        assertEquals("consumerKey", twitterBaseImpl.getAuthorization().getConsumerKey());
        assertEquals("consumerSecret", twitterBaseImpl.getAuthorization().getConsumerSecret());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetOAuthConsumerWithNullConsumerKey() {
        Configuration conf = new Configuration();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl.setOAuthConsumer(null, "consumerSecret");
    }

    @Test(expected = IllegalStateException.class)
    public void testSetOAuthConsumerWithNullConsumerSecret() {
        Configuration conf = new Configuration();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl.setOAuthConsumer("consumerKey", null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetOAuthConsumerWithBasicAuthorization() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("userId", "password");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        assertNotNull(requestToken);
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken);
        assertNotNull(accessToken);
    }

    @Test
    public void testEquals() {
        Configuration conf1 = new Configuration();
        Configuration conf2 = new Configuration();
        Authorization auth1 = new OAuthAuthorization(conf1);
        Authorization auth2 = new OAuthAuthorization(conf2);
        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf1, auth1);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf2, auth2);

        assertEquals(twitterBaseImpl1, twitterBaseImpl1);
        assertEquals(twitterBaseImpl1, twitterBaseImpl1);
        assertEquals(twitterBaseImpl1, twitterBaseImpl2);
        assertNotEquals(twitterBaseImpl1, new Object());
    }

    @Test
    public void testHashCode() {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        TwitterBaseImpl twitterBaseImplCopy = new TwitterBaseImpl(conf, auth);

        int hashCode1 = twitterBaseImpl.hashCode();
        int hashCode2 = twitterBaseImplCopy.hashCode();
        assertEquals(hashCode1, hashCode2);
    }
}