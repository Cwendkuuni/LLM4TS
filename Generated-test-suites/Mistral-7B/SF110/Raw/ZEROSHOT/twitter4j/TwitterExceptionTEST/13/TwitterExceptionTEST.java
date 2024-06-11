import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.RateLimitStatus;

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
 Assert.assertSame(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse response = new HttpResponse(400, "Test Response");
 TwitterException exception = new TwitterException("Test Exception Message", response);
 Assert.assertEquals("Test Exception Message\nTest Response", exception.getMessage());
 Assert.assertEquals(400, exception.getStatusCode());
 Assert.assertNotNull(exception.getRateLimitStatus());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 IOException cause = new IOException("Test IOException Message");
 TwitterException exception = new TwitterException("Test Exception Message", cause, 400);
 Assert.assertEquals("Test Exception Message", exception.getMessage());
 Assert.assertSame(cause, exception.getCause());
 Assert.assertEquals(400, exception.getStatusCode());
 }

 @Test
 public void testResetAfterProperty() {
 HttpResponse response = new HttpResponse(420, "Test Response");
 TwitterException exception = new TwitterException("Test Exception Message", response);
 Assert.assertEquals(-1, exception.getRetryAfter());
 response.setResponseHeader("Retry-After", "30");
 exception = new TwitterException("Test Exception Message", response);
 Assert.assertEquals(30, exception.getRetryAfter());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(400, ""));
 Assert.assertTrue(exception.exceededRateLimitation());
 exception = new TwitterException("Test Exception Message", new HttpResponse(420, ""));
 Assert.assertTrue(exception.exceededRateLimitation());
 exception = new TwitterException("Test Exception Message");
 Assert.assertFalse(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(404, ""));
 Assert.assertTrue(exception.resourceNotFound());
 exception = new TwitterException("Test Exception Message", new HttpResponse(200, ""));
 Assert.assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 IOException cause = new IOException("Test IOException Message");
 TwitterException exception = new TwitterException("Test Exception Message", cause);
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 exception = new TwitterException("Test Exception Message");
 Assert.assertFalse(exception.isCausedByNetworkIssue());
 }

 @Test
 public void testEquals() {
 TwitterException exception1 = new TwitterException("Test Exception Message 1", new HttpResponse(400, ""));
 TwitterException exception2 = new TwitterException("Test Exception Message 2", new HttpResponse(400, ""));
 Assert.assertTrue(exception1.equals(exception2));
 Assert.assertTrue(exception2.equals(exception1));
 TwitterException exception3 = new TwitterException("Test Exception Message 1", new HttpResponse(401, ""));
 Assert.assertFalse(exception1.equals(exception3));
 Assert.assertFalse(exception3.equals(exception1));
 }

 @Test
 public void testHashCode() {
 TwitterException exception1 = new TwitterException("Test Exception Message 1", new HttpResponse(400, ""));
 TwitterException exception2 = new TwitterException("Test Exception Message 2", new HttpResponse(400, ""));
 int hashCode1 = exception1.hashCode();
 int hashCode2 = exception2.hashCode();
 Assert.assertEquals(hashCode1, hashCode2);
 TwitterException exception3 = new TwitterException("Test Exception Message 1", new HttpResponse(401, ""));
 int hashCode3 = exception3.hashCode();
 Assert.assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(400, ""));
 String toString = exception.toString();
 Assert.assertTrue(toString.contains("TwitterException{exceptionCode=[...], statusCode=400, retryAfter=-1, rateLimitStatus=null, version=..."));
 }

 @Test
 public void testGetExceptionCodeWithRateLimitStatus() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(420, ""));
 String exceptionCode = exception.getExceptionCode();
 Assert.assertNotNull(exceptionCode);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWithoutRateLimitation() {
 TwitterException exception = new TwitterException("Test Exception Message");
 exception.getRetryAfter();
 }
}