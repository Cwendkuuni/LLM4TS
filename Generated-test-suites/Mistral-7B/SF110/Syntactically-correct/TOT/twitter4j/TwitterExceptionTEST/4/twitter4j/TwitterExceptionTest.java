
package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import twitter4j.*;
import twitter4j.internal.http.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test message");;
        String message = exception.getMessage();
        assertEquals("Test message", message);
        assertFalse(exception.exceededRateLimitation());

        // Test edge cases
        TwitterException exceptionWithRateLimitStatus = new TwitterException("Test message", new HttpResponse(400, null, null));
        assertTrue(exceptionWithRateLimitStatus.exceededRateLimitation());
        TwitterException exceptionWithStatusCode420 = new TwitterException("Test message", new HttpResponse(420, null, null));
        assertTrue(exceptionWithStatusCode420.exceededRateLimitation());

        // Test error scenario
        TwitterException exceptionWithIOException = new TwitterException("Test message", new IOException("Test error message"));
        assertFalse(exceptionWithIOException.exceededRateLimitation());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        TwitterException twitterException = new TwitterException("Test message");
        IOException ioException = new IOException("Test error message");
        twitterException.setCause(ioException);
        TwitterException nestedTwitterException = new TwitterException("Nested test message", twitterException);

        assertEquals("Test message", twitterException.getMessage());
        assertTrue(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());

        assertEquals("Nested test message", nestedTwitterException.getMessage());
        assertTrue(nestedTwitterException.isCausedByNetworkIssue());
        assertTrue(nestedTwitterException.isNested());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        String message = "Test message";
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(420);
        when(httpResponse.getResponseHeader("Retry-After")).thenReturn("120");

        TwitterException exception = new TwitterException(message, httpResponse);

        assertEquals(message, exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertEquals("120", exception.getRetryAfter());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());

        verify(httpResponse, times(1)).getStatusCode();
        verify(httpResponse, times(1)).getResponseHeader("Retry-After");
    }
}
