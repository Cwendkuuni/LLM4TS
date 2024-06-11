import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test message");
 assertEquals("Test message", exception.getMessage());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 Exception cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException(cause);
 assertEquals("Test IOException", exception.getCause().getMessage());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 String cause = "Test cause";
 HttpResponse httpResponse = new HttpResponse(420, new ByteArrayInputStream(cause.getBytes()));
 TwitterException exception = new TwitterException("Test message", httpResponse);
 assertEquals(420, exception.getStatusCode());
 assertEquals(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 String message = "Test message";
 Exception cause = new IOException("Test IOException");
 int statusCode = 420;
 TwitterException exception = new TwitterException(message, cause, statusCode);
 assertEquals(message, exception.getMessage());
 assertEquals(cause, exception.getCause());
 assertEquals(statusCode, exception.getStatusCode());
 }

 @Test
 public void testGetRateLimitStatus() {
 TwitterException exception = new TwitterException("Test message", new HttpResponse(420, new ByteArrayInputStream(new byte[0])));
 assertNull(exception.getRateLimitStatus());

 HttpResponse rateLimitResponse = new HttpRateLimitHeader("0/1000", "Normal").getResponse();
 exception = new TwitterException("Test message", rateLimitResponse);
 assertEquals(RateLimitStatus.LIMIT_NORMAL, exception.getRateLimitStatus());
 }

 @Test
 public void testGetFeatureSpecificRateLimitStatus() {
 TwitterException exception = new TwitterException("Test message", new HttpResponse(420, new ByteArrayInputSteam("{\"limit\":1,\"remaining\":1,\"reset\":1421123165}".getBytes())));
 assertEquals(new RateLimitStatus(1, 1, 1421123165), exception.getFeatureSpecificRateLimitStatus());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 TwitterException exception = new TwitterException("Test message", new IOException("Test IOException"));
 assertTrue(exception.isCausedByNetworkIssue());

 exception = new TwitterException("Test message", new Exception("Test Exception"));
 assertFalse(exception.isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 TwitterException exception1 = new TwitterException("Test message", new HttpResponse(400, new ByteArrayInputStream(new byte[0])));
 assertFalse(exception1.exceededRateLimitation());

 TwitterException exception2 = new TwitterException("Test message", new HttpResponse(420, new ByteArrayInputStream(new byte[0])));
 assertTrue(exception2.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 TwitterException exception1 = new TwitterException("Test message", new HttpResponse(401, new ByteArrayInputStream(new byte[0])));
 assertFalse(exception1.resourceNotFound());

 TwitterException exception2 = new TwitterException("Test message", new HttpResponse(404, new ByteArrayInputStream(new byte[0])));
 assertTrue(exception2.resourceNotFound());
 }
}