import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import twitter4j.Authorization;
import twitter4j.Configuration;
import twitter4j.OAuth2Authorization;
import twitter4j.OAuth2Token;
import twitter4j.OAuthSupport;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.TwitterBase;
import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwitterBaseImplTest {

 private TwitterBase twitterBase;
 private Configuration configuration;
 private Authorization auth;

 @Before
 public void setUp() {
 ConfigurationBuilder cb = new ConfigurationBuilder();
 configuration = cb.build();
 auth = new OAuth2Authorization(configuration);
 twitterBase = new TwitterBaseImpl(configuration, auth);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 String screenName = twitterBase.getScreenName();
 Assert.assertNotNull(screenName);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithDisabledAuth() throws IllegalStateException {
 twitterBase.setAuthorization(null);
 Assert.assertThrows(IllegalStateException.class, () -> twitterBase.getScreenName());
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 long id = twitterBase.getId();
 Assert.assertEquals(0, id);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithDisabledAuth() throws IllegalStateException {
 twitterBase.setAuthorization(null);
 Assert.assertThrows(IllegalStateException.class, () -> twitterBase.getId());
 }

 @Test
 public void testFillInIDAndScreenName() throws TwitterException {
 User user = twitterBase.fillInIDAndScreenName();
 Assert.assertNotNull(user);
 Assert.assertNotNull(user.getScreenName());
 Assert.assertNotNull(user.getId());
 }

 @Test(expected = TwitterException.class)
 public void testFillInIDAndScreenNameWithTwitterException() throws TwitterException {
 twitterBase.setHttpResponseReceivedHandler(event -> {
 throw new TwitterException("Test Exception", 500, new HttpResponseEvent(500, "Test Exception", null));
 });
 Assert.assertThrows(TwitterException.class, () -> twitterBase.fillInIDAndScreenName());
 }

 @Test
 public void testAddRateLimitStatusListener() {
 twitterBase.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 // Do something
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 // Do something
 }
 });
 }

 @Test
 public void testHttpResponseReceived() {
 twitterBase.onRateLimitStatus(new RateLimitStatusEvent(twitterBase, null, true));
 }

 @Test
 public void testGetAuthorization() {
 Authorization auth = twitterBase.getAuthorization();
 Assert.assertNotNull(auth);
 }

 @Test
 public void testGetConfiguration() {
 Configuration configuration = twitterBase.getConfiguration();
 Assert.assertEquals(this.configuration, configuration);
 }

 @Test
 public void testSetOAuthConsumer() {
 twitterBase.setOAuthConsumer("consumerKey", "consumerSecret");
 Authorization auth = twitterBase.getAuthorization();
 OAuth2Authorization oauth2Auth = (OAuth2Authorization) auth;
 Assert.assertEquals("consumerKey", oauth2Auth.getConsumerKey());
 Assert.assertEquals("consumerSecret", oauth2Auth.getConsumerSecret());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetOAuthConsumerWithOAuthAuth() {
 twitterBase.setAuthorization(new OAuthAuthorization(configuration));
 Assert.assertThrows(IllegalStateException.class, () -> twitterBase.setOAuthConsumer("consumerKey", "consumerSecret"));
 }

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 RequestToken requestToken = twitterBase.getOAuthRequestToken();
 Assert.assertNotNull(requestToken);
 }

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 AccessToken accessToken = twitterBase.getOAuthAccessToken();
 Assert.assertNotNull(accessToken);
 }

 @Test
 public void testSetOAuthAccessToken() {
 AccessToken accessToken = new AccessToken(1L, "testAccessToken");
 twitterBase.setOAuthAccessToken(accessToken);
 Assert.assertEquals(accessToken, twitterBase.getOAuthAccessToken());
 }

 @Test
 public void testEquals() {
 TwitterBaseImpl twitterBase1 = new TwitterBaseImpl(configuration, auth);
 TwitterBaseImpl twitterBase2 = new TwitterBaseImpl(configuration, auth);
 Assert.assertEquals(twitterBase1, twitterBase2);
 }

 @Test
 public void testHashCode() {
 TwitterBaseImpl twitterBase1 = new TwitterBaseImpl(configuration, auth);
 TwitterBaseImpl twitterBase2 = new TwitterBaseImpl(configuration, auth);
 Assert.assertEquals(twitterBase1.hashCode(), twitterBase2.hashCode());
 }

 @Test
 public void testToString() {
 String toString = twitterBase.toString();
 Assert.assertNotNull(toString);
 }
}