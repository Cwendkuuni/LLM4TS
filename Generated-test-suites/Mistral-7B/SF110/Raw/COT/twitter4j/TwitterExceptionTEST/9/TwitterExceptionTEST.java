import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertEquals("Test Exception Message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException("IO Exception Message");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse httpResponse =new HttpResponse();
        httpResponse.setStatusCode(420);
        httpResponse.setResponseHeader("Retry-After", "30");
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        assertEquals(420, exception.getStatusCode());
        assertEquals(30, exception.getRetryAfter());
        assertNotNull(exception.getRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        TwitterException exception = new TwitterException("Test Exception Message");
        exception.setStatusCode(410);
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException ioException = new IOException("IO Exception Message");
        TwitterException twitterException = new TwitterException("Test Exception Message", ioException);
        assertTrue(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse =new HttpResponse();
        httpResponse.setStatusCode(400);
        RateLimitStatus rateLimitStatus = new RateLimitStatus();
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse, rateLimitStatus);
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse httpResponse =new HttpResponse();
        httpResponse.setStatusCode(404);
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception Message 1", new IOException("IO Exception Message 1"), 420, new RateLimitStatus());
        TwitterException exception2 = new TwitterException("Test Exception Message 2", new IOException("IO Exception Message 2"), 420, new RateLimitStatus());
        assertTrue(exception1.equals(exception2));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test Exception Message 1", new IOException("IO Exception Message 1"), 420, new RateLimitStatus());
        TwitterException exception2 = new TwitterException("Test Exception Message 2", new IOException("IO Exception Message 2"), 420, new RateLimitStatus());
        assertEquals(exception1.hashCode(), exception2.hashCode());
    }
}