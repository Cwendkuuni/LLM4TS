import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import twitter4j.HttpResponse;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusJSONImpl;
import twitter4j.TwitterException;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test Exception Message");
 assertEquals("Test Exception Message", exception.getMessage());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 Exception cause = mock(IOException.class);
 TwitterException exception = new TwitterException("Test Exception Message", cause);
 assertEquals("Test Exception Message", exception.getMessage());
 assertEquals(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse httpResponse = mock(HttpResponse.class);
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 assertEquals("Test Exception Message" + System.lineSeparator() + getCause(httpResponse), exception.getMessage());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 Exception cause = mock(IOException.class);
 HttpResponse httpResponse = mock(HttpResponse.class);
 when(httpResponse.getStatusCode()).thenReturn(400);
 TwitterException exception = new TwitterException("Test Exception Message", cause, 400);
 assertEquals("Test Exception Message", exception.getMessage());
 assertEquals(cause, exception.getCause());
 assertEquals(400, exception.getStatusCode());
 }

 @Test
 public void testGetStatusCode() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(400));
 assertEquals(400, exception.getStatusCode());
 }

 @Test
 public void testGetRateLimitStatus() {
 HttpResponse httpResponse = mock(HttpResponse.class);
 RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
 when(httpResponse.getHeader("X-Rate-Limit-Limit")).thenReturn("100");
 when(httpResponse.getHeader("X-Rate-Limit-Remaining")).thenReturn("50");
 when(httpResponse.getHeader("X-Rate-Limit-Reset")).thenReturn("1234567890");
 when(RateLimitStatusJSONImpl.createFromResponseHeader(httpResponse)).thenReturn(rateLimitStatus);
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 assertEquals(rateLimitStatus, exception.getRateLimitStatus());
 }

 @Test
 public void testGetFeatureSpecificRateLimitStatus() {
 HttpResponse httpResponse = mock(HttpResponse.class);
 RateLimitStatusJSONImpl rateLimitStatusJSONImpl = mock(RateLimitStatusJSONImpl.class);
 RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
 when(httpResponse.getHeader("X-Rate-Limit-Limit-Friends-Write")).thenReturn("50");
 when(httpResponse.getHeader("X-Rate-Limit-Remaining-Friends-Write")).thenReturn("25");
 when(httpResponse.getHeader("X-Rate-Limit-Reset-Friends-Write")).thenReturn("1234567890");
 when(rateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(httpResponse)).thenReturn(rateLimitStatus);
 when(RateLimitStatusJSONImpl.createFromResponseHeader(httpResponse)).thenReturn(rateLimitStatus);
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 assertEquals(rateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 TwitterException exception = new TwitterException("Test Exception Message", new IOException());
 assertTrue(exception.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(400));
 assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testExceededRateLimitationWithRateLimitStatus() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(420));
 assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(404));
 assertTrue(exception.resourceNotFound());
 }

 @Test
 public void testGetRetryAfter() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(420, "Retry-After: 5"));
 assertEquals(5, exception.getRetryAfter());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWithInvalidStatusCode() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(404));
 exception.getRetryAfter();
 }
}