package twitter4j;

import static org.junit.Assert.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import org.junit.Test;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("user", "password");
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth);
        assertEquals("user", twitterBase.getScreenName());

        // Test when screenName is null
        twitterBase = new TwitterBaseImpl(conf, null);
        assertNull(twitterBase.getScreenName());

        // Test when auth is null
        twitterBase = new TwitterBaseImpl(conf, null);
        assertNull(twitterBase.getScreenName());
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("user", "password");
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth);
        assertEquals(123456789L, twitterBase.getId());

        // Test when id is 0
        twitterBase = new TwitterBaseImpl(conf, auth);
        assertEquals(0L, twitterBase.getId());
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("user", "password");
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth);
        User user = twitterBase.fillInIDAndScreenName();
        assertEquals("user", user.getScreenName());
        assertEquals(123456789L, user.getId());
    }

    @Test
    public void testFillInIDAndScreenNameWithParameters() throws TwitterException {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("user", "password");
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth);
        HttpParameter[] parameters = new HttpParameter[0];
        User user = twitterBase.fillInIDAndScreenName(parameters);
        assertEquals("user", user.getScreenName());
        assertEquals(123456789L, user.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("user", "password");
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth);
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBase.addRateLimitStatusListener(listener);
        assertEquals(1, twitterBase.getRateLimitStatusListeners().size());
    }

    @Test
    public void testOnRateLimitStatus() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("user", "password");
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth);
        Consumer<RateLimitStatusEvent> action = arg -> {
            assertTrue(arg.isAuthenticated());
        };
        twitterBase.onRateLimitStatus(action);
        // Verify that the rate limit status listener was called
        // when a rate limit status event is received
        // This cannot be tested directly in the test case
        // as the HttpResponseReceiver implementation is not available here
        // To test this, you can create a mock HttpResponseReceiver
        // and use a mock HttpClient to send requests and receive responses
        // and verify that the onRateLimitStatus method is called with the correct arguments
    }

    @Test
    public void testOnRateLimitReached() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("user", "password");
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth);
        Consumer<RateLimitStatusEvent> action = arg -> {
            assertTrue(arg.isAuthenticated());
        };
        twitterBase.onRateLimitReached(action);
        // Verify that the rate limit status listener was called
        // when a rate limit is reached
        // This cannot be tested directly in the test case
        // as the HttpResponseReceiver implementation is not available here
        // To test this, you can create a mock HttpResponseReceiver
        // and use a mock HttpClient to send requests and receive responses
        // and verify that the onRateLimitReached method is called with the correct arguments
    }

    @Test
    public void testHttpResponseReceived() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("user", "password");
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth);
        List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<>();
        twitterBase.addRateLimitStatusListener(rateLimitStatusListeners::add);
        HttpResponseEvent event = mock(HttpResponseEvent.class);
        RateLimitStatus status = mock(RateLimitStatus.class);
        when(status.getLimit()).thenReturn(60);
        when(status.getUsed()).thenReturn(10);
        when(status.getReset()).thenReturn(1586451526000L);
        when(event.getRateLimitStatus()).thenReturn(status);
        twitterBase.httpResponseReceived(event);
        assertEquals(1, rateLimitStatusListeners.size());
        RateLimitStatusEvent statusEvent = rateLimitStatusListeners.get(0);
        assertEquals(twitterBase, statusEvent.getTwitterBase());
        assertEquals(status, statusEvent.getRateLimitStatus());
        assertTrue(statusEvent.isAuthenticated());

        // Test when status code is not a rate limit status code
        when(event.getTwitterException()).thenReturn(new TwitterException("Error message", 403));
        twitterBase.httpResponseReceived(event);
        assertEquals(0, rateLimitStatusListeners.size());
    }

    @Test
    public void testGetAuthorization() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("user", "password");
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth);
        assertEquals(auth, twitterBase.getAuthorization());
    }

    @Test
    public void testGetConfiguration() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("user", "password");
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth);
        assertEquals(conf, twitterBase.getConfiguration());
    }

    @Test
    public void testEnsureAuthorizationEnabled() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("user", "password");
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth);
        twitterBase.ensureAuthorizationEnabled();

        // Test when auth is null
        twitterBase = new TwitterBaseImpl(conf, null);
        try {
            twitterBase.ensureAuthorizationEnabled();
            fail("An IllegalStateException should have been thrown");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testEnsureOAuthEnabled() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("user", "password");
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth);
        twitterBase.ensureOAuthEnabled();

        // Test when auth is not an OAuthAuthorization or OAuth2Authorization
        twitterBase = new TwitterBaseImpl(conf, new NullAuthorization());
        try {
            twitterBase.ensureOAuthEnabled();
            fail("An IllegalStateException should have been thrown");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testWriteObject() throws IOException {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("user", "password");
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(outputStream);
        output.writeObject(twitterBase);
        byte[] bytes = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        TwitterBaseImpl deserializedTwitterBase = (TwitterBaseImpl)inputStream.readObject();
        assertEquals(twitterBase, deserializedTwitterBase);
    }

    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("user", "password");
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(outputStream);
        output.writeObject(twitterBase);
        byte[] bytes = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        TwitterBaseImpl deserializedTwitterBase = (TwitterBaseImpl)inputStream.readObject();
        assertEquals(twitterBase, deserializedTwitterBase);
    }

    @Test
    public void testSetOAuthConsumer() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new BasicAuthorization("user", "password");
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth);
        twitterBase.setOAuthConsumer("consumerKey", "consumerSecret");
        assertEquals("consumerKey", twitterBase.getAuthorization().getConsumerKey());
        assertEquals("consumerSecret", twitterBase.getAuthorization().getConsumerSecret());

        // Test when consumerKey or consumerSecret are null
        twitterBase = new TwitterBaseImpl(conf, auth);
        try {
            twitterBase.setOAuthConsumer(null, "consumerSecret");
            fail("An NullPointerException should have been thrown");
        } catch (NullPointerException e) {
            // Expected
        }

        twitterBase = new TwitterBaseImpl(conf, auth);
        try {
            twitterBase.setOAuthConsumer("consumerKey", null);
            fail("An NullPointerException should have been thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}