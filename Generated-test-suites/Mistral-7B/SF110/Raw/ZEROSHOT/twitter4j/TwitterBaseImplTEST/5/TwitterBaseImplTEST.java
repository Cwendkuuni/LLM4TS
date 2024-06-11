package twitter4j.test;

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
 ConfigurationBuilder cb = new ConfigurationBuilder();
 Configuration conf = cb.build();
 authorization = new OAuthAuthorization(conf);
 twitterBaseImpl = new TwitterBaseImpl(conf, authorization);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 assertNull(twitterBaseImpl.getScreenName());
 twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
 assertNull(twitterBaseImpl.getScreenName());
 twitterBaseImpl.fillInIDAndScreenName();
 assertEquals("screenName", twitterBaseImpl.getScreenName());
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 assertZero(twitterBaseImpl.getId());
 twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
 twitterBaseImpl.fillInIDAndScreenName();
 assertEquals(123456789L, twitterBaseImpl.getId());
 }

 @Test
 public void testAddRateLimitStatusListener() {
 RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
 twitterBaseImpl.addRateLimitStatusListener(listener);
 assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());
 }

 @Test
 public void testOnRateLimitStatus() {
 RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
 Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
 twitterBaseImpl.onRateLimitStatus(action);
 twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
 verify(action).accept(event);
 }

 @Test
 public void testOnRateLimitReached() {
 RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
 Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
 twitterBaseImpl.onRateLimitReached(action);
 twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
 verify(action).accept(event);
 }

 @Test
 public void testHttpResponseReceived() {
 RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
 Consumer<RateLimitStatusEvent> onRateLimitStatus = mock(Consumer.class);
 Consumer<RateLimitStatusEvent> onRateLimitReached = mock(Consumer.class);
 twitterBaseImpl.onRateLimitStatus(onRateLimitStatus);
 twitterBaseImpl.onRateLimitReached(onRateLimitReached);
 twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
 verify(onRateLimitStatus, times(1)).accept(event);
 verify(onRateLimitReached, times(1)).accept(event);
 }

 @Test
 public void testGetAuthorization() {
 assertEquals(authorization, twitterBaseImpl.getAuthorization());
 }

 @Test
 public void testGetConfiguration() {
 assertEquals(configuration, twitterBaseImpl.getConfiguration());
 }

 @Test
 public void testEnsureAuthorizationEnabled() {
 twitterBaseImpl.setOAuthConsumer(null, null);
 assertThrows(IllegalStateException.class, twitterBaseImpl::getScreenName);
 }

 @Test
 public void testEnsureOAuthEnabled() {
 twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
 assertThrows(IllegalStateException.class, twitterBaseImpl::getOAuthRequestToken);
 }

 @Test
 public void testWriteObject() throws IOException {
 ObjectOutputStream out = new ObjectOutputStream(new ByteArrayOutputStream());
 twitterBaseImpl.writeObject(out);
 out.close();
 byte[] bytes = ((ByteArrayOutputStream)out.sync()).toByteArray();assertNotNull(bytes);
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(configuration, authorization);
 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TwitterBaseImpl twitterBaseImpl3 = (TwitterBaseImpl)in.readObject();
 assertEquals(twitterBaseImpl, twitterBaseImpl3);
 }

 @Test
 public void testReadObject() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 objectOutputStream.writeObject(twitterBaseImpl);
 objectOutputStream.close();
 byte[] bytes = byteArrayOutputStream.toByteArray();
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
 TwitterBaseImpl twitterBaseImpl2 = (TwitterBaseImpl)new ObjectInputStream(byteArrayInputStream).readObject();
 assertEquals(twitterBaseImpl, twitterBaseImpl2);
 }

 @Test
 public void testSetOAuthConsumer() {
 twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
 assertEquals("consumerKey", twitterBaseImpl.getAuthorization().getConsumerKey());
 assertEquals("consumerSecret", twitterBaseImpl.getAuthorization().getConsumerSecret());
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
 public void testSetOAuthAccessToken() {
 AccessToken accessToken = mock(AccessToken.class);
 twitterBaseImpl.setOAuthAccessToken(accessToken);
 assertEquals(accessToken, twitterBaseImpl.getOAuthAccessToken());
 }

 @Test
 public void testSetOAuthAccessTokenWithOAuthVerifier() throws TwitterException {
 AccessToken accessToken = mock(AccessToken.class);
 RequestToken requestToken = mock(RequestToken.class);
 twitterBaseImpl.setOAuthAccessToken(accessToken, "oauthVerifier");
 assertEquals(accessToken, twitterBaseImpl.getOAuthAccessToken());
 }

 @Test
 public void testSetOAuthAccessTokenWithRequestToken() throws TwitterException {
 AccessToken accessToken = mock(AccessToken.class);
 RequestToken requestToken = mock(RequestToken.class);
 twitterBaseImpl.setOAuthAccessToken(accessToken, requestToken);
 assertEquals(accessToken, twitterBaseImpl.getOAuthAccessToken());
 }

 @Test
 public void testSetOAuthAccessTokenWithRequestTokenAndOAuthVerifier() throws TwitterException {
 AccessToken accessToken = mock(AccessToken.class);
 RequestToken requestToken = mock(RequestToken.class);
 twitterBaseImpl.setOAuthAccessToken(accessToken, requestToken, "oauthVerifier");
 assertEquals(accessToken, twitterBaseImpl.getOAuthAccessToken());
 }

 @Test
 public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("screenName", "password");
 assertNotNull(accessToken);
 }

 @Test
 public void testEquals() {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(configuration, authorization);
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(configuration, authorization);
 assertTrue(twitterBaseImpl1.equals(twitterBaseImpl2));
 }

 @Test
 public void testNotEquals() {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(configuration, authorization);
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(configuration, null);
 assertFalse(twitterBaseImpl1.equals(twitterBaseImpl2));
 }

 @Test
 public void testHashCode() {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(configuration, authorization);
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(configuration, authorization);
 assertEquals(twitterBaseImpl1.hashCode(), twitterBaseImpl2.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("TwitterBase{conf=" + configuration + ", http=" + twitterBaseImpl.http + ", rateLimitStatusListeners=" + twitterBaseImpl.rateLimitStatusListeners + ", auth=" + authorization + '}', twitterBaseImpl.toString());
 }
}