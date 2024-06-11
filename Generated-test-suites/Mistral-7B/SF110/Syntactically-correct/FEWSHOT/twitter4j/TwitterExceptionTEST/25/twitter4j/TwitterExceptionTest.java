package twitter4j; // Make sure to import necessary packages

import static org.junit.Assert.*;
import static twitter4j.internal.http.StatusCodes.*;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException ex = new TwitterException("Test message");;;
        assertEquals("Test message", ex.getMessage());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertNull(ex.getRetryAfter());
        assertFalse(ex.isCausedByNetworkIssue());
        assertFalse(ex.exceededRateLimitation());
        assertFalse(ex.resourceNotFound());
        assertNull(ex.getExceptionCode());
        assertFalse(ex.isNested());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        IOException cause = new IOException("Test IOException");
        TwitterException ex = new TwitterException("Test message", cause);
        assertEquals("Test message", ex.getMessage());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertNull(ex.getRetryAfter());
        assertTrue(ex.isCausedByNetworkIssue());
        assertFalse(ex.exceededRateLimitation());
        assertFalse(ex.resourceNotFound());
        assertNull(ex.getExceptionCode());
        assertFalse(ex.isNested());
        assertEquals("Test IOException", ex.getCause().getMessage());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponse(OK, "{}");
        TwitterException ex = new TwitterException("Test message", null, httpResponse);
        assertEquals("Test message", ex.getMessage());
        assertEquals(OK, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertNull(ex.getRetryAfter());
        assertFalse(ex.isCausedByNetworkIssue());
        assertFalse(ex.exceededRateLimitation());
        assertFalse(ex.resourceNotFound());
        assertNull(ex.getExceptionCode());
        assertFalse(ex.isNested());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponseAndCause() {
        IOException cause = new IOException("Test IOException");
        HttpResponse httpResponse = new HttpResponse(INTERNAL_SERVER_ERROR, "{}");
        TwitterException ex = new TwitterException("Test message", cause, httpResponse);
        assertEquals("Test message: " + INTERNAL_SERVER_ERROR + ": Something is broken. Please post to the group so the Twitter team can investigate.", ex.getMessage());
        assertEquals(INTERNAL_SERVER_ERROR, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertNull(ex.getRetryAfter());
        assertTrue(ex.isCausedByNetworkIssue());
        assertFalse(ex.exceededRateLimitation());
        assertFalse(ex.resourceNotFound());
        assertNull(ex.getExceptionCode());
        assertFalse(ex.isNested());
        assertEquals("Test IOException", ex.getCause().getMessage());
    }

    @Test
    public void testTwitterExceptionWithHttpResponseAndCause() {
        IOException cause = new IOException("Test IOException");
        HttpResponse httpResponse = new HttpResponse(INTERNAL_SERVER_ERROR, "{}");
        TwitterException ex = new TwitterException(null, cause, httpResponse);
        assertEquals("Internal Server Error: Something is broken. Please post to the group so the Twitter team can investigate.", ex.getMessage());
        assertEquals(INTERNAL_SERVER_ERROR, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertNull(ex.getRetryAfter());
        assertTrue(ex.isCausedByNetworkIssue());
        assertFalse(ex.exceededRateLimitation());
        assertFalse(ex.resourceNotFound());
        assertNull(ex.getExceptionCode());
        assertFalse(ex.isNested());
        assertEquals("Test IOException", ex.getCause().getMessage());
    }

    @Test
    public void testGetStatusCode() {
        HttpResponse httpResponse = new HttpResponse(INTERNAL_SERVER_ERROR, "{}");
        TwitterException ex = new TwitterException(null, null, httpResponse);
        assertEquals(INTERNAL_SERVER_ERROR, ex.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        HttpResponse httpResponse = new HttpResponse(RATE_LIMITED, "{}");
        TwitterException ex = new TwitterException(null, null, httpResponse);
        assertNotNull(ex.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        HttpResponse httpResponse = new HttpResponse(RATE_LIMITED, "{\"limit\": 1000, \"remaining\": 800, \"reset\": 1541753586}");
        TwitterException ex = new TwitterException(null, null, httpResponse);
        assertNotNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        HttpResponse httpResponse = new HttpResponse(RATE_LIMITED, "{}");
        TwitterException ex = new TwitterException(null, null, httpResponse);
        assertEquals(-1, ex.getRetryAfter());

        HttpResponse httpResponseWithRetryAfter = new HttpResponse(RATE_LIMITED, "{}", new Header("Retry-After", "120"));
        TwitterException exWithRetryAfter = new TwitterException(null, null, httpResponseWithRetryAfter);
        assertEquals(120, exWithRetryAfter.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("Test IOException");
        TwitterException exWithCause = new TwitterException("Test message", cause);
        assertTrue(exWithCause.isCausedByNetworkIssue());

        TwitterException exWithoutCause = new TwitterException("Test message");
        assertFalse(exWithoutCause.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = new HttpResponse(RATE_LIMITED, "{}");
        TwitterException ex = new TwitterException(null, null, httpResponse);
        assertTrue(ex.exceededRateLimitation());

        HttpResponse httpResponseWithoutRateLimitation = new HttpResponse(OK, "{}");
        TwitterException exWithoutRateLimitation = new TwitterException(null, null, httpResponseWithoutRateLimitation);
        assertFalse(exWithoutRateLimitation.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse httpResponse = new HttpResponse(NOT_FOUND, "{}");
        TwitterException ex = new TwitterException(null, null, httpResponse);
        assertTrue(ex.resourceNotFound());

        HttpResponse httpResponseWithoutNotFound = new HttpResponse(OK, "{}");
        TwitterException exWithoutNotFound = new TwitterException(null, null, httpResponseWithoutNotFound);
        assertFalse(exWithoutNotFound.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        HttpResponse httpResponse = new HttpResponse(RATE_LIMITED, "{}");
        TwitterException ex = new TwitterException(null, null, httpResponse);
        assertEquals("400: The request was invalid. An accompanying error message will explain why. This is the status code will be returned during rate limiting.", ex.getExceptionCode());

        HttpResponse httpResponseWithFeatureSpecificRateLimitation = new HttpResponse(RATE_LIMITED, "{\"limit\": 1000, \"remaining\": 800, \"reset\": 1541753586, \"code\": 200}");
        TwitterException exWithFeatureSpecificRateLimitation = new TwitterException(null, null, httpResponseWithFeatureSpecificRateLimitation);
        assertEquals("400: The request was invalid. An accompanying error message will explain why. This is the status code will be returned during rate limiting. Code: 200", exWithFeatureSpecificRateLimitation.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException parentException = new TwitterException("Test parent message");
        TwitterException childException = new TwitterException("Test child message");

        childException.setNested();

        assertTrue(childException.isNested());
        assertTrue(parentException.getCauses().contains(childException));
    }
}