package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;
import twitter4j.auth.*;

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

        impl1.setAuthorization(new BasicAuthorization("consumerKey2", "consumerSecret2"));
        assertFalse(impl1.equals(impl2));

        impl1.setConfiguration(conf2);
        assertTrue(impl1.equals(impl2));

        impl1.http = HttpClientFactory.getInstance(conf1.getHttpClientConfiguration());
        impl2.http = HttpClientFactory.getInstance(conf2.getHttpClientConfiguration());
        assertTrue(impl1.equals(impl2));

        List<RateLimitStatusListener> listeners1 = new ArrayList<>();
        List<RateLimitStatusListener> listeners2 = new ArrayList<>();
        impl1.addRateLimitStatusListener(listeners1::println);
        impl2.addRateLimitStatusListener(listeners2::println);
        assertTrue(impl1.equals(impl2));
    }

    @Test
    public void testHashCode() {
        Configuration conf1 = new Configuration();
        Configuration conf2 = new Configuration();
        TwitterBaseImpl impl1 = new TwitterBaseImpl(conf1, null);
        TwitterBaseImpl impl2 = new TwitterBaseImpl(conf2, null);
        int hash1 = impl1.hashCode();
        int hash2 = impl2.hashCode();
        assertNotEquals(hash1, hash2);

        impl1.setAuthorization(new BasicAuthorization("consumerKey1", "consumerSecret1"));
        impl2.setAuthorization(new BasicAuthorization("consumerKey1", "consumerSecret1"));
        int hash3 = impl1.hashCode();
        int hash4 = impl2.hashCode();
        assertEquals(hash3, hash4);

        impl1.setAuthorization(new BasicAuthorization("consumerKey2", "consumerSecret2"));
        int hash5 = impl1.hashCode();
        int hash6 = impl2.hashCode();
        assertNotEquals(hash5, hash6);

        impl1.setConfiguration(conf2);
        int hash7 = impl1.hashCode();
        assertEquals(hash7, hash6);

        impl1.http = HttpClientFactory.getInstance(conf1.getHttpClientConfiguration());
        impl2.http = HttpClientFactory.getInstance(conf2.getHttpClientConfiguration());
        int hash8 = impl1.hashCode();
        assertEquals(hash8, hash6);

        List<RateLimitStatusListener> listeners1 = new ArrayList<>();
        List<RateLimitStatusListener> listeners2 = new ArrayList<>();
        impl1.addRateLimitStatusListener(listeners1::println);
        impl2.addRateLimitStatusListener(listeners2::println);
        int hash9 = impl1.hashCode();
        assertEquals(hash9, hash6);

        impl1.addRateLimitStatusListener(new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
            }
        });
        int hash10 = impl1.hashCode();
        assertNotEquals(hash9, hash10);
    }

    @Test
    public void testGetConfiguration() {
        Configuration conf = new Configuration();
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, null);
        assertEquals(conf, impl.getConfiguration());
    }

    @Test
    public void testSetOAuthConsumer() {
        Configuration conf = new Configuration();
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, null);

        String consumerKey = "consumerKey";
        String consumerSecret = "consumerSecret";

        try {
            impl.setOAuthConsumer(consumerKey, consumerSecret);
            assertEquals(consumerKey, impl.getAuthorization().getConsumerKey());
            assertEquals(consumerSecret, impl.getAuthorization().getConsumerSecret());
        } catch (IllegalStateException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            impl.setOAuthConsumer(consumerKey, consumerSecret);
            fail("Expected exception: consumer key/secret already set");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testGetOAuthRequestToken() {
        Configuration conf = new Configuration();
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, null);

        RequestToken requestToken = impl.getOAuthRequestToken();
        assertNotNull(requestToken);

        String callbackUrl = "callbackUrl";
        RequestToken requestTokenWithCallbackUrl = impl.getOAuthRequestToken(callbackUrl);
        assertEquals(callbackUrl, requestTokenWithCallbackUrl.getCallbackUrl());

        String xAuthAccessType = "xAuthAccessType";
        RequestToken requestTokenWithXAuthAccessType = impl.getOAuthRequestToken(callbackUrl, xAuthAccessType);
        assertEquals(xAuthAccessType, requestTokenWithXAuthAccessType.getXAuthAccessType());

        String xAuthMode = "xAuthMode";
        RequestToken requestTokenWithXAuthMode = impl.getOAuthRequestToken(callbackUrl, xAuthAccessType, xAuthMode);
        assertEquals(xAuthMode, requestTokenWithXAuthMode.getXAuthMode());
    }

    @Test
    public void testGetOAuthAccessToken() {
        Configuration conf = new Configuration();
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, null);

        String screenName = "screenName";
        String password = "password";

        AccessToken accessToken = impl.getOAuthAccessToken(screenName, password);
        assertNotNull(accessToken);
        assertEquals(screenName, accessToken.getScreenName());
        assertEquals(password, accessToken.getPassword());

        RequestToken requestToken = impl.getOAuthRequestToken();
        AccessToken accessTokenWithRequestToken = impl.getOAuthAccessToken(requestToken);
        assertEquals(requestToken.getToken(), accessTokenWithRequestToken.getToken());
        assertEquals(requestToken.getTokenSecret(), accessTokenWithRequestToken.getTokenSecret());

        String oauthVerifier = "oauthVerifier";
        AccessToken accessTokenWithOAuthVerifier = impl.getOAuthAccessToken(requestToken, oauthVerifier);
        assertEquals(oauthVerifier, accessTokenWithOAuthVerifier.getOAuthVerifier());

        impl.setAuthorization(new BasicAuthorization("consumerKey", "consumerSecret"));
        Authorization auth = impl.getAuthorization();
        AccessToken accessTokenWithoutRequestTokenOrOAuthVerifier = impl.getOAuthAccessToken();
        assertNull(accessTokenWithoutRequestTokenOrOAuthVerifier);
        assertEquals("consumerKey", auth.getConsumerKey());
        assertEquals("consumerSecret", auth.getConsumerSecret());
    }

    @Test
    public void testInit() {
        Configuration conf = new Configuration();
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, null);

        assertNull(impl.getScreenName());
        assertNull(impl.getId());
        assertNull(impl.getAuthorization());
        assertNull(impl.getHttp());

        impl.init();

        assertNotNull(impl.getScreenName());
        assertNotNull(impl.getId());
        assertNotNull(impl.getAuthorization());
        assertNotNull(impl.getHttp());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        Configuration conf = new Configuration();
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, null);

        Consumer<RateLimitStatusEvent> consumer = (event) -> {};
        impl.onRateLimitStatus(consumer);

        List<RateLimitStatusListener> listeners = new ArrayList<>();
        impl.addRateLimitStatusListener(consumer);
        assertTrue(listeners.contains(consumer));
    }

    @Test
    public void testHttpResponseReceived() throws IOException {
        Configuration conf = new Configuration();
        TwitterBaseImpl impl = new TwitterBaseImpl(conf, null);

        HttpClient http = HttpClientFactory.getInstance(conf.getHttpClientConfiguration());
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK);

        impl.http = http;
        impl.init();

        impl.httpResponseReceived(new HttpResponseEvent(null, response, null, null, null, null, null));

        List<RateLimitStatusListener> listeners = new ArrayList<>();
        impl.addRateLimitStatusListener(new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                listeners.add(event);
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
            }
        });

        RateLimitStatus rateLimitStatus = JSONImplFactory.createRateLimitStatusFromResponseHeader(response);
        RateLimitStatusEvent event = new RateLimitStatusEvent(impl, rateLimitStatus, true);
        assertEquals(rateLimitStatus, listeners.get(0).getRateLimitStatus());
    }
}