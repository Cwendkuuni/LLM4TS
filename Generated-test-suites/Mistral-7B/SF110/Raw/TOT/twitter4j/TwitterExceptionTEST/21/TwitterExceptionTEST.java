import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.StatusLine;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test Exception Message");
 Assert.assertEquals("Test Exception Message", exception.getMessage());
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
 TwitterException exception = new TwitterException(new IOException("Test IOException Message"));
 Assert.assertEquals("Test IOException Message", exception.getMessage());
 Assert.assertEquals(-1, exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertEquals(0, exception.getRetryAfter());
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 Assert.assertFalse(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse httpResponse = new HttpResponse();
 StatusLine statusLine = new StatusLine();
 statusLine.setStatusCode(400);
 httpResponse.setStatusLine(statusLine);
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 Assert.assertEquals("Test Exception Message" + System.lineSeparator() + getCause(httpResponse), exception.getMessage());
 Assert.assertEquals(400, exception.getStatusCode());
 Assert.assertNotNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertEquals(-1, exception.getRetryAfter());
 Assert.assertFalse(exception.isCausedByNetworkIssue());
 Assert.assertTrue(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
 HttpResponse httpResponse = new HttpResponse();
 StatusLine statusLine = new StatusLine();
 statusLine.setStatusCode(420);
 httpResponse.setStatusLine(statusLine);
 TwitterException exception = new TwitterException("Test Exception Message", new IOException("Test IOException Message"), httpResponse);
 Assert.assertEquals("Test Exception Message" + System.lineSeparator() + getCause(httpResponse), exception.getMessage());
 Assert.assertEquals(420, exception.getStatusCode());
 Assert.assertNotNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertEquals(-1, exception.getRetryAfter());
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 Assert.assertTrue(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageStatusCodeAndFeatureSpecificRateLimitStatus() {
 TwitterException exception = new TwitterException("Test Exception Message", 400, new RateLimitStatus());
 Assert.assertEquals("Test Exception Message", exception.getMessage());
 Assert.assertEquals(400, exception.getStatusCode());
 Assert.assertNotNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertEquals(0, exception.getRetryAfter());
 Assert.assertFalse(exception.isCausedByNetworkIssue());
 Assert.assertTrue(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testEquals() {
 TwitterException exception1 = new TwitterException("Test Exception Message 1");
 TwitterException exception2 = new TwitterException("Test Exception Message 2");
 TwitterException exception3 = new TwitterException("Test Exception Message 1");
 Assert.assertNotSame(exception1, exception2);
 Assert.assertEquals(exception1, exception3);
 }

 @Test
 public void testHashCode() {
 TwitterException exception1 = new TwitterException("Test Exception Message 1");
 TwitterException exception2 = new TwitterException("Test Exception Message 2");
 TwitterException exception3 = new TwitterException("Test Exception Message 1");
 Assert.assertEquals(exception1.hashCode(), exception3.hashCode());
 Assert.assertNotEquals(exception1.hashCode(), exception2.hashCode());
 }

 private static String getCause(HttpResponse httpResponse) {
 String cause = "";
 StatusLine statusLine = httpResponse.getStatusLine();
 int statusCode = statusLine.getStatusCode();
 switch (statusCode) {
 case 304:
 cause = "There was no new data to return.";
 break;
 case 400:
 cause = "The request was invalid. An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
 break;
 case 401:
 cause = "Authentication credentials were missing or incorrect.";
 break;
 case 403:
 cause = "The request is understood, but it has been refused. An accompanying error message will explain why.";
 break;
 case 404:
 cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
 break;
 case 406:
 cause = "Returned by the Search API when an invalid format is specified in the request.";
 break;
 case 420:
 cause = "The number of requests you have made exceeds the quota afforded by your assigned rate limit.";
 break;
 case 500:
 cause = "Something is broken. Please post to the group so the Twitter team can investigate.";
 break;
 case 502:
 cause = "Twitter is down or being upgraded.";
 break;
 case 503:
 cause = "Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
 break;
 default:
 cause = "";
 break;
 }
 return statusCode + ": " + cause;
 }
}