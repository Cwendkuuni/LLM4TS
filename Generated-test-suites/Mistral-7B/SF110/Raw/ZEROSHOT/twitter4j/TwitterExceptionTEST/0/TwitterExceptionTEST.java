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
 Exception cause = new IOException();
 TwitterException exception = new TwitterException("Test Exception Message", cause);
 Assert.assertEquals("Test Exception Message", exception.getMessage());
 Assert.assertEquals(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse httpResponse = new HttpResponse();
 httpResponse.setStatusCode(420);
 httpResponse.setResponseHeader("Retry-After", "120");
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 Assert.assertEquals("Test Exception Message\nRetry-after: 120", exception.getMessage());
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertEquals(120, exception.getRetryAfter());
 }

 @Test
 public void testGetExceptionCode() {
 TwitterException exception = new TwitterException("Test Exception Message");
 Assert.assertNotSame("", exception.getExceptionCode());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeIsNot420() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse());
 exception.getRetryAfter();
 }

 @Test
 public void testEquals() {
 TwitterException exception1 = new TwitterException("Test Exception Message 1");
 TwitterException exception2 = new TwitterException("Test Exception Message 2");
 Assert.assertFalse(exception1.equals(exception2));

 TwitterException exception3 = new TwitterException("Test Exception Message 1");
 Assert.assertTrue(exception1.equals(exception3));

 HttpResponse httpResponse1 = new HttpResponse();
 httpResponse1.setStatusCode(420);
 httpResponse1.setResponseHeader("Retry-After", "120");
 TwitterException exception4 = new TwitterException("Test Exception Message 1", httpResponse1);
 TwitterException exception5 = new TwitterException("Test Exception Message 1", new HttpResponse());
 Assert.assertFalse(exception4.equals(exception5));

 TwitterException exception6 = new TwitterException("Test Exception Message 1", httpResponse1);
 Assert.assertTrue(exception4.equals(exception6));
 }

 @Test
 public void testHashCode() {
 TwitterException exception1 = new TwitterException("Test Exception Message 1");
 TwitterException exception2 = new TwitterException("Test Exception Message 2");
 Assert.assertNotSame(exception1.hashCode(), exception2.hashCode());

 TwitterException exception3 = new TwitterException("Test Exception Message 1");
 Assert.assertEquals(exception1.hashCode(), exception3.hashCode());

 HttpResponse httpResponse1 = new HttpResponse();
 httpResponse1.setStatusCode(420);
 httpResponse1.setResponseHeader("Retry-After", "120");
 TwitterException exception4 = new TwitterException("Test Exception Message 1", httpResponse1);
 TwitterException exception5 = new TwitterException("Test Exception Message 1", new HttpResponse());
 Assert.assertNotSame(exception4.hashCode(), exception5.hashCode());

 TwitterException exception6 = new TwitterException("Test Exception Message 1", httpResponse1);
 Assert.assertEquals(exception4.hashCode(), exception6.hashCode());
 }

 @Test
 public void testToString() {
 TwitterException exception = new TwitterException("Test Exception Message");
 Assert.assertTrue(exception.toString().contains("TwitterException{"));
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 TwitterException exception = new TwitterException("Test Exception Message", new IOException());
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse());
 exception.setStatusCode(420);
 Assert.assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse());
 exception.setStatusCode(404);
 Assert.assertTrue(exception.resourceNotFound());
 }
}