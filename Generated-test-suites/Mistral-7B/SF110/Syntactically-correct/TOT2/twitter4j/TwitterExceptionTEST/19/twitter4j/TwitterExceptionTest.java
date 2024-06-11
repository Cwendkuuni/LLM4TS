import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.HttpResponse;
import twitter4j.TwitterException;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionDefaultConstructor() {
        TwitterException exception = new TwitterException("Default constructor");
        assertEquals("Default constructor", exception.getMessage());
        assertNull(exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithStringMessage() {
        TwitterException exception = new TwitterException("Test exception with string message");
        assertEquals("Test exception with string message", exception.getMessage());
        assertNull(exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithExceptionCause() {
        Exception cause = new Exception("Test exception with exception cause");
        TwitterException exception = new TwitterException("Test exception with exception cause", cause);
        assertEquals("Test exception with exception cause", exception.getMessage());
        assertNull(exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isNested());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse res = new HttpResponse(400, new byte[0]);
        TwitterException exception = new TwitterException("Test exception with HttpResponse", res);
        assertEquals("Test exception with HttpResponse", exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isNested());
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testTwitterExceptionWithNullHttpResponse() {
        TwitterException exception = new TwitterException("Test exception with null HttpResponse");
        assertNull(exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isNested());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertFalse(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testTwitterExceptionWithNegativeStatusCode() {
        TwitterException exception = new TwitterException("Negative status code", new HttpResponse(-1, new byte[0]));
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertFalse(exception.isCausedByNetworkIssue());
        assertEquals(-1, exception.getStatusCode());
    }

    @Test
    public void testTwitterExceptionWithBothRateLimitStatusAndFeatureSpecificRateLimitStatus() {
        HttpResponse res = new HttpResponse(420, new byte[0]);
        TwitterException exception = new TwitterException("Test exception", res);
        assertTrue(exception.exceededRateLimitation());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithIOExceptionCause() {
        IOException ioException = new IOException("IOException test");
        TwitterException exception = new TwitterException("Test exception", ioException);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testTwitterExceptionEquality() {
        TwitterException exception1 = new TwitterException("Test exception 1", new HttpResponse(400, new byte[0]));
        TwitterException exception2 = new TwitterException("Test exception 2", new HttpResponse(400, new byte[0]));
        TwitterException exception3 = new TwitterException("Test exception 3", new HttpResponse(401, new byte[0]));
        assertEquals(exception1, exception1);
        assertEquals(exception1, exception1);
        assertNotEquals(exception1, exception2);
        assertNotEquals(exception1, exception3);
        assertEquals(exception2, exception2);
        assertEquals(exception2, exception2);
        assertNotEquals(exception2, exception1);
        assertNotEquals(exception2, exception3);
        assertFalse(exception1.equals(null));
    }

    @Test
    public void testTwitterExceptionHashCode() {
        TwitterException exception1 = new TwitterException("Test exception 1", new HttpResponse(400, new byte[0]));
        TwitterException exception2 = new TwitterException("Test exception 2", new HttpResponse(400, new byte[0]));
        TwitterException exception3 = new TwitterException("Test exception 3", new HttpResponse(401, new byte[0]));
        int hashCode1 = exception1.hashCode();
        int hashCode2 = exception1.hashCode();
        int hashCode3 = exception1.hashCode();
        int hashCode4 = exception2.hashCode();
        int hashCode5 = exception3.hashCode();
        assertFalse(hashCode1 == hashCode2);
        assertFalse(hashCode1 == hashCode3);
        assertFalse(hashCode2 == hashCode3);
        assertFalse(hashCode1 == hashCode4);
        assertFalse(hashCode1 == hashCode5);
        assertFalse(hashCode2 == hashCode4);
        assertFalse(hashCode2 == hashCode5);
        assertFalse(hashCode3 == hashCode4);
        assertFalse(hashCode3 == hashCode5);
    }
}