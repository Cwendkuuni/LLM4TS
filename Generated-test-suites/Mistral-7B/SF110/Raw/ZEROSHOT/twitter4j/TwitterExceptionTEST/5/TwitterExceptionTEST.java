package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.StatusResponse;
import twitter4j.internal.http.StatusResponseImpl;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusJSONImpl;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test Exception Message");
 assertEquals("Test Exception Message", exception.getMessage());
 assertNull(exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isNested());
 assertNull(exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException cause = new IOException("Test IOException Message");
 TwitterException exception = new TwitterException("Test Exception Message", cause);
 assertEquals("Test Exception Message", exception.getMessage());
 assertEquals(-1, exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertTrue(exception.isNested());
 assertEquals(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 StatusResponse response = new StatusResponseImpl(420, "Test Response", new byte[0]);
 TwitterException exception = new TwitterException("Test Exception Message", response);
 assertEquals(-1, exception.getStatusCode());
 assertEquals(RateLimitStatus.RATE_LIMITED, exception.getRateLimitStatus().getLimit());
 assertEquals(RateLimitStatus.RATE_LIMITED, exception.getRateLimitStatus().getRemaining());
 assertEquals(-1, exception.getFeatureSpecificRateLimitStatus().getLimit());
 assertEquals(-1, exception.getFeatureSpecificRateLimitStatus().getRemaining());
 assertFalse(exception.isNested());
 assertNull(exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 IOException cause = new IOException("Test IOException Message");
 TwitterException exception = new TwitterException("Test Exception Message", cause, 420);
 assertEquals("Test Exception Message", exception.getMessage());
 assertEquals(420, exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertTrue(exception.isNested());
 assertEquals(cause, exception.getCause());
 }

 @Test
 public void testGetExceptionCode() {
 TwitterException exception = new TwitterException("Test Exception Message", new IOException("Test IOException Message"));
 assertEquals("312080216241338368", exception.getExceptionCode());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 IOException cause = new IOException("Test IOException Message");
 TwitterException exception = new TwitterException("Test Exception Message", cause);
 assertTrue(exception.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception = new TwitterException("Test Exception Message", new StatusResponseImpl(420, "Test Response", new byte[0]));
 assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Test Exception Message", new StatusResponseImpl(404, "Test Response", new byte[0]));
 assertTrue(exception.resourceNotFound());
 }

 @Test
 public void testEquals() {
 TwitterException exception1 = new TwitterException("Test Exception Message 1", new StatusResponseImpl(420, "Test Response 1", new byte[0]));
 TwitterException exception2 = new TwitterException("Test Exception Message 2", new StatusResponseImpl(420, "Test Response 2", new byte[0]));
 TwitterException exception3 = new TwitterException("Test Exception Message 1", new StatusResponseImpl(420, "Test Response 1", new byte[0]));
 assertTrue(exception1.equals(exception1));
 assertTrue(exception1.equals(exception3));
 assertFalse(exception1.equals(exception2));
 }

 @Test
 public void testHashCode() {
 TwitterException exception1 = new TwitterException("Test Exception Message 1", new StatusResponseImpl(420, "Test Response 1", new byte[0]));
 TwitterException exception2 = new TwitterException("Test Exception Message 2", new StatusResponseImpl(420, "Test Response 2", new byte[0]));
 TwitterException exception3 = new TwitterException("Test Exception Message 1", new StatusResponseImpl(420, "Test Response 1", new byte[0]));
 assertEquals(exception1.hashCode(), exception3.hashCode());
 assertNotSame(exception1.hashCode(), exception2.hashCode());
 }
}