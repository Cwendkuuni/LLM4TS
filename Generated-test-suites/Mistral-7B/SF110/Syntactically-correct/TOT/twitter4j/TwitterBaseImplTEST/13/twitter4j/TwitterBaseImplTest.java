package twitter4j.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.exceptions.TwitterException;
import twitter4j.http.HttpClient;
import twitter4j.auth.Authorization;
import twitter4j.auth.OAuth2Authorization;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.http.HttpResponse;
import twitter4j.http.HttpResponseEvent;
import twitter4j.http.HttpResponseListener;
import twitter4j.http.StatusLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration conf;
 private Authorization auth;
 private HttpClient http;

 @Before
 public void setUp() throws TwitterException {
 conf = new ConfigurationBuilder()
 .setDebugEnabled(true)
 .setOAuthConsumerKey("testConsumerKey")
 .setOAuthConsumerSecret("testConsumerSecret")
 .build();
 auth = new OAuthAuthorization(conf);
 http = HttpClientFactory.getInstance(conf.getHttpClientConfiguration());
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 }

 @After
 public void tearDown() {
 twitterBaseImpl = null;
 auth = null;
 http = null;
 conf = null;
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 assertNull(twitterBaseImpl.getScreenName());
 twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 System.out.println("Rate limit reached: " + event.getRateLimitStatus().getResetTimestamp());
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 fail("Rate limit reached unexpectedly.");
 }
 });
 twitterBaseImpl.getScreenName();
 assertEquals("testUser", twitterBaseImpl.getScreenName());
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 assertZero(twitterBaseImpl.getId());
 twitterBaseImpl.fillInIDAndScreenName();
 assertEquals(123456789L, twitterBaseImpl.getId());
 }

 @Test
 public void testFillInIDAndScreenName() throws TwitterException {
 twitterBaseImpl.ensureAuthorizationEnabled();
 User user = twitterBaseImpl.fillInIDAndScreenName();
 assertEquals("testUser", user.getScreenName());
 assertEquals(123456789L, user.getId());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithoutAuth() throws TwitterException, IllegalStateException {
 twitterBaseImpl = new TwitterBaseImpl(conf, null);
 twitterBaseImpl.getScreenName();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithoutAuth() throws TwitterException, IllegalStateException {
 twitterBaseImpl = new TwitterBaseImpl(conf, null);
 twitterBaseImpl.getId();
 }

 @Test
 public void testAddRateLimitStatusListener() {
 twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 System.out.println("Rate limit reached.");
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 System.out.println("Rate limit exceeded.");
 }
 });
 twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 System.out.println("Rate limit reset.");
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 System.out.println("Rate limit exceeded.");
 }
 });
 }

 @Test
 public void testHttpResponseReceived() throws TwitterException {
 HttpResponse response = new HttpResponse(new StatusLine(429, "Too Many Requests"), Arrays.asList("Retry-After: 60"), null);
 HttpResponseEvent event = new HttpResponseEvent(http, response);
 List<RateLimitStatusListener> listeners = new ArrayList<>();TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new ConfigurationBuilder().build(), null); twitterBaseImpl.onRateLimitReached(new Consumer<RateLimitStatusEvent>() {
 @Override public void accept(RateLimitStatusEvent event) {
 System.out.println("Rate limit reached.");
 }
 });
 twitterBaseImpl.onRateLimitStatus(new Consumer<RateLimitStatusEvent>() {
 @Override public void accept(RateLimitStatusEvent event) {
 System.out.println("Rate limit status: " + event.getRateLimitStatus().getResetTimestamp());
 }
 });
 twitterBaseImpl.httpResponseReceived(event);
 }

 @Test
 public void testGetAuthorization() {
 assertTrue(twitterBaseImpl.getAuthorization() instanceof OAuthAuthorization);
 }

 @Test
 public void testGetConfiguration() {
 assertEquals(conf, twitterBaseImpl.getConfiguration());
 }

 @Test
 public void testEnsureAuthorizationEnabled() throws TwitterException, IllegalStateException {
 twitterBaseImpl = new TwitterBaseImpl(null, null);
 assertFalse(twitterBaseImpl.isEnabled());
 twitterBaseImpl.ensureAuthorizationEnabled();
 assertTrue(twitterBaseImpl.isEnabled());
 }

 @Test
 public void testEnsureOAuthEnabled() throws TwitterException, IllegalStateException {
 twitterBaseImpl = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 assertFalse(twitterBaseImpl.isOAuthEnabled());
 twitterBaseImpl.ensureOAuthEnabled();
 assertTrue(twitterBaseImpl.isOAuthEnabled());
 }

 @Test
 public void testSetOAuthConsumer() throws TwitterException {
 Configuration confWithConsumerKeySecret = new ConfigurationBuilder().setOAuthConsumerKey("testConsumerKey")
 .setOAuthConsumerSecret("testConsumerSecret").build();
 twitterBaseImpl = new TwitterBaseImpl(confWithConsumerKeySecret, null);
 assertEquals("testConsumerKey", twitterBaseImpl.getAuthorization().getConsumerKey());
 assertEquals("testConsumerSecret", twitterBaseImpl.getAuthorization().getConsumerSecret());
 }

 @Test(expected = NullPointerException.class)
 public void testSetOAuthConsumerNullConsumerKey() throws TwitterException {
 Configuration confWithNullConsumerKey = new ConfigurationBuilder().setOAuthConsumerKey(null)
 .setOAuthConsumerSecret("testConsumerSecret").build();
 twitterBaseImpl = new TwitterBaseImpl(confWithNullConsumerKey, null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetOAuthConsumerNullConsumerSecret() throws TwitterException {
 Configuration confWithNullConsumerSecret = new ConfigurationBuilder().setOAuthConsumerKey("testConsumerKey")
 .setOAuthConsumerSecret(null).build();
 twitterBaseImpl = new TwitterBaseImpl(confWithNullConsumerSecret, null);
 }

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 assertNotNull(requestToken.getToken());
 assertNotNull(requestToken.getTokenSecret());
 }

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 assertNotNull(accessToken.getScreenName());
 assertNotNull(accessToken.getUserId());
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuthAccessTokenWithoutScreenName() throws TwitterException {
 twitterBaseImpl.getOAuthAccessToken(null);
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuthAccessTokenWithoutPassword() throws TwitterException {
 twitterBaseImpl.getOAuthAccessToken("testUser", null);
 }

 @Test
 public void testSetOAuthAccessToken() {
 AccessToken accessToken = new AccessToken("testScreenName", 123456789L, null, null, null);
 twitterBaseImpl.setOAuthAccessToken(accessToken);
 assertEquals("testScreenName", twitterBaseImpl.getScreenName());
 assertEquals(123456789L, twitterBaseImpl.getId());
 }

 @Test
 public void testGetOAuthAccessTokenWithScreenName() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("testUser");
 assertEquals("testUser", accessToken.getScreenName());
 assertEquals(123456789L, accessToken.getId());
 }

 @Test
 public void testGetOAuthAccessTokenWithPassword() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("testUser", "testPassword");
 assertEquals("testUser", accessToken.getScreenName());
 assertEquals(123456789L, accessToken.getId());
 }

 @Test
 public void testGetOAuthAccessTokenWithOAuthToken() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken((String)null, (String)null, new AccessToken("testToken", "testTokenSecret"));
 assertEquals("testToken", twitterBaseImpl.getScreenName());
 assertEquals(123456789L, twitterBaseImpl.getId());
 }

 @Test
 public void testGetOAuthAccessTokenWithOAuthTokenSecret() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken((String)null, "testTokenSecret", new AccessToken("testToken", null));
 assertEquals("testToken", twitterBaseImpl.getScreenName());
 assertEquals(123456789L, twitterBaseImpl.getId());
 }

 @Test
 public void testGetOAuthAccessTokenWithOAuthVerifier() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 String oauthVerifier = "testOAuthVerifier";
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, oauthVerifier);
 assertEquals("testOAuthVerifier", accessToken.getOAuthToken());
 }

 @Test
 public void testGetOAuthAccessTokenWithOAuthTokenAndOAuthTokenSecret() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(new AccessToken("testToken", "testTokenSecret"));
 assertEquals("testToken", twitterBaseImpl.getScreenName());
 assertEquals(123456789L, twitterBaseImpl.getId());
 }
}