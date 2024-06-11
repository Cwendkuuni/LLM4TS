import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test exception message");
 Assert.assertEquals("Test exception message", exception.getMessage());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException cause = new IOException("Test IO exception");
 TwitterException exception = new TwitterException("Test exception message", cause);
 Assert.assertSame(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse httpResponse = new HttpResponse(400, "Bad Request", new byte[0]);
 TwitterException exception = new TwitterException("Test exception message", httpResponse);
 Assert.assertEquals(400, exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 IOException cause = new IOException("Test IO exception");
 TwitterException exception = new TwitterException("Test exception message", cause, 400);
 Assert.assertEquals(400, exception.getStatusCode());
 Assert.assertSame(cause, exception.getCause());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseStatusCodeAndRateLimitStatus() {
 HttpResponse httpResponse = new HttpResponse(420, "Rate Limit Exceeded", new byte[0]);
 RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 15, 60);
 TwitterException exception = new TwitterException("Test exception message", new IOException("Test IO exception"), 420, rateLimitStatus);
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertSame(new IOException("Test IO exception"), exception.getCause());
 Assert.assertEquals(rateLimitStatus, exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseStatusCodeRateLimitStatusAndFeatureSpecificRateLimitStatus() {
 HttpResponse httpResponse = new HttpResponse(420, "Rate Limit Exceeded", new byte[0]);
 RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 15, 60);
 RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus(5, 5, 300);
 TwitterException exception = new TwitterException("Test exception message", new IOException("Test IO exception"), 420, rateLimitStatus, featureSpecificRateLimitStatus);
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertSame(new IOException("Test IO exception"), exception.getCause());
 Assert.assertEquals(rateLimitStatus, exception.getRateLimitStatus());
 Assert.assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testGetRateLimitStatus() {
 TwitterException exception = new TwitterException("Test exception message", new HttpResponse(420, "Rate Limit Exceeded", new byte[0]));
 Assert.assertNotNull(exception.getRateLimitStatus());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRateLimitStatusWhenStatusCodeIsNot420() {
 TwitterException exception = new TwitterException("Test exception message", new HttpResponse(400, "Bad Request", new byte[0]));
 exception.getRateLimitStatus();
 }

 @Test
 public void testGetFeatureSpecificRateLimitStatus() {
 TwitterException exception = new TwitterException("Test exception message", new HttpResponse(420, "Rate Limit Exceeded", new byte[0]));
 RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus(5, 5, 300);
 exception.setFeatureSpecificRateLimitStatus(featureSpecificRateLimitStatus);
 Assert.assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetFeatureSpecificRateLimitStatusWhenStatusCodeIsNot420() {
 TwitterException exception = new TwitterException("Test exception message", new HttpResponse(400, "Bad Request", new byte[0]));
 exception.getFeatureSpecificRateLimitStatus();
 }

 @Test
 public void testGetRetryAfter() {
 HttpResponse httpResponse = new HttpResponse(420, "Rate Limit Exceeded", "Retry-After: 120");
 TwitterException exception = new TwitterException("Test exception message", httpResponse);
 Assert.assertEquals(120, exception.getRetryAfter());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 IOException cause = new IOException("Test IO exception");
 TwitterException exception = new TwitterException("Test exception message", cause);
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception = new TwitterException("Test exception message", new HttpResponse(400, "Bad Request", new byte[0]));
 Assert.assertFalse(exception.exceededRateLimitation());
 }

 @Test
 public void testExceededRateLimitationWithRateLimitStatus() {
 TwitterException exception = new TwitterException("Test exception message", new HttpResponse(420, "Rate Limit Exceeded", new byte[0]));
 Assert.assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testExceededRateLimitationWithFeatureSpecificRateLimitStatus() {
 TwitterException exception = new TwitterException("Test exception message", new HttpResponse(429, "Too Many Requests", new byte[0]));
 exception.setFeatureSpecificRateLimitStatus(new RateLimitStatus(5, 5, 300));
 Assert.assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Test exception message", new HttpResponse(404, "Not Found", new byte[0]));
 Assert.assertTrue(exception.resourceNotFound());
 }

 @Test
 public void testResourceNotFoundWithMessage() {
 TwitterException exception = new TwitterException("Test exception message", new HttpResponse(200, "OK", new byte[0]));
 Assert.assertFalse(exception.resourceNotFound());
 }
}