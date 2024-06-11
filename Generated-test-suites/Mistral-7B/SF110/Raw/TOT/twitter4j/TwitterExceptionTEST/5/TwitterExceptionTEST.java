import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.RateLimitStatus;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test Message");
 assertNotNull(exception);
 assertEquals("Test Message", exception.getMessage());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException("Test Message", cause);
 assertNotNull(exception);
 assertEquals("Test Message", exception.getMessage());
 assertSame(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse httpResponse = new HttpResponse();
 httpResponse.setStatusCode(420);
 httpResponse.setResponseHeader("Retry-After", "120");
 TwitterException exception = new TwitterException("Test Message", httpResponse);
 assertNotNull(exception);
 assertEquals("Test Message: " + httpResponse.getStatusCode() + ":" + getErrorMessage(httpResponse.getStatusCode()), exception.getMessage());
 assertEquals(420, exception.getStatusCode());
 assertEquals(120, exception.getRetryAfter());
 assertEquals(RateLimitStatus.OVERALL_LIMIT, exception.getRateLimitStatus().getLimit());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
 IOException cause = new IOException("Test IOException");
 HttpResponse httpResponse = new HttpResponse();
 httpResponse.setStatusCode(420);
 httpResponse.setResponseHeader("Retry-After", "120");
 TwitterException exception = new TwitterException("Test Message", cause, httpResponse);
 assertNotNull(exception);
 assertEquals("Test Message", exception.getMessage());
 assertSame(cause, exception.getCause());
 assertEquals(420, exception.getStatusCode());
 assertEquals(120, exception.getRetryAfter());
 assertEquals(RateLimitStatus.OVERALL_LIMIT, exception.getRateLimitStatus().getLimit());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseHttpResponseAndStatus() {
 IOException cause = new IOException("Test IOException");
 HttpResponse httpResponse = new HttpResponse();
 httpResponse.setStatusCode(404);
 TwitterException exception = new TwitterException("Test Message", cause, httpResponse, 404);
 assertNotNull(exception);
 assertEquals("Test Message", exception.getMessage());
 assertSame(cause, exception.getCause());
 assertEquals(404, exception.getStatusCode());
 assertNull(exception.getRetryAfter());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeIsNot420() {
 TwitterException exception = new TwitterException("Test Message", new HttpResponse(), 401);
 try {
 exception.getRetryAfter();
 }
 catch (IllegalStateException e) {
 assertEquals("Rate limitation is not exceeded", e.getMessage());
 }
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception = new TwitterException("Test Message", new HttpResponse(), 400, new RateLimitStatus(1, 5));
 assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Test Message", new HttpResponse(), 404);
 assertTrue(exception.resourceNotFound());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 IOException cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException("Test Message", cause);
 assertTrue(exception.isCausedByNetworkIssue());
 }

 @Test
 public void testEquals() {
 TwitterException exception1 = new TwitterException("Test Message 1", new HttpResponse(), 420, new RateLimitStatus(1, 5));
 TwitterException exception2 = new TwitterException("Test Message 2", new HttpResponse(), 420, new RateLimitStatus(1, 5));
 TwitterException exception3 = new TwitterException("Test Message 1", new HttpResponse(), 421, new RateLimitStatus(1, 5));
 assertTrue(exception1.equals(exception1));
 assertTrue(exception1.equals(exception2));
 assertFalse(exception1.equals(exception3));
 }

 private String getErrorMessage(int statusCode) {
 String errorMessage = "";
 switch (statusCode) {
 case 400:
 errorMessage = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
 break;
 case 401:
 errorMessage = "Authentication credentials were missing or incorrect.";
 break;
 case 403:
 errorMessage = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
 break;
 case 404:
 errorMessage = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
 break;
 case 406:
 errorMessage = "Returned by the Search API when an invalid format is specified in the request.";
 break;
 case 420:
 errorMessage = "The number of requests you have made exceeds the quota afforded by your assigned rate limit.";
 break;
 case 500:
 errorMessage = "Something is broken.  Please post to the group so the Twitter team can investigate.";
 break;
 case 502:
 errorMessage = "Twitter is down or being upgraded.";
 break;
 case 503:
 errorMessage = "Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
 break;
 default:
 errorMessage = "";
 }
 return errorMessage;
 }
}