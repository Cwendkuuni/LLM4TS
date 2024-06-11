import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import twitter4j.Authorization;
import twitter4j.Configuration;
import twitter4j.OAuth;
import twitter4j.OAuthAuthorization;
import twitter4j.OAuthRequestToken;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration config;
 private Authorization auth;
 private OAuth oauth;

 @Before
 public void setUp() {
 ConfigurationBuilder cb = new ConfigurationBuilder();
 config = cb.build();
 auth = new OAuthAuthorization(config);
 oauth = new OAuthImpl();
 twitterBaseImpl = new TwitterBaseImpl(config, auth);
 twitterBaseImpl.setOAuth(oauth);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 // Test normal case
 assertEquals("user1", twitterBaseImpl.getScreenName());

 // Test error case: Neither user ID/password combination nor OAuth consumer key/secret combination supplied
 assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getScreenName());
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 // Test normal case
 assertEquals(1L, twitterBaseImpl.getId());

 // Test error case: Neither user ID/password combination nor OAuth consumer key/secret combination supplied
 assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getId());
 }

 @Test
 public void testFillInIDAndScreenName() throws TwitterException {
 // Test normal case
 twitterBaseImpl.fillInIDAndScreenName();
 assertNotNull(twitterBaseImpl.getScreenName());
 assertNotNull(twitterBaseImpl.getId());

 // Test error case: twitterBaseImpl.auth is not initialized
 twitterBaseImpl.auth = null;
 assertThrows(TwitterException.class, () -> twitterBaseImpl.fillInIDAndScreenName());
 }

 @Test
 public void testAddRateLimitStatusListener() {
 List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<>();
 twitterBaseImpl.addRateLimitStatusListener(mock(RateLimitStatusListener.class));
 assertEquals(1, twitterBaseImpl.rateLimitStatusListeners.size());
 }

 @Test
 public void testHttpResponseReceived() throws TwitterException {
 // Test normal case: no rate limit reached
 HttpResponseEvent event = new HttpResponseEvent(new HttpResponse(200, "OK", new byte[0]));
 twitterBaseImpl.httpResponseReceived(event);
 verifyNoMoreInteractions(twitterBaseImpl.rateLimitStatusListeners);

 // Test edge case: rate limit reached
 HttpResponseEvent eventWithRateLimit = new HttpResponseEvent(new HttpResponse(420, "Rate limit exceeded", new byte[0]));
 twitterBaseImpl.httpResponseReceived(eventWithRateLimit);
 ArgumentCaptor<RateLimitStatusEvent> rateLimitEventCaptor = ArgumentCaptor.forClass(RateLimitStatusEvent.class);
 verify(twitterBaseImpl.rateLimitStatusListeners).onRateLimitStatus(rateLimitEventCaptor.capture());
 RateLimitStatusEvent rateLimitEvent = rateLimitEventCaptor.getValue();
 assertEquals(twitterBaseImpl, rateLimitEvent.getSource());
 assertTrue(rateLimitEvent.isAuthenticated());
 assertEquals(RateLimitStatus.LIMIT_RATE_LIMIT, rateLimitEvent.getRateLimitStatus().getLimit());
 assertEquals(RateLimitStatus.LIMIT_RATE_LIMIT, rateLimitEvent.getRateLimitStatus().getRemaining());
 assertEquals(RateLimitStatus.LIMIT_RATE_LIMIT, rateLimitEvent.getRateLimitStatus().getSecondsRemaining());
 }

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 OAuthRequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 assertNotNull(requestToken.getToken());
 assertNotNull(requestToken.getTokenSecret());
 }

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 assertNotNull(accessToken.getScreenName());
 assertNotNull(accessToken.getUserId());
 }

 @Test
 public void testSetOAuthAccessToken() {
 AccessToken accessToken = new AccessToken(1L, "user1", "token", "tokenSecret");
 twitterBaseImpl.setOAuthAccessToken(accessToken);
 assertEquals(accessToken.getScreenName(), twitterBaseImpl.screenName);
 assertEquals(accessToken.getUserId(), twitterBaseImpl.id);
 }

 @Test
 public void testEquals() {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(config, auth);
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(config, auth);
 assertTrue(twitterBaseBaseImpl1.equals(twitterBaseImpl2));

 TwitterBaseImpl twitterBaseImpl3 = new TwitterBaseImpl(config, null);
 assertFalse(twitterBaseImpl1.equals(twitterBaseImpl3));
 }
}