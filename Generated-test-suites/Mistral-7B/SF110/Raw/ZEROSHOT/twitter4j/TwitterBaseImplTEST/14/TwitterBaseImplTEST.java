package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;
import twitter4j.auth.*;

@RunWith(MockitoJUnitRunner.class)
public class TwitterBaseImplTest {

    @Mock
    private Configuration conf;

    @Mock
    private Authorization auth;

    @Mock
    private HttpClient http;

    @Mock
    private List<RateLimitStatusListener> rateLimitStatusListeners;

    @Mock
    private ObjectFactory factory;

    @Mock
    private OAuth2Authorization oauth2;

    @Mock
    private OAuthAuthorization oauth3;

    @Mock
    private AccessToken accessToken;

    @Mock
    private RateLimitStatusEvent rateLimitStatusEvent;

    @InjectMocks
    private TwitterBaseImpl twitterBaseImpl;

    @Before
    public void setup() {
        Mockito.when(conf.getOAuthConsumerKey()).thenReturn("consumerKey");
        Mockito.when(conf.getOAuthConsumerSecret()).thenReturn("consumerSecret");
        Mockito.when(conf.isApplicationOnlyAuthEnabled()).thenReturn(false);
        Mockito.when(conf.getOAuthAccessToken()).thenReturn("accessToken");
        Mockito.when(conf.getOAuthAccessTokenSecret()).thenReturn("accessTokenSecret");
        Mockito.when(conf.getHttpClientConfiguration()).thenReturn(new HttpClientConfiguration());

        Mockito.when(auth.isEnabled()).thenReturn(true);
        Mockito.when(auth.getInstance()).thenReturn(auth);

        Mockito.when(oauth2.isOAuth2TokenEnabled()).thenReturn(true);
        Mockito.when(oauth2.getOAuth2TokenType()).thenReturn("tokenType");
        Mockito.when(oauth2.getOAuth2AccessToken()).thenReturn("accessToken");

        Mockito.when(oauth3.isOAuthTokenEnabled()).thenReturn(false);

        Mockito.when(factory.createJSONParser()).thenReturn(Mockito.mock(JSONParser.class));
        Mockito.when(factory.createOAuth(conf)).thenReturn(auth);
        Mockito.when(factory.createRateLimitStatusListener(Mockito.any(TwitterBase.class), Mockito.any(RateLimitStatusEvent.class), Mockito.anyBoolean())).thenAnswer(invocation -> new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                invocation.callRealMethod();
            }
            
            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
                invocation.callRealMethod();
            }
        });

        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        when(auth.getInstance()).thenReturn(auth3);
        assertEquals("expectedScreenName", twitterBaseImpl.getScreenName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithoutAuth() throws TwitterException, IllegalStateException {
        when(auth.getInstance()).thenReturn(null);
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        when(auth.getInstance()).thenReturn(auth3);
        assertEquals(123456L, twitterBaseImpl.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithoutAuth() throws TwitterException, IllegalStateException {
        when(auth.getInstance()).thenReturn(null);
        twitterBaseImpl.getId();
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        when(auth.getInstance()).thenReturn(auth3);
        User user = twitterBaseImpl.fillInIDAndScreenName();
        assertEquals("expectedUser", user);
    }

    @Test
    public void testAddRateLimitStatusListener() {
        twitterBaseImpl.addRateLimitStatusListener(Mockito.mock(RateLimitStatusListener.class));
        verify(rateLimitStatusListeners).add(Mockito.any(RateLimitStatusListener.class));
    }

    @Test
    public void testOnRateLimitStatus() {
        Consumer<RateLimitStatusEvent> action = Mockito.mock(Consumer.class);
        twitterBaseImpl.onRateLimitStatus(action);
        verify(rateLimitStatusListeners).stream()
                .filter(listener -> listener instanceof RateLimitStatusListener)
                .map(listener -> (RateLimitStatusListener)listener)
                .forEach(listener -> listener.onRateLimitStatus(rateLimitStatusEvent));
        action.accept(rateLimitStatusEvent);
        verify(rateLimitStatusListeners).stream()
                .filter(listener -> listener instanceof RateLimitStatusListener)
                .map(listener -> (RateLimitStatusListener)listener)
                .forEach(listener -> listener.onRateLimitReached(rateLimitStatusEvent));
    }

    @Test
    public void testHttpResponseReceived() {
        HttpResponseEvent httpResponseEvent = Mockito.mock(HttpResponseEvent.class);
        RateLimitStatus rateLimitStatus = Mockito.mock(RateLimitStatus.class);
        RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, rateLimitStatus, true);
        when(httpResponseEvent.getTwitterException()).thenReturn(null);
        when(httpResponseEvent.getResponse()).thenReturn(httpResponse);

        twitterBaseImpl.httpResponseReceived(httpResponseEvent);
        verify(rateLimitStatusListeners).stream()
                .filter(listener -> listener instanceof RateLimitStatusListener)
                .map(listener -> (RateLimitStatusListener)listener)
                .forEach(listener -> listener.onRateLimitStatus(rateLimitStatusEvent));

        HttpResponseEvent httpResponseEventWithException = new HttpResponseEvent(httpResponse, new TwitterException("error"));
        twitterBaseImpl.httpResponseReceived(httpResponseEventWithException);
        verify(rateLimitStatusListeners).stream()
                .filter(listener -> listener instanceof RateLimitStatusListener)
                .map(listener -> (RateLimitStatusListener)listener)
                .forEach(listener -> listener.onRateLimitStatus(rateLimitStatusEvent));
        verify(rateLimitStatusListeners).stream()
                        .filter(listener -> listener instanceof RateLimitStatusListener)
                        .map(listener -> (RateLimitStatusListener)listener)
                        .forEach(listener -> listener.onRateLimitReached(rateLimitStatusEvent));

        when(httpResponseEvent.getTwitterException()).thenReturn(new TwitterRateLimitException(rateLimitStatus));
        twitterBaseImpl.httpResponseReceived(httpResponseEvent);
        verify(rateLimitStatusListeners).stream()
                .filter(listener -> listener instanceof RateLimitStatusListener)
                .map(listener -> (RateLimitStatusListener)listener)
                .forEach(listener -> listener.onRateLimitStatus(rateLimitStatusEvent));
        verify(rateLimitStatusListeners).stream()
                        .filter(listener -> listener instanceof RateLimitStatusListener)
                        .map(listener -> (RateLimitStatusListener)listener)
                        .forEach(listener -> listener.onRateLimitReached(rateLimitStatusEvent));
    }

    @Test
    public void testGetAuthorization() {
        assertEquals(auth, twitterBaseImpl.getAuthorization());
    }

    @Test
    public void testGetConfiguration() {
        assertEquals(conf, twitterBaseImpl.getConfiguration());
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureAuthorizationEnabledWithoutAuth() {
        twitterBaseImpl.ensureAuthorizationEnabled();
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureOAuthEnabledWithoutOAuth() {
        twitterBaseImpl.ensureOAuthEnabled();
    }

    @Test
    public void testWriteObject() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        twitterBaseImpl.writeObject(out);
        byte[] bytes = baos.toByteArray();
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
        TwitterBaseImpl twitterBaseImpl3 = (TwitterBaseImpl)in.readObject();
        assertEquals(twitterBaseImpl, twitterBaseImpl3);
    }

    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(twitterBaseImpl);
        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(inputStream);
        TwitterBaseImpl twitterBaseImpl2 = (TwitterBaseImpl)in.readObject();
        assertEquals(twitterBaseImpl, twitterBaseImpl2);
    }

    @Test
    public void testSetOAuthConsumer() {
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
        assertEquals("consumerKey", twitterBaseImpl.getConfiguration().getOAuthConsumerKey());
        assertEquals("consumerSecret", twitterBaseImpl.getConfiguration().getOAuthConsumerSecret());
    }

    @Test(expected = NullPointerException.class)
    public void testSetOAuthConsumerWithNullConsumerKey() {
        twitterBaseImpl.setOAuthConsumer(null, "consumerSecret");
    }

    @Test(expected = NullPointerException.class)
    public void testSetOAuthConsumerWithNullConsumerSecret() {
        twitterBaseImpl.setOAuthConsumer("consumerKey", null);
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        assertEquals("requestToken", requestToken);
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
        assertEquals("accessToken", accessToken);
    }

    @Test
    public void testSetOAuthAccessToken() {
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
        twitterBaseImpl.setOAuthAccessToken(accessToken);
        assertEquals(accessToken, twitterBaseImpl.getOAuthAccessToken());
    }
}