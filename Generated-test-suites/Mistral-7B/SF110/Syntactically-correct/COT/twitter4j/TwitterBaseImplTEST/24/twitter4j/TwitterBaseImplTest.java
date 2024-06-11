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
 conf = new Configuration();
 auth = new BasicAuthorization("testConsumerKey", "testConsumerSecret");
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 // Basic test
 String screenName = twitterBaseImpl.getScreenName();
 assertNotNull(screenName);

 // Test with null auth
 assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getScreenName());
 twitterBaseImpl.setAuthorization(null);
 assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getScreenName());

 // Test with empty screenName
 twitterBaseImpl.setAuthorization(new BasicAuthorization("", ""));
 assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getScreenName());
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 // Basic test
 long id = twitterBaseImpl.getId();
 assertNotNull(id);

 // Test with null auth
 assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getId());
 twitterBaseImpl.setAuthorization(null);
 assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getId());

 // Test with id = 0
 twitterBaseImpl.fillInIDAndScreenName();
 assertEquals(0L, twitterBaseImpl.getId());
 }

 @Test
 public void testFillInIDAndScreenName() throws TwitterException {
 // Basic test
 twitterBaseImpl.fillInIDAndScreenName();
 assertNotNull(twitterBaseImpl.getScreenName());
 assertNotNull(twitterBaseImpl.getId());

 // Test with null parameters
 twitterBaseImpl.fillInIDAndScreenName(null);
 assertNotNull(twitterBaseImpl.getScreenName());
 assertNotNull(twitterBaseImpl.getId());

 // Test with empty parameters
 twitterBaseImpl.fillInIDAndScreenName(new HttpParameter[0]);
 assertNotNull(twitterBaseImpl.getScreenName());
 assertNotNull(twitterBaseImpl.getId());
 }

 @Test
 public void testAddRateLimitStatusListener() {
 // Basic test
 twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 // Implementation
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 // Implementation
 }
 });

 // Test with duplicate listener
 twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 // Implementation
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 // Implementation
 }
 });
 twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 // Implementation
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 // Implementation
 }
 });
 assertEquals(2, twitterBaseImpl.getRateLimitStatusListeners().size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddNullRateLimitStatusListener() {
 twitterBaseImpl.addRateLimitStatusListener(null);
 }

 @Test
 public void testOnRateLimitStatus() {
 final List<RateLimitStatusEvent> rateLimitStatusEvents = new ArrayList<>();
 twitterBaseImpl.onRateLimitStatus((Consumer<RateLimitStatusEvent>)(event -> rateLimitStatusEvents.add(event)));
 assertFalse(rateLimitStatusEvents.isEmpty());
 }

 @Test
 public void testHttpResponseReceived() {
 final List<RateLimitStatusEvent> rateLimitStatusEvents = new ArrayList<>();
 twitterBaseImpl.onRateLimitStatus((Consumer<RateLimitStatusEvent>)(event -> rateLimitStatusEvents.add(event)));
 twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
 assertFalse(rateLimitStatusEvents.isEmpty());
 }

 @Test
 public void testGetAuthorization() {
 assertNotNull(twitterBaseImpl.getAuthorization());
 }

 @Test
 public void testGetConfiguration() {
 assertNotNull(twitterBaseImpl.getConfiguration());
 }

 @Test
 public void testEnsureAuthorizationEnabled() {
 assertThrows(IllegalStateException.class, () -> twitterBaseImpl.ensureAuthorizationEnabled());
 twitterBaseImpl.setAuthorization(NullAuthorization.getInstance());
 assertThrows(IllegalStateException.class, () -> twitterBaseImpl.ensureAuthorizationEnabled());
 }

 @Test
 public void testSetOAuthConsumer() {
 // Basic test
 twitterBaseImpl.setOAuthConsumer("testConsumerKey", "testConsumerSecret");
 assertNotNull(twitterBaseImpl.getAuthorization());

 // Test with null consumerKey or consumerSecret
 twitterBaseImpl.setOAuthConsumer(null, "testConsumerSecret");
 assertNotNull(twitterBaseImpl.getAuthorization());
 twitterBaseImpl.setOAuthConsumer("testConsumerKey", null);
 assertNotNull(twitterBaseImpl.getAuthorization());

 // Test with empty consumerKey or consumerSecret
 twitterBaseImpl.setOAuthConsumer("", "");
 assertNotNull(twitterBaseImpl.getAuthorization());
 }

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 // Basic test
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 assertNotNull(requestToken);

 // Test with callbackUrl
 RequestToken requestTokenCallbackUrl = twitterBaseImpl.getOAuthRequestToken("testCallbackUrl");
 assertNotNull(requestTokenCallbackUrl);

 // Test with xAuthAccessType
 RequestToken requestTokenXAuthAccessType = twitterBaseImpl.getOAuthRequestToken("testCallbackUrl", "read");
 assertNotNull(requestTokenXAuthAccessType);

 // Test with xAuthAccessType and xAuthMode
 RequestToken requestTokenXAuthAccessTypeAndMode = twitterBaseImpl.getOAuthRequestToken("testCallbackUrl", "read", "approval");
 assertNotNull(requestTokenXAuthAccessTypeAndMode);
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuthRequestTokenWithoutAuthorization() throws TwitterException {
 twitterBaseImpl.setAuthorization(NullAuthorization.getInstance());
 twitterBaseImpl.getOAuthRequestToken();
 }

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 // Basic test
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 assertNotNull(accessToken);
 assertNotNull(accessToken.getScreenName());
 assertNotNull(accessToken.getId());

 // Test with oauthVerifier
 AccessToken accessTokenWithVerifier = twitterBaseImpl.getOAuthAccessToken("testOauthVerifier");
 assertNotNull(accessTokenWithVerifier);
 assertNotNull(accessTokenWithVerifier.getScreenName());
 assertNotNull(accessTokenWithVerifier.getId());

 // Test with requestToken
 AccessToken accessTokenWithRequestToken = twitterBaseImpl.getOAuthAccessToken(twitterBaseImpl.getOAuthRequestToken());
 assertNotNull(accessTokenWithRequestToken);
 assertNotNull(accessTokenWithRequestToken.getScreenName());
 assertNotNull(accessTokenWithRequestToken.getId());

 // Test with oauthVerifier and requestToken
 AccessToken accessTokenWithVerifierAndRequestToken = twitterBaseImpl.getOAuthAccessToken(twitterBaseImpl.getOAuthRequestToken(), "testOauthVerifier");
 assertNotNull(accessTokenWithVerifierAndRequestToken);
 assertNotNull(accessTokenWithVerifierAndRequestToken.getScreenName());
 assertNotNull(accessTokenWithVerifierAndRequestToken.getId());
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuthAccessTokenWithoutAuthorization() throws TwitterException {
 twitterBaseImpl.setAuthorization(NullAuthorization.getInstance());
 twitterBaseImpl.getOAuthAccessToken();
 }

 @Test
 public void testSetOAuthAccessToken() {
 // Basic test
 AccessToken accessToken = new AccessToken("testScreenName", 1L, "testAccessToken");
 twitterBaseImpl.setOAuthAccessToken(accessToken);
 assertNotNull(twitterBaseImpl.getScreenName());
 assertNotNull(twitterBaseImpl.getId());
 assertEquals("testAccessToken", twitterBaseImpl.getAccessToken());

 // Test with null accessToken
 twitterBaseImpl.setOAuthAccessToken(null);
 assertNull(twitterBaseImpl.getScreenName());
 assertNull(twitterBaseImpl.getId());
 assertNull(twitterBaseImpl.getAccessToken());
 }

 @Test
 public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("testScreenName", "testPassword");
 assertNotNull(accessToken);
 assertNotNull(accessToken.getScreenName());
 assertNotNull(accessToken.getId());
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuthAccessTokenWithoutScreenNameAndPassword() throws TwitterException {
 twitterBaseImpl.getOAuthAccessToken();
 }

 @Test
 public void testGetOAuth() {
 assertNotNull(twitterBaseImpl.getOAuth());
 }

 @Test
 public void testGetOAuth2() {
 assertNotNull(twitterBaseImpl.getOAuth2());
 }

 @Test
 public void testGetOAuth2Token() throws TwitterException {
 OAuth2Token oAuth2Token = twitterBaseImpl.getOAuth2Token();
 assertNotNull(oAuth2Token);
 assertNotNull(oAuth2Token.getTokenType());
 assertNotNull(oAuth2Token.getAccessToken());
 }

 @Test
 public void testSetOAuth2Token() {
 OAuth2Token oAuth2Token = new OAuth2Token("bearer", "testAccessToken");
 twitterBaseImpl.setOAuth2Token(oAuth2Token);
 assertNotNull(twitterBaseImpl.getAccessToken());
 assertEquals("testAccessToken", twitterBaseImpl.getAccessToken());
 }

 @Test
 public void testInvalidateOAuth2Token() throws TwitterException {
 twitterBaseImpl.invalidateOAuth2Token();
 assertNull(twitterBaseImpl.getAccessToken());
 }

 @Test
 public void testEquals() {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf, auth);
 assertTrue(twitterBaseImpl.equals(twitterBaseImpl1));

 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
 assertTrue(twitterBaseImpl.equals(twitterBaseImpl2));

 TwitterBaseImpl twitterBaseImpl3 = new TwitterBaseImpl(conf, null);
 assertFalse(twitterBaseImpl.equals(twitterBaseImpl3));

 TwitterBaseImpl twitterBaseImpl4 = new TwitterBaseImpl(null, auth);
 assertFalse(twitterBaseImpl.equals(twitterBaseImpl4));

 TwitterBaseImpl twitterBaseImpl5 = new TwitterBaseImpl(conf, new OAuth2Authorization(conf));
 assertFalse(twitterBaseImpl.equals(twitterBaseImpl5));
 }

 @Test
 public void testHashCode() {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf, auth);
 int hashCode1 = twitterBaseImpl1.hashCode();

 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
 int hashCode2 = twitterBaseImpl2.hashCode();

 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 String toString = twitterBaseImpl.toString();
 assertTrue(toString.contains("TwitterBase{conf="));
 assertTrue(toString.contains("http="));
 assertTrue(toString.contains("rateLimitStatusListeners="));
 assertTrue(toString.contains("auth="));
 }
}