package twitter4j;

import static org.junit.Assert.*;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterException() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isNested());

        TwitterException nestedException = new TwitterException("Nested Exception Message", new IOException("Nested IOException Message"));
        assertEquals("Nested Exception Message", exception.getCause().getMessage());
        assertTrue(exception.isCausedByNetworkIssue());
        assertTrue(nestedException.isNested());

        TwitterException httpResponseException = new TwitterException("Test Exception Message", new HttpResponse(400, null));
        assertEquals("Test Exception Message\nThere was no new data to return.", exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isNested());

        TwitterException httpResponseExceptionWithRateLimit = new TwitterException("Test Exception Message", new HttpResponse(420, null));
        assertEquals("Test Exception Message\nThe number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getRetryAfter());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isNested());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertFalse(exception.isCausedByNetworkIssue());

        TwitterException httpResponseException = new TwitterException("Test Exception Message", new IOException("IOException Message"));
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertFalse(exception.exceededRateLimitation());

        TwitterException httpResponseException = new TwitterException("Test Exception Message", new HttpResponse(400, null));
        assertFalse(exception.exceededRateLimitation());

        TwitterException httpResponseExceptionWithRateLimit = new TwitterException("Test Exception Message", new HttpResponse(420, null));
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertFalse(exception.resourceNotFound());

        TwitterException httpResponseException = new TwitterException("Test Exception Message", new HttpResponse(404, null));
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertEquals("00", exception.getExceptionCode());

        TwitterException httpResponseException = new TwitterException("Test Exception Message", new HttpResponse(400, null));
        assertEquals("14", exception.getExceptionCode());

        TwitterException httpResponseExceptionWithRateLimit = new TwitterException("Test Exception Message", new HttpResponse(420, null));
        assertEquals("41", exception.getExceptionCode());
    }
}