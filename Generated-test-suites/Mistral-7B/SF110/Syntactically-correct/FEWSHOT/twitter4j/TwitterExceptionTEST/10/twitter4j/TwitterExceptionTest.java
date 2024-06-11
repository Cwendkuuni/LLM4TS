package twitter4j; // Make sure to import necessary classes

import static org.junit.Assert.*;
import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterException() {
        TwitterException exception = new TwitterException("Test Message");
        assertEquals("Test Message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isNested());

        exception = new TwitterException(new Exception("Inner Exception"));
        assertEquals("Inner Exception", exception.getCause().getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isNested());

        exception.setNested();
        TwitterException nestedException = new TwitterException("Nested Test Message", exception);
        assertEquals("Nested Test Message", nestedException.getMessage());
        assertEquals("Inner Exception", nestedException.getCause().getMessage());
        assertEquals(-1, nestedException.getStatusCode());
        assertNull(nestedException.getRateLimitStatus());
        assertNull(nestedException.getFeatureSpecificRateLimitStatus());
        assertTrue(nestedException.isNested());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse httpResponse = new DefaultHttpResponse(420, "Rate Limit Exceeded");
        String retryAfterHeader = "7200";
        httpResponse.headers().set("Retry-After", retryAfterHeader);

        TwitterException exception = new TwitterException("Test Message", httpResponse);
        assertEquals("Test Message" + System.lineSeparator() + "Rate Limit Exceeded", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertEquals("7200", exception.getRetryAfter());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isNested());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Message");
        String exceptionCode = exception.getExceptionCode();
        assertEquals("twitter4j", exceptionCode.substring(0, exceptionCode.indexOf(':')));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        TwitterException exception = new TwitterException("Test Message", new DefaultHttpResponse(200, "OK"));
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test Message");
        assertFalse(exception.isCausedByNetworkIssue());

        IOException networkIssue = new IOException("Network Issue");
        TwitterException exceptionWithNetworkIssue = new TwitterException("Test Message", networkIssue);
        assertTrue(exceptionWithNetworkIssue.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = new DefaultHttpResponse(400, "Bad Request");
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        assertFalse(exception.exceededRateLimitation());

        httpResponse = new DefaultHttpResponse(420, "Rate Limit Exceeded");
        exception = new TwitterException("Test Message", httpResponse);
        assertTrue(exception.exceededRateLimitation());

        httpResponse = new DefaultHttpResponse(400, "Bad Request");
        exception = new TwitterException("Test Message", httpResponse, new TwitterRateLimitStatus());
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test Message", new DefaultHttpResponse(404, "Not Found"));
        assertTrue(exception.resourceNotFound());

        exception = new TwitterException("Test Message", new DefaultHttpResponse(500, "Internal Server Error"));
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Message");
        String expected = "TwitterException{statusCode=[-1], retryAfter=[-1], rateLimitStatus=null, version=}";
        assertEquals(expected, exception.toString());

        HttpResponse httpResponse = new DefaultHttpResponse(420, "Rate Limit Exceeded");
        exception = new TwitterException("Test Message", httpResponse);
        expected = "TwitterException{exceptionCode=[twitter4j], statusCode=420, retryAfter=7200, rateLimitStatus=RateLimitStatus{}, version=}";
        assertEquals(expected, exception.toString());
    }
}