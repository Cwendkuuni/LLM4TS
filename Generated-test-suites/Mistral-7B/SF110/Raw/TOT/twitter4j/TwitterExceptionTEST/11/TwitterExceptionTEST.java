import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test Exception Message");
 Assert.assertEquals("Test Exception Message", exception.getMessage());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException cause = new IOException("Test IOException Message");
 TwitterException exception = new TwitterException("Test Exception Message", cause);
 Assert.assertEquals("Test Exception Message", exception.getMessage());
 Assert.assertEquals(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse response = new HttpResponse();
 response.setStatusCode(400);
 response.setResponseHeader("Retry-After", "120");
 TwitterException exception = new TwitterException("Test Exception Message", response);
 Assert.assertEquals(400, exception.getStatusCode());
 Assert.assertEquals(120, exception.getRetryAfter());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 IOException cause = new IOException("Test IOException Message");
 TwitterException exception = new TwitterException("Test Exception Message", cause, 400);
 Assert.assertEquals("Test Exception Message", exception.getMessage());
 Assert.assertEquals(cause, exception.getCause());
 Assert.assertEquals(400, exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseStatusCodeRateLimitStatus() {
 HttpResponse response = new HttpResponse();
 response.setStatusCode(420);
 RateLimitStatus rateLimitStatus = new RateLimitStatus(5, 24, 60);
 response.setResponseHeader("X-Rate-Limit-Limit", "5");
 response.setResponseHeader("X-Rate-Limit-Remaining", "24");
 response.setResponseHeader("X-Rate-Limit-Reset", "1356998463");
 TwitterException exception = new TwitterException("Test Exception Message", new IOException("Test IOException Message"), response);
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertEquals(rateLimitStatus, exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseStatusCodeRateLimitStatusFeatureSpecificRateLimitStatus() {
 HttpResponse response = new HttpResponse();
 response.setStatusCode(420);
 RateLimitStatus rateLimitStatus = new RateLimitStatus(5, 24, 60);
 response.setResponseHeader("X-Rate-Limit-Limit", "5");
 response.setResponseHeader("X-Rate-Limit-Remaining", "24");
 response.setResponseHeader("X-Rate-Limit-Reset", "1356998463");
 response.setResponseHeader("X-Twitter-Rate-Limit-Bucket", "bucket1");
 RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus(1, 2, 3);
 TwitterException exception = new TwitterException("Test Exception Message", new IOException("Test IOException Message"), response);
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertEquals(rateLimitStatus, exception.getRateLimitStatus());
 Assert.assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
 }

 @Test(expected = IllegalStateException.class)
 public void testTwitterExceptionWithoutStatuscode() {
 TwitterException exception = new TwitterException("Test Exception Message");
 exception.setStatusCode(420);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTwitterExceptionWithNegativeRetryAfter() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse());
 exception.setRetryAfter(-1);
 }
}