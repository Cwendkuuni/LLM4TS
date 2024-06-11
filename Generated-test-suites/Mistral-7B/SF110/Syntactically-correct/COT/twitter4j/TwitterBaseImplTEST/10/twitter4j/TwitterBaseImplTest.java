package twitter4j; // import necessary packages here

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private Configuration conf;
    private Authorization auth;
    private HttpClient http;
    private List<RateLimitStatusListener> rateLimitStatusListeners;
    private ObjectFactory factory;

    @Before
    public void setup() {
        conf = new Configuration.Builder().build();
        auth = Mockito.mock(Authorization.class);
        http = Mockito.mock(HttpClient.class);
        rateLimitStatusListeners = new ArrayList<>();
        factory = new JSONImplFactory(conf);

        twitterBaseImpl = new TwitterBaseImpl(conf, auth) {
            @Override
            public HttpClient getHttpClient() {
                return http;
            }

            @Override
            public ObjectFactory getFactory() {
                return factory;
            }
        };
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        String expectedScreenName = "testUser";
        twitterBaseImpl.addRateLimitStatusListener(mockRateLimitStatusListener());
        twitterBaseImpl.setAuthorization(new BasicAuthorization("testConsumerKey", "testConsumerSecret"));
        Mockito.when(http.get(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpParameter[].class), ArgumentMatchers.any(Authorization.class), ArgumentMatchers.any(HttpResponseListener.class)))
                .thenReturn(new HttpResponse(200, "{}"));

        String actualScreenName = twitterBaseImpl.getScreenName();
        assertEquals(expectedScreenName, actualScreenName);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithoutAuthorization() throws TwitterException, IllegalStateException {
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        long expectedId = 123456789L;
        twitterBaseImpl.addRateLimitStatusListener(mockRateLimitStatusListener());
        twitterBaseImpl.setAuthorization(new BasicAuthorization("testConsumerKey", "testConsumerSecret"));
        Mockito.when(http.get(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpParameter[].class), ArgumentMatchers.any(Authorization.class), ArgumentMatchers.any(HttpResponseListener.class)))
                .thenReturn(new HttpResponse(200, "{\"id\": " + expectedId + "}"));

        long actualId = twitterBaseImpl.getId();
        assertEquals(expectedId, actualId);
    }

    @Test(expected = IllegalStateException.class)
    public void testgetIdWithoutAuthorization() throws TwitterException, IllegalStateException {
        twitterBaseImpl.getId();
    }

    @Test
    public void testAddAndRemoveRateLimitStatusListener() {
        RateLimitStatusListener listener1 = mockRateLimitStatusListener();
        RateLimitStatusListener listener2 = mockRateLimitStatusListener();

        twitterBaseImpl.addRateLimitStatusListener(listener1);
        twitterBaseImpl.addRateLimitStatusListener(listener2);

        assertTrue(twitterBaseImpl.getRateLimitStatusListeners().contains(listener1));
        assertTrue(twitterBaseImpl.getRateLimitStatusListeners().contains(listener2));

        twitterBaseImpl.removeRateLimitStatusListener(listener1);

        assertFalse(twitterBaseImpl.getRateLimitStatusListeners().contains(listener1));
        assertTrue(twitterBaseImpl.getRateLimitStatusListeners().contains(listener2));
    }

    @Test
    public void testHttpResponseReceived() {
        RateLimitStatusEvent statusEvent = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(60, 1800), true);
        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);

        twitterBaseImpl.onRateLimitStatus(action);
        Mockito.verify(action).accept(statusEvent);
    }

    @Test
    public void testGetConfiguration() {
        Configuration expectedConf = new Configuration.Builder().build();
        assertEquals(expectedConf, twitterBaseImpl.getConfiguration());
    }

    @Test
    public void testEnsureAuthorizationEnabled() {
        twitterBaseImpl.ensureAuthorizationEnabled();
        assertTrue(twitterBaseImpl.getAuthorization().isEnabled());
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureAuthorizationDisabled() {
        twitterBaseImpl.ensureAuthorizationEnabled();
        twitterBaseImpl.setAuthorization(new NullAuthorization());
        twitterBaseImpl.ensureAuthorizationEnabled();
    }

    @Test
    public void testWriteObject() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(outputStream);

        twitterBaseImpl.writeObject(output);

        byte[] serializedObject = outputStream.toByteArray();
        TwitterBaseImpl deserializedObject = (TwitterBaseImpl) new ObjectInputStream(new ByteArrayInputStream(serializedObject)).readObject();

        assertEquals(twitterBaseImpl.getScreenName(), deserializedObject.getScreenName());
        assertEquals(twitterBaseImpl.getId(), deserializedObject.getId());
        assertEquals(twitterBaseImpl.getAuthorization(), deserializedObject.getAuthorization());
        assertEquals(twitterBaseImpl.getConfiguration(), deserializedObject.getConfiguration());
        assertEquals(twitterBaseImpl.getHttpClient(), deserializedObject.getHttpClient());
        assertEquals(twitterBaseImpl.getRateLimitStatusListeners(), deserializedObject.getRateLimitStatusListeners());
    }

    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(outputStream);

        Configuration conf = new Configuration.Builder().build();
        Authorization auth = new BasicAuthorization("testConsumerKey", "testConsumerSecret");
        HttpClient http = HttpClientFactory.getInstance(new HttpClientConfiguration());
        List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<>();

        output.writeObject(conf);
        output.writeObject(auth);
        output.writeObject(rateLimitStatusListeners);

        byte[] serializedObject = outputStream.toByteArray();
        TwitterBaseImpl deserializedObject = (TwitterBaseImpl) new ObjectInputStream(new ByteArrayInputStream(serializedObject)).readObject();

        assertEquals(conf, deserializedObject.getConfiguration());
        assertEquals(auth, deserializedObject.getAuthorization());
        assertEquals(http, deserializedObject.getHttpClient());
        assertEquals(rateLimitStatusListeners, deserializedObject.getRateLimitStatusListeners());
    }

    @Test
    public void testSetOAuthConsumer() {
        String consumerKey = "testConsumerKey";
        String consumerSecret = "testConsumerSecret";

        twitterBaseImpl.setOAuthConsumer(consumerKey, consumerSecret);

        assertEquals(consumerKey, twitterBaseImpl.getAuthorization().getConsumerKey());
        assertEquals(consumerSecret, twitterBaseImpl.getAuthorization().getConsumerSecret());
    }

    @Test(expected = NullPointerException.class)
    public void testSetOAuthConsumerNullConsumerKey() {
        twitterBaseImpl.setOAuthConsumer(null, "testConsumerSecret");
    }

    @Test(expected = NullPointerException.class)
    public void testSetOAuthConsumerNullConsumerSecret() {
        twitterBaseImpl.setOAuthConsumer("testConsumerKey", null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetOAuthConsumerAlreadySet() {
        twitterBaseImpl.setAuthorization(new BasicAuthorization("testConsumerKey", "testConsumerSecret"));
        twitterBaseImpl.setOAuthConsumer("newConsumerKey", "newConsumerSecret");
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        String callbackUrl = "http://test.com";
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken(callbackUrl);

        assertNotNull(requestToken);
        assertEquals(callbackUrl, requestToken.getCallbackUrl());
    }

    @Test
    public void testGetOAuthRequestTokenWithAccessType() throws TwitterException {
        String callbackUrl = "http://test.com";
        String accessType = "read";
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken(callbackUrl, accessType);

        assertNotNull(requestToken);
        assertEquals(callbackUrl, requestToken.getCallbackUrl());
        assertEquals(accessType, requestToken.getXAuthAccessType());
    }

    @Test(expected = TwitterException.class)
    public void testGetOAuthRequestTokenWithoutCallbackUrl() throws TwitterException {
        twitterBaseImpl.getOAuthRequestToken();
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        String oauthVerifier = "testVerifier";
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(oauthVerifier);

        assertNotNull(accessToken);
        assertNotNull(accessToken.getScreenName());
        assertNotNull(accessToken.getId());
    }

    @Test(expected = TwitterException.class)
    public void testGetOAuthAccessTokenWithoutVerifier() throws TwitterException {
        twitterBaseImpl.getOAuthAccessToken();
    }

    @Test
    public void testSetOAuthAccessToken() {
        AccessToken accessToken = new AccessToken("testScreenName", 1234567890L);
        twitterBaseImpl.setOAuthAccessToken(accessToken);

        assertEquals(accessToken.getScreenName(), twitterBaseImpl.getScreenName());
        assertEquals(accessToken.getId(), twitterBaseImpl.getId());
    }

    @Test
    public void testGetOAuthAccessTokenFromScreenNameAndPassword() throws TwitterException {
        String screenName = "testScreenName";
        String password = "testPassword";
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(screenName, password);

        assertNotNull(accessToken);
        assertEquals(screenName, accessToken.getScreenName());
        assertEquals(1234567890L, accessToken.getId());
    }

    @Test(expected = TwitterException.class)
    public void testGetOAuthAccessTokenFromScreenNameAndPasswordWrongCredentials() throws TwitterException {
        String screenName = "testScreenName";
        String password = "testPassword";
        twitterBaseImpl.getOAuthAccessToken(screenName, password);
    }

    @Test
    public void testGetOAuthSupport() {
        OAuthSupport oAuthSupport = twitterBaseImpl.getOAuth();
        assertTrue(oAuthSupport instanceof OAuthAuthorization);
    }

    @Test
    public void testGetOAuth2Support() {
        OAuth2Support oAuth2Support = twitterBaseImpl.getOAuth2();
        assertTrue(oAuth2Support instanceof OAuth2Authorization);
    }

    private RateLimitStatusListener mockRateLimitStatusListener() {
        return Mockito.mock(RateLimitStatusListener.class);
    }
}

