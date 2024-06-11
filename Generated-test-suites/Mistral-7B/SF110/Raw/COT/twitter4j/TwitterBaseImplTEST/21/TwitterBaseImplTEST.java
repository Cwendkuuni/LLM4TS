package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.Configuration;
import twitter4j.OAuthAuthorization;
import twitter4j.OAuthConsumer;
import twitter4j.TwitterBase;
import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwitterBaseImplTest {

 TwitterBase twitterBase;

 @Test
 public void testTwitterBaseImpl_constructor() {
 Configuration conf = new ConfigurationBuilder().build();
 OAuthAuthorization auth = new OAuthAuthorization(conf);
 twitterBase = new TwitterBaseImpl(conf, auth);
 Assert.assertNotNull(twitterBase.getScreenName());
 Assert.assertNotNull(twitterBase.getId());
 }

 @Test(expected = IllegalStateException.class)
 public void testTwitterBaseImpl_constructor_noAuthCredentials() {
 Configuration conf = new ConfigurationBuilder().build();
 twitterBase = new TwitterBaseImpl(conf, null);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 twitterBase = new TwitterBaseImpl(new ConfigurationBuilder().build(), new OAuthAuthorization(new ConfigurationBuilder().build()));
 twitterBase.setScreenName("testUser");
 Assert.assertEquals("testUser", twitterBase.getScreenName());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenName_noScreenNameSet() throws TwitterException, IllegalStateException {
 twitterBase = new TwitterBaseImpl(new ConfigurationBuilder().build(), new OAuthAuthorization(new ConfigurationBuilder().build()));
 Assert.assertNull(twitterBase.getScreenName());
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 twitterBase = new TwitterBaseImpl(new ConfigurationBuilder().build(), new OAuthAuthorization(new ConfigurationBuilder().build()));
 twitterBase.setId(1L);
 Assert.assertEquals(1L, twitterBase.getId());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetId_noIDSet() throws TwitterException, IllegalStateException {
 twitterBase = new TwitterBaseImpl(new ConfigurationBuilder().build(), new OAuthAuthorization(new ConfigurationBuilder().build()));
 Assert.assertNull(twitterBase.getId());
 }

 @Test
 public void testFillInIDAndScreenName() throws TwitterException {
 Configuration conf = new ConfigurationBuilder().build();
 twitterBase = new TwitterBaseImpl(conf, null);
 twitterBase.fillInIDAndScreenName();
 Assert.assertNotNull(twitterBase.getScreenName());
 Assert.assertNotNull(twitterBase.getId());
 }

 @Test
 public void testAddRateLimitStatusListener() {
 twitterBase = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 List<RateLimitStatusListener> listeners = new ArrayList<>();
 twitterBase.addRateLimitStatusListener((listener) -> System.out.println("Listener added"));
 twitterBase.onRateLimitStatus((event) -> System.out.println("Rate limit event received"));
 twitterBase.onRateLimitReached((event) -> System.out.println("Rate limit reached event received"));
 Assert.assertEquals(3, listeners.size());
 }

 @Test
 public void testHttpResponseReceived() throws TwitterException {
 twitterBase = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 List<RateLimitStatusListener> listeners = new ArrayList<>();
 twitterBase.addRateLimitStatusListener((listener) -> listeners.add(listener));
 twitterBase.onRateLimitStatus((event) -> {});
 twitterBase.onRateLimitReached((event) -> {});
 twitterBase.httpResponseReceived(new HttpResponseEvent(new HttpResponse(), new TwitterException(429, "Rate limit reached")));
 Assert.assertEquals(2, listeners.size());
 }

 @Test
 public void testEquals() {
 TwitterBaseImpl twitterBase1 = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 TwitterBaseImpl twitterBase2 = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 Assert.assertFalse(twitterBase1.equals(twitterBase2));
 TwitterBaseImpl twitterBase3 = new TwitterBaseImpl(twitterBase1);
 Assert.assertTrue(twitterBase1.equals(twitterBase3));
 }

 @Test
 public void testHashCode() {
 TwitterBaseImpl twitterBase1 = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 TwitterBaseImpl twitterBase2 = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 Assert.assertNotEquals(twitterBase1.hashCode(), twitterBase2.hashCode());
 }

 @Test
 public void testToString() {
 TwitterBaseImpl twitterBase = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 Assert.assertEquals("TwitterBase{conf=Configuration{...}, http=null, rateLimitStatusListeners=[], auth=null}", twitterBase.toString());
 }

 @Test
 public void testSetOAuthConsumer() {
 Configuration conf = new ConfigurationBuilder().build();
 OAuthConsumer consumer = new OAuthConsumer("consumerKey", "consumerSecret");
 twitterBase = new TwitterBaseImpl(conf, null);
 twitterBase.setOAuthConsumer("consumerKey2", "consumerSecret2");
 Assert.assertEquals("consumerKey2", twitterBase.getAuthorization().getConsumerKey());
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuthRequestToken_nullCallbackUrl() throws TwitterException {
 twitterBase = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 twitterBase.getOAuth().getOAuthRequestToken(null);
 }

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 twitterBase = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 RequestToken requestToken = twitterBase.getOAuth().getOAuthRequestToken("http://example.com/oauth_callback");
 Assert.assertNotNull(requestToken.getToken());
 Assert.assertNotNull(requestToken.getTokenSecret());
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuthAccessToken_nullVerifier() throws TwitterException {
 twitterBase = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 AccessToken accessToken = twitterBase.getOAuth().getOAuthAccessToken(null);
 }

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 twitterBase = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 RequestToken requestToken = twitterBase.getOAuth().getOAuthRequestToken("http://example.com/oauth_callback");
 AccessToken accessToken = twitterBase.getOAuth().getOAuthAccessToken(requestToken.getToken(), "oauth_verifier");
 Assert.assertNotNull(accessToken.getScreenName());
 Assert.assertNotNull(accessToken.getId());
 }

 @Test
 public void testGetOAuthAccessToken_nullAccessToken() {
 AccessToken accessToken = null;
 twitterBase.setOAuthAccessToken(accessToken);
 Assert.assertNull(twitterBase.getScreenName());
 Assert.assertNull(twitterBase.getId());
 }

 @Test
 public void testSetOAuthAccessToken() {
 AccessToken accessToken = new AccessToken(1L, "accessToken", "accessTokenSecret", "testUser");
 twitterBase.setOAuthAccessToken(accessToken);
 Assert.assertEquals("testUser", twitterBase.getScreenName());
 Assert.assertEquals(1L, twitterBase.getId());
 }

 @Test
 public void testGetOAuthAccessToken_screenNameAndPassword() throws TwitterException {
 twitterBase = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 AccessToken accessToken = twitterBase.getOAuth().getOAuthAccessToken("testUser", "password");
 Assert.assertEquals("testUser", accessToken.getScreenName());
 Assert.assertEquals(1L, accessToken.getId());
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuthAccessToken_screenNameAndPassword_wrongCredentials() throws TwitterException {
 twitterBase = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 Assert.assertThrows(TwitterException.class, () -> twitterBase.getOAuth().getOAuthAccessToken("wrongUser", "password"));
 }

 @Test
 public void testEqualityWithSameObject() {
 TwitterBaseImpl twitterBase1 = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 Assert.assertEquals(twitterBase1, twitterBase1);
 }

 @Test
 public void testEqualityWithDifferentObjectsButEqualValues() {
 Configuration conf1 = new ConfigurationBuilder().build();
 Configuration conf2 = new ConfigurationBuilder().build();
 conf1.setDebugEnabled(true);
 conf2.setDebugEnabled(true);
 TwitterBaseImpl twitterBase1 = new TwitterBaseImpl(conf1, null);
 TwitterBaseImpl twitterBase2 = new TwitterBaseImpl(conf2, null);
 Assert.assertEquals(twitterBase1, twitterBase2);
 }

 @Test
 public void testEqualityWithDifferentObjectsButDifferentValues() {
 Configuration conf1 = new ConfigurationBuilder().build();
 Configuration conf2 = new ConfigurationBuilder().build();
 conf1.setDebugEnabled(true);
 conf2.setDebugEnabled(false);
 TwitterBaseImpl twitterBase1 = new TwitterBaseImpl(conf1, null);
 TwitterBaseImpl twitterBase2 = new TwitterBaseImpl(conf2, null);
 Assert.assertNotEquals(twitterBase1, twitterBase2);
 }

 @Test
 public void testHashCodeEquals() {
 TwitterBaseImpl twitterBase1 = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 TwitterBaseImpl twitterBase2 = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 Assert.assertEquals(twitterBase1.hashCode(), twitterBase2.hashCode());
 }

 @Test
 public void testHashCodeDifferentObjects() {
 TwitterBaseImpl twitterBase1 = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 TwitterBaseImpl twitterBase2 = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 twitterBase2.setScreenName("testUser2");
 Assert.assertNotEquals(twitterBase1.hashCode(), twitterBase2.hashCode());
 }

 @Test
 public void testToStringWithOAuthToken() throws TwitterException {
 Configuration conf = new ConfigurationBuilder().build();
 twitterBase = new TwitterBaseImpl(conf, null);
 RequestToken requestToken = twitterBase.getOAuth().getOAuthRequestToken("http://example.com/oauth_callback");
 AccessToken accessToken = twitterBase.getOAuth().getOAuthAccessToken(requestToken.getToken(), "oauth_verifier");
 twitterBase.setOAuthAccessToken(accessToken);
 Assert.assertEquals("TwitterBase{conf=Configuration{...}, http=HttpClientFactory$DefaultHttpClient{...}, rateLimitStatusListeners=[], auth=OAuthAuthorization{consumerKey=consumerKey, consumerSecret=consumerSecret, accessToken=accessToken, accessTokenSecret=accessTokenSecret, tokenType=Bearer}, screenName=testUser, id=1}", twitterBase.toString());
 }

 @Test
 public void testToStringWithNullOAuthToken() {
 AccessToken accessToken = null;
 twitterBase.setOAuthAccessToken(accessToken);
 Assert.assertEquals("TwitterBase{conf=Configuration{...}, http=HttpClientFactory$DefaultHttpClient{...}, rateLimitStatusListeners=[], auth=NullAuthorization, screenName=null, id=0}", twitterBase.toString());
 }

 @Test
 public void testToStringWithRateLimitListeners() {
 twitterBase = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
 List<RateLimitStatusListener> listeners = Arrays.asList(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 }
 }, new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 }
 }, new RateLimitStatusListener() {
 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 }
 });
 twitterBase.addRateLimitStatusListener((listener) -> {});
 twitterBase.onRateLimitStatus((event) -> {});
 twitterBase.onRateLimitReached((event) -> {});
 Assert.assertEquals("TwitterBase{conf=Configuration{...}, http=HttpClientFactory$DefaultHttpClient{...}, rateLimitStatusListeners=[RateLimitStatusListenerImpl@722e88c, RateLimitStatusListenerImpl@3e9d6b41, RateLimitStatusListenerImpl@474d3364, RateLimitStatusListenerImpl@195397f, RateLimitStatusListenerImpl@7d53b6e7], auth=NullAuthorization, screenName=null, id=0}", twitterBase.toString());
 }
}