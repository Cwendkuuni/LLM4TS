import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test Message");
        Assert.assertEquals("Test Message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException();
        TwitterException exception = new TwitterException("Test Message", cause);
        Assert.assertEquals("Test Message", exception.getMessage());
        Assert.assertSame(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponse(420, "Retry-After: 5");
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        Assert.assertEquals(420, exception.getStatusCode());
        Assert.assertEquals(5, exception.getRetryAfter());
        Assert.assertNotNull(exception.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        String message = "Test Message";
        Exception cause = new IOException();
        int statusCode = 420;
        TwitterException exception = new TwitterException(message, cause, statusCode);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertSame(cause, exception.getCause());
        Assert.assertEquals(statusCode, exception.getStatusCode());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        TwitterException exception = new TwitterException("Test Message");
        exception.setStatusCode(401);
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException ioException = new IOException();
        TwitterException twitterException = new TwitterException("Test Message", ioException);
        Assert.assertTrue(twitterException.isCausedByNetworkIssue());

        Exception nonIoException = new RuntimeException();
        TwitterException twitterException2 = new TwitterException("Test Message", nonIoException);
        Assert.assertFalse(twitterException2.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = new HttpResponse(400, "Rate limit exceeded");
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        Assert.assertTrue(exception.exceededRateLimitation());

        HttpResponse httpResponse2 = new HttpResponse(401, "Unauthorized");
        TwitterException exception2 = new TwitterException("Test Message", httpResponse2);
        Assert.assertFalse(exception2.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse httpResponse = new HttpResponse(404, "Resource not found");
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        Assert.assertTrue(exception.resourceNotFound());

        HttpResponse httpResponse2 = new HttpResponse(400, "Invalid request");
        TwitterException exception2 = new TwitterException("Test Message", httpResponse2);
        Assert.assertFalse(exception2.resourceNotFound());
    }
}