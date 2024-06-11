package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;

public class TwitterExceptionTest {

    @Test
    public void testTwitterException() {
        TwitterException twitterException = new TwitterException("Test Message");
        assertEquals("Test Message", twitterException.getMessage());
    }

    // Test for negative status code
    @Test
    public void testNegativeStatusCode() {
        TwitterException twitterException = new TwitterException("Negative Status Code Test");
        twitterException.setStatusCode(-1);
        assertEquals(-1, twitterException.getStatusCode());
    }

    // Test for null rateLimitStatus
    @Test
    public void testNullRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test Message");
        assertNull(twitterException.getRateLimitStatus());
    }

    // Test for null featureSpecificRateLimitStatus
    @Test
    public void testNullFeatureSpecificRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test Message");
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    // Test for retryAfter with negative value
    @Test
    public void testNegativeRetryAfter() {
        TwitterException twitterException = new TwitterException("Negative Retry After Test");
        twitterException.setRetryAfter(-1);
        assertEquals(-1, twitterException.getRetryAfter());
    }

    // Test for isCausedByNetworkIssue() with various types of exceptions
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException twitterException = new TwitterException("Test Message");
        IOException ioException = new IOException("Network Exception");
        twitterException.setCause(ioException);
        assertTrue(twitterException.isCausedByNetworkIssue());

        Exception exception = new Exception("Non-network Exception");
        twitterException.setCause(exception);
        assertFalse(twitterException.isCausedByNetworkIssue());
    }

    // Test for exceededRateLimitation() with different status codes
    @Test
    public void testExceededRateLimitation() {
        TwitterException twitterException1 = new TwitterException("Test Message 1", new HttpResponse(400, new byte[0]));
        assertFalse(twitterException1.exceededRateLimitation());

        TwitterException twitterException2 = new TwitterException("Test Message 2", new HttpResponse(420, new byte[0]));
        assertTrue(twitterException2.exceededRateLimitation());
    }

    // Test for resourceNotFound() with different status codes
    @Test
    public void testResourceNotFound() {
        TwitterException twitterException1 = new TwitterException("Test Message 1", new HttpResponse(400, new byte[0]));
        assertFalse(twitterException1.resourceNotFound());

        TwitterException twitterException2 = new TwitterException("Test Message 2", new HttpResponse(404, new byte[0]));
        assertTrue(twitterException2.resourceNotFound());
    }
}