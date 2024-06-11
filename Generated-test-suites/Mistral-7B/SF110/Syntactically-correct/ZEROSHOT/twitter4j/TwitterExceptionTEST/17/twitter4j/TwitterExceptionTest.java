package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

import static org.mockito.Mockito.mock;;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test Exception Message");
 Assert.assertEquals("Test Exception Message", exception.getMessage());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException cause = mock(IOException.class);
 TwitterException exception = new TwitterException("Test Exception Message", cause);
 Assert.assertEquals("Test Exception Message", exception.getMessage());
 Assert.assertSame(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 String causeMessage = "Http Response Cause Message";
 HttpResponse response = mock(HttpResponse.class);
 when(response.getStatusCode()).thenReturn(420);
 when(response.getResponseHeader("Retry-After")).thenReturn("120");
 TwitterException exception = new TwitterException("Test Exception Message", response);
 Assert.assertEquals("Test Exception Message\nHttp Response Cause Message", exception.getMessage());
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertEquals(120, exception.getRetryAfter());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertNotNull(exception.getRateLimitStatus());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 String causeMessage = "Http Response Cause Message";
 TwitterException causeException = new TwitterException("Http Response Cause Message");
 causeException.setNested();
 IOException exceptionCause = mock(IOException.class);
 TwitterException exception = new TwitterException("Test Exception Message", causeException, 400);
 Assert.assertEquals("Test Exception Message", exception.getMessage());
 Assert.assertSame(causeException, exception.getCause());
 Assert.assertSame(exceptionCause, causeException.getCause());
 Assert.assertEquals(400, exception.getStatusCode());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeIsNot420() {
 TwitterException exception = new TwitterException("Test Exception Message");
 exception.setStatusCode(404);
 exception.getRetryAfter();
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception = new TwitterException("Test Exception Message", mock(HttpResponse.class));
 when(exception.getStatusCode()).thenReturn(400);
 when(exception.getRateLimitStatus()).thenReturn(new RateLimitStatus(100, 120));
 Assert.assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Test Exception Message", mock(HttpResponse.class));
 when(exception.getStatusCode()).thenReturn(404);
 Assert.assertTrue(exception.resourceNotFound());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 TwitterException exception = new TwitterException("Test Exception Message", mock(IOException.class));
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 }

 @Test
 public void testGetExceptionCode() {
 TwitterException exception = new TwitterException("Test Exception Message", mock(HttpResponse.class));
 when(exception.getStatusCode()).thenReturn(400);
 when(exception.getRateLimitStatus()).thenReturn(new RateLimitStatus(100, 120));
 Assert.assertEquals("400:The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.", exception.getExceptionCode());
 }
}