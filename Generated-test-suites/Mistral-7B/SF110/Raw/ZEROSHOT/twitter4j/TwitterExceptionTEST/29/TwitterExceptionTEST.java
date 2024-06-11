package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

 private HttpResponse createMockHttpResponse(int statusCode, String responseBody) {
 // Create a mock HttpResponse object
 HttpResponse mockHttpResponse = new HttpResponse() {
 @Override
 public int getStatusCode() {
 return statusCode;
 }

 @Override
 public String getResponseBody() {
 return responseBody;
 }
 };
 return mockHttpResponse;
 }

 @Test
 public void testTwitterExceptionWithMessage() {
 String message = "Test exception message";
 TwitterException exception = new TwitterException(message);
 assertEquals(message, exception.getMessage());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 String causeMessage = "Test cause message";
 Exception cause = new Exception(causeMessage);
 TwitterException exception = new TwitterException("Test exception message", cause);
 assertEquals(causeMessage, cause.getMessage());
 assertEquals(exception.getCause(), cause);
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 String message = "Test exception message";
 HttpResponse httpResponse = createMockHttpResponse(400, "");
 TwitterException exception = new TwitterException(message, httpResponse);
 assertEquals(message, exception.getMessage());
 assertEquals(httpResponse.getStatusCode(), exception.getStatusCode());
 }

 @Test
 public void testTwitterExceptionWithCauseAndHttpResponse() {
 String causeMessage = "Test cause message";
 Exception cause = new Exception(causeMessage);
 HttpResponse httpResponse = createMockHttpResponse(400, "");
 TwitterException exception = new TwitterException(null, cause, 400);
 assertEquals(causeMessage, cause.getMessage());
 assertEquals(httpResponse.getStatusCode(), exception.getStatusCode());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
 String message = "Test exception message";
 String causeMessage = "Test cause message";
 Exception cause = new Exception(causeMessage);
 HttpResponse httpResponse = createMockHttpResponse(400, "");
 TwitterException exception = new TwitterException(message, cause, httpResponse);
 assertEquals(message, exception.getMessage());
 assertEquals(causeMessage, cause.getMessage());
 assertEquals(httpResponse.getStatusCode(), exception.getStatusCode());
 }

 @Test
 public void testGetStatusCode() {
 TwitterException exception = new TwitterException("Test exception message");
 exception.setStatusCode(400);
 assertEquals(400, exception.getStatusCode());
 }

 @Test
 public void testGetRateLimitStatus() {
 HttpResponse httpResponse = createMockHttpResponse(420, "{\"rate_limit_status\":{\"limit\":15,\"remaining\":12,\"reset\":1495637537}}");
 TwitterException exception = new TwitterException("Test exception message", httpResponse);
 RateLimitStatus rateLimitStatus = exception.getRateLimitStatus();
 assertEquals(15, rateLimitStatus.getLimit());
 assertEquals(12, rateLimitStatus.getRemaining());
 assertEquals(1495637537, rateLimitStatus.getReset());
 }

 @Test
 public void testGetFeatureSpecificRateLimitStatus() {
 HttpResponse httpResponse = createMockHttpResponse(420, "{\"rate_limit_status\":{\"limit\":15,\"remaining\":12,\"reset\":1495637537,\"local\":{\"search\":{\"limit\":5,\"remaining\":3}}}}");
 TwitterException exception = new TwitterException("Test exception message", httpResponse);
 RateLimitStatus featureSpecificRateLimitStatus = exception.getFeatureSpecificRateLimitStatus();
 assertEquals(5, featureSpecificRateLimitStatus.getLimit());
 assertEquals(3, featureSpecificRateLimitStatus.getRemaining());
 }

 @Test
 public void testGetRetryAfter() {
 HttpResponse httpResponse = createMockHttpResponse(420, "Retry-After: 60");
 TwitterException exception = new TwitterException("Test exception message", httpResponse);
 assertEquals(60, exception.getRetryAfter());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 IOException mockIOException = new IOException("Test IOException message");
 TwitterException exception = new TwitterException("Test exception message", mockIOException);
 assertTrue(exception.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception = new TwitterException("Test exception message", createMockHttpResponse(400, ""));
 assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Test exception message", createMockHttpResponse(404, ""));
 assertTrue(exception.resourceNotFound());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRetryAfterWhenStatusCodeIsNot420() {
 TwitterException exception = new TwitterException("Test exception message", createMockHttpResponse(400, ""));
 assertFalse(exception.exceededRateLimitation());
 assertTrue(exception.isCausedByNetworkIssue());
 try {
 exception.getRetryAfter();
 } catch (IllegalStateException e) {
 // Expected
 }
 }

}