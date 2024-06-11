import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import twitter4j.*;
import twitter4j.conf.*;
import java.util.*;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration conf;
 private Authorization auth;

 @Before
 public void setup() {
 conf = new ConfigurationBuilder()
 .setDebugEnabled(true)
 .build();
 auth = Mockito.mock(Authorization.class);
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 // Test with null authorization
 Mockito.doThrow(new IllegalStateException("Neither user ID/password combination nor OAuth consumer key/secret combination supplied"))
 .when(twitterBaseImpl).getScreenName();
 // Test with initialized authorization
 twitterBaseImpl.addRateLimitStatusListener(Mockito.mock(RateLimitStatusListener.class));
 Mockito.when(auth.isEnabled()).thenReturn(true);
 Mockito.when(auth.getInstance()).thenReturn("authorized_instance");
 Mockito.when(twitterBaseImpl.getAuthorization()).thenReturn(auth);
 Mockito.when(twitterBaseImpl.getFactory()).thenReturn(Mockito.mock(ObjectFactory.class));
 Mockito.when(twitterBaseImpl.http.get(Mockito.anyString(), Mockito.any(HttpParameter[].class), Mockito.eq(auth), Mockito.eq(conf)))
 .thenReturn(new HttpResponse(200, new ByteArrayInputStream("{\"screen_name\":\"test_screen_name\", \"id\":123456789}".getBytes()), null));

 String screenName = twitterBaseImpl.getScreenName();
 Assert.assertEquals("test_screen_name", screenName);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithUninitializedAuthorization() throws TwitterException, IllegalStateException {
 // Test with uninitialized authorization
 twitterBaseImpl.addRateLimitStatusListener(Mockito.mock(RateLimitStatusListener.class));
 Mockito.when(twitterBaseImpl.getAuthorization()).thenReturn(null);
 twitterBaseImpl.getScreenName();
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 // Test with null authorization
 Mockito.doThrow(new IllegalStateException("Neither user ID/password combination nor OAuth consumer key/secret combination supplied"))
 .when(twitterBaseImpl).getId();
 // Test with initialized authorization
 twitterBaseImpl.addRateLimitStatusListener(Mockito.mock(RateLimitStatusListener.class));
 Mockito.when(auth.isEnabled()).thenReturn(true);
 Mockito.when(auth.getInstance()).thenReturn("authorized_instance");
 Mockito.when(twitterBaseImpl.getAuthorization()).thenReturn(auth);
 Mockito.when(twitterBaseImpl.getFactory()).thenReturn(Mockito.mock(ObjectFactory.class));
 Mockito.when(twitterBaseImpl.http.get(Mockito.anyString(), Mockito.any(HttpParameter[].class), Mockito.eq(auth), Mockito.eq(conf)))
 .thenReturn(new HttpResponse(200, new ByteArrayInputStream("{\"screen_name\":\"test_screen_name\", \"id\":123456789}".getBytes()), null));

 long id = twitterBaseImpl.getId();
 Assert.assertEquals(123456789, id);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithUninitializedAuthorization() throws TwitterException, IllegalStateException {
 // Test with uninitialized authorization
 twitterBaseImpl.addRateLimitStatusListener(Mockito.mock(RateLimitStatusListener.class));
 Mockito.when(twitterBaseImpl.getAuthorization()).thenReturn(null);
 twitterBaseImpl.getId();
 }

 @Test
 public void testAddRateLimitStatusListener() {
 // Test adding a rate limit status listener
 twitterBaseImpl.addRateLimitStatusListener(Mockito.mock(RateLimitStatusListener.class));
 twitterBaseImpl.addRateLimitStatusListener(Mockito.mock(RateLimitStatusListener.class));
 List<RateLimitStatusListener> rateLimitStatusListeners = twitterBaseImpl.getRateLimitStatusListeners();
 Assert.assertEquals(2, rateLimitStatusListeners.size());
 }

 @Test
 public void testOnRateLimitStatus() {
 // Test onRateLimitStatus event
 twitterBaseImpl.addRateLimitStatusListener(Mockito.mock(RateLimitStatusListener.class));
 twitterBaseImpl.onRateLimitStatus(Mockito.mock(RateLimitStatusEvent.class));
 }

 @Test
 public void testSetOAuthConsumer() throws TwitterException {
 // Test setting OAuth consumer key and secret
 twitterBaseImpl.setOAuthConsumer("consumer_key", "consumer_secret");
 Mockito.verify(twitterBaseImpl.getAuthorization(), Mockito.times(1)).setOAuthConsumer("consumer_key", "consumer_secret");
 }

 @Test(expected = IllegalStateException.class)
 public void testSetOAuthConsumerWithUninitializedAuthorization() throws TwitterException {
 // Test setting OAuth consumer key and secret with uninitialized authorization
 twitterBaseImpl.setOAuthConsumer("consumer_key", "consumer_secret");
 }

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 // Test getting OAuth request token
 twitterBaseImpl.addRateLimitStatusListener(Mockito.mock(RateLimitStatusListener.class));
 Mockito.when(twitterBaseImpl.getAuthorization()).thenReturn(auth);
 Mockito.when(auth.getOAuthRequestToken()).thenReturn(Mockito.mock(RequestToken.class));
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 Assert.assertNotNull(requestToken);
 }

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 // Test getting OAuth access token
 twitterBaseImpl.addRateLimitStatusListener(Mockito.mock(RateLimitStatusListener.class));
 Mockito.when(twitterBaseImpl.getAuthorization()).thenReturn(auth);
 Mockito.when(auth.getOAuthAccessToken("authorized_instance", "user_password")).thenReturn(Mockito.mock(AccessToken.class));
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 Assert.assertNotNull(accessToken);
 }

 @Test
 public void testEquals() throws TwitterException {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf, auth);
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
 Assert.assertEquals(twitterBaseImpl1, twitterBaseImpl2);

 TwitterBaseImpl twitterBaseImpl3 = new TwitterBaseImpl(conf, null);
 Assert.assertNotEquals(twitterBaseImpl1, twitterBaseImpl3);

 TwitterBaseImpl twitterBaseImpl4 = new TwitterBaseImpl(null, conf);
 Assert.assertNotEquals(twitterBaseImpl1, twitterBaseImpl4);

 TwitterBaseImpl twitterBaseImpl5 = new TwitterBaseImpl(conf, auth);
 TwitterBaseImpl twitterBaseImpl6 = new TwitterBaseImpl(conf, null);
 Assert.assertNotEquals(twitterBaseImpl5, twitterBaseImpl6);
 }

 @Test
 public void testHashCode() {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf, auth);
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
 Assert.assertEquals(twitterBaseImpl1.hashCode(), twitterBaseImpl2.hashCode());

 TwitterBaseImpl twitterBaseImpl3 = new TwitterBaseImpl(conf, null);
 Assert.assertNotEquals(twitterBaseImpl1.hashCode(), twitterBaseImpl3.hashCode());

 TwitterBaseImpl twitterBaseImpl4 = new TwitterBaseImpl(null, conf);
 Assert.assertNotEquals(twitterBaseImpl1.hashCode(), twitterBaseImpl4.hashCode());

 TwitterBaseImpl twitterBaseImpl5 = new TwitterBaseImpl(conf, auth);
 TwitterBaseImpl twitterBaseImpl6 = new TwitterBaseImpl(conf, null);
 Assert.assertNotEquals(twitterBaseImpl5.hashCode(), twitterBaseImpl6.hashCode());
 }

 @Test
 public void testToString() {
 String toString = twitterBaseImpl.toString();
 Assert.assertNotNull(toString);
 }
}