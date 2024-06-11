import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionMessage() {
 TwitterException exception = new TwitterException("Test Exception Message");
 assertEquals("Test Exception Message", exception.getMessage());
 }

 @Test
 public void testTwitterExceptionCause() {
 Exception cause = new IOException("Test IOException Message");
 TwitterException exception = new TwitterException("Test Exception Message", cause);
 assertEquals("Test Exception Message", exception.getMessage());
 assertEquals(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionHttpResponse() {
 HttpResponse response = mock(HttpResponse.class);
 when(response.getStatusCode()).thenReturn(420);
 when(response.getResponseHeader("Retry-After")).thenReturn("120");
 TwitterException exception = new TwitterException("Test Exception Message", response);
 assertEquals(420, exception.getStatusCode());
 assertEquals(120, exception.getRetryAfter());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testTwitterExceptionHttpResponseWithFeatureSpecificRateLimitStatus() {
 HttpResponse response = mock(HttpResponse.class);
 when(response.getStatusCode()).thenReturn(420);
 when(response.getResponseHeader("Retry-After")).thenReturn("120");
 when(response.getResponseHeader("X-Rate-Limit-Reset")).thenReturn("Mon, 14 Mar 2022 12:34:56 GMT");
 TwitterException exception = new TwitterException("Test Exception Message", response);
 assertEquals(420, exception.getStatusCode());
 assertEquals(120, exception.getRetryAfter());
 RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus();
 featureSpecificRateLimitStatus.setReset(1647422896000L);
 featureSpecificRateLimitStatus.setLimit(60);
 featureSpecificRateLimitStatus.setRemaining(51);
 when(response.getResponseHeader("X-Rate-Limit-Limit-Remaining")).thenReturn("51");
 when(response.getResponseHeader("X-Rate-Limit-Limit-Reset")).thenReturn("Mon, 14 Mar 2022 12:34:56 GMT");
 when(response.getResponseHeader("X-Rate-Limit-Reset-After")).thenReturn("Mon, 14 Mar 2022 12:46:56 GMT");
 exception = new TwitterException("Test Exception Message", response);
 assertEquals(420, exception.getStatusCode());
 assertEquals(120, exception.getRetryAfter());
 assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 TwitterException exception = new TwitterException("Test Exception Message");
 assertFalse(exception.isCausedByNetworkIssue());
 IOException cause = new IOException("Test IOException Message");
 TwitterException exceptionWithCause = new TwitterException("Test Exception Message", cause);
 assertTrue(exceptionWithCause.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception = new TwitterException("Test Exception Message");
 assertFalse(exception.exceededRateLimitation());
 exception = new TwitterException("Test Exception Message", new HttpResponse() {{
 setStatusCode(400);
 setRateLimitStatus(new RateLimitStatus());
 }});
 assertFalse(exception.exceededRateLimitation());
 exception = new TwitterException("Test Exception Message", new HttpResponse() {{
 setStatusCode(420);
 setRateLimitStatus(null);
 }});
 assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Test Exception Message");
 assertFalse(exception.resourceNotFound());
 exception = new TwitterException("Test Exception Message", new HttpResponse() {{
 setStatusCode(404);
 }});
 assertTrue(exception.resourceNotFound());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeIsNot420() {
 TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse() {{
 setStatusCode(400);
 }});
 exception.getRetryAfter();
 }
}