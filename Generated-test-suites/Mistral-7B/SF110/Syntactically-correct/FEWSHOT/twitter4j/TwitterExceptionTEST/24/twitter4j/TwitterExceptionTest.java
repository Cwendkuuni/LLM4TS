package twitter4j;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class TwitterExceptionTest {

    private HttpResponse mockHttpResponse;
    private TwitterException exception;

    @Before
    public void setUp() {
        mockHttpResponse = mock(HttpResponse.class);
        exception = new TwitterException("Test Exception");
    }

    @Test
    public void testTwitterExceptionEmpty() {
        assertEquals("Test Exception", exception.getMessage());
        assertNull(exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRetryAfter());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
        assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        IOException cause = new IOException("Test IOException");
        exception = new TwitterException("Test Exception", cause);
        when(mockHttpResponse.getStatusCode()).thenReturn(420);
        exception = new TwitterException("Test Exception", mockHttpResponse);

        assertEquals("Test Exception: There is a rate limit exceeded", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(-1, exception.getRetryAfter());
        assertTrue(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
        assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        when(mockHttpResponse.getStatusCode()).thenReturn(404);
        exception = new TwitterException("Test Exception", mockHttpResponse);

        assertEquals("Test Exception: The URI requested is invalid or the resource requested, such as a user, does not exists", exception.getMessage());
        assertEquals(404, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRetryAfter());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertTrue(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
        assertFalse(exception.isNested());
    }

    @Test
    public void testGetExceptionCode() {
        when(mockHttpResponse.getStatusCode()).thenReturn(420);
        exception = new TwitterException("Test Exception", mockHttpResponse);

        String code = exception.getExceptionCode();
        assertEquals("5e542811b855a30e9bd613b7", code);
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception 1");
        TwitterException exception2 = new TwitterException("Test Exception 2");
        TwitterException exception3 = new TwitterException("Test Exception 1");

        assertFalse(exception1.equals(exception2));
        assertTrue(exception1.equals(exception3));
    }
}