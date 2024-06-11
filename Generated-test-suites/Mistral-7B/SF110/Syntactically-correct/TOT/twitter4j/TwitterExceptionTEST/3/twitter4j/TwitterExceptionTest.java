import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        String message = "Test TwitterException with message";
        TwitterException exception = new TwitterException(message);
        assertEquals(message, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        String causeMessage = "Test TwitterException with cause";
        IOException cause = new IOException(causeMessage);
        TwitterException exception = new TwitterException("Test TwitterException with cause", cause);
        assertEquals(causeMessage, cause.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        String message = "Test TwitterException with message and HttpResponse";
        HttpResponse response = new HttpResponse(-1, 420, null);
        TwitterException exception = new TwitterException(message, response);
        assertEquals(getMessageWithStatusCodes(message, 420), exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
        String message = "Test TwitterException with message, cause and HttpResponse";
        HttpResponse response = new HttpResponse(-1, 420, null);
        IOException cause = new IOException("Test IOException with message");
        TwitterException exception = new TwitterException(message, cause, response.getStatusCode());
        assertEquals(getMessageWithStatusCodes(message, response.getStatusCode()), exception.getMessage());
        assertEquals(response.getStatusCode(), exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        String message = "Test TwitterException getExceptionCode";
        HttpResponse response = new HttpResponse(-1, 420, null);
        TwitterException exception = new TwitterException(message, response);
        String exceptionCode = exception.getExceptionCode();
        String expectedExceptionCode = "40:1c031b0b3f0e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e";
        assertEquals(expectedExceptionCode, exceptionCode);
    }

    private String getMessageWithStatusCodes(String message, int statusCode) {
        String cause = "";
        String statusCodeMessage = "";
        switch (statusCode) {
            case 400:
                statusCodeMessage = "The request was invalid.  An accompanying error message will explain why.";
                break;
            case 420:
                statusCodeMessage = "The number of requests you have made exceeds the quota afforded by your assigned rate limit.";
                break;
            default:
                statusCodeMessage = "";
        }
        return message + " " + statusCodeMessage;
    }
}