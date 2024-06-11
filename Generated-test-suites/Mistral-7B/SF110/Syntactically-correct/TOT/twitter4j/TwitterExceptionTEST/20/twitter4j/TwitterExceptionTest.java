import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Error message");
        Assert.assertEquals("Error message", exception.getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertEquals(0, exception.getRetryAfter());
        Assert.assertFalse(exception.isCausedByNetworkIssue());
        Assert.assertFalse(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getExceptionCode());
        Assert.assertFalse(exception.isNested());
        Assert.assertFalse(exception.equals(null));
        Assert.assertFalse(exception.equals(""));
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException();
        TwitterException exception = new TwitterException("Error message", cause);
        Assert.assertEquals("Error message", exception.getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertEquals(0, exception.getRetryAfter());
        Assert.assertTrue(exception.isCausedByNetworkIssue());
        Assert.assertFalse(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getExceptionCode());
        Assert.assertFalse(exception.isNested());
        Assert.assertFalse(exception.equals(null));
        Assert.assertFalse(exception.equals(""));
        Assert.assertEquals(cause, exception.getCause());
        Assert.assertFalse(exception.equals(cause));
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(400);
        response.setResponseHeader("Retry-After", "120");
        TwitterException exception = new TwitterException("Error message", response);
        Assert.assertEquals("Error message", exception.getMessage());
        Assert.assertEquals(400, exception.getStatusCode());
        Assert.assertNotNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertEquals(120, exception.getRetryAfter());
        Assert.assertFalse(exception.isCausedByNetworkIssue());
        Assert.assertTrue(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getExceptionCode());
        Assert.assertFalse(exception.isNested());
        Assert.assertFalse(exception.equals(null));
        Assert.assertFalse(exception.equals(""));
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(420);
        response.setResponseHeader("Retry-After", "180");
        IOException cause = new IOException();
        TwitterException exception = new TwitterException("Error message", cause, response);
        Assert.assertEquals("Error message", exception.getMessage());
        Assert.assertEquals(420, exception.getStatusCode());
        Assert.assertNotNull(exception.getRateLimitStatus());
        Assert.assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertEquals(180, exception.getRetryAfter());
        Assert.assertTrue(exception.isCausedByNetworkIssue());
        Assert.assertTrue(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getExceptionCode());
        Assert.assertTrue(exception.isNested());
        Assert.assertFalse(exception.equals(null));
        Assert.assertFalse(exception.equals(""));
        Assert.assertEquals(cause, exception.getCause());
        Assert.assertFalse(exception.equals(cause));
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Error message 1");
        TwitterException exception2 = new TwitterException("Error message 2");
        TwitterException exception3 = new TwitterException("Error message 1");
        Assert.assertFalse(exception1.equals(exception2));
        Assert.assertTrue(exception1.equals(exception3));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Error message 1");
        TwitterException exception2 = new TwitterException("Error message 2");
        TwitterException exception3 = new TwitterException("Error message 1");
        Assert.assertNotEquals(exception1.hashCode(), exception2.hashCode());
        Assert.assertEquals(exception1.hashCode(), exception3.hashCode());
    }
}