import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static twitter4j.internal.http.StatusCodes.*;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionEmptyMessage() {
 TwitterException exception = new TwitterException("Empty Message");
 assertEquals("Empty Message", exception.getMessage());
 assertNull(exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionIOExceptionCause() {
 IOException cause = new IOException();
 TwitterException exception = new TwitterException("IOException Message", cause);
 assertEquals("IOException Message", exception.getMessage());
 assertNull(exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertTrue(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionHttpResponseStatus400() {
 HttpResponse response = new HttpResponse(BAD_REQUEST);
 TwitterException exception = new TwitterException("HttpResponse Message", response);
 assertEquals("HttpResponse Message" + "\nThere was an error:  The request was invalid.  An accompanying error message will explain why.", exception.getMessage());
 assertEquals(BAD_REQUEST, exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionHttpResponseStatus400WithRateLimitStatus() {
 HttpResponse response = new HttpResponse(BAD_REQUEST);
 response.setHeader("Retry-After", "120");
 RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 240, 60);
 response.setHeader("X-Rate-Limit-Limit-Normal", rateLimitStatus.getLimitNormal().toString());
 response.setHeader("X-Rate-Limit-Remaining-Normal", rateLimitStatus.getRemainingNormal().toString());
 response.setHeader("X-Rate-Limit-Reset-Normal", rateLimitStatus.getResetNormal().toString());
 TwitterException exception = new TwitterException("HttpResponse Message", response);
 assertEquals("HttpResponse Message" + "\nThere was an error:  The request was invalid.  An accompanying error message will explain why.", exception.getMessage());
 assertEquals(BAD_REQUEST, exception.getStatusCode());
 assertEquals(rateLimitStatus, exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testTwitterExceptionHttpResponseStatus420() {
 HttpResponse response = new HttpResponse(I_AM_A_TEAPOT);
 TwitterException exception = new TwitterException("HttpResponse Message", response);
 assertEquals("HttpResponse Message" + "\nThe number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
 assertEquals(I_AM_A_TEAPOT, exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertTrue(exception.isCausedByNetworkIssue());
 assertTrue(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 assertEquals(120, exception.getRetryAfter());
 }

 @Test
 public void testTwitterExceptionHttpResponseStatus420WithRateLimitStatus() {
 HttpResponse response = new HttpResponse(I_AM_A_TEAPOT);
 response.setHeader("Retry-After", "120");
 RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 240, 60);
 response.setHeader("X-Rate-Limit-Limit-Normal", rateLimitStatus.getLimitNormal().toString());
 response.setHeader("X-Rate-Limit-Remaining-Normal", rateLimitStatus.getRemainingNormal().toString());
 response.setHeader("X-Rate-Limit-Reset-Normal", rateLimitStatus.getResetNormal().toString());
 TwitterException exception = new TwitterException("HttpResponse Message", response);
 assertEquals("HttpResponse Message" + "\nThe number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
 assertEquals(I_AM_A_TEAPOT, exception.getStatusCode());
 assertEquals(rateLimitStatus, exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertTrue(exception.isCausedByNetworkIssue());
 assertTrue(exception.exceededRateLimitation());
 assertEquals(120, exception.getRetryAfter());
 }

 @Test
 public void testTwitterExceptionHttpResponseStatus420WithFeatureSpecificRateLimitStatus() {
 HttpResponse response = new HttpResponse(I_AM_A_TEAPOT);
 response.setHeader("Retry-After", "120");
 response.setHeader("X-Rate-Limit-Limit-Normal", "15/300");
 response.setHeader("X-Rate-Limit-Remaining-Normal", "14/300");
 response.setHeader("X-Rate-Limit-Reset-Normal", "135");
 response.setHeader("X-Twitter-Rate-Limit-Scope", "search");
 response.setHeader("X-Twitter-Rate-Limit-Bucket", "0");
 RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 240, 60);
 RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus(3, 60, 60);
 response.setHeader("X-Rate-Limit-Limit-Search", "3/60");
 response.setHeader("X-Rate-Limit-Remaining-Search", "2/60");
 response.setHeader("X-Rate-Limit-Reset-Search", "120");
 TwitterException exception = new TwitterException("HttpResponse Message", response);
 assertEquals("HttpResponse Message" + "\nThe number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
 assertEquals(I_AM_A_TEAPOT, exception.getStatusCode());
 assertEquals(rateLimitStatus, exception.getRateLimitStatus());
 assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
 assertTrue(exception.isCausedByNetworkIssue());
 assertTrue(exception.exceededRateLimitation());
 assertEquals(120, exception.getRetryAfter());
 }
}