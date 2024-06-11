package twitter4j.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;
import twitter4j.auth.*;

/**
 * Test class for TwitterBaseImpl
 */
public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration conf;
 private Authorization auth;

 @Before
 public void setUp() {
 conf = new ConfigurationBuilder()
 .setDebugEnabled(true)
 .setOAuthConsumerKey("testKey")
 .setOAuthConsumerSecret("testSecret")
 .build();
 auth = new OAuthAuthorization(conf);
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 // Test without authentication
 assertNull(twitterBaseImpl.getScreenName());
 twitterBaseImpl.setOAuthAccessToken(new AccessToken("1234567890", "abcdefg1234567890"));
 // Test with authentication
 assertEquals("testScreenName", twitterBaseImpl.getScreenName());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithoutAuthentication() throws TwitterException, IllegalStateException {
 twitterBaseImpl.getScreenName();
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 // Test without authentication
 assertEquals(0L, twitterBaseImpl.getId());
 twitterBaseImpl.setOAuthAccessToken(new AccessToken("1234567890", "abcdefg1234567890"));
 // Test with authentication
 assertEquals(1234567890L, twitterBaseImpl.getId());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithoutAuthentication() throws TwitterException, IllegalStateException {
 twitterBaseImpl.getId();
 }

 @Test
 public void testFillInIDAndScreenName() throws TwitterException {
 // Test without authentication
 assertNull(twitterBaseImpl.getScreenName());
 assertEquals(0L, twitterBaseImpl.getId());
 twitterBaseImpl.fillInIDAndScreenName();
 // Test with authentication
 assertEquals("testScreenName", twitterBaseImpl.getScreenName());
 assertEquals(1234567890L, twitterBaseImpl.getId());
 }

 @Test
 public void testAddRateLimitStatusListener() {
 twitterBaseImpl.addRateLimitStatusListener(new TestRateLimitStatusListener());
 twitterBaseImpl.addRateLimitStatusListener(new TestRateLimitStatusListener());
 twitterBaseImpl.onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, null, true));
 twitterBaseImpl.onRateLimitReached(new RateLimitStatusEvent(twitterBaseImpl, null, true));
}

 @Test
 public void testOnRateLimitStatus() {
 twitterBaseImpl.addRateLimitStatusListener(new TestRateLimitStatusListener());
 twitterBaseImpl.onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, null, true));
 twitterBaseImpl.onRateLimitReached(new RateLimitStatusEvent(twitterBaseImpl, null, true));
 twitterBaseImpl.removeRateLimitStatusListener(new TestRateLimitStatusListener());
 assertTrue(twitterBaseImpl.getRateLimitStatusListeners().isEmpty());
}

 @Test
 public void testHttpResponseReceived() throws TwitterException {
 twitterBaseImpl.onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, null, true));
 twitterBaseImpl.onRateLimitReached(new RateLimitStatusEvent(twitterBaseImpl, null, true));
}

 @Test
 public void testGetAuthorization() {
 assertEquals(auth, twitterBaseImpl.getAuthorization());
}

 @Test
 public void testGetConfiguration() {
 assertEquals(conf, twitterBaseImpl.getConfiguration());
}

 @Test
 public void testEnsureAuthorizationEnabled() {
 twitterBaseImpl.ensureAuthorizationEnabled();
}

 @Test(expected = IllegalStateException.class)
 public void testEnsureAuthorizationDisabled() {
 twitterBaseImpl = new TwitterBaseImpl(conf, null);
 twitterBaseImpl.ensureAuthorizationEnabled();
}

 @Test
 public void testWriteObject() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(baos);
 twitterBaseImpl.writeObject(out);
 byte[] bytes = baos.toByteArray();
 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TwitterBaseImpl twitterBaseImpl2 = (TwitterBaseImpl)in.readObject();
 assertEquals(twitterBaseImpl, twitterBaseImpld2);
}

 @Test
 public void testReadObject() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(baos);
 out.writeObject(twitterBaseImpl);
 byte[] bytes = baos.toByteArray();
 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TwitterBaseImpl twitterBaseImpld2 = (TwitterBaseImpl)in.readObject();
 assertEquals(twitterBaseImpl, twitterBaseImpld2);
}

 @Test
 public void testSetOAuthConsumer() throws TwitterException {
 Configuration conf2 = new ConfigurationBuilder()
 .setDebugEnabled(true)
 .setOAuthConsumerKey("testKey2")
 .setOAuthConsumerSecret("testSecret2")
 .build();
 twitterBaseImpl.setOAuthConsumer("testKey2", "testSecret2");
 assertEquals("testKey2", twitterBaseImpl.getAuthorization().getOAuthConsumerKey());
 assertEquals("testSecret2", twitterBaseImpl.getAuthorization().getOAuthConsumerSecret());
 twitterBaseImpl.setOAuthConsumer(conf2.getOAuthConsumerKey(), conf2.getOAuthConsumerSecret());
 assertEquals("testKey2", twitterBaseImpl.getAuthorization().getOAuthConsumerKey());
 assertEquals("testSecret2", twitterBaseImpl.getAuthorization().getOAuthConsumerSecret());
}

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 assertNotNull(requestToken);
}

 @Test
 public void testGetOAuthRequestTokenWithCallbackUrl() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken("http://example.com");
 assertNotNull(requestToken);
}

 @Test
 public void testGetOAuthRequestTokenWithCallbackUrlAndXAuthAccessType() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken("http://example.com", "read");
 assertNotNull(requestToken);
}

 @Test
 public void testGetOAuthRequestTokenWithCallbackUrlAndXAuthAccessTypeAndXAuthMode() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken("http://example.com", "read", "device");
 assertNotNull(requestToken);
}

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 assertNotNull(accessToken);
}

 @Test
 public void testGetOAuthAccessTokenWithOAuthVerifier() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken.getOAuthToken(), requestToken.getOAuthTokenSecret());
 assertNotNull(accessToken);
}

 @Test
 public void testGetOAuthAccessTokenWithRequestToken() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken);
 assertNotNull(accessToken);
}

 @Test
 public void testGetOAuthAccessTokenWithRequestTokenAndOAuthVerifier() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, "verifier");
 assertNotNull(accessToken);
}

 @Test
 public void testSetOAuthAccessToken() throws TwitterException {
 AccessToken accessToken = new AccessToken("testScreenName", "testAccessToken", "testAccessSecret");
 twitterBaseImpl.setOAuthAccessToken(accessToken);
 assertEquals(accessToken.getScreenName(), twitterBaseImpl.getScreenName());
 assertEquals(accessToken.getAccessToken(), twitterBaseImpl.getAuthorization().getAccessToken());
 assertEquals(accessToken.getAccessTokenSecret(), twitterBaseImpl.getAuthorization().getAccessTokenSecret());
}

 @Test
 public void testSetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
 String screenName = "testScreenName";
 String password = "testPassword";
 twitterBaseImpl.setOAuthAccessToken(screenName, password);
 assertEquals(screenName, twitterBaseImpl.getScreenName());
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 assertEquals(screenName, accessToken.getScreenName());
 assertEquals(password, accessToken.getAccessTokenSecret());
}

 @Test
 public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
 String screenName = "testScreenName";
 String password = "testPassword";
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(screenName, password);
 assertEquals(screenName, accessToken.getScreenName());
 assertEquals(password, accessToken.getAccessTokenSecret());
}

 @Test
 public void testGetOAuthToken() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 assertEquals(accessToken.getAccessToken(), twitterBaseImpl.getAuthorization().getAccessToken());
}

 @Test
 public void testGetOAuthTokenSecret() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 assertEquals(accessToken.getAccessTokenSecret(), twitterBaseImpl.getAuthorization().getAccessTokenSecret());
}

 @Test
 public void testGetOAuthScreenName() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 assertEquals(accessToken.getScreenName(), twitterBaseImpl.getScreenName());
}

 @Test
 public void testGetOAuthId() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 assertEquals(accessToken.getId(), twitterBaseImpl.getId());
}

 @Test
 public void testGetOAuthAccessTokenWithCallbackUrl() throws TwitterException {
 String callbackUrl = "http://example.com";
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken(callbackUrl);
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, "verifier");
 assertEquals(callbackUrl, twitterBaseImpl.getAuthorization().getOAuthConsumer().getCallbackUrl());
}

 @Test
 public void testGetOAuthTokenWithCallbackUrl() throws TwitterException {
 String callbackUrl = "http://example.com";
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken(callbackUrl);
 assertEquals(callbackUrl, twitterBaseImpl.getAuthorization().getOAuthConsumer().getCallbackUrl());
}

 @Test
 public void testGetOAuthAccessTokenWithRequestTokenNull() throws TwitterException {
 twitterBaseImpl.setOAuthAccessToken(new AccessToken("testScreenName", "testAccessToken", "testAccessSecret"));
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 assertEquals(twitterBaseImpl.getScreenName(), accessToken.getScreenName());
 assertEquals(twitterBaseImpl.getAuthorization().getAccessToken(), accessToken.getAccessToken());
 assertEquals(twitterBaseImpl.getAuthorization().getAccessTokenSecret(), accessToken.getAccessTokenSecret());
}

 @Test
 public void testGetOAuthAccessTokenWithRequestTokenAndOAuthVerifierNull() throws TwitterException {
 twitterBaseImpl.setOAuthAccessToken(new AccessToken("testScreenName", "testAccessToken", "testAccessSecret"));
 RequestToken requestToken = new RequestToken("testToken", "testTokenSecret");
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, null);
 assertEquals(twitterBaseImpl.getScreenName(), accessToken.getScreenName());
 assertEquals(twitterBaseImpl.getAuthorization().getAccessToken(), accessToken.getAccessToken());
 assertEquals(twitterBaseImpl.getAuthorization().getAccessTokenSecret(), accessToken.getAccessTokenSecret());
}
}

 /**
 * Test rate limit status listener
 */
 class TestRateLimitStatusListener implements RateLimitStatusListener {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 // Test implementation
}

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 // Test implementation
}
 }