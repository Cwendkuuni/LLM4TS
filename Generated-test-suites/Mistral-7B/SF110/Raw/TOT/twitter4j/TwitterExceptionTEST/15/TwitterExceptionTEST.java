import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Mock error message");
 Assert.assertEquals("Mock error message", exception.getMessage());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException cause = new IOException("Mock IO error");
 TwitterException exception = new TwitterException("Mock error message", cause);
 Assert.assertEquals("Mock error message", exception.getMessage());
 Assert.assertEquals(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse response = new HttpResponse(420, "Retry-After: 120", null);
 TwitterException exception = new TwitterException("Mock error message", response);
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertNotNull(exception.getRateLimitStatus());
 Assert.assertEquals(120, exception.getRetryAfter());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 IOException cause = new IOException("Mock IO error");
 TwitterException exception = new TwitterException("Mock error message", cause, 400);
 Assert.assertEquals("Mock error message", exception.getMessage());
 Assert.assertEquals(cause, exception.getCause());
 Assert.assertEquals(400, exception.getStatusCode());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeIsNot420() {
 TwitterException exception = new TwitterException("Mock error message", new HttpResponse(200, null, null));
 exception.getRetryAfter();
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 IOException ioException = new IOException("Mock IO error");
 TwitterException twitterException = new TwitterException("Mock error message", ioException);
 Assert.assertTrue(twitterException.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception = new TwitterException("Mock error message", new HttpResponse(400, null, null));
 Assert.assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Mock error message", new HttpResponse(404, null, null));
 Assert.assertTrue(exception.resourceNotFound());
 }
}