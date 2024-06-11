import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test exception message");
 Assert.assertEquals("Test exception message", exception.getMessage());
 Assert.assertNull(exception.getCause());
 Assert.assertNull(exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertFalse(exception.isNested());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException cause = new IOException("Test cause message");
 TwitterException exception = new TwitterException("Test exception message", cause);
 Assert.assertEquals("Test cause message: Test exception message", exception.getMessage());
 Assert.assertSame(cause, exception.getCause());
 Assert.assertNull(exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertFalse(exception.isNested());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse httpResponse = new HttpResponse(420, "Retry-After: 30, X-Twitter-Rate-Limit-Reset: Wed, 01 Jan 1970 00:00:00 GMT");
 TwitterException exception = new TwitterException("Test exception message", httpResponse);
 Assert.assertEquals("Test exception message\nRetry-After: 30, X-Twitter-Rate-Limit-Reset: Wed, 01 Jan 1970 00:00:00 GMT", exception.getMessage());
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertNotNull(exception.getRateLimitStatus());
 Assert.assertNotNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertFalse(exception.isNested());
 Assert.assertEquals(30, exception.getRetryAfter());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
 IOException cause = new IOException("Test cause message");
 HttpResponse httpResponse = new HttpResponse(420, "Retry-After: 30, X-Twitter-Rate-Limit-Reset: Wed, 01 Jan 1970 00:00:00 GMT");
 TwitterException exception = new TwitterException("Test exception message", cause, 420, httpResponse);
 Assert.assertEquals("Test exception message: Test cause message", exception.getMessage());
 Assert.assertSame(cause, exception.getCause());
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertNotNull(exception.getRateLimitStatus());
 Assert.assertNotNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertFalse(exception.isNested());
 Assert.assertEquals(30, exception.getRetryAfter());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseStatusCodeAndHttpResponse() {
 IOException cause = new IOException("Test cause message");
 HttpResponse httpResponse = new HttpResponse(420, "Retry-After: 30, X-Twitter-Rate-Limit-Reset: Wed, 01 Jan 1970 00:00:00 GMT");
 TwitterException exception = new TwitterException("Test exception message", cause, 420, httpResponse.getStatusCode(), httpResponse);
 Assert.assertEquals("Test exception message: Test cause message", exception.getMessage());
 Assert.assertSame(cause, exception.getCause());
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertNotNull(exception.getRateLimitStatus());
 Assert.assertNotNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertFalse(exception.isNested());
 Assert.assertEquals(30, exception.getRetryAfter());
 }

 @Test
 public void testGetExceptionCode() {
 TwitterException exception = new TwitterException("Test exception message", new IOException("Test cause message"), 420, new HttpResponse(420, "Retry-After: 30, X-Twitter-Rate-Limit-Reset: Wed, 01 Jan 1970 00:00:00 GMT"));
 String expectedExceptionCode = "4200001:Test cause message:Test exception message";
 String actualExceptionCode = exception.getExceptionCode();
 Assert.assertEquals(expectedExceptionCode, actualExceptionCode);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeIsNot420() {
 TwitterException exception = new TwitterException("Test exception message", new HttpResponse(404, ""));
 exception.getRetryAfter();
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 IOException cause = new IOException("Test cause message");
 TwitterException exception = new TwitterException("Test exception message", cause);
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 HttpResponse httpResponse = new HttpResponse(420, "Retry-After: 30, X-Twitter-Rate-Limit-Reset: Wed, 01 Jan 1970 00:00:00 GMT");
 TwitterException exception = new TwitterException("Test exception message", httpResponse);
 Assert.assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 HttpResponse httpResponse = new HttpResponse(404, "");
 TwitterException exception = new TwitterException("Test exception message", httpResponse);
 Assert.assertTrue(exception.resourceNotFound());
 }
}