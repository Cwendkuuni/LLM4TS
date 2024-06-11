package twitter4j;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito. ArgumentMatchers;
import org.mockito.Mockito;
import twitter4j.conf.*;
import twitter4j.auth.*;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;

import static org.junit.Assert.*;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration conf;
 private Authorization auth;
 private HttpClient http;
 private List<RateLimitStatusListener> rateLimitStatusListeners;
 private ObjectFactory factory;

 @Before
 public void setUp() {
 conf = new ConfigurationBuilder().build();
 auth = Mockito.mock(Authorization.class);
 http = Mockito.mock(HttpClient.class);
 rateLimitStatusListeners = new ArrayList<>();
 factory = Mockito.mock(ObjectFactory.class);
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 twitterBaseImpl.setFactory(factory);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 // Test case 1: User ID/password combination supplied
 conf.setOAuthConsumerKey("consumerKey1");
 conf.setOAuthConsumerSecret("consumerSecret1");
 twitterBaseImpl = new TwitterBaseImpl(conf, null);
 twitterBaseImpl.init();
 twitterBaseImpl.setFactory();
 assertEquals("expectedUserName1", twitterBaseImpl.getScreenName());

 // Test case 2: OAuth consumer key/secret combination supplied
 conf.setOAuthConsumerKey("consumerKey2");
 conf.setOAuthConsumerSecret("consumerSecret2");
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 twitterBaseImpl.init();
 twitterBaseImpl.setFactory();
 twitterBaseImpl.ensureOAuthEnabled();
 ArgumentCaptor<HttpParameter[]> httpParameters = ArgumentCaptor.forClass(HttpParameter[].class);
 Mockito.verify(http).get(ArgumentMatchers.eq(conf.getRestBaseURL() + "account/verify_credentials.json"), ArgumentMatchers.eq(auth), ArgumentMatchers.eq(httpParameters.capture()), Mockito.eq(TwitterBaseImpl.class), Mockito.anyInt());
 RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, null, true);
 twitterBaseImpl.onRateLimitStatus(rateLimitStatusEvent);
 twitterBaseImpl.onRateLimitReached(rateLimitStatusEvent);
 assertEquals("expectedUserName2", twitterBaseImpl.getScreenName());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithoutAuthorization() throws TwitterException, IllegalStateException {
 twitterBaseImpl.getScreenName();
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 // Test case 1: User ID/password combination supplied
 twitterBaseImpl.getScreenName();
 User user = twitterBaseImpl.fillInIDAndScreenName();
 assertEquals("expectedUserId1", user.getId());

 // Test case 2: OAuth consumer key/secret combination supplied
 User user2 = twitterBaseImpl.fillInIDAndScreenName();
 assertEquals("expectedUserId2", user2.getId());
 }

 @Test(expected = TwitterException.class)
 public void testFillInIDAndScreenNameWithoutAuthorization() throws TwitterException {
 twitterBaseImpl.fillInIDAndScreenName();
 }

 @Test
 public void testAddRateLimitStatusListener() {
 twitterBaseImpl.addRateLimitStatusListener(Mockito.mock(RateLimitStatusListener.class));
 assertEquals(1, twitterBaseImpl.rateLimitStatusListeners.size());
 }

 @Test
 public void testOnRateLimitStatus() {
 twitterBaseImpl.onRateLimitStatus(Mockito.mock(RateLimitStatusEvent.class));
 }

 @Test
 public void testOnRateLimitReached() {
 twitterBaseImpl.onRateLimitReached(Mockito.mock(RateLimitStatusEvent.class));
 }

 @Test
 public void testHttpResponseReceived() throws TwitterException {
 RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, null, true);
 twitterBaseImpl.httpResponseReceived(Mockito.mock(HttpResponseEvent.class));
 for (RateLimitStatusListener listener : twitterBaseImpl.rateLimitStatusListeners) {
 listener.onRateLimitStatus(rateLimitStatusEvent);
 listener.onRateLimitReached(rateLimitStatusEvent);
 }
 }

 @Test
 public void testGetAuthorization() {
 assertEquals(auth, twitterBaseImpl.getAuthorization());
 }

 @Test
 public void testGetConfiguration() {
 assertEquals(conf, twitterBaseImpl.getConfiguration());
 }
}