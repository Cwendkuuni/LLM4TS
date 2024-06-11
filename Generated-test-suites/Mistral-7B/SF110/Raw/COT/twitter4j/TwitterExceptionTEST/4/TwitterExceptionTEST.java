import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import java.io.IOException;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        IOException cause = new IOException("Test IOException");
        TwitterException exception = new TwitterException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse httpResponse = new HttpResponse(400, "Test response");
        TwitterException exception = new TwitterException("Test message", httpResponse);
        assertEquals(400, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithHttpResponseAndCause() {
        HttpResponse httpResponse = new HttpResponse(400, "Test response");
        IOException cause = new IOException("Test IOException");
        TwitterException exception = new TwitterException("Test message", httpResponse, cause);
        assertEquals(400, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getRetryAfter());
        assertEquals(cause, exception.getCause());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        HttpResponse httpResponse = new HttpResponse(401, "Test response");
        TwitterException exception = new TwitterException("Test message", httpResponse);
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("Test IOException");
        TwitterException exception = new TwitterException("Test message", cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = new HttpResponse(400, "Test response");
        TwitterException exception = new TwitterException("Test message", httpResponse);
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse httpResponse = new HttpResponse(404, "Test response");
        TwitterException exception = new TwitterException("Test message", httpResponse);
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException(400, new IOException("Test IOException1"));
        TwitterException exception2 = new TwitterException(400, new IOException("Test IOException2"));
        TwitterException exception3 = new TwitterException(400, new IOException("Test IOException1"));
        TwitterException exception4 = new TwitterException(401, new IOException("Test IOException1"));

        assertTrue(exception1.equals(exception1));
        assertTrue(exception1.equals(exception3));
        assertFalse(exception1.equals(exception2));
        assertFalse(exception1.equals(exception4));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException(400, new IOException("Test IOException1"));
        TwitterException exception2 = new TwitterException(400, new IOException("Test IOException2"));
        TwitterException exception3 = new TwitterException(400, new IOException("Test IOException1"));
        TwitterException exception4 = new TwitterException(401, new IOException("Test IOException1"));

        int hashCode1 = exception1.hashCode();
        int hashCode2 = exception2.hashCode();
        int hashCode3 = exception3.hashCode();
        int hashCode4 = exception4.hashCode();

        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode4);
    }

}