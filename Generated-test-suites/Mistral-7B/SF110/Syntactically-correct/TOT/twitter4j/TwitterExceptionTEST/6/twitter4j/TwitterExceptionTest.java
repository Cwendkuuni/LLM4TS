import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test Message");
        assertEquals("Test Message", exception.getMessage());
        assertNull(exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getRetryAfter());
        assertFalse(exception.nested);
    }

    @Test
    public void testTwitterExceptionWithCause() {
        IOException cause = new IOException("Test IOException");
        TwitterException exception = new TwitterException("Test Message", cause);
        assertEquals("Test Message: Test IOException", exception.getMessage());
        assertNull(exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getRetryAfter());
        assertFalse(exception.nested);
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(400);
        TwitterException exception = new TwitterException("Test Message", res);
        assertEquals("Test Message: There was an error: [400: The request was invalid]", exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getRetryAfter());
        assertFalse(exception.nested);
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        TwitterException causeException = new TwitterException("Cause Message", res);
        IOException cause = new IOException("Test IOException");
        TwitterException exception = new TwitterException("Test Message", causeException);
        assertEquals("Test Message: There was an error: [420: The number of requests you have made exceeds the quota afforded by your assigned rate limit] Cause Message", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals(-1, exception.getRetryAfter());
        assertFalse(exception.nested);
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Message 1");
        TwitterException exception2 = new TwitterException("Test Message 2");
        assertFalse(exception1.equals(exception2));

        TwitterException exception3 = new TwitterException("Test Message 1");
        assertTrue(exception1.equals(exception3));

        TwitterException exception4 = new TwitterException("Test Message 1");
        exception4.setNested(true);
        assertFalse(exception1.equals(exception4));

        TwitterException exception5 = new TwitterException("Test Message 1");
        exception5.setStatusCode(400);
        assertFalse(exception1.equals(exception5));

        TwitterException exception6 = new TwitterException("Test Message 1");
        exception6.setRetryAfter(10);
        assertFalse(exception1.equals(exception6));

        TwitterException exception7 = new TwitterException("Test Message 1");
        exception7.setRateLimitStatus(new RateLimitStatus(10, 100));
        assertFalse(exception1.equals(exception7));
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Message");
        String exceptionCode = exception.getExceptionCode();
        assertNotNull(exceptionCode);
        assertEquals("0000000001010101", exceptionCode);
    }
}