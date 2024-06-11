import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test error message");
 Assert.assertEquals("Test error message", exception.getMessage());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 TwitterException exception = new TwitterException(new IOException("Test network error"));
 Assert.assertNotNull(exception.getCause());
 Assert.assertEquals("Test network error", exception.getCause().getMessage());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse response = new HttpResponse(420, "retry-after: 30");
 TwitterException exception = new TwitterException("Test error message", response);
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertEquals(30, exception.getRetryAfter());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 TwitterException exception = new TwitterException("Test error message", new IOException("Test network error"), 400);
 Assert.assertEquals("Test error message", exception.getMessage());
 Assert.assertNotNull(exception.getCause());
 Assert.assertEquals("Test network error", exception.getCause().getMessage());
 Assert.assertEquals(400, exception.getStatusCode());
 }

 @Test
 public void testGetRateLimitStatus() {
 HttpResponse response = new HttpResponse(420, "x-ratelimit-limit: 100, x-ratelimit-remaining: 50, retry-after: 30");
 TwitterException exception = new TwitterException("Test error message", response);
 RateLimitStatus rateLimitStatus = exception.getRateLimitStatus();
 Assert.assertNotNull(rateLimitStatus);
 Assert.assertEquals(100, rateLimitStatus.getLimit());
 Assert.assertEquals(50, rateLimitStatus.getRemaining());
 }

 @Test
 public void testGetFeatureSpecificRateLimitStatus() {
 HttpResponse response = new HttpResponse(420, "x-ratelimit-limit: 100, x-ratelimit-remaining: 50, x-ratelimit-reset: 123456789");
 TwitterException exception = new TwitterException("Test error message", response);
 RateLimitStatus featureSpecificRateLimitStatus = exception.getFeatureSpecificRateLimitStatus();
 Assert.assertNotNull(featureSpecificRateLimitStatus);
 Assert.assertEquals(123456789, featureSpecificRateLimitStatus.getReset());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 TwitterException exception = new TwitterException("Test error message", new IOException("Test network error"));
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception = new TwitterException("Test error message", new HttpResponse(400, ""));
 Assert.assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Test error message", new HttpResponse(404, ""));
 Assert.assertTrue(exception.resourceNotFound());
 }
}