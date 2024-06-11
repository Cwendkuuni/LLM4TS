import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

    @Test
    public void testEmptyConstructor() {
        TwitterException exception = new TwitterException("");
        Assert.assertEquals("", exception.getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertEquals(false, exception.isNested());
    }

    // Add other test cases for all methods

    @Test
    public void testExceededRateLimitationWithRetryAfter() {
        HttpResponse response = new HttpResponse(420, "", "Retry-After: 5");
        TwitterException exception = new TwitterException("Test message", response);
        Assert.assertEquals(5, exception.getRetryAfter());
        Assert.assertTrue(exception.exceededRateLimitation());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWithoutRateLimitation() {
        TwitterException exception = new TwitterException("Test message");
        exception.setStatusCode(400);
        exception.setRateLimitStatus(null);
        exception.setFeatureSpecificRateLimitStatus(null);
        exception.setRetryAfter(10);
        exception.getRetryAfter();
    }

    // Add other test cases for edge cases and exception handling
}