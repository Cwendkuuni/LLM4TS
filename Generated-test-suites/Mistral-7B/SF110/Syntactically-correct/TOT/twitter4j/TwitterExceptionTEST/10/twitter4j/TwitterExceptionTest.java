import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
 IOException cause = new IOException("Test IOException Message");
 TwitterException exception = new TwitterException("Test Exception Message", cause);
 assertEquals("Test Exception Message", exception.getMessage());
 assertEquals(cause.getMessage(), exception.getCause().getMessage());
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
 HttpResponse response = new HttpResponse(420, "Retry-After: 120");
 TwitterException exception = new TwitterException("Test Exception Message", response);
 assertEquals("Test Exception Message", exception.getMessage());
 assertEquals(420, exception.getStatusCode());
 assertNotNull(exception.getRateLimitStatus());
 assertNotNull(exception.getFeatureSpecificRateLimitStatus());
 assertEquals(120, exception.getRetryAfter());
 assertTrue(exception.isCausedByNetworkIssue());
 assertTrue(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
 HttpResponse response = new HttpResponse(420, "Retry-After: 120");
 IOException cause = new IOException("Test IOException Message");
 TwitterException exception = new TwitterException("Test Exception Message", cause, response);
 assertEquals("Test Exception Message", exception.getMessage());
 assertEquals(cause.getMessage(), exception.getCause().getMessage());
 assertEquals(420, exception.getStatusCode());
 assertNotNull(exception.getRateLimitStatus());
 assertNotNull(exception.getFeatureSpecificRateLimitStatus());
 assertEquals(120, exception.getRetryAfter());
 assertTrue(exception.isCausedByNetworkIssue());
 assertTrue(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }
}