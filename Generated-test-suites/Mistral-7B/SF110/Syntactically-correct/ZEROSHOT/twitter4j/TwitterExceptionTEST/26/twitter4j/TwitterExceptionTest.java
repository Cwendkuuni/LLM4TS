package twitter4j.test;

import static org.junit.Assert.*;

import twitter4j.TwitterException;
import twitter4j.internal.http.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        String msg = "Test Exception";
        TwitterException exception = new TwitterException(msg);
        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        IOException cause = new IOException("Test IOException");
        TwitterException exception = new TwitterException("Test Exception", cause);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getRetryAfter());
        assertEquals(cause, exception.getCause());
        assertTrue(exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        int statusCode = 400;
        RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 30);
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus(5, 60);
        HttpResponse response = new HttpResponse(statusCode, null, new ByteArrayInputStream("{}".getBytes()));

        TwitterException exception = new TwitterException("Test Exception", response);
        assertEquals("Test Exception: " + getCause(response), exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals(-1, exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
        int statusCode = 400;
        RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 30);
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus(5, 60);
        IOException cause = new IOException("Test IOException");
        HttpResponse response = new HttpResponse(statusCode, null, new ByteArrayInputStream("{}".getBytes()));

        TwitterException exception = new TwitterException("Test Exception", cause, statusCode);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getRetryAfter());
        assertEquals(cause, exception.getCause());
        assertTrue(exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithMessageHttpResponseAndRateLimitStatus() {
        int statusCode = 400;
        RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 30);
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus(5, 60);
        HttpResponse response = new HttpResponse(statusCode, null, new ByteArrayInputStream("{}".getBytes()));

        TwitterException exception = new TwitterException("Test Exception", statusCode, rateLimitStatus);
        assertEquals("Test Exception: " + getCause(response), exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals(-1, exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithMessageHttpResponseRateLimitStatusAndFeatureSpecificRateLimitStatus() {
        int statusCode = 400;
        RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 30);
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus(5, 60);
        HttpResponse response = new HttpResponse(statusCode, null, new ByteArrayInputStream("{ \"rate_limit_status\": " + rateLimitStatus.toJSON() + ", \"errors\": [ { \"code\": 17, \"message\": \"You are a suspended user.\" } ] }".getBytes()));

        TwitterException exception = new TwitterException("Test Exception", statusCode, rateLimitStatus, featureSpecificRateLimitStatus);
        assertEquals("Test Exception: " + getCause(response), exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals(-1, exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithMessageHttpResponseRateLimitStatusAndFeatureSpecificRateLimitStatusNull() {
        int statusCode = 400;
        RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 30);
        HttpResponse response = new HttpResponse(statusCode, null, new ByteArrayInputStream("{ \"rate_limit_status\": " + rateLimitStatus.toJSON() + "}".getBytes()));

        TwitterException exception = new TwitterException("Test Exception", statusCode, rateLimitStatus, null);
        assertEquals("Test Exception: " + getCause(response), exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals(-1, exception.getRetryAfter());
    }

    @Test
    public void testGetCause() {
        HttpResponse response = new HttpResponse(400, null, new ByteArrayInputStream("{}".getBytes()));
        String cause = getCause(response);
        String expectedCause = "400: The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
        assertEquals(expectedCause, cause);
    }

    private String getCause(HttpResponse response) {
        int statusCode = response.getStatusCode();
        String cause = null;
        switch (statusCode) {
            case 304: {
                cause = "There was no new data to return.";
                break;
            }
            case 400: {
                cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
                break;
            }
            case 401: {
                cause = "Authentication credentials were missing or incorrect.";
                break;
            }
            case 403: {
                cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
                break;
            }
            case 404: {
                cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
                break;
            }
            case 406: {
                cause = "Returned by the Search API when an invalid format is specified in the request.";
                break;
            }
            case 420: {
                cause = "The number of requests you have made exceeds the quota afforded by your assigned rate limit.";
                break;
            }
            case 500: {
                cause = "Something is broken.  Please post to the group so the Twitter team can investigate.";
                break;
            }
            case 502: {
                cause = "Twitter is down or being upgraded.";
                break;
            }
            case 503: {
                cause = "Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
                break;
            }
            default: {
                cause = "";
                break;
            }
        }
        return statusCode + ":" + cause;
    }
}