package twitter4j;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import twitter4j.auth.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import twitter4j.HttpResponseEvent;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.http.HttpClient;
import twitter4j.http.HttpResponse;
import twitter4j.http.HttpResponseListener;
import twitter4j.http.HttpResponseStatus;
import twitter4j.json.JSONFactory;
import twitter4j.json.JSONObject;
import twitter4j.util.RateLimitStatusEvent;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        when(auth.isEnabled()).thenReturn(true);
        when(auth.getUserId()).thenReturn("testUserID");

        assertEquals("testUserID", twitterBaseImpl.getScreenName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithoutAuth() throws TwitterException, IllegalStateException {
        Configuration conf = mock(Configuration.class);
        Authorization auth = null;
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        when(auth.isEnabled()).thenReturn(true);
        when(auth.getUserId()).thenReturn(1L);

        assertEquals(1L, twitterBaseImpl.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithoutAuth() throws TwitterException, IllegalStateException {
        Configuration conf = mock(Configuration.class);
        Authorization auth = null;
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl.getId();
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        HttpParameter[] parameters = new HttpParameter[0];
        User user = mock(User.class);

        when(auth.isEnabled()).thenReturn(true);
        when(user.getScreenName()).thenReturn("testScreenName");
        when(user.getId()).thenReturn(1L);

        twitterBaseImpl.fillInIDAndScreenName(parameters);

        assertEquals("testScreenName", twitterBaseImpl.getScreenName());
        assertEquals(1L, twitterBaseImpl.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);

        twitterBaseImpl.addRateLimitStatusListener(listener);

        assertTrue(twitterBaseImpl.rateLimitStatusListeners.contains(listener));
    }

    @Test
    public void testHttpResponseReceived() throws TwitterException {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        HttpClient http = mock(HttpClient.class);
        HttpResponseEvent event = mock(HttpResponseEvent.class);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);

        when(auth.isEnabled()).thenReturn(true);
        when(http.get(anyString(), any(HttpRequest.class), eq(auth), any(), eq(this), anyInt())).thenReturn(event);
        when(event.getTwitterException()).thenReturn(null);

        twitterBaseImpl.httpResponseReceived(event);

        verify(rateLimitStatusListeners, times(1)).onRateLimitStatus(any(RateLimitStatusEvent.class));
    }

    @Test
    public void testHttpResponseReceivedWithRateLimitReached() throws TwitterException {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        HttpClient http = mock(HttpClient.class);
        HttpResponseEvent event = mock(HttpResponseEvent.class);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);

        when(auth.isEnabled()).thenReturn(true);
        when(http.get(anyString(), any(HttpRequest.class), eq(auth), any(), eq(this), anyInt())).thenReturn(event);
        when(event.getTwitterException()).thenReturn(new TwitterException(429, "Rate limit reached", null, null, null, null));

        twitterBaseImpl.httpResponseReceived(event);

        verify(rateLimitStatusListeners, times(1)).onRateLimitStatus(any(RateLimitStatusEvent.class));
        verify(rateLimitStatusListeners, times(1)).onRateLimitReached(any(RateLimitStatusEvent.class));
    }

    @Test
    public void testSetOAuthConsumer() {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl.setOAuthConsumer("testConsumerKey", "testConsumerSecret");

        assertEquals("testConsumerKey", twitterBaseImpl.getAuthorization().getConsumerKey());
        assertEquals("testConsumerSecret", twitterBaseImpl.getAuthorization().getConsumerSecret());
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        OAuthSupport oauth = mock(OAuthSupport.class);

        when(auth.isEnabled()).thenReturn(true);
        when(auth instanceof OAuthAuthorization).thenReturn(true);
        when(oauth.getOAuthRequestToken(anyString())).thenReturn(new RequestToken("oauth_token", "oauth_token_secret"));

        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken("testCallbackUrl");

        assertEquals("oauth_token", requestToken.getToken());
        assertEquals("oauth_token_secret", requestToken.getTokenSecret());
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        OAuthSupport oauth = mock(OAuthSupport.class);

        when(auth.isEnabled()).thenReturn(true);
        when(auth instanceof OAuthAuthorization).thenReturn(true);
        when(oauth.getOAuthAccessToken(anyString(), anyString())).thenReturn(new AccessToken("access_token", "access_token_secret"));

        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("testOAuthVerifier");

        assertEquals("access_token", accessToken.getToken());
        assertEquals("access_token_secret", accessToken.getTokenSecret());
    }

    @Test
    public void testSetOAuthAccessToken() {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        AccessToken accessToken = mock(AccessToken.class);

        twitterBaseImpl.setOAuthAccessToken(accessToken);

        assertEquals(accessToken, twitterBaseImpl.getAuthorization().getAccessToken());
    }

    @Test
    public void testEquals() {
        Configuration conf1 = mock(Configuration.class);
        Configuration conf2 = mock(Configuration.class);
        Authorization auth1 = mock(Authorization.class);
        Authorization auth2 = mock(Authorization.class);
        HttpClient http1 = mock(HttpClient.class);
        HttpClient http2 = mock(HttpClient.class);
        List<RateLimitStatusListener> rateLimitStatusListeners1 = new ArrayList<>();
        List<RateLimitStatusListener> rateLimitStatusListeners2 = new ArrayList<>();

        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf1, auth1);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf2, auth2);

        twitterBaseImpl1.setHttpClient(http1);
        twitterBaseImpl2.setHttpClient(http2);
        twitterBaseImpl1.setRateLimitStatusListeners(rateLimitStatusListeners1);
        twitterBaseImpl2.setRateLimitStatusListeners(rateLimitStatusListeners2);

        when(auth1.equals(auth2)).thenReturn(true);
        when(conf1.equals(conf2)).thenReturn(true);
        when(http1.equals(http2)).thenReturn(true);
        when(rateLimitStatusListeners1.equals(rateLimitStatusListeners2)).thenReturn(true);

        assertTrue(twitterBaseImpl1.equals(twitterBaseImpl2));
    }

    @Test
    public void testHashCode() {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        HttpClient http = mock(HttpClient.class);
        List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<>();

        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf, auth);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);

        twitterBaseImpl1.setHttpClient(http);
        twitterBaseImpl2.setHttpClient(http);
        twitterBaseImpl1.setRateLimitStatusListeners(rateLimitStatusListeners);
        twitterBaseImpl2.setRateLimitStatusListeners(rateLimitStatusListeners);

        int hashCode1 = twitterBaseImpl1.hashCode();
        int hashCode2 = twitterBaseImpl2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }
}