import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusJSONImpl;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test Message");
 assertEquals("Test Message", exception.getMessage());
 assertNull(exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 assertNull(exception.getExceptionCode());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 TwitterException causeException = mock(TwitterException.class);
 when(causeException.getMessage()).thenReturn("Cause Message");
 TwitterException exception = new TwitterException("Test Message", causeException);
 assertEquals("Test Message: Cause Message", exception.getMessage());
 assertSame(causeException, exception.getCause());
 assertNull(exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 assertNull(exception.getExceptionCode());
 }

 @Test
 public void testTwitterExceptionWithMessageAndStatusCode() {
 TwitterException exception = new TwitterException("Test Message", 404);
 assertEquals("Test Message", exception.getMessage());
 assertEquals(404, exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 assertNull(exception.getExceptionCode());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 TwitterException causeException = mock(TwitterException.class);
 when(causeException.getMessage()).thenReturn("Cause Message");
 TwitterException exception = new TwitterException("Test Message", causeException, 404);
 assertEquals("Test Message: Cause Message", exception.getMessage());
 assertSame(causeException, exception.getCause());
 assertEquals(404, exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertFalse(exception.isCausedByNetworkIssue());
 assertFalse(exception.exceededRateLimitation());
 assertFalse(exception.resourceNotFound());
 assertNull(exception.getExceptionCode());
 }

 @Test
 public void testGetStatusCode() {
 HttpResponse response = mock(HttpResponse.class);
 when(response.getStatusCode()).thenReturn(420);
 TwitterException exception = new TwitterException("Test Message", response);
 assertEquals(420, exception.getStatusCode());
 }

 @Test
 public void testExceededRateLimitation() {
 HttpResponse response = mock(HttpResponse.class);
 when(response.getStatusCode()).thenReturn(400);
 TwitterException exception = new TwitterException("Test Message", response);
 assertFalse(exception.exceededRateLimitation());

 HttpResponse rateLimitedResponse = mock(HttpResponse.class);
 when(rateLimitedResponse.getStatusCode()).thenReturn(420);
 TwitterException rateLimitedException = new TwitterException("Test Message", rateLimitedResponse);
 assertTrue(rateLimitedException.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 HttpResponse response = mock(HttpResponse.class);
 when(response.getStatusCode()).thenReturn(404);
 TwitterException exception = new TwitterException("Test Message", response);
 assertTrue(exception.resourceNotFound());
 }

 @Test
 public void testGetRateLimitStatus() {
 HttpResponse response = mock(HttpResponse.class);
 RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
 when(rateLimitStatus.getRateLimitStatus()).thenReturn("test");
 when(response.getResponseHeader("X-Rate-Limit-Limit")).thenReturn("test");
 when(response.getResponseHeader("X-Rate-Limit-Remaining")).thenReturn("test");
 when(response.getResponseHeader("X-Rate-Limit-Reset")).thenReturn("test");
 TwitterException exception = new TwitterException("Test Message", response);
 assertEquals(rateLimitStatus, exception.getRateLimitStatus());
 }

 @Test
 public void testGetFeatureSpecificRateLimitStatus() {
 HttpResponse response = mock(HttpResponse.class);
 RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
 RateLimitStatus featureSpecificRateLimitStatus = mock(RateLimitStatus.class);
 when(rateLimitStatus.getRateLimitStatus()).thenReturn("test");
 when(rateLimitStatus.getFeatureSpecificRateLimitStatus()).thenReturn(featureSpecificRateLimitStatus);
 when(response.getResponseHeader("X-Rate-Limit-Limit-Bearer-Application")).thenReturn("test");
 when(response.getResponseHeader("X-Rate-Limit-Remaining-Bearer-Application")).thenReturn("test");
 when(response.getResponseHeader("X-Rate-Limit-Reset-Bearer-Application")).thenReturn("test");
 when(featureSpecificRateLimitStatus.getRateLimitStatus()).thenReturn("test");
 TwitterException exception = new TwitterException("Test Message", response);
 assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testGetRetryAfter() {
 HttpResponse response = mock(HttpResponse.class);
 when(response.getStatusCode()).thenReturn(420);
 when(response.getResponseHeader("Retry-After")).thenReturn("123");
 TwitterException exception = new TwitterException("Test Message", response);
 assertEquals(123, exception.getRetryAfter());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeNot420() {
 HttpResponse response = mock(HttpResponse.class);
 when(response.getStatusCode()).thenReturn(404);
 TwitterException exception = new TwitterException("Test Message", response);
 exception.getRetryAfter();
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 HttpResponse response = mock(HttpResponse.class);
 IOException exception = mock(IOException.class);
 when(response.getStatusCode()).thenReturn(500);
 when(response.getCause()).thenReturn(exception);
 TwitterException twitterException = new TwitterException("Test Message", response);
 assertTrue(twitterException.isCausedByNetworkIssue());
 }

 @Test
 public void testEquals() {
 TwitterException exception1 = new TwitterException("Test Message 1");
 TwitterException exception2 = new TwitterException("Test Message 2");
 TwitterException exception3 = new TwitterException("Test Message 1");
 assertTrue(exception1.equals(exception1));
 assertTrue(exception1.equals(exception3));
 assertFalse(exception1.equals(exception2));
 }

 @Test
 public void testHashCode() {
 TwitterException exception1 = new TwitterException("Test Message 1", 404, null);
 TwitterException exception2 = new TwitterException("Test Message 2", 404, null);
 TwitterException exception3 = new TwitterException("Test Message 1", 404, null);
 assertEquals(exception1.hashCode(), exception3.hashCode());
 assertNotSame(exception1.hashCode(), exception2.hashCode());
 }
}