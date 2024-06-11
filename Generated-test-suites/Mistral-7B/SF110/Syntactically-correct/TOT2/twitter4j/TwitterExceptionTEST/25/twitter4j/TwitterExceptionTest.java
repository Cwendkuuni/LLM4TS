package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.*;

public class TwitterExceptionTest {

    @Test
    public void testConstructWithMessage() {
        TwitterException exception = new TwitterException("Test Message");
        assertEquals("Test Message", exception.getMessage());
    }

    @Test
    public void testConstructWithNullMessage() {
        TwitterException exception = new TwitterException(null);
        assertNotNull(exception.getMessage());
        assertEquals("", exception.getMessage());
    }

    @Test
    public void testConstructWithCause() {
        IOException exception1 = new IOException("Test IOException");
        TwitterException twitterException = new TwitterException(exception1);
        assertEquals(exception1.getMessage(), twitterException.getCause().getMessage());
    }

    @Test
    public void testConstructWithNestedException() {
        IOException exception1 = new IOException("Test IOException");
        TwitterException twitterException = new TwitterException(exception1);
        twitterException.setNested();
        assertTrue(twitterException.isNested());
    }

    @Test
    public void testConstructWithMessageAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(200);
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(200, exception.getStatusCode());
    }

    @Test
    public void testConstructWithMessageAndNonRateLimitingHttpResponse() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(201);
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testGetStatusCode() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(420);
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        assertEquals(420, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        HttpResponse httpResponse = new HttpResponse();
        RateLimitStatus rateLimitStatus = new RateLimitStatus();
        httpResponse.setResponseHeader("X-Rate-Limit-Limit", "100");
        httpResponse.setResponseHeader("X-Rate-Limit-Remaining", "50");
        httpResponse.setResponseHeader("X-Rate-Limit-Reset", "3600");
        RateLimitStatusJSONImpl rateLimitStatusJSONImpl = new RateLimitStatusJSONImpl();
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        assertEquals(rateLimitStatusJSONImpl.createFromResponseHeader(httpResponse), exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        HttpResponse httpResponse = new HttpResponse();
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus();
        httpResponse.setResponseHeader("X-Twitter-Rate-Limit-Limit", "100");
        httpResponse.setResponseHeader("X-Twitter-Rate-Limit-Remaining", "50");
        httpResponse.setResponseHeader("X-Twitter-Rate-Limit-Reset", "3600");
        RateLimitStatusJSONImpl rateLimitStatusJSONImpl = new RateLimitStatusJSONImpl();
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        assertEquals(rateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(httpResponse), exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setResponseHeader("Retry-After", "3600");
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        assertEquals(3600, exception.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException exception1 = new IOException("Test IOException");
        TwitterException twitterException = new TwitterException("Test Message", exception1);
        assertTrue(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(400);
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(404);
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Message 1", new HttpResponse(), 1, null, null, null);
        TwitterException exception2 = new TwitterException("Test Message 2", new HttpResponse(), 1, null, null, null);
        TwitterException exception3 = new TwitterException("Test Message 1", new HttpResponse(), 1, null, null, null);
        assertEquals(exception1, exception3);
        assertFalse(exception1.equals(exception2));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test Message 1", new HttpResponse(), 1, null, null, null);
        TwitterException exception2 = new TwitterException("Test Message 2", new HttpResponse(), 1, null, null, null);
        int hashCode1 = exception1.hashCode();
        int hashCode2 = exception2.hashCode();
        assertEquals(hashCode1, exception1.hashCode());
        assertNotEquals(hashCode1, hashCode2);
    }

}