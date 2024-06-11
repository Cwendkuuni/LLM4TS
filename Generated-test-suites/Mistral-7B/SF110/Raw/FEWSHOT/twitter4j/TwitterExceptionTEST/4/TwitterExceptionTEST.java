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
        Exception cause = new Exception("Caused by another exception");
        TwitterException exception = new TwitterException(cause);
        assertEquals("Caused by another exception", exception.getCause().getMessage());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        String message = "Test exception message with status code";
        HttpResponse res = new HttpResponse(200, new byte[0], new String[]{"Content-Type: application/json"});
        TwitterException exception = new TwitterException(message, res);
        assertEquals(200, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
        String message = "Test exception message with status code and cause";
        Exception cause = new Exception("Caused by another exception");
        HttpResponse res = new HttpResponse(200, new byte[0], new String[]{"Content-Type: application/json"});
        TwitterException exception = new TwitterException(message, cause, res);
        assertEquals(200, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getRetryAfter());
        assertEquals(cause.getMessage(), exception.getCause().getMessage());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseHttpResponseStatusCode() {
        String message = "Test exception message with status code, cause and statusCode";
        Exception cause = new Exception("Caused by another exception");
        int statusCode = 200;
        TwitterException exception = new TwitterException(message, cause, statusCode);
        assertEquals(statusCode, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getRetryAfter());
        assertEquals(cause.getMessage(), exception.getCause().getMessage());
    }

    @Test
    public void testGetRateLimitStatus() {
        HttpResponse res = new HttpResponse(429, new byte[0], new String[]{"X-Rate-Limit-Limit: 100, X-Rate-Limit-Remaining: 50, Retry-After: 300"});
        TwitterException exception = new TwitterException("Test exception message", res);
        RateLimitStatus rateLimitStatus = exception.getRateLimitStatus();
        assertEquals(100, rateLimitStatus.getLimit());
        assertEquals(50, rateLimitStatus.getRemaining());
        assertEquals(300, rateLimitStatus.getResetAfter());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        HttpResponse res = new HttpResponse(429, new byte[0], new String[]{"X-Rate-Limit-Limit: 100, X-Rate-Limit-Remaining: 50, Retry-After: 300, X-Rate-Limit-Reset: Sat, 04 Mar 2023 06:35:57 GMT, X-Rate-Limit-Bucket-Key: bucketKey"});
        TwitterException exception = new TwitterException("Test exception message", res);
        RateLimitStatus featureSpecificRateLimitStatus = exception.getFeatureSpecificRateLimitStatus();
        assertEquals("bucketKey", featureSpecificRateLimitStatus.getBucketKey());
        assertEquals(100, featureSpecificRateLimitStatus.getLimit());
        assertEquals(50, featureSpecificRateLimitStatus.getRemaining());
        assertEquals("Sat, 04 Mar 2023 06:35:57 GMT", featureSpecificRateLimitStatus.getResetAfter());
    }

    @Test
    public void testGetRetryAfter() {
        HttpResponse res = new HttpResponse(429, new byte[0], new String[]{"Retry-After: 300"});
        TwitterException exception = new TwitterException("Test exception message", res);
        assertEquals(300, exception.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException ioException = new IOException("Network error");
        TwitterException twitterException = new TwitterException("Test exception message", ioException);
        assertTrue(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse res = new HttpResponse(429, new byte[0], new String[]{"X-Rate-Limit-Limit: 100, X-Rate-Limit-Remaining: 50, Retry-After: 300"});
        TwitterException exception = new TwitterException("Test exception message", res);
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse res = new HttpResponse(404, new byte[0], new String[0]);
        TwitterException exception = new TwitterException("Test exception message", res);
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test exception message");
        String exceptionCode = exception.getExceptionCode();
        assertTrue(exceptionCode.startsWith("twitter4j"));
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test exception message 1");
        TwitterException exception2 = new TwitterException("Test exception message 2");
        TwitterException exception3 = new TwitterException("Test exception message 1");
        TwitterException exception4 = new TwitterException("Test exception message 1");
        TwitterException exception5 = new TwitterException("Test exception message 1", new IOException("Network error"));
        assertTrue(exception1.equals(exception1));
        assertTrue(exception1.equals(exception3));
        assertTrue(exception1.equals(exception4));
        assertFalse(exception1.equals(exception2));
        assertFalse(exception1.equals(null));
        assertFalse(exception1.equals(""));
        assertFalse(exception1.equals(exception5));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test exception message 1");
        TwitterException exception2 = new TwitterException("Test exception message 2");
        TwitterException exception3 = new TwitterException("Test exception message 1");
        TwitterException exception4 = new TwitterException("Test exception message 1");
        TwitterException exception5 = new TwitterException("Test exception message 1", new IOException("Network error"));
        int hashCode1 = exception1.hashCode();
        int hashCode2 = exception2.hashCode();
        int hashCode3 = exception3.hashCode();
        int hashCode4 = exception4.hashCode();
        int hashCode5 = exception5.hashCode();
        assertTrue(hashCode1 == hashCode3);
        assertTrue(hashCode1 == hashCode4);
        assertFalse(hashCode1 == hashCode2);
        assertFalse(hashCode1 == hashCode5);
    }
}