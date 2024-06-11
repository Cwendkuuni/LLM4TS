import org.junit.Assert;
import org.junit.Test;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;

public class TwitterExceptionTest {

 private TwitterException createTwitterException(int statusCode, String message) {
 return new TwitterException(message);
 }

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException e = createTwitterException(0, "Test Message");
 Assert.assertEquals("Test Message", e.getMessage());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException cause = new IOException("Test IOException");
 TwitterException e = new TwitterException("Test Message", cause);
 Assert.assertEquals("Test Message", e.getMessage());
 Assert.assertEquals(cause, e.getCause());
 }

 @Test
 public void testTwitterExceptionWithHttpResponse() {
 HttpResponse httpResponse = new DefaultHttpResponse(400, "Bad Request");
 TwitterException e = new TwitterException("Test Message", httpResponse);
 Assert.assertEquals(400, e.getStatusCode());
 Assert.assertNotNull(e.getRateLimitStatus());
 }

 @Test
 public void testTwitterExceptionWithHttpResponseAndCause() {
 IOException cause = new IOException("Test IOException");
 HttpResponse httpResponse = new DefaultHttpResponse(400, "Bad Request");
 TwitterException e = new TwitterException("Test Message", httpResponse, cause);
 Assert.assertEquals(400, e.getStatusCode());
 Assert.assertNotNull(e.getRateLimitStatus());
 Assert.assertEquals(cause, e.getCause());
 }

 @Test
 public void testTwitterExceptionWithStatusCode() {
 TwitterException e = new TwitterException("Test Message", 400);
 Assert.assertEquals(400, e.getStatusCode());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWithoutStatusCode420() {
 TwitterException e = new TwitterException("Test Message", 404);
 try {
 e.getRetryAfter();
 }
 catch (IllegalStateException ex) {
 // Expected exception
 }
 }

 @Test
 public void testIsCausedByNetworkIssueWithIOExceptionCause() {
 TwitterException e = new TwitterException("Test Message", new IOException("Test IOException"));
 Assert.assertTrue(e.isCausedByNetworkIssue());
 }

 @Test
 public void testIsCausedByNetworkIssueWithNonIOExceptionCause() {
 TwitterException e = new TwitterException("Test Message", new RuntimeException("Test RuntimeException"));
 Assert.assertFalse(e.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException e = new TwitterException("Test Message", 400, new RateLimitStatus(10, 15));
 Assert.assertTrue(e.exceededRateLimitation());
 }

 @Test
 public void testExceededRateLimitationWithoutRateLimitStatus() {
 TwitterException e = new TwitterException("Test Message", 400);
 Assert.assertFalse(e.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException e = new TwitterException("Test Message", 404);
 Assert.assertTrue(e.resourceNotFound());
 }

 @Test
 public void testResourceNotFoundWithoutStatusCode404() {
 TwitterException e = new TwitterException("Test Message", 403);
 Assert.assertFalse(e.resourceNotFound());
 }
}