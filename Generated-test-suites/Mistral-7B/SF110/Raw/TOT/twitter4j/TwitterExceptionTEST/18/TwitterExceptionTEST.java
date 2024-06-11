import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.RateLimitStatus;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test Exception Message");
 assertEquals("Test Exception Message", exception.getMessage());
 assertNull(exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException cause = new IOException("Test IOException Message");
 TwitterException exception = new TwitterException("Test Exception Message", cause);
 assertEquals("Test Exception Message", exception.getMessage());
 assertSame(cause, exception.getCause());
 assertNull(exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertTrue(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse httpResponse = new HttpResponse(420, null);
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 assertEquals("Test Exception Message" + "\n" + getCause(httpResponse), exception.getMessage());
 assertEquals(420, exception.getStatusCode());
 assertNotNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertTrue(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 IOException cause = new IOException("Test IOException Message");
 TwitterException exception = new TwitterException("Test Exception Message", cause, 404);
 assertEquals("Test Exception Message", exception.getMessage());
 assertSame(cause, exception.getCause());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 assertEquals(404, exception.getStatusCode());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(420, null));
 assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testNotExceededRateLimitation() {
 TwitterException exception = new TwitterException("Test Exception Message");
 assertFalse(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(404, null));
 assertTrue(exception.resourceNotFound());
 }

 @Test
 public void testNotResourceNotFound() {
 TwitterException exception = new TwitterException("Test Exception Message");
 assertFalse(exception.resourceNotFound());
 }

 private String getCause(HttpResponse httpResponse) {
 String cause = null;
 switch (httpResponse.getStatusCode()) {
 case 304:
 cause = "There was no new data to return.";
 break;
 case 400:
 cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
 break;
 case 401:
 cause = "Authentication credentials were missing or incorrect.";
 break;
 case 403:
 cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
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
 cause = "Something is broken.  Please post to the group so the Twitter team can investigate.";
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
 return httpResponse.getStatusCode() + ":" + cause;
 }
}