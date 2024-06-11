import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test message");
 Assert.assertEquals("Test message", exception.getMessage());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 Exception cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException(cause);
 Assert.assertEquals("Test IOException", exception.getCause().getMessage());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 String message = "Test message";
 int statusCode = 400;
 HttpResponse httpResponse = new HttpResponse(statusCode, "Test Response");
 TwitterException exception = new TwitterException(message, httpResponse);
 Assert.assertEquals(message, exception.getMessage());
 Assert.assertEquals(statusCode, exception.getStatusCode());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 String message = "Test message";
 Exception cause = new IOException("Test IOException");
 int statusCode = 400;
 TwitterException exception = new TwitterException(message, cause, statusCode);
 Assert.assertEquals(message, exception.getMessage());
 Assert.assertEquals(cause, exception.getCause());
 Assert.assertEquals(statusCode, exception.getStatusCode());
 }

 @Test
 public void testGetStatusCode() {
 TwitterException exception = new TwitterException("Test message", new HttpResponse(400, ""));
 Assert.assertEquals(400, exception.getStatusCode());
 }

 @Test
 public void testGetRateLimitStatus() {
 HttpResponse httpResponse = new HttpResponse(420, "");
 TwitterException exception = new TwitterException("Test message", httpResponse);
 Assert.assertNotNull(exception.getRateLimitStatus());
 }

 @Test
 public void testGetFeatureSpecificRateLimitStatus() {
 HttpResponse httpResponse = new HttpResponse(420, "");
 TwitterException exception = new TwitterException("Test message", httpResponse);
 Assert.assertNotNull(exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testGetRetryAfter() {
 HttpResponse httpResponse = new HttpResponse(420, "Retry-After: 123");
 TwitterException exception = new TwitterException("Test message", httpResponse);
 Assert.assertEquals(123, exception.getRetryAfter());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeIsNot420() {
 HttpResponse httpResponse = new HttpResponse(400, "");
 TwitterException exception = new TwitterException("Test message", httpResponse);
 exception.getRetryAfter();
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 IOException ioException = new IOException("Test IOException");
 TwitterException exception = new TwitterException("Test message", ioException);
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 HttpResponse httpResponse = new HttpResponse(400, "Rate limit exceeded");
 TwitterException exception = new TwitterException("Test message", httpResponse);
 Assert.assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 HttpResponse httpResponse = new HttpResponse(404, "");
 TwitterException exception = new TwitterException("Test message", httpResponse);
 Assert.assertTrue(exception.resourceNotFound());
 }

 @Test
 public void testGetExceptionCode() {
 TwitterException exception = new TwitterException("Test message", new HttpResponse(400, ""));
 Assert.assertNotNull(exception.getExceptionCode());
 }

 @Test
 public void testSetNested() {
 TwitterException parentException = new TwitterException("Parent message");
 TwitterException childException = new TwitterException("Child message");
 childException.setNested();
 parentException.initCause(childException);
 Assert.assertTrue(parentException.isNested());
 }
}