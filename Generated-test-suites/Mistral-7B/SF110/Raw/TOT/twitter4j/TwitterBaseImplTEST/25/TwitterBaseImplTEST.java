package twitter4j.test;

import org.junit.Before;
import org.junit.Test;
import twitter4j.*;
import static org.junit.Assert.*;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBase;
 private Configuration conf;
 private Authorization auth;

 @Before
 public void setUp() {
 conf = new ConfigurationBuilder()
 .setDebugEnabled(true)
 .setOAuthConsumerKey("consumerKey")
 .setOAuthConsumerSecret("consumerSecret")
 .build();
 auth = new OAuthAuthorization(conf);
 twitterBase = new TwitterBaseImpl(conf, auth);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 // Test with initialized screenName
 assertEquals("expectedScreenName", twitterBase.getScreenName());

 // Test with null screenName and initialized auth
 twitterBase = new TwitterBaseImpl(conf, null);
 assertNull(twitterBase.getScreenName());

 // Test with null screenName and initialized OAuthAuthorization
 twitterBase = new TwitterBaseImpl(conf, new OAuthAuthorization(conf));
 assertNull(twitterBase.getScreenName());

 // Test with initialized auth and null screenName
 twitterBase = new TwitterBaseImpl(conf, new BasicAuthorization("userId", "password"));
 assertNull(twitterBase.getScreenName());

 // Test with initialized auth and screenName containing "@"
 twitterBase = new TwitterBaseImpl(conf, new BasicAuthorization("userId@domain.com", "password"));
 assertNull(twitterBase.getScreenName());

 // Test with initialized OAuthAuthorization and screenName
 twitterBase = new TwitterBaseImpl(conf, new OAuthAuthorization(conf));
 twitterBase.fillInIDAndScreenName();
 assertEquals("expectedScreenName", twitterBase.getScreenName());
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 // Test with initialized id
 assertEquals(1L, twitterBase.getId());

 // Test with null id and initialized auth
 twitterBase = new TwitterBaseImpl(conf, null);
 assertThrows(IllegalStateException.class, () -> twitterBase.getId());

 // Test with initialized auth and null id
 twitterBase = new TwitterBaseImpl(conf, new BasicAuthorization("userId", "password"));
 assertThrows(IllegalStateException.class, () -> twitterBase.getId());

 // Test with initialized OAuthAuthorization and id
 twitterBase = new TwitterBaseImpl(conf, new OAuthAuthorization(conf));
 twitterBase.fillInIDAndScreenName();
 assertEquals(1L, twitterBase.getId());
 }

 @Test
 public void testAddRateLimitStatusListener() {
 // Test adding listener
 twitterBase.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 // Test event
 assertNotNull(event.getRateLimitStatus());
 assertNotNull(event.isAuthenticated());
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 // Test event
 assertNotNull(event.getRateLimitStatus());
 assertNotNull(event.isAuthenticated());
 }
 });

 // Test adding multiple listeners
 twitterBase.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 // Test event
 assertNotNull(event.getRateLimitStatus());
 assertNotNull(event.isAuthenticated());
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 // Test event
 assertNotNull(event.getRateLimitStatus());
 assertNotNull(event.isAuthenticated());
 }
 });
 twitterBase.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 // Test event
 assertNotNull(event.getRateLimitStatus());
 assertNotNull(event.isAuthenticated());
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 // Test event
 assertNotNull(event.getRateLimitStatus());
 assertNotNull(event.isAuthenticated());
 }
 });

 // Test removing listener
 twitterBase.removeRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 // Test event
 assertNotNull(event.getRateLimitStatus());
 assertNotNull(event.isAuthenticated());
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 // Test event
 assertNotNull(event.getRateLimitStatus());
 assertNotNull(event.isAuthenticated());
 }
 });
 }

 @Test
 public void testHttpResponseReceived() {
 // Test no listeners
 HttpResponseEvent event = new HttpResponseEvent(null, null, null, null, null);
 twitterBase.httpResponseReceived(event);
 assertFalse(event.isAuthenticated());
 assertNull(event.getRateLimitStatus());

 // Test with authenticated response and rate limit exceeded
 event = new HttpResponseEvent(null, null, null, null, new RateLimitStatus(15, 15, 15, 15));
 twitterBase.httpResponseReceived(event);
 twitterBase.onRateLimitReached(new RateLimitStatusEvent(twitterBase, event.getRateLimitStatus(), true));
 assertTrue(event.isAuthenticated());
 assertNotNull(event.getRateLimitStatus());

 // Test with unauthenticated response and rate limit exceeded
 event = new HttpResponseEvent(null, null, null, null, new RateLimitStatus(15, 15, 15, 15));
 twitterBase.httpResponseReceived(event);
 twitterBase.onRateLimitStatus(new RateLimitStatusEvent(twitterBase, event.getRateLimitStatus(), false));
 assertFalse(event.isAuthenticated());
 assertNotNull(event.getRateLimitStatus());
 }

 @Test
 public void testGetAuthorization() {
 assertEquals("Authorization class not initialized", auth.getClass(), twitterBase.getAuthorization().getClass());
 }

 @Test
 public void testGetConfiguration() {
 assertEquals("Configuration not initialized", conf, twitterBase.getConfiguration());
 }

 @Test(expected = IllegalStateException.class)
 public void testIsEnabledAuthorizationNotSet() {
 twitterBase.ensureAuthorizationEnabled();
 fail("This should have thrown an exception");
 }

 @Test(expected = IllegalStateException.class)
 public void testIsEnabledOAuthNotSet() {
 twitterBase.ensureOAuthEnabled();
 fail("This should have thrown an exception");
 }

 @Test
 public void testFillInIDAndScreenName() throws TwitterException {
 // Test with initialized auth
 twitterBase = new TwitterBaseImpl(conf, new BasicAuthorization("userId", "password"));
 User user = twitterBase.fillInIDAndScreenName();
 assertEquals("expectedScreenName", user.getScreenName());
 assertEquals(1L, user.getId());

 // Test with initialized OAuthAuthorization
 twitterBase = new TwitterBaseImpl(conf, new OAuthAuthorization(conf));
 user = twitterBase.fillInIDAndScreenName();
 assertEquals("expectedScreenName", user.getScreenName());
 assertEquals(1L, user.getId());
 }

 @Test(expected = TwitterException.class)
 public void testFillInIDAndScreenNameFailure() throws TwitterException {
 twitterBase = new TwitterBaseImpl(conf, new BasicAuthorization("userId", "password"));
 twitterBase.fillInIDAndScreenName(new HttpParameter[] {new HttpParameter("foo", "bar")});
 fail("This should have thrown an exception");
 }
}