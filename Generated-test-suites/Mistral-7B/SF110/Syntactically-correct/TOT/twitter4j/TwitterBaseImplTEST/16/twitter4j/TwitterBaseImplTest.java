import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import twitter4j.*;
import twitter4j.conf.ConfigurationImpl;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.HttpClient;
import twitter4j.util.function.RateLimitStatusListener;

import java.util.ArrayList;
import java.util.List;

public class TwitterBaseImplTest {

    @InjectMocks
    private TwitterBaseImpl twitterBaseImpl;
    private Configuration conf;
    private Authorization auth;
    private HttpClient http;
    private List<RateLimitStatusListener> rateLimitStatusListeners;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        conf = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .build();
        auth = new OAuthAuthorization(conf);
        http = HttpClientFactory.getInstance(new ConfigurationImpl());
        rateLimitStatusListeners = new ArrayList<>(0);
        twitterBaseImpl = new TwitterBaseImpl(conf, auth);
    }

    @Test
    public void testGetScreenName() {
        // Test with basic authentication
        twitterBaseImpl.setAuthorization(conf.getOAuthConsumerKey(), conf.getOAuthConsumerSecret());
        twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                // Handle rate limit status
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
                // Handle rate limit reached
            }
        });
        String expectedScreenName = "testUser";
        twitterBaseImpl.setScreenName(expectedScreenName);
        String actualScreenName = twitterBaseImpl.getScreenName();
        assert (actualScreenName.equals(expectedScreenName));

        // Test with OAuth authentication
        twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                // Handle rate limit status
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
                // Handle rate limit reached
            }
        });
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
        twitterBaseImpl.setOAuthAccessToken("accessToken", "accessTokenSecret");
        String actualScreenNameWithOAuth = twitterBaseImpl.getScreenName();
        assert (actualScreenNameWithOAuth != null);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithNoAuthorization() {
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() {
        // Test with basic authentication
        twitterBaseImpl.setAuthorization(conf.getOAuthConsumerKey(), conf.getOAuthConsumerSecret());
        twitterBaseImpl.fillInIDAndScreenName();
        long expectedId = 1L;
        long actualId = twitterBaseImpl.getId();
        assert (actualId == expectedId);

        // Test with OAuth authentication
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
        twitterBaseImpl.setOAuthAccessToken("accessToken", "accessTokenSecret");
        twitterBaseImpl.fillInIDAndScreenName();
        long actualIdWithOAuth = twitterBaseImpl.getId();
        assert (actualIdWithOAuth != 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithNoAuthorization() {
        twitterBaseImpl.getId();
    }

    @Test
    public void testAddRateLimitStatusListener() {
        twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                // Handle rate limit status
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
                // Handle rate limit reached
            }
        });
        RateLimitStatusListener listener = twitterBaseImpl.getAuthorization();
        assert (listener != null);
    }

    @Test
    public void testOnRateLimitStatus() {
        RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(60, 100), true);
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                int limit = event.getLimit();
                int remaining = event.getRemaining();
                boolean authenticated = event.isAuthenticated();
                assert (limit == 60);
                assert (remaining == 100);
                assert (authenticated == true);
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
                // Handle rate limit reached
            }
        };
        twitterBaseImpl.onRateLimitStatus(event);
    }

    @Test
    public void testOnRateLimitReached() {
        RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, null, true);
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                // Handle rate limit status
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
                int limit = event.getLimit();
                int remaining = event.getRemaining();
                boolean authenticated = event.isAuthenticated();
                assert (limit == 0);
                assert (remaining == 0);
                assert (authenticated == true);
            }
        };
        twitterBaseImpl.onRateLimitReached(event);
    }

    @Test
    public void testHttpResponseReceived() {
        HttpResponseEvent event = new HttpResponseEvent(new HttpResponse(200, "OK", null), null);
        RateLimitStatus rateLimitStatus = new RateLimitStatus(60, 100);
        RateLimitStatusEvent statusEvent = new RateLimitStatusEvent(twitterBaseImpl, rateLimitStatus, true);
        HttpResponse response = new HttpResponse(429, "Too Many Requests", null);
        HttpResponseEvent responseEvent = new HttpResponseEvent(response, twitterBaseImpl.getAuthorization());
        twitterBaseImpl.httpResponseReceived(event);
        twitterBaseImpl.onRateLimitStatus(statusEvent);
        twitterBaseImpl.onRateLimitReached(statusEvent);
        twitterBaseImpl.httpResponseReceived(responseEvent);
    }

    @Test
    public void testGetAuthorization() {
        assert (twitterBaseImpl.getAuthorization() instanceof OAuthAuthorization);
    }

    @Test
    public void testGetConfiguration() {
        assert (twitterBaseImpl.getConfiguration() != null);
    }
}