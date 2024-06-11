package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;
import twitter4j.auth.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret", "accessToken", "accessTokenSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        String screenName = twitterBaseImpl.getScreenName();
        assertNull(screenName);

        twitterBaseImpl.fillInIDAndScreenName();
        screenName = twitterBaseImpl.getScreenName();
        assertNotNull(screenName);
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret", "accessToken", "accessTokenSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        long id = twitterBaseImpl.getId();
        assertEquals(0, id);

        twitterBaseImpl.fillInIDAndScreenName();
        id = twitterBaseImpl.getId();
        assertNotEquals(0, id);
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl.ensureAuthorizationEnabled();

        HttpParameter[] parameters = new HttpParameter[0];
        User user = twitterBaseImpl.fillInIDAndScreenName(parameters);
        assertNotNull(user);
        assertNotNull(user.getScreenName());
        assertNotEquals(0, user.getId());
    }

    @Test
    public void testGetAuthorization() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        Authorization actualAuthorization = twitterBaseImpl.getAuthorization();
        assertEquals(auth, actualAuthorization);
    }

    @Test
    public void testGetConfiguration() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        Configuration actualConfiguration = twitterBaseImpl.getConfiguration();
        assertEquals(conf, actualConfiguration);
    }

    @Test
    public void testSetOAuthConsumer() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");

        Authorization actualAuthorization = twitterBaseImpl.getAuthorization();
        assertTrue(actualAuthorization instanceof OAuthAuthorization);
        OAuthAuthorization oauthAuthorization = (OAuthAuthorization) actualAuthorization;
        assertEquals("consumerKey", oauthAuthorization.getConsumerKey());
        assertEquals("consumerSecret", oauthAuthorization.getConsumerSecret());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetOAuthConsumerWithoutAuthorization() {
        Configuration conf = new Configuration();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        assertNotNull(requestToken);
        assertNotNull(requestToken.getToken());
        assertNotNull(requestToken.getTokenSecret());
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret", "accessToken", "accessTokenSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
        assertNotNull(accessToken);
        assertNotNull(accessToken.getScreenName());
        assertNotEquals(0, accessToken.getId());
    }

    @Test
    public void testSetOAuthAccessToken() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        AccessToken accessToken = new AccessToken("screenName", 1L, "token", "tokenSecret");
        twitterBaseImpl.setOAuthAccessToken(accessToken);

        AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken();
        assertEquals(accessToken, actualAccessToken);
    }

    @Test
    public void testEquals() {
        Configuration conf1 = new Configuration();
        Configuration conf2 = new Configuration();
        Authorization auth1 = new BasicAuthorization("consumerKey1", "consumerSecret1");
        Authorization auth2 = new BasicAuthorization("consumerKey2", "consumerSecret2");
        HttpClient http1 = HttpClientFactory.getInstance(conf1.getHttpClientConfiguration());
        HttpClient http2 = HttpClientFactory.getInstance(conf2.getHttpClientConfiguration());
        List<RateLimitStatusListener> rateLimitStatusListeners1 = new ArrayList<>();
        List<RateLimitStatusListener> rateLimitStatusListeners2 = new ArrayList<>();
        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf1, auth1);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf2, auth2);

        twitterBaseImpl1.setHttp(http1);
        twitterBaseImpl2.setHttp(http2);

        twitterBaseImpl1.addRateLimitStatusListener(rateLimitStatusListeners1.iterator().next());
        twitterBaseImpl2.addRateLimitStatusListener(rateLimitStatusListeners2.iterator().next());

        assertTrue(twitterBaseImpl1.equals(twitterBaseImpl2));

        conf1.setHttpClientConfiguration(new HttpClientConfiguration());
        conf2.setHttpClientConfiguration(new HttpClientConfiguration());

        assertFalse(twitterBaseImpl1.equals(twitterBaseImpl2));
    }

    @Test
    public void testHashCode() {
        Configuration conf1 = new Configuration();
        Configuration conf2 = new Configuration();
        Authorization auth1 = new BasicAuthorization("consumerKey1", "consumerSecret1");
        Authorization auth2 = new BasicAuthorization("consumerKey2", "consumerSecret2");
        HttpClient http1 = HttpClientFactory.getInstance(conf1.getHttpClientConfiguration());
        HttpClient http2 = HttpClientFactory.getInstance(conf2.getHttpClientConfiguration());
        List<RateLimitStatusListener> rateLimitStatusListeners1 = new ArrayList<>();
        List<RateLimitStatusListener> rateLimitStatusListeners2 = new ArrayList<>();
        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf1, auth1);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf2, auth2);

        twitterBaseImpl1.setHttp(http1);
        twitterBaseImpl2.setHttp(http2);

        twitterBaseImpl1.addRateLimitStatusListener(rateLimitStatusListeners1.iterator().next());
        twitterBaseImpl2.addRateLimitStatusListener(rateLimitStatusListeners2.iterator().next());

        int hashCode1 = twitterBaseImpl1.hashCode();
        int hashCode2 = twitterBaseImpl2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        HttpClient http = HttpClientFactory.getInstance(conf.getHttpClientConfiguration());
        List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<>();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.setHttp(http);
        twitterBaseImpl.addRateLimitStatusListener(rateLimitStatusListeners.iterator().next());

        String actualString = twitterBaseImpl.toString();
        String expectedString = "TwitterBase{conf=" + conf + ", http=" + http + ", rateLimitStatusListeners=" + rateLimitStatusListeners + ", auth=" + auth + '}';
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testInitWithOAuth2Credentials() {
        Configuration conf = new Configuration();
        Authorization auth = new OAuth2Authorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        assertNotNull(twitterBaseImpl.getAuthorization());
        assertTrue(twitterBaseImpl.getAuthorization() instanceof OAuth2Authorization);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitWithoutAuthorization() {
        Configuration conf = new Configuration();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitWithApplicationOnlyAuthEnabled() {
        Configuration conf = new Configuration();
        conf.setApplicationOnlyAuthEnabled(true);
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
    }

    @Test
    public void testInitializeWithBasicAuth() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        assertNotNull(twitterBaseImpl.getAuthorization());
        assertNotNull(twitterBaseImpl.getId());
        assertNotNull(twitterBaseImpl.getScreenName());
    }
}