import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static twitter4j.TwitterException.FILTER;

import twitter4j.HttpResponse;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusJSONImpl;
import twitter4j.TwitterException;
import twitter4j.internal.http.StatusLine;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test Message");
 assertEquals("Test Message", exception.getMessage());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 Exception cause = new IOException();
 TwitterException exception = new TwitterException("Test Message", cause);
 assertEquals("Test Message", exception.getMessage());
 assertEquals(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse httpResponse = mock(HttpResponse.class);
 StatusLine statusLine = mock(StatusLine.class);
 when(httpResponse.getStatusLine()).thenReturn(statusLine);
 TwitterException exception = new TwitterException("Test Message", httpResponse);
 assertEquals("Test Message" + System.lineSeparator() + getCause(httpResponse), exception.getMessage());
 assertEquals(httpResponse.getStatusCode(), exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testTwitterExceptionWithMessageAndCauseAndStatusCode() {
 Exception cause = new IOException();
 TwitterException exception = new TwitterException("Test Message", cause, 400);
 assertEquals("Test Message", exception.getMessage());
 assertEquals(cause, exception.getCause());
 assertEquals(400, exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 IOException ioException = new IOException();
 TwitterException twitterException = new TwitterException("Test Message", ioException);
 assertTrue(twitterException.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException twitterException = new TwitterException("Test Message", mockHttpResponse(400));
 assertTrue(twitterException.exceededRateLimitation());
 }

 @Test
 public void testExceededRateLimitationWithRateLimitStatus() {
 TwitterException twitterException = new TwitterException("Test Message", mockHttpResponse(420));
 assertNotNull(twitterException.getRateLimitStatus());
 }

 @Test
 public void testExceededRateLimitationWithFeatureSpecificRateLimitStatus() {
 TwitterException twitterException = new TwitterException("Test Message", mockHttpResponse(420, FILTER[0]));
 assertNotNull(twitterException.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException twitterException = new TwitterException("Test Message", mockHttpResponse(404));
 assertTrue(twitterException.resourceNotFound());
 }

 @Test
 public void testGetExceptionCode() {
 TwitterException twitterException = new TwitterException("Test Message", mockHttpResponse(420, FILTER[0]));
 assertEquals("0110101101101111011011000110011001111101", twitterException.getExceptionCode());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeIsNot420() {
 TwitterException twitterException = new TwitterException("Test Message", mockHttpResponse(400));
 twitterException.getRetryAfter();
 }

 private HttpResponse mockHttpResponse(int statusCode) {
 return mockHttpResponse(statusCode, FILTER);
 }

 private HttpResponse mockHttpResponse(int statusCode, String[] filters) {
 HttpResponse httpResponse = mock(HttpResponse.class);
 StatusLine statusLine = mock(StatusLine.class);
 when(statusLine.getStatusCode()).thenReturn(statusCode);
 when(httpResponse.getStatusLine()).thenReturn(statusLine);
 when(httpResponse.getResponseHeaderNames()).thenReturn(new String[0]);
 when(httpResponse.getAllHeaders()).thenReturn(new String[0][0]);
 RateLimitStatusJSONImpl rateLimitStatusJSON = new RateLimitStatusJSONImpl();
 RateLimitStatus rateLimitStatus = rateLimitStatusJSON.createFromResponseHeader(statusLine);
 when(rateLimitStatusJSON.createFromResponseHeader(statusLine)).thenReturn(rateLimitStatus);
 when(rateLimitStatusJSON.createFeatureSpecificRateLimitStatusFromResponseHeader(statusLine)).thenReturn(null);
 return httpResponse;
}
}