package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusJSONImpl;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.HttpResponseFactory;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionEmpty() {
        TwitterException e = new TwitterException("Empty exception message");
        assertEquals("Empty exception message", e.getMessage());
        assertNull(e.getStatusCode());
        assertNull(e.getRateLimitStatus());
        assertNull(e.getFeatureSpecificRateLimitStatus());
        assertFalse(e.isCausedByNetworkIssue());
        assertFalse(e.exceededRateLimitation());
        assertFalse(e.resourceNotFound());
        assertNull(e.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionCause() {
        IOException ioe = new IOException("Network error occurred");
        TwitterException e = new TwitterException("Server error message", ioe);
        assertEquals("Server error message" + System.lineSeparator() + "Network error occurred", e.getMessage());
        assertEquals(IOException.class, e.getCause().getClass());
        assertEquals(-1, e.getStatusCode());
        assertNull(e.getRateLimitStatus());
        assertNull(e.getFeatureSpecificRateLimitStatus());
        assertTrue(e.isCausedByNetworkIssue());
        assertFalse(e.exceededRateLimitation());
        assertFalse(e.resourceNotFound());
        assertNull(e.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse res = mock(HttpResponse.class);
        when(res.getStatusCode()).thenReturn(400);
        when(res.getResponseHeader("Retry-After")).thenReturn("120");
        ArgumentCaptor<HttpResponse> argRes = ArgumentCaptor.forClass(HttpResponse.class);

        TwitterException e = new TwitterException("Server error message", null, res);
        assertEquals("Server error message" + System.lineSeparator() + "There was no new data to return.", e.getMessage());
        assertEquals(400, e.getStatusCode());
        assertEquals(new RateLimitStatus(), e.getRateLimitStatus());
        assertEquals(null, e.getFeatureSpecificRateLimitStatus());
        assertEquals(120, e.getRetryAfter());
        assertTrue(e.isCausedByNetworkIssue());
        assertFalse(e.exceededRateLimitation());
        assertFalse(e.resourceNotFound());

        when(res.getStatusCode()).thenReturn(420);
        TwitterException e2 = new TwitterException("Rate limiting exception message", null, res);
        assertEquals("Rate limiting exception message" + System.lineSeparator() + "The number of requests you have made exceeds the quota afforded by your assigned rate limit.", e2.getMessage());
        assertEquals(420, e2.getStatusCode());
        assertEquals(new RateLimitStatus(), e2.getRateLimitStatus());
        assertEquals(new RateLimitStatus(), e2.getFeatureSpecificRateLimitStatus());
        assertEquals(120, e2.getRetryAfter());
        assertFalse(e2.isCausedByNetworkIssue());
        assertTrue(e2.exceededRateLimitation());
        assertFalse(e2.resourceNotFound());

        when(res.getStatusCode()).thenReturn(404);
        TwitterException e3 = new TwitterException("Resource not found exception message", null, res);
        assertEquals("Resource not found exception message" + System.lineSeparator() + "The URI requested is invalid or the resource requested, such as a user, does not exists.", e3.getMessage());
        assertEquals(404, e3.getStatusCode());
        assertNull(e3.getRateLimitStatus());
        assertNull(e3.getFeatureSpecificRateLimitStatus());
        assertNull(e3.getRetryAfter());
        assertFalse(e3.isCausedByNetworkIssue());
        assertFalse(e3.exceededRateLimitation());
        assertTrue(e3.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException e = new TwitterException("Test exception message");
        assertEquals("68746d61333230393236313332303732303837", e.getExceptionCode());

        HttpResponse res = mock(HttpResponse.class);
        TwitterException e2 = new TwitterException("Test exception message", null, res);
        RateLimitStatus rateLimitStatus = new RateLimitStatus();
        rateLimitStatus.setLimit(100);
        rateLimitStatus.setRemaining(50);
        when(res.getResponseHeader("X-Rate-Limit-Limit")).thenReturn("100");
        when(res.getResponseHeader("X-Rate-Limit-Remaining")).thenReturn("50");
        when(res.getResponseHeader("X-Rate-Limit-Reset")).thenReturn("1234567890");
        when(RateLimitStatusJSONImpl.createFromResponseHeader(res)).thenReturn(rateLimitStatus);

        TwitterException e3 = new TwitterException("Test exception message", null, res);
        assertEquals("686572656e636f7433323039323631333230373230383734353637383930", e3.getExceptionCode());
    }
}

class RateLimitStatus {
    private int limit;
    private int remaining;

    public RateLimitStatus() {
        this.limit = 100;
        this.remaining = 100;
    }

    public int getLimit() {
        return limit;
    }

    public int getRemaining() {
        return remaining;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        RateLimitStatus that = (RateLimitStatus)o;
        if (this.limit != that.limit) {
            return false;
        }
        if (this.remaining != that.remaining) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = this.limit;
        result = 31 * result + this.remaining;
        return result;
    }
}

class RateLimitStatusJSONImpl {
    public static RateLimitStatus createFromResponseHeader(HttpResponse res) {
        Map<String, String> headers = new HashMap<>();
        for (String headerName : res.getAllHeaders().keySet()) {
            headers.put(headerName, res.getAllHeaders().get(headerName));
        }
        return createFromResponseHeader(headers);
    }

    public static RateLimitStatus createFeatureSpecificRateLimitStatusFromResponseHeader(HttpResponse res) {
        Map<String, String> headers = new HashMap<>();
        for (String headerName : res.getAllHeaders().keySet()) {
            headers.put(headerName, res.getAllHeaders().get(headerName));
        }
        return createFeatureSpecificRateLimitStatusFromResponseHeader(headers);
    }

    public static RateLimitStatus createFromResponseHeader(Map<String, String> headers) {
        if (headers.containsKey("X-Rate-Limit-Limit")) {
            int limit = Integer.parseInt(headers.get("X-Rate-Limit-Limit"));
            int remaining = Integer.parseInt(headers.get("X-Rate-Limit-Remaining"));
            int reset = Integer.parseInt(headers.get("X-Rate-Limit-Reset"));
            return new RateLimitStatus() {{
                this.limit = limit;
                this.remaining = remaining;
            }};
        }
        return new RateLimitStatus();
    }

    public static RateLimitStatus createFeatureSpecificRateLimitStatusFromResponseHeader(Map<String, String> headers) {
        RateLimitStatus rateLimitStatus = new RateLimitStatus();
        if (headers.containsKey("X-Rate-Limit-Limit-Bursts")) {
            int burst = Integer.parseInt(headers.get("X-Rate-Limit-Limit-Bursts"));
            rateLimitStatus.setBurst(burst);
        }
        if (headers.containsKey("X-Rate-Limit-Remaining-Bursts")) {
            int remainingBursts = Integer.parseInt(headers.get("X-Rate-Limit-Remaining-Bursts"));
            rateLimitStatus.setRemainingBursts(remainingBursts);
        }
        if (headers.containsKey("X-Rate-Limit-Reset-After")) {
            int reset = Integer.parseInt(headers.get("X-Rate-Limit-Reset-After"));
            rateLimitStatus.setReset(reset);
        }
        return rateLimitStatus;
    }
}

class ExceptionDiagnosis {
    private static final String[] FILTER = { "twitter4j" };

    private final TwitterException exception;

    public ExceptionDiagnosis(TwitterException exception, String[] filter) {
        this.exception = exception;
    }

    public ExceptionDiagnosis(TwitterException exception) {
        this(exception, TWITTER4J_FILTER);
    }

    public static String[] TWITTER4J_FILTER = new String[] { "twitter4j" };

    public String asHexString() {
        StackTraceElement[] stackTrace = this.exception.getStackTrace();
        StringBuilder result = new StringBuilder();
        for (StackTraceElement element : stackTrace) {
            if (isApplicableClass(element.getClassName())) {
                result.append(hexDigits(element.getMethodName().hashCode()));
                result.append(hexDigits(element.getClassName().hashCode()));
                break;
            }
        }
        return result.toString();
    }

    private static boolean isApplicableClass(String className) {
        for (String filter : FILTER) {
            if (className.startsWith(filter)) {
                return true;
            }
        }
        return false;
    }

    private static String hexDigits(int num) {
        String hex = Integer.toHexString(num);
        int length = hex.length();
        if (length < 2) {
            return "0" + hex;
        }
        return hex;
    }
}

class HttpResponseFactory {
    public static HttpResponse createHttpResponse(int statusCode, String responseBody) {
        return new HttpResponse() {{
            this.setStatusCode(statusCode);
            this.setResponse(responseBody);
        }};
    }
}