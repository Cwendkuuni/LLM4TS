package test;

import static org.junit.Assert.*;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

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
 auth = new BasicAuthorization("testUser", "testPassword");
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 // Test when screenName is null
 assertNull(twitterBaseImpl.getScreenName());

 // Test when screenName is not set
 twitterBaseImpl.init();
 assertNull(twitterBaseImpl.getScreenName());

 // Test when screenName is set
 twitterBaseImpl.fillInIDAndScreenName();
 assertNotNull(twitterBaseImpl.getScreenName());
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 // Test when id is 0
 assertEq(0L, twitterBaseImpl.getId());
 twitterBaseImpl.fillInIDAndScreenName();
 assertNotNull(twitterBaseImpl.getId());
 }

 @Test
 public void testAddRateLimitStatusListener() {
 RateLimitStatusListener listener = new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 // Implementation
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 // Implementation
 }
 };
 twitterBaseImpl.addRateLimitStatusListener(listener);
 assertTrue(twitterBaseImpl.rateLimitStatusListeners.contains(listener));
 }

 @Test
 public void testOnRateLimitStatus() {
 Consumer<RateLimitStatusEvent> action = new Consumer<RateLimitStatusEvent>() {
 @Override
 public void accept(RateLimitStatusEvent event) {
 // Implementation
 }
 };
 twitterBaseImpl.onRateLimitStatus(action);
 assertTrue(twitterBaseImpl.rateLimitStatusListeners.size() > 0);
 }

 @Test
 public void testOnRateLimitReached() {
 Consumer<RateLimitStatusEvent> action = new Consumer<RateLimitStatusEvent>() {
 @Override
 public void accept(RateLimitStatusEvent event) {
 // Implementation
 }
 };
 twitterBaseImg.onRateLimitReached(action);
 assertTrue(twitterBaseImpl.rateLimitStatusListeners.size() > 0);
 }

 @Test
 public void testHttpResponseReceived() throws TwitterException {
 HttpResponseEvent event = new HttpResponseEvent(null, null, null, null, null, null, null, null, null);
 twitterBaseImpl.httpResponseReceived(event);
 assertTrue(twitterBaseImpl.rateLimitStatusListeners.size() > 0);
 }

 @Test
 public void testGetAuthorization() {
 assertEquals(auth, twitterBaseImpl.getAuthorization());
 }

 @Test
 public void testGetConfiguration() {
 assertEquals(conf, twitterBaseImpl.getConfiguration());
 }

 @Test(expected = IllegalStateException.class)
 public void testEnsureAuthorizationEnabled() throws IllegalStateException {
 twitterBaseImpl.ensureAuthorizationEnabled();
 }

 @Test(expected = IllegalStateException.class)
 public void testEnsureOAuthEnabled() throws IllegalStateException {
 twitterBaseImpl.ensureOAuthEnabled();
 }

 @Test
 public void testWriteObject() throws IOException {
 ObjectOutputStream out = new ObjectOutputStream(new ByteArrayOutputStream());
 twitterBaseImpl.writeObject(out);
 out.close();
 byte[] bytes = ((ByteArrayOutputStream)out.sync()).toByteArray();
 TwitterBaseImpl twitterBaseImplCopy = (TwitterBaseImpl)new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
 assertEquals(twitterBaseImpl, twitterBaseImplCopy);
 }

 @Test
 public void testReadObject() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 objectOutputStream.writeObject(twitterBaseImpl);
 objectOutputStream.close();
 byte[] bytes = byteArrayOutputStream.toByteArray();
 ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TwitterBaseImpl twitterBaseImplCopy = (TwitterBaseImpl)objectInputStream.readObject();
 assertEquals(twitterBaseImpl, twitterBaseImplCopy);
 }

 @Test
 public void testSetOAuthConsumer() {
 twitterBaseImpl.setOAuthConsumer("testConsumerKey", "testConsumerSecret");
 assertTrue(twitterBaseImpl.getAuthorization() instanceof OAuthAuthorization);
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
 public void testGetOAuthAccessTokenWithOAuthVerifier() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("testOAuthVerifier");
 assertNotNull(accessToken);
 }

 @Test
 public void testSetOAuthAccessToken() {
 AccessToken accessToken = new AccessToken("testAccessToken", "testAccessTokenSecret", "testScreenName", 12345L);
 twitterBaseImpl.setOAuthAccessToken(accessToken);
 assertEquals(accessToken, twitterBaseImpl.getOAuthAccessToken());
 }

 @Test
 public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("testScreenName", "testPassword");
 assertNotNull(accessToken);
 }

 @Test
 public void testGetOAuthSupport() {
 OAuthSupport oAuthSupport = twitterBaseImpl.getOAuth();
 assertNotNull(oAuthSupport);
 }

 @Test
 public void testGetOAuth2Support() {
 OAuth2Support oAuth2Support = twitterBaseImpl.getOAuth2();
 assertNotNull(oAuth2Support);
 }

 @Test
 public void testEquals() {
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
 assertEquals(twitterBaseImpl, twitterBaseImpl2);
 }

 @Test
 public void testHashCode() {
 int hashCode1 = twitterBaseImpl.hashCode();
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
 int hashCode2 = twitterBaseImpl2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 String toString = twitterBaseImpl.toString();
 assertNotNull(toString);
 }
}