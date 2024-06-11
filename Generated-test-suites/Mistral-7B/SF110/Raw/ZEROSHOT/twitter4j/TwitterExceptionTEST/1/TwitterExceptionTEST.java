import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionEmpty() {
 TwitterException exception = new TwitterException("Test Message");
 Assert.assertEquals("Test Message", exception.getMessage());
 Assert.assertEquals(-1, exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertEquals(-1, exception.getRetryAfter());
 Assert.assertFalse(exception.isCausedByNetworkIssue());
 Assert.assertFalse(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 Assert.assertNull(exception.getExceptionCode());
 Assert.assertFalse(exception.isNested());
 }

 @Test
 public void testTwitterExceptionException() {
 Exception cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException(cause);
 Assert.assertEquals("Test IOException", exception.getCause().getMessage());
 Assert.assertEquals(-1, exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertEquals(-1, exception.getRetryAfter());
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 Assert.assertFalse(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 Assert.assertNull(exception.getExceptionCode());
 Assert.assertFalse(exception.isNested());
 }

 @Test
 public void testTwitterExceptionStringHttpResponse() {
 String errorMessage = "Test Error Message";
 HttpResponse response = new HttpResponse(420, errorMessage.getBytes());
 TwitterException exception = new TwitterException(errorMessage, response);
 Assert.assertEquals(errorMessage, exception.getMessage());
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertNotNull(exception.getRateLimitStatus());
 Assert.assertNotNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertEquals(-1, exception.getRetryAfter());
 Assert.assertFalse(exception.isCausedByNetworkIssue());
 Assert.assertTrue(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 Assert.assertNull(exception.getExceptionCode());
 Assert.assertFalse(exception.isNested());
 }

 @Test
 public void testTwitterExceptionStringException() {
 String errorMessage = "Test Error Message";
 Exception cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException(errorMessage, cause);
 Assert.assertEquals(errorMessage + "\n" + cause.getMessage(), exception.getMessage());
 Assert.assertEquals(-1, exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertEquals(-1, exception.getRetryAfter());
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 Assert.assertFalse(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 Assert.assertNull(exception.getExceptionCode());
 Assert.assertFalse(exception.isNested());
 }

 @Test
 public void testTwitterExceptionStringExceptionInt() {
 String errorMessage = "Test Error Message";
 Exception cause = new IOException("Test IOException");
 int statusCode = 400;
 TwitterException exception = new TwitterException(errorMessage, cause, statusCode);
 Assert.assertEquals(errorMessage + "\n" + cause.getMessage(), exception.getMessage());
 Assert.assertEquals(statusCode, exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertEquals(-1, exception.getRetryAfter());
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 Assert.assertFalse(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 Assert.assertNull(exception.getExceptionCode());
 Assert.assertFalse(exception.isNested());
 }

 @Test
 public void testGetExceptionCode() {
 String errorMessage = "Test Error Message";
 HttpResponse response = new HttpResponse(420, errorMessage.getBytes());
 TwitterException exception = new TwitterException(errorMessage, response);
 String exceptionCode = exception.getExceptionCode();
 Assert.assertNotNull(exceptionCode);
 }

 @Test
 public void testEquals() {
 TwitterException exception1 = new TwitterException("Test Error 1");
 TwitterException exception2 = new TwitterException("Test Error 2");
 Assert.assertFalse(exception1.equals(exception2));

 TwitterException exception3 = new TwitterException("Test Error 1");
 Assert.assertTrue(exception1.equals(exception3));

 TwitterException exception4 = new TwitterException("Test Error 1");
 TwitterException exception5 = new TwitterException("Test Error 1");
 exception4.setNested();
 Assert.assertFalse(exception4.equals(exception5));
}

 @Test
 public void testHashCode() {
 TwitterException exception1 = new TwitterException("Test Error 1");
 int hashCode1 = exception1.hashCode();

 TwitterException exception2 = new TwitterException("Test Error 1");
 int hashCode2 = exception2.hashCode();

 Assert.assertEquals(hashCode1, hashCode2);
 }
}