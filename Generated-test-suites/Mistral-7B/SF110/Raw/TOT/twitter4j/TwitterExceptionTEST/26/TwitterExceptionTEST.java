import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test Message");
 assertEquals("Test Message", exception.getMessage());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException("Test Message", cause);
 assertEquals("Test Message", exception.getMessage());
 assertEquals(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse response = new HttpResponse();
 response.setStatusCode(420);
 response.setResponseHeader("Retry-After", "30");
 TwitterException exception = new TwitterException("Test Message", response);
 assertEquals(420, exception.getStatusCode());
 assertEquals(30, exception.getRetryAfter());
 assertNotNull(exception.getRateLimitStatus());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
 HttpResponse response = new HttpResponse();
 response.setStatusCode(420);
 response.setResponseHeader("Retry-After", "30");
 IOException cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException("Test Message", cause, response);
 assertEquals(420, exception.getStatusCode());
 assertEquals(30, exception.getRetryAfter());
 assertNotNull(exception.getRateLimitStatus());
 assertEquals(cause, exception.getCause());
 }

 @Test
 public void testGetStatusCode() {
 TwitterException exception = new TwitterException("Test Message", new HttpResponse());
 exception.setStatusCode(404);
 assertEquals(404, exception.getStatusCode());
 }

 @Test
 public void testGetRateLimitStatus() {
 HttpResponse response = new HttpResponse();
 response.setStatusCode(420);
 response.setResponseHeader("X-Rate-Limit-Limit", "100");
 response.setResponseHeader("X-Rate-Limit-Remaining", "50");
 response.setResponseHeader("X-Rate-Limit-Reset", "120");
 TwitterException exception = new TwitterException("Test Message", response);
 RateLimitStatus rateLimitStatus = exception.getRateLimitStatus();
 assertEquals(100, rateLimitStatus.getLimit());
 assertEquals(50, rateLimitStatus.getRemaining());
 assertEquals(120, rateLimitStatus.getReset());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 IOException cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException("Test Message", cause);
 assertTrue(exception.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception = new TwitterException("Test Message", new HttpResponse());
 exception.setStatusCode(400);
 assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Test Message", new HttpResponse());
 exception.setStatusCode(404);
 assertTrue(exception.resourceNotFound());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeIsNot420() {
 TwitterException exception = new TwitterException("Test Message", new HttpResponse());
 exception.setStatusCode(400);
 try {
 exception.getRetryAfter();
 }
 catch (IllegalStateException e) {
 assertTrue(true);
 }
 }
}