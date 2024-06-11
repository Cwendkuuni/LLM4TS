import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import twitter4j.HttpResponse;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusJSONImpl;
import twitter4j.TwitterException;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionEmptyMessage() {
 TwitterException twitterException = new TwitterException("Message");
 assertEquals("Message", twitterException.getMessage());
 assertEquals(-1, twitterException.getStatusCode());
 assertNull(twitterException.getRateLimitStatus());
 assertNull(twitterException.getFeatureSpecificRateLimitStatus());
 assertNull(twitterException.getRetryAfter());
 assertFalse(twitterException.isCausedByNetworkIssue());
 assertFalse(twitterException.exceededRateLimitation());
 assertFalse(twitterException.resourceNotFound());
 assertNull(twitterException.getExceptionCode());
 assertFalse(twitterException.isNested());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 TwitterException causeTwitterException = new TwitterException("Message", new IOException("IOException Message"));
 TwitterException twitterException = new TwitterException("Message", causeTwitterException);
 assertEquals("Message", twitterException.getMessage());
 assertEquals(-1, twitterException.getStatusCode());
 assertNull(twitterException.getRateLimitStatus());
 assertNull(twitterException.getFeatureSpecificRateLimitStatus());
 assertNull(twitterException.getRetryAfter());
 assertFalse(twitterException.isCausedByNetworkIssue());
 assertTrue(twitterException.isNested());
 assertEquals("IOException Message", twitterException.getCause().getMessage());
 }

 @Test
 public void testTwitterExceptionWithHttpResponse() {
 HttpResponse httpResponse = mock(HttpResponse.class);
 when(httpResponse.getStatusCode()).thenReturn(400);
 TwitterException twitterException = new TwitterException("Message", httpResponse);
 assertEquals("Message", twitterException.getMessage());
 assertEquals(400, twitterException.getStatusCode());
 assertNull(twitterException.getRateLimitStatus());
 assertNull(twitterException.getFeatureSpecificRateLimitStatus());
 assertNull(twitterException.getRetryAfter());
 assertFalse(twitterException.isCausedByNetworkIssue());
 assertTrue(twitterException.exceededRateLimitation());
 }

 @Test
 public void testTwitterExceptionWithHttpResponseAndRateLimitStatus() {
 HttpResponse httpResponse = mock(HttpResponse.class);
 when(httpResponse.getStatusCode()).thenReturn(420);
 when(httpResponse.getResponseHeader("Retry-After")).thenReturn("120");
 RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
 when(rateLimitStatus.getLimit()).thenReturn(100);
 when(rateLimitStatus.getRemaining()).thenReturn(50);
 when(rateLimitStatus.getResetTime()).thenReturn(1234567890L);
 RateLimitStatusJSONImpl rateLimitStatusJSONImpl = mock(RateLimitStatusJSONImpl.class);
 when(rateLimitStatusJSONImpl.createFromResponseHeader(httpResponse)).thenReturn(rateLimitStatus);
 TwitterException twitterException = new TwitterException("Message", httpResponse);
 assertEquals("Message", twitterException.getMessage());
 assertEquals(420, twitterException.getStatusCode());
 assertEquals(rateLimitStatus, twitterException.getRateLimitStatus());
 assertEquals(120, twitterException.getRetryAfter());
 }

 @Test
 public void testTwitterExceptionWithHttpResponseAndFeatureSpecificRateLimitStatus() {
 HttpResponse httpResponse = mock(HttpResponse.class);
 when(httpResponse.getStatusCode()).thenReturn(420);
 when(httpResponse.getResponseHeader("X-Rate-Limit-Remaining-Bucket-1")).thenReturn("50");
 when(httpResponse.getResponseHeader("X-Rate-Limit-Reset-Bucket-1")).thenReturn("1234567890");
 RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
 when(rateLimitStatus.getLimit()).thenReturn(100);
 when(rateLimitStatus.getRemaining()).thenReturn(50);
 when(rateLimitStatus.getResetTime()).thenReturn(1234567890L);
 RateLimitStatusJSONImpl rateLimitStatusJSONImpl = mock(RateLimitStatusJSONImpl.class);
 when(rateLimitStatusJSONImpl.createFromResponseHeader(httpResponse)).thenReturn(rateLimitStatus);
 when(rateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(httpResponse)).thenReturn(new RateLimitStatus("Bucket-1"));
 TwitterException twitterException = new TwitterException("Message", httpResponse);
 assertEquals("Message", twitterException.getMessage());
 assertEquals(420, twitterException.getStatusCode());
 assertEquals(rateLimitStatus, twitterException.getRateLimitStatus());
 assertEquals("Bucket-1", twitterException.getFeatureSpecificRateLimitStatus().getBucket());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeIsNot420() {
 TwitterException twitterException = new TwitterException("Message", mock(HttpResponse.class));
 twitterException.setStatusCode(410);
 twitterException.getRetryAfter();
 }

 @Test
 public void testIsCausedByNetworkIssueWithIOExceptionCause() {
 TwitterException twitterException = new TwitterException("Message", new IOException("IOException Message"));
 assertTrue(twitterException.isCausedByNetworkIssue());
 }

 @Test
 public void testIsCausedByNetworkIssueWithOtherCause() {
 TwitterException twitterException = new TwitterException("Message", new Exception("Exception Message"));
 assertFalse(twitterException.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitationWithStatusCode400() {
 TwitterException twitterException = new TwitterException("Message", mock(HttpResponse.class));
 twitterException.setStatusCode(400);
 assertTrue(twitterException.exceededRateLimitation());
 }

 @Test
 public void testExceededRateLimitationWithStatusCode420() {
 TwitterException twitterException = new TwitterException("Message", mock(HttpResponse.class));
 twitterException.setStatusCode(420);
 assertTrue(twitterException.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFoundWithStatusCode404() {
 TwitterException twitterException = new TwitterException("Message", mock(HttpResponse.class));
 twitterException.setStatusCode(404);
 assertTrue(twitterException.resourceNotFound());
 }

 @Test
 public void testResourceNotFoundWithOtherStatusCode() {
 TwitterException twitterException = new TwitterException("Message", mock(HttpResponse.class));
 twitterException.setStatusCode(403);
 assertFalse(twitterException.resourceNotFound());
 }

 @Test
 public void testGetExceptionCode() {
 TwitterException twitterException = new TwitterException("Message", mock(HttpResponse.class));
 twitterException.setStatusCode(400);
 assertEquals("00-0022-000", twitterException.getExceptionCode());
 }
}