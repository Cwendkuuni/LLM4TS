package com.example.twitterexceptiontest; // replace with your package name if necessary

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.RateLimitStatus;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test exception message");
        assertEquals("Test exception message", exception.getMessage());
        assertNull(exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        IOException cause = new IOException("Test IOException message");
        TwitterException exception = new TwitterException("Test exception message", cause);
        assertEquals("Test exception message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertNull(exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponse(420, "Retry-After: 60", new byte[0]);
        TwitterException exception = new TwitterException("Test exception message", httpResponse);
        assertEquals("Test exception message" + System.lineSeparator() + "Retry-After: 60", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        HttpResponse httpResponse = new HttpResponse(420, "Retry-After: 60", new byte[0]);
        IOException cause = new IOException("Test IOException message");
        TwitterException exception = new TwitterException("Test exception message", cause, 420);
        assertEquals("Test exception message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(420, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        HttpResponse httpResponse = new HttpResponse(404, "Retry-After: 60", new byte[0]);
        TwitterException exception = new TwitterException("Test exception message", httpResponse);
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssueWhenCauseIsIOException() {
        IOException cause = new IOException("Test IOException message");
        TwitterException exception = new TwitterException("Test exception message", cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitationWhenStatusCodeIs400Or420() {
        HttpResponse httpResponse1 = new HttpResponse(400, "Rate limit exceeded", new byte[0]);
        HttpResponse httpResponse2 = new HttpResponse(420, "Rate limit exceeded", new byte[0]);
        TwitterException exception1 = new TwitterException("Test exception message", httpResponse1);
        TwitterException exception2 = new TwitterException("Test exception message", httpResponse2);
        assertTrue(exception1.exceededRateLimitation());
        assertTrue(exception2.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFoundWhenStatusCodeIs404() {
        HttpResponse httpResponse = new HttpResponse(404, "Resource not found", new byte[0]);
        TwitterException exception = new TwitterException("Test exception message", httpResponse);
        assertTrue(exception.resourceNotFound());
    }
}