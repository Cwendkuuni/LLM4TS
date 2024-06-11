import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
 assertNull(exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertNull(exception.getRetryAfter());
 assertTrue(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse response = mock(HttpResponse.class);
 when(response.getStatusCode()).thenReturn(400);
 TwitterException exception = new TwitterException("Test Exception Message", response);
 assertEquals("Test Exception Message", exception.getMessage());
 assertEquals(400, exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertNull(exception.getRetryAfter());
 assertFalse(exception.isCausedByNetworkIssue());
 assertTrue(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 IOException cause = mock(IOException.class);
 HttpResponse response = mock(HttpResponse.class);
 when(response.getStatusCode()).thenReturn(400);
 TwitterException exception = new TwitterException("Test Exception Message", cause, 400);
 assertEquals("Test Exception Message", exception.getMessage());
 assertEquals(400, exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertNull(exception.getRetryAfter());
 assertTrue(exception.isCausedByNetworkIssue());
 assertTrue(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithHttpResponse() {
 HttpResponse response = mock(HttpResponse.class);
 when(response.getStatusCode()).thenReturn(420);
 when(response.getResponseHeader("Retry-After")).thenReturn("120");
 TwitterException exception = new TwitterException("Test Exception Message", response);
 assertEquals("Test Exception Message", exception.getMessage());
 assertEquals(420, exception.getStatusCode());
 assertEquals(120, exception.getRetryAfter());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertTrue(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testRateLimitStatus() {
 RateLimitStatus rateLimitStatus = new RateLimitStatus();
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(), rateLimitStatus);
 assertEquals(rateLimitStatus, exception.getRateLimitStatus());
 }

 @Test
 public void testFeatureSpecificRateLimitStatus() {
 RateLimitStatus rateLimitStatus = new RateLimitStatus();
 RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus();
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(), rateLimitStatus, featureSpecificRateLimitStatus);
 assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
 }
}