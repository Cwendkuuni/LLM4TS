package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        // Basic test case
        TwitterException exception = new TwitterException("Test Message");
        assertEquals("Test Message", exception.getMessage());

        // Edge cases
        TwitterException exceptionEmptyMessage = new TwitterException("");
        assertNull(exceptionEmptyMessage.getMessage());

        TwitterException exceptionNullMessage = new TwitterException(null);
        assertNull(exceptionNullMessage.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        // Basic test case
        Exception cause = new IOException();
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());

        // Edge cases
        TwitterException exceptionNullCause = new TwitterException(null);
        assertNull(exceptionNullCause.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        // Basic test case
        HttpResponse httpResponse = new HttpResponse(200, "OK", new byte[0]);
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        assertEquals(200, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());

        // Edge cases
        TwitterException exceptionNullHttpResponse = new TwitterException("Test Message", null);
        assertNull(exceptionNullHttpResponse.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        // Basic test case
        Exception cause = new IOException();
        TwitterException exception = new TwitterException("Test Message", cause, 404);
        assertEquals(404, exception.getStatusCode());

        // Edge cases
        TwitterException exceptionNullCauseAndStatusCode = new TwitterException("Test Message", null, 404);
        assertEquals(404, exceptionNullCauseAndStatusCode.getStatusCode());
    }
}