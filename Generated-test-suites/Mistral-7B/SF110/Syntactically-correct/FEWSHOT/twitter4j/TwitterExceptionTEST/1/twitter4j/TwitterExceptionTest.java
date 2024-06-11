package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test message");;
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        IOException cause = new IOException("Test cause");
        TwitterException exception = new TwitterException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertSame(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        int statusCode = 400;
        String retryAfterStr = "120";
        RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 30);
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus(5, 120);

        InputStream inputStream = new ByteArrayInputStream(("HTTP/1.1 " + statusCode + " OK\r\n" +
                "Retry-After: " + retryAfterStr + "\r\n" +
                "X-Rate-Limit-Limit: " + rateLimitStatus.getRemainingHits() + "\r\n" +
                "X-Rate-Limit-Remaining: " + rateLimitStatus.getRemainingHits() + "\r\n" +
                "X-Rate-Limit-Reset: " + (System.currentTimeMillis() + Long.parseLong(retryAfterStr) * 1000) + "\r\n" +
                "Content-Type: application/json; charset=UTF-8\r\n" +
                "Transfer-Encoding: chunked\r\n" +
                "\r\n" +
                "{\"rate_limit_status\":{\"remaining_hits\": " + rateLimitStatus.getRemainingHits() + ",\"reset_time\": " + rateLimitStatus.getResetTime() + ",\"limit_reset\": " + rateLimitStatus.getLimitReset() + "}}").getBytes());

        TwitterException exception = new TwitterException("Test message", null, statusCode, inputStream);
        assertEquals("Test message" + "\n" + getCause(inputStream), exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertSame(rateLimitStatus, exception.getRateLimitStatus());
        assertSame(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
        assertEquals(Integer.valueOf(120), exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithCauseAndHttpResponse() {
        int statusCode = 400;
        String retryAfterStr = "120";
        RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 30);
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus(5, 120);

        InputStream inputStream = new ByteArrayInputStream(("HTTP/1.1 " + statusCode + " OK\r\n" +
                "Retry-After: " + retryAfterStr + "\r\n" +
                "X-Rate-Limit-Limit: " + rateLimitStatus.getRemainingHits() + "\r\n" +
                "X-Rate-Limit-Remaining: " + rateLimitStatus.getRemainingHits() + "\r\n" +
                "X-Rate-Limit-Reset: " + (System.currentTimeMillis() + Long.parseLong(retryAfterStr) * 1000) + "\r\n" +
                "Content-Type: application/json; charset=UTF-8\r\n" +
                "Transfer-Encoding: chunked\r\n" +
                "\r\n" +
                "{\"rate_limit_status\":{\"remaining_hits\": " + rateLimitStatus.getRemainingHits() + ",\"reset_time\": " + rateLimitStatus.getResetTime() + ",\"limit_reset\": " + rateLimitStatus.getLimitReset() + "}}").getBytes());

        IOException cause = new IOException("Test cause");
        TwitterException exception = new TwitterException("Test message", cause, statusCode, inputStream);
        assertEquals("Test message" + "\n" + getCause(inputStream), exception.getMessage());
        assertSame(cause, exception.getCause());
        assertEquals(statusCode, exception.getStatusCode());
        assertSame(rateLimitStatus, exception.getRateLimitStatus());
        assertSame(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
        assertEquals(Integer.valueOf(120), exception.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("Test cause");
        TwitterException exception = new TwitterException("Test message", cause);
        assertTrue(exception.isCausedByNetworkIssue());

        String causeString = new String("Test cause");
        TwitterException exceptionString = new TwitterException("Test message", new Exception(causeString));
        assertFalse(exceptionString.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        int statusCode = 400;
        RateLimitStatus rateLimitStatus = new RateLimitStatus(0, 15);

        InputStream inputStream = new ByteArrayInputStream(("HTTP/1.1 " + statusCode + " OK\r\n" +
                "X-Rate-Limit-Limit: " + rateLimitStatus.getRemainingHits() + "\r\n" +
                "X-Rate-Limit-Remaining: " + rateLimitStatus.getRemainingHits() + "\r\n" +
                "X-Rate-Limit-Reset: " + (System.currentTimeMillis() + rateLimitStatus.getResetTime() * 1000) + "\r\n" +
                "Content-Type: application/json; charset=UTF-8\r\n" +
                "Transfer-Encoding: chunked\r\n" +
                "\r\n" +
                "{\"rate_limit_status\":{\"remaining_hits\": " + rateLimitStatus.getRemainingHits() + ",\"reset_time\": " + rateLimitStatus.getResetTime() + ",\"limit_reset\": " + rateLimitStatus.getLimitReset() + "}}").getBytes());

        TwitterException exception = new TwitterException("Test message", null, statusCode, inputStream);
        assertTrue(exception.exceededRateLimitation());

        RateLimitStatus rateLimitStatus1 = new RateLimitStatus(15, 30);
        InputStream inputStream1 = new ByteArrayInputStream(("HTTP/1.1 " + 404 + " Not Found\r\n" +
                "Content-Type: application/json; charset=UTF-8\r\n" +
                "Transfer-Encoding: chunked\r\n" +
                "\r\n" +
                "{}").getBytes());

        TwitterException exception1 = new TwitterException("Test message", null, 404, inputStream1);
        assertFalse(exception1.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test message", null, 404);
        assertTrue(exception.resourceNotFound());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        TwitterException exception = new TwitterException("Test message", null, 404);
        exception.getRetryAfter();
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test message 1", null, 400, null);
        TwitterException exception2 = new TwitterException("Test message 2", null, 400, null);
        TwitterException exception3 = new TwitterException("Test message 1", null, 400, null);
        TwitterException exception4 = new TwitterException("Test message 1", null, 401, null);

        Map<String, TwitterException> map = new HashMap<>();
        map.put("test1", exception1);
        map.put("test2", exception2);
        map.put("test3", exception3);

        assertTrue(exception1.equals(exception1));
        assertTrue(exception1.equals(exception3));
        assertFalse(exception1.equals(exception2));
        assertFalse(exception1.equals(null));
        assertFalse(exception1.equals("test"));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test message 1", null, 400, null);
        TwitterException exception2 = new TwitterException("Test message 2", null, 400, null);
        TwitterException exception3 = new TwitterException("Test message 1", null, 400, null);

        assertEquals(exception1.hashCode(), exception1.hashCode());
        assertEquals(exception1.hashCode(), exception3.hashCode());
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
    }

    private static String getCause(InputStream inputStream) {
        String cause = null;
        try {
            cause = new String(inputStream.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cause;
    }
}