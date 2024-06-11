package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;
import twitter4j.conf.*;
import twitter4j.auth.*;
import twitter4j.TwitterBase;
import twitter4j.TwitterException;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;

public class TwitterBaseImplTest {

    @Test
    public void testGetConfiguration() {
        Configuration conf = new Configuration();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        Configuration confFromImpl = twitterBaseImpl.getConfiguration();
        assertEquals(conf, confFromImpl);
    }

    @Test
    public void testGetAuthorization() {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        Authorization authFromImpl = twitterBaseImpl.getAuthorization();
        assertEquals(auth, authFromImpl);
    }

    @Test
    public void testInitWithOAuth() {
        Configuration conf = new Configuration();
        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        Authorization auth = new OAuthAuthorization("accessToken", "accessTokenSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertTrue(twitterBaseImpl.getHttp() instanceof HttpClient);
        assertTrue(twitterBaseImpl.getFactory() instanceof JSONImplFactory);
    }

    @Test
    public void testInitWithOAuth2() {
        Configuration conf = new Configuration();
        conf.setOAuth2TokenType("tokenType");
        conf.setOAuth2AccessToken("accessToken");
        conf.setOAuth2AccessTokenSecret("accessTokenSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        assertTrue(twitterBaseImpl.getHttp() instanceof HttpClient);
        assertTrue(twitterBaseImpl.getFactory() instanceof JSONImplFactory);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitWithNoAuth() {
        Configuration conf = new Configuration();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("userId");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        User user = twitterBaseImpl.fillInIDAndScreenName();
        assertNotNull(user);
        assertEquals("userId", user.getScreenName());
        assertEquals(123L, user.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        assertTrue(twitterBaseImpl.rateLimitStatusListeners.contains(listener));
    }

    @Test
    public void testOnRateLimitStatus() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);
        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
        RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
        twitterBaseImpl.onRateLimitStatus(event);
        twitterBaseImpl.onRateLimitStatus(action);
        verify(action).accept(event);
    }

    @Test
    public void testOnRateLimitReached() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);
        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
        RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
        twitterBaseImpl.onRateLimitReached(event);
        twitterBaseImpl.onRateLimitReached(action);
        verify(action).accept(event);
    }

    @Test(expected = TwitterException.class)
    public void testHttpResponseReceivedWithException() throws TwitterException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);
        HttpResponseEvent event = mock(HttpResponseEvent.class);
        HttpResponse res = mock(HttpResponse.class);
        when(event.getResponse()).thenReturn(res);
        when(res.getStatusCode()).thenReturn(420);
        twitterBaseImpl.httpResponseReceived(event);
    }

    @Test
    public void testHttpResponseReceivedWithRateLimitStatus() throws TwitterException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);
        HttpResponseEvent event = mock(HttpResponseEvent.class);
        HttpResponse res = mock(HttpResponse.class);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        when(event.getResponse()).thenReturn(res);
        when(res.getStatusCode()).thenReturn(429);
        when(JSONImplFactory.createRateLimitStatusFromResponseHeader(res)).thenReturn(rateLimitStatus);
        RateLimitStatusEvent statusEvent = new RateLimitStatusEvent(twitterBaseImpl, rateLimitStatus, false);
        twitterBaseImpl.rateLimitStatusListeners.add(mock(RateLimitStatusListener.class));
        twitterBaseImpl.rateLimitStatusListeners.add(mock(RateLimitStatusListener.class));
        twitterBaseImpl.httpResponseReceived(event);
        verify(statusEvent.getListeners().get(0), times(1)).onRateLimitStatus(statusEvent);
        verify(statusEvent.getListeners().get(0), times(1)).onRateLimitReached(statusEvent);
        verify(statusEvent.getListeners().get(1), times(1)).onRateLimitStatus(statusEvent);
        verify(statusEvent.getListeners().get(1), times(1)).onRateLimitReached(statusEvent);
    }

    @Test
    public void testHttpResponseReceivedWithoutRateLimitStatus() throws TwitterException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(null, null);
        HttpResponseEvent event = mock(HttpResponseEvent.class);
        HttpResponse res = mock(HttpResponse.class);
        when(event.getResponse()).thenReturn(res);
        when(res.getStatusCode()).thenReturn(200);
        twitterBaseImpl.rateLimitStatusListeners.add(mock(RateLimitStatusListener.class));
        twitterBaseImpl.rateLimitStatusListeners.add(mock(RateLimitStatusListener.class));
        twitterBaseImpl.httpResponseReceived(event);
        verify(statusEvent.getListeners().get(0), times(1)).onRateLimitStatus(statusEvent);
        verify(statusEvent.getListeners().get(0), times(0)).onRateLimitReached(statusEvent);
        verify(statusEvent.getListeners().get(1), times(1)).onRateLimitStatus(statusEvent);
        verify(statusEvent.getListeners().get(1), times(0)).onRateLimitReached(statusEvent);
    }

    @Test
    public void testSetOAuthConsumer() {
        Configuration conf = new Configuration();
        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.setOAuthConsumer("newConsumerKey", "newConsumerSecret");
        Authorization auth = twitterBaseImpl.getAuthorization();
        assertEquals("newConsumerKey", auth.getConsumerKey());
        assertEquals("newConsumerSecret", auth.getConsumerSecret());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetOAuthConsumerWithOAuthAuth() {
        Configuration conf = new Configuration();
        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        Authorization auth = new OAuthAuthorization("accessToken", "accessTokenSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.setOAuthConsumer("newConsumerKey", "newConsumerSecret");
    }

    @Test(expected = IllegalStateException.class)
    public void testSetOAuthConsumerWithNullAuth() {
        Configuration conf = new Configuration();
        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.setOAuthConsumer("newConsumerKey", "newConsumerSecret");
    }

    @Test
    public void testSetOAuthRequestToken() throws TwitterException {
        Configuration conf = new Configuration();
        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        Authorization auth = new OAuthAuthorization("accessToken", "accessTokenSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        RequestToken requestToken = mock(RequestToken.class);
        twitterBaseImpl.setOAuthRequestToken(requestToken);
        assertEquals(requestToken, twitterBaseImpl.getOAuthRequestToken());
    }

    @Test
    public void testSetOAuthAccessToken() throws TwitterException {
        Configuration conf = new Configuration();
        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        Authorization auth = new OAuthAuthorization("accessToken", "accessTokenSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        AccessToken accessToken = mock(AccessToken.class);
        twitterBaseImpl.setOAuthAccessToken(accessToken);
        assertEquals(accessToken, twitterBaseImpl.getOAuthAccessToken());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetOAuthAccessTokenWithoutOAuthAuth() {
        Configuration conf = new Configuration();
        conf.setOAuthConsumerKey("consumerKey");
        conf.setOAuthConsumerSecret("consumerSecret");
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        AccessToken accessToken = mock(AccessToken.class);
        twitterBaseImpl.setOAuthAccessToken(accessToken);
    }

    @Test
    public void testWriteObject() throws IOException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<>();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(outputStream);
        twitterBaseImpl.writeObject(output);
        byte[] bytes = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(new Configuration(), null);
        twitterBaseImpl2.readObject(inputStream);
        assertEquals(twitterBaseImpl, twitterBaseImpl2);
    }

    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        Configuration conf = new Configuration();
        Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
        List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<>();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(outputStream);
        twitterBaseImpl.writeObject(output);
        byte[] bytes = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(new Configuration(), null);
        twitterBaseImpl2.readObject(inputStream);
        assertEquals(twitterBaseImpl, twitterBaseImpl2);
    }
}