import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.auth.*;
import twitter4j.util.function.*;
import java.util.*;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration conf;
 private Authorization auth;

 @Before
 public void setUp() {
 conf = new ConfigurationBuilder()
 .setDebugEnabled(true)
 .setOAuthConsumerKey("consumerKey")
 .setOAuthConsumerSecret("consumerSecret")
 .build();
 auth = new BasicAuthorization("userName", "password");
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 String expectedScreenName = "userName";
 String actualScreenName = twitterBaseImpl.getScreenName();
 assertEquals(expectedScreenName, actualScreenName);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWhenAuthorizationNotEnabled() throws TwitterException, IllegalStateException {
 twitterBaseImpl.setAuthorization(null);
 twitterBaseImpl.getScreenName();
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 long expectedId = 1L;
 long actualId = twitterBaseImpl.getId();
 assertEquals(expectedId, actualId);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWhenAuthorizationNotEnabled() throws TwitterException, IllegalStateException {
 twitterBaseImpl.setAuthorization(null);
 twitterBaseImpl.getId();
 }

 @Test
 public void testAddRateLimitStatusListener() {
 RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
 twitterBaseImpl.addRateLimitStatusListener(listener);
 assertTrue(twitterBaseImpl.getRateLimitStatusListeners().contains(listener));
 }

 @Test
 public void testHttpResponseReceivedWithRateLimit() {
 HttpResponseEvent event = mock(HttpResponseEvent.class);
 RateLimitStatus rateLimitStatus = new RateLimitStatus(60, 100, 60);
 when(event.getRateLimitStatus()).thenReturn(rateLimitStatus);
 twitterBaseImpl.onRateLimitStatus(event);
 twitterBaseImpl.onRateLimitReached(event);
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuthAccessTokenWithNullConsumerKey() throws TwitterException {
 twitterBaseImpl.setAuthorization(null);
 try {
 twitterBaseImpl.getOAuthAccessToken();
 fail("Expected TwitterException");
 } catch (TwitterException e) {
 // expected
 }
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuthAccessTokenWithNullConsumerSecret() throws TwitterException {
 twitterBaseImpl.setAuthorization(new BasicAuthorization("userName", null));
 try {
 twitterBaseImpl.getOAuthAccessToken();
 fail("Expected TwitterException");
 } catch (TwitterException e) {
 // expected
 }
 }

 @Test
 public void testEquals() {
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
 assertTrue(twitterBaseImpl.equals(twitterBaseImpl2));
 }

 @Test
 public void testHashCode() {
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
 assertEquals(twitterBaseImpl.hashCode(), twitterBaseImpl2.hashCode());
 }

 @Test
 public void testToString() {
 String expectedToString = "TwitterBase{conf=Configuration{...}, http=HttpClient{...}, rateLimitStatusListeners=[], auth=BasicAuthorization{userName=userName, password=password}}";
 String actualToString = twitterBaseImpl.toString();
 assertEquals(expectedToString, actualToString);
 }
}