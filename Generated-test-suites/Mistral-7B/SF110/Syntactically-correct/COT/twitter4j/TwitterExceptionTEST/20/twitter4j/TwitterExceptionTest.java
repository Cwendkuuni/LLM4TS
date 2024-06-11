import org.junit.Assert;
import org.junit.Test;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test Exception Message");
        Assert.assertEquals("Test Exception Message", exception.getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException("Test IOException Message");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        Assert.assertEquals("Test Exception Message", exception.getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertTrue(exception.isNested());
        Assert.assertSame(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponse(420, "Retry-After: 120", null);
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        Assert.assertEquals("Test Exception Message" + System.lineSeparator() + "420: The number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
        Assert.assertEquals(420, exception.getStatusCode());
        Assert.assertNotNull(exception.getRateLimitStatus());
        Assert.assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isNested());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        TwitterException exception = new TwitterException("Test Exception Message");
        exception.setStatusCode(400);
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test Exception Message", new IOException("Test IOException Message"));
        Assert.assertTrue(exception.isCausedByNetworkIssue());
        exception = new TwitterException("Test Exception Message");
        Assert.assertFalse(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exceptionWithRateLimit = new TwitterException("Test Exception Message", new HttpResponse(400, null, new RateLimitStatus(10, 20)));
        Assert.assertTrue(exceptionWithRateLimit.exceededRateLimitation());
        TwitterException exceptionWithoutRateLimit = new TwitterException("Test Exception Message", new HttpResponse(404, null, null));
        Assert.assertFalse(exceptionWithoutRateLimit.exceededRateLimitation());
        TwitterException exceptionWithStatus420 = new TwitterException("Test Exception Message", new HttpResponse(420, null, null));
        Assert.assertTrue(exceptionWithStatus420.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exceptionWith404Status = new TwitterException("Test Exception Message", new HttpResponse(404, null, null));
        Assert.assertTrue(exceptionWith404Status.resourceNotFound());
        TwitterException exceptionWithOtherStatus = new TwitterException("Test Exception Message", new HttpResponse(400, null, null));
        Assert.assertFalse(exceptionWithOtherStatus.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException twitterException = new TwitterException("Test Exception Message");
        String exceptionCode = twitterException.getExceptionCode();
        Assert.assertNotNull(exceptionCode);
    }
}