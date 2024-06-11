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
        Exception cause = new IOException();
        TwitterException exception = new TwitterException("Test message", cause);
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse response = new HttpResponse(420, "Retry-After: 120");
        TwitterException exception = new TwitterException("Test message", response);
        assertEquals(420, exception.getStatusCode());
        assertEquals(120, exception.getRetryAfter());
        assertNotNull(exception.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        String message = "Test message";
        Exception cause = new IOException();
        int statusCode = 420;
        TwitterException exception = new TwitterException(message, cause, statusCode);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWithoutStatusCode420() {
        TwitterException exception = new TwitterException("Test message");
        exception.getRetryAfter();
    }

    @Test(expected = IllegalStateException.class)
    public void testIsCausedByNetworkIssueWithNonIOExceptionCause() {
        TwitterException exception = new TwitterException("Test message", new NullPointerException());
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitationWithoutRateLimitStatus() {
        TwitterException exception = new TwitterException("Test message", new HttpResponse(400));
        assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testExceededRateLimitationWithRateLimitStatus() {
        TwitterException exception = new TwitterException("Test message", new HttpResponse(420));
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test message", new HttpResponse(404));
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test message 1", new HttpResponse(420));
        TwitterException exception2 = new TwitterException("Test message 2", new HttpResponse(420));
        TwitterException exception3 = new TwitterException("Test message 1", new HttpResponse(421));
        assertTrue(exception1.equals(exception1));
        assertTrue(exception1.equals(exception2));
        assertFalse(exception1.equals(exception3));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test message 1", new HttpResponse(420));
        TwitterException exception2 = new TwitterException("Test message 2", new HttpResponse(420));
        TwitterException exception3 = new TwitterException("Test message 1", new HttpResponse(421));
        int hashCode1 = exception1.hashCode();
        int hashCode2 = exception2.hashCode();
        int hashCode3 = exception3.hashCode();
        assertNotSame(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test message", new HttpResponse(420));
        String expected = "TwitterException{statusCode=420, retryAfter=120, rateLimitStatus=RateLimitStatus{}, version=}";
        String actual = exception.toString();
        assertEquals(expected, actual);
    }
}