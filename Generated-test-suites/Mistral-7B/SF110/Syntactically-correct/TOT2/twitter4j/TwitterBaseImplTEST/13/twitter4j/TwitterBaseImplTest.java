import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.auth.NullAuthorization;
import twitter4j.impl.Base45Encoding;
import twitter4j.impl.HttpClientImpl;
import twitter4j.json.JSONFactory;
import twitter4j.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;

    @Before
    public void setUp() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("testConsumerKey")
                .setOAuthConsumerSecret("testConsumerSecret")
                .setOAuthAccessToken("testAccessToken")
                .setOAuthAccessTokenSecret("testAccessTokenSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(cb.build()), new NullAuthorization());
        this.twitterBaseImpl = twitterBaseImpl;
    }

    @Test
    public void testGetScreenName() throws Exception {
        String expectedScreenName = "testUser";
        this.twitterBaseImpl.setFactory();
        String actualScreenName = this.twitterBaseImpl.getScreenName();
        Assert.assertEquals(expectedScreenName, actualScreenName);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithoutAuthorization() throws Exception {
        this.twitterBaseImpl.auth = null;
        this.twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws Exception {
        long expectedId = 123456789L;
        this.twitterBaseImpl.setFactory();
        long actualId = this.twitterBaseImpl.getId();
        Assert.assertEquals(expectedId, actualId);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithoutAuthorization() throws Exception {
        this.twitterBaseImpl.auth = null;
        this.twitterBaseImpl.getId();
    }

    @Test
    public void testFillInIDAndScreenName() throws Exception {
        User expectedUser = new UserJSONImpl(new JSONObject("{\"id\": 123456789, \"screen_name\": \"testUser\"}"), this.twitterBaseImpl.getConfiguration());
        this.twitterBaseImpl.fillInIDAndScreenName();
        User actualUser = this.twitterBaseImpl.getUser();
        Assert.assertEquals(expectedUser.getScreenName(), actualUser.getScreenName());
        Assert.assertEquals(expectedUser.getId(), actualUser.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = arg -> {};
        this.twitterBaseImpl.addRateLimitStatusListener(listener);
        Assert.assertEquals(1, this.twitterBaseImpl.rateLimitStatusListeners.size());
    }

    @Test
    public void testHttpResponseReceived() {
        HttpClientImpl httpClient = new HttpClientImpl();
        RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(this.twitterBaseImpl, new RateLimitStatus(100, 100), false);
        HttpResponseEvent httpResponseEvent = new HttpResponseEvent(httpClient, 200, null, null, null, null, null);
        this.twitterBaseImpl.httpResponseReceived(httpResponseEvent);
        for (RateLimitStatusListener listener : this.twitterBaseImpl.rateLimitStatusListeners) {
            listener.onRateLimitStatus(rateLimitStatusEvent);
        }
    }

    @Test
    public void testGetAuthorization() {
        Authorization expectedAuthorization = new ConfigurationBuilder()
                .setOAuthConsumerKey("testConsumerKey")
                .setOAuthConsumerSecret("testConsumerSecret")
                .buildAuthorization();
        Authorization actualAuthorization = this.twitterBaseImpl.getAuthorization();
        Assert.assertEquals(expectedAuthorization.getConsumerKey(), actualAuthorization.getConsumerKey());
        Assert.assertEquals(expectedAuthorization.getConsumerSecret(), actualAuthorization.getConsumerSecret());
    }

    @Test
    public void testGetConfiguration() {
        Configuration expectedConfiguration = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .build();
        Configuration actualConfiguration = this.twitterBaseImpl.getConfiguration();
        Assert.assertEquals(expectedConfiguration.isDebugEnabled(), actualConfiguration.isDebugEnabled());
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureAuthorizationEnabledWithoutAuthorization() {
        this.twitterBaseImpl.ensureAuthorizationEnabled();
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureOAuthEnabledWithoutOAuthSupport() {
        this.twitterBaseImpl.conf = null;
        this.twitterBaseImpl.ensureOAuthEnabled();
    }

    @Test
    public void testWriteObject() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(this.twitterBaseImpl);
        byte[] bytes = baos.toByteArray();
        TwitterBaseImpl deserializedObject = (TwitterBaseImpl) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        Assert.assertEquals(this.twitterBaseImpl.getScreenName(), deserializedObject.getScreenName());
    }

    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(this.twitterBaseImpl);
        byte[] bytes = baos.toByteArray();
        TwitterBaseImpl deserializedObject = (TwitterBaseImpl) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        Assert.assertEquals(this.twitterBaseImpl.getScreenName(), deserializedObject.getScreenName());
    }

    @Test
    public void testSetOAuthConsumer() {
        String consumerKey = "testConsumerKey";
        String consumerSecret = "testConsumerSecret";
        this.twitterBaseImpl.setOAuthConsumer(consumerKey, consumerSecret);
        Assert.assertEquals(consumerKey, this.twitterBaseImpl.auth.getConsumerKey());
        Assert.assertEquals(consumerSecret, this.twitterBaseImpl.auth.getConsumerSecret());
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        RequestToken expectedRequestToken = new RequestToken("oauth_token", "oauth_token_secret");
        this.twitterBaseImpl.setFactory();
        RequestToken actualRequestToken = this.twitterBaseImpl.getOAuthRequestToken();
        Assert.assertEquals(expectedRequestToken.getToken(), actualRequestToken.getToken());
        Assert.assertEquals(expectedRequestToken.getTokenSecret(), actualRequestToken.getTokenSecret());
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        AccessToken expectedAccessToken = new AccessToken("access_token", "access_token_secret");
        this.twitterBaseImpl.getOAuth().setOAuthAccessToken(expectedAccessToken);
        AccessToken actualAccessToken = this.twitterBaseImpl.getOAuthAccessToken();
        Assert.assertEquals(expectedAccessToken.getToken(), actualAccessToken.getToken());
        Assert.assertEquals(expectedAccessToken.getTokenSecret(), actualAccessToken.getTokenSecret());
    }

    @Test
    public void testGetOAuthAccessTokenWithOAuthVerifier() throws TwitterException {
        AccessToken expectedAccessToken = new AccessToken("access_token", "access_token_secret");
        this.twitterBaseImpl.getOAuth().setOAuthAccessToken(expectedAccessToken);
        String oauthVerifier = "testOAuthVerifier";
        AccessToken actualAccessToken = this.twitterBaseImpl.getOAuthAccessToken(oauthVerifier);
        Assert.assertEquals(expectedAccessToken.getToken(), actualAccessToken.getToken());
        Assert.assertEquals(expectedAccessToken.getTokenSecret(), actualAccessToken.getTokenSecret());
    }

    @Test
    public void testSetOAuthAccessToken() throws TwitterException {
        AccessToken accessToken = new AccessToken("access_token", "access_token_secret");
        this.twitterBaseImpl.setOAuthAccessToken(accessToken);
        Assert.assertEquals(accessToken.getToken(), this.twitterBaseImpl.getOAuthAccessToken().getToken());
        Assert.assertEquals(accessToken.getTokenSecret(), this.twitterBaseImpl.getOAuthAccessToken().getTokenSecret());
    }

    @Test
    public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
        String screenName = "testUser";
        String password = "testPassword";
        AccessToken expectedAccessToken = new AccessToken("access_token", "access_token_secret");
        this.twitterBaseImpl.getOAuth().setOAuthAccessToken(expectedAccessToken);
        AccessToken actualAccessToken = this.twitterBaseImpl.getOAuthAccessToken(screenName, password);
        Assert.assertEquals(expectedAccessToken.getToken(), actualAccessToken.getToken());
        Assert.assertEquals(expectedAccessToken.getTokenSecret(), actualAccessToken.getTokenSecret());
    }

    @Test
    public void testGetOAuth2Token() throws TwitterException {
        OAuth2Token expectedOAuth2Token = new OAuth2Token("token_type", "access_token", "refresh_token", "expires_in");
        this.twitterBaseImpl.getOAuth2().setOAuth2Token(expectedOAuth2Token);
        OAuth2Token actualOAuth2Token = this.twitterBaseImpl.getOAuth2Token();
        Assert.assertEquals(expectedOAuth2Token.getTokenType(), actualOAuth2Token.getTokenType());
        Assert.assertEquals(expectedOAuth2Token.getAccessToken(), actualOAuth2Token.getAccessToken());
        Assert.assertEquals(expectedOAuth2Token.getRefreshToken(), actualOAuth2Token.getRefreshToken());
        Assert.assertEquals(expectedOAuth2Token.getExpiresIn(), actualOAuth2Token.getExpiresIn());
    }

    @Test
    public void testSetOAuth2Token() throws TwitterException {
        OAuth2Token oauth2Token = new OAuth2Token("token_type", "access_token", "refresh_token", "expires_in");
        this.twitterBaseImpl.setOAuth2Token(oauth2Token);
        OAuth2Token actualOAuth2Token = this.twitterBaseImpl.getOAuth2Token();
        Assert.assertEquals(oauth2Token.getTokenType(), actualOAuth2Token.getTokenType());
        Assert.assertEquals(oauth2Token.getAccessToken(), actualOAuth2Token.getAccessToken());
        Assert.assertEquals(oauth2Token.getRefreshToken(), actualOAuth2Token.getRefreshToken());
        Assert.assertEquals(oauth2Token.getExpiresIn(), actualOAuth2Token.getExpiresIn());
    }

    @Test
    public void testInvalidateOAuth2Token() throws TwitterException {
        this.twitterBaseImpl.getOAuth2().invalidateOAuth2Token();
        OAuth2Token oauth2Token = this.twitterBaseImpl.getOAuth2Token();
        Assert.assertNull(oauth2Token);
    }
}