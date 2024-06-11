package twitter4j.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import twitter4j.HttpResponse;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusJSONImpl;
import twitter4j.TwitterException;
import twitter4j.internal.http.StatusLine;

public class TwitterExceptionTest {

    private HttpResponse response;
    private TwitterException exception;

    @Before
    public void setUp() {
        response = mock(HttpResponse.class);
        exception = new TwitterException("Test Exception");
    }

    @Test
    public void testTwitterExceptionNullMessage() {
        new TwitterException(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTwitterExceptionNullCause() {
        new TwitterException(null, null);
    }

    @Test
    public void testTwitterExceptionNoArguments() {
        TwitterException e = new TwitterException();
        assertNull(e.getMessage());
        assertNull(e.getCause());
        assertNull(e.getStatusCode());
        assertNull(e.getRateLimitStatus());
        assertNull(e.getFeatureSpecificRateLimitStatus());
        assertFalse(e.isCausedByNetworkIssue());
        assertFalse(e.exceededRateLimitation());
        assertFalse(e.resourceNotFound());
        assertNull(e.getExceptionCode());
        assertEquals(0, e.hashCode());
    }

    @Test
    public void testTwitterExceptionExceptionMessage() {
        String causeMessage = "Internal Server Error";
        Exception cause = new IOException(causeMessage);
        TwitterException e = new TwitterException("Test Exception", cause);
        assertEquals("Test Exception: Internal Server Error", e.getMessage());
        assertEquals(cause, e.getCause());
        assertNull(e.getStatusCode());
        assertNull(e.getRateLimitStatus());
        assertNull(e.getFeatureSpecificRateLimitStatus());
        assertTrue(e.isCausedByNetworkIssue());
        assertFalse(e.exceededRateLimitation());
        assertFalse(e.resourceNotFound());
        assertNull(e.getExceptionCode());
        assertEquals(0, e.hashCode());
    }

    @Test
    public void testTwitterExceptionHttpResponse() {
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(400);
        when(statusLine.getReasonPhrase()).thenReturn("Bad Request");
        when(response.getStatusLine()).thenReturn(statusLine);

        exception.setStatusCode(400);
        exception.setRateLimitStatus(RateLimitStatusJSONImpl.createFromResponseHeader(response));
        exception.setFeatureSpecificRateLimitStatus(RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(response));
        exception.initCause(response);

        assertEquals(400, exception.getStatusCode());
        assertEquals("Bad Request", exception.getMessage());
        assertSame(response, exception.getCause());
        assertSame(RateLimitStatusJSONImpl.createFromResponseHeader(response), exception.getRateLimitStatus());
        assertSame(RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(response), exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
        assertEquals(0, exception.hashCode());
    }

    @Test
    public void testTwitterExceptionHttpResponseAndMessage() {
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(420);
        when(statusLine.getReasonPhrase()).thenReturn("Rate Limit Exceeded");
        when(response.getStatusLine()).thenReturn(statusLine);

        String message = "Test Exception";
        exception.setStatusCode(420);
        exception.setRateLimitStatus(RateLimitStatusJSONImpl.createFromResponseHeader(response));
        exception.setFeatureSpecificRateLimitStatus(RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(response));
        exception.setMessage(message);
        exception.initCause(response);

        assertEquals(message, exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertSame(response, exception.getCause());
        assertSame(RateLimitStatusJSONImpl.createFromResponseHeader(response), exception.getRateLimitStatus());
        assertSame(RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(response), exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
        assertEquals(0, exception.hashCode());

        Map<String, Object> rateLimitStatusMap = new HashMap<String, Object>();
        rateLimitLimitStatusMap.put("limit", 100);
        rateLimitStatusMap.put("remaining", 50);
        exception.setRateLimitStatus(RateLimitStatusJSONImpl.createFromJson(rateLimitStatusMap.toString()));

        assertEquals("limit=100, remaining=50", exception.getExceptionCode());
        assertEquals(420, exception.getStatusCode());
        assertSame(response, exception.getCause());
        assertSame(RateLimitStatusJSONImpl.createFromJson(rateLimitStatusMap.toString()), exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
        assertEquals(1414386546, exception.hashCode()); // different from previous hashCode because of the different rateLimitStatus
    }

    @Test
    public void testTwitterExceptionExceptionMessageAndHttpResponse() {
        String causeMessage = "Internal Server Error";
        Exception cause = new IOException(causeMessage);
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(400);
        when(statusLine.getReasonPhrase()).thenReturn("Bad Request");
        when(response.getStatusLine()).thenReturn(statusLine);

        TwitterException e = new TwitterException("Test Exception", cause);
        e.setStatusCode(400);
        e.setRateLimitStatus(RateLimitStatusJSONImpl.createFromResponseHeader(response));
        e.setFeatureSpecificRateLimitStatus(RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(response));
        e.initCause(response);

        assertEquals("Test Exception: " + causeMessage, e.getMessage());
        assertEquals("Test Exception: Internal Server Error", e.getMessage());
        assertEquals(400, e.getStatusCode());
        assertSame(response, e.getCause());
        assertSame(RateLimitStatusJSONImpl.createFromResponseHeader(response), e.getRateLimitStatus());
        assertSame(RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(response), e.getFeatureSpecificRateLimitStatus());
        assertTrue(e.isCausedByNetworkIssue());
        assertFalse(e.exceededRateLimitation());
        assertFalse(e.resourceNotFound());
        assertNull(e.getExceptionCode());
        assertEquals(0, e.hashCode());
    }

    @Test
    public void testTwitterExceptionExceptionMessageAndHttpResponseWithRateLimitStatus() {
        String causeMessage = "Internal Server Error";
        Exception cause = new IOException(causeMessage);
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(420);
        when(statusLine.getReasonPhrase()).thenReturn("Rate Limit Exceeded");
        when(response.getStatusLine()).thenReturn(statusLine);

        Map<String, Object> rateLimitStatusMap = new HashMap<String, Object>();
        rateLimitStatusMap.put("limit", 100);
        rateLimitStatusMap.put("remaining", 50);
        when(response.getResponseHeader("X-Rate-Limit-Limit")).thenReturn("100");
        when(response.getResponseHeader("X-Rate-Limit-Remaining")).thenReturn("50");
        when(response.getResponseHeader("X-Rate-Limit-Reset")).thenReturn("12345");

        TwitterException e = new TwitterException("Test Exception", cause);
        e.setStatusCode(420);
        e.setRateLimitStatus(RateLimitStatusJSONImpl.createFromResponseHeader(response));
        e.setFeatureSpecificRateLimitStatus(RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(response));
        e.initCause(response);

        assertEquals("Test Exception: " + causeMessage, e.getMessage());
        assertEquals(420, e.getStatusCode());
        assertSame(response, e.getCause());
        assertSame(RateLimitStatusJSONImpl.createFromResponseHeader(response), e.getRateLimitStatus());
        assertSame(RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(response), e.getFeatureSpecificRateLimitStatus());
        assertTrue(e.isCausedByNetworkIssue());
        assertTrue(e.exceededRateLimitation());
        assertTrue(e.resourceNotFound());
        assertEquals("limit=100, remaining=50, reset=12345", e.getExceptionCode());
        assertEquals(1414386546, e.hashCode()); // different from previous hashCode because of the different rateLimitStatus
    }

    @Test
    public void testTwitterExceptionExceptionMessageAndHttpResponseWithNetworkError() {
        String causeMessage = "Internal Server Error";
        Exception cause = new IOException(causeMessage);
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenThrow(new IOException("Network Error"));

        TwitterException e = new TwitterException("Test Exception", cause);
        e.initCause(statusLine);

        assertEquals("Test Exception: " + causeMessage, e.getMessage());
        assertEquals("Test Exception: Internal Server Error", e.getMessage());
        assertEquals(-1, e.getStatusCode());
        assertSame(statusLine, e.getCause());
        assertNull(e.getRateLimitStatus());
        assertNull(e.getFeatureSpecificRateLimitStatus());
        assertTrue(e.isCausedByNetworkIssue());
        assertFalse(e.exceededRateLimitation());
        assertFalse(e.resourceNotFound());
        assertNull(e.getExceptionCode());
        assertEquals(0, e.hashCode());
    }

    @Test
    public void testTwitterExceptionExceptionMessageAndHttpResponseWithNetworkErrorAndRateLimitStatus() {
        String causeMessage = "Internal Server Error";
        Exception cause = new IOException(causeMessage);
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenThrow(new IOException("Network Error"));

        Map<String, Object> rateLimitStatusMap = new HashMap<String, Object>();
        rateLimitStatusMap.put("limit", 100);
        rateLimitStatusMap.put("remaining", 50);
        when(statusLine.getResponseHeaders()).thenReturn(new byte[0]);
        when(response.getResponseBodyAsString()).thenReturn(RateLimitStatusJSONImpl.toJson(rateLimitStatusMap).toString());

        TwitterException e = new TwitterException("Test Exception", cause);
        e.initCause(statusLine);

        assertEquals("Test Exception: " + causeMessage, e.getMessage());
        assertEquals("Test Exception: Internal Server Error", e.getMessage());
        assertEquals(-1, e.getStatusCode());
        assertSame(statusLine, e.getCause());
        assertSame(RateLimitStatusJSONImpl.createFromJson(rateLimitStatusMap.toString()), e.getRateLimitStatus());
        assertNull(e.getFeatureSpecificRateLimitStatus());
        assertTrue(e.isCausedByNetworkIssue());
        assertTrue(e.exceededRateLimitation());
        assertTrue(e.resourceNotFound());
        assertEquals("limit=100, remaining=50", e.getExceptionCode());
        assertEquals(1414386546, e.hashCode()); // different from previous hashCode because of the different rateLimitStatus
    }
}