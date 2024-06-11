package com.example.twitterexception.test;

import static org.junit.Assert.*;

import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getRetryAfter());
        assertFalse(exception.nested);
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException();
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertSame(cause, exception.getCause());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getRetryAfter());
        assertFalse(exception.nested);
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(400);
        TwitterException exception = new TwitterException("Test Exception Message", null, httpResponse);
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getRetryAfter());
        assertFalse(exception.nested);
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(400);
        Exception cause = new IOException();
        TwitterException exception = new TwitterException("Test Exception Message", cause, httpResponse);
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertSame(cause, exception.getCause());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getRetryAfter());
        assertFalse(exception.nested);
    }

    @Test
    public void testTwitterExceptionWithMessageStatusCode() {
        TwitterException exception = new TwitterException("Test Exception Message", -1, 400);
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getRetryAfter());
        assertFalse(exception.nested);
    }

    @Test
    public void testGetRateLimitStatus() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(420);
        RateLimitStatus rateLimitStatus = new RateLimitStatus();
        httpResponse.setResponseHeader("Retry-After", "120");
        TwitterException exception = new TwitterException("Test Exception Message", null, httpResponse);
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
        assertEquals(120, exception.getRetryAfter());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(420);
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus();
        httpResponse.setResponseHeader("X-Rate-Limit-Remaining", "0");
        httpResponse.setResponseHeader("X-Rate-Limit-Reset", "1342360521");
        TwitterException exception = new TwitterException("Test Exception Message", null, httpResponse);
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
        assertEquals("0", exception.getFeatureSpecificRateLimitStatus().getRemaining());
        assertEquals("1342360521", exception.getFeatureSpecificRateLimitStatus().getReset());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(400);
        TwitterException exception = new TwitterException("Test Exception Message", null, httpResponse);
        exception.getRetryAfter();
    }
}