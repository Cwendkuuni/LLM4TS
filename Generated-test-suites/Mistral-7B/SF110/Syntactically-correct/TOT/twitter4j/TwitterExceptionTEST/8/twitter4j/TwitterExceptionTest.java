package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.RateLimitStatus;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test Message");
 assertEquals("Test Message", exception.getMessage());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertNull(exception.getStatusCode());
 assertNull(exception.getRetryAfter());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException(cause);
 assertEquals("Test IOException", exception.getCause().getMessage());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertNull(exception.getStatusCode());
 assertNull(exception.getRetryAfter());
 assertTrue(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse response = new HttpResponse(200, new byte[0]);
 TwitterException exception = new TwitterException("Test Message", response);
 assertEquals("Test Message" + "\n" + getCause(response).substring(3), exception.getMessage());
 assertEquals(200, exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertNull(exception.getRetryAfter());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 IOException cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException("Test Message", cause, 404);
 assertEquals("Test Message", exception.getMessage());
 assertEquals(cause, exception.getCause());
 assertEquals(404, exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertTrue(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseStatusCodeRateLimit() {
 HttpResponse response = new HttpResponse(420, new byte[0]);
 TwitterException exception = new TwitterException("Test Message", new IOException("Test IOException"), response);
 assertEquals("Test IOException: The number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
 assertEquals(420, exception.getStatusCode());
 assertEquals(RateLimitStatus.LIMIT_RATE_LIMIT, exception.getRateLimitStatus().getLimit());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertTrue(exception.isCausedByNetworkIssue());
 assertTrue(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseStatusCodeRateLimitFeatureSpecific() {
 HttpResponse response = new HttpResponse(420, new byte[0]);
 TwitterException exception = new TwitterException("Test Message", new IOException("Test IOException"), response);
 assertEquals(RateLimitStatusJSONImpl.createFromResponseHeader(response), exception.getFeatureSpecificRateLimitStatus());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeIsNot420() {
 TwitterException exception = new TwitterException("Test Message", new HttpResponse(400, new byte[0]));
 try {
 exception.getRetryAfter();
 } catch (IllegalStateException e) {
 assertEquals("Rate limitation is not exceeded", e.getMessage());
 }
 }
}