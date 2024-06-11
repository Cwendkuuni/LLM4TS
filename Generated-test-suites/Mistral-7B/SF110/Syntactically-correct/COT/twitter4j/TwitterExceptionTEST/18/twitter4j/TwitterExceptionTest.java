import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.http.HttpResponse;
import java.io.IOException;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test exception message");
        assertEquals("Test exception message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        IOException ioException = new IOException("IO exception message");
        TwitterException twitterException = new TwitterException("Test exception message", ioException);
        assertEquals("Test exception message", twitterException.getMessage());
        assertEquals(ioException, twitterException.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse response = new HttpResponse(404, new byte[0]);
        TwitterException exception = new TwitterException("Test exception message", response);
        assertEquals(404, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        IOException ioException = new IOException("IO exception message");
        TwitterException exception = new TwitterException("Test exception message", ioException, 404);
        assertEquals("Test exception message", exception.getMessage());
        assertEquals(ioException, exception.getCause());
        assertEquals(404, exception.getStatusCode());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        TwitterException exception = new TwitterException("Test exception message");
        exception.setStatusCode(401);
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException ioException = new IOException("IO exception message");
        TwitterException exception = new TwitterException("Test exception message", ioException);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse response = new HttpResponse(400, new byte[0]);
        TwitterException exception = new TwitterException("Test exception message", response);
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse response = new HttpResponse(404, new byte[0]);
        TwitterException exception = new TwitterException("Test exception message", response);
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test exception message1", new HttpResponse(400, new byte[0]));
        TwitterException exception2 = new TwitterException("Test exception message2", new HttpResponse(400, new byte[0]));
        TwitterException exception3 = new TwitterException("Test exception message1", new HttpResponse(401, new byte[0]));
        assertTrue(exception1.equals(exception1));
        assertTrue(exception1.equals(exception2));
        assertFalse(exception1.equals(exception3));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test exception message1", new HttpResponse(400, new byte[0]));
        TwitterException exception2 = new TwitterException("Test exception message2", new HttpResponse(400, new byte[0]));
        TwitterException exception3 = new TwitterException("Test exception message1", new HttpResponse(401, new byte[0]));
        assertEquals(exception1.hashCode(), exception1.hashCode());
        assertEquals(exception1.hashCode(), exception2.hashCode());
        assertNotEquals(exception1.hashCode(), exception3.hashCode());
    }
}