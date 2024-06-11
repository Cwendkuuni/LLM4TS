import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import twitter4j.TwitterException;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusJSONImpl;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.StatusLine;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test message");
 assertEquals("Test message", exception.getMessage());
 assertNull(exception.getCause());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 TwitterException exception = new TwitterException(new IOException("Test IOException"));
 assertEquals("Test IOException", exception.getCause().getMessage());
 assertEquals("Test IOException", exception.getMessage());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse response = new HttpResponse();
 StatusLine statusLine = new StatusLine("HTTP/1.1", 400, "Invalid Request");
 response.setStatusLine(statusLine);
 TwitterException exception = new TwitterException("Test message", response);
 assertEquals("Test message: Invalid Request", exception.getMessage());
 assertEquals(400, exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
 HttpResponse response = new HttpResponse();
 StatusLine statusLine = new StatusLine("HTTP/1.1", 420, "Rate limit exceeded");
 response.setStatusLine(statusLine);
 TwitterException exception = new TwitterException("Test message", new IOException("Test IOException"), response);
 assertEquals("Test message: Rate limit exceeded", exception.getMessage());
 assertEquals(420, exception.getStatusCode());
 assertNotNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertTrue(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseHttpResponseRateLimitStatus() {
 HttpResponse response = new HttpResponse();
 StatusLine statusLine = new StatusLine("HTTP/1.1", 420, "Rate limit exceeded");
 response.setStatusLine(statusLine);
 RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
 response.setResponseHeader("Retry-After", "120");
 exception.setRateLimitStatus(rateLimitStatus);
 TwitterException exception = new TwitterException("Test message", new IOException("Test IOException"), response);
 assertEquals("Test message: Rate limit exceeded", exception.getMessage());
 assertEquals(420, exception.getStatusCode());
 assertEquals(120, exception.getRetryAfter());
 assertNotNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertTrue(exception.isCausedByNetworkIssue());
 assertTrue(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }
}