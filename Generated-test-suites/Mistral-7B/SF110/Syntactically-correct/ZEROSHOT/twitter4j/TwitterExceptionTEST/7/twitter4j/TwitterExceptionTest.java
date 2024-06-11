package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test Exception Message");
 assertEquals("Test Exception Message", exception.getMessage());
 assertNull(exception.getCause());
 assertNull(exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertNull(exception.getRetryAfter());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException cause = mock(IOException.class);
 TwitterException exception = new TwitterException("Test Exception Message", cause);
 assertEquals("Test Exception Message", exception.getMessage());
 assertSame(cause, exception.getCause());
 assertNull(exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertNull(exception.getRetryAfter());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse httpResponse = mock(HttpResponse.class);
 when(httpResponse.getStatusCode()).thenReturn(420);
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 assertEquals("Test Exception Message" + "\n" + getCause(httpResponse), exception.getMessage());
 assertEquals(420, exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertEquals(-1, exception.getRetryAfter());
 assertFalse(exception.isCausedByNetworkIssue());
 assertTrue(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 IOException cause = mock(IOException.class);
 HttpResponse httpResponse = mock(HttpResponse.class);
 when(httpResponse.getStatusCode()).thenReturn(400);
 TwitterException exception = new TwitterException("Test Exception Message", cause, 400);
 assertEquals("Test Exception Message", exception.getMessage());
 assertSame(cause, exception.getCause());
 assertEquals(400, exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertNull(exception.getRetryAfter());
 assertFalse(exception.isCausedByNetworkIssue());
 assertTrue(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionEquals() {
 TwitterException exception1 = new TwitterException("Test Exception Message 1");
 TwitterException exception2 = new TwitterException("Test Exception Message 2");
 assertFalse(exception1.equals(exception2));

 TwitterException exception3 = new TwitterException("Test Exception Message 1");
 TwitterException exception4 = new TwitterException("Test Exception Message 1");
 assertTrue(exception3.equals(exception4));

 TwitterException exception5 = new TwitterException("Test Exception Message 1", 420, RateLimitStatus.RATE_LIMIT_LIMIT_NONE);
 TwitterException exception6 = new TwitterException("Test Exception Message 1", 420, RateLimitStatus.RATE_LIMIT_LIMIT_NONE);
 assertTrue(exception5.equals(exception6));

 TwitterException exception7 = new TwitterException("Test Exception Message 1", 420, RateLimitStatus.RATE_LIMIT_LIMIT_NONE);
 TwitterException exception8 = new TwitterException("Test Exception Message 1", 420, RateLimitStatus.RATE_LIMIT_LIMIT_NONE, 10);
 assertFalse(exception7.equals(exception8));
}

 @Test
 public void testTwitterExceptionHashCode() {
 TwitterException exception1 = new TwitterException("Test Exception Message 1");
 TwitterException exception2 = new TwitterException("Test Exception Message 2");
 assertNotEquals(exception1.hashCode(), exception2.hashCode());

 TwitterException exception3 = new TwitterException("Test Exception Message 1");
 TwitterException exception4 = new TwitterException("Test Exception Message 1");
 assertEquals(exception3.hashCode(), exception4.hashCode());

 TwitterException exception5 = new TwitterException("Test Exception Message 1", 420, RateLimitStatus.RATE_LIMIT_LIMIT_NONE);
 TwitterException exception6 = new TwitterException("Test Exception Message 1", 420, RateLimitStatus.RATE_LIMIT_LIMIT_NONE);
 assertEquals(exception5.hashCode(), exception6.hashCode());

 TwitterException exception7 = new TwitterException("Test Exception Message 1", 420, RateLimitStatus.RATE_LIMIT_LIMIT_NONE);
 TwitterException exception8 = new TwitterException("Test Exception Message 1", 420, RateLimitStatus.RATE_LIMIT_LIMIT_NONE, 10);
 assertNotEquals(exception7.hashCode(), exception8.hashCode());
}

 private static String getCause(HttpResponse httpResponse) {
 // Implementation based on TwitterException.getCause(HttpResponse res)
 // method
 }
}