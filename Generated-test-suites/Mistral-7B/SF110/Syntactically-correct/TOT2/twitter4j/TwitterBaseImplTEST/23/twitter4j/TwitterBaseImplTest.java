package twitter4j.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import twitter4j.auth.*;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration conf;
 private Authorization auth;

 @Before
 public void setUp() {
 Configuration conf = new Configuration();
 Authorization auth = new OAuthAuthorization();
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 }

 @Test
 public void testGetScreenName() {
 String expectedScreenName = "testUser";
 twitterBaseImpl.setScreenName(expectedScreenName);
 String actualScreenName = twitterBaseImpl.getScreenName();
 assertEquals(expectedScreenName, actualScreenName);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameThrowsExceptionWhenAuthorizationNotEnabled() {
 twitterBaseImpl = new TwitterBaseImpl(new Configuration(), null);
 try {
 twitterBaseImpl.getScreenName();
 fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testGetId() {
 long expectedId = 1L;
 twitterBaseImpl.setId(expectedId);
 long actualId = twitterBaseImpl.getId();
 assertEquals(expectedId, actualId);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdThrowsExceptionWhenAuthorizationNotEnabled() {
 twitterBaseImpl = new TwitterBaseImpl(new Configuration(), null);
 try {
 twitterBaseImpl.getId();
 fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testAddRateLimitStatusListener() {
 RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
 twitterBaseImpl.addRateLimitStatusListener(listener);
 assertTrue(twitterBaseImpl.getRateLimitStatusListeners().contains(listener));
 }

 @Test
 public void testOnRateLimitStatus() {
 Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
 RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
 twitterBaseImpl.onRateLimitStatus(action);
 twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(null, null, new RateLimitStatusEvent(twitterBaseImpl, null, true, null, null, null, null, null, null, null)));
 verify(action).accept(eq(event));
 }

 @Test
 public void testOnRateLimitReached() {
 Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
 RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
 twitterBaseImpl.onRateLimitReached(action);
 twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(null, null, new RateLimitStatusEvent(twitterBaseImpl, null, true, null, null, null, null, null, null, null, true)));
 verify(action).accept(eq(event));
 }

 @Test
 public void testHttpResponseReceivedWhenNoListeners() {
 HttpResponseEvent event = mock(HttpResponseEvent.class);
 twitterBaseImpl.httpResponseReceived(event);
 assertTrue(twitterBaseImpl.getRateLimitStatusListeners().isEmpty());
 }

 @Test
 public void testGetAuthorization() {
 Authorization expectedAuthorization = new OAuthAuthorization();
 twitterBaseImpl.setAuthorization(expectedAuthorization);
 Authorization actualAuthorization = twitterBaseImpl.getAuthorization();
 assertEquals(expectedAuthorization, actualAuthorization);
 }

 @Test
 public void testGetConfiguration() {
 Configuration expectedConfiguration = new Configuration();
 twitterBaseImpl.setConfiguration(expectedConfiguration);
 Configuration actualConfiguration = twitterBaseImpl.getConfiguration();
 assertEquals(expectedConfiguration, actualConfiguration);
 }

 @Test
 public void testGetOAuth() {
 OAuthSupport expectedOAuth = new OAuthAuthorization();
 twitterBaseImpl.setAuthorization(expectedOAuth);
 OAuthSupport actualOAuth = twitterBaseImpl.getOAuth();
 assertEquals(expectedOAuth, actualOAuth);
 }

 @Test
 public void testFillInIDAndScreenNameWithNullParameters() throws TwitterException {
 twitterBaseImpl.fillInIDAndScreenName(null);
 assertTrue(twitterBaseImpl.getId() == 0L);
 assertTrue(twitterBaseImpl.getScreenName() == null);
 }

 @Test
 public void testFillInIDAndScreenNameWithParameters() throws TwitterException {
 HttpParameter[] parameters = new HttpParameter[0];
 User user = new UserJSONImpl(new StatusJSONImpl(), conf);
 twitterBaseImpl.fillInIDAndScreenName(parameters);
 assertEquals(user.getId(), twitterBaseImpl.getId());
 assertEquals(user.getScreenName(), twitterBaseImpl.getScreenName());
 }

 @Test
 public void testWriteObject() throws IOException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutputStream os = new ObjectOutputStream(out);
 twitterBaseImpl.writeObject(os);
 byte[] serialized = out.toByteArray();
 TwitterBaseImpl deserialized = (TwitterBaseImpl)new ObjectInputStream(new ByteArrayInputStream(serialized)).readObject();
 assertEquals(twitterBaseImpl.toString(), deserialized.toString());
 }

 @Test
 public void testReadObject() throws IOException, ClassNotFoundException {
 ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
 ObjectInputStream is = new ObjectInputStream(in);
 twitterBaseImpl = (TwitterBaseImpl)is.readObject();
 assertNull(twitterBaseImpl.getAuthorization());
 assertNull(twitterBaseImpl.getConfiguration());
 assertTrue(twitterBaseImpl.getRateLimitStatusListeners().isEmpty());
 }

 @Test
 public void testSetOAuthConsumer() {
 String consumerKey = "testConsumerKey";
 String consumerSecret = "testConsumerSecret";
 twitterBaseImpl.setOAuthConsumer(consumerKey, consumerSecret);
 OAuthAuthorization actualAuthorization = (OAuthAuthorization)twitterBaseImpl.getAuthorization();
 assertEquals(consumerKey, actualAuthorization.getConsumerKey());
 assertEquals(consumerSecret, actualAuthorization.getConsumerSecret());
 }

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 RequestToken expectedRequestToken = new RequestToken("oauth_token", "oauth_token_secret");
 twitterBaseImpl.setOAuth(new OAuthAuthorization());
 RequestToken actualRequestToken = twitterBaseImpl.getOAuthRequestToken();
 assertEquals(expectedRequestToken.getOauthToken(), actualRequestToken.getOauthToken());
 assertEquals(expectedRequestToken.getOauthTokenSecret(), actualRequestToken.getOauthTokenSecret());
 }

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 AccessToken expectedAccessToken = new AccessToken("access_token", "access_token_secret");
 twitterBaseImpl.setAuthorization(new OAuthAuthorization());
 AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken();
 assertEquals(expectedAccessToken.getScreenName(), actualAccessToken.getScreenName());
 assertEquals(expectedAccessToken.getUserId(), actualAccessToken.getUserId());
 }

 @Test
 public void testSetOAuthAccessToken() {
 AccessToken accessToken = new AccessToken("access_token", "access_token_secret");
 twitterBaseImpl.setOAuthAccessToken(accessToken);
 AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken();
 assertEquals(accessToken.getScreenName(), actualAccessToken.getScreenName());
 assertEquals(accessToken.getUserId(), actualAccessToken.getUserId());
 }

 @Test
 public void testEquality() {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf, auth);
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
 assertTrue(twitterBaseImpl1.equals(twitterBaseImpl2));
 assertTrue(twitterBaseImpl1.hashCode() == twitterBaseImpl2.hashCode());
 }

 @Test
 public void testInequality() {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf, auth);
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(new Configuration(), auth);
 assertFalse(twitterBaseImpl1.equals(twitterBaseImpl2));
 }
}