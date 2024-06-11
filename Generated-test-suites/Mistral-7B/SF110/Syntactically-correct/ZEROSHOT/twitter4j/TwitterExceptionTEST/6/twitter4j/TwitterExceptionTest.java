package twitter4j.test;

import static org.junit.Assert.*;

import org.junit.Test;

import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j. RateLimitStatus;

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
 IOException cause = new IOException("Test IO Exception Message");
 TwitterException exception = new TwitterException("Test Exception Message", cause);
 assertEquals("Test Exception Message", exception.getMessage());
 assertEquals(cause, exception.getCause());
 assertNull(exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse httpResponse = new HttpResponse(400, "Test Response Message");
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 assertEquals("Test Exception Message: There was an error: Test Response Message", exception.getMessage());
 assertEquals(400, exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
 IOException cause = new IOException("Test IO Exception Message");
 HttpResponse httpResponse = new HttpResponse(400, "Test Response Message");
 TwitterException exception = new TwitterException("Test Exception Message", cause, httpResponse);
 assertEquals("Test Exception Message: There was an error: Test Response Message", exception.getMessage());
 assertEquals(cause, exception.getCause());
 assertEquals(400, exception.getStatusCode());
 assertEquals(null, exception.getRateLimitStatus());
 assertEquals(null, exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testGetStatusCode() {
 HttpResponse httpResponse = new HttpResponse(404, "");
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 assertEquals(404, exception.getStatusCode());
 }

 @Test
 public void testExceededRateLimitation() {
 HttpResponse httpResponse = new HttpResponse(420, "");
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 HttpResponse httpResponse = new HttpResponse(400, "");
 IOException cause = new IOException("Test IO Exception Message");
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse, cause);
 assertTrue(exception.isCausedByNetworkIssue());
 }

 @Test
 public void testResourceNotFound() {
 HttpResponse httpResponse = new HttpResponse(404, "");
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 assertTrue(exception.resourceNotFound());
 }

 @Test
 public void testGetRateLimitStatus() {
 HttpResponse httpResponse = new HttpResponse(420, "");
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 RateLimitStatus rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(httpResponse);
 assertEquals(rateLimitLimitStatus, exception.getRateLimitStatus());
 }

 @Test
 public void testGetFeatureSpecificRateLimitStatus() {
 HttpResponse httpResponse = new HttpResponse(420, "");
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 RateLimitStatus featureSpecificRateLimitStatus = RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(httpResponse);
 assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testGetRetryAfter() {
 HttpResponse httpResponse = new HttpResponse(420, "Retry-After: 300");
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 assertEquals(300, exception.getRetryAfter());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeIsNot420() {
 HttpResponse httpResponse = new HttpResponse(400, "");
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 exception.getRetryAfter();
 }
}