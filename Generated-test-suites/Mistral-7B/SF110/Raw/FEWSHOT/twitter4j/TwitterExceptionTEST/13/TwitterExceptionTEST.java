package twitter4j.test;

import static org.junit.Assert.*;
import static twitter4j.internal.http.StatusCodes.*;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

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
        assertEquals("Test IOException", ex.getCause().getMessage());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse res = new HttpResponse(400, new Date(), new byte[0]);
        TwitterException ex = new TwitterException("Test message", null, res);
        assertEquals("Test message" + "\n" + "400: The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.", ex.getMessage());
        assertEquals(400, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertEquals(-1, ex.getRetryAfter());
        assertFalse(ex.isCausedByNetworkIssue());
        assertTrue(ex.exceededRateLimitation());
        assertFalse(ex.resourceNotFound());
        assertNull(ex.getExceptionCode());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
        HttpResponse res = new HttpResponse(400, new Date(), new byte[0]);
        IOException cause = new IOException("Test IOException");
        TwitterException ex = new TwitterException("Test message", cause, res);
        assertEquals("Test message", ex.getMessage());
        assertEquals(400, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertEquals(-1, ex.getRetryAfter());
        assertTrue(ex.isCausedByNetworkIssue());
        assertTrue(ex.exceededRateLimitation());
        assertFalse(ex.resourceNotFound());
        assertNull(ex.getExceptionCode());
        assertEquals("Test IOException", ex.getCause().getMessage());
    }

    @Test
    public void testTwitterExceptionWithMessageStatusCode() {
        TwitterException ex = new TwitterException("Test message", 400);
        assertEquals("Test message", ex.getMessage());
        assertEquals(400, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertNull(ex.getRetryAfter());
        assertFalse(ex.isCausedByNetworkIssue());
        assertTrue(ex.exceededRateLimitation());
        assertFalse(ex.resourceNotFound());
        assertNull(ex.getExceptionCode());
    }

    @Test
    public void testGetCause() {
        HttpResponse res = new HttpResponse(400, new Date(), new byte[0]);
        TwitterException ex = new TwitterException("Test message", res);
        assertEquals("400: The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.", ex.getCause().getMessage());
    }

    @Test
    public void testRateLimitStatus() {
        HttpResponse res = new HttpResponse(420, new Date(), new byte[0]);
        RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 15, 15, 15, 15, 15, 15, 15);
        res.setResponseHeader("X-Rate-Limit-Limit", "15");
        res.setResponseHeader("X-Rate-Limit-Remaining", "15");
        res.setResponseHeader("X-Rate-Limit-Reset", "15");
        TwitterException ex = new TwitterException("Test message", null, res);
        assertEquals(rateLimitStatus, ex.getRateLimitStatus());
        assertEquals(15, ex.getRateLimitStatus().getLimit());
        assertEquals(15, ex.getRateLimitStatus().getRemaining());
        assertEquals(15, ex.getRateLimitStatus().getReset());
        assertEquals(15, ex.getRateLimitStatus().getSecondPerRequest());
        assertEquals(15, ex.getRateLimitStatus().getSecondRemaining());
        assertEquals(15, ex.getRateLimitStatus().getSecondReset());
        assertEquals(15, ex.getRateLimitStatus().getMinutes());
        assertEquals(15, ex.getRateLimitStatus().getMinutesLeft());
    }

    @Test
    public void testFeatureSpecificRateLimitStatus() {
        HttpResponse res = new HttpResponse(420, new Date(), new byte[0]);
        RateLimitStatusFeatureSpecific rateLimitStatus = new RateLimitStatusFeatureSpecific(15, 15, 15, 15);
        res.setResponseHeader("X-Rate-Limit-Limit-Account-Activity", "15");
        res.setResponseHeader("X-Rate-Limit-Remaining-Account-Activity", "15");
        res.setResponseHeader("X-Rate-Limit-Reset-Account-Activity", "15");
        TwitterException ex = new TwitterException("Test message", null, res);
        assertEquals(rateLimitStatus, ex.getFeatureSpecificRateLimitStatus());
        assertEquals(15, ex.getFeatureSpecificRateLimitStatus().getLimit());
        assertEquals(15, ex.getFeatureSpecificRateLimitStatus().getRemaining());
        assertEquals(15, ex.getFeatureSpecificRateLimitStatus().getReset());
    }

    @Test
    public void testRetryAfter() {
        HttpResponse res = new HttpResponse(420, new Date(), new byte[0]);
        String retryAfterStr = "120";
        res.setResponseHeader("Retry-After", retryAfterStr);
        TwitterException ex = new TwitterException("Test message", null, res);
        assertEquals(Integer.valueOf(120), ex.getRetryAfter());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWithout420StatusCode() {
        TwitterException ex = new TwitterException("Test message", null);
        ex.setStatusCode(400);
        ex.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("Test IOException");
        TwitterException ex = new TwitterException("Test message", cause);
        assertTrue(ex.isCausedByNetworkIssue());
        TwitterException ex2 = new TwitterException("Test message");
        assertFalse(ex2.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse res = new HttpResponse(400, new Date(), new byte[0]);
        TwitterException ex = new TwitterException("Test message", null, res);
        assertFalse(ex.exceededRateLimitation());
        HttpResponse res2 = new HttpResponse(420, new Date(), new byte[0]);
        TwitterException ex2 = new TwitterException("Test message", null, res2);
        assertTrue(ex2.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse res = new HttpResponse(404, new Date(), new byte[0]);
        TwitterException ex = new TwitterException("Test message", null, res);
        assertTrue(ex.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException ex = new TwitterException("Test message");
        String exceptionCode = ex.getExceptionCode();
        assertTrue(exceptionCode.length() > 0);
    }

    @Test
    public void testEquals() {
        TwitterException ex1 = new TwitterException("Test message 1", 400);
        TwitterException ex2 = new TwitterException("Test message 2", 400);
        assertFalse(ex1.equals(ex2));

        TwitterException ex3 = new TwitterException("Test message 1", 400);
        assertTrue(ex1.equals(ex3));

        TwitterException ex4 = new TwitterException("Test message 1", 400);
        ex4.setStatusCode(420);
        assertFalse(ex1.equals(ex4));

        TwitterException ex5 = new TwitterException("Test message 1", 400);
        ex5.setRetryAfter(10);
        assertFalse(ex1.equals(ex5));

        TwitterException ex6 = new TwitterException("Test message 1", 400);
        RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 15, 15, 15, 15, 15, 15, 15);
        ex6.setRateLimitStatus(rateLimitStatus);
        assertFalse(ex1.equals(ex6));
    }

    @Test
    public void testHashCode() {
        TwitterException ex1 = new TwitterException("Test message 1", 400);
        TwitterException ex2 = new TwitterException("Test message 2", 400);
        assertNotEquals(ex1.hashCode(), ex2.hashCode());

        TwitterException ex3 = new TwitterException("Test message 1", 400);
        TwitterException ex4 = new TwitterException("Test message 1", 400);
        assertEquals(ex3.hashCode(), ex4.hashCode());

        TwitterException ex5 = new TwitterException("Test message 1", 400);
        ex5.setStatusCode(420);
        assertNotEquals(ex3.hashCode(), ex5.hashCode());

        TwitterException ex6 = new TwitterException("Test message 1", 400);
        ex6.setRetryAfter(10);
        assertNotEquals(ex3.hashCode(), ex6.hashCode());

        TwitterException ex7 = new TwitterException("Test message 1", 400);
        RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 15, 15, 15, 15, 15, 15, 15);
        ex7.setRateLimitStatus(rateLimitStatus);
        assertNotEquals(ex3.hashCode(), ex7.hashCode());
    }
}