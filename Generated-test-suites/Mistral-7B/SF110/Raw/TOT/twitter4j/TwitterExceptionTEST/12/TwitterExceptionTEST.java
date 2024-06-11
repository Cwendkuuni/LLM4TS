import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.RateLimitStatus;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException ex = new TwitterException("Test Exception");
 assertEquals("Test Exception", ex.getMessage());
 assertNull(ex.getStatusCode());
 assertNull(ex.getRateLimitStatus());
 assertNull(ex.getFeatureSpecificRateLimitStatus());
 assertNull(ex.getRetryAfter());
 assertFalse(ex.isCausedByNetworkIssue());
 assertFalse(ex.exceededRateLimitation());
 assertFalse(ex.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException ioException = new IOException("Test IOException");
 TwitterException ex = new TwitterException("Test Exception", ioException);
 assertEquals("Test Exception: Test IOException", ex.getMessage());
 assertNull(ex.getStatusCode());
 assertNull(ex.getRateLimitStatus());
 assertNull(ex.getFeatureSpecificRateLimitStatus());
 assertNull(ex.getRetryAfter());
 assertFalse(ex.isCausedByNetworkIssue());
 assertFalse(ex.exceededRateLimitation());
 assertFalse(ex.resourceNotFound());
 assertNotNull(ex.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse res = new HttpResponse(400, "Bad Request", new byte[0]);
 TwitterException ex = new TwitterException("Test Exception", res);
 assertEquals("Test Exception: There was no new data to return.", ex.getMessage());
 assertEquals(400, ex.getStatusCode());
 assertNull(ex.getRateLimitStatus());
 assertNull(ex.getFeatureSpecificRateLimitStatus());
 assertNull(ex.getRetryAfter());
 assertFalse(ex.isCausedByNetworkIssue());
 assertFalse(ex.exceededRateLimitation());
 assertFalse(ex.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
 HttpResponse res = new HttpResponse(400, "Bad Request", new byte[0]);
 IOException ioException = new IOException("Test IOException");
 TwitterException ex = new TwitterException("Test Exception", ioException, 400, res);
 assertEquals("Test Exception: There was no new data to return: Test IOException", ex.getMessage());
 assertEquals(400, ex.getStatusCode());
 assertNotNull(ex.getRateLimitStatus());
 assertNull(ex.getFeatureSpecificRateLimitStatus());
 assertNull(ex.getRetryAfter());
 assertFalse(ex.isCausedByNetworkIssue());
 assertFalse(ex.exceededRateLimitation());
 assertFalse(ex.resourceNotFound());
 assertNotNull(ex.getCause());
 }

 @Test
 public void testTwitterExceptionWithHttpResponse() {
 HttpResponse res = new HttpResponse(420, "Rate Limit Exceeded", new byte[0]);
 TwitterException ex = new TwitterException(null, res);
 assertEquals("420: The number of requests you have made exceeds the quota afforded by your assigned rate limit.", ex.getMessage());
 assertEquals(420, ex.getStatusCode());
 assertNotNull(ex.getRateLimitStatus());
 assertNotNull(ex.getFeatureSpecificRateLimitStatus());
 assertTrue(ex.isCausedByNetworkIssue());
 assertFalse(ex.exceededRateLimitation());
 assertFalse(ex.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageHttpResponse() {
 HttpResponse res = new HttpResponse(420, "Rate Limit Exceeded", new byte[0]);
 TwitterException ex = new TwitterException("Test Exception", res);
 assertEquals("Test Exception: Rate Limit Exceeded", ex.getMessage());
 assertEquals(420, ex.getStatusCode());
 assertNotNull(ex.getRateLimitStatus());
 assertNotNull(ex.getFeatureSpecificRateLimitStatus());
 assertTrue(ex.isCausedByNetworkIssue());
 assertFalse(ex.exceededRateLimitation());
 assertFalse(ex.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithCauseHttpResponse() {
 HttpResponse res = new HttpResponse(420, "Rate Limit Exceeded", new byte[0]);
 IOException ioException = new IOException("Test IOException");
 TwitterException ex = new TwitterException(null, ioException, 420, res);
 assertEquals("Rate Limit Exceeded: Test IOException", ex.getMessage());
 assertEquals(420, ex.getStatusCode());
 assertNotNull(ex.getRateLimitStatus());
 assertNotNull(ex.getFeatureSpecificRateLimitStatus());
 assertTrue(ex.isCausedByNetworkIssue());
 assertFalse(ex.exceededRateLimitation());
 assertFalse(ex.resourceNotFound());
 assertNotNull(ex.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseHttpResponse() {
 HttpResponse res = new HttpResponse(420, "Rate Limit Exceeded", new byte[0]);
 IOException ioException = new IOException("Test IOException");
 TwitterException ex = new TwitterException("Test Exception", ioException, 420, res);
 assertEquals("Test Exception: Rate Limit Exceeded: Test IOException", ex.getMessage());
 assertEquals(420, ex.getStatusCode());
 assertNotNull(ex.getRateLimitStatus());
 assertNotNull(ex.getFeatureSpecificRateLimitStatus());
 assertTrue(ex.isCausedByNetworkIssue());
 assertFalse(ex.exceededRateLimitation());
 assertFalse(ex.resourceNotFound());
 assertNotNull(ex.getCause());
 }

 @Test
 public void testRateLimitStatus() {
 RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 30, 15);
 TwitterException ex = new TwitterException(null, null, 420, null, rateLimitStatus);
 assertEquals(15, ex.getRateLimitStatus().getSeconds());
 assertEquals(30, ex.getRateLimitStatus().getMillis());
 assertEquals(15, ex.getRateLimitStatus().getRemaining());
 }

 @Test
 public void testFeatureSpecificRateLimitStatus() {
 RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus(5, 60, 5);
 TwitterException ex = new TwitterException(null, null, 420, null, null, featureSpecificRateLimitStatus);
 assertEquals(5, ex.getFeatureSpecificRateLimitStatus().getSeconds());
 assertEquals(60, ex.getFeatureSpecificRateLimitStatus().getMillis());
 assertEquals(5, ex.getFeatureSpecificRateLimitStatus().getRemaining());
 }
}