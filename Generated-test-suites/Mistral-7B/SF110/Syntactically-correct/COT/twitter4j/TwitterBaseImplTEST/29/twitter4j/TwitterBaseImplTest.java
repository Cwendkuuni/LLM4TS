package twitter4j; // Make sure the test package matches the production package

import static org.junit.Assert.*;
import static twitter4j.conf.Configuration.*;
import twitter4j.auth.*;
import twitter4j.http.HttpClient;
import twitter4j.http.HttpResponse;
import twitter4j.oauth.OAuthAuthorization;
import twitter4j.oauth.OAuthRequest;
import twitter4j.oauth.OAuthSupport;
import twitter4j.util.function.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.junit.rules.TestNameRule;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class TwitterBaseImplTest {

    private static final String WWW_DETAILS = "See http://twitter4j.org/en/configuration.html for details. See and register at http://apps.twitter.com/";

    @Rule
    public TestNameRule name = new TestNameRule();

    private TwitterBaseImpl twitterBaseImpl;
    private Configuration conf;
    private Authorization auth;
    private HttpClient http;
    private OAuthSupport oauth;
    private OAuthAuthorization oauthAuthorization;

    @Before
    public void setUp() throws Exception {
        conf = new Configuration();
        auth = Mockito.mock(Authorization.class);
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        http = Mockito.mock(HttpClient.class);
        oauth = Mockito.mock(OAuthSupport.class);
        oauthAuthorization = Mockito.mock(OAuthAuthorization.class);
        when(auth.isEnabled()).thenReturn(false);
        when(auth.getInstance()).thenReturn(oauthAuthorization);
        when(oauthAuthorization.getOAuthConsumer()).thenReturn(new OAuthConsumer(CONF_KEY, CONF_SECRET));
        when(oauthAuthorization.getOAuthAccessToken()).then(new Answer<AccessToken>() {
            @Override
            public AccessToken answer(InvocationOnMock invocation) throws Throwable {
                return new AccessToken(1L, "test_screen_name");
            }
        });
        when(auth.getInstance()).thenReturn(oauthAuthorization);
        when(oauth.getOAuthAccessToken()).thenAnswer(new Answer<AccessToken>() {
            @Override
            public AccessToken answer(InvocationOnMock invocation) throws Throwable {
                return new AccessToken(1L, "test_screen_name");
            }
        });
        when(oauth.getOAuthAccessToken(any(RequestToken.class))).thenAnswer(new Answer<AccessToken>() {
            @Override
            public AccessToken answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                RequestToken requestToken = (RequestToken)args[0];
                return new AccessToken(1L, "test_screen_name");
            }
        });
        when(oauth.getOAuthAccessToken(any(RequestToken.class), anyString())).thenAnswer(new Answer<AccessToken>() {
            @Override
            public AccessToken answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                RequestToken requestToken = (RequestToken)args[0];
                String oauthVerifier = (String)args[1];
                return new AccessToken(1L, "test_screen_name");
            }
        });
        when(oauth.getOAuthAccessToken(any(RequestToken.class), anyString(), anyString())).thenAnswer(new Answer<AccessToken>() {
            @Override
            public AccessToken answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                RequestToken requestToken = (RequestToken)args[0];
                String oauthVerifier = (String)args[1];
                String xAuthMode = (String)args[2];
                return new AccessToken(1L, "test_screen_name");
            }
        });
        when(oauth.getOAuthRequestToken(anyString())).thenAnswer(new Answer<RequestToken>() {
            @Override
            public RequestToken answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String callbackUrl = (String)args[0];
                return new RequestToken(callbackUrl);
            }
        });
        when(oauth.getOAuthRequestToken(anyString(), anyString())).thenAnswer(new Answer<RequestToken>() {
            @Override
            public RequestToken answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String callbackUrl = (String)args[0];
                String xAuthAccessType = (String)args[1];
                return new RequestToken(callbackUrl);
            }
        });
        when(oauth.getOAuthRequestToken(anyString(), anyString(), anyString())).thenAnswer(new Answer<RequestToken>() {
            @Override
            public RequestToken answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String callbackUrl = (String)args[0];
                String xAuthAccessType = (String)args[1];
                String xAuthMode = (String)args[2];
                return new RequestToken(callbackUrl);
            }
        });
        when(http.newRequest(any(OAuthSupport.class), anyString(), any(HttpRequest.class), any(HttpResponseListener.class))).thenAnswer(new Answer<HttpResponse>() {
            @Override
            public HttpResponse answer(InvocationOnMock invocation) throws Exception {
                Object[] args = invocation.getArguments();
                HttpResponse httpResponse = new HttpResponse(200, new ByteArrayInputStream("{}".getBytes()));
                HttpRequest httpRequest = (HttpRequest)args[2];
                if (httpRequest instanceof GetRequest) {
                    GetRequest getRequest = (GetRequest)httpRequest;
                    when(getRequest.getUrl()).thenReturn("http://www.example.com");
                }
                return httpResponse;
            }
        });
        when(auth.getInstance()).thenReturn(auth);
        when(oauth.getInstance()).thenReturn(oauth);
        twitterBaseImpl.setHttp(http);
        twitterBaseImpl.init();
    }

    @Test
    public void testGetScreenName() throws Exception {
        assertEquals("test_screen_name", twitterBaseImpl.getScreenName());
    }

    @Test
    public void testGetScreenName_authNotEnabled() throws Exception {
        when(auth.isEnabled()).thenReturn(false);
        try {
            twitterBaseImpl.getScreenName();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(1L, twitterBaseImpl.getId());
    }

    @Test
    public void testGetId_authNotEnabled() throws Exception {
        when(auth.isEnabled()).thenReturn(false);
        try {
            twitterBaseImpl.getId();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }
    }

    @Test
    public void testFillInIDAndScreenName() throws Exception {
        User user = twitterBaseImpl.fillInIDAndScreenName();
        assertEquals(1L, user.getId());
        assertEquals("test_screen_name", user.getScreenName());
    }

    @Test
    public void testFillInIDAndScreenName_authenticationFailed() throws Exception {
        when(auth.isEnabled()).thenReturn(true);
        when(oauthAuthorization.getOAuthAccessToken()).thenThrow(new TwitterException("Authentication failed"));
        try {
            twitterBaseImpl.fillInIDAndScreenName();
            fail("TwitterException expected");
        } catch (TwitterException e) {
            assertEquals("Authentication failed", e.getMessage());
        }
    }

    @Test
    public void testAddRateLimitStatusListener() {
        twitterBaseImpl.addRateLimitStatusListener(Mockito.mock(RateLimitStatusListener.class));
        assertEquals(1, twitterBaseImpl.rateLimitStatusListeners.size());
    }

    @Test
    public void testOnRateLimitStatus() {
        RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(500, 1000), false);
        Consumer<RateLimitStatusEvent> consumer = Mockito.mock(Consumer.class);
        twitterBaseImpl.onRateLimitStatus(consumer);
        twitterBaseImpl.httpResponseReceived(Mockito.mock(HttpResponseEvent.class));
        verify(consumer).accept(rateLimitStatusEvent);
    }

    @Test
    public void testOnRateLimitReached() {
        RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(500, 1000), false);
        Consumer<RateLimitStatusEvent> consumer = Mockito.mock(Consumer.class);
        twitterBaseImpl.onRateLimitReached(consumer);
        twitterBaseImpl.httpResponseReceived(Mockito.mock(HttpResponseEvent.class));
        verify(consumer).accept(rateLimitStatusEvent);
    }

    @Test
    public void testHttpResponseReceived() throws InterruptedException {
        HttpResponseEvent httpResponseEvent = Mockito.mock(HttpResponseEvent.class);
        when(httpResponseEvent.getStatusLine()).thenReturn(new StatusLine(200, "OK"));
        twitterBaseImpl.httpResponseReceived(httpResponseEvent);
        verifyNoMoreInteractions(httpResponseEvent);

        HttpResponseEvent httpResponseEvent2 = Mockito.mock(HttpResponseEvent.class);
        when(httpResponseEvent.getStatusLine()).thenReturn(new StatusLine(420, "Rate Limit Exceeded"));
        twitterBaseImpl.onRateLimitStatus(Mockito.mock(Consumer.class));
        twitterBaseImpl.httpResponseReceived(httpResponseEvent2);
        verify(httpResponseEvent2).getTwitterException();

        HttpResponseEvent httpResponseEvent3 = Mockito.mock(HttpResponseEvent.class);
        when(httpResponseEvent.getStatusLine()).thenReturn(new StatusLine(503, "Service Unavailable"));
        twitterBaseImpl.httpResponseReceived(httpResponseEvent3);
        verify(httpResponseEvent3).getTwitterException();

        HttpResponseEvent httpResponseEvent4 = Mockito.mock(HttpResponseEvent.class);
        when(httpResponseEvent.getStatusLine()).thenReturn(new StatusLine(429, "Too Many Requests"));
        RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(500, 1000), false);
        Consumer<RateLimitStatusEvent> consumer = Mockito.mock(Consumer.class);
        twitterBaseImpl.onRateLimitStatus(consumer);
        twitterBaseImpl.onRateLimitReached(consumer);
        twitterBaseImpl.httpResponseReceived(httpResponseEvent4);
        verify(consumer).accept(rateLimitStatusEvent);
    }

    @Test
    public void testGetAuthorization() {
        assertEquals(auth, twitterBaseImpl.getAuthorization());
    }

    @Test
    public void testGetConfiguration() {
        assertEquals(conf, twitterBaseImpl.getConfiguration());
    }

    @Test
    public void testGetOAuth() {
        assertEquals(oauth, twitterBaseImpl.getOAuth());
    }

    @Test
    public void testEquals() {
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(new Configuration(), Mockito.mock(Authorization.class));
        assertFalse(twitterBaseImpl.equals(twitterBaseImpl2));

        TwitterBaseImpl twitterBaseImpl3 = new TwitterBaseImpl(new Configuration(), auth);
        assertTrue(twitterBaseImpl.equals(twitterBaseImpl3));
    }

    @Test
    public void testHashCode() {
        int hashCode = twitterBaseImpl.hashCode();
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(new Configuration(), auth);
        assertEquals(hashCode, twitterBaseImpl2.hashCode());
    }

    @Test
    public void testToString() {
        String toString = twitterBaseImpl.toString();
        assertTrue(toString.contains("TwitterBase{conf="));
        assertTrue(toString.contains("http="));
        assertTrue(toString.contains("auth="));
    }
}