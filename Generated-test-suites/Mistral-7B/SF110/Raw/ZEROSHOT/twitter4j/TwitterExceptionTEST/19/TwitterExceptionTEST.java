import org.junit.Assert;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.StatusLine;

public class TwitterExceptionTest {

 @org.junit.Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test Exception Message");
 Assert.assertEquals("Test Exception Message", exception.getMessage());
 Assert.assertEquals(-1, exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertFalse(exception.isCausedByNetworkIssue());
 Assert.assertFalse(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 Assert.assertNull(exception.getExceptionCode());
 Assert.assertFalse(exception.isNested());
 }

 @org.junit.Test
 public void testTwitterExceptionWithCause() {
 TwitterException exception = new TwitterException(new IOException("Test IOException Message"));
 Assert.assertEquals("Test IOException Message", exception.getCause().getMessage());
 Assert.assertEquals(-1, exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 Assert.assertFalse(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 Assert.assertNull(exception.getExceptionCode());
 Assert.assertFalse(exception.isNested());
 }

 @org.junit.Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 StatusLine statusLine = new StatusLine(304, "Not Modified");
 HttpResponse httpResponse = new HttpResponse(statusLine);
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 Assert.assertEquals("Test Exception Message" + System.lineSeparator() + "304: There was no new data to return.", exception.getMessage());
 Assert.assertEquals(304, exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertFalse(exception.isCausedByNetworkIssue());
 Assert.assertFalse(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 Assert.assertNull(exception.getExceptionCode());
 Assert.assertFalse(exception.isNested());
 }

 @org.junit.Test
 public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
 StatusLine statusLine = new StatusLine(400, "Bad Request");
 HttpResponse httpResponse = new HttpResponse(statusLine);
 TwitterException exception = new TwitterException("Test Exception Message", new IOException("Test IOException Message"), httpResponse);
 Assert.assertEquals("Test Exception Message" + System.lineSeparator() + "400: The request was invalid. An accompanying error message will explain why. This is the status code will be returned during rate limiting." + System.lineSeparator() + "IOException message: Test IOException Message", exception.getMessage());
 Assert.assertEquals(400, exception.getStatusCode());
 Assert.assertEquals(RateLimitStatus.LIMIT_RATE_LIMIT, exception.getRateLimitStatus().getLimit());
 Assert.assertEquals(RateLimitStatus.LIMIT_RATE_LIMIT, exception.getRateLimitStatus().getRemaining());
 Assert.assertEquals(-1, exception.getFeatureSpecificRateLimitStatus().getLimit());
 Assert.assertEquals(-1, exception.getFeatureSpecificRateLimitStatus().getRemaining());
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 Assert.assertTrue(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 Assert.assertNull(exception.getExceptionCode());
 Assert.assertFalse(exception.isNested());
 }

 @org.junit.Test
 public void testTwitterExceptionWithMessageCauseStatusCode() {
 TwitterException exception = new TwitterException("Test Exception Message", new IOException("Test IOException Message"), 503);
 Assert.assertEquals("Test Exception Message" + System.lineSeparator() + "503: Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later.", exception.getMessage());
 Assert.assertEquals(503, exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertFalse(exception.isCausedByNetworkIssue());
 Assert.assertFalse(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 Assert.assertNull(exception.getExceptionCode());
 Assert.assertFalse(exception.isNested());
 }

 @org.junit.Test
 public void testEquals() {
 TwitterException exception1 = new TwitterException("Test Exception Message 1");
 TwitterException exception2 = new TwitterException("Test Exception Message 2");
 TwitterException exception3 = new TwitterException("Test Exception Message 1");
 TwitterException exception4 = new TwitterException("Test Exception Message 1");
 TwitterException exception5 = new TwitterException("Test Exception Message 1", new IOException("Test IOException Message"), 420, 60);
 TwitterException exception6 = new TwitterException("Test Exception Message 1", new IOException("Test IOException Message"), 420, 60);
 TwitterException exception7 = new TwitterException("Test Exception Message 1", new IOException("Test IOException Message"), 421, 60);
 TwitterException exception8 = new TwitterException("Test Exception Message 1", new IOException("Test IOException Message"), 420, 59);

 Assert.assertTrue(exception1.equals(exception3));
 Assert.assertTrue(exception1.equals(exception4));
 Assert.assertFalse(exception1.equals(exception2));
 Assert.assertFalse(exception1.equals(null));
 Assert.assertFalse(exception1.equals("Test String"));
 Assert.assertFalse(exception1.equals(new Object()));

 Assert.assertTrue(exception5.equals(exception6));
 Assert.assertFalse(exception5.equals(exception1));
 Assert.assertFalse(exception5.equals(exception7));
 Assert.assertFalse(exception5.equals(exception8));
 }

 @org.junit.Test
 public void testHashCode() {
 TwitterException exception1 = new TwitterException("Test Exception Message 1");
 TwitterException exception2 = new TwitterException("Test Exception Message 2");
 TwitterException exception3 = new TwitterException("Test Exception Message 1");
 TwitterException exception4 = new TwitterException("Test Exception Message 1");
 TwitterException exception5 = new TwitterException("Test Exception Message 1", new IOException("Test IOException Message"), 420, 60);
 TwitterException exception6 = new TwitterException("Test Exception Message 1", new IOException("Test IOException Message"), 420, 60);
 TwitterException exception7 = new TwitterException("Test Exception Message 1", new IOException("Test IOException Message"), 421, 60);
 TwitterException exception8 = new TwitterException("Test Exception Message 1", new IOException("Test IOException Message"), 420, 59);

 Assert.assertEquals(exception1.hashCode(), exception3.hashCode());
 Assert.assertEquals(exception1.hashCode(), exception4.hashCode());
 Assert.assertEquals(exception5.hashCode(), exception6.hashCode());
 }

 @org.junit.Test
 public void testToString() {
 TwitterException exception = new TwitterException("Test Exception Message", new IOException("Test IOException Message"), 420, 60);
 Assert.assertEquals("TwitterException{exceptionCode=[], statusCode=420, retryAfter=60, rateLimitStatus=RateLimitStatus{limit=LIMIT_RATE_LIMIT, remaining=LIMIT_RATE_LIMIT}, version=4.0.7}", exception.toString());
 }
}