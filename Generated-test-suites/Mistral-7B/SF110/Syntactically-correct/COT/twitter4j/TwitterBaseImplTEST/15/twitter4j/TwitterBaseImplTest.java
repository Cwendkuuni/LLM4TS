import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.HttpClient;
import twitter4j.http.HttpResponse;
import twitter4j.oauth.OAuthAuthorization;
import twitter4j.oauth.OAuth2Authorization;
import twitter4j.util.function.Consumer;

import java.util.ArrayList;
import java.util.List;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;

    @Before
    public void setup() {
        Configuration conf = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        Authorization auth = new OAuthAuthorization(conf);
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
    }

    @Test
    public void testGetScreenName() {
        String actualScreenName = twitterBaseImpl.getScreenName();
        Assert.assertNotNull(actualScreenName);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithUninitializedAuth() {
        twitterBaseImpl.setAuth(null);
        try {
            twitterBaseImpl.getScreenName();
        } catch (IllegalStateException e) {
            Assert.assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }
    }

    @Test
    public void testGetId() {
        long actualId = twitterBaseImpl.getId();
        Assert.assertNotEquals(0L, actualId);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithUninitializedAuth() {
        try {
            twitterBaseImpl.getId();
        } catch (IllegalStateException e) {
            Assert.assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        Assert.assertTrue(twitterBaseImpl.rateLimitStatusListeners.contains(listener));
    }

    @Test
    public void testOnRateLimitStatus() {
        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
        RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
        twitterBaseImpl.onRateLimitStatus(action);
        twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(new HttpResponse(200, "{}")));
        twitterBaseImpl.onRateLimitStatus(event);
        verify(action).accept(event);
    }

    @Test
    public void testOnRateLimitReached() {
        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
        RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
        twitterBaseImpl.onRateLimitReached(action);
        twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(new HttpResponse(429, "{}")));
        twitterBaseImpl.onRateLimitStatus(event);
        verify(action).accept(event);
        verify(action).onRateLimitReached(event);
    }

    @Test
    public void testHttpResponseReceived() {
        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
        HttpResponseEvent httpResponseEvent = new HttpResponseEvent(new HttpResponse(429, "{}"));
        RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, null, false);
        twitterBaseImpl.onRateLimitStatus(action);
        twitterBaseImpl.httpResponseReceived(httpResponseEvent);
        verify(action).onRateLimitStatus(rateLimitStatusEvent);

        twitterBaseImpl.onRateLimitReached(action);
        twitterBaseImpl.httpResponseReceived(httpResponseEvent);
        verify(action).onRateLimitReached(rateLimitStatusEvent);
    }

    @Test
    public void testEnsureAuthorizationEnabled() {
        twitterBaseImpl.setAuth(null);
        try {
            twitterBaseImpl.ensureAuthorizationEnabled();
            fail("expected IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Authentication credentials are missing. See http://twitter4j.org/en/configuration.html for details. See and register at http://apps.twitter.com/", e.getMessage());
        }
    }

    @Test
    public void testEnsureOAuthEnabled() {
        twitterBaseImpl.setAuth(new OAuthAuthorization(new ConfigurationBuilder().build()));
        try {
            twitterBaseImpl.ensureOAuthEnabled();
        } catch (IllegalStateException e) {
            fail("expected no exception");
        }

        twitterBaseImpl.setAuth(new BasicAuthorization("user", "password"));
        try {
            twitterBaseImpl.ensureOAuthEnabled();
            fail("expected IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("OAuth required. Authentication credentials are missing. See http://twitter4j.org/en/configuration.html for details. See and register at http://apps.twitter.com/", e.getMessage());
        }
    }

    @Test
    public void testWriteObject() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        twitterBaseImpl.writeObject(objectOutputStream);
        byte[] serializedObject = outputStream.toByteArray();
        Object object = new ObjectInputStream(new ByteArrayInputStream(serializedObject)).readObject();
        Assert.assertEquals(twitterBaseImpl, object);
    }

    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(twitterBaseImpl);
        byte[] serializedObject = outputStream.toByteArray();
        Object object = new ObjectInputStream(new ByteArrayInputStream(serializedObject)).readObject();
        Assert.assertEquals(twitterBaseImpl, object);
    }

    @Test
    public void testSetOAuthConsumer() {
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
        Assert.assertTrue(twitterBaseImpl.getAuthorization() instanceof OAuthAuthorization);
    }

    @Test(expected = NullPointerException.class)
    public void testSetOAuthConsumerWithNullConsumerKeyOrConsumerSecret() {
        twitterBaseImpl.setOAuthConsumer(null, "consumerSecret");
    }

    @Test(expected = NullPointerException.class)
    public void testSetOAuthConsumerWithNullConsumerSecret() {
        twitterBaseImpl.setOAuthConsumer("consumerKey", null);
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        RequestToken actualRequestToken = twitterBaseImpl.getOAuthRequestToken();
        Assert.assertNotNull(actualRequestToken);
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken();
        Assert.assertNotNull(actualAccessToken);
    }

    @Test
    public void testGetOAuthAccessTokenWithOAuthVerifier() throws TwitterException {
        AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken("oauthVerifier");
        Assert.assertNotNull(actualAccessToken);
    }

    @Test
    public void testSetOAuthAccessToken() throws TwitterException {
        AccessToken accessToken = new AccessToken("accessToken", "accessTokenSecret", "screenName", 1L);
        twitterBaseImpl.setOAuthAccessToken(accessToken);
        AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken();
        Assert.assertEquals(accessToken, actualAccessToken);
    }

    @Test
    public void testSetOAuthAccessTokenWithNullAccessToken() {
        try {
            twitterBaseImpl.setOAuthAccessToken(null);
            fail("expected IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Access token is null", e.getMessage());
        }
    }

    @Test
    public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
        AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken("screenName", "password");
        Assert.assertNotNull(actualAccessToken);
    }

    @Test
    public void testGetOAuth2Token() throws TwitterException {
        OAuth2Token actualOAuth2Token = twitterBaseImpl.getOAuth2Token();
        Assert.assertNotNull(actualOAuth2Token);
    }

    @Test
    public void testSetOAuth2Token() throws TwitterException {
        OAuth2Token oauth2Token = new OAuth2Token("tokenType", "accessToken", "refreshToken", "tokenSecret");
        twitterBaseImpl.setOAuth2Token(oauth2Token);
        OAuth2Token actualOAuth2Token = twitterBaseImpl.getOAuth2Token();
        Assert.assertEquals(oauth2Token, actualOAuth2Token);
    }

    @Test
    public void testInvalidateOAuth2Token() throws TwitterException {
        twitterBaseImpl.setOAuth2Token(new OAuth2Token("tokenType", "accessToken", "refreshToken", "tokenSecret"));
        twitterBaseImpl.invalidateOAuth2Token();
        OAuth2Token actualOAuth2Token = twitterBaseImpl.getOAuth2Token();
        Assert.assertNull(actualOAuth2Token);
    }
}