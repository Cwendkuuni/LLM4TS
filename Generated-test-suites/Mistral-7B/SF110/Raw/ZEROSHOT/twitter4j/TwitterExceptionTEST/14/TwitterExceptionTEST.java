package twitter4j.test;

import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

/**
 * Test class for TwitterException.
 */
public class TwitterExceptionTest {

    /**
     * Test constructor with message only.
     */
    @Test
    public void testTwitterExceptionMessage() {
        TwitterException ex = new TwitterException("Test exception message.");
        assertEquals("Test exception message.", ex.getMessage());
        assertNull(ex.getCause());
        assertNull(ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertFalse(ex.isNested());
        assertFalse(ex.exceededRateLimitation());
        assertFalse(ex.isCausedByNetworkIssue());
        assertFalse(ex.resourceNotFound());
    }

    /**
     * Test constructor with cause only.
     */
    @Test
    public void testTwitterExceptionCause() {
        IOException cause = new IOException("Test IOException message.");
        TwitterException ex = new TwitterException(cause);
        assertEquals("Test IOException message.", ex.getCause().getMessage());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertFalse(ex.isNested());
        assertFalse(ex.exceededRateLimitation());
        assertTrue(ex.isCausedByNetworkIssue());
        assertFalse(ex.resourceNotFound());
    }

    /**
     * Test constructor with message and HttpResponse.
     */
    @Test
    public void testTwitterExceptionMessageHttpResponse() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(400);
        response.setResponseHeader("Retry-After", "120");
        TwitterException ex = new TwitterException("Test exception message.", response);

        assertEquals("Test exception message.\n400: The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.", ex.getMessage());
        assertEquals(400, ex.getStatusCode());
        assertEquals(120, ex.getRetryAfter());
        assertNotNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertFalse(ex.isNested());
        assertFalse(ex.exceededRateLimitation());
        assertFalse(ex.isCausedByNetworkIssue());
        assertFalse(ex.resourceNotFound());
    }

    /**
     * Test constructor with message, cause, and status code.
     */
    @Test
    public void testTwitterExceptionMessageCauseStatusCode() {
        IOException cause = new IOException("Test IOException message.");
        TwitterException ex = new TwitterException("Test exception message.", cause, 403);

        assertEquals("Test exception message.", ex.getMessage());
        assertEquals(-1, ex.getStatusCode());
        assertNotNull(ex.getCause());
        assertEquals("Test IOException message.", ex.getCause().getMessage());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertFalse(ex.isNested());
        assertFalse(ex.exceededRateLimitation());
        assertFalse(ex.isCausedByNetworkIssue());
        assertFalse(ex.resourceNotFound());
    }

    /**
     * Test getStatusCode().
     */
    @Test
    public void testGetStatusCode() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(420);
        TwitterException ex = new TwitterException("Test exception message.", response);
        assertEquals(420, ex.getStatusCode());

