package twitter4j.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.impl.TwitterBaseImpl;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;

 @Before
 public void setup() {
 ConfigurationBuilder config = new ConfigurationBuilder();
 config.setDebugEnabled(true)
 .setOAuthConsumerKey("CONSUMER_KEY")
 .setOAuthConsumerSecret("CONSUMER_SECRET")
 .setOAuthAccessToken("ACCESS_TOKEN")
 .setOAuthAccessTokenSecret("ACCESS_TOKEN_SECRET");
 TwitterFactory factory = new TwitterFactory(config.build());
 twitterBaseImpl = (TwitterBaseImpl) factory.getInstance();
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 assertNull(twitterBaseImpl.getScreenName());
 twitterBaseImpl.init();
 assertNotNull(twitterBaseImpl.getScreenName());
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 assertFalse(twitterBaseImpl.getAuthorization().isEnabled());
 twitterBaseImpl.init();
 assertTrue(twitterBaseImpl.getAuthorization().isEnabled());
 assertFalse(twitterBaseImpl.getId() > 0);
 User user = twitterBaseImpl.fillInIDAndScreenName();
 assertTrue(twitterBaseImpl.getId() > 0);
 assertEquals(user.getScreenName(), twitterBaseImpl.getScreenName());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithDisabledAuthentication() throws TwitterException, IllegalStateException {
 twitterBaseImpl.getScreenName();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithDisabledAuthentication() throws TwitterException, IllegalStateException {
 twitterBaseImpl.fillInIDAndScreenName();
 }

 @Test
 public void testAddRateLimitStatusListener() {
 twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 fail("onRateLimitStatus should not be called as no rate limit reached");
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 // Do nothing
 }
 });
 twitterBaseImpl.onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(0, 100, 1000), true));
 twitterBaseImpl.onRateLimitReached(new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(0, 100, 1000), true));
 }

 @Test
 public void testHttpResponseReceived() throws TwitterException {
 twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetOAuthConsumerWithNullConsumerKey() {
 twitterBaseImpl.setOAuthConsumer(null, "CONSUMER_SECRET");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetOAuthConsumerWithNullConsumerSecret() {
 twitterBaseImpl.setOAuthConsumer("CONSUMER_KEY", null);
 }

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 assertNotNull(requestToken);
 }

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 assertNotNull(accessToken);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetOAuthAccessTokenWithDisabledAuthentication() throws TwitterException {
 twitterBaseImpl.getOAuthAccessToken();
 }

 @Test
 public void testSetOAuthAccessToken() {
 AccessToken accessToken = new AccessToken(123456789L, "access_token");
 twitterBaseImpl.setOAuthAccessToken(accessToken);
 assertEquals(accessToken.getScreenName(), twitterBaseImpl.getScreenName());
 assertEquals(accessToken.getUserId(), twitterBaseImpl.getId());
 }

 @Test
 public void testEquals() {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(new ConfigurationBuilder().build(), new NullAuthorization());
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(new ConfigurationBuilder().build(), new NullAuthorization());
 assertFalse(twitterBaseImpl1.equals(twitterBaseImpl2));
 twitterBaseImpl2.setAuthorization(twitterBaseImpl1.getAuthorization());
 assertTrue(twitterBaseImpl1.equals(twitterBaseImpl2));
 }
}