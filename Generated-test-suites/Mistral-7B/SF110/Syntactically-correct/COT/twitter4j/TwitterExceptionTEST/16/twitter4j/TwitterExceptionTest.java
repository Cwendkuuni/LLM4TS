import org.junit.Assert;
import org.junit.Test;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test Exception Message");
        Assert.assertEquals("Test Exception Message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException("Test IO Exception");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        Assert.assertEquals("Test Exception Message", exception.getMessage());
        Assert.assertSame(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse httpResponse = new BasicHttpResponse(420);
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        Assert.assertEquals(420, exception.getStatusCode());
        Assert.assertNotNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isCausedByNetworkIssue());
        Assert.assertTrue(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithMessageAndCause() {
        String message = "Test Exception Message";
        Exception cause = new IOException("Test IO Exception");
        TwitterException exception = new TwitterException(message, cause);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertSame(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        String message = "Test Exception Message";
        Exception cause = new IOException("Test IO Exception");
        int statusCode = 420;
        TwitterException exception = new TwitterException(message, cause, statusCode);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertSame(cause, exception.getCause());
        Assert.assertEquals(statusCode, exception.getStatusCode());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(200));
        exception.getRetryAfter();
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception Message 1", new HttpResponse(420));
        TwitterException exception2 = new TwitterException("Test Exception Message 2", new HttpResponse(420));
        TwitterException exception3 = new TwitterException("Test Exception Message 1", new HttpResponse(404));
        Assert.assertTrue(exception1.equals(exception1));
        Assert.assertTrue(exception1.equals(exception2));
        Assert.assertFalse(exception1.equals(exception3));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test Exception Message 1", new HttpResponse(420));
        TwitterException exception2 = new TwitterException("Test Exception Message 2", new HttpResponse(420));
        Assert.assertEquals(exception1.hashCode(), exception2.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(420));
        Assert.assertTrue(exception.toString().contains("TwitterException{statusCode=" + 420 + "}"));
    }

    @Test
    public void testIsCausedByNetworkIssueWithIOExceptionCause() {
        Exception cause = new IOException("Test IO Exception");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        Assert.assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testIsCausedByNetworkIssueWithNonIOExceptionCause() {
        Exception cause = new IllegalArgumentException("Test IllegalArgument Exception");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        Assert.assertFalse(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitationWithStatusCode400AndRateLimitStatusNull() {
        HttpResponse httpResponse = new BasicHttpResponse(400);
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        Assert.assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testExceededRateLimitationWithStatusCode420AndRateLimitStatusNotNull() {
        HttpResponse httpResponse = new BasicHttpResponse(420);
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        Assert.assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testExceededRateLimitationWithStatusCodeOtherThan400Or420() {
        HttpResponse httpResponse = new BasicHttpResponse(500);
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        Assert.assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFoundWithStatusCode404() {
        HttpResponse httpResponse = new BasicHttpResponse(404);
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        Assert.assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testResourceNotFoundWithStatusCodeOtherThan404() {
        HttpResponse httpResponse = new BasicHttpResponse(200);
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        Assert.assertFalse(exception.resourceNotFound());
    }
}