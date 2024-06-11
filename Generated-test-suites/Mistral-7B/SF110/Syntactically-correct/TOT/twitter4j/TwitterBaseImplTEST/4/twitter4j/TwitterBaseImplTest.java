package twitter4j.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.util.function.RateLimitStatusListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private Configuration conf;
    private Authorization auth;

    @Before
    public void setup() {
        conf = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .build();
        auth = Mockito.mock(Authorization.class);
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
    }

    @Test
    public void testGetScreenName() {
        when(auth.isEnabled()).thenReturn(true);
        when(auth.getInstance(Authorization.Type.BASIC)).thenReturn(new BasicAuthorization("testUser", "testPassword"));
        assertEquals("testUser", twitterBaseImpl.getScreenName());

        when(auth.getInstance(Authorization.Type.OAuth)).thenReturn(new OAuthAuthorization("testConsumerKey", "testConsumerSecret"));
        when(auth.getInstance(Authorization.Type.OAuth2)).thenReturn(new OAuth2Authorization("testConsumerKey", "testConsumerSecret"));
        when(auth.getInstance(Authorization.Type.XAuth)).thenReturn(new XAuthAuthorization("testConsumerKey", "testConsumerSecret"));
        assertEquals(null, twitterBaseImpl.getScreenName());

        when(auth.isEnabled()).thenReturn(false);
        assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getScreenName());
    }

    @Test
    public void testGetId() {
        when(auth.isEnabled()).thenReturn(true);
        when(auth.getInstance(Authorization.Type.BASIC)).thenReturn(new BasicAuthorization("testUser", "testPassword"));
        assertEquals(123456789L, twitterBaseImpl.getId());

        when(auth.getInstance(Authorization.Type.OAuth)).thenReturn(new OAuthAuthorization("testConsumerKey", "testConsumerSecret"));
        when(auth.getInstance(Authorization.Type.OAuth2)).thenReturn(new OAuth2Authorization("testConsumerKey", "testConsumerSecret"));
        when(auth.getInstance(Authorization.Type.XAuth)).thenReturn(new XAuthAuthorization("testConsumerKey", "testConsumerSecret"));
        assertNull(twitterBaseImpl.getId());

        when(auth.isEnabled()).thenReturn(false);
        assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getId());
    }

    @Test
    public void testSetFactory() {
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertNull(twitterBaseImpl.getFactory());

        twitterBaseImpl.setFactory();
        assertNotNull(twitterBaseImpl.getFactory());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        List<RateLimitStatusListener> listeners = new ArrayList<>();
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl.addRateLimitStatusListener(listeners.get(0));
        assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());

        twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
            }
        });
        assertEquals(2, twitterBaseImpl.getRateLimitStatusListeners().size());
    }

    @Test
    public void testOnRateLimitStatus() {
        List<RateLimitStatusListener> listeners = new ArrayList<>();
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        mockStatic(TwitterBaseImpl.class);
        when(TwitterBaseImpl.class.getDeclaredField("rateLimitStatusListeners")).thenReturn(twitterBaseImpl.getClass().getDeclaredField("rateLimitStatusListeners"));
        twitterBaseImpl.getRateLimitStatusListeners().setAccessible(true);

        twitterBaseImpl.addRateLimitStatusListener(listeners.get(0));
        when(listeners.get(0).onRateLimitStatus(any(RateLimitStatusEvent.class))).thenReturn();

        RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
        twitterBaseImpl.onRateLimitStatus(event);
        verify(listeners.get(0)).onRateLimitStatus(event);

        reset(listeners.get(0));
        when(listeners.get(0).onRateLimitReached(any(RateLimitStatusEvent.class))).thenReturn();

        reset(twitterBaseImpl);
        when(event.isAuthenticated()).thenReturn(true);
        twitterBaseImpl.onRateLimitStatus(event);
        verify(listeners.get(0)).onRateLimitStatus(event);

        reset(event);
        when(event.isAuthenticated()).thenReturn(false);
        twitterBaseImpl.onRateLimitStatus(event);
        verifyNoInteractions(listeners.get(0));
    }

    @Test
    public void testOnRateLimitReached() {
        List<RateLimitStatusListener> listeners = new ArrayList<>();
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        mockStatic(TwitterBaseImpl.class);
        when(TwitterBaseImpl.class.getDeclaredField("rateLimitStatusListeners")).thenReturn(twitterBaseImpl.getClass().getDeclaredField("rateLimitStatusListeners"));
        twitterBaseImpl.getRateLimitStatusListeners().setAccessible(true);

        twitterBaseImpl.addRateLimitStatusListener(listeners.get(0));
        when(listeners.get(0).onRateLimitReached(any(RateLimitStatusEvent.class))).thenReturn();

        RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
        twitterBaseImpl.onRateLimitReached(event);
        verify(listeners.get(0)).onRateLimitReached(event);
    }

    @Test
    public void testHttpResponseReceived() {
        List<RateLimitStatusListener> listeners = new ArrayList<>();
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        mockStatic(TwitterBaseImpl.class);
        when(TwitterBaseImpl.class.getDeclaredField("rateLimitStatusListeners")).thenReturn(twitterBaseImpl.getClass().getDeclaredField("rateLimitStatusListeners"));
        twitterBaseImpl.getRateLimitStatusListeners().setAccessible(true);

        twitterBaseImpl.addRateLimitStatusListener(listeners.get(0));

        HttpResponseEvent httpEvent = mock(HttpResponseEvent.class);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        when(rateLimitStatus.getSecondsUntilReset()).thenReturn(120);
        when(httpEvent.getRateLimitStatus()).thenReturn(rateLimitStatus);

        twitterBaseImpl.onRateLimitStatus(httpEvent);
        verify(listeners.get(0)).onRateLimitStatus(eq(httpEvent));
        verify(listeners.get(0)).onRateLimitReached(eq(httpEvent));

        reset(listeners.get(0));
        when(httpEvent.getRateLimitStatus()).thenReturn(null);
        twitterBaseImpl.onRateLimitStatus(httpEvent);
        verifyNoInteractions(listeners.get(0));
    }

    @Test
    public void testGetAuthorization() {
        when(auth.isEnabled()).thenReturn(true);
        assertEquals(auth, twitterBaseImpl.getAuthorization());

        when(auth.isEnabled()).thenReturn(false);
        assertNull(twitterBaseImpl.getAuthorization());
    }

    @Test
    public void testGetConfiguration() {
        assertEquals(conf, twitterBaseImpl.getConfiguration());
    }

    @Test
    public void testSetOAuthConsumer() {
        Configuration conf = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("testConsumerKey")
                .setOAuthConsumerSecret("testConsumerSecret")
                .build();
        twitterBaseImpl = new TwitterBaseImpl(conf, null);

        when(auth.isEnabled()).thenReturn(false);
        assertThrows(IllegalStateException.class, () -> twitterBaseImpl.setOAuthConsumer("testConsumerKey", "testConsumerSecret"));

        twitterBaseImpl.setAuthorization(new BasicAuthorization("testUser", "testPassword"));
        twitterBaseImpl.setOAuthConsumer("testConsumerKey", "testConsumerSecret");
        assertEquals("testConsumerKey", twitterBaseImpl.getAuthorization().getConsumerKey());
        assertEquals("testConsumerSecret", twitterBaseImpl.getAuthorization().getConsumerSecret());

        twitterBaseImpl.setAuthorization(new OAuthAuthorization("testConsumerKey", "testConsumerSecret"));
        assertThrows(IllegalStateException.class, () -> twitterBaseImpl.setOAuthConsumer("testConsumerKey", "testConsumerSecret"));

        twitterBaseImpl.setAuthorization(new OAuth2Authorization("testConsumerKey", "testConsumerSecret"));
        assertThrows(IllegalStateException.class, () -> twitterBaseImpl.setOAuthConsumer("testConsumerKey", "testConsumerSecret"));

        twitterBaseImpl.setAuthorization(new XAuthAuthorization("testConsumerKey", "testConsumerSecret"));
        twitterBaseImpl.setOAuthConsumer("testConsumerKey", "testConsumerSecret");
        assertEquals("testConsumerKey", twitterBaseImpl.getAuthorization().getConsumerKey());
        assertEquals("testConsumerSecret", twitterBaseImpl.getAuthorization().getConsumerSecret());
    }

    @Test
    public void testGetOAuthRequestToken() {
        when(auth.isEnabled()).thenReturn(true);
        when(auth.getInstance(Authorization.Type.OAuth)).thenReturn(new OAuthAuthorization("testConsumerKey", "testConsumerSecret"));

        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        assertEquals("oauth_callback", requestToken.getOAuthToken());
        assertEquals("oauth_token_secret", requestToken.getOAuthTokenSecret());

        Configuration conf = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("testConsumerKey")
                .setOAuthConsumerSecret("testConsumerSecret")
                .setOAuthRequestTokenCallback("http://localhost:8080")
                .build();
        twitterBaseImpl = new TwitterBaseImpl(conf, null);
        requestToken = twitterBaseImpl.getOAuthRequestToken();
        assertEquals("http://localhost:8080?oauth_token=" + requestToken.getOAuthToken(), requestToken.getAuthorizationURL());
    }

    @Test
    public void testGetOAuthAccessToken() {
        when(auth.isEnabled()).thenReturn(true);
        when(auth.getInstance(Authorization.Type.OAuth)).thenReturn(new OAuthAuthorization("testConsumerKey", "testConsumerSecret"));

        RequestToken requestToken = new RequestToken("requestToken", "requestTokenSecret");
        when(requestToken.getOAuthToken()).thenReturn("oauth_token");
        when(requestToken.getOAuthTokenSecret()).thenReturn("oauth_token_secret");

        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, "oauth_verifier");
        assertEquals("oauth_token", accessToken.getToken());
        assertEquals("oauth_token_secret", accessToken.getTokenSecret());
        assertEquals("testUser", accessToken.getScreenName());
        assertEquals(123456789L, accessToken.getUserId());

        when(auth.getInstance(Authorization.Type.OAuth)).thenReturn(null);
        assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getOAuthAccessToken(requestToken, "oauth_verifier"));
    }

    @Test
    public void testSetOAuthAccessToken() {
        AccessToken accessToken = new AccessToken("testToken", "testTokenSecret", "testUser", 123456789L);

        twitterBaseImpl.setOAuthAccessToken(accessToken);
        assertEquals("testToken", twitterBaseImpl.getAuthorization().getToken());
        assertEquals("testTokenSecret", twitterBaseImpl.getAuthorization().getTokenSecret());
        assertEquals("testUser", twitterBaseImpl.getScreenName());
        assertEquals(123456789L, twitterBaseImpl.getId());
    }

    @Test
    public void testEquals() {
        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf, auth);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
        TwitterBaseImpl twitterBaseImpl3 = new TwitterBaseImpl(conf, auth);

        assertEquals(twitterBaseImpl1, twitterBaseImpl1);
        assertEquals(twitterBaseImpl1, twitterBaseImpl2);
        assertEquals(twitterBaseImpl1, twitterBaseImpl3);

        twitterBaseImpl1.getAuthorization().setToken("newToken");
        assertNotEquals(twitterBaseImpl1, twitterBaseImpl2);

        twitterBaseImpl2.setAuthorization(new BasicAuthorization("testUser", "testPassword"));
        assertNotEquals(twitterBaseImpl1, twitterBaseImpl2);

        twitterBaseImpl2.setAuthorization(new OAuthAuthorization("testConsumerKey", "testConsumerSecret"));
        assertNotEquals(twitterBaseImpl1, twitterBaseImpl2);

        twitterBaseImpl2.setAuthorization(new OAuth2Authorization("testConsumerKey", "testConsumerSecret"));
        assertNotEquals(twitterBaseImpl1, twitterBaseImpl2);

        twitterBaseImpl2.setAuthorization(new XAuthAuthorization("testConsumerKey", "testConsumerSecret"));
        assertNotEquals(twitterBaseImpl1, twitterBaseImpl2);
    }

    @Test
    public void testHashCode() {
        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf, auth);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
        TwitterBaseImpl twitterBaseImpl3 = new TwitterBaseImpl(conf, auth);

        int hashCode1 = twitterBaseImpl1.hashCode();
        int hashCode2 = twitterBaseImpl2.hashCode();
        int hashCode3 = twitterBaseImpl3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode3);

        twitterBaseImpl1.getAuthorization().setToken("newToken");
        assertNotEquals(hashCode1, twitterBaseImpl2.hashCode());

        twitterBaseImpl2.setAuthorization(new BasicAuthorization("testUser", "testPassword"));
        assertNotEquals(hashCode1, twitterBaseImpl2.hashCode());

        twitterBaseImpl2.setAuthorization(new OAuthAuthorization("testConsumerKey", "testConsumerSecret"));
        assertNotEquals(hashCode1, twitterBaseImpl2.hashCode());

        twitterBaseImpl2.setAuthorization(new OAuth2Authorization("testConsumerKey", "testConsumerSecret"));
        assertNotEquals(hashCode1, twitterBaseImpl2.hashCode());

        twitterBaseImpl2.setAuthorization(new XAuthAuthorization("testConsumerKey", "testConsumerSecret"));
        assertNotEquals(hashCode1, twitterBaseImpl2.hashCode());
    }

    @Test
    public void testToString() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertEquals("TwitterBase{conf=" + conf + ", http=" + twitterBaseImpl.getHttp() + ", rateLimitStatusListeners=" + twitterBaseImpl.getRateLimitStatusListeners() + ", auth=" + twitterBaseImpl.getAuthorization() + '}', twitterBaseImpl.toString());
    }
}