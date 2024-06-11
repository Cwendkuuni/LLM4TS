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
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("testUser", "testPassword");
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, auth);
        String screenName = impl.getScreenName();
        assertNull(screenName);
        impl.fillInIDAndScreenName();
        assertNotNull(screenName);
    }

    @Test
    public void testGetId() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("testUser", "testPassword");
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, auth);
        long id = impl.getId();
        assertEquals(-1, id);
        impl.fillInIDAndScreenName();
        assertNotNull(id);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameNoAuth() {
        Configuration conf = new Configuration();
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, null);
        impl.getScreenName();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdNoAuth() {
        Configuration conf = new Configuration();
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, null);
        impl.getId();
    }

    @Test
    public void testFillInIDAndScreenName() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("testUser", "testPassword");
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, auth);
        impl.fillInIDAndScreenName();
        assertNotNull(impl.getScreenName());
        assertNotNull(impl.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("testUser", "testPassword");
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, auth);
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        impl.addRateLimitStatusListener(listener);
        assertTrue(impl.rateLimitStatusListeners.contains(listener));
    }

    @Test
    public void testOnRateLimitStatus() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("testUser", "testPassword");
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, auth);
        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
        impl.onRateLimitStatus(action);
        verify(action).accept(isA(RateLimitStatusEvent.class));
        verify(action).accept(isA(RateLimitStatusEvent.class));
    }

    @Test
    public void testOnRateLimitReached() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("testUser", "testPassword");
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, auth);
        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
        impl.onRateLimitReached(action);
        verify(action).accept(isA(RateLimitStatusEvent.class));
        verify(action).accept(isA(RateLimitStatusEvent.class));
    }

    @Test
    public void testHttpResponseReceived() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("testUser", "testPassword");
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, auth);
        HttpResponseEvent event = mock(HttpResponseEvent.class);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        when(event.getResponse()).thenReturn(new HttpResponse(200, ""));
        when(event.isAuthenticated()).thenReturn(true);
        RateLimitStatusEvent statusEvent = new RateLimitStatusEvent(impl, rateLimitStatus, true);
        impl.onRateLimitStatus(statusEvent);
        impl.onRateLimitReached(statusEvent);
        verify(statusEvent).onRateLimitStatus(isA(RateLimitStatusListener.class));
        verify(statusEvent).onRateLimitReached(isA(RateLimitStatusListener.class));
    }

    @Test
    public void testSetOAuthConsumer() {
        Configuration conf = new Configuration();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, auth);
        String consumerKey = "testConsumerKey";
        String consumerSecret = "testConsumerSecret";
        impl.setOAuthConsumer(consumerKey, consumerSecret);
        assertNotNull(impl.getAuthorization());
        assertTrue(impl.getAuthorization() instanceof OAuthAuthorization);
        OAuthAuthorization auth2 = (OAuthAuthorization)impl.getAuthorization();
        assertEquals(consumerKey, auth2.getConsumerKey());
        assertEquals(consumerSecret, auth2.getConsumerSecret());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetOAuthConsumerAlreadySet() {
        Configuration conf = new Configuration();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, auth);
        String consumerKey = "testConsumerKey";
        String consumerSecret = "testConsumerSecret";
        impl.setOAuthConsumer(consumerKey, consumerSecret);
        impl.setOAuthConsumer(consumerKey, consumerSecret);
    }

    @Test
    public void testGetOAuthRequestToken() {
        Configuration conf = new Configuration();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, auth);
        RequestToken token = impl.getOAuthRequestToken();
        assertNotNull(token);
        assertTrue(token instanceof RequestTokenImpl);
    }

    @Test(expected = TwitterException.class)
    public void testGetOAuthRequestTokenNullAuth() {
        Configuration conf = new Configuration();
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, null);
        impl.getOAuthRequestToken();
    }

    @Test(expected = TwitterException.class)
    public void testGetOAuthAccessTokenNullAuth() {
        Configuration conf = new Configuration();
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, null);
        impl.getOAuthAccessToken();
    }

    @Test
    public void testGetOAuthAccessTokenBasicAuth() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("testUser", "testPassword");
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, auth);
        AccessToken token = impl.getOAuthAccessToken();
        assertNotNull(token);
        assertTrue(token instanceof AccessTokenImpl);
        assertEquals("testUser", token.getScreenName());
        assertEquals(Long.parseLong("1234567890"), token.getId());
    }

    @Test
    public void testGetOAuthAccessTokenXAuth() {
        Configuration conf = new Configuration();
        Authorization auth = new XAuthAuthorization(new BasicAuthorization("testUser", "testPassword"));
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, auth);
        AccessToken token = impl.getOAuthAccessToken();
        assertNotNull(token);
        assertTrue(token instanceof AccessTokenImpl);
        assertEquals("testUser", token.getScreenName());
        assertEquals(Long.parseLong("1234567890"), token.getId());
    }

    @Test
    public void testEquals() {
        Configuration conf1 = new Configuration();
        Authorization auth1 = new BasicAuthorization("testUser1", "testPassword1");
        TwitterBaseImpl impl1 = new TwitterBaseImpl(conf1, auth1);

        Configuration conf2 = new Configuration();
        Authorization auth2 = new BasicAuthorization("testUser2", "testPassword2");
        TwitterBaseImpl impl2 = new TwitterBaseImpl(conf2, auth2);

        Configuration conf3 = new Configuration();
        Authorization auth3 = new BasicAuthorization("testUser1", "testPassword1");
        TwitterBaseImpl impl3 = new TwitterBaseImpl(conf3, auth3);

        Configuration conf4 = new Configuration();
        Authorization auth4 = new XAuthAuthorization(new BasicAuthorization("testUser1", "testPassword1"));
        TwitterBaseImpl impl4 = new TwitterBaseImpl(conf4, auth4);

        Configuration conf5 = new Configuration();
        Authorization auth5 = new OAuthAuthorization(conf5);
        TwitterBaseImpl impl5 = new TwitterBaseImpl(conf5, auth5);

        TwitterBaseImpl impl6 = new TwitterBaseImpl(impl5.getConf(), impl5.getAuthorization());

        assertTrue(impl1.equals(impl1));
        assertTrue(impl1.equals(impl3));
        assertFalse(impl1.equals(impl2));
        assertFalse(impl1.equals(impl4));
        assertFalse(impl1.equals(impl5));
        assertFalse(impl1.equals(impl6));
        assertFalse(impl1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        Configuration conf1 = new Configuration();
        Authorization auth1 = new BasicAuthorization("testUser1", "testPassword1");
        TwitterBaseImpl impl1 = new TwitterBaseImpl(conf1, auth1);

        Configuration conf2 = new Configuration();
        Authorization auth2 = new BasicAuthorization("testUser2", "testPassword2");
        TwitterBaseImpl impl2 = new TwitterBaseImpl(conf2, auth2);

        Configuration conf3 = new Configuration();
        Authorization auth3 = new BasicAuthorization("testUser1", "testPassword1");
        TwitterBaseImpl impl3 = new TwitterBaseImpl(conf3, auth3);

        Configuration conf4 = new Configuration();
        Authorization auth4 = new XAuthAuthorization(new BasicAuthorization("testUser1", "testPassword1"));
        TwitterBaseImpl impl4 = new TwitterBaseImpl(conf4, auth4);

        Configuration conf5 = new Configuration();
        Authorization auth5 = new OAuthAuthorization(conf5);
        TwitterBaseImpl impl5 = new TwitterBaseImpl(conf5, auth5);

        TwitterBaseImpl impl6 = new TwitterBaseImpl(impl5.getConf(), impl5.getAuthorization());

        int hashCode1 = impl1.hashCode();
        int hashCode2 = impl1.hashCode();
        int hashCode3 = impl2.hashCode();
        int hashCode4 = impl3.hashCode();
        int hashCode5 = impl4.hashCode();
        int hashCode6 = impl5.hashCode();
        int hashCode7 = impl6.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode5);
        assertNotEquals(hashCode1, hashCode6);
        assertNotEquals(hashCode1, hashCode7);
    }

    @Test
    public void testToString() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("testUser", "testPassword");
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, auth);
        String expected = "TwitterBase{conf=Configuration{...}, http=null, rateLimitStatusListeners=[], auth=BasicAuthorization{userId=testUser, password=testPassword}}";
        assertEquals(expected, impl.toString());
    }
}