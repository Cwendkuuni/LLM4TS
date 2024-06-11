package twitter4j;

import static org.junit.Assert.*;
import twitter4j.internal.http.HttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertEquals("Test Exception Message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        IOException cause = new IOException("IO Exception Message");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse httpResponse = new DefaultHttpResponse(403, "Forbidden");
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        assertEquals(403, exception.getStatusCode());
        assertNull(exception.getRetryAfter());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetStatusCode() {
        HttpResponse httpResponse = new DefaultHttpResponse(420, "Rate Limit Exceeded");
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        assertEquals(420, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        HttpResponse httpResponse = new DefaultHttpResponse(420, "Rate Limit Exceeded");
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Rate-Limit-Limit", "100");
        headers.put("X-Rate-Limit-Remaining", "50");
        headers.put("X-Rate-Limit-Reset", "1460972474");
        httpResponse.setHeaders(headers);
        assertEquals(100, exception.getRateLimitStatus().getLimit());
        assertEquals(50, exception.getRateLimitStatus().getRemaining());
        assertEquals(1460972474, exception.getRateLimitStatus().getReset());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        HttpResponse httpResponse = new DefaultHttpResponse(420, "Rate Limit Exceeded");
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Rate-Limit-Limit", "100");
        headers.put("X-Rate-Limit-Remaining", "50");
        headers.put("X-Rate-Limit-Reset", "1460972474");
        headers.put("X-Rate-Limit-Reset-After-1h", "1460973074");
        httpResponse.setHeaders(headers);
        assertEquals("1h", exception.getFeatureSpecificRateLimitStatus().getBucket());
        assertEquals(1460973074, exception.getFeatureSpecificRateLimitStatus().getReset());
    }

    @Test
    public void testGetRetryAfter() {
        HttpResponse httpResponse = new DefaultHttpResponse(420, "Rate Limit Exceeded");
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        Map<String, String> headers = new HashMap<>();
        headers.put("Retry-After", "3600");
        httpResponse.setHeaders(headers);
        assertEquals(3600, exception.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("IO Exception Message");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = new DefaultHttpResponse(400, "Bad Request");
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        assertFalse(exception.exceededRateLimitation());

        httpResponse = new DefaultHttpResponse(420, "Rate Limit Exceeded");
        exception = new TwitterException("Test Exception Message", httpResponse);
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse httpResponse = new DefaultHttpResponse(404, "Not Found");
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        assertTrue(exception.resourceNotFound());

        HttpResponse httpResponse2 = new DefaultHttpResponse(304, "Not Modified");
        TwitterException exception2 = new TwitterException("Test Exception Message", httpResponse2);
        assertFalse(exception2.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertEquals("000000", exception.getExceptionCode());

        exception = new TwitterException("Test Exception Message", new IOException("IO Exception Message"));
        assertEquals("00-01", exception.getExceptionCode());

        HttpResponse httpResponse = new DefaultHttpResponse(400, "Bad Request");
        TwitterException exception3 = new TwitterException("Test Exception Message", httpResponse);
        assertEquals("400000", exception3.getExceptionCode());

        HttpResponse httpResponse2 = new DefaultHttpResponse(420, "Rate Limit Exceeded");
        TwitterException exception4 = new TwitterException("Test Exception Message", httpResponse2);
        assertEquals("420000", exception4.getExceptionCode());
    }
}