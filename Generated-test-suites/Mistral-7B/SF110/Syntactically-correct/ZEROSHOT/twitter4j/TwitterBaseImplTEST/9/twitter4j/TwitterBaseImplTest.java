package test;

import static org.junit.Assert.*;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import org.junit.Test;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration configuration;
 private Authorization authorization;

 @Before
 public void setUp() {
 ConfigurationConf conf = new ConfigurationConf();
 conf.setOAuthConsumerKey("consumerKey");
 conf.setOAuthConsumerSecret("consumerSecret");
 configuration = conf.getConfiguration();
 authorization = new OAuthAuthorization(configuration);
 twitterBaseImpl = new TwitterBaseImpl(configuration, authorization);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 assertEquals("consumerKey", twitterBaseImpl.getScreenName());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithoutAuth() throws TwitterException, IllegalStateException {
 twitterBaseImpl.setAuthorization(null);
 try {
 twitterBaseImpl.getScreenName();
 } catch (IllegalStateException e) {
 assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
 }
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 assertEquals(0, twitterBaseImpl.getId());
 twitterBaseImpl.fillInIDAndScreenName();
 assertNotEquals(0, twitterBaseImpl.getId());
 }

 @Test(expected = TwitterException.class)
 public void testFillInIDAndScreenNameException() throws TwitterException {
 twitterBaseImpl.setAuthorization(null);
 try {
 twitterBaseImpl.fillInIDAndScreenName();
 } catch (TwitterException e) {
 assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
 }
 }

 @Test
 public void testAddRateLimitStatusListener() {
 twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 fail("onRateLimitStatus should not be called");
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 fail("onRateLimitReached should not be called");
 }
 });
 }

 @Test
 public void testHttpResponseReceived() {
 HttpResponseEvent event = new HttpResponseEvent(new HttpResponse(200, ""));
 twitterBaseImpl.httpResponseReceived(event);
 assertTrue(twitterBaseImpl.rateLimitStatusListeners.isEmpty());
 }

 @Test
 public void testOnRateLimitStatus() {
 RateLimitStatusListener listener = new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 fail("onRateLimitStatus should not be called");
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 assertEquals(RateLimitStatus.LIMIT_NONE, event.getRateLimitStatus());
 }
 };
 twitterBaseImpl.onRateLimitStatus(listener);
 twitterBaseImpl.onRateLimitReached(listener);
 }

 @Test
 public void testOnRateLimitReached() {
 RateLimitStatusListener listener = new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 fail("onRateLimitStatus should not be called");
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 fail("onRateLimitReached should not be called");
 }
 };
 twitterBaseImpl.onRateLimitReached(listener);
 twitterBaseImpl.onRateLimitStatus(listener);
 }

 @Test
 public void testSetOAuthConsumer() {
 twitterBaseImpl.setOAuthConsumer("newConsumerKey", "newConsumerSecret");
 assertNotEquals("consumerKey", twitterBaseImpl.getAuthorization().getConsumerKey());
 assertEquals("newConsumerSecret", twitterBaseImpl.getAuthorization().getConsumerSecret());
 }

 @Test(expected = NullPointerException.class)
 public void testSetOAuthConsumerNullConsumerKey() {
 twitterBaseImpl.setOAuthConsumer(null, "consumerSecret");
 }

 @Test(expected = NullPointerException.class)
 public void testSetOAuthConsumerNullConsumerSecret() {
 twitterBaseImpl.setOAuthConsumer("consumerKey", null);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetOAuthConsumerAlreadySet() {
 twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
 try {
 twitterBaseImpl.setOAuthConsumer("consumerKey2", "consumerSecret2");
 } catch (IllegalStateException e) {
 assertEquals("OAuth consumer key/secret pair already set.", e.getMessage());
 }
 }

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 assertNotNull(requestToken.getToken());
 assertNotNull(requestToken.getTokenSecret());
 }

 @Test
 public void testGetOAuthRequestTokenWithCallbackUrl() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken("http://callbackUrl");
 assertNotNull(requestToken.getToken());
 assertNotNull(requestToken.getTokenSecret());
 assertNotNull(requestToken.getAuthorizationUrl());
 }

 @Test
 public void testGetOAuthRequestTokenWithXAuthAccessType() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken("http://callbackUrl", "read");
 assertNotNull(requestToken.getToken());
 assertNotNull(requestToken.getTokenSecret());
 assertNotNull(requestToken.getAuthorizationUrl());
 }

 @Test
 public void testGetOAuthRequestTokenWithXAuthAccessTypeAndXAuthMode() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken("http://callbackUrl", "read", "approval");
 assertNotNull(requestToken.getToken());
 assertNotNull(requestToken.getTokenSecret());
 assertNotNull(requestToken.getAuthorizationUrl());
 }

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 assertNotNull(accessToken.getScreenName());
 assertNotNull(accessToken.getId());
 }

 @Test
 public void testGetOAuthAccessTokenWithOAuthVerifier() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("oauthVerifier");
 assertNotNull(accessToken.getScreenName());
 assertNotNull(accessToken.getId());
 }

 @Test
 public void testGetOAuthAccessTokenWithRequestToken() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken);
 assertNotNull(accessToken.getScreenName());
 assertNotNull(accessToken.getId());
 }

 @Test
 public void testGetOAuthAccessTokenWithRequestTokenAndOAuthVerifier() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, "oauthVerifier");
 assertNotNull(accessToken.getScreenName());
 assertNotNull(accessToken.getId());
 }

 @Test
 public void testSetOAuthAccessToken() {
 AccessToken accessToken = new AccessToken("screenName", 1L, "token", "tokenSecret");
 twitterBaseImpl.setOAuthAccessToken(accessToken);
 assertEquals("screenName", twitterBaseImpl.getScreenName());
 assertEquals(1L, twitterBaseImpl.getId());
 }

 @Test
 public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("screenName", "password");
 assertNotNull(accessToken.getScreenName());
 assertNotNull(accessToken.getId());
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuthAccessTokenWithInvalidCredentials() throws TwitterException {
 twitterBaseImpl.setAuthorization(null);
 try {
 twitterBaseImpl.getOAuthAccessToken("screenName", "password");
 } catch (TwitterException e) {
 assertEquals("Authentication credentials are missing. See http://twitter4j.org/en/configuration.html for details. See and register at http://apps.twitter.com/", e.getMessage());
 }
 }

 @Test
 public void testGetOAuth() {
 OAuthSupport oauth = twitterBaseImpl.getOAuth();
 assertNotNull(oauth);
 }

 @Test
 public void testGetOAuth2() {
 OAuth2Support oauth2 = twitterBaseImpl.getOAuth2();
 assertNotNull(oauth2);
 }

 @Test
 public void testEquals() {
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(configuration, authorization);
 assertTrue(twitterBaseImpl.equals(twitterBaseImpl2));
 twitterBaseImpl2.setAuthorization(null);
 assertFalse(twitterBaseImpl.equals(twitterBaseImpl2));
 }

 @Test
 public void testHashCode() {
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(configuration, authorization);
 int hashCode1 = twitterBaseImpl.hashCode();
 twitterBaseImpl.setAuthorization(null);
 int hashCode2 = twitterBaseImpl.hashCode();
 twitterBaseImpl2.setAuthorization(null);
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