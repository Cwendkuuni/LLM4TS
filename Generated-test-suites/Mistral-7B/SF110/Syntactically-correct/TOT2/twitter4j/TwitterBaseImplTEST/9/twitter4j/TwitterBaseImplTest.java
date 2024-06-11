import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration conf;
 private HttpClient httpMock;
 private Authorization authMock;
 private RateLimitStatusListener rateLimitStatusListenerMock;

 @Before
 public void setup() {
 conf = new Configuration();
 httpMock = Mockito.mock(HttpClient.class);
 authMock = Mockito.mock(Authorization.class);
 rateLimitStatusListenerMock = Mockito.mock(RateLimitStatusListener.class);
 twitterBaseImpl = new TwitterBaseImpl(conf, authMock);
 twitterBaseImpl.setHttp(httpMock);
 twitterBaseImpl.addRateLimitStatusListener(rateLimitStatusListenerMock);
 }

 @Test
 public void testGetScreenName() {
 // Test with initialized screenName and ID
 twitterBaseImpl.setScreenName("testUser");
 twitterBaseImpl.setId(1L);
 Assert.assertEquals("testUser", twitterBaseImpl.getScreenName());

 // Test with null screenName and ID, throws IllegalStateException
 twitterBaseImpl.setScreenName(null);
 twitterBaseImpl.setId(0L);
 try {
 twitterBaseImpl.getScreenName();
 FailureException.fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }

 // Test with initialized ID and null screenName, fills in screenName and ID
 twitterBaseImpl.setId(1L);
 twitterBaseImpl.fillInIDAndScreenName();
 Assert.assertNotNull(twitterBaseImpl.getScreenName());

 // Test with initialized ID and no screenName, throws TwitterException
 twitterBaseImpl.setId(1L);
 try {
 twitterBaseImpl.fillInIDAndScreenName(new HttpParameter[0]);
 FailureException.fail("Expected TwitterException");
 } catch (TwitterException e) {
 // Expected
 }
 }

 @Test
 public void testGetId() {
 // Test with initialized ID
 twitterBaseImpl.setId(1L);
 Assert.assertEquals(1L, twitterBaseImpl.getId());

 // Test with null ID, throws IllegalStateException
 twitterBaseImpl.setId(0L);
 try {
 twitterBaseImpl.getId();
 FailureException.fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testFillInIDAndScreenName() {
 // Test with initialized User
 User user = new UserJSONImpl(new Status(), conf);
 twitterBaseImpl.fillInIDAndScreenName(new HttpParameter[0]);
 Assert.assertEquals(user.getScreenName(), twitterBaseImpl.getScreenName());
 Assert.assertEquals(user.getId(), twitterBaseImpl.getId());

 // Test with no User, throws TwitterException
 try {
 twitterBaseImpl.fillInIDAndScreenName(new HttpParameter[0]);
 FailureException.fail("Expected TwitterException");
 } catch (TwitterException e) {
 // Expected
 }
 }

 @Test
 public void testAddRateLimitStatusListener() {
 // Test adding RateLimitStatusListener
 twitterBaseImpl.addRateLimitStatusListener(rateLimitStatusListenerMock);
 Mockito.verify(rateLimitStatusListenerMock).onRateLimitStatus(Mockito.any(RateLimitStatusEvent.class));
 Mockito.verifyNoMoreInteractions(rateLimitStatusListenerMock);

 // Test adding duplicate RateLimitStatusListener
 twitterBaseImpl.addRateLimitStatusListener(rateLimitStatusListenerMock);
 Mockito.verify(rateLimitStatusListenerMock, Mockito.times(2)).onRateLimitStatus(Mockito.any(RateLimitStatusEvent.class));
 }

 @Test
 public void testOnRateLimitStatus() {
 // Test onRateLimitStatus with authenticated request
 HttpResponseEvent event = new HttpResponseEvent(httpMock, null, null, 200, null, 0, null, false);
 twitterBaseImpl.httpResponseReceived(event);
 Mockito.verify(rateLimitStatusListenerMock).onRateLimitStatus(Mockito.any(RateLimitStatusEvent.class));

 // Test onRateLimitStatus with unauthenticated request
 HttpResponseEvent eventUnauthenticated = new HttpResponseEvent(httpMock, null, null, 401, null, 0, null, false);
 twitterBaseImpl.httpResponseReceived(eventUnauthenticated);
 Mockito.verifyNoMoreInteractions(rateLimitStatusListenerMock);
 }

 @Test
 public void testOnRateLimitReached() {
 // Test onRateLimitReached with authenticated request
 RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, null, false);
 twitterBaseImpl.onRateLimitStatus(rateLimitStatusEvent);
 twitterBaseImpl.onRateLimitReached(rateLimitStatusEvent);
 Mockito.verify(rateLimitStatusListenerMock).onRateLimitReached(rateLimitStatusEvent);

 // Test onRateLimitReached with unauthenticated request
 RateLimitStatusEvent rateLimitStatusEventUnauthenticated = new RateLimitStatusEvent(twitterBaseImpl, null, true);
 twitterBaseImpl.onRateLimitStatus(rateLimitStatusEventUnauthenticated);
 twitterBaseImpl.onRateLimitReached(rateLimitStatusEventUnauthenticated);
 Mockito.verifyNoMoreInteractions(rateLimitStatusListenerMock);
 }
}