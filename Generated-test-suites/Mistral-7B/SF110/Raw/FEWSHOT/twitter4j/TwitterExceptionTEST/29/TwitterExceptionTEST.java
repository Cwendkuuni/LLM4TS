package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test exception message");
        assertEquals("Test exception message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new Exception("Cause exception message");
        TwitterException exception = new TwitterException("Test exception message", cause);
        assertEquals("Test exception message", exception.getMessage());
        assertEquals("Cause exception message", exception.getCause().getMessage());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse httpResponse = new DefaultHttpResponse(400, "Bad Request");
        TwitterException exception = new TwitterException("Test exception message", httpResponse);
        assertEquals(Integer.valueOf(400), exception.getRetryAfter());
        assertEquals(RateLimitStatus.RATE_LIMITED, exception.getRateLimitStatus());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test exception message", new HttpResponse(404, "Not Found"));
        assertEquals(404, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test exception message", new HttpResponse(420, "Rate Limit Exceeded"));
        assertEquals(RateLimitStatus.RATE_LIMITED, exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test exception message", new HttpResponse(420, "Rate Limit Exceeded", "x-ratelimit-limit: 100, x-ratelimit-remaining: 50, x-ratelimit-reset: 123456"));
        assertEquals(50, exception.getFeatureSpecificRateLimitStatus().getRemaining());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test exception message", new IOException("Network error"));
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test exception message", new HttpResponse(420, "Rate Limit Exceeded"));
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test exception message", new HttpResponse(404, "Not Found"));
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test exception message", new HttpResponse(420, "Rate Limit Exceeded"));
        assertEquals("420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test exception message");
        exception.setNested();
        assertTrue(exception.isNested());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test exception message 1", new HttpResponse(420, "Rate Limit Exceeded"));
        TwitterException exception2 = new TwitterException("Test exception message 2", new HttpResponse(420, "Rate Limit Exceeded"));
        TwitterException exception3 = new TwitterException("Test exception message 1", new HttpResponse(404, "Not Found"));
        assertEquals(exception1, exception1);
        assertEquals(exception1, exception1);
        assertNotEquals(exception1, exception2);
        assertNotEquals(exception1, exception3);
        assertNotEquals(exception1, null);
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test exception message 1", new HttpResponse(420, "Rate Limit Exceeded"));
        TwitterException exception2 = new TwitterException("Test exception message 2", new HttpResponse(420, "Rate Limit Exceeded"));
        TwitterException exception3 = new TwitterException("Test exception message 1", new HttpResponse(404, "Not Found"));
        int hashCode1 = exception1.hashCode();
        int hashCode2 = exception2.hashCode();
        int hashCode3 = exception3.hashCode();
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test exception message", new HttpResponse(420, "Rate Limit Exceeded"));
        String result = exception.toString();
        String expected = "TwitterException{statusCode=420, retryAfter=null, rateLimitStatus=RATE_LIMITED, version=4.15.1}";
        assertEquals(expected, result);
    }
}