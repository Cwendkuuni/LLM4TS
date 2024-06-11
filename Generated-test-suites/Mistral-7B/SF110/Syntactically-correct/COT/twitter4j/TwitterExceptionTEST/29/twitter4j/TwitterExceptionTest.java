import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals("Test Exception", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException();
        TwitterException exception = new TwitterException("Test Exception", cause);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse response = new HttpResponse(400, "Bad Request");
        TwitterException exception = new TwitterException("Test Exception", response);
        assertEquals("Test Exception\n" + response.getStatusCode() + ": The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.", exception.getMessage());
        assertEquals(response.getStatusCode(), exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test Exception", new HttpResponse(400, "Bad Request"));
        assertEquals(400, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        HttpResponse response = new HttpResponse(420, "Too Many Requests");
        TwitterException exception = new TwitterException("Test Exception", response);
        assertNotNull(exception.getRateLimitStatus());
        assertEquals("rate_limit_status", exception.getRateLimitStatus().getResource());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        HttpResponse response = new HttpResponse(420, "Too Many Requests");
        TwitterException exception = new TwitterException("Test Exception", response);
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals("feature_specific_rate_limit_status", exception.getFeatureSpecificRateLimitStatus().getResource());
    }

    @Test
    public void testGetRetryAfter() {
        HttpResponse response = new HttpResponse(420, "Too Many Requests", "Retry-After: 120");
        TwitterException exception = new TwitterException("Test Exception", response);
        assertEquals(120, exception.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        HttpResponse response = new HttpResponse(400, "Bad Request");
        TwitterException exception = new TwitterException("Test Exception", response);
        assertFalse(exception.isCausedByNetworkIssue());

        IOException cause = new IOException();
        exception = new TwitterException("Test Exception", cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse response = new HttpResponse(400, "Rate Limit Exceeded");
        TwitterException exception = new TwitterException("Test Exception", response);
        assertTrue(exception.exceededRateLimitation());

        response = new HttpResponse(404, "Not Found");
        exception = new TwitterException("Test Exception", response);
        assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse response = new HttpResponse(404, "Not Found");
        TwitterException exception = new TwitterException("Test Exception", response);
        assertTrue(exception.resourceNotFound());

        response = new HttpResponse(400, "Bad Request");
        exception = new TwitterException("Test Exception", response);
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception 1", new HttpResponse(400, "Bad Request 1"));
        TwitterException exception2 = new TwitterException("Test Exception 1", new HttpResponse(400, "Bad Request 2"));
        assertFalse(exception1.equals(exception2));

        exception1 = new TwitterException("Test Exception 1", new HttpResponse(400, "Bad Request 1"));
        exception2 = new TwitterException("Test Exception 1", new HttpResponse(400, "Bad Request 1"));
        assertTrue(exception1.equals(exception2));
    }
}