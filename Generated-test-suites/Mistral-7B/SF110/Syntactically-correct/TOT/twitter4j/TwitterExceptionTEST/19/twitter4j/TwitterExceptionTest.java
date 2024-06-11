import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.RateLimitStatus;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test exception message");
        Assert.assertEquals("Test exception message", exception.getMessage());
        Assert.assertNull(exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isCausedByNetworkIssue());
        Assert.assertFalse(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        IOException cause = new IOException("Test IOException message");
        TwitterException exception = new TwitterException("Test exception message", cause);
        Assert.assertEquals("Test exception message", exception.getMessage());
        Assert.assertSame(cause, exception.getCause());
        Assert.assertNull(exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertTrue(exception.isCausedByNetworkIssue());
        Assert.assertFalse(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponse(200, "OK", new byte[0]);
        TwitterException exception = new TwitterException("Test exception message", httpResponse);
        Assert.assertEquals("Test exception message", exception.getMessage());
        Assert.assertEquals(200, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isCausedByNetworkIssue());
        Assert.assertFalse(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
        IOException cause = new IOException("Test IOException message");
        HttpResponse httpResponse = new HttpResponse(200, "OK", new byte[0]);
        TwitterException exception = new TwitterException("Test exception message", cause, httpResponse);
        Assert.assertEquals("Test exception message", exception.getMessage());
        Assert.assertSame(cause, exception.getCause());
        Assert.assertEquals(200, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertTrue(exception.isCausedByNetworkIssue());
        Assert.assertFalse(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithMessageStatusCode() {
        TwitterException exception = new TwitterException("Test exception message", 400);
        Assert.assertEquals("Test exception message", exception.getMessage());
        Assert.assertEquals(400, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isCausedByNetworkIssue());
        Assert.assertFalse(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithMessageStatusCodeRateLimitStatus() {
        RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 30);
        TwitterException exception = new TwitterException("Test exception message", 400, rateLimitStatus);
        Assert.assertEquals("Test exception message", exception.getMessage());
        Assert.assertEquals(400, exception.getStatusCode());
        Assert.assertSame(rateLimitStatus, exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isCausedByNetworkIssue());
        Assert.assertTrue(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithMessageStatusCodeRateLimitStatusFeatureSpecificRateLimitStatus() {
        RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 30);
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus(5, 10);
        TwitterException exception = new TwitterException("Test exception message", 400, rateLimitStatus, featureSpecificRateLimitStatus);
        Assert.assertEquals("Test exception message", exception.getMessage());
        Assert.assertEquals(400, exception.getStatusCode());
        Assert.assertSame(rateLimitStatus, exception.getRateLimitStatus());
        Assert.assertSame(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isCausedByNetworkIssue());
        Assert.assertTrue(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithMessageStatusCodeRetryAfter() {
        int retryAfter = 60;
        TwitterException exception = new TwitterException("Test exception message", 420, null, null, retryAfter);
        Assert.assertEquals("Test exception message", exception.getMessage());
        Assert.assertEquals(420, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isCausedByNetworkIssue());
        Assert.assertTrue(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertEquals(retryAfter, exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionEquals() {
        TwitterException exception1 = new TwitterException("Test exception message 1", 400, new RateLimitStatus(15, 30));
        TwitterException exception2 = new TwitterException("Test exception message 2", 400, new RateLimitStatus(15, 30));
        Assert.assertEquals(exception1, exception2);
    }

    @Test
    public void testTwitterExceptionNotEquals() {
        TwitterException exception1 = new TwitterException("Test exception message 1", 400, new RateLimitStatus(15, 30));
        TwitterException exception2 = new TwitterException("Test exception message 2", 401, new RateLimitStatus(15, 30));
        Assert.assertNotEquals(exception1, exception2);
    }

    @Test
    public void testTwitterExceptionHashCode() {
        TwitterException exception1 = new TwitterException("Test exception message 1", 400, new RateLimitStatus(15, 30));
        TwitterException exception2 = new TwitterException("Test exception message 2", 400, new RateLimitStatus(15, 30));
        Assert.assertEquals(exception1.hashCode(), exception2.hashCode());
    }

    @Test
    public void testTwitterExceptionToString() {
        HttpResponse httpResponse = new HttpResponse(200, "OK", new byte[0]);
        TwitterException exception = new TwitterException("Test exception message", httpResponse);
        String expected = "TwitterException{statusCode=200, retryAfter=-1, rateLimitStatus=null, version=1.1.5}";
        Assert.assertEquals(expected, exception.toString());
    }
}