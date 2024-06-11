import static org.junit.Assert.*;
import twitter4j.Configuration;
import twitter4j.OAuthAuthorization;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.BasicAuthorization;
import twitter4j.auth.OAuth2Authorization;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.exceptions.TwitterException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;

    @Before
    public void setUp() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        Configuration conf = cb.setDebugEnabled(true)
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        twitterBaseImpl = new TwitterBaseImpl(conf, null);
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        String screenName = twitterBaseImpl.getScreenName();
        assertNull(screenName);

        twitterBaseImpl.setOAuthConsumer("testUser", "testPassword");
        screenName = twitterBaseImpl.getScreenName();
        assertNotNull(screenName);
        assertEquals("testUser", screenName);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithoutAuth() throws TwitterException, IllegalStateException {
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        twitterBaseImpl.setOAuthConsumer("testUser", "testPassword");
        long id = twitterBaseImpl.getId();
        assertNotEquals(0L, id);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithoutAuth() throws TwitterException, IllegalStateException {
        twitterBaseImpl.getId();
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        twitterBaseImpl.setOAuthConsumer("testUser", "testPassword");
        User user = twitterBaseImpl.fillInIDAndScreenName();
        assertNotNull(user);
        assertEquals("testUser", user.getScreenName());
        assertEquals(1L, user.getId());
    }

    @Test
    public void testFillInIDAndScreenNameWithParameters() throws TwitterException {
        twitterBaseImpl.setOAuthConsumer("testUser", "testPassword");
        HttpParameter[] parameters = {new BasicNameValuePair("foo", "bar")};
        User user = twitterBaseImpl.fillInIDAndScreenName(parameters);
        assertNotNull(user);
        assertEquals("testUser", user.getScreenName());
        assertEquals(1L, user.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        assertTrue(twitterBaseImpl.rateLimitStatusListeners.contains(listener));
    }

    @Test
    public void testOnRateLimitStatus() {
        Consumer<RateLimitStatusEvent> action = arg -> assertEquals(420, arg.getStatus().getLimitValue());
        twitterBaseImpl.onRateLimitStatus(action);
        HttpResponseEvent event = mock(HttpResponseEvent.class);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        when(event.getResponse()).thenReturn(rateLimitStatus);
        twitterBaseImpl.httpResponseReceived(event);
        verify(action).accept(any(RateLimitStatusEvent.class));
    }

    @Test
    public void testGetAuthorization() {
        assertInstanceOf(OAuthAuthorization.class, twitterBaseImpl.getAuthorization());
    }

    @Test
    public void testGetConfiguration() {
        assertEquals("consumerKey", twitterBaseImpl.getConfiguration().getOAuthConsumerKey());
    }

    @Test
    public void testEnsureAuthorizationEnabled() throws TwitterException {
        twitterBaseImpl = new TwitterBaseImpl(null, null);
        try {
            twitterBaseImpl.ensureAuthorizationEnabled();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testEnsureOAuthEnabled() throws TwitterException {
        Configuration conf = new Configuration();
        twitterBaseImpl = new TwitterBaseImpl(conf, null);
        try {
            twitterBaseImpl.ensureOAuthEnabled();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testWriteObject() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        twitterBaseImpl.writeObject(out);
        byte[] bytes = baos.toByteArray();
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(new Configuration(), null);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        TwitterBaseImpl twitterBaseImpl3 = (TwitterBaseImpl) ois.readObject();
        assertEquals(twitterBaseImpl, twitterBaseImpl3);
        twitterBaseImpl3.getOAuth().getOAuthAccessToken(new RequestToken("token", "tokenSecret"));
        assertEquals("consumerKey", twitterBaseImpl3.getConfiguration().getOAuthConsumerKey());
    }

    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(twitterBaseImpl);
        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bais);
        TwitterBaseImpl twitterBaseImpl2 = (TwitterBaseImpl) in.readObject();
        assertEquals(twitterBaseImpl, twitterBaseImpl2);
    }

    @Test
    public void testSetOAuthConsumer() throws TwitterException {
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
        OAuthAuthorization oauthAuthorization = (OAuthAuthorization) twitterBaseImpl.getAuthorization();
        assertEquals("consumerKey", oauthAuthorization.getConsumerKey());
        assertEquals("consumerSecret", oauthAuthorization.getConsumerSecret());
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        assertNotNull(requestToken);
        assertNull(requestToken.getToken());
        assertNull(requestToken.getTokenSecret());

        RequestToken requestToken2 = twitterBaseImpl.getOAuthRequestToken("http://localhost:8080/oauth/authorize/callback");
        assertNotNull(requestToken2);
        assertNotNull(requestToken2.getToken());
        assertNotNull(requestToken2.getTokenSecret());
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
        twitterBaseImpl.getOAuth().getOAuthAccessToken("testUser", "testPassword");
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
        assertNotNull(accessToken);
        assertNotNull(accessToken.getScreenName());
        assertEquals("testUser", accessToken.getScreenName());
        assertNotNull(accessToken.getId());
        assertEquals(1L, accessToken.getId());
    }

    @Test
    public void testGetOAuthAccessTokenWithOAuthRequestToken() throws TwitterException {
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken);
        assertNotNull(accessToken);
        assertNotNull(accessToken.getScreenName());
        assertEquals("testUser", accessToken.getScreenName());
        assertNotNull(accessToken.getId());
        assertEquals(1L, accessToken.getId());
    }

    @Test
    public void testGetOAuthAccessTokenWithOAuthVerifier() throws TwitterException {
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken("http://localhost:8080/oauth/authorize/callback");
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, "oauthVerifier");
        assertNotNull(accessToken);
        assertNotNull(accessToken.getScreenName());
        assertEquals("testUser", accessToken.getScreenName());
        assertNotNull(accessToken.getId());
        assertEquals(1L, accessToken.getId());
    }

    @Test
    public void testSetOAuthAccessToken() {
        AccessToken accessToken = new AccessToken("testToken", "testTokenSecret", "testScreenName", 1L);
        twitterBaseImpl.setOAuthAccessToken(accessToken);
        AccessToken accessToken2 = twitterBaseImpl.getOAuthAccessToken();
        assertEquals(accessToken, accessToken2);
    }

    @Test
    public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("testUser", "testPassword");
        assertNotNull(accessToken);
        assertNotNull(accessToken.getScreenName());
        assertEquals("testUser", accessToken.getScreenName());
        assertNotNull(accessToken.getId());
        assertEquals(1L, accessToken.getId());
    }

    @Test
    public void testGetOAuth2Token() throws TwitterException {
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
        OAuth2Authorization oauth2Authorization = (OAuth2Authorization) twitterBaseImpl.getAuthorization();
        oauth2Authorization.setOAuth2Token(new OAuth2Token("tokenType", "accessToken"));
        OAuth2Token oauth2Token = twitterBaseImpl.getOAuth2Token();
        assertNotNull(oauth2Token);
        assertEquals("tokenType", oauth2Token.getTokenType());
        assertEquals("accessToken", oauth2Token.getToken());
    }

    @Test
    public void testSetOAuth2Token() {
        OAuth2Token oauth2Token = new OAuth2Token("tokenType", "accessToken");
        twitterBaseImpl.setOAuth2Token(oauth2Token);
        OAuth2Token oauth2Token2 = twitterBaseImpl.getOAuth2Token();
        assertEquals(oauth2Token, oauth2Token2);
    }

    @Test
    public void testInvalidateOAuth2Token() throws TwitterException {
        OAuth2Token oauth2Token = mock(OAuth2Token.class);
        twitterBaseImpl.setOAuth2Token(oauth2Token);
        twitterBaseImpl.invalidateOAuth2Token();
        verify(oauth2Token).invalidate();
    }

    @Test
    public void testEquals() {
        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(new Configuration(), null);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(new Configuration(), null);
        assertFalse(twitterBaseImpl1.equals(twitterBaseImpl2));

        twitterBaseImpl2.setOAuthConsumer("consumerKey", "consumerSecret");
        assertFalse(twitterBaseImpl1.equals(twitterBaseImpl2));

        twitterBaseImpl1.setOAuthConsumer("consumerKey", "consumerSecret");
        assertTrue(twitterBaseImpl1.equals(twitterBaseImpl2));

        twitterBaseImpl1.setOAuthRequestToken(new RequestToken("token", "tokenSecret"));
        twitterBaseImpl2.setOAuthRequestToken(new RequestToken("token", "tokenSecret"));
        assertTrue(twitterBaseImpl1.equals(twitterBaseImpl2));

        twitterBaseImpl1.getOAuthAccessToken("testUser", "testPassword");
        twitterBaseImpl2.getOAuthAccessToken("testUser", "testPassword");
        assertTrue(twitterBaseImpl1.equals(twitterBaseImpl2));
    }

    @Test
    public void testHashCode() {
        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(new Configuration(), null);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(new Configuration(), null);
        int hashCode1 = twitterBaseImpl1.hashCode();
        int hashCode2 = twitterBaseImpl2.hashCode();
        assertNotEquals(hashCode1, hashCode2);

        twitterBaseImpl2.setOAuthConsumer("consumerKey", "consumerSecret");
        int hashCode3 = twitterBaseImpl1.hashCode();
        int hashCode4 = twitterBaseImpl2.hashCode();
        assertNotEquals(hashCode3, hashCode4);

        twitterBaseImpl1.setOAuthConsumer("consumerKey", "consumerSecret");
        int hashCode5 = twitterBaseImpl1.hashCode();
        int hashCode6 = twitterBaseImpl2.hashCode();
        assertEquals(hashCode5, hashCode6);

        twitterBaseImpl1.setOAuthRequestToken(new RequestToken("token", "tokenSecret"));
        int hashCode7 = twitterBaseImpl1.hashCode();
        int hashCode8 = twitterBaseImpl2.hashCode();
        assertEquals(hashCode7, hashCode8);

        twitterBaseImpl1.getOAuthAccessToken("testUser", "testPassword");
        int hashCode9 = twitterBaseImpl1.hashCode();
        int hashCode10 = twitterBaseImpl2.hashCode();
        assertEquals(hashCode9, hashCode10);
    }

    @Test
    public void testToString() {
        String toString = twitterBaseImpl.toString();
        assertTrue(toString.contains("TwitterBase{conf="));
        assertTrue(toString.contains("http="));
        assertTrue(toString.contains("rateLimitStatusListeners="));
        assertTrue(toString.contains("auth="));
    }
}