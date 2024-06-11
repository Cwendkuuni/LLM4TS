package twitter4j;

import static org.junit.Assert.*;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterException() {
        String msg = "Test Exception";
        TwitterException exception = new TwitterException(msg);
        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals(-1, exception.getRetryAfter());
        assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException("network error");
        String msg = "Test Exception";
        TwitterException exception = new TwitterException(msg, cause);
        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals(-1, exception.getRetryAfter());
        assertFalse(exception.isNested());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        String msg = "Test Exception";
        HttpResponse res = new DefaultHttpResponse(420);
        TwitterException exception = new TwitterException(msg, null, res);
        assertEquals(msg, exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals(-1, exception.getRetryAfter());
        assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithHttpResponseAndCause() {
        String msg = "Test Exception";
        Exception cause = new IOException("network error");
        HttpResponse res = new DefaultHttpResponse(420);
        TwitterException exception = new TwitterException(msg, cause, res);
        assertEquals(msg, exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals(-1, exception.getRetryAfter());
        assertFalse(exception.isNested());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testGetRetryAfter() {
        String msg = "Test Exception";
        HttpResponse res = new DefaultHttpResponse(420);
        res.setResponseHeader("Retry-After", "30");
        TwitterException exception = new TwitterException(msg, null, res);
        assertEquals(420, exception.getStatusCode());
        assertEquals(30, exception.getRetryAfter());
    }

    @Test
    public void testGetRetryAfterNegative() {
        String msg = "Test Exception";
        HttpResponse res = new DefaultHttpResponse(420);
        TwitterException exception = new TwitterException(msg, null, res);
        assertEquals(420, exception.getStatusCode());
        assertEquals(-1, exception.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        String msg = "Test Exception";
        IOException cause = new IOException("network error");
        TwitterException exception = new TwitterException(msg, cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testIsCausedByNetworkIssueFalse() {
        String msg = "Test Exception";
        TwitterException exception = new TwitterException(msg);
        assertFalse(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        String msg = "Test Exception";
        HttpResponse res = new DefaultHttpResponse(420);
        TwitterException exception = new TwitterException(msg, null, res);
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testExceededRateLimitationFalse() {
        String msg = "Test Exception";
        TwitterException exception = new TwitterException(msg);
        assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        String msg = "Test Exception";
        HttpResponse res = new DefaultHttpResponse(404);
        TwitterException exception = new TwitterException(msg, null, res);
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testResourceNotFoundFalse() {
        String msg = "Test Exception";
        TwitterException exception = new TwitterException(msg);
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testEquals() {
        String msg1 = "Test Exception 1";
        String msg2 = "Test Exception 2";
        int retryAfter1 = 1;
        int retryAfter2 = 2;
        TwitterException exception1 = new TwitterException(msg1, null, null);
        TwitterException exception2 = new TwitterException(msg2, null, null);
        TwitterException exception3 = new TwitterException(msg1, null, null);
        TwitterException exception4 = new TwitterException(msg1, null, null);
        exception1.setRetryAfter(retryAfter1);
        exception2.setRetryAfter(retryAfter2);
        assertTrue(exception1.equals(exception1));
        assertTrue(exception1.equals(exception3));
        assertTrue(exception1.equals(exception4));
        assertFalse(exception1.equals(exception2));
    }

    @Test
    public void testHashCode() {
        String msg1 = "Test Exception 1";
        String msg2 = "Test Exception 2";
        int retryAfter1 = 1;
        int retryAfter2 = 2;
        TwitterException exception1 = new TwitterException(msg1, null, null);
        TwitterException exception2 = new TwitterException(msg2, null, null);
        TwitterException exception3 = new TwitterException(msg1, null, null);
        TwitterException exception4 = new TwitterException(msg1, null, null);
        exception1.setRetryAfter(retryAfter1);
        exception2.setRetryAfter(retryAfter2);
        int hashCode1 = exception1.hashCode();
        int hashCode2 = exception2.hashCode();
        assertEquals(hashCode1, exception1.hashCode());
        assertEquals(hashCode1, exception3.hashCode());
        assertEquals(hashCode1, exception4.hashCode());
        assertNotEquals(hashCode1, exception2.hashCode());
    }

    @Test
    public void testToString() {
        String msg = "Test Exception";
        int statusCode = 400;
        int retryAfter = 30;
        RateLimitStatus rateLimitStatus = new RateLimitStatus(10, 15);
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus(5, 10);
        TwitterException exception = new TwitterException(msg, null, null, statusCode, rateLimitStatus, featureSpecificRateLimitStatus);
        String expected = "TwitterException{exceptionCode=[], statusCode=400, retryAfter=30, rateLimitStatus=RateLimitStatus{limit=10, remaining=15}, version=}";
        assertEquals(expected, exception.toString());
    }

    @Test
    public void testToStringWithNestedException() {
        String msg = "Test Exception";
        IOException cause = new IOException("network error");
        int statusCode = 400;
        int retryAfter = 30;
        RateLimitStatus rateLimitStatus = new RateLimitStatus(10, 15);
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus(5, 10);
        TwitterException exception = new TwitterException(msg, cause, statusCode, rateLimitStatus, featureSpecificRateLimitStatus);
        String expected = "TwitterException{exceptionCode=[], statusCode=400, retryAfter=30, rateLimitStatus=RateLimitStatus{limit=10, remaining=15}, version=exceptionCode=[network error], nested=true}";
        assertEquals(expected, exception.toString());
    }
}