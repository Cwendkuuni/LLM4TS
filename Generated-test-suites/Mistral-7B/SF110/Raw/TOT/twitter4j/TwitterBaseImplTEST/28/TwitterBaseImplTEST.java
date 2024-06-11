package twitter4j.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Twitter;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.oauth.OAuthAuthorization;
import twitter4j.oauth.OAuthSupport;
import twitter4j.TwitterException;
import twitter4j.util.function.Consumer;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private ConfigurationBuilder config;
 private OAuthAuthorization auth;

 @Before
 public void setUp() {
 config = new ConfigurationBuilder();
 auth = new OAuthAuthorization(config.build());
 twitterBaseImpl = new TwitterBaseImpl(config.build(), auth);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 assertEquals("testUser", twitterBaseImpl.getScreenName());
 twitterBaseImpl.setScreenName("newTestUser");
 assertEquals("newTestUser", twitterBaseImpl.getScreenName());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithNoAuth() throws TwitterException {
 twitterBaseImpl.setAuth(null);
 twitterBaseImpl.getScreenName();
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 assertEquals(1L, twitterBaseImpl.getId());
 twitterBaseImpl.setId(2L);
 assertEquals(2L, twitterBaseImpl.getId());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithNoAuth() throws TwitterException, IllegalStateException {
 twitterBaseImpl.setAuth(null);
 twitterBaseImpl.getId();
 }

 @Test
 public void testAddRateLimitStatusListener() {
 Consumer<RateLimitStatusEvent> listener = new Consumer<RateLimitStatusEvent>() {
 @Override
 public void accept(RateLimitStatusEvent event) {
 assertEquals(RateLimitStatus.LIMIT_NONE, event.getRateLimitStatus().getLimit());
 }
 };
 twitterBaseImpl.addRateLimitStatusListener(listener);
 }

 @Test
 public void testOnRateLimitStatus() throws TwitterException {
 RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(RateLimitStatus.LIMIT_RATE_LIMIT, 60), false);
 twitterBaseImpl.onRateLimitStatus(event);
 }

 @Test
 public void testOnRateLimitReached() throws TwitterException {
 RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(RateLimitStatus.LIMIT_RATE_LIMIT, 60), false);
 twitterBaseImpl.onRateLimitReached(event);
 }

 @Test
 public void testHttpResponseReceived() throws TwitterException {
 HttpResponseEvent event = new HttpResponseEvent(new HttpResponse(200, "success"), null, null, null);
 twitterBaseImpl.httpResponseReceived(event);
 }

 @Test
 public void testGetAuthorization() {
 assertEquals(auth, twitterBaseImpl.getAuthorization());
 }

 @Test
 public void testGetConfiguration() {
 assertEquals(config.build(), twitterBaseImpl.getConfiguration());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetConfigurationWithNoAuth() {
 twitterBaseImpl.setAuth(null);
 twitterBaseImpl.getConfiguration();
 }

 @Test
 public void testEnsureAuthorizationEnabled() throws TwitterException {
 twitterBaseImpl.ensureAuthorizationEnabled();
 }

 @Test(expected = IllegalStateException.class)
 public void testEnsureAuthorizationEnabledWithNoAuth() throws TwitterException {
 twitterBaseImpl.setAuth(null);
 twitterBaseImpl.ensureAuthorizationEnabled();
 }

 @Test
 public void testEnsureOAuthEnabled() throws TwitterException {
 twitterBaseImpl.ensureOAuthEnabled();
 }

 @Test(expected = IllegalStateException.class)
 public void testEnsureOAuthEnabledWithNoOAuth() throws TwitterException {
 twitterBaseImpl.setAuth(new BasicAuthorization("", ""));
 twitterBaseImpl.ensureOAuthEnabled();
 }
}