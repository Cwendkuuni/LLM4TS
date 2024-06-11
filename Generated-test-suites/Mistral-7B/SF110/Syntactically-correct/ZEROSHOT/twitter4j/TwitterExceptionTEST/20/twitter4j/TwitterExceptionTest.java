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
 IOException cause = new IOException("Test IOException Message");
 TwitterException exception = new TwitterException("Test Exception Message", cause);
 Assert.assertEquals("Test Exception Message", exception.getMessage());
 Assert.assertSame(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithHttpResponse() {
 HttpResponse httpResponse = new HttpResponse(420, "Test Response");
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertEquals("Test Response", exception.getCause().getMessage());
 }

 @Test
 public void testTwitterExceptionWithHttpResponseAndCause() {
 HttpResponse httpResponse = new HttpResponse(420, "Test Response");
 IOException cause = new IOException("Test IOException Message");
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse, cause);
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertEquals("Test Response", exception.getCause().getMessage());
 Assert.assertSame(cause, exception.getCause());
 }

 @Test
 public void testGetStatusCode() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(420, "Test Response"));
 Assert.assertEquals(420, exception.getStatusCode());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeIsNot420() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(400, "Test Response"));
 exception.getRetryAfter();
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 TwitterException exception = new TwitterException("Test Exception Message", new IOException("Test IOException Message"));
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(400, "Test Response"));
 Assert.assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(404, "Test Response"));
 Assert.assertTrue(exception.resourceNotFound());
 }

 @Test
 public void testEquals() {
 TwitterException exception1 = new TwitterException("Test Exception Message 1", new HttpResponse(420, "Test Response 1"));
 TwitterException exception2 = new TwitterException("Test Exception Message 2", new HttpResponse(420, "Test Response 2"));
 TwitterException exception3 = new TwitterException("Test Exception Message 1", new HttpResponse(420, "Test Response 1"));
 Assert.assertTrue(exception1.equals(exception1));
 Assert.assertTrue(exception1.equals(exception3));
 Assert.assertFalse(exception1.equals(exception2));
 }

 @Test
 public void testHashCode() {
 TwitterException exception1 = new TwitterException("Test Exception Message 1", new HttpResponse(420, "Test Response 1"));
 TwitterException exception2 = new TwitterException("Test Exception Message 2", new HttpResponse(420, "Test Response 2"));
 Assert.assertEquals(exception1.hashCode(), exception1.hashCode());
 Assert.assertNotEquals(exception1.hashCode(), exception2.hashCode());
 }

 @Test
 public void testToString() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(420, "Test Response"));
 Assert.assertNotNull(exception.toString());
 }

}