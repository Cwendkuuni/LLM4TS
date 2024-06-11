package twitter4j;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Exception message");
        assertEquals("Exception message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRetryAfter());
        assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = mock(Exception.class);
        TwitterException exception = new TwitterException("Exception message", cause);
        assertEquals("Exception message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(cause, exception.getCause());
        assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        int statusCode = 429;
        when(httpResponse.getStatusCode()).thenReturn(statusCode);
        String retryAfterHeader = "120";
        when(httpResponse.getResponseHeader("Retry-After")).thenReturn(retryAfterHeader);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        RateLimitStatus featureSpecificRateLimitStatus = mock(RateLimitStatus.class);
        TwitterException exception = new TwitterException("Exception message", null, httpResponse);
        assertEquals("Exception message" + System.lineSeparator() + getCause(httpResponse), exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
        assertEquals(Integer.valueOf(120), exception.getRetryAfter());
        assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithMessageAndCauseAndHttpResponse() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        int statusCode = 429;
        when(httpResponse.getStatusCode()).thenReturn(statusCode);
        String retryAfterHeader = "120";
        when(httpResponse.getResponseHeader("Retry-After")).thenReturn(retryAfterHeader);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        RateLimitStatus featureSpecificRateLimitStatus = mock(RateLimitStatus.class);
        Exception cause = mock(Exception.class);
        TwitterException exception = new TwitterException("Exception message", cause, httpResponse);
        assertEquals("Exception message", exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
        assertEquals(Integer.valueOf(120), exception.getRetryAfter());
        assertEquals(cause, exception.getCause());
        assertTrue(exception.isNested());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException ioException = new IOException("Network error");
        TwitterException exception = new TwitterException("Exception message", ioException);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(400);
        TwitterException exception = new TwitterException("Exception message", null, httpResponse);
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(404);
        TwitterException exception = new TwitterException("Exception message", null, httpResponse);
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Exception message 1", null);
        TwitterException exception2 = new TwitterException("Exception message 2", null);
        TwitterException exception3 = new TwitterException("Exception message 1", null);
        TwitterException exception4 = new TwitterException("Exception message 1", new IOException("Network error"));
        TwitterException exception5 = new TwitterException("Exception message 1", null, new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        });
        TwitterException exception6 = new TwitterException("Exception message 1", new IOException("Network error"), 420);
        assertEquals(exception1, exception1);
        assertEquals(exception1, exception3);
        assertNotEquals(exception1, exception2);
        assertNotEquals(exception1, exception4);
        assertNotEquals(exception1, exception5);
        assertNotEquals(exception1, exception6);
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Exception message 1", null);
        TwitterException exception2 = new TwitterException("Exception message 2", null);
        TwitterException exception3 = new TwitterException("Exception message 1", null);
        TwitterException exception4 = new TwitterException("Exception message 1", new IOException("Network error"));
        TwitterException exception5 = new TwitterException("Exception message 1", null, new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        });
        TwitterException exception6 = new TwitterException("Exception message 1", new IOException("Network error"), 420);
        int hashCode1 = exception1.hashCode();
        int hashCode2 = exception2.hashCode();
        int hashCode3 = exception3.hashCode();
        int hashCode4 = exception4.hashCode();
        int hashCode5 = exception5.hashCode();
        int hashCode6 = exception6.hashCode();
        assertNotEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode5);
        assertNotEquals(hashCode1, hashCode6);
    }

    private static String getCause(HttpResponse res) {
        int statusCode = res.getStatusCode();
        String cause = "";
        switch (statusCode) {
            case 304:
                cause = "There was no new data to return.";
                break;
            case 400:
                cause = "The request was invalid. An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
                break;
            case 401:
                cause = "Authentication credentials were missing or incorrect.";
                break;
            case 403:
                cause = "The request is understood, but it has been refused. An accompanying error message will explain why.";
                break;
            case 404:
                cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
                break;
            case 406:
                cause = "Returned by the Search API when an invalid format is specified in the request.";
                break;
            case 420:
                cause = "The number of requests you have made exceeds the quota afforded by your assigned rate limit.";
                break;
            case 500:
                cause = "Something is broken. Please post to the group so the Twitter team can investigate.";
                break;
            case 502:
                cause = "Twitter is down or being upgraded.";
                break;
            case 503:
                cause = "Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
                break;
            default:
                cause = "";
                break;
        }
        return statusCode + ":" + cause;
    }
}