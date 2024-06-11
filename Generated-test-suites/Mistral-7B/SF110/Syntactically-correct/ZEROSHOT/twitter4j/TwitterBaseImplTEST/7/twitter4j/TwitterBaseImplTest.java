package twitter4j.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;
import static org.mockito.Mockito.*;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration conf;
 private Authorization auth;

 @Before
 public void setUp() {
 conf = new ConfigurationBuilder()
 .setDebugEnabled(true)
 .build();
 auth = mock(Authorization.class);
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 // Test with null auth
 twitterBaseImpl.setAuthorization(null);
 try {
 twitterBaseImpl.getScreenName();
 Assert.fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 Assert.assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
 }

 // Test with initialized auth
 when(auth.isEnabled()).thenReturn(true);
 twitterBaseImpl.setAuthorization(auth);
 twitterBaseImpl.setFactory();
 twitterBaseImpl.init();
 twitterBaseImpl.http.expects(once()).method("get").withArgument("http://twitter.com/account/verify_credentials.json").willReturn(new HttpResponse(200, new ByteArrayInputStream("{\"screen_name\":\"test\",\"id\":123456789}".getBytes())));
 User user = twitterBaseImpl.fillInIDAndScreenName();
 Assert.assertEquals("test", twitterBaseImpl.getScreenName());
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 // Test with null auth
 twitterBaseImpl.setAuthorization(null);
 try {
 twitterBaseImpl.getId();
 Assert.fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 Assert.assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
 }

 // Test with initialized auth
 when(auth.isEnabled()).thenReturn(true);
 twitterBaseImpl.setAuthorization(auth);
 twitterBaseImpl.setFactory();
 twitterBaseImpl.init();
 twitterBaseImpl.http.expects(once()).method("get").withArgument("http://twitter.com/account/verify_credentials.json").willReturn(new HttpResponse(200, new ByteArrayInputStream("{\"screen_name\":\"test\",\"id\":123456789}".getBytes())));
 User user = twitterBaseImpl.fillInIDAndScreenName();
 Assert.assertEquals(123456789, twitterBaseImpl.getId());
 }

 @Test
 public void testAddRateLimitStatusListener() {
 RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
 twitterBaseImpl.addRateLimitStatusListener(listener);
 verify(listener, times(1)).onRateLimitStatus(any(RateLimitStatusEvent.class));
 }

 @Test
 public void testOnRateLimitStatus() {
 Consumer<RateLimitStatusEvent> action = arg -> {};
 twitterBaseImpl.onRateLimitStatus(action);
 RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(15, 15), true);
 twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(new HttpResponse(420, new ByteArrayInputStream("".getBytes())), null, new TwitterException(420, "Rate limit exceeded"), null));
 verify(action, times(1)).accept(event);
 }

 @Test
 public void testOnRateLimitReached() {
 Consumer<RateLimitStatusEvent> action = arg -> {};
 twitterBaseImpl.onRateLimitReached(action);
 RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(15, 14), true);
 twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(new HttpResponse(420, new ByteArrayInputStream("".getBytes())), null, new TwitterException(420, "Rate limit exceeded"), null));
 verify(action, times(1)).accept(event);
 }

 @Test
 public void testHttpResponseReceived() throws TwitterException {
 RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(15, 15), true);
 HttpResponseEvent httpResponseEvent = new HttpResponseEvent(new HttpResponse(420, new ByteArrayInputStream("".getBytes())), null, new TwitterException(420, "Rate limit exceeded"), null);
 twitterBaseImpl.httpResponseReceived(httpResponseEvent);
 for (RateLimitStatusListener listener : twitterBaseImpl.rateLimitStatusListeners) {
 listener.onRateLimitStatus(event);
 listener.onRateLimitReached(event);
 }
 }

}