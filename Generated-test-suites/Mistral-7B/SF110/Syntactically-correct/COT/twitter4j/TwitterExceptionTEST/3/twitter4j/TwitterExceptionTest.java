import org.junit.Assert;
import org.junit.Test;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test Message");
        Assert.assertEquals("Test Message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException("IOException Message");
        TwitterException exception = new TwitterException("Test Message", cause);
        Assert.assertEquals("IOException Message", exception.getCause().getMessage());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse res = new DefaultHttpResponse(420, "Rate Limit Exceeded");
        TwitterException exception = new TwitterException("Test Message", res);
        Assert.assertEquals(420, exception.getStatusCode());
        Assert.assertNotNull(exception.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithHttpResponseAndCause() {
        HttpResponse res = new DefaultHttpResponse(420, "Rate Limit Exceeded");
        IOException cause = new IOException("IOException Message");
        TwitterException exception = new TwitterException("Test Message", res, cause);
        Assert.assertEquals(420, exception.getStatusCode());
        Assert.assertNotNull(exception.getRateLimitStatus());
        Assert.assertEquals("IOException Message", exception.getCause().getMessage());
    }

    @Test
    public void testTwitterExceptionWithStatusCode() {
        TwitterException exception = new TwitterException("Test Message", 404);
        Assert.assertEquals(404, exception.getStatusCode());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        TwitterException exception = new TwitterException("Test Message", new DefaultHttpResponse(404, ""));
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("IOException Message");
        TwitterException exception = new TwitterException("Test Message", cause);
        Assert.assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse res = new DefaultHttpResponse(400, "");
        TwitterException exception = new TwitterException("Test Message", res);
        Assert.assertTrue(exception.exceededRateLimitation());

        res = new DefaultHttpResponse(420, "");
        exception = new TwitterException("Test Message", res);
        Assert.assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse res = new DefaultHttpResponse(404, "");
        TwitterException exception = new TwitterException("Test Message", res);
        Assert.assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Message 1", 400, null, null);
        TwitterException exception2 = new TwitterException("Test Message 2", 400, null, null);
        Assert.assertEquals(exception1, exception2);

        exception1 = new TwitterException("Test Message 1", 400, null, null);
        exception2 = new TwitterException("Test Message 2", 400, null, null);
        exception2.setRetryAfter(10);
        Assert.assertNotEquals(exception1, exception2);

        exception1 = new TwitterException("Test Message 1", 400, null, null);
        exception2 = new TwitterException("Test Message 1", 400, null, new RateLimitStatus());
        Assert.assertNotEquals(exception1, exception2);
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test Message 1", 400, null, null);
        TwitterException exception2 = new TwitterException("Test Message 2", 400, null, null);
        Assert.assertEquals(exception1.hashCode(), exception2.hashCode());

        exception1 = new TwitterException("Test Message 1", 400, null, null);
        exception2 = new TwitterException("Test Message 1", 400, null, new RateLimitStatus());
        Assert.assertNotEquals(exception1.hashCode(), exception2.hashCode());
    }

    @Test
    public void testToString() {
        HttpResponse res = new DefaultHttpResponse(420, "");
        TwitterException exception = new TwitterException("Test Message", res);
        String expected = "TwitterException{statusCode=420, retryAfter=0, rateLimitStatus=null, version=}";
        Assert.assertEquals(expected, exception.toString());
    }
}