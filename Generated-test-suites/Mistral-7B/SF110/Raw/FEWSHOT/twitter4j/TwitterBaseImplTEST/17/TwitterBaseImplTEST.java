package twitter4j;

import static org.junit.Assert.*;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;
import org.junit.Test;

public class TwitterBaseImplTest {

    @Test
    public void testInitializeWithBasicAuthorization() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertNotNull(twitterBaseImpl.getAuthorization());
        assertNotNull(twitterBaseImpl.getHttp());
        assertNotNull(twitterBaseImpl.getFactory());
        assertNull(twitterBaseImpl.getScreenName());
        assertNull(twitterBaseImpl.getId());
    }

    @Test
    public void testInitializeWithOAuthAuthorization() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new OAuthAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertNotNull(twitterBaseImpl.getAuthorization());
        assertNotNull(twitterBaseImpl.getHttp());
        assertNotNull(twitterBaseImpl.getFactory());
        assertNull(twitterBaseImpl.getScreenName());
        assertNull(twitterBaseImpl.getId());
    }

    @Test
    public void testInitializeWithOAuth2Authorization() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new OAuth2Authorization("clientId", "clientSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertNotNull(twitterBaseImpl.getAuthorization());
        assertNotNull(twitterBaseImpl.getHttp());
        assertNotNull(twitterBaseImpl.getFactory());
        assertNull(twitterBaseImpl.getScreenName());
        assertNull(twitterBaseImpl.getId());
    }

    @Test
    public void testInitializeWithoutAuthorization() {
        Configuration conf = new ConfigurationBuilder().build();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        assertNull(twitterBaseImpl.getAuthorization());
        assertNotNull(twitterBaseImpl.getHttp());
        assertNotNull(twitterBaseImpl.getFactory());
        assertNull(twitterBaseImpl.getScreenName());
        assertNull(twitterBaseImpl.getId());
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertEquals(null, twitterBaseImpl.getScreenName());
        twitterBaseImpl.fillInIDAndScreenName();
        assertNotNull(twitterBaseImpl.getScreenName());
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertEquals(0L, twitterBaseImpl.getId());
        twitterBaseImpl.fillInIDAndScreenName();
        assertNotNull(twitterBaseImpl.getId());
    }

    @Test
    public void testGetUser() throws TwitterException {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        User user = twitterBaseImpl.fillInIDAndScreenName();
        assertNotNull(user);
        assertNotNull(user.getScreenName());
        assertNotNull(user.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        assertTrue(twitterBaseImpl.getRateLimitStatusListeners().contains(listener));
    }

    @Test
    public void testOnRateLimitStatus() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        Consumer<RateLimitStatusEvent> action = arg -> {};
        twitterBaseImpl.onRateLimitStatus(action);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        RateLimitStatus status = mock(RateLimitStatus.class);
        RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, status, true);
        listener.onRateLimitStatus(event);
        listener.onRateLimitReached(event);
        verify(listener, times(2)).onRateLimitStatus(eq(event));
    }

    @Test
    public void testWriteObject() throws IOException {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        twitterBaseImpl.writeObject(out);
        byte[] bytes = bos.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        TwitterBaseImpl twitterBaseImpl2 = (TwitterBaseImpl)bis.readObject();
        assertNotNull(twitterBaseImpl2);
        assertEquals(twitterBaseImpl.getAuth(), twitterBaseImpl2.getAuth());
        assertEquals(twitterBaseImpl.getConf(), twitterBaseImpl2.getConf());
    }

    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(twitterBaseImpl);
        byte[] bytes = bos.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        TwitterBaseImpl twitterBaseImpl2 = (TwitterBaseImpl)bis.readObject();
        assertEquals(twitterBaseImpl, twitterBaseImpl2);
    }

    @Test
    public void testSetOAuthConsumer() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.setOAuthConsumer("newConsumerKey", "newConsumerSecret");
        assertNotNull(twitterBaseImpl.getAuthorization());
        assertTrue(twitterBaseImpl.getAuthorization() instanceof OAuthAuthorization);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetOAuthConsumerTwice() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        assertNotNull(requestToken);
        assertNotNull(requestToken.getToken());
        assertNotNull(requestToken.getTokenSecret());
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
        assertNotNull(accessToken);
        assertNotNull(accessToken.getToken());
        assertNotNull(accessToken.getTokenSecret());
    }

    @Test
    public void testSetOAuthAccessToken() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        AccessToken accessToken = new AccessToken("token", "tokenSecret");
        twitterBaseImpl.setOAuthAccessToken(accessToken);
        assertEquals(accessToken.getToken(), twitterBaseImpl.getScreenName());
        assertEquals(accessToken.getTokenSecret(), twitterBaseImpl.getId());
    }
}