        response.setStatusCode(500);
        ex = new TwitterException("Test exception message.", response);
        assertEquals(500, ex.getStatusCode());
    }

    /**
     * Test getRateLimitStatus().
     */
    @Test
    public void testGetRateLimitStatus() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(420);
        InputStream inputStream = new ByteArrayInputStream("{\"limit\":15,\"remaining\":5}".getBytes());
        response.setEntity(inputStream);
        TwitterException ex = new TwitterException("Test exception message.", response);

        assertEquals(15, ex.getRateLimitStatus().getLimit());
        assertEquals(5, ex.getRateLimitStatus().getRemaining());
    }

    /**
     * Test getFeatureSpecificRateLimitStatus().
     */
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(420);
        InputStream inputStream = new ByteArrayInputStream("{\"limit\":15,\"remaining\":5,\"reset_time\":1516230460}".getBytes());
        response.setEntity(inputStream);
        TwitterException ex = new TwitterException("Test exception message.", response);

        assertEquals(15, ex.getFeatureSpecificRateLimitStatus().getLimit());
        assertEquals(5, ex.getFeatureSpecificRateLimitStatus().getRemaining());
        assertEquals(1516230460L, ex.getFeatureSpecificRateLimitStatus().getResetTime());
    }

    /**
     * Test getRetryAfter().
     */
    @Test
    public void testGetRetryAfter() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(420);
        response.setResponseHeader("Retry-After", "120");
        TwitterException ex = new TwitterException("Test exception message.", response);

        assertEquals(120, ex.getRetryAfter());

        response.setStatusCode(400);
        ex = new TwitterException("Test exception message.", response);
        assertFalse(ex.getRetryAfter() > 0);
    }

    /**
     * Test isCausedByNetworkIssue().
     */
    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("Test IOException message.");
        TwitterException ex = new TwitterException("Test exception message.", cause);
        assertTrue(ex.isCausedByNetworkIssue());

        ex = new TwitterException("Test exception message.");
        assertFalse(ex.isCausedByNetworkIssue());
    }

    /**
     * Test exceededRateLimitation().
     */
    @Test
    public void testExceededRateLimitation() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(400);
        TwitterException ex = new TwitterException("Test exception message.", response);
        assertFalse(ex.exceededRateLimitation());

        response.setStatusCode(420);
        ex = new TwitterException("Test exception message.", response);
        assertTrue(ex.exceededRateLimitation());
    }

    /**
     * Test resourceNotFound().
     */
    @Test
    public void testResourceNotFound() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(404);
        TwitterException ex = new TwitterException("Test exception message.", response);
        assertTrue(ex.resourceNotFound());

        response.setStatusCode(400);
        ex = new TwitterException("Test exception message.", response);
        assertFalse(ex.resourceNotFound());
    }

    /**
     * Test equals().
     */
    @Test
    public void testEquals() {
        TwitterException ex1 = new TwitterException("Test exception message 1.");
        TwitterException ex2 = new TwitterException("Test exception message 2.");
        assertFalse(ex1.equals(ex2));

        ex1.setStatusCode(420);
        ex1.setRetryAfter(120);
        ex2.setStatusCode(420);
        ex2.setRetryAfter(120);
        assertTrue(ex1.equals(ex2));

        ex1.setRateLimitStatus(new RateLimitStatus(15, 5));
        ex2.setRateLimitStatus(new RateLimitStatus(15, 5));
        assertTrue(ex1.equals(ex2));

        ex1 = new TwitterException("Test exception message 1.", new IOException("Test IOException message 1."));
        ex2 = new TwitterException("Test exception message 2.", new IOException("Test IOException message 2."));
        assertFalse(ex1.equals(ex2));

        ex1.setNested();
        ex2.setNested();
        assertTrue(ex1.equals(ex2));
    }

    /**
     * Test hashCode().
     */
    @Test
    public void testHashCode() {
        TwitterException ex1 = new TwitterException("Test exception message 1.");
        TwitterException ex2 = new TwitterException("Test exception message 2.");
        assertNotEqual(ex1.hashCode(), ex2.hashCode());

        ex1.setStatusCode(420);
        ex1.setRetryAfter(120);
        ex2.setStatusCode(420);
        ex2.setRetryAfter(120);
        assertEquals(ex1.hashCode(), ex2.hashCode());

        ex1.setRateLimitStatus(new RateLimitStatus(15, 5));
        ex2.setRateLimitStatus(new RateLimitStatus(15, 5));
        assertEquals(ex1.hashCode(), ex2.hashCode());
    }

    /**
     * Test toString().
     */
    @Test
    public void testToString() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(420);
        response.setResponseHeader("Retry-After", "120");
        TwitterException ex = new TwitterException("Test exception message.", response);

        String expected = "TwitterException{statusCode=420, retryAfter=120, rateLimitStatus=RateLimitStatus{limit=15, remaining=5}, version=}";
        assertEquals(expected, ex.toString());
    }

    private void assertNotEqual(int expected, int actual) {
        try {
            assertEquals(expected, actual);
            fail("Expected unequal integers.");
        } catch (AssertionError e) {
            // Expected.
        }
    }
}