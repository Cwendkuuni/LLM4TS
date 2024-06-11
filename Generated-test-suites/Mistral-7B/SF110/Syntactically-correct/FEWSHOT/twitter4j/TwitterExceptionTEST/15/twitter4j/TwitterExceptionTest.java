package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Serializable;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test message");;
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        IOException cause = mock(IOException.class);
        TwitterException exception = new TwitterException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(400);
        TwitterException exception = new TwitterException("Test message", httpResponse);
        assertEquals("Test message" + System.lineSeparator() + getCause(httpResponse), exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        IOException cause = mock(IOException.class);
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(400);
        TwitterException exception = new TwitterException("Test message", cause, 400);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(400, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = mock(IOException.class);
        TwitterException exception = new TwitterException("Test message", cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testIsNotCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(420);
        TwitterException exception = new TwitterException("Test message", httpResponse);
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(404);
        TwitterException exception = new TwitterException("Test message", httpResponse);
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(420);
        TwitterException exception = new TwitterException("Test message", httpResponse);
        assertEquals("02000001", exception.getExceptionCode());
    }

    @Test
    public void testGetExceptionCodeWithCustomFilter() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(420);
        TwitterException exception = new TwitterException("Test message", httpResponse);
        assertEquals("02000001", new ExceptionDiagnosis(exception, new String[]{"twitter4j_custom"}).asHexString());
    }

    @Test
    public void testEquals() {
        HttpResponse httpResponse1 = mock(HttpResponse.class);
        HttpResponse httpResponse2 = mock(HttpResponse.class);
        TwitterException exception1 = new TwitterException("Test message 1", httpResponse1);
        TwitterException exception2 = new TwitterException("Test message 1", httpResponse2);
        TwitterException exception3 = new TwitterException("Test message 2", httpResponse1);

        assertEquals(exception1, exception1);
        assertEquals(exception1, exception1);
        assertEquals(exception1, exception2);
        assertNotEquals(exception1, exception3);
        assertNotEquals(exception3, exception1);
        assertNotEquals(exception1, null);
        assertNotEquals(null, exception1);

        when(httpResponse1.getStatusCode()).thenReturn(420);
        when(httpResponse2.getStatusCode()).thenReturn(400);
        assertNotEquals(exception1, exception2);

        when(httpResponse1.getStatusCode()).thenReturn(420);
        when(httpResponse2.getStatusCode()).thenReturn(420);
        when(httpResponse1.getRetryAfter()).thenReturn(5);
        when(httpResponse2.getRetryAfter()).thenReturn(10);
        assertNotEquals(exception1, exception2);

        when(httpResponse1.getStatusCode()).thenReturn(420);
        when(httpResponse2.getStatusCode()).thenReturn(420);
        when(httpResponse1.getRetryAfter()).thenReturn(5);
        when(httpResponse2.getRetryAfter()).thenReturn(5);
        when(httpResponse1.getRateLimitStatus()).thenReturn(mock(RateLimitStatus.class));
        when(httpResponse2.getRateLimitStatus()).thenReturn(mock(RateLimitStatus.class));
        assertNotEquals(exception1, exception2);

        when(httpResponse1.getStatusCode()).thenReturn(420);
        when(httpResponse2.getStatusCode()).thenReturn(420);
        when(httpResponse1.getRetryAfter()).thenReturn(5);
        when(httpResponse2.getRetryAfter()).thenReturn(5);
        when(httpResponse1.getRateLimitStatus()).thenReturn(null);
        when(httpResponse2.getRateLimitStatus()).thenReturn(null);
        assertNotEquals(exception1, exception2);

        when(httpResponse1.getStatusCode()).thenReturn(420);
        when(httpResponse2.getStatusCode()).thenReturn(420);
        when(httpResponse1.getRetryAfter()).thenReturn(5);
        when(httpResponse2.getRetryAfter()).thenReturn(5);
        when(httpResponse1.getRateLimitStatus()).thenReturn(new RateLimitStatus(1000, 1500, 60000));
        when(httpResponse2.getRateLimitStatus()).thenReturn(new RateLimitStatus(500, 1000, 60000));
        assertNotEquals(exception1, exception2);

        when(httpResponse1.getStatusCode()).thenReturn(420);
        when(httpResponse2.getStatusCode()).thenReturn(420);
        when(httpResponse1.getRetryAfter()).thenReturn(5);
        when(httpResponse2.getRetryAfter()).thenReturn(5);
        when(httpResponse1.getRateLimitStatus()).thenReturn(null);
        when(httpResponse2.getRateLimitStatus()).thenReturn(new RateLimitStatus(500, 1000, 60000));
        assertNotEquals(exception1, exception2);

        when(httpResponse1.getStatusCode()).thenReturn(420);
        when(httpResponse2.getStatusCode()).thenReturn(420);
        when(httpResponse1.getRetryAfter()).thenReturn(5);
        when(httpResponse2.getRetryAfter()).thenReturn(5);
        when(httpResponse1.getRateLimitStatus()).thenReturn(new RateLimitStatus(500, 1000, 60000));
        when(httpResponse2.getRateLimitStatus()).thenReturn(null);
        assertNotEquals(exception1, exception2);

        when(httpResponse1.getStatusCode()).thenReturn(420);
        when(httpResponse2.getStatusCode()).thenReturn(420);
        when(httpResponse1.getRetryAfter()).thenReturn(5);
        when(httpResponse2.getRetryAfter()).thenReturn(5);
        when(httpResponse1.getRateLimitStatus()).thenReturn(mock(RateLimitStatus.class));
        when(httpResponse2.getRateLimitStatus()).thenReturn(mock(RateLimitStatus.class));
        when(httpResponse1.getFeatureSpecificRateLimitStatus()).thenReturn(mock(RateLimitStatus.class));
        when(httpResponse2.getFeatureSpecificRateLimitStatus()).thenReturn(mock(RateLimitStatus.class));
        assertNotEquals(exception1, exception2);

        when(httpResponse1.getStatusCode()).thenReturn(420);
        when(httpResponse2.getStatusCode()).thenReturn(420);
        when(httpResponse1.getRetryAfter()).thenReturn(5);
        when(httpResponse2.getRetryAfter()).thenReturn(5);
        when(httpResponse1.getRateLimitStatus()).thenReturn(new RateLimitStatus(1000, 1500, 60000));
        when(httpResponse2.getRateLimitStatus()).thenReturn(new RateLimitStatus(1000, 1500, 60000));
        when(httpResponse1.getFeatureSpecificRateLimitStatus()).thenReturn(null);
        when(httpResponse2.getFeatureSpecificRateLimitStatus()).thenReturn(null);
        assertEquals(exception1, exception2);

        when(httpResponse1.getStatusCode()).thenReturn(420);
        when(httpResponse2.getStatusCode()).thenReturn(420);
        when(httpResponse1.getRetryAfter()).thenReturn(5);
        when(httpResponse2.getRetryAfter()).thenReturn(5);
        when(httpResponse1.getRateLimitStatus()).thenReturn(new RateLimitStatus(1000, 1500, 60000));
        when(httpResponse2.getRateLimitStatus()).thenReturn(new RateLimitStatus(1000, 1500, 60000));
        when(httpResponse1.getFeatureSpecificRateLimitStatus()).thenReturn(new RateLimitStatus(500, 1000, 60000));
        when(httpResponse2.getFeatureSpecificRateLimitStatus()).thenReturn(new RateLimitStatus(500, 1000, 60000));
        assertEquals(exception1, exception2);

        when(httpResponse1.getStatusCode()).thenReturn(420);
        when(httpResponse2.getStatusCode()).thenReturn(420);
        when(httpResponse1.getRetryAfter()).thenReturn(5);
        when(httpResponse2.getRetryAfter()).thenReturn(5);
        when(httpResponse1.getRateLimitStatus()).thenReturn(new RateLimitStatus(1000, 1500, 60000));
        when(httpResponse2.getRateLimitStatus()).thenReturn(new RateLimitStatus(1000, 1500, 60000));
        when(httpResponse1.getFeatureSpecificRateLimitStatus()).thenReturn(new RateLimitStatus(500, 1000, 60000));
        when(httpResponse2.getFeatureSpecificRateLimitStatus()).thenReturn(new RateLimitStatus(500, 1000, 60000));
        when(httpResponse1.getNestedTwitterException()).thenReturn(null);
        when(httpResponse2.getNestedTwitterException()).thenReturn(null);
        assertEquals(exception1, exception2);

        when(httpResponse1.getStatusCode()).thenReturn(420);
        when(httpResponse2.getStatusCode()).thenReturn(420);
        when(httpResponse1.getRetryAfter()).thenReturn(5);
        when(httpResponse2.getRetryAfter()).thenReturn(5);
        when(httpResponse1.getRateLimitStatus()).thenReturn(new RateLimitStatus(1000, 1500, 60000));
        when(httpResponse2.getRateLimitStatus()).thenReturn(new RateLimitStatus(1000, 1500, 60000));
        when(httpResponse1.getFeatureSpecificRateLimitStatus()).thenReturn(new RateLimitStatus(500, 1000, 60000));
        when(httpResponse2.getFeatureSpecificRateLimitStatus()).thenReturn(new RateLimitStatus(500, 1000, 60000));
        when(httpResponse1.getNestedTwitterException()).thenReturn(new TwitterException("Nested exception"));
        when(httpResponse2.getNestedTwitterException()).thenReturn(new TwitterException("Nested exception"));
        assertEquals(exception1, exception2);
    }

    @Test
    public void testHashCode() {
        HttpResponse httpResponse1 = mock(HttpResponse.class);
        HttpResponse httpResponse2 = mock(HttpResponse.class);
        TwitterException exception1 = new TwitterException("Test message 1", httpResponse1);
        TwitterException exception2 = new TwitterException("Test message 1", httpResponse2);
        TwitterException exception3 = new TwitterException("Test message 2", httpResponse1);

        int hashCode1 = exception1.hashCode();
        int hashCode2 = exception1.hashCode();
        int hashCode3 = exception2.hashCode();
        int hashCode4 = exception3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode1);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode3, hashCode1);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode4, hashCode1);
    }

    @Test
    public void testToString() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        TwitterException exception = new TwitterException("Test message", httpResponse);
        String toString = exception.toString();
        assertTrue(toString.contains("TwitterException{exceptionCode=[02000001], statusCode=420, retryAfter=0, rateLimitStatus=null, version=..."));
    }

    private String getCause(HttpResponse res) {
        return "";
    }
}