package twitter4j; // Make sure to import necessary packages

import static org.junit.Assert.*;
import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertEquals("Test Exception Message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException("Test IOException Message");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse httpResponse = createMockHttpResponse(400, "Bad Request");
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        assertEquals("Test Exception Message" + System.lineSeparator() + getCause(httpResponse), exception.getMessage());
        assertEquals(400, exception.getStatusCode());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        Exception cause = new IOException("Test IOException Message");
        TwitterException exception = new TwitterException("Test Exception Message", cause, 400);
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(400, exception.getStatusCode());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test Exception Message", createMockHttpResponse(400, "Bad Request"));
        assertEquals(400, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        HttpResponse httpResponse = createMockHttpResponse(420, "Rate Limit Exceeded", getRateLimitResponseHeader());
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        assertEquals(RateLimitStatus.LIMIT_RATE_LIMIT, exception.getRateLimitStatus().getLimit());
        assertEquals(RateLimitStatus.LIMIT_RATE_LIMIT, exception.getRateLimitStatus().getLimitRemaining());
        assertEquals(RateLimitStatus.LIMIT_RATE_LIMIT, exception.getRateLimitStatus().getLimitReset());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        HttpResponse httpResponse = createMockHttpResponse(420, "Rate Limit Exceeded", getFeatureSpecificRateLimitResponseHeader());
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        assertEquals(15, exception.getFeatureSpecificRateLimitStatus().getSeconds());
        assertEquals(18, exception.getFeatureSpecificRateLimitStatus().getMinutes());
        assertEquals(240, exception.getFeatureSpecificRateLimitStatus().getHours());
        assertEquals(720, exception.getFeatureSpecificRateLimitStatus().getMinutesRemaining());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test Exception Message", createMockHttpResponse(420, "Rate Limit Exceeded", "Retry-After: 60"));
        assertEquals(60, exception.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("Test IOException Message");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        assertTrue(exception.isCausedByNetworkIssue());

        exception = new TwitterException("Test Exception Message");
        assertFalse(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception1 = new TwitterException("Test Exception Message", createMockHttpResponse(400, "Bad Request"));
        assertFalse(exception1.exceededRateLimitation());

        TwitterException exception2 = new TwitterException("Test Exception Message", createMockHttpResponse(420, "Rate Limit Exceeded"));
        assertTrue(exception2.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test Exception Message", createMockHttpResponse(404, "Not Found"));
        assertTrue(exception.resourceNotFound());

        exception = new TwitterException("Test Exception Message", createMockHttpResponse(400, "Bad Request"));
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Exception Message", createMockHttpResponse(400, "Bad Request"));
        assertEquals("twitter4j:400:Bad Request", exception.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException exception1 = new TwitterException("Test Exception Message");
        assertFalse(exception1.isNested());

        exception1.setNested();
        assertTrue(exception1.isNested());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception Message");
        TwitterException exception2 = new TwitterException("Test Exception Message");
        TwitterException exception3 = new TwitterException("Test Exception Message", new IOException("Test IOException Message"));
        TwitterException nestedException = new TwitterException("Test Nested Exception Message", exception3);

        assertTrue(exception1.equals(exception1));
        assertTrue(exception1.equals(exception2));
        assertFalse(exception1.equals(null));
        assertFalse(exception1.equals(new Object()));
        assertFalse(exception1.equals(new Integer(1)));
        assertFalse(exception1.equals(42));
        assertFalse(exception1.equals(exception3));
        assertFalse(exception1.equals(nestedException));

        TwitterException exception4 = new TwitterException("Test Exception Message");
        exception4.setNested();
        assertTrue(exception1.equals(exception4));
    }

    private HttpResponse createMockHttpResponse(int statusCode, String reasonPhrase) {
        return new DefaultHttpResponse(new BasicHttpEntityEnclosingRequest(HttpMethod.GET, ""), statusCode, reasonPhrase);
    }

    private static Header[] getRateLimitResponseHeader() {
        return new Header[] {
            new BasicHeader("X-Rate-Limit-Limit", "15"),
            new BasicHeader("X-Rate-Limit-Remaining", "13"),
            new BasicHeader("X-Rate-Limit-Reset", "1350525975")
        };
    }

    private static Header[] getFeatureSpecificRateLimitResponseHeader() {
        return new Header[] {
            new BasicHeader("X-Rate-Limit-Limit", "15"),
            new BasicHeader("X-Rate-Limit-Remaining", "13"),
            new BasicHeader("X-Rate-Limit-Reset", "1350525975"),
            new BasicHeader("X-Twitter-Trends-Limit", "1"),
            new BasicHeader("X-Twitter-Trends-Remaining", "0"),
            new BasicHeader("X-Twitter-Trends-Reset", "1361232983")
        };
    }
}