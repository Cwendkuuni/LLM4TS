package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertEquals("Test Exception Message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException("IO Exception Cause");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(400);
        when(httpResponse.getResponseHeader("Retry-After")).thenReturn("300");
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        assertEquals("Test Exception Message\n400: The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting. Retry-After: 300", exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        assertEquals(300, exception.getRetryAfter());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        Exception cause = new IOException("IO Exception Cause");
        TwitterException exception = new TwitterException("Test Exception Message", cause, 401);
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(401, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test Exception Message", new IOException("IO Exception Cause"));
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(420);
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(404);
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertEquals("000000", exception.getExceptionCode());

        IOException cause = new IOException("IO Exception Cause");
        exception = new TwitterException("Test Exception Message", cause);
        assertEquals("010001", exception.getExceptionCode());

        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(400);
        when(httpResponse.getResponseHeader("Retry-After")).thenReturn("300");
        exception = new TwitterException("Test Exception Message", httpResponse);
        assertEquals("410004300", exception.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException exception1 = new TwitterException("Test Exception Message 1");
        TwitterException exception2 = new TwitterException("Test Exception Message 2");
        exception1.setNested();
        exception2.initCause(exception1);
        assertTrue(exception2.isNested());
    }
}