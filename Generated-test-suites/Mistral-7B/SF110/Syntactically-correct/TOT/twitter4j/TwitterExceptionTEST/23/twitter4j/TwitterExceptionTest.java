import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TwitterExceptionTest {

 private TwitterException exception;
 private HttpResponse response;

 @Before
 public void setup() {
 response = Mockito.mock(HttpResponse.class);
 exception = new TwitterException("Test Message");
 }

 @Test
 public void testEmptyConstructor() {
 assertEquals("Test Message", exception.getMessage());
 assertNull(exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertNull(exception.getRetryAfter());
 assertFalse(exception.isNested());
 }

 @Test
 public void testExceptionConstructor() {
 Exception cause = new Exception("Test Cause");
 TwitterException nestedException = new TwitterException("Test Nested Message", cause);
 TwitterException exception = new TwitterException("Test Message", nestedException);
 assertEquals("Test Message: Test Cause", exception.getMessage());
 assertEquals(-1, exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertTrue(exception.isNested());
 assertEquals(nestedException, exception.getCause());
 }

 @Test
 public void testHttpResponseConstructor() {
 exception = new TwitterException("Test Message", response);
 assertEquals("Test Message: " + getCause(response), exception.getMessage());
 assertEquals(response.getStatusCode(), exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertNull(exception.getRetryAfter());
 }

 @Test
 public void testHttpResponseConstructorWithRateLimitStatus() {
 Mockito.when(response.getStatusCode()).thenReturn(420);
 Mockito.when(response.getResponseHeader("Retry-After")).thenReturn("120");
 exception = new TwitterException("Test Message", response);
 assertEquals("Test Message: There was no new data to return.", exception.getMessage());
 assertEquals(420, exception.getStatusCode());
 assertEquals(new RateLimitStatus(120), exception.getRateLimitStatus());
 assertEquals(-1, exception.getRetryAfter());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testHttpResponseConstructorWithRateLimitStatusAndFeatureSpecificRateLimitStatus() {
 Mockito.when(response.getStatusCode()).thenReturn(420);
 Mockito.when(response.getResponseHeader("Retry-After")).thenReturn("120");
 Mockito.when(response.getResponseHeader("X-Rate-Limit-Remaining")).thenReturn("10");
 Mockito.when(response.getResponseHeader("X-Rate-Limit-Limit")).thenReturn("15");
 exception = new TwitterException("Test Message", response);
 assertEquals("Test Message: The number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
 assertEquals(420, exception.getStatusCode());
 assertEquals(new RateLimitStatus(120, 10, 15), exception.getRateLimitStatus());
 assertEquals(120, exception.getRetryAfter());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testGetStatusCodes() {
 assertEquals(-1, exception.getStatusCode());
 exception.setStatusCode(400);
 assertEquals(400, exception.getStatusCode());
 }

 @Test
 public void testGetRateLimitStatus() {
 RateLimitStatus rateLimitStatus = new RateLimitStatus(120, 10, 15);
 exception.setRateLimitStatus(rateLimitStatus);
 assertEquals(rateLimitStatus, exception.getRateLimitStatus());
 }

 @Test
 public void testGetFeatureSpecificRateLimitStatus() {
 RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus(120, 10, 15, new RateLimitStatus.FeatureSpecificRateLimit("search", 5));
 exception.setFeatureSpecificRateLimitStatus(featureSpecificRateLimitStatus);
 assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testGetRetryAfter() {
 exception.setStatusCode(420);
 exception.setRetryAfter(120);
 assertEquals(120, exception.getRetryAfter());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 IOException ioException = new IOException("Test IOException");
 exception = new TwitterException("Test Message", ioException);
 assertTrue(exception.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 exception = new TwitterException("Test Message", new HttpResponse(400, null));
 assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 exception = new TwitterException("Test Message", new HttpResponse(404, null));
 assertTrue(exception.resourceNotFound());
 }

 @Test
 public void testGetExceptionCode() {
 String exceptionCode = exception.getExceptionCode();
 assertEquals("twitter4j:01:400:0:", exceptionCode);
 }

 @Test
 public void testSetNested() {
 TwitterException nestedException = new TwitterException("Test Nested Message");
 exception.setNested(nestedException);
 assertTrue(exception.isNested());
 }

 private static String getCause(HttpResponse res) {
 return Mockito.invoke(TwitterException.class, "getCause", res);
 }
}