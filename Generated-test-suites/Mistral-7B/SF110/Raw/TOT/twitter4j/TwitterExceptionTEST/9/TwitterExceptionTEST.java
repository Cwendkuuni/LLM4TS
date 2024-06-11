import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test Message");
 assertEquals("Test Message", exception.getMessage());
 }

 @Test
 public void testTwitterExceptionWithIOException() {
 IOException cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException(cause);
 assertTrue(exception.isCausedByNetworkIssue());
 assertEquals("Test IOException", exception.getCause().getMessage());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 String msg = "Test Message";
 HttpResponse response = new HttpResponse();
 response.setStatusCode(420);
 response.setResponseHeader("Retry-After", "120");
 TwitterException exception = new TwitterException(msg, response);
 assertEquals(420, exception.getStatusCode());
 assertEquals(120, exception.getRetryAfter());
 }

 @Test
 public void testTwitterExceptionWithMessageAndException() {
 String msg = "Test Message";
 Exception cause = new Exception("Test Exception");
 TwitterException exception = new TwitterException(msg, cause);
 assertEquals(msg, exception.getMessage());
 assertEquals(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageAndExceptionAndStatusCode() {
 String msg = "Test Message";
 Exception cause = new Exception("Test Exception");
 int statusCode = 400;
 TwitterException exception = new TwitterException(msg, cause, statusCode);
 assertEquals(msg, exception.getMessage());
 assertEquals(cause, exception.getCause());
 assertEquals(statusCode, exception.getStatusCode());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception = new TwitterException("Test Message", new HttpResponse());
 exception.setStatusCode(420);
 assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception = new TwitterException("Test Message", new HttpResponse());
 exception.setStatusCode(404);
 assertTrue(exception.resourceNotFound());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 TwitterException exception = new TwitterException("Test Message", new IOException("Test IOException"));
 assertTrue(exception.isCausedByNetworkIssue());
 }
}