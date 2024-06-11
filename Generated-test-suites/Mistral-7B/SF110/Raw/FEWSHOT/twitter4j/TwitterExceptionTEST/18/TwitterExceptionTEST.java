package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class TwitterExceptionTest {

    private HttpResponse mockHttpResponse;
    private TwitterException twitterException;

    @Before
    public void setUp() {
        mockHttpResponse = mock(HttpResponse.class);
        twitterException = new TwitterException("Test Exception");
    }

    @Test
    public void testEmptyConstructor() {
        assertEquals("Test Exception", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertNull(twitterException.getRetryAfter());
        assertFalse(twitterException.isNested());
    }

    @Test
    public void testExceptionConstructor() {
        Exception cause = new Exception("Caused by another exception");
        twitterException = new TwitterException(cause);
        assertEquals("Caused by another exception", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertNull(twitterException.getRetryAfter());
        assertTrue(twitterException.isNested());
        assertEquals(cause, twitterException.getCause());
    }

    @Test
    public void testTwoArgumentConstructor() {
        String msg = "Test Message";
        twitterException = new TwitterException(msg);
        assertEquals(msg, twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertNull(twitterException.getRetryAfter());
        assertFalse(twitterException.isNested());
    }

    @Test
    public void testThreeArgumentConstructor() {
        String msg = "Test Message";
        int statusCode = 403;
        twitterException = new TwitterException(msg, statusCode);
        assertEquals(msg, twitterException.getMessage());
        assertEquals(statusCode, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertNull(twitterException.getRetryAfter());
        assertFalse(twitterException.isNested());
    }

    @Test
    public void testFourArgumentConstructor() {
        String msg = "Test Message";
        int statusCode = 403;
        Exception cause = new Exception("Caused by another exception");
        twitterException = new TwitterException(msg, cause, statusCode);
        assertEquals(msg, twitterException.getMessage());
        assertEquals(statusCode, twitterException.getStatusCode());
        assertEquals(cause, twitterException.getCause());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertNull(twitterException.getRetryAfter());
        assertTrue(twitterException.isNested());
    }

    @Test
    public void testHttpResponseConstructor() {
        when(mockHttpResponse.getStatusCode()).thenReturn(420);
        when(mockHttpResponse.getResponseHeader("Retry-After")).thenReturn("120");
        when(mockHttpResponse.getResponseHeader("X-Rate-Limit-Limit")).thenReturn("100");
        when(mockHttpResponse.getResponseHeader("X-Rate-Limit-Remaining")).thenReturn("50");
        twitterException = new TwitterException("Test Exception", mockHttpResponse);
        assertEquals("Test Exception: 420: The number of requests you have made exceeds the quota afforded by your assigned rate limit.", twitterException.getMessage());
        assertEquals(420, twitterException.getStatusCode());
        assertEquals(120, twitterException.getRetryAfter());
        assertEquals(new RateLimitStatus(100, 50), twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertTrue(twitterException.isNested());
    }

    @Test
    public void testEqualsAndHashCode() {
        TwitterException twitterException1 = new TwitterException("Test Exception 1");
        TwitterException twitterException2 = new TwitterException("Test Exception 1");
        TwitterException twitterException3 = new TwitterException("Test Exception 2");
        assertEquals(twitterException1, twitterException1);
        assertEquals(twitterException1, twitterException2);
        assertNotEquals(twitterException1, twitterException3);
        int hashCode1 = twitterException1.hashCode();
        int hashCode2 = twitterException1.hashCode();
        assertNotEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, twitterException1.hashCode());
        assertEquals(hashCode1, twitterException2.hashCode());
    }

    @Test
    public void testToString() {
        String toString1 = twitterException.toString();
        twitterException.setNested();
        String toString2 = twitterException.toString();
        assertEquals("TwitterException{statusCode=-1, retryAfter=0, rateLimitStatus=null, version=}", toString1);
        assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=}", toString2);
    }

    @Test
    public void testGetExceptionCode() {
        String exceptionCode = twitterException.getExceptionCode();
        assertEquals("", exceptionCode);
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        assertFalse(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        assertFalse(twitterException.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        twitterException = new TwitterException("Test Exception", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 404;
            }
        });
        assertTrue(twitterException.resourceNotFound());
    }
}