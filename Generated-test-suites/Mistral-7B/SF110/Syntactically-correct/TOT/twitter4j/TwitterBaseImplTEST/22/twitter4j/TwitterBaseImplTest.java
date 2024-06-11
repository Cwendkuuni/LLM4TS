package twitter4j.test;

import static org.junit.Assert.*;
import static twitter4j.conf.Configuration.*;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.http.*;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.rules.TestRule;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration conf;
 private Authorization auth;
 private HttpClient http;
 private static final String CONSUMER_KEY = "testConsumerKey";
 private static final String CONSUMER_SECRET = "testConsumerSecret";
 private static final String ACCESS_TOKEN = "testAccessToken";
 private static final String ACCESS_TOKEN_SECRET = "testAccessTokenSecret";
 private OAuthSupport oAuthSupport;

 @Rule
 public TestName testName = new TestName();

 @Before
 public void setUp() {
 conf = new ConfigurationBuilder()
 .setDebugEnabled(true)
 .setOAuthConsumerKey(CONSUMER_KEY)
 .setOAuthConsumerSecret(CONSUMER_SECRET)
 .setOAuthAccessToken(ACCESS_TOKEN)
 .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)
 .build();
 auth = new OAuthAuthorization(conf);
 http = HttpClientFactory.getInstance(conf.getHttpClientConfiguration());
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 oAuthSupport = twitterBaseImpl;
 }

 @After
 public void tearDown() {
 // Clear rate limit listeners to prevent test interference
 twitterBaseImpl.getRateLimitStatusListeners().clear();
 }

 @Test
 public void testGetScreenName() {
 // Test successful getScreenName call with basic authentication
 twitterBaseImpl.setFactory();
 twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 fail("Rate limit reached");
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 fail("Rate limit reached");
 }
 });
 String expectedScreenName = "testUser";
 String actualScreenName = twitterBaseImpl.getScreenName();
 assertEquals(expectedScreenName, actualScreenName);

 // Test failed getScreenName call without authentication
 twitterBaseImpl.auth = null;
 try {
 twitterBaseImpl.getScreenName();
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testGetId() {
 // Test successful getId call with basic authentication
 twitterBaseImpl.setFactory();
 twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 fail("Rate limit reached");
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 fail("Rate limit reached");
 }
 });
 long expectedId = 1234567890L;
 long actualId = twitterBaseImpl.getId();
 assertEquals(expectedId, actualId);

 // Test failed getId call without authentication
 twitterBaseImpl.auth = null;
 try {
 twitterBaseImpl.getId();
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testFillInIDAndScreenName() throws TwitterException {
 // Test successful fillInIDAndScreenName call with basic authentication
 User expectedUser = new UserJSONImpl(new UserJSONImpl.UserImpl(1234567890L, "testUser"), conf);
 User actualUser = twitterBaseImpl.fillInIDAndScreenName();
 assertEquals(expectedUser.getScreenName(), actualUser.getScreenName());
 assertEquals(expectedUser.getId(), actualUser.getId());

 // Test failed fillInIDAndScreenName call without authentication
 twitterBaseImpl.auth = null;
 try {
 twitterBaseImpl.fillInIDAndScreenName();
 fail("Expected TwitterException");
 } catch (TwitterException e) {
 // Expected
 }
 }

 @Test
 public void testAddRateLimitStatusListener() {
 // Test adding a rate limit status listener
 twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 fail("Should not be called");
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 fail("Should not be called");
 }
 });
 assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());

 // Test adding the same rate limit status listener multiple times
 twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 fail("Should not be called");
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 fail("Should not be called");
 }
 });
 assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());
}

 @Test
 public void testOnRateLimitStatus() {
 // Test onRateLimitStatus listener callback with valid rate limit status
 RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(15, 15), true);
 RateLimitStatusListener listener = new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 assertEquals(15, event.getLimit());
 assertEquals(15, event.getRemaining());
 assertTrue(event.isAuthenticated());
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 fail("Should not be called");
 }
 };
 twitterBaseImpl.onRateLimitStatus(event);
 twitterBaseImpl.addRateLimitStatusListener(listener);
 twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(HttpMethod.GET, new URL("http://example.com"), null, null, null, null));

 // Test onRateLimitStatus listener callback with rate limit reached
 event = new RateLimitStatusEvent(twitterBaseImpl, null, true);
 listener.onRateLimitReached(event);
 twitterBaseImpl.addRateLimitStatusListener(listener);
 twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(HttpMethod.GET, new URL("http://example.com"), null, null, null, new TwitterException(TwitterException.RateLimitException.class, 420, "Rate limit reached")));
}

 @Test
 public void testOnRateLimitReached() {
 // Test onRateLimitReached listener callback with valid rate limit status
 RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(15, 15), true);
 RateLimitStatusListener listener = new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 fail("Should not be called");
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 assertEquals(15, event.getLimit());
 assertEquals(0, event.getRemaining());
 assertTrue(event.isAuthenticated());
 }
 };
 twitterBaseImpl.onRateLimitStatus(event);
 twitterBaseImpl.addRateLimitStatusListener(listener);
 twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(HttpMethod.GET, new URL("http://example.com"), null, null, null, null));

 // Test onRateLimitReached listener callback with rate limit reached
 event = new RateLimitStatusEvent(twitterBaseImpl, null, true);
 listener.onRateLimitStatus(event);
 twitterBaseImpl.addRateLimitStatusListener(listener);
 twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(HttpMethod.GET, new URL("http://example.com"), null, null, null, new TwitterException(TwitterException.RateLimitException.class, 420, "Rate limit reached")));
}

 @Test
 public void testHttpResponseReceived() {
 // Test successful httpResponseReceived call
 HttpResponseEvent httpResponseEvent = new HttpResponseEvent(HttpMethod.GET, new URL("http://example.com"), null, null, null, null);
 twitterBaseImpl.httpResponseReceived(httpResponseEvent);
 twitterBaseImpl.rateLimitStatusListeners.forEach(listener -> listener.onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, null, true)));

 // Test failed httpResponseReceived call with rate limit reached
 HttpResponseEvent httpResponseEventFailed = new HttpResponseEvent(HttpMethod.GET, new URL("http://example.com"), null, null, null, new TwitterException(TwitterException.RateLimitException.class, 420, "Rate limit reached"));
 twitterBaseImpl.httpResponseReceived(httpResponseEventFailed);
 twitterBaseImpl.rateLimitStatusListeners.forEach(listener -> listener.onRateLimitReached(new RateLimitStatusEvent(twitterBaseImpl, null, true)));
}

 @Test
 public void testSetAuthorization() {
 // Test setting authorization with consumer key and secret
 twitterBaseImpl.setAuthorization(new OAuthAuthorization(conf));
 OAuthAuthorization expectedAuth = (OAuthAuthorization)twitterBaseImpl.getAuthorization();
 assertEquals(CONSUMER_KEY, expectedAuth.getConsumerKey());
 assertEquals(CONSUMER_SECRET, expectedAuth.getConsumerSecret());

 // Test setting authorization with OAuth2 access token and token secret
 twitterBaseImpl.setAuthorization(new OAuth2Authorization(conf));
 OAuth2Authorization expectedAuth2 = (OAuth2Authorization)twitterBaseImpl.getAuthorization();
 assertEquals(ACCESS_TOKEN, expectedAuth2.getAccessToken());
 assertEquals(ACCESS_TOKEN_SECRET, expectedAuth2.getAccessTokenSecret());
}

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 RequestToken expectedRequestToken = new RequestToken("oauth_token", "oauth_token_secret");
 RequestToken actualRequestToken = twitterBaseImpl.getOAuthRequestToken();
 assertEquals(expectedRequestToken.getToken(), actualRequestToken.getToken());
 assertEquals(expectedRequestToken.getTokenSecret(), actualRequestToken.getTokenSecret());
}

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 AccessToken expectedAccessToken = new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
 AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken();
 assertEquals(expectedAccessToken.getScreenName(), actualAccessToken.getScreenName());
 assertEquals(expectedAccessToken.getId(), actualAccessToken.getId());
}

 @Test
 public void testEquals() {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf, auth);
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
 assertEquals(twitterBaseImpl1, twitterBaseImpl2);

 TwitterBaseImpl twitterBaseImpl3 = new TwitterBaseImpl(conf, null);
 assertNotEquals(twitterBaseImpl1, twitterBaseImpl3);
}

 @Test
 public void testHashCode() {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf, auth);
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
 int expectedHashCode = twitterBaseImpl1.hashCode();
 int actualHashCode = twitterBaseImpl2.hashCode();
 assertEquals(expectedHashCode, actualHashCode);

 TwitterBaseImpl twitterBaseImpl3 = new TwitterBaseImpl(conf, null);
 int expectedHashCode2 = twitterBaseImpl1.hashCode();
 int actualHashCode2 = twitterBaseImpl3.hashCode();
 assertNotEquals(expectedHashCode2, actualHashCode2);
}

 @Test
 public void testToString() {
 String expectedString = "TwitterBaseImpl{conf=" + conf.toString() + ", http=" + http.toString() + ", rateLimitStatusListeners=" + twitterBaseImpl.getRateLimitStatusListeners().toString() + ", auth=" + auth.toString() + '}';
 String actualString = twitterBaseImpl.toString();
 assertEquals(expectedString, actualString);
}

}