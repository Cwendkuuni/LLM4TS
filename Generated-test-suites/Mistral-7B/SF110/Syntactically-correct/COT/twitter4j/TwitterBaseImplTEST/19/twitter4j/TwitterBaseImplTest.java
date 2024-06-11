package twitter4j.test; // replace with the actual package name if needed

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;;

public class TwitterBaseImplTest {
    

    @Mock
    private Configuration conf;

    @Mock
    private Authorization auth;

    @Mock
    private HttpClient http;

    @Mock
    private ObjectFactory factory;

    @Mock
    private RateLimitStatusListener listener;

    @Captor
    private ArgumentCaptor<RateLimitStatusEvent> statusEventCaptor;

    private TwitterBaseImpl twitterBaseImpl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        twitterBaseImpl.addRateLimitStatusListener(listener);
        when(auth.isEnabled()).thenReturn(true);
        when(auth.getInstance(RateLimitStatusListener.class)).thenReturn(listener);
        when(conf.getRestBaseURL()).thenReturn("http://twitter.com/api/");

        twitterBaseImpl.getScreenName();

        verify(listener, times(2)).onRateLimitStatus(statusEventCaptor.capture());
        assertEquals("twitterBaseImpl", statusEventCaptor.getValue().getSource().getName());
        assertEquals(RateLimitStatus.LIMIT_NORMAL, statusEventCaptor.getValue().getRateLimitStatus().getLimit());
        assertEquals(RateLimitStatus.LIMIT_NORMAL, statusEventCaptor.getValue().getRateLimitStatus().getRemaining());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithoutAuth() throws TwitterException, IllegalStateException {
        when(auth.isEnabled()).thenReturn(false);
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        User user = new UserJSONImpl(null, new UserImpl());
        user.setScreenName("testUser");
        user.setId(123456789);

        when(auth.isEnabled()).thenReturn(true);
        when(auth.getInstance(User.class)).thenReturn(user);
        when(conf.getRestBaseURL()).thenReturn("http://twitter.com/api/");

        twitterBaseImpl.getId();

        verify(listener, times(2)).onRateLimitStatus(statusEventCaptor.capture());
        assertEquals("twitterBaseImpl", statusEventCaptor.getValue().getSource().getName());
        assertEquals(RateLimitStatus.LIMIT_NORMAL, statusEventCaptor.getValue().getRateLimitStatus().getLimit());
        assertEquals(RateLimitStatus.LIMIT_NORMAL, statusEventCaptor.getValue().getRateLimitStatus().getRemaining());
        assertEquals(123456789, twitterBaseImpl.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithoutAuth() throws TwitterException, IllegalStateException {
        when(auth.isEnabled()).thenReturn(false);
        twitterBaseImpl.getId();
    }

    @Test
    public void testAddRateLimitStatusListener() {
        twitterBaseImpl.addRateLimitStatusListener(listener);
        verify(listener, never()).onRateLimitStatus(statusEventCaptor.capture());
        verify(listener, never()).onRateLimitReached(statusEventCaptor.capture());

        twitterBaseImpl.addRateLimitStatusListener(listener);
        verify(listener, times(1)).onRateLimitStatus(statusEventCaptor.capture());
        verify(listener, never()).onRateLimitReached(statusEventCaptor.capture());

        twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
            public void onRateLimitStatus(RateLimitStatusEvent event) {
            }

            public void onRateLimitReached(RateLimitStatusEvent event) {
            }
        });
        verify(listener, times(1)).onRateLimitStatus(statusEventCaptor.capture());
        verify(listener, times(1)).onRateLimitReached(statusEventCaptor.capture());
    }

    @Test
    public void testHttpResponseReceivedWithRateLimitHit() {
        HttpResponseEvent event = new HttpResponseEvent(null, null, null, null, null, null, null, null);
        RateLimitStatus rateLimitStatus = new RateLimitStatus(RateLimitStatus.LIMIT_NONE, 0, 0, 0);

        twitterBaseImpl.onRateLimitStatus(new Consumer<RateLimitStatusEvent>() {
            public void accept(RateLimitStatusEvent event) {
            }
        });

        twitterBaseImpl.onRateLimitStatus(listener);
        twitterBaseImpl.httpResponseReceived(event);

        verify(listener, times(1)).onRateLimitStatus(statusEventCaptor.capture());
        assertEquals(rateLimitStatus, statusEventCaptor.getValue().getRateLimitStatus());
    }

    @Test
    public void testGetAuthorization() {
        when(auth.isEnabled()).thenReturn(true);
        Authorization expectedAuth = new OAuthAuthorization(conf);
        when(auth.getInstance(Authorization.class)).thenReturn(expectedAuth);

        Authorization actualAuth = twitterBaseImpl.getAuthorization();

        assertEquals(expectedAuth, actualAuth);
    }

    @Test
    public void testGetConfiguration() {
        when(conf.getHttpClientConfiguration()).thenReturn(new HttpClientConfiguration());
        when(conf.getOAuthConsumerKey()).thenReturn("testConsumerKey");
        when(conf.getOAuthConsumerSecret()).thenReturn("testConsumerSecret");
        when(conf.getOAuthAccessToken()).thenReturn("testAccessToken");
        when(conf.getOAuthAccessTokenSecret()).thenReturn("testAccessTokenSecret");
        when(conf.isApplicationOnlyAuthEnabled()).thenReturn(false);

        Configuration actualConf = twitterBaseImpl.getConfiguration();

        HttpClientConfiguration httpClientConfiguration = actualConf.getHttpClientConfiguration();
        String consumerKey = actualConf.getOAuthConsumerKey();
        String consumerSecret = actualConf.getOAuthConsumerSecret();
        String accessToken = actualConf.getOAuthAccessToken();
        String accessTokenSecret = actualConf.getOAuthAccessTokenSecret();
        boolean applicationOnlyAuthEnabled = actualConf.isApplicationOnlyAuthEnabled();

        assertEquals(httpClientConfiguration, twitterBaseImpl.getConfiguration().getHttpClientConfiguration());
        assertEquals("testConsumerKey", consumerKey);
        assertEquals("testConsumerSecret", consumerSecret);
        assertEquals("testAccessToken", accessToken);
        assertEquals("testAccessTokenSecret", accessTokenSecret);
        assertFalse(applicationOnlyAuthEnabled);
    }

    @Test
    public void testSetOAuthConsumer() {
        when(conf.isApplicationOnlyAuthEnabled()).thenReturn(false);

        String consumerKey = "testConsumerKey";
        String consumerSecret = "testConsumerSecret";

        twitterBaseImpl.setOAuthConsumer(consumerKey, consumerSecret);

        ArgumentCaptor<Consumer<Response>> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(http).setOAuthConsumer(consumerKey, consumerSecret);
        verify(http).setTwitter4jHttpClient(twitterBaseImpl);
        verify(http).get(anyString(), eq(consumerKey), eq(consumerSecret), responseCaptor.capture());
    }

    
}