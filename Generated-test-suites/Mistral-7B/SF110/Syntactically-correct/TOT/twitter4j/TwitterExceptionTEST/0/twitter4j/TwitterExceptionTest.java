import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.ratelimit.RateLimitStatus;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test exception message");
 Assert.assertEquals("Test exception message", exception.getMessage());
 Assert.assertEquals(-1, exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertEquals(0, exception.getRetryAfter());
 Assert.assertFalse(exception.isCausedByNetworkIssue());
 Assert.assertFalse(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException cause = new IOException("Test cause message");
 TwitterException exception = new TwitterException("Test exception message", cause);
 Assert.assertEquals("Test exception message", exception.getMessage());
 Assert.assertEquals(-1, exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertEquals(0, exception.getRetryAfter());
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 Assert.assertArrayEquals(new Object[]{cause}, exception.getSuppressed());
 Assert.assertFalse(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse response = new HttpResponse(400, "Error message", "text/plain");
 TwitterException exception = new TwitterException("Test exception message", response);
 Assert.assertEquals("Test exception message\nError message", exception.getMessage());
 Assert.assertEquals(400, exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertEquals(0, exception.getRetryAfter());
 Assert.assertFalse(exception.isCausedByNetworkIssue());
 Assert.assertFalse(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
 IOException cause = new IOException("Test cause message");
 HttpResponse response = new HttpResponse(420, "Error message", "text/plain");
 TwitterException exception = new TwitterException("Test exception message", cause, response);
 Assert.assertEquals("Test exception message", exception.getMessage());
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertNotNull(exception.getRateLimitStatus());
 Assert.assertNotNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertEquals(0, exception.getRetryAfter());
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 Assert.assertTrue(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeNot420() {
 TwitterException exception = new TwitterException("Test exception message", new HttpResponse(400, "Error message", "text/plain"));
 Assert.assertEquals(-1, exception.getRetryAfter());
 }

 @Test
 public void testEquals() {
 TwitterException exception1 = new TwitterException("Test exception message 1", new HttpResponse(420, "Error message 1", "text/plain"));
 TwitterException exception2 = new TwitterException("Test exception message 2", new HttpResponse(420, "Error message 2", "text/plain"));
 TwitterException exception3 = new TwitterException("Test exception message 1", new HttpResponse(420, "Error message 1", "text/plain"));
 TwitterException exception4 = new TwitterException("Test exception message 1", new HttpResponse(400, "Error message 1", "text/plain"));
 Assert.assertTrue(exception1.equals(exception1));
 Assert.assertTrue(exception1.equals(exception3));
 Assert.assertFalse(exception1.equals(exception2));
 Assert.assertFalse(exception1.equals(exception4));
 }

 @Test
 public void testGetHashCode() {
 TwitterException exception1 = new TwitterException("Test exception message 1", new HttpResponse(420, "Error message 1", "text/plain"));
 TwitterException exception2 = new TwitterException("Test exception message 2", new HttpResponse(420, "Error message 2", "text/plain"));
 TwitterException exception3 = new TwitterException("Test exception message 1", new HttpResponse(420, "Error message 1", "text/plain"));
 TwitterException exception4 = new TwitterException("Test exception message 1", new HttpResponse(400, "Error message 1", "text/plain"));
 Assert.assertEquals(exception1.hashCode(), exception3.hashCode());
 Assert.assertNotEquals(exception1.hashCode(), exception2.hashCode());
 Assert.assertNotEquals(exception1.hashCode(), exception4.hashCode());
 }
}