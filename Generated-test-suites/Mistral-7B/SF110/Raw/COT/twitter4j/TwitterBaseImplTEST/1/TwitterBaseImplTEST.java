package twitter4j.test;

import static org.junit.Assert.*;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import org.junit.Test;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration conf;
 private Authorization auth;

 @Before
 public void setUp() {
 conf = new ConfigurationBuilder()
 .setDebugEnabled(true)
 .build();
 auth = new BasicAuthorization("testConsumerKey", "testConsumerSecret");
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 }

 @Test
 public void testGetScreenName() {
 assertNull(twitterBaseImpl.getScreenName());
 twitterBaseImpl.setFactory();
 twitterBaseImpl.init();
 assertNotNull(twitterBaseImpl.getScreenName());
 }

 @Test(expected = TwitterException.class)
 public void testGetScreenNameWithDisabledAuth() {
 twitterBaseImpl.getScreenName();
 }

 @Test
 public void testGetId() {
 assertNotNull(twitterBaseImpl.getId());
 twitterBaseImpl.setFactory();
 twitterBaseImpl.init();
 assertEquals(twitterBaseImpl.getId(), 123L); // Assuming the test ID is 123
 }

 @Test(expected = TwitterException.class)
 public void testGetIdWithDisabledAuth() {
 twitterBaseImpl.getId();
 }

 @Test
 public void testFillInIDAndScreenName() {
 twitterBaseImpl.fillInIDAndScreenName();
 assertNotNull(twitterBaseImpl.getScreenName());
 assertNotNull(twitterBaseImpl.getId());
 }

 @Test(expected = TwitterException.class)
 public void testFillInIDAndScreenNameWithDisabledAuth() {
 twitterBaseImpl.fillInIDAndScreenName();
 }

 @Test
 public void testAddRateLimitStatusListener() {
 RateLimitStatusListener listener = new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 // Handle rate limit status event
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 // Handle rate limit reached event
 }
 };
 twitterBaseImpl.addRateLimitStatusListener(listener);
 assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());
 }

 @Test
 public void testOnRateLimitStatus() {
 RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(null, 0, 0, 0, 0, 0), true);
 twitterBaseImpl.onRateLimitStatus(event);
 twitterBaseImpl.onRateLimitReached(event);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetOAuthConsumerWithNullConsumerKey() {
 twitterBaseImpl.setOAuthConsumer(null, "testConsumerSecret");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetOAuthConsumerWithNullConsumerSecret() {
 twitterBaseImpl.setOAuthConsumer("testConsumerKey", null);
 }

 @Test
 public void testGetOAuthRequestToken() {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 assertNotNull(requestToken.getToken());
 assertNotNull(requestToken.getTokenSecret());
 }

 @Test
 public void testGetOAuthAccessToken() {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 assertNotNull(accessToken.getScreenName());
 assertNotNull(accessToken.getUserId());
 }

 @Test
 public void testSetOAuthAccessToken() {
 AccessToken accessToken = new AccessToken("testScreenName", 123, "testToken", "testTokenSecret");
 twitterBaseImpl.setOAuthAccessToken(accessToken);
 assertEquals(accessToken.getScreenName(), twitterBaseImpl.getScreenName());
 assertEquals(accessToken.getId(), twitterBaseImpl.getId());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetOAuthAccessTokenWithNullAccessToken() {
 twitterBaseImpl.setOAuthAccessToken(null);
 }
}