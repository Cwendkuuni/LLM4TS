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
 Exception cause = new IOException("Test IO Exception");
 TwitterException exception = new TwitterException("Test Exception Message", cause);
 Assert.assertEquals("Test Exception Message", exception.getMessage());
 Assert.assertEquals(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse httpResponse = new HttpResponse(420, "Retry-After: 30");
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertEquals(30, exception.getRetryAfter());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 Exception cause = new IOException("Test IO Exception");
 TwitterException exception = new TwitterException("Test Exception Message", cause, 400);
 Assert.assertEquals("Test Exception Message", exception.getMessage());
 Assert.assertEquals(cause, exception.getCause());
 Assert.assertEquals(400, exception.getStatusCode());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 IOException ioException = new IOException("Test IO Exception");
 TwitterException twitterException = new TwitterException("Test Exception Message", ioException);
 Assert.assertTrue(twitterException.isCausedByNetworkIssue());
}

 @Test
 public void testExceededRateLimitation() {
 TwitterException twitterException = new TwitterException("Test Rate Limit Exceeded", new HttpResponse(400, null));
 Assert.assertTrue(twitterException.exceededRateLimitation());
}

 @Test
 public void testResourceNotFound() {
 TwitterException twitterException = new TwitterException("Test Resource Not Found", new HttpResponse(404, null));
 Assert.assertTrue(twitterException.resourceNotFound());
}

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeIsNot420() {
 TwitterException twitterException = new TwitterException("Test Exception Message", new HttpResponse(401, null));
 twitterException.getRetryAfter();
 }
}