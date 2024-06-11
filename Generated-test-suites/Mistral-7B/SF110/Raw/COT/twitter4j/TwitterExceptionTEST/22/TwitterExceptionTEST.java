import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import twitter4j.HttpResponse;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusJSONImpl;
import twitter4j.TwitterException;
import twitter4j.TwitterResponse;
import twitter4j.internal.http.StatusLine;

public class TwitterExceptionTest {

 private TwitterException twitterException;
 private HttpResponse httpResponse;
 private RateLimitStatus rateLimitStatus;
 private RateLimitStatus featureSpecificRateLimitStatus;

 @Test
 public void testTwitterExceptionWithMessage() {
  twitterException = new TwitterException("Test Message");
  assertEquals("Test Message", twitterException.getMessage());
  assertNull(twitterException.getCause());
  assertNull(twitterException.getStatusCode());
  assertNull(twitterException.getRateLimitStatus());
  assertNull(twitterException.getFeatureSpecificRateLimitStatus());
  assertFalse(twitterException.isCausedByNetworkIssue());
  assertFalse(twitterException.exceededRateLimitation());
  assertFalse(twitterException.resourceNotFound());
  assertNull(twitterException.getExceptionCode());
 }

 @Test
 public void testTwitterExceptionWithCause() {
  Throwable cause = new IOException();
  twitterException = new TwitterException("Test Message", cause);
  assertEquals("Test Message", twitterException.getMessage());
  assertEquals(cause, twitterException.getCause());
  assertNull(twitterException.getStatusCode());
  assertNull(twitterException.getRateLimitStatus());
  assertNull(twitterException.getFeatureSpecificRateLimitStatus());
  assertFalse(twitterException.isCausedByNetworkIssue());
  assertFalse(twitterException.exceededRateLimitation());
  assertFalse(twitterException.resourceNotFound());
  assertNull(twitterException.getExceptionCode());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
  httpResponse = mock(HttpResponse.class);
  statusLine = mock(StatusLine.class);
  when(httpResponse.getStatusLine()).thenReturn(statusLine);
  when(statusLine.getStatusCode()).thenReturn(200);
  twitterException = new TwitterException("Test Message", httpResponse);
  assertNull(twitterException.getCause());
  assertNull(twitterException.getStatusCode());
  assertNull(twitterException.getRateLimitStatus());
  assertNull(twitterException.getFeatureSpecificRateLimitStatus());
  assertFalse(twitterException.isCausedByNetworkIssue());
  assertFalse(twitterException.exceededRateLimitation());
  assertFalse(twitterException.resourceNotFound());
  assertNull(twitterException.getExceptionCode());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
  httpResponse = mock(HttpResponse.class);
  statusLine = mock(StatusLine.class);
  when(httpResponse.getStatusLine()).thenReturn(statusLine);
  when(statusLine.getStatusCode()).thenReturn(420);
  rateLimitStatus = mock(RateLimitStatus.class);
  featureSpecificRateLimitStatus = mock(RateLimitStatus.class);
  when(RateLimitStatusJSONImpl.createFromResponseHeader(statusLine)).thenReturn(rateLimitStatus);
  when(RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(statusLine)).thenReturn(featureSpecificRateLimitStatus);
  twitterException = new TwitterException("Test Message", httpResponse);
  assertEquals(420, twitterException.getStatusCode());
  assertEquals(rateLimitStatus, twitterException.getRateLimitStatus());
  assertEquals(featureSpecificRateLimitStatus, twitterException.getFeatureSpecificRateLimitStatus());
  assertFalse(twitterException.isCausedByNetworkIssue());
  assertTrue(twitterException.exceededRateLimitation());
  assertFalse(twitterException.resourceNotFound());
  assertNull(twitterException.getExceptionCode());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseStatusCodeAndRetryAfter() {
  httpResponse = mock(HttpResponse.class);
  statusLine = mock(StatusLine.class);
  when(httpResponse.getStatusLine()).thenReturn(statusLine);
  when(statusLine.getStatusCode()).thenReturn(420);
  when(statusLine.getResponseHeader("Retry-After")).thenReturn("120");
  twitterException = new TwitterException("Test Message", httpResponse);
  assertEquals(420, twitterException.getStatusCode());
  assertEquals(120, twitterException.getRetryAfter());
  assertNull(twitterException.getRateLimitStatus());
  assertNull(twitterException.getFeatureSpecificRateLimitStatus());
  assertFalse(twitterException.isCausedByNetworkIssue());
  assertTrue(twitterException.exceededRateLimitation());
  assertFalse(twitterException.resourceNotFound());
  assertNull(twitterException.getExceptionCode());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseStatusCodeAndNested() {
  Throwable cause = new IOException();
  TwitterException nestedException = new TwitterException("Test Nested Message", cause);
  twitterException = new TwitterException("Test Message", nestedException);
  assertEquals("Test Message", twitterException.getMessage());
  assertEquals(nestedException, twitterException.getCause());
  assertNull(twitterException.getStatusCode());
  assertNull(twitterException.getRateLimitStatus());
  assertNull(twitterException.getFeatureSpecificRateLimitStatus());
  assertTrue(twitterException.isCausedByNetworkIssue());
  assertFalse(twitterException.exceededRateLimitation());
  assertFalse(twitterException.resourceNotFound());
  assertNull(twitterException.getExceptionCode());
 }

 @Test
 public void testEquals() {
  TwitterException twitterException1 = new TwitterException(1, "Test Message1", new IOException());
  TwitterException twitterException2 = new TwitterException(1, "Test Message1", new IOException());
  TwitterException twitterException3 = new TwitterException(2, "Test Message2", new IOException());
  assertEquals(twitterException1, twitterException1);
  assertEquals(twitterException1, twitterException2);
  assertNotEquals(twitterException1, twitterException3);
 }

 @Test
 public void testHashCode() {
  TwitterException twitterException1 = new TwitterException(1, "Test Message1", new IOException());
  TwitterException twitterException2 = new TwitterException(1, "Test Message1", new IOException());
  TwitterException twitterException3 = new TwitterException(2, "Test Message2", new IOException());
  assertEquals(hashCode(twitterException1), hashCode(twitterException1));
  assertEquals(hashCode(twitterException1), hashCode(twitterException2));
  assertNotEquals(hashCode(twitterException1), hashCode(twitterException3));
 }

 private int hashCode(TwitterException exception) {
  return exception.getStatusCode() * 31 + exception.getRetryAfter() * 31 + (exception.getRateLimitStatus() != null ? exception.getRateLimitStatus().hashCode() : 0);
 }

}