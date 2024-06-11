package test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import twitter4j.*;
import twitter4j.auth.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import static org.mockito.Mockito.*;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration conf;
 private Authorization auth;
 private HttpClient httpClient;
 private RateLimitStatusListener rateLimitStatusListener;
 private RateLimitStatusEvent rateLimitStatusEvent;

 @Before
 public void init() {
 conf = new ConfigurationBuilder().build();
 auth = mock(Authorization.class);
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 httpClient = mock(HttpClient.class);
 rateLimitStatusListener = mock(RateLimitStatusListener.class);
 rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(0, 100, 100), true);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 // Test when screenName is not set
 assertNull(twitterBaseImpl.getScreenName());

 // Test when screenName is set
 twitterBaseImpl.addRateLimitStatusListener(rateLimitStatusListener);
 twitterBaseImpl.fillInIDAndScreenName();
 verify(rateLimitStatusListener).onRateLimitStatus(rateLimitStatusEvent);
 verify(rateLimitStatusListener).onRateLimitReached(rateLimitStatusEvent);
 assertNotNull(twitterBaseImpl.getScreenName());
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 // Test when id is not set
 assertNull(twitterBaseImpl.getId());

 // Test when id is set
 twitterBaseImpl.addRateLimitStatusListener(rateLimitStatusListener);
 twitterBaseImpl.fillInIDAndScreenName();
 verify(rateLimitStatusListener).onRateLimitStatus(rateLimitStatusEvent);
 verify(rateLimitStatusListener).onRateLimitReached(rateLimitStatusEvent);
 assertNotNull(twitterBaseImpl.getId());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithoutAuthorization() throws TwitterException, IllegalStateException {
 twitterBaseImpl = new TwitterBaseImpl(conf, null);
 twitterBaseImpl.getScreenName();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithoutAuthorization() throws TwitterException, IllegalStateException {
 twitterBaseImpl = new TwitterBaseImpl(conf, null);
 twitterBaseImpl.getId();
 }

 @Test
 public void testAddRateLimitStatusListener() {
 twitterBaseImpl.addRateLimitStatusListener(rateLimitStatusListener);
 verify(twitterBaseImpl.rateLimitStatusListeners, isA(ArrayList.class));
 verify(twitterBaseImpl.rateLimitStatusListeners).add(rateLimitStatusListener);
 }

 @Test
 public void testOnRateLimitStatus() {
 twitterBaseImpl.addRateLimitStatusListener(rateLimitStatusListener);
 ArgumentCaptor<RateLimitStatusEvent> captor = ArgumentCaptor.forClass(RateLimitStatusEvent.class);
 verify(rateLimitStatusListener).onRateLimitStatus(captor.capture());
 assertSame(rateLimitStatusEvent, captor.getValue());
 }

 @Test
 public void testOnRateLimitReached() {
 twitterBaseImpl.onRateLimitReached(argThat(e -> e.getRateLimitStatus().getRemaining() == 0));
 verify(rateLimitStatusListener).onRateLimitReached(rateLimitStatusEvent);
 }

 @Test
 public void testHttpResponseReceived() {
 ArgumentCaptor<RateLimitStatusEvent> captor = ArgumentCaptor.forClass(RateLimitStatusEvent.class);
 httpClient.addListener(twitterBaseImpl);
 httpClient.sendRequest(new HttpRequest("GET", "http://test.com", null, httpClient.getDefaultHeaders(), null, null));
 twitterBaseImpl.onRateLimitStatus(rateLimitStatusEvent);
 twitterBaseImpl.onRateLimitReached(rateLimitStatusEvent);
 verify(rateLimitStatusListener).onRateLimitStatus(captor.capture());
 assertSame(rateLimitStatusEvent, captor.getValue());
 }

}