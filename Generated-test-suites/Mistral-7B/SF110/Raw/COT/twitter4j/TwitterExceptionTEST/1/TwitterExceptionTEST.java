import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

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
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponse(400, "Bad Request", new byte[0]);
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        Assert.assertEquals("Test Exception Message" + System.lineSeparator() + getCause(httpResponse), exception.getMessage());
        Assert.assertEquals(400, exception.getStatusCode());
        Assert.assertEquals(RateLimitStatus.LIMIT_RATE_LIMIT, exception.getRateLimitStatus().getLimit());
        Assert.assertEquals(RateLimitStatus.LIMIT_RATE_LIMIT, exception.getRateLimitStatus().getRemaining());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        Exception cause = new IOException("Test IOException Message");
        TwitterException exception = new TwitterException("Test Exception Message", cause, 404);
        Assert.assertEquals("Test Exception Message", exception.getMessage());
        Assert.assertEquals(404, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertTrue(exception.isNested());
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(200, "OK", new byte[0]));
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException ioException = new IOException("Test IOException Message");
        TwitterException twitterException = new TwitterException("Test Exception Message", ioException);
        Assert.assertTrue(twitterException.isCausedByNetworkIssue());
        Exception nonIOExceptionCause = new Exception("Test Non IOException Message");
        TwitterException twitterExceptionWithNonIoExceptionCause = new TwitterException("Test Exception Message", nonIOExceptionCause);
        Assert.assertFalse(twitterExceptionWithNonIoExceptionCause.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = new HttpResponse(400, "Bad Request", new byte[0]);
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        Assert.assertTrue(exception.exceededRateLimitation());
        HttpResponse httpResponseWithoutRateLimit = new HttpResponse(500, "Internal Server Error", new byte[0]);
        TwitterException exceptionWithoutRateLimit = new TwitterException("Test Exception Message", httpResponseWithoutRateLimit);
        Assert.assertFalse(exceptionWithoutRateLimit.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse httpResponse = new HttpResponse(404, "Not Found", new byte[0]);
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        Assert.assertTrue(exception.resourceNotFound());
        HttpResponse httpResponseWithout404StatusCode = new HttpResponse(500, "Internal Server Error", new byte[0]);
        TwitterException exceptionWithout404StatusCode = new TwitterException("Test Exception Message", httpResponseWithout404StatusCode);
        Assert.assertFalse(exceptionWithout404StatusCode.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        HttpResponse httpResponse = new HttpResponse(400, "Bad Request", new byte[0]);
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        String exceptionCode = exception.getExceptionCode();
        String expectedExceptionCode = "32100001";
        Assert.assertEquals(expectedExceptionCode, exceptionCode);
    }

    private static String getCause(HttpResponse res) {
        return res.getStatusCode() + ": " + res.getReasonPhrase();
    }
}