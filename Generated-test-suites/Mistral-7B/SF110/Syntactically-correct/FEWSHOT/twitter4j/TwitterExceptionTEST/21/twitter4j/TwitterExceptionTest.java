package twitter4j;

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
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse res = new HttpResponse(403, "Forbidden", new byte[0]);
        TwitterException exception = new TwitterException("Test Exception Message", res);
        assertEquals(403, exception.getStatusCode());
        assertEquals("Forbidden", res.getReasonPhrase());
        assertNotNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithHttpResponseAndCause() {
        HttpResponse res = new HttpResponse(403, "Forbidden", new byte[0]);
        Exception cause = new IOException("Test IOException Message");
        TwitterException exception = new TwitterException("Test Exception Message", res, cause);
        assertEquals(403, exception.getStatusCode());
        assertEquals("Forbidden", res.getReasonPhrase());
        assertNotNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRetryAfter());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithHttpResponseCodeAndMessage() {
        TwitterException exception = new TwitterException("Test Exception Message", 403);
        assertEquals(403, exception.getStatusCode());
        assertNull(exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithHttpResponseCodeMessageAndCause() {
        Exception cause = new IOException("Test IOException Message");
        TwitterException exception = new TwitterException("Test Exception Message", 403, cause);
        assertEquals(403, exception.getStatusCode());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("Test IOException Message");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse res = new HttpResponse(420, "Rate Limit Exceeded", new byte[0]);
        TwitterException exception = new TwitterException("Test Exception Message", res);
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse res = new HttpResponse(404, "Not Found", new byte[0]);
        TwitterException exception = new TwitterException("Test Exception Message", res);
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Exception Message", new IOException("Test IOException Message"));
        String exceptionCode = exception.getExceptionCode();
        assertNotNull(exceptionCode);
        assertEquals(exceptionCode.length(), 8);
    }

    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test Exception Message");
        exception.setNested();
        assertTrue(exception.isNested());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception Message 1", 403);
        TwitterException exception2 = new TwitterException("Test Exception Message 2", 403);
        TwitterException exception3 = new TwitterException("Test Exception Message 1", 404);
        TwitterException exception4 = new TwitterException("Test Exception Message 1", 403, new IOException("Test IOException Message"));
        TwitterException exception5 = new TwitterException("Test Exception Message 1", new IOException("Test IOException Message"));

        assertFalse(exception1.equals(exception2));
        assertFalse(exception1.equals(exception3));
        assertFalse(exception1.equals(exception4));
        assertFalse(exception1.equals(exception5));
        assertFalse(exception1.equals(null));
        assertFalse(exception1.equals(""));

        TwitterException exception6 = new TwitterException("Test Exception Message 1", 403, exception1);
        assertTrue(exception1.equals(exception6));
    }
}