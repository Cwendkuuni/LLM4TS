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
 IOException cause = new IOException();
 TwitterException exception = new TwitterException("Test Exception Message", cause);
 Assert.assertEquals("Test Exception Message", exception.getMessage());
 Assert.assertSame(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse response = new HttpResponse(420, "Retry-After: 10, X-Rate-Limit-Limit: 15, X-Rate-Limit-Remaining: 10, X-Rate-Limit-Reset: Wed, 14 Jan 2021 04:03:37 GMT");
 TwitterException exception = new TwitterException("Test Exception Message", response);
 Assert.assertEquals("Test Exception Message\nRetry-After: 10, X-Rate-Limit-Limit: 15, X-Rate-Limit-Remaining: 10, X-Rate-Limit-Reset: Wed, 14 Jan 2021 04:03:37 GMT", exception.getMessage());
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertNotNull(exception.getRateLimitStatus());
 Assert.assertNotNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertEquals(10, exception.getRetryAfter());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 IOException cause = new IOException();
 TwitterException exception = new TwitterException("Test Exception Message", cause, 404);
 Assert.assertEquals("Test Exception Message", exception.getMessage());
 Assert.assertSame(cause, exception.getCause());
 Assert.assertEquals(404, exception.getStatusCode());
 }

 @Test
 public void testGetStatusCode() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(420, ""));
 Assert.assertEquals(420, exception.getStatusCode());
 }

 @Test
 public void testGetRateLimitStatus() {
 HttpResponse response = new HttpResponse(420, "Retry-After: 10, X-Rate-Limit-Limit: 15, X-Rate-Limit-Remaining: 10, X-Rate-Limit-Reset: Wed, 14 Jan 2021 04:03:37 GMT");
 TwitterException exception = new TwitterException("Test Exception Message", response);
 Assert.assertNotNull(exception.getRateLimitStatus());
 }

 @Test
 public void testGetFeatureSpecificRateLimitStatus() {
 HttpResponse response = new HttpResponse(420, "Retry-After: 10, X-Rate-Limit-Limit: 15, X-Rate-Limit-Remaining: 10, X-Rate-Limit-Reset: Wed, 14 Jan 2021 04:03:37 GMT, X-Rate-Limit-Bucket: tweets, X-Rate-Limit-Bucket-Period: 15");
 TwitterException exception = new TwitterException("Test Exception Message", response);
 Assert.assertNotNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertEquals("tweets", exception.getFeatureSpecificRateLimitStatus().getBucket());
 Assert.assertEquals(15, exception.getFeatureSpecificRateLimitStatus().getBucketPeriod());
 }

 @Test
 public void testGetRetryAfter() {
 HttpResponse response = new HttpResponse(420, "Retry-After: 10");
 TwitterException exception = new TwitterException("Test Exception Message", response);
 Assert.assertEquals(10, exception.getRetryAfter());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 TwitterException exception = new TwitterException("Test Exception Message", new IOException());
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception1 = new TwitterException("Test Exception Message", new HttpResponse(400, ""));
 Assert.assertFalse(exception1.exceededRateLimitation());
 TwitterException exception2 = new TwitterException("Test Exception Message", new HttpResponse(420, ""));
 Assert.assertTrue(exception2.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception1 = new TwitterException("Test Exception Message", new HttpResponse(401, ""));
 Assert.assertFalse(exception1.resourceNotFound());
 TwitterException exception2 = new TwitterException("Test Exception Message", new HttpResponse(404, ""));
 Assert.assertTrue(exception2.resourceNotFound());
 }

 @Test
 public void testGetExceptionCode() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(420, ""));
 Assert.assertNotNull(exception.getExceptionCode());
 }

 @Test
 public void testSetNested() {
 TwitterException exception1 = new TwitterException("Test Exception Message", new HttpResponse(420, ""));
 Assert.assertFalse(exception1.isNested());
 exception1.setNested();
 Assert.assertTrue(exception1.isNested());
 }
}