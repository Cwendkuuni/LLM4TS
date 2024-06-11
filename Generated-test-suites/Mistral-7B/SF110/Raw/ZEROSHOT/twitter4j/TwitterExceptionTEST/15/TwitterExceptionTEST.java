package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

 private HttpResponse httpResponse;

 @Before
 public void setUp() {
 httpResponse = mock(HttpResponse.class);
 }

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test Exception");
 assertEquals("Test Exception", exception.getMessage());
 assertNull(exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertNull(exception.getRetryAfter());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 TwitterException exception = new TwitterException(new FileNotFoundException("Test File Not Found"));
 assertEquals("Test File Not Found", exception.getMessage());
 assertSame(exception.getCause(), new FileNotFoundException("Test File Not Found"));
 assertNull(exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertNull(exception.getRetryAfter());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 when(httpResponse.getStatusCode()).thenReturn(420);
 TwitterException exception = new TwitterException("Test Exception", httpResponse);
 assertEquals("Test Exception: 420: The number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
 assertEquals(420, exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertNull(exception.getRetryAfter());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
 when(httpResponse.getStatusCode()).thenReturn(420);
 when(httpResponse.getResponseHeader("Retry-After")).thenReturn("1234");
 TwitterException exception = new TwitterException("Test Exception", new FileNotFoundException("Test File Not Found"), httpResponse);
 assertEquals("Test Exception: 420: The number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
 assertEquals(420, exception.getStatusCode());
 assertEquals(1234, exception.getRetryAfter());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseStatusCodeAndRetryAfter() {
 TwitterException exception = new TwitterException("Test Exception", new FileNotFoundException("Test File Not Found"), 420, 1234);
 assertEquals("Test Exception: 420: The number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
 assertEquals(420, exception.getStatusCode());
 assertEquals(1234, exception.getRetryAfter());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testGetRateLimitStatus() {
 when(httpResponse.getStatusCode()).thenReturn(420);
 when(httpResponse.getResponseHeader("X-Rate-Limit-Limit")).thenReturn("1000");
 when(httpResponse.getResponseHeader("X-Rate-Limit-Remaining")).thenReturn("500");
 when(httpResponse.getResponseHeader("X-Rate-Limit-Reset")).thenReturn("1234567890");
 RateLimitStatus expectedRateLimitStatus = new RateLimitStatus(1000, 500, 1234567890L);
 TwitterException exception = new TwitterException("Test Exception", httpResponse);
 RateLimitStatus actualRateLimitStatus = exception.getRateLimitStatus();
 assertSame(expectedRateLimitStatus, actualRateLimitStatus);
 }

 @Test
 public void testGetFeatureSpecificRateLimitStatus() {
 when(httpResponse.getStatusCode()).thenReturn(420);
 when(httpResponse.getResponseHeader("X-Rate-Limit-Reset-After")).thenReturn("1234");
 when(httpResponse.getResponseHeader("X-Rate-Limit-Remaining-Hourly")).thenReturn("100");
 when(httpResponse.getResponseHeader("X-Rate-Limit-Limit-Hourly")).thenReturn("1500");
 when(httpResponse.getResponseHeader("X-Rate-Limit-Reset-Seconds")).thenReturn("12345");
 RateLimitStatus expectedRateLimitStatus = new RateLimitStatus(1500, 100, 12345L, new RateLimitStatus[]{new RateLimitStatus(100, 1500)});
 TwitterException exception = new TwitterException("Test Exception", httpResponse);
 RateLimitStatus actualRateLimitStatus = exception.getFeatureSpecificRateLimitStatus();
 assertSame(expectedRateLimitStatus, actualRateLimitStatus);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeIsNot420() {
 TwitterException exception = new TwitterException("Test Exception", httpResponse);
 exception.getRetryAfter();
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 TwitterException exception = new TwitterException("Test Exception", new IOException("Test IOException"));
 assertTrue(exception.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception = new TwitterException("Test Exception", httpResponse);
 assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Test Exception", httpResponse);
 when(httpResponse.getStatusCode()).thenReturn(404);
 assertTrue(exception.resourceNotFound());
 }
}