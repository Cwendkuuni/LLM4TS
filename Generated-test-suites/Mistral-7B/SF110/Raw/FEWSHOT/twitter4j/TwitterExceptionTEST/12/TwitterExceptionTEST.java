package twitter4j.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static twitter4j.internal.http.StatusCodes.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

    @Test
    public void testTwitterException_String() {
        TwitterException ex = new TwitterException("Test Exception");
        assertEquals("Test Exception", ex.getMessage());
    }

    @Test
    public void testTwitterException_Exception() {
        IOException ioex = new IOException("Test IOException");
        TwitterException ex = new TwitterException(ioex);
        assertEquals("Test IOException", ex.getCause().getMessage());
        assertTrue(ex.isCausedByNetworkIssue());
    }

    @Test
    public void testTwitterException_String_HttpResponse() {
        int statusCode = 400;
        String retryAfterStr = "60";
        Map<String, String> headers = new HashMap<>();
        headers.put("Retry-After", retryAfterStr);
        HttpResponse res = new HttpResponse(statusCode, headers, new byte[0]);
        TwitterException ex = new TwitterException("Test Exception", res);
        assertEquals(statusCode, ex.getStatusCode());
        assertEquals(RateLimitStatus.LIMIT_RATE_LIMIT, ex.getRateLimitStatus().getLimit());
        assertEquals(Integer.parseInt(retryAfterStr), ex.getRetryAfter());
    }

    @Test
    public void testTwitterException_String_Exception_HttpResponse() {
        int statusCode = 400;
        String retryAfterStr = "60";
        Map<String, String> headers = new HashMap<>();
        headers.put("Retry-After", retryAfterStr);
        HttpResponse res = new HttpResponse(statusCode, headers, new byte[0]);
        IOException ioex = new IOException("Test IOException");
        TwitterException ex = new TwitterException("Test Exception", ioex, statusCode);
        assertEquals(statusCode, ex.getStatusCode());
        assertEquals(RateLimitStatus.LIMIT_RATE_LIMIT, ex.getRateLimitStatus().getLimit());
        assertEquals(Integer.parseInt(retryAfterStr), ex.getRetryAfter());
    }

    @Test
    public void testTwitterException_String_Exception_int() {
        int statusCode = 400;
        IOException ioex = new IOException("Test IOException");
        TwitterException ex = new TwitterException("Test Exception", ioex, statusCode);
        assertEquals(statusCode, ex.getStatusCode());
        assertFalse(ex.isCausedByNetworkIssue());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException ex = new TwitterException("Test Exception", new HttpResponse(200, new HashMap<>(), new byte[0]));
        assertEquals(-1, ex.getStatusCode());
        ex = new TwitterException("Test Exception", new HttpResponse(400, new HashMap<>(), new byte[0]));
        assertEquals(400, ex.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException ex = new TwitterException("Test Exception", new HttpResponse(420, new HashMap<>(), new byte[0]));
        assertNull(ex.getRateLimitStatus());
        ex = new TwitterException("Test Exception", new HttpResponse(400, new HashMap<>(), new byte[0]));
        assertEquals(RateLimitStatus.LIMIT_RATE_LIMIT, ex.getRateLimitStatus().getLimit());
        assertEquals(RateLimitStatus.LIMIT_RESET_MS, ex.getRateLimitStatus().getReset());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException ex = new TwitterException("Test Exception", new HttpResponse(420, new HashMap<>(), new byte[0]));
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        ex = new TwitterException("Test Exception", new HttpResponse(429, new HashMap<>(), new byte[0]));
        assertEquals(RateLimitStatus.LIMIT_STATUS_SMS, ex.getFeatureSpecificRateLimitStatus().getLimit());
        assertEquals(RateLimitStatus.LIMIT_RESET_MS, ex.getFeatureSpecificRateLimitStatus().getReset());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException ex = new TwitterException("Test Exception", new HttpResponse(400, new HashMap<>(), new byte[0]));
        assertEquals(-1, ex.getRetryAfter());
        ex = new TwitterException("Test Exception", new HttpResponse(420, new HashMap<>(), new byte[0]));
        assertEquals(60, ex.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException ex1 = new TwitterException("Test Exception");
        assertFalse(ex1.isCausedByNetworkIssue());
        IOException ioex = new IOException("Test IOException");
        TwitterException ex2 = new TwitterException("Test Exception", ioex);
        assertTrue(ex2.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException ex1 = new TwitterException("Test Exception");
        assertFalse(ex1.exceededRateLimitation());
        HttpResponse res = new HttpResponse(400, new HashMap<>(), new byte[0]);
        TwitterException ex2 = new TwitterException("Test Exception", res);
        assertTrue(ex2.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException ex1 = new TwitterException("Test Exception");
        assertFalse(ex1.resourceNotFound());
        TwitterException ex2 = new TwitterException("Test Exception", new HttpResponse(404, new HashMap<>(), new byte[0]));
        assertTrue(ex2.resourceNotFound());
    }

    @Test
    public void testEquals() {
        TwitterException ex1 = new TwitterException("Test Exception");
        TwitterException ex2 = new TwitterException("Test Exception");
        assertEquals(ex1, ex2);

        ex1 = new TwitterException("Test Exception");
        ex2 = new TwitterException("Test Exception2");
        assertFalse(ex1.equals(ex2));

        ex1.setNested();
        ex2 = new TwitterException("Test Exception");
        assertFalse(ex1.equals(ex2));

        ex1 = new TwitterException("Test Exception");
        ex2 = new TwitterException("Test Exception", new HttpResponse(400, new HashMap<>(), new byte[0]));
        assertFalse(ex1.equals(ex2));

        ex1 = new TwitterException("Test Exception", new HttpResponse(400, new HashMap<>(), new byte[0]));
        ex2 = new TwitterException("Test Exception", new HttpResponse(400, new HashMap<>(), new byte[0]));
        assertEquals(ex1, ex2);
    }

    @Test
    public void testHashCode() {
        TwitterException ex1 = new TwitterException("Test Exception");
        TwitterException ex2 = new TwitterException("Test Exception");
        assertEquals(ex1.hashCode(), ex2.hashCode());

        ex1 = new TwitterException("Test Exception");
        ex2 = new TwitterException("Test Exception2");
        assertNotSame(ex1.hashCode(), ex2.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException ex = new TwitterException("Test Exception", new HttpResponse(400, new HashMap<>(), new byte[0]));
        String expected = "TwitterException{exceptionCode=[], statusCode=400, retryAfter=-1, rateLimitStatus=null, version=4.0.6}";
        assertEquals(expected, ex.toString());
    }
}