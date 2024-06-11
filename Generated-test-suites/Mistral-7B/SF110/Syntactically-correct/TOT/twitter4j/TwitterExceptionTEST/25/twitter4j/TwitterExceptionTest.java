import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.RateLimitStatus;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test Message");
 Assert.assertEquals("Test Message", exception.getMessage());
 Assert.assertNull(exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertFalse(exception.isNested());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException("Test Message", cause);
 Assert.assertEquals("Test Message: Test IOException", exception.getMessage());
 Assert.assertNull(exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertFalse(exception.isNested());
 Assert.assertSame(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithHttpResponse() {
 HttpResponse response = new HttpResponse(420, "Retry-After: 30");
 TwitterException exception = new TwitterException("Test Message", response);
 Assert.assertEquals("Test Message: There was a rate limit exceeded. Retry-After: 30", exception.getMessage());
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertNotNull(exception.getRateLimitStatus());
 Assert.assertEquals(RateLimitStatus.RATE_LIMITED, exception.getRateLimitStatus().getLimit());
 Assert.assertEquals(30, exception.getRetryAfter());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertFalse(exception.isNested());
 }

 @Test
 public void testTwitterExceptionWithHttpResponseAndCause() {
 HttpResponse response = new HttpResponse(420, "Retry-After: 30");
 IOException cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException("Test Message", response, cause);
 Assert.assertEquals("Test Message: There was a rate limit exceeded. Retry-After: 30: Test IOException", exception.getMessage());
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertNotNull(exception.getRateLimitStatus());
 Assert.assertEquals(RateLimitStatus.RATE_LIMITED, exception.getRateLimitStatus().getLimit());
 Assert.assertEquals(30, exception.getRetryAfter());
 Assert.assertNotNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertFalse(exception.isNested());
 Assert.assertSame(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithHttpResponseNullRateLimitStatus() {
 HttpResponse response = new HttpResponse(420, null);
 TwitterException exception = new TwitterException("Test Message", response);
 Assert.assertEquals("Test Message: There was a rate limit exceeded.", exception.getMessage());
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertFalse(exception.isNested());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeIsNot420() {
 TwitterException exception = new TwitterException("Test Message", new HttpResponse(404, ""));
 exception.getRetryAfter();
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 IOException cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException("Test Message", cause);
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 IOException nestedIOException = new IOException("Nested IOException");
 TwitterException nestedException = new TwitterException("Test Message", nestedIOException);
 nestedException.setNested();
 TwitterException exceptionWithNested = new TwitterException("Test Message", nestedException);
 Assert.assertFalse(exceptionWithNested.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception1 = new TwitterException("Test Message", new HttpResponse(400, null));
 Assert.assertTrue(exception1.exceededRateLimitation());
 TwitterException exception2 = new TwitterException("Test Message", new HttpResponse(420, ""));
 Assert.assertTrue(exception2.exceededRateLimitation());
 TwitterException exception3 = new TwitterException("Test Message", new HttpResponse(304, ""));
 Assert.assertFalse(exception3.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Test Message", new HttpResponse(404, ""));
 Assert.assertTrue(exception.resourceNotFound());
 }
}