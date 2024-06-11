import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new Exception("Cause message");
        TwitterException exception = new TwitterException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse httpResponse = new HttpResponse(200, "OK", new byte[0]);
        TwitterException exception = new TwitterException("Test message", httpResponse);
        assertEquals(200, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test message", new HttpResponse(200, "OK", new byte[0]));
        assertEquals(200, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        HttpResponse httpResponse = new HttpResponse(429, "Too Many Requests", new byte[0]);
        TwitterException exception = new TwitterException("Test message", httpResponse);
        RateLimitStatus rateLimitStatus = exception.getRateLimitStatus();
        assertNotNull(rateLimitStatus);
        assertEquals(429, rateLimitStatus.getStatusCode());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        HttpResponse httpResponse = new HttpResponse(429, "Too Many Requests", new byte[0]);
        HttpResponseHeader rateLimitHeader = new HttpResponseHeader("X-Rate-Limit-Limit", "100", "X-Rate-Limit-Remaining", "80", "X-Rate-Limit-Reset", "1482827355");
        httpResponse.setResponseHeaders(new HttpResponseHeader[] { rateLimitHeader });
        TwitterException exception = new TwitterException("Test message", httpResponse);
        RateLimitStatus rateLimitStatus = exception.getRateLimitStatus();
        RateLimitStatus featureSpecificRateLimitStatus = exception.getFeatureSpecificRateLimitStatus();
        assertNotNull(featureSpecificRateLimitStatus);
        assertEquals(100, featureSpecificRateLimitStatus.getLimit());
        assertEquals(80, featureSpecificRateLimitStatus.getRemaining());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test message", new IOException("Test network error"));
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = new HttpResponse(400, "Bad Request", new byte[0]);
        TwitterException exception = new TwitterException("Test message", httpResponse);
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse httpResponse = new HttpResponse(404, "Not Found", new byte[0]);
        TwitterException exception = new TwitterException("Test message", httpResponse);
        assertTrue(exception.resourceNotFound());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        HttpResponse httpResponse = new HttpResponse(200, "OK", new byte[0]);
        TwitterException exception = new TwitterException("Test message", httpResponse);
        exception.getRetryAfter();
    }
}