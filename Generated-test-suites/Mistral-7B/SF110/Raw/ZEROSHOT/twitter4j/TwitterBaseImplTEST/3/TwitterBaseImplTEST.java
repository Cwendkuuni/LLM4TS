package twitter4j.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import twitter4j.*;
import twitter4j.auth.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration configuration;
 private Authorization authorization;

 @Before
 public void setup() {
 ConfigurationBuilder cb = new ConfigurationBuilder();
 cb.setDebugEnabled(true)
 .setOAuthConsumerKey("consumerKey")
 .setOAuthConsumerSecret("consumerSecret");
 configuration = cb.build();
 authorization = new OAuthAuthorization(configuration);
 twitterBaseImpl = new TwitterBaseImpl(configuration, authorization);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 Assert.assertNull(twitterBaseImpl.getScreenName());
 twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
 Assert.assertNull(twitterBaseImpl.getScreenName());
 twitterBaseImpl.fillInIDAndScreenName();
 Assert.assertNotNull(twitterBaseImpl.getScreenName());
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 Assert.assertEquals(-1L, twitterBaseImpl.getId());
 twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
 twitterBaseImpl.fillInIDAndScreenName();
 Assert.assertNotNull(twitterBaseImpl.getId());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithoutAuth() throws TwitterException, IllegalStateException {
 twitterBaseImpl.getScreenName();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithoutAuth() throws TwitterException, IllegalStateException {
 twitterBaseImpl.getId();
 }

 @Test
 public void testAddRateLimitStatusListener() {
 RateLimitStatusListener listener = new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 // do something
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 // do something
 }
 };
 twitterBaseImpl.addRateLimitStatusListener(listener);
 Assert.assertTrue(twitterBaseImpl.rateLimitStatusListeners.contains(listener));
 }

 @Test
 public void testOnRateLimitStatus() {
 Consumer<RateLimitStatusEvent> consumer = new Consumer<RateLimitStatusEvent>() {
 @Override
 public void accept(RateLimitStatusEvent event) {
 // do something
 }
 };
 twitterBaseImpl.onRateLimitStatus(consumer);
 }

 @Test
 public void testOnRateLimitReached() {
 Consumer<RateLimitStatusEvent> consumer = new Consumer<RateLimitStatusEvent>() {
 @Override
 public void accept(RateLimitStatusEvent event) {
 // do something
 }
 };
 twitterBaseImpl.onRateLimitReached(consumer);
 }

 @Test
 public void testHttpResponseReceived() throws TwitterException {
 HttpResponseEvent httpResponseEvent = new HttpResponseEvent(new HttpResponse(200), null, null);
 twitterBaseImpl.httpResponseReceived(httpResponseEvent);
 }

 @Test
 public void testGetAuthorization() {
 Assert.assertNotNull(twitterBaseImpl.getAuthorization());
 }

 @Test
 public void testGetConfiguration() {
 Assert.assertNotNull(twitterBaseImpl.getConfiguration());
 Assert.assertEquals("consumerKey", twitterBaseImpl.getConfiguration().getOAuthConsumerKey());
 }

 @Test
 public void testEnsureAuthorizationEnabled() {
 twitterBaseImpl.ensureAuthorizationEnabled();
 }

 @Test(expected = IllegalStateException.class)
 public void testEnsureAuthorizationDisabled() {
 twitterBaseImpl.ensureOAuthEnabled();
 }

 @Test(expected = IllegalStateException.class)
 public void testSetOAuthConsumerWithoutAuth() {
 twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
 }

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 OAuthRequestToken oauthRequestToken = twitterBaseImpl.getOAuthRequestToken();
 Assert.assertNotNull(oauthRequestToken);
 }

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 AccessToken oauthAccessToken = twitterBaseImpl.getOAuthAccessToken();
 Assert.assertNotNull(oauthAccessToken);
 }

 @Test
 public void testSetOAuthAccessToken() {
 AccessToken oauthAccessToken = new AccessToken("accessToken", "accessTokenSecret", null);
 twitterBaseImpl.setOAuthAccessToken(oauthAccessToken);
 Assert.assertEquals(oauthAccessToken, twitterBaseImpl.getOAuthAccessToken());
 }

 @Test
 public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
 AccessToken oauthAccessToken = twitterBaseImpl.getOAuthAccessToken("username", "password");
 Assert.assertNotNull(oauthAccessToken);
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuthAccessTokenWithoutScreenNameOrPassword() throws TwitterException {
 twitterBaseImpl.getOAuthAccessToken();
 }

 @Test
 public void testEquals() {
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(configuration, authorization);
 Assert.assertEquals(twitterBaseImpl, twitterBaseImpl2);
 }

 @Test
 public void testHashCode() {
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(configuration, authorization);
 Assert.assertEquals(twitterBaseImpl.hashCode(), twitterBaseImpl2.hashCode());
 }
}