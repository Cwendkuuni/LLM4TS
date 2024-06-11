package twitter4j;

import static org.junit.Assert.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import org.junit.Test;

public class TwitterBaseImplTest {

    @Test
    public void testEquals() {
        Configuration conf1 = new Configuration();
        Configuration conf2 = new Configuration();
        TwitterBaseImpl impl1 = new TwitterBaseImpl(conf1, null);
        TwitterBaseImpl impl2 = new TwitterBaseImpl(conf2, null);
        assertFalse(impl1.equals(impl2));

        impl1.setAuthorization(new BasicAuthorization("consumerKey1", "consumerSecret1"));
        impl2.setAuthorization(new BasicAuthorization("consumerKey1", "consumerSecret1"));

        assertTrue(impl1.equals(impl2));

        impl1.setConfiguration(new Configuration().setHttpClientConfiguration(new HttpClientConfiguration().setMaxRetries(1)));
        impl2.setConfiguration(new Configuration().setHttpClientConfiguration(new HttpClientConfiguration().setMaxRetries(1)));

        assertTrue(impl1.equals(impl2));

        impl1.setHttp(HttpClientFactory.getInstance(conf1.getHttpClientConfiguration()));
        impl2.setHttp(HttpClientFactory.getInstance(conf2.getHttpClientConfiguration()));

        assertTrue(impl1.equals(impl2));

        impl1.setRateLimitStatusListeners(Collections.singletonList(new RateLimitStatusListener() {}));
        impl2.setRateLimitStatusListeners(Collections.singletonList(new RateLimitStatusListener() {}));

        assertTrue(impl1.equals(impl2));

        impl1.setAuthorization(new OAuthAuthorization("consumerKey2", "consumerSecret2"));
        assertFalse(impl1.equals(impl2));
    }

    @Test
    public void testGetAuthorization() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, auth);

        assertEquals(auth, impl.getAuthorization());

        impl.setAuthorization(new OAuthAuthorization("consumerKey2", "consumerSecret2"));
        assertEquals(new OAuthAuthorization("consumerKey2", "consumerSecret2"), impl.getAuthorization());
    }

    @Test
    public void testGetConfiguration() {
        Configuration conf = new Configuration();
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, null);

        assertEquals(conf, impl.getConfiguration());

        impl.setConfiguration(new Configuration().setHttpClientConfiguration(new HttpClientConfiguration().setMaxRetries(1)));
        assertEquals(new Configuration().setHttpClientConfiguration(new HttpClientConfiguration().setMaxRetries(1)), impl.getConfiguration());
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, auth);

        assertNull(impl.getScreenName());

        impl.setAuthorization(new BasicAuthorization("1234567890@example.com"));
        assertEquals("1234567890", impl.getScreenName());

        impl.setAuthorization(new OAuthAuthorization("consumerKey", "consumerSecret"));
        impl.fillInIDAndScreenName();
        User user = impl.fillInIDAndScreenName();
        assertEquals("1234567890", impl.getScreenName());
        assertEquals(1, user.getId());
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, auth);

        assertNull(impl.getId());

        impl.setAuthorization(new BasicAuthorization("1234567890@example.com"));
        assertEquals(1, impl.getId());

        impl.setAuthorization(new OAuthAuthorization("consumerKey", "consumerSecret"));
        impl.fillInIDAndScreenName();
        User user = impl.fillInIDAndScreenName();
        assertEquals(user.getId(), impl.getId());
    }

    @Test
    public void testHttpResponseReceived() {
        TwitterBaseImpl impl = new TwitterBaseImpl(new Configuration(), null);
        impl.setRateLimitStatusListeners(new ArrayList<RateLimitStatusListener>() {{
            add(new RateLimitStatusListener() {
                @Override
                public void onRateLimitStatus(RateLimitStatusEvent event) {
                    assertEquals(event.getLimit(), 15);
                    assertEquals(event.getRemaining(), 14);
                    assertEquals(event.getReset(), 25 * 60 * 1000L);
                }

                @Override
                public void onRateLimitReached(RateLimitStatusEvent event) {
                    fail("Rate limit reached should not be called");
                }
            });
        }});

        HttpResponseEvent event = new HttpResponseEvent(null, null, 200, null, null);
        impl.httpResponseReceived(event);
    }
}