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
 TwitterException exception = new TwitterException("Test Error Message");
 assertEquals("Test Error Message", exception.getMessage());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException("Test Error Message", cause);
 assertEquals("Test Error Message", exception.getMessage());
 assertEquals(cause, exception.getCause());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse response = new HttpResponse(400, null, null);
 TwitterException exception = new TwitterException("Test Error Message", response);
 assertEquals("Test Error Message" + "\n" + getCause(response), exception.getMessage());
 assertEquals(response.getStatusCode(), exception.getStatusCode());
 }

 @Test
 public void testTwitterExceptionWithMessageCauseAndStatusCode() {
 IOException cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException("Test Error Message", cause, 400);
 assertEquals("Test Error Message", exception.getMessage());
 assertEquals(cause, exception.getCause());
 assertEquals(400, exception.getStatusCode());
 }

 @Test
 public void testGetRetryAfter() {
 HttpResponse response = new HttpResponse(420, "Retry-After: 120", null);
 TwitterException exception = new TwitterException("Test Error Message", response);
 assertEquals(120, exception.getRetryAfter());
 }

 @Test
 public void testIsCausedByNetworkIssue() {
 IOException exception = new IOException("Test IOException");
 assertTrue(new TwitterException("Test Error Message", exception).isCausedByNetworkIssue());
 }

 @Test
 public void testExceededRateLimitation() {
 HttpResponse response = new HttpResponse(400, null, null);
 TwitterException exception = new TwitterException("Test Error Message", response);
 assertTrue(exception.exceededRateLimitation());
 }

 @Test
 public void testResourceNotFound() {
 HttpResponse response = new HttpResponse(404, null, null);
 TwitterException exception = new TwitterException("Test Error Message", response);
 assertTrue(exception.resourceNotFound());
 }

 @Test
 public void testGetExceptionCode() {
 IOException cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException("Test Error Message", cause);
 String exceptionCode = exception.getExceptionCode();
 assertTrue(exceptionCode.startsWith("twitter4j"));
 }

 @Test
 public void testEquals() {
 TwitterException exception1 = new TwitterException("Test Error Message 1");
 TwitterException exception2 = new TwitterException("Test Error Message 2");
 assertFalse(exception1.equals(exception2));

 TwitterException exception3 = new TwitterException("Test Error Message 1");
 TwitterException exception4 = new TwitterException("Test Error Message 1");
 assertTrue(exception3.equals(exception4));

 TwitterException exception5 = new TwitterException("Test Error Message 1");
 exception5.setNested();
 assertFalse(exception3.equals(exception5));

 HttpResponse response = new HttpResponse(420, "Retry-After: 120", null);
 TwitterException exception6 = new TwitterException("Test Error Message", response);
 TwitterException exception7 = new TwitterException("Test Error Message", response);
 assertTrue(exception6.equals(exception7));

 TwitterException exception8 = new TwitterException("Test Error Message", response);
 exception8.setNested();
 assertFalse(exception6.equals(exception8));
 }

 @Test
 public void testHashCode() {
 TwitterException exception1 = new TwitterException("Test Error Message 1");
 TwitterException exception2 = new TwitterException("Test Error Message 2");
 int hashCode1 = exception1.hashCode();
 int hashCode2 = exception2.hashCode();
 assertNotEquals(hashCode1, hashCode2);

 TwitterException exception3 = new TwitterException("Test Error Message 1");
 TwitterException exception4 = new TwitterException("Test Error Message 1");
 assertEquals(hashCode1, exception3.hashCode());
 assertEquals(hashCode1, exception4.hashCode());
 }

 private static InputStream getInputStream(String content) {
 return new ByteArrayInputStream(content.getBytes());
 }

 private static HttpResponse getResponseWithStatusCodeAndContent(int statusCode, String content) throws IOException {
 HttpResponse response = new HttpResponse(statusCode, null, null);
 response.setEntity(getInputStream(content));
 return response;
 }

 private static String getCause(HttpResponse res) {
 return res.getResponseHeader("X-Twitter-Error-Message");
 }
}