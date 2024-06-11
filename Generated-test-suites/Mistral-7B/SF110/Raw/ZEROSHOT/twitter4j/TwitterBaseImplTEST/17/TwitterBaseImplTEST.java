package twitter4j.test;

import static org.junit.Assert.*;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;
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
 public void testGetScreenName() {
 // Test with initialized screenName
 twitterBaseImpl.setScreenName("testScreenName");
 assertEquals("testScreenName", twitterBaseImpl.getScreenName());

 // Test with uninitialized screenName
 assertNull(twitterBaseImpl.getScreenName());

 // Test with uninitialized and unauthenticated TwitterBaseImpl
 assertNull(twitterBaseImpl.getScreenName());
 twitterBaseImpl.ensureAuthorizationEnabled();
 assertNull(twitterBaseImpl.getScreenName());
 }

 @Test
 public void testGetId() {
 // Test with initialized id
 twitterBaseImpl.fillInIDAndScreenName();
 assertEquals(1L, twitterBaseImpl.getId());

 // Test with uninitialized id
 assertZeroEquals(twitterBaseImpl.getId());

 // Test with uninitialized and unauthenticated TwitterBaseImpl
 assertZeroEquals(twitterBaseImpl.getId());
 twitterBaseImpl.ensureAuthorizationEnabled();
 assertZeroEquals(twitterBaseImpl.getId());
 }

 @Test
 public void testAddRateLimitStatusListener() {
 RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
 twitterBaseImpl.addRateLimitStatusListener(listener);
 assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddRateLimitStatusListenerWithUnmodifiableList() {
 RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
 twitterBaseImpl.addRateLimitStatusListener(listener);
 twitterBaseImpl.setRateLimitStatusListeners(Arrays.asList(listener));
 twitterBaseImpl.addRateLimitStatusListener(listener);
 }

 @Test
 public void testOnRateLimitStatus() {
 RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
 Consumer<RateLimitStatusEvent> action = arg -> assertEquals(RateLimitStatus.RATE_LIMITED, arg.getRateLimitStatus().getLimit());
 twitterBaseImpl.onRateLimitStatus(action);
 // Simulate rate limit event
 twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(HttpResponseStatus.TOO_MANY_REQUESTS, null));
 twitterBaseImpl.getRateLimitStatusListeners().forEach(listener -> listener.onRateLimitStatus(event));
 action.accept(event);
 }

 @Test
 public void testHttpResponseReceived() throws IOException, TwitterException {
 HttpResponseEvent event = new HttpResponseEvent(HttpResponseStatus.OK, null);
 twitterBaseImpl.httpResponseReceived(event);
 // No rate limit status listeners
 assertFalse(twitterBaseImpl.getRateLimitStatusListeners().isEmpty());
 }

 @Test
 public void testGetAuthorization() {
 assertSame(auth, twitterBaseImpl.getAuthorization());
 }

 @Test
 public void testGetConfiguration() {
 assertSame(conf, twitterBaseImpl.getConfiguration());
 }

 @Test(expected = IllegalStateException.class)
 public void testEnsureAuthorizationEnabledWithoutAuthorization() {
 twitterBaseImpl.ensureAuthorizationEnabled();
 }

 @Test(expected = IllegalStateException.class)
 public void testEnsureOAuthEnabledWithoutOAuth() {
 twitterBaseImpl.ensureOAuthEnabled();
 }

 @Test
 public void testWriteObject() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 twitterBaseImpl.writeObject(out);
 byte[] bytes = byteStream.toByteArray();
 assertTrue(bytes.length > 0);
 }

 @Test
 public void testReadObject() throws IOException, ClassNotFoundException {
 ByteArrayInputStream byteStream = new ByteArrayInputStream(twitterBaseImpl.writeObject(new ByteArrayOutputStream()));
 ObjectInputStream in = new ObjectInputStream(byteStream);
 TwitterBaseImpl twitterBaseImplCopy = (TwitterBaseImpl)in.readObject();
 assertEquals(twitterBaseImpl, twitterBaseImplCopy);
 }

 @Test
 public void testSetOAuthConsumer() {
 twitterBaseImpl.setOAuthConsumer("newConsumerKey", "newConsumerSecret");
 assertSame("newConsumerKey", twitterBaseImpl.getConfiguration().getOAuthConsumerKey());
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

 @Test
 public void testSetOAuthAccessToken() throws TwitterException {
 AccessToken accessToken = new AccessToken(1L, "testAccessToken");
 twitterBaseImpl.setOAuthAccessToken(accessToken);
 assertEquals(accessToken, twitterBaseImpl.getOAuthAccessToken());
 }

 @Test
 public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("testScreenName", "testPassword");
 assertNotNull(accessToken);
 }
}