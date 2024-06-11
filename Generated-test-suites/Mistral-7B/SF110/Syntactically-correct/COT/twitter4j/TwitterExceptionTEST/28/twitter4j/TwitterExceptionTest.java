import org.junit.Assert;
import org.junit.Test;
import twitter4j.RateLimitStatus;
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
        IOException cause = new IOException();
        TwitterException exception = new TwitterException("Test Message", cause);
        Assert.assertEquals("Test Message", exception.getMessage());
        Assert.assertSame(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponse(420, "Retry-After: 60", new byte[0]);
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        Assert.assertEquals(420, exception.getStatusCode());
        Assert.assertEquals(RateLimitStatus.RATE_LIMIT, exception.getRateLimitStatus());
        Assert.assertEquals(60, exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        IOException cause = new IOException();
        TwitterException exception = new TwitterException("Test Message", cause, 404);
        Assert.assertEquals("Test Message", exception.getMessage());
        Assert.assertSame(cause, exception.getCause());
        Assert.assertEquals(404, exception.getStatusCode());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        TwitterException exception = new TwitterException("Test Message");
        exception.setStatusCode(410);
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssueWhenCauseIsInstanceOfIOException() {
        IOException cause = new IOException();
        TwitterException exception = new TwitterException("Test Message", cause);
        Assert.assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testIsCausedByNetworkIssueWhenCauseIsNotInstanceOfIOException() {
        RuntimeException cause = new RuntimeException();
        TwitterException exception = new TwitterException("Test Message", cause);
        Assert.assertFalse(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitationWhenStatusCodeIs400Or420() {
        HttpResponse httpResponse = new HttpResponse(400, "", new byte[0]);
        TwitterException exception1 = new TwitterException("Test Message", httpResponse);
        Assert.assertTrue(exception1.exceededRateLimitation());

        HttpResponse httpResponse2 = new HttpResponse(420, "Retry-After: 60", new byte[0]);
        TwitterException exception2 = new TwitterException("Test Message", httpResponse2);
        Assert.assertTrue(exception2.exceededRateLimitation());
    }

    @Test
    public void testExceededRateLimitationWhenStatusCodeIsNot400And420() {
        HttpResponse httpResponse = new HttpResponse(410, "", new byte[0]);
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        Assert.assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFoundWhenStatusCodeIs404() {
        HttpResponse httpResponse = new HttpResponse(404, "", new byte[0]);
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        Assert.assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testResourceNotFoundWhenStatusCodeIsNot404() {
        HttpResponse httpResponse = new HttpResponse(403, "", new byte[0]);
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        Assert.assertFalse(exception.resourceNotFound());
    }
}