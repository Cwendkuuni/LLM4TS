import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        String expectedMsg = "Expected message";
        TwitterException exception = new TwitterException(expectedMsg);
        assertEquals(expectedMsg, exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException("IO exception");
        TwitterException exception = new TwitterException("Expected message", cause);
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        String expectedMsg = "Expected message";
        TwitterException exception = new TwitterException(expectedMsg, res);
        assertEquals(HttpResponseStatus.OK.getStatusCode(), exception.getStatusCode());
    }

    @Test
    public void testTwitterExceptionWithMessageAndCause() {
        String expectedMsg = "Expected message";
        Exception cause = new IOException("IO exception");
        TwitterException exception = new TwitterException(expectedMsg, cause);
        assertEquals(expectedMsg, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        String expectedMsg = "Expected message";
        Exception cause = new IOException("IO exception");
        int expectedStatusCode = 400;
        TwitterException exception = new TwitterException(expectedMsg, cause, expectedStatusCode);
        assertEquals(expectedMsg, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(expectedStatusCode, exception.getStatusCode());
    }
}