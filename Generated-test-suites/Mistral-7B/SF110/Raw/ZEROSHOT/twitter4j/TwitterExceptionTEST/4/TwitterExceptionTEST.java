import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusJSONImpl;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.StatusLine;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test Exception Message");
 assertEquals("Test Exception Message", exception.getMessage());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException cause = mock(IOException.class);
 TwitterException exception = new TwitterException("Test Exception Message", cause);
 assertEquals("Test Exception Message", exception.getMessage());
 assertSame(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse response = mock(HttpResponse.class);
 when(response.getStatusCode()).thenReturn(400);
 when(response.getResponseHeader("Retry-After")).thenReturn("120");
 TwitterException exception = new TwitterException("Test Exception Message", response);
 assertEquals(400, exception.getStatusCode());
 assertEquals(120, exception.getRetryAfter());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 IOException cause = mock(IOException.class);
 TwitterException exception = new TwitterException("Test Exception Message", cause, 400);
 assertEquals("Test Exception Message", exception.getMessage());
 assertSame(cause, exception.getCause());
 assertEquals(400, exception.getStatusCode());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseStatusCodeAndRateLimitStatus() {
 IOException cause = mock(IOException.class);
 StatusLine statusLine = mock(StatusLine.class);
 when(statusLine.getStatusCode()).thenReturn(400);
 when(statusLine.getResponseHeader("Retry-After")).thenReturn("120");
 HttpResponse response = mock(HttpResponse.class);
 when(response.getStatusLine()).thenReturn(statusLine);
 TwitterException exception = new TwitterException("Test Exception Message", cause, response);
 assertEquals(400, exception.getStatusCode());
 assertEquals(120, exception.getRetryAfter());
 RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
 when(response.getResponseHeader("X-Rate-Limit-Limit")).thenReturn("1000");
 when(response.getResponseHeader("X-Rate-Limit-Remaining")).thenReturn("999");
 when(response.getResponseHeader("X-Rate-Limit-Reset")).thenReturn("1234567890");
 assertSame(rateLimitStatus, exception.getRateLimitStatus());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseStatusCodeAndFeatureSpecificRateLimitStatus() {
 IOException cause = mock(IOException.class);
 StatusLine statusLine = mock(StatusLine.class);
 when(statusLine.getStatusCode()).thenReturn(400);
 when(statusLine.getResponseHeader("Retry-After")).thenReturn("120");
 when(statusLine.getResponseHeader("X-Rate-Limit-Limit")).thenReturn("1000");
 when(statusLine.getResponseHeader("X-Rate-Limit-Remaining")).thenReturn("999");
 when(statusLine.getResponseHeader("X-Rate-Limit-Reset")).thenReturn("1234567890");
 when(statusLine.getResponseHeader("X-Twitter-Trends-Shield-Status")).thenReturn("enabled");
 HttpResponse response = mock(HttpResponse.class);
 when(response.getStatusLine()).thenReturn(statusLine);
 TwitterException exception = new TwitterException("Test Exception Message", cause, response);
 assertEquals(400, exception.getStatusCode());
 assertEquals(120, exception.getRetryAfter());
 RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
 assertSame(rateLimitStatus, exception.getRateLimitStatus());
 RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatusJSONImpl();
 assertSame(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
 assertSame(rateLimitStatus, featureSpecificRateLimitStatus);
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 IOException cause = mock(IOException.class);
 TwitterException exception = new TwitterException("Test Exception Message", cause);
 assertTrue(exception.isCausedByNetworkIssue());
}

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse() {{
 setStatusCode(400);
 setResponseHeader("X-Rate-Limit-Limit", "1000");
 setResponseHeader("X-Rate-Limit-Remaining", "999");
 setResponseHeader("X-Rate-Limit-Reset", "1234567890");
 setResponseHeader("Retry-After", "120");
 }});
 assertTrue(exception.exceededRateLimitation());
}

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse() {{
 setStatusCode(404);
 }});
 assertTrue(exception.resourceNotFound());
}

 @Test
 public void testGetExceptionCode() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse() {{
 setStatusCode(400);
 setResponseHeader("X-Rate-Limit-Limit", "1000");
 setResponseHeader("X-Rate-Limit-Remaining", "999");
 setResponseHeader("X-Rate-Limit-Reset", "1234567890");
 setResponseHeader("Retry-After", "120");
 setResponseHeader("X-Twitter-Trends-Shield-Status", "enabled");
 }});
 String exceptionCode = exception.getExceptionCode();
 String expectedExceptionCode = "400:An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
 assertEquals(expectedExceptionCode, exceptionCode);
}
}