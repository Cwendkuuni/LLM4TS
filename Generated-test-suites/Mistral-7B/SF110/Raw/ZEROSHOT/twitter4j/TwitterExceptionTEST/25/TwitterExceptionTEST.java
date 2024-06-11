package twitter4j.test;

import static org.junit.Assert.*;

import twitter4j.*;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.StatusLine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        String message = "Test TwitterException with message";
        TwitterException exception = new TwitterException(message);
        assertEquals(message, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
        assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        IOException cause = new IOException("Test IOException");
        TwitterException exception = new TwitterException("Test TwitterException", cause);
        assertEquals("Test IOException", cause.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
        assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        String message = "Test TwitterException with message and HttpResponse";
        HttpResponse httpResponse = createHttpResponse(420, "Retry-After: 10");

        TwitterException exception = new TwitterException(message, httpResponse);
        assertEquals(message, exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertEquals(RateLimitStatus.Limited, exception.getRateLimitStatus().getLimit());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
        assertFalse(exception.isNested());
        assertEquals(10, exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithMessageAndCauseAndHttpResponse() {
        String message = "Test TwitterException with message, cause and HttpResponse";
        IOException cause = new IOException("Test IOException");
        HttpResponse httpResponse = createHttpResponse(420, "Retry-After: 10");

        TwitterException exception = new TwitterException(message, cause, httpResponse);
        assertEquals(message, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
        assertFalse(exception.isNested());
        assertEquals(10, exception.getRetryAfter());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test TwitterException 1");
        TwitterException exception2 = new TwitterException("Test TwitterException 2");

        assertFalse(exception1.equals(exception2));

        TwitterException exception3 = new TwitterException("Test TwitterException 1");
        assertTrue(exception1.equals(exception3));

        HttpResponse httpResponse1 = createHttpResponse(420, "Retry-After: 10");
        HttpResponse httpResponse2 = createHttpResponse(420, "Retry-After: 10");

        TwitterException exception4 = new TwitterException("Test TwitterException 1", httpResponse1);
        TwitterException exception5 = new TwitterException("Test TwitterException 1", httpResponse2);

        assertTrue(exception4.equals(exception5));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test TwitterException 1");
        TwitterException exception2 = new TwitterException("Test TwitterException 2");

        int hashCode1 = exception1.hashCode();
        int hashCode2 = exception2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        TwitterException exception3 = new TwitterException("Test TwitterException 1");
        int hashCode3 = exception3.hashCode();

        assertEquals(hashCode1, hashCode3);

        HttpResponse httpResponse1 = createHttpResponse(420, "Retry-After: 10");
        HttpResponse httpResponse2 = createHttpResponse(420, "Retry-After: 10");

        TwitterException exception4 = new TwitterException("Test TwitterException 1", httpResponse1);
        TwitterException exception5 = new TwitterException("Test TwitterException 1", httpResponse2);

        int hashCode4 = exception4.hashCode();
        int hashCode5 = exception5.hashCode();

        assertEquals(hashCode4, hashCode5);
    }

    private HttpResponse createHttpResponse(int statusCode, String retryAfterHeader) {
        StatusLine statusLine = new StatusLine(200, "OK", "");
        statusLine.setStatusCode(statusCode);
        statusLine.setReasonPhrase("Status Reason Phrase");
        statusLine.setHeaders(createHeadersMap(retryAfterHeader));

        HttpResponse httpResponse = new HttpResponse(statusLine);
        return httpResponse;
    }

    private Map<String, String> createHeadersMap(String retryAfterHeader) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Retry-After", retryAfterHeader);
        return headers;
    }
}