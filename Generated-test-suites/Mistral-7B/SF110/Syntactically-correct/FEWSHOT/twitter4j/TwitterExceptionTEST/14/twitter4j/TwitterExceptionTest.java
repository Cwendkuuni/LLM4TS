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
import org.mockito.Mockito;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test Message");
        assertEquals("Test Message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException("Caused by IOException");
        TwitterException exception = new TwitterException("Test Message", cause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        assertEquals(420, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRateLimitStatus());
        assertEquals(0, exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithHttpResponseAndCause() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        Exception cause = new IOException("Caused by IOException");
        TwitterException exception = new TwitterException("Test Message", httpResponse, cause);
        assertEquals(420, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRateLimitStatus());
        assertEquals(0, exception.getRetryAfter());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithHttpResponseAndCauseAndRateLimitStatus() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        TwitterException exception = new TwitterException("Test Message", httpResponse, rateLimitStatus);
        assertEquals(420, exception.getStatusCode());
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(0, exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithHttpResponseAndCauseAndRateLimitStatusAndFeatureSpecificRateLimitStatus() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        RateLimitStatus featureSpecificRateLimitStatus = mock(RateLimitStatus.class);
        TwitterException exception = new TwitterException("Test Message", httpResponse, rateLimitStatus, featureSpecificRateLimitStatus);
        assertEquals(420, exception.getStatusCode());
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRetryAfter());
    }

    @Test
    public void testGetRetryAfter() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(420);
        when(httpResponse.getResponseHeader("Retry-After")).thenReturn("123");
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        assertEquals(123, exception.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test Message");
        assertFalse(exception.isCausedByNetworkIssue());

        IOException ioe = new IOException("Test IOException");
        TwitterException exceptionWithIoE = new TwitterException("Test Message", ioe);
        assertTrue(exceptionWithIoE.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(400);
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        assertFalse(exception.exceededRateLimitation());

        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        when(rateLimitStatus.getRemainingBuckets()).thenReturn(new HashMap<String, Integer>() {{
            put("0", 0);
            put("1", 0);
            put("2", 0);
        }});
        when(httpResponse.getRateLimitStatus()).thenReturn(rateLimitStatus);
        TwitterException exceptionWithRateLimitStatus = new TwitterException("Test Message", httpResponse);
        assertFalse(exceptionWithRateLimitStatus.exceededRateLimitation());

        when(httpResponse.getStatusCode()).thenReturn(420);
        TwitterException exceptionWithStatusCode = new TwitterException("Test Message", httpResponse);
        assertTrue(exceptionWithStatusCode.exceededRateLimitation());

        when(httpResponse.getStatusCode()).thenReturn(420);
        when(httpResponse.getRateLimitStatus()).thenReturn(rateLimitStatus);
        TwitterException exceptionWithRateLimitStatusAndStatusCode = new TwitterException("Test Message", httpResponse);
        assertTrue(exceptionWithRateLimitStatusAndStatusCode.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test Message", new HttpResponse() {{
            this.statusCode = 404;
        }});
        assertTrue(exception.resourceNotFound());

        TwitterException exceptionWithStatusCode = new TwitterException("Test Message", 404);
        assertTrue(exceptionWithStatusCode.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Message", new HttpResponse() {{
            this.statusCode = 400;
            this.getResponseHeader("X-Twitter-Exception-Code") = "TestCode";
        }});
        assertEquals("TestCode", exception.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test Message");
        exception.setNested();
        assertTrue(exception.isNested());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Message 1");
        TwitterException exception2 = new TwitterException("Test Message 2");
        TwitterException exception3 = new TwitterException("Test Message 1");

        assertFalse(exception1.equals(exception2));
        assertTrue(exception1.equals(exception3));

        TwitterException exception4 = new TwitterException("Test Message 1");
        exception4.setNested();
        assertFalse(exception1.equals(exception4));

        exception1.setNested();
        assertTrue(exception1.equals(exception4));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test Message 1");
        TwitterException exception2 = new TwitterException("Test Message 2");
        TwitterException exception3 = new TwitterException("Test Message 1");

        int hashCode1 = exception1.hashCode();
        int hashCode2 = exception2.hashCode();
        int hashCode3 = exception3.hashCode();

        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode2);

        TwitterException exception4 = new TwitterException("Test Message 1");
        exception4.setNested();

        int hashCode4 = exception1.hashCode();
        int hashCode5 = exception4.hashCode();

        assertNotEquals(hashCode1, hashCode5);
    }
}