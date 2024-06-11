package twitter4j.test;

import static org.junit.Assert.*;

import twitter4j.TwitterException;
import twitter4j.internal.http.StatusLine;
import twitter4j.internal.http.StatusLineImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException ex = new TwitterException("Test exception message");
        assertEquals("Test exception message", ex.getMessage());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertFalse(ex.isCausedByNetworkIssue());
        assertFalse(ex.exceededRateLimitation());
        assertFalse(ex.resourceNotFound());
        assertNull(ex.getRetryAfter());
        assertFalse(ex.nested);
    }

    @Test
    public void testTwitterExceptionWithCause() {
        IOException ioException = new IOException("IO exception message");
        TwitterException ex = new TwitterException("Test exception message", ioException);
        assertEquals("Test exception message", ex.getMessage());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertTrue(ex.isCausedByNetworkIssue());
        assertFalse(ex.exceededRateLimitation());
        assertFalse(ex.resourceNotFound());
        assertNull(ex.getRetryAfter());
        assertFalse(ex.nested);
        assertEquals(ioException, ex.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        StatusLine statusLine = new StatusLineImpl("HTTP/1.1", "400", "Bad Request");
        TwitterException ex = new TwitterException("Test exception message", statusLine);
        assertEquals("Test exception message: There was an error: Bad Request", ex.getMessage());
        assertEquals(400, ex.getStatusCode());
        assertNotNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertFalse(ex.isCausedByNetworkIssue());
        assertTrue(ex.exceededRateLimitation());
        assertFalse(ex.resourceNotFound());
        assertNull(ex.getRetryAfter());
        assertFalse(ex.nested);
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
        StatusLine statusLine = new StatusLineImpl("HTTP/1.1", "400", "Bad Request");
        IOException ioException = new IOException("IO exception message");
        TwitterException ex = new TwitterException("Test exception message", ioException, 400, statusLine);
        assertEquals("Test exception message: IO exception message", ex.getMessage());
        assertEquals(400, ex.getStatusCode());
        assertNotNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertTrue(ex.isCausedByNetworkIssue());
        assertTrue(ex.exceededRateLimitation());
        assertFalse(ex.resourceNotFound());
        assertNull(ex.getRetryAfter());
        assertFalse(ex.nested);
        assertEquals(ioException, ex.getCause());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        StatusLine statusLine = new StatusLineImpl("HTTP/1.1", "403", "Forbidden");
        TwitterException ex = new TwitterException("Test exception message", statusLine);
        ex.getRetryAfter();
    }

    @Test
    public void testExceededRateLimitation() {
        StatusLine statusLine = new StatusLineImpl("HTTP/1.1", "400", "Bad Request");
        TwitterException ex = new TwitterException("Test exception message", statusLine);
        assertTrue(ex.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        StatusLine statusLine = new StatusLineImpl("HTTP/1.1", "404", "Not Found");
        TwitterException ex = new TwitterException("Test exception message", statusLine);
        assertTrue(ex.resourceNotFound());
    }

    @Test
    public void testEquals() {
        TwitterException ex1 = new TwitterException("Test exception message 1");
        TwitterException ex2 = new TwitterException("Test exception message 2");
        assertFalse(ex1.equals(ex2));

        TwitterException ex3 = new TwitterException("Test exception message 1");
        TwitterException ex4 = new TwitterException("Test exception message 1");
        assertTrue(ex3.equals(ex4));

        TwitterException ex5 = new TwitterException("Test exception message 1", ex3);
        assertTrue(ex3.equals(ex5));
        assertTrue(ex5.equals(ex3));

        TwitterException ex6 = new TwitterException("Test exception message 1", ex3);
        ex6.setNested();
        assertFalse(ex3.equals(ex6));
        assertTrue(ex6.equals(ex6));

        TwitterException ex7 = new TwitterException("Test exception message 1", ex3);
        ex7.setRetryAfter(1);
        assertFalse(ex3.equals(ex7));
        assertFalse(ex7.equals(ex3));

        TwitterException ex8 = new TwitterException("Test exception message 1", ex3);
        ex8.setStatusCode(401);
        assertFalse(ex3.equals(ex8));
        assertFalse(ex8.equals(ex3));

        TwitterException ex9 = new TwitterException("Test exception message 1", ex3);
        ex9.setRateLimitStatus(new RateLimitStatusImpl(6, 10));
        assertFalse(ex3.equals(ex9));
        assertFalse(ex9.equals(ex3));
    }

    @Test
    public void testHashCode() {
        TwitterException ex1 = new TwitterException("Test exception message 1");
        TwitterException ex2 = new TwitterException("Test exception message 2");
        assertNotEquals(ex1.hashCode(), ex2.hashCode());

        TwitterException ex3 = new TwitterException("Test exception message 1");
        TwitterException ex4 = new TwitterException("Test exception message 1");
        assertEquals(ex3.hashCode(), ex4.hashCode());

        TwitterException ex5 = new TwitterException("Test exception message 1", ex3);
        assertEquals(ex3.hashCode(), ex5.hashCode());
        assertEquals(ex5.hashCode(), ex3.hashCode());

        TwitterException ex6 = new TwitterException("Test exception message 1", ex3);
        ex6.setNested();
        assertNotEquals(ex3.hashCode(), ex6.hashCode());
        assertEquals(ex6.hashCode(), ex6.hashCode());

        TwitterException ex7 = new TwitterException("Test exception message 1", ex3);
        ex7.setRetryAfter(1);
        assertNotEquals(ex3.hashCode(), ex7.hashCode());
        assertNotEquals(ex7.hashCode(), ex3.hashCode());

        TwitterException ex8 = new TwitterException("Test exception message 1", ex3);
        ex8.setStatusCode(401);
        assertNotEquals(ex3.hashCode(), ex8.hashCode());
        assertNotEquals(ex8.hashCode(), ex3.hashCode());

        TwitterException ex9 = new TwitterException("Test exception message 1", ex3);
        ex9.setRateLimitStatus(new RateLimitStatusImpl(6, 10));
        assertNotEquals(ex3.hashCode(), ex9.hashCode());
        assertNotEquals(ex9.hashCode(), ex3.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException ex = new TwitterException("Test exception message");
        StringBuilder expected = new StringBuilder();
        expected.append("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=-1, rateLimitStatus=null, version=}");
        assertEquals(expected.toString(), ex.toString());

        ex.setStatusCode(400);
        expected.setLength(0);
        expected.append("TwitterException{exceptionCode=[], statusCode=400, retryAfter=-1, rateLimitStatus=null, version=");
        expected.append(Version.getVersion());
        expected.append('}');
        assertEquals(expected.toString(), ex.toString());

        ex.setRetryAfter(1);
        expected.setLength(0);
        expected.append("TwitterException{exceptionCode=[], statusCode=400, retryAfter=1, rateLimitStatus=null, version=");
        expected.append(Version.getVersion());
        expected.append('}');
        assertEquals(expected.toString(), ex.toString());

        ex.setRateLimitStatus(new RateLimitStatusImpl(6, 10));
        expected.setLength(0);
        expected.append("TwitterException{exceptionCode=[], statusCode=400, retryAfter=-1, rateLimitStatus={limit=6, remaining=10}, version=");
        expected.append(Version.getVersion());
        expected.append('}');
        assertEquals(expected.toString(), ex.toString());
    }

    @Test
    public void testGetCauseWithHttpResponse() {
        StatusLine statusLine = new StatusLineImpl("HTTP/1.1", "400", "Bad Request");
        TwitterException ex = new TwitterException("Test exception message", statusLine);
        String cause = TwitterException.getCause(statusLine);
        String expected = "There was an error: Bad Request";
        assertEquals(expected, cause);
    }

    @Test
    public void testGetRetryAfterWithHttpResponse() {
        StatusLine statusLine = new StatusLineImpl("HTTP/1.1", "420", "Too Many Requests");
        String retryAfterStr = "120, 240";
        statusLine.setResponseHeader("Retry-After", retryAfterStr);
        TwitterException ex = new TwitterException("Test exception message", statusLine);
        int retryAfter = ex.getRetryAfter();
        int expected = 120;
        assertEquals(expected, retryAfter);
    }

    @Test
    public void testGetRateLimitStatusWithHttpResponse() {
        StatusLine statusLine = new StatusLineImpl("HTTP/1.1", "420", "Too Many Requests");
        List<String> headerNames = new ArrayList<>();
        headerNames.add("X-Rate-Limit-Limit");
        headerNames.add("X-Rate-Limit-Remaining");
        headerNames.add("X-Rate-Limit-Reset");
        List<String> headerValues = new ArrayList<>();
        headerValues.add("100");
        headerValues.add("90");
        headerValues.add("1234567890");
        for (int i = 0; i < headerNames.size(); i++) {
            statusLine.addHeader(headerNames.get(i), headerValues.get(i));
        }
        TwitterException ex = new TwitterException("Test exception message", statusLine);
        RateLimitStatus rateLimitStatus = ex.getRateLimitStatus();
        int limit = rateLimitStatus.getLimit();
        int remaining = rateLimitStatus.getRemaining();
        long reset = rateLimitStatus.getReset();
        int expectedLimit = 100;
        int expectedRemaining = 90;
        long expectedReset = 1234567890;
        assertEquals(expectedLimit, limit);
        assertEquals(expectedRemaining, remaining);
        assertEquals(expectedReset, reset);
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatusWithHttpResponse() {
        StatusLine statusLine = new StatusLineImpl("HTTP/1.1", "420", "Too Many Requests");
        List<String> headerNames = new ArrayList<>();
        headerNames.add("X-Rate-Limit-Bucket-Key-1");
        headerNames.add("X-Rate-Limit-Limit-1");
        headerNames.add("X-Rate-Limit-Remaining-1");
        headerNames.add("X-Rate-Limit-Reset-1");
        List<String> headerValues = new ArrayList<>();
        headerValues.add("bucket1");
        headerValues.add("limit1");
        headerValues.add("remaining1");
        headerValues.add("reset1");
        for (int i = 0; i < headerNames.size(); i++) {
            statusLine.addHeader(headerNames.get(i), headerValues.get(i));
        }
        TwitterException ex = new TwitterException("Test exception message", statusLine);
        RateLimitStatus featureSpecificRateLimitStatus = ex.getFeatureSpecificRateLimitStatus();
        String bucketKey = featureSpecificRateLimitStatus.getBucketKey();
        int limit = featureSpecificRateLimitStatus.getLimit();
        int remaining = featureSpecificRateLimitStatus.getRemaining();
        long reset = featureSpecificRateLimitStatus.getReset();
        String expectedBucketKey = "bucket1";
        int expectedLimit = 1;
        int expectedRemaining = 1;
        long expectedReset = 1234567890;
        assertEquals(expectedBucketKey, bucketKey);
        assertEquals(expectedLimit, limit);
        assertEquals(expectedRemaining, remaining);
        assertEquals(expectedReset, reset);
    }
}