import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.RateLimitStatusJSONImpl;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        String message = "Test TwitterException with message";
        TwitterException exception = new TwitterException(message);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertEquals(false, exception.isCausedByNetworkIssue());
        Assert.assertEquals(false, exception.exceededRateLimitation());
        Assert.assertEquals(false, exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException("Test IOException");
        TwitterException exception = new TwitterException(cause);
        Assert.assertEquals("Test IOException", exception.getCause().getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertEquals(false, exception.isCausedByNetworkIssue());
        Assert.assertEquals(false, exception.exceededRateLimitation());
        Assert.assertEquals(false, exception.resourceNotFound());
        Assert.assertTrue(exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        String message = "Test TwitterException with message and HttpResponse";
        HttpResponse response = new HttpResponse(420, "Retry-After: 120");
        TwitterException exception = new TwitterException(message, response);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(420, exception.getStatusCode());
        Assert.assertNotNull(exception.getRateLimitStatus());
        Assert.assertEquals(RateLimitStatus.RATE_LIMIT_BLOWN, exception.getRateLimitStatus().getStatus());
        Assert.assertEquals(-1, exception.getRetryAfter());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertEquals(false, exception.isCausedByNetworkIssue());
        Assert.assertEquals(true, exception.exceededRateLimitation());
        Assert.assertEquals(false, exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
        String message = "Test TwitterException with message, cause and HttpResponse";
        HttpResponse response = new HttpResponse(420, "Retry-After: 120");
        Exception cause = new IOException("Test IOException");
        TwitterException exception = new TwitterException(message, cause, 420);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(420, exception.getStatusCode());
        Assert.assertNotNull(exception.getRateLimitStatus());
        Assert.assertEquals(RateLimitStatus.RATE_LIMIT_BLOWN, exception.getRateLimitStatus().getStatus());
        Assert.assertEquals(-1, exception.getRetryAfter());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertEquals(false, exception.isCausedByNetworkIssue());
        Assert.assertEquals(true, exception.exceededRateLimitation());
        Assert.assertEquals(false, exception.resourceNotFound());
        Assert.assertTrue(exception.isNested());
        Assert.assertEquals(cause.getMessage(), exception.getCause().getMessage());
    }
}