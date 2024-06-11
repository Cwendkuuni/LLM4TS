package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.StatusCodes;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionEmptyMessage() {
 TwitterException exception = new TwitterException("Test Message");
 assertEquals("Test Message", exception.getMessage());
 assertNull(exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionCause() {
 IOException ioException = new IOException();
 TwitterException twitterException = new TwitterException("Test Message", ioException);
 assertEquals("Test Message: java.io.IOException", twitterException.getMessage());
 assertEquals(ioException, twitterException.getCause());
 assertNull(twitterException.getStatusCode());
 assertNull(twitterException.getRateLimitStatus());
 assertNull(twitterException.getFeatureSpecificRateLimitStatus());
 assertFalse(twitterException.isCausedByNetworkIssue());
 assertFalse(twitterException.exceededRateLimitation());
 assertFalse(twitterException.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionMessageAndHttpResponse() {
 String responseMessage = "Twitter error message";
 HttpResponse httpResponse = new HttpResponse();
 httpResponse.setStatusCode(StatusCodes.BAD_REQUEST);
 httpResponse.setStatusMessage(responseMessage);
 TwitterException twitterException = new TwitterException("Test Message", httpResponse);
 assertEquals("Test Message: Twitter error message", twitterException.getMessage());
 assertEquals(StatusCodes.BAD_REQUEST, twitterException.getStatusCode());
 assertNotNull(twitterException.getRateLimitStatus());
 assertNull(twitterException.getFeatureSpecificRateLimitStatus());
 assertFalse(twitterException.isCausedByNetworkIssue());
 assertTrue(twitterException.exceededRateLimitation());
 assertFalse(twitterException.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionMessageCauseAndHttpResponse() {
 String responseMessage = "Twitter error message";
 IOException ioException = new IOException();
 HttpResponse httpResponse = new HttpResponse();
 httpResponse.setStatusCode(StatusCodes.BAD_REQUEST);
 httpResponse.setStatusMessage(responseMessage);
 TwitterException twitterException = new TwitterException("Test Message", ioException, StatusCodes.BAD_REQUEST);
 assertEquals("Test Message: Twitter error message: java.io.IOException", twitterException.getMessage());
 assertEquals(ioException, twitterException.getCause());
 assertEquals(StatusCodes.BAD_REQUEST, twitterException.getStatusCode());
 assertNotNull(twitterException.getRateLimitStatus());
 assertNull(twitterException.getFeatureSpecificRateLimitStatus());
 assertTrue(twitterException.isCausedByNetworkIssue());
 assertTrue(twitterException.exceededRateLimitation());
 assertFalse(twitterException.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionMessageCauseHttpResponseRateLimitStatus() {
 String responseMessage = "Twitter error message";
 IOException ioException = new IOException();
 HttpResponse httpResponse = new HttpResponse();
 httpResponse.setStatusCode(StatusCodes.TOO_MANY_REQUESTS);
 httpResponse.setStatusMessage(responseMessage);
 httpResponse.setHeader("Retry-After", "300");
 TwitterException twitterException = new TwitterException("Test Message", ioException, httpResponse);
 assertEquals("Test Message: Twitter error message: java.io.IOException", twitterException.getMessage());
 assertEquals(ioException, twitterException.getCause());
 assertEquals(StatusCodes.TOO_MANY_REQUESTS, twitterException.getStatusCode());
 assertEquals(300, twitterException.getRetryAfter());
 assertNotNull(twitterException.getRateLimitStatus());
 assertNull(twitterException.getFeatureSpecificRateLimitStatus());
 assertTrue(twitterException.isCausedByNetworkIssue());
 assertTrue(twitterException.exceededRateLimitation());
 assertFalse(twitterException.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionMessageCauseHttpResponseRateLimitStatusFeatureSpecificRateLimitStatus() {
 String responseMessage = "Twitter error message";
 IOException ioException = new IOException();
 HttpResponse httpResponse = new HttpResponse();
 httpResponse.setStatusCode(StatusCodes.TOO_MANY_REQUESTS);
 httpResponse.setStatusMessage(responseMessage);
 httpResponse.setHeader("Retry-After", "300");
 httpResponse.setHeader("x-rate-x-limit-remaining", "100");
 httpResponse.setHeader("x-rate-x-limit-reset", "1234567890");
 TwitterException twitterException = new TwitterException("Test Message", ioException, httpResponse);
 assertEquals("Test Message: Twitter error message: java.io.IOException", twitterException.getMessage());
 assertEquals(ioException, twitterException.getCause());
 assertEquals(StatusCodes.TOO_MANY_REQUESTS, twitterException.getStatusCode());
 assertEquals(300, twitterException.getRetryAfter());
 assertNotNull(twitterException.getRateLimitStatus());
 assertEquals(100, twitterException.getRateLimitStatus().getRemaining());
 assertEquals(1234567890L, twitterException.getRateLimitStatus().getReset());
 assertNull(twitterException.getFeatureSpecificRateLimitStatus());
 assertTrue(twitterException.isCausedByNetworkIssue());
 assertTrue(twitterException.exceededRateLimitation());
 assertFalse(twitterException.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionEquals() {
 TwitterException twitterException1 = new TwitterException("Test Message 1", new IOException(), new HttpResponse(), 1, new RateLimitStatus(100, 1234567890), new RateLimitStatus(150, 1111111111));
 TwitterException twitterException2 = new TwitterException("Test Message 2", new IOException(), new HttpResponse(), 2, new RateLimitStatus(100, 1234567890), new RateLimitStatus(150, 1111111111));
 assertFalse(twitterException1.equals(twitterException2));
 TwitterException twitterException3 = new TwitterException("Test Message 1", new IOException(), new HttpResponse(), 1, new RateLimitStatus(100, 1234567890), new RateLimitStatus(150, 1111111111));
 assertTrue(twitterException1.equals(twitterException3));
 }

 @Test
 public void testTwitterExceptionHashCode() {
 TwitterException twitterException1 = new TwitterException("Test Message 1", new IOException(), new HttpResponse(), 1, new RateLimitStatus(100, 1234567890), new RateLimitStatus(150, 1111111111));
 TwitterException twitterException2 = new TwitterException("Test Message 2", new IOException(), new HttpResponse(), 2, new RateLimitStatus(100, 1234567890), new RateLimitStatus(150, 1111111111));
 int hashCode1 = twitterException1.hashCode();
 int hashCode2 = twitterException2.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 TwitterException twitterException3 = new TwitterException("Test Message 1", new IOException(), new HttpResponse(), 1, new RateLimitStatus(100, 1234567890), new RateLimitStatus(150, 1111111111));
 int hashCode3 = twitterException3.hashCode();
 assertEquals(hashCode1, hashCode3);
 }

}