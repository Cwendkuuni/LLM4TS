package twitter4j;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Configuration;
import twitter4j.OAuthAuthorization;
import twitter4j.OAuthConsumer;
import twitter4j.OAuthSupport;
import twitter4j. TwitterBase;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.RequestToken;
import twitter4j.auth.XAuthAuthorization;

public class TwitterBaseImplTest {

 private TwitterBase twitterBase;
 private Configuration config;
 private Authorization auth;
 private OAuthSupport oauth;

 @Before
 public void setUp() {
 config = new Configuration();
 auth = new OAuthAuthorization(config);
 oauth = (OAuthSupport)auth;
 twitterBase = new TwitterBaseImpl(config, auth);
 }

 @Test
 public void testGetScreenName() {
 // Test with valid authentication
 String expectedScreenName = "testUser";
 auth.setOAuthConsumer("consumerKey", "consumerSecret");
 twitterBase.setOAuthAccessToken(new AccessToken("accessToken", "accessTokenSecret"));
 twitterBase.ensureAuthorizationEnabled();
 twitterBase.ensureOAuthEnabled();
 String actualScreenName = twitterBase.getScreenName();
 assertEquals(expectedScreenName, actualScreenName);

 // Test with null authentication
 assertThrows(IllegalStateException.class, twitterBase::getScreenName);
 }

 @Test
 public void testGetId() {
 // Test with valid authentication
 long expectedId = 1L;
 auth.setOAuthConsumer("consumerKey", "consumerSecret");
 twitterBase.setOAuthAccessToken(new AccessToken("accessToken", "accessTokenSecret"));
 twitterBase.ensureAuthorizationEnabled();
 twitterBase.ensureOAuthEnabled();
 long actualId = twitterBase.getId();
 assertEquals(expectedId, actualId);

 // Test with null authentication
 assertThrows(IllegalStateException.class, twitterBase::getId);
 }

 @Test
 public void testAddRateLimitStatusListener() {
 // Test adding a listener
 RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
 twitterBase.addRateLimitStatusListener(listener);
 verify(listener, times(1)).onRateLimitStatus(any(RateLimitStatusEvent.class));
 verify(listener, times(1)).onRateLimitReached(any(RateLimitStatusEvent.class));

 // Test adding the same listener multiple times
 twitterBase.addRateLimitStatusListener(listener);
 verify(listener, times(2)).onRateLimitStatus(any(RateLimitStatusEvent.class));
 verify(listener, times(2)).onRateLimitReached(any(RateLimitStatusEvent.class));
 }

 @Test
 public void testOnRateLimitStatus() {
 // Test with valid listener and rate limit status event
 RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBase, new RateLimitStatus(60, 120), true);
 RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
 twitterBase.onRateLimitStatus(event);
 verify(listener, times(1)).onRateLimitStatus(event);
 verify(listener, times(0)).onRateLimitReached(event);

 // Test with valid listener and rate limit reached status event
 RateLimitStatusEvent event2 = new RateLimitStatusEvent(twitterBase, null, true);
 twitterBase.onRateLimitStatus(event2);
 verify(listener, times(0)).onRateLimitStatus(event2);
 verify(listener, times(1)).onRateLimitReached(event2);
 }

 @Test
 public void testOnRateLimitReached() {
 // Test with valid listener and rate limit status event
 RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBase, new RateLimitStatus(60, 120), true);
 RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
 twitterBase.onRateLimitReached(event);
 verify(listener, times(1)).onRateLimitReached(event);
 verify(listener, times(0)).onRateLimitStatus(event);

 // Test with valid listener and no rate limit status event
 RateLimitStatusEvent event2 = new RateLimitStatusEvent(twitterBase, null, true);
 twitterBase.onRateLimitReached(event2);
 verify(listener, times(0)).onRateLimitStatus(event2);
 verify(listener, times(1)).onRateLimitReached(event2);
 }

 @Test
 public void testHttpResponseReceived() throws TwitterException {
 // Test with valid listener and response event
 HttpResponseEvent event = new HttpResponseEvent(null, new HttpResponse(200, null, null), null);
 twitterBase.httpResponseReceived(event);
 twitterBase.onRateLimitStatus(new RateLimitStatusEvent(twitterBase, null, true));
 twitterBase.onRateLimitReached(new RateLimitStatusEvent(twitterBase, null, true));
 verify(rateLimitStatusListeners.get(0), times(1)).onRateLimitStatus(any(RateLimitStatusEvent.class));
 verify(rateLimitStatusListeners.get(0), times(1)).onRateLimitReached(any(RateLimitStatusEvent.class));
 verify(rateLimitStatusListeners.get(1), times(1)).onRateLimitStatus(any(RateLimitStatusEvent.class));
 verify(rateLimitStatusListeners.get(1), times(1)).onRateLimitReached(any(RateLimitStatusEvent.class));

 // Test with valid listener and TwitterException
 twitterBase.httpResponseReceived(new HttpResponseEvent(null, null, new TwitterException("Test error")));
 twitterBase.onRateLimitStatus(new RateLimitStatusEvent(twitterBase, null, true));
 twitterBase.onRateLimitReached(new RateLimitStatusEvent(twitterBase, null, true));
 verify(rateLimitStatusListeners.get(0), times(1)).onRateLimitStatus(any(RateLimitStatusEvent.class));
 verify(rateLimitStatusListeners.get(0), times(0)).onRateLimitReached(any(RateLimitStatusEvent.class));
 verify(rateLimitStatusListeners.get(1), times(0)).onRateLimitStatus(any(RateLimitStatusEvent.class));
 verify(rateLimitStatusListeners.get(1), times(1)).onRateLimitReached(any(RateLimitStatusEvent.class));
 }

 @Test
 public void testGetAuthorization() {
 Authorization expectedAuth = auth;
 Authorization actualAuth = twitterBase.getAuthorization();
 assertEquals(expectedAuth, actualAuth);
 }

 @Test
 public void testGetConfiguration() {
 Configuration expectedConfig = config;
 Configuration actualConfig = twitterBase.getConfiguration();
 assertEquals(expectedConfig, actualConfig);
 }

 @Test
 public void testSetOAuthConsumer() {
 String consumerKey = "newConsumerKey";
 String consumerSecret = "newConsumerSecret";
 twitterBase.setOAuthConsumer(consumerKey, consumerSecret);
 assertEquals(consumerKey, auth.getConsumerKey());
 assertEquals(consumerSecret, auth.getConsumerSecret());
 }

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 RequestToken expectedRequestToken = new RequestToken("requestToken", "requestTokenSecret");
 RequestToken actualRequestToken = twitterBase.getOAuthRequestToken();
 assertEquals(expectedRequestToken.getToken(), actualRequestToken.getToken());
 assertEquals(expectedRequestToken.getTokenSecret(), actualRequestToken.getTokenSecret());
 }

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 AccessToken expectedAccessToken = new AccessToken("accessToken", "accessTokenSecret");
 AccessToken actualAccessToken = twitterBase.getOAuthAccessToken();
 assertEquals(expectedAccessToken.getScreenName(), actualAccessToken.getScreenName());
 assertEquals(expectedAccessToken.getId(), actualAccessToken.getId());
 assertEquals(expectedAccessToken.getToken(), actualAccessToken.getToken());
 assertEquals(expectedAccessToken.getTokenSecret(), actualAccessToken.getTokenSecret());
 }

 @Test
 public void testSetOAuthAccessToken() throws TwitterException {
 AccessToken accessToken = new AccessToken("accessToken", "accessTokenSecret");
 twitterBase.setOAuthAccessToken(accessToken);
 twitterBase.ensureAuthorizationEnabled();
 twitterBase.ensureOAuthEnabled();
 String screenName = twitterBase.getScreenName();
 long id = twitterBase.getId();
 assertEquals(accessToken.getScreenName(), screenName);
 assertEquals(accessToken.getId(), id);
 }

 @Test
 public void testGetOAuthAccessTokenWithVerifier() throws TwitterException {
 RequestToken requestToken = new RequestToken("requestToken", "requestTokenSecret");
 String oauthVerifier = "oauthVerifier";
 AccessToken accessToken = twitterBase.getOAuthAccessToken(requestToken, oauthVerifier);
 twitterBase.ensureAuthorizationEnabled();
 twitterBase.ensureOAuthEnabled();
 String actualScreenName = twitterBase.getScreenName();
 long actualId = twitterBase.getId();
 assertEquals(accessToken.getScreenName(), actualScreenName);
 assertEquals(accessToken.getId(), actualId);
 }
}