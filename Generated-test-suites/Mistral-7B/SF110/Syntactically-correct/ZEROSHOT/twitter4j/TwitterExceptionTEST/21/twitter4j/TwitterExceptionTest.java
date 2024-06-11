import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

 private HttpResponse createHttpResponse(int statusCode) {
 return new HttpResponse() {
 @Override
 public int getStatusCode() {
 return statusCode;
 }
 };
 }

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Invalid request");
 assertEquals("Invalid request", exception.getMessage());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException cause = new IOException("Network error");
 TwitterException exception = new TwitterException("Rate limit exceeded", cause);
 assertEquals("Rate limit exceeded", exception.getMessage());
 assertEquals("Network error", exception.getCause().getMessage());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse httpResponse = createHttpResponse(420);
 TwitterException exception = new TwitterException("Rate limit exceeded", httpResponse);
 assertEquals("Rate limit exceeded" + System.lineSeparator() + "420:" + "The number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
 assertEquals(420, exception.getStatusCode());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 IOException cause = new IOException("Network error");
 TwitterException exception = new TwitterException("Rate limit exceeded", cause, 420);
 assertEquals("Rate limit exceeded", exception.getMessage());
 assertEquals("Network error", exception.getCause().getMessage());
 assertEquals(420, exception.getStatusCode());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 IOException cause = new IOException("Network error");
 TwitterException exception = new TwitterException("Rate limit exceeded", cause);
 assertTrue(exception.isCausedByNetworkIssue());
}

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception = new TwitterException("Invalid request", new HttpResponse() {
 @Override
 public int getStatusCode() {
 return 400;
 }
 });
 assertTrue(exception.exceededRateLimitation());

 TwitterException exception1 = new TwitterException("Rate limit exceeded", new HttpResponse() {
 @Override
 public int getStatusCode() {
 return 420;
 }
 });
 assertTrue(exception1.exceededRateLimitation());
}

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Invalid request", new HttpResponse() {
 @Override
 public int getStatusCode() {
 return 404;
 }
 });
 assertTrue(exception.resourceNotFound());
}

 @Test
 public void testGetRetryAfter() {
 HttpResponse httpResponse = createHttpResponse(420);
 String retryAfterStr = "120";
 httpResponse.setResponseHeader("Retry-After", retryAfterStr);
 TwitterException exception = new TwitterException("Rate limit exceeded", httpResponse);
 assertEquals(120, exception.getRetryAfter());
}

 @Test
 public void testEquals() {
 TwitterException exception1 = new TwitterException("Rate limit exceeded", new HttpResponse() {
 @Override
 public int getStatusCode() {
 return 420;
 }
 });
 TwitterException exception2 = new TwitterException("Rate limit exceeded", new HttpResponse() {
 @Override
 public int getStatusCode() {
 return 420;
 }
 });
 assertTrue(exception1.equals(exception2));

 TwitterException exception3 = new TwitterException("Rate limit exceeded", new HttpResponse() {
 @Override
 public int getStatusCode() {
 return 400;
 }
 });
 assertFalse(exception1.equals(exception3));
}

}