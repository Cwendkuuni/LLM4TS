package twitter4j;

import static org.junit.Assert.*;
import twitter4j.internal.http.HttpResponse;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test error message");
        assertEquals("Test error message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException("Test IO exception");
        TwitterException exception = new TwitterException("Test error message", cause);
        assertEquals("Test error message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithHttpResponseMessage() {
        HttpResponse response = new HttpResponse(400, "Invalid Request", new byte[0]);
        TwitterException exception = new TwitterException("Test error message", response);
        assertEquals("Test error message" + System.lineSeparator() + response.getStatusMessage(), exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        assertEquals(RateLimitStatus.LIMIT_NONE, exception.getRateLimitStatus());
        assertEquals(RateLimitStatus.LIMIT_NONE, exception.getFeatureSpecificRateLimitStatus());
        assertEquals(-1, exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithHttpResponseMessageAndCause() {
        HttpResponse response = new HttpResponse(400, "Invalid Request", new byte[0]);
        IOException cause = new IOException("Test IO exception");
        TwitterException exception = new TwitterException("Test error message", response, cause);
        assertEquals("Test error message" + System.lineSeparator() + response.getStatusMessage(), exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        assertEquals(RateLimitStatus.LIMIT_NONE, exception.getRateLimitStatus());
        assertEquals(RateLimitStatus.LIMIT_NONE, exception.getFeatureSpecificRateLimitStatus());
        assertEquals(-1, exception.getRetryAfter());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithHttpResponseMessageAndCauseAndStatusCode() {
        HttpResponse response = new HttpResponse(400, "Invalid Request", new byte[0]);
        IOException cause = new IOException("Test IO exception");
        TwitterException exception = new TwitterException("Test error message", cause, 401);
        assertEquals("Test error message", exception.getMessage());
        assertEquals(401, exception.getStatusCode());
        assertEquals(RateLimitStatus.LIMIT_NONE, exception.getRateLimitStatus());
        assertEquals(RateLimitStatus.LIMIT_NONE, exception.getFeatureSpecificRateLimitStatus());
        assertEquals(-1, exception.getRetryAfter());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testEquals() {
        TwitterException ex1 = new TwitterException("Test error message 1");
        TwitterException ex2 = new TwitterException("Test error message 1");
        TwitterException ex3 = new TwitterException("Test error message 2");

        assertTrue(ex1.equals(ex1));
        assertTrue(ex1.equals(ex2));
        assertFalse(ex1.equals(ex3));
    }

    @Test
    public void testHashCode() {
        TwitterException ex1 = new TwitterException("Test error message 1");
        TwitterException ex2 = new TwitterException("Test error message 1");
        TwitterException ex3 = new TwitterException("Test error message 2");

        int hashCode1 = ex1.hashCode();
        int hashCode2 = ex2.hashCode();
        int hashCode3 = ex3.hashCode();

        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertTrue(hashCode1 == hashCode1);
        assertTrue(hashCode2 == hashCode2);
        assertTrue(hashCode3 == hashCode3);
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception1 = new TwitterException("Test error message 1");
        IOException cause = new IOException("Test IO exception");
        TwitterException exception2 = new TwitterException("Test error message 2", cause);

        assertFalse(exception1.isCausedByNetworkIssue());
        assertTrue(exception2.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() throws IOException {
        HttpResponse response = new HttpResponse(420, null, new byte[0]);
        TwitterException exception = new TwitterException("Test error message", response);

        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.isCausedByNetworkIssue());
        assertNull(exception.getRetryAfter());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test error message", 404);

        assertTrue(exception.resourceNotFound());
        assertFalse(exception.isCausedByNetworkIssue());
        assertNull(exception.getRetryAfter());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test error message", new IOException("Test IO exception"));
        String exceptionCode = exception.getExceptionCode();

        String hexString = "48656c6c6f207468697320696d2074686520666f722063686172736574";
        String expectedExceptionCode = "twitter4jtwitter4j".toUpperCase();
        assertTrue(Arrays.equals(hexString.substring(0, 16).getBytes(), exceptionCode.getBytes()));
        assertEquals(expectedExceptionCode, exceptionCode.substring(16));
    }
}