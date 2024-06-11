import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

import static org.junit.Assert.*;

public class TwitterExceptionTest {

    private HttpResponse httpResponse;
    private TwitterException twitterException;

    @Before
    public void setup() {
        httpResponse = Mockito.mock(HttpResponse.class);
        twitterException = new TwitterException("Test Message");
    }

    @Test
    public void testEmptyConstructor() {
        assertEquals("Test Message", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isNested());
    }

    @Test
    public void testExceptionConstructor() {
        Exception cause = new IOException("Mock IOException");
        twitterException = new TwitterException("Test Message", cause);
        assertEquals("Test Message: Mock IOException", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertTrue(twitterException.isNested());
    }

    @Test
    public void testHttpResponseConstructor() {
        when(httpResponse.getStatusCode()).thenReturn(400);

        twitterException = new TwitterException("Test Message", httpResponse);
        assertEquals("Test Message: There was an error: The request was invalid. An accompanying error message will explain why. This is the status code will be returned during rate limiting.", twitterException.getMessage());
        assertEquals(400, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isNested());

        when(httpResponse.getStatusCode()).thenReturn(420);

        twitterException = new TwitterException("Test Message", httpResponse);
        assertEquals("Test Message: The number of requests you have made exceeds the quota afforded by your assigned rate limit.", twitterException.getMessage());
        assertEquals(420, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isNested());

        when(httpResponse.getStatusCode()).thenReturn(401);
        String retryAfterStr = "120";
        when(httpResponse.getResponseHeader("Retry-After")).thenReturn(retryAfterStr);

        twitterException = new TwitterException("Test Message", httpResponse);
        assertEquals("Test Message: Authentication credentials were missing or incorrect.", twitterException.getMessage());
        assertEquals(401, twitterException.getStatusCode());
        assertEquals(120, twitterException.getRetryAfter());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isNested());

        when(httpResponse.getStatusCode()).thenReturn(429);
        RateLimitStatus rateLimitStatus = Mockito.mock(RateLimitStatus.class);
        when(rateLimitStatus.getRemainingBuckets()).thenReturn(new int[]{1, 1});
        when(rateLimitStatus.getResetTime()).thenReturn(System.currentTimeMillis() + 60000);
        when(httpResponse.getResponseHeader("X-Rate-Limit-Remaining")).thenReturn("2/3");
        when(httpResponse.getResponseHeader("X-Rate-Limit-Reset")).thenReturn("Thu, 01 Dec 2022 12:00:00 GMT");

        twitterException = new TwitterException("Test Message", httpResponse);
        assertEquals("Test Message: The number of requests you have made exceeds the quota afforded by your assigned rate limit.", twitterException.getMessage());
        assertEquals(429, twitterException.getStatusCode());
        assertEquals(rateLimitStatus, twitterException.getRateLimitStatus());
        assertEquals(2, twitterException.getRateLimitStatus().getRemainingBuckets()[0]);
        assertEquals(3, twitterException.getRateLimitStatus().getRemainingBuckets()[1]);
        assertEquals(60000, twitterException.getRateLimitStatus().getResetTime() - System.currentTimeMillis());
        assertEquals("Thu, 01 Dec 2022 12:00:00 GMT", twitterException.getRateLimitStatus().getResetTimeAsString());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isNested());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("Mock IOException");
        twitterException = new TwitterException("Test Message", cause);
        assertTrue(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        twitterException = new TwitterException("Test Message", null);
        twitterException.setStatusCode(400);
        twitterException.setRateLimitStatus(Mockito.mock(RateLimitStatus.class));
        assertTrue(twitterException.exceededRateLimitation());

        twitterException = new TwitterException("Test Message", null);
        twitterException.setStatusCode(420);
        twitterException.setRateLimitStatus(null);
        assertTrue(twitterException.exceededRateLimitation());

        twitterException = new TwitterException("Test Message", null);
        twitterException.setStatusCode(400);
        twitterException.setRateLimitStatus(null);
        assertFalse(twitterException.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        twitterException = new TwitterException("Test Message", null);
        twitterException.setStatusCode(404);
        assertTrue(twitterException.resourceNotFound());

        twitterException = new TwitterException("Test Message", null);
        twitterException.setStatusCode(400);
        assertFalse(twitterException.resourceNotFound());
    }
}