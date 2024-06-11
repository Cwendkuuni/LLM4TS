import org.junit.Assert;
import org.junit.Test;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.TwitterException;
import twitter4j.auth.Authorization;
import twitter4j.conf.Configuration;
import twitter4j.impl.BaseTest;
import twitter4j.oauth.OAuthAuthorization;
import twitter4j.oauth.OAuthConsumer;
import twitter4j.oauth.OAuthRequestToken;
import twitter4j.oauth.OAuthSupport;
import twitter4j.oauth.OAuthToken;
import twitter4j.util.RateLimitStatus;

import java.util.ArrayList;
import java.util.List;

public class TwitterBaseImplTest extends BaseTest {

    @Test
    public void testConstructors() {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        Assert.assertNotNull(twitterBaseImpl.getScreenName());
        Assert.assertNotNull(twitterBaseImpl.getId());
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        Assert.assertEquals(twitterBaseImpl.getScreenName(), twitterBaseImpl.getScreenName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameThrowsIllegalStateException() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        Assert.assertEquals(twitterBaseImpl.getId(), twitterBaseImpl.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdThrowsIllegalStateException() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.getId();
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.fillInIDAndScreenName();
        Assert.assertNotNull(twitterBaseImpl.getScreenName());
        Assert.assertNotNull(twitterBaseImpl.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        twitterBaseImpl.onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, null, true));
        verify(listener).onRateLimitStatus(any(RateLimitStatusEvent.class));
    }

    @Test
    public void testOnRateLimitStatus() {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        List<RateLimitStatusListener> listeners = new ArrayList<>();
        twitterBaseImpl.onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, null, true));
        listeners.add(mock(RateLimitStatusListener.class));
        twitterBaseImpl.onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, null, true));
        twitterBaseImpl.onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, null, true));
        verify(listeners.get(0), times(3)).onRateLimitStatus(any(RateLimitStatusEvent.class));
    }

    @Test
    public void testOnRateLimitReached() {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        List<RateLimitStatusListener> listeners = new ArrayList<>();
        twitterBaseImpl.onRateLimitReached(new RateLimitStatusEvent(twitterBaseImpl, null, true));
        listeners.add(mock(RateLimitStatusListener.class));
        twitterBaseImpl.onRateLimitReached(new RateLimitStatusEvent(twitterBaseImpl, null, true));
        twitterBaseImpl.onRateLimitReached(new RateLimitStatusEvent(twitterBaseImpl, null, true));
        verify(listeners.get(0), times(3)).onRateLimitReached(any(RateLimitStatusEvent.class));
    }

    @Test
    public void testHttpResponseReceived() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        HttpResponseEvent event = mock(HttpResponseEvent.class);
        RateLimitStatus rateLimitStatus = new RateLimitStatus(1, 1, 1, 1);
        RateLimitStatusEvent statusEvent = new RateLimitStatusEvent(twitterBaseImpl, rateLimitStatus, true);
        when(event.getTwitterException()).thenReturn(null);
        when(event.getRateLimitStatus()).thenReturn(rateLimitStatus);
        twitterBaseImpl.httpResponseReceived(event);
        List<RateLimitStatusListener> listeners = new ArrayList<>();
        twitterBaseImpl.onRateLimitStatus(statusEvent);
        listeners.add(mock(RateLimitStatusListener.class));
        twitterBaseImpl.onRateLimitReached(statusEvent);
        verify(listeners.get(0), times(1)).onRateLimitStatus(any(RateLimitStatusEvent.class));
        verify(listeners.get(0), times(1)).onRateLimitReached(any(RateLimitStatusEvent.class));
    }

    @Test
    public void testSetOAuthConsumer() {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
        OAuthConsumer consumer = twitterBaseImpl.getAuthorization().getOAuthConsumer();
        Assert.assertEquals(consumer.getConsumerKey(), "consumerKey");
        Assert.assertEquals(consumer.getConsumerSecret(), "consumerSecret");
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        OAuthSupport oauthSupport = twitterBaseImpl.getOAuth();
        OAuthRequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        Assert.assertNotNull(requestToken.getToken());
        Assert.assertNotNull(requestToken.getTokenSecret());
        Assert.assertEquals(requestToken.getToken(), oauthSupport.getOAuthRequestToken().getToken());
        Assert.assertEquals(requestToken.getTokenSecret(), oauthSupport.getOAuthRequestToken().getTokenSecret());
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        OAuthSupport oauthSupport = twitterBaseImpl.getOAuth();
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
        Assert.assertNotNull(accessToken.getScreenName());
        Assert.assertNotNull(accessToken.getId());
        Assert.assertEquals(accessToken.getScreenName(), oauthSupport.getOAuthAccessToken().getScreenName());
        Assert.assertEquals(accessToken.getId(), oauthSupport.getOAuthAccessToken().getId());
    }

    @Test
    public void testSetOAuthAccessToken() {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        AccessToken accessToken = new AccessToken("screenName", 1L);
        twitterBaseImpl.setOAuthAccessToken(accessToken);
        Assert.assertEquals(twitterBaseImpl.getScreenName(), accessToken.getScreenName());
        Assert.assertEquals(twitterBaseImpl.getId(), accessToken.getId());
    }
}