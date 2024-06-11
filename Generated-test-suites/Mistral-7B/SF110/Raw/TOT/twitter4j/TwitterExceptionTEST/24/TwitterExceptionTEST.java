import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static twitter4j.internal.http.StatusCodes.*;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.internal.HttpResponse;
import twitter4j.internal.http.StatusLine;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithCustomMessage() {
 TwitterException exception = new TwitterException("Test custom message");
 assertEquals("Test custom message", exception.getMessage());
 assertNull(exception.getCause());
 assertNull(exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertNull(exception.getRetryAfter());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 IOException cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException("Test custom message", cause);
 assertEquals("Test custom message: Test IOException", exception.getMessage());
 assertEquals(cause, exception.getCause());
 assertNull(exception.getStatusCode());
 assertNull(exception.getRateLimitStatus());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertNull(exception.getRetryAfter());
 }

 @Test
 public void testTwitterExceptionWithHttpResponseCodeAndCustomMessage() {
 HttpResponse response = new HttpResponse() {
 @Override
 public int getStatusCode() {
 return FORBIDDEN;
 }

 @Override
 public String getResponseHeader(String name) {
 if (RATE_LIMIT_REMAINING.equals(name)) {
 return "100";
 }
 return null;
 }
 };
 TwitterException exception = new TwitterException("Test custom message", response);
 assertEquals("Test custom message", exception.getMessage());
 assertEquals(FORBIDDEN, exception.getStatusCode());
 assertNotNull(exception.getRateLimitStatus());
 assertEquals(100, exception.getRateLimitStatus().getRemaining());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertNull(exception.getRetryAfter());
 }

 @Test
 public void testTwitterExceptionWithHttpResponseCodeAndCause() {
 HttpResponse response = new HttpResponse() {
 @Override
 public int getStatusCode() {
 return FORBIDDEN;
 }

 @Override
 public String getResponseHeader(String name) {
 if (RATE_LIMIT_REMAINING.equals(name)) {
 return "100";
 }
 return null;
 }
 };
 IOException cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException("Test custom message", cause, response);
 assertEquals("Test custom message: Test IOException", exception.getMessage());
 assertEquals(FORBIDDEN, exception.getStatusCode());
 assertNotNull(exception.getRateLimitStatus());
 assertEquals(100, exception.getRateLimitStatus().getRemaining());
 assertEquals(cause, exception.getCause());
 assertNull(exception.getFeatureSpecificRateLimitStatus());
 assertNull(exception.getRetryAfter());
 }

 @Test
 public void testTwitterExceptionWithHttpResponseCodeAndCauseAndRetryAfter() {
 HttpResponse response = new HttpResponse() {
 @Override
 public int getStatusCode() {
 return FORBIDDEN;
 }

 @Override
 public String getResponseHeader(String name) {
 if (RATE_LIMIT_REMAINING.equals(name)) {
 return "100";
 }
 if ("Retry-After".equals(name)) {
 return "60";
 }
 return null;
 }
 };
 IOException cause = new IOException("Test IOException");
 TwitterException exception = new TwitterException("Test custom message", cause, response);
 assertEquals("Test custom message: Test IOException", exception.getMessage());
 assertEquals(FORBIDDEN, exception.getStatusCode());
 assertNotNull(exception.getRateLimitStatus());
 assertEquals(100, exception.getRateLimitStatus().getRemaining());
 assertNotNull(exception.getRetryAfter());
 assertEquals(60, exception.getRetryAfter());
 }
}