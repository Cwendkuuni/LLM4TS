import org.junit.Assert;
import org.junit.Test;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test Exception Message");
        Assert.assertEquals("Test Exception Message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException();
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        Assert.assertEquals("Test Exception Message", exception.getMessage());
        Assert.assertSame(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageAndResponse() {
        HttpResponse response = new HttpResponse(200, "OK", new byte[0]);
        TwitterException exception = new TwitterException("Test Exception Message", response);
        Assert.assertEquals("Test Exception Message" + System.lineSeparator() + "Response:[statusCode=200, reason=OK]", exception.getMessage());
        Assert.assertEquals(200, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        Exception cause = new IOException();
        TwitterException exception = new TwitterException("Test Exception Message", cause, 200);
        Assert.assertEquals("Test Exception Message", exception.getMessage());
        Assert.assertSame(cause, exception.getCause());
        Assert.assertEquals(200, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetStatusCodeWhenNotRateLimitation() {
        TwitterException exception = new TwitterException("Test Exception Message");
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException();
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        Assert.assertTrue(exception.isCausedByNetworkIssue());

        Exception nonNetworkCause = new RuntimeException();
        TwitterException exception2 = new TwitterException("Test Exception Message", nonNetworkCause);
        Assert.assertFalse(exception2.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse response = new HttpResponse(420, "Rate Limit Exceeded", new byte[0]);
        TwitterException exception = new TwitterException("Test Exception Message", response);
        Assert.assertTrue(exception.exceededRateLimitation());

        HttpResponse response2 = new HttpResponse(400, "Bad Request", new byte[0]);
        TwitterException exception2 = new TwitterException("Test Exception Message", response2);
        Assert.assertFalse(exception2.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse response = new HttpResponse(404, "Not Found", new byte[0]);
        TwitterException exception = new TwitterException("Test Exception Message", response);
        Assert.assertTrue(exception.resourceNotFound());

        HttpResponse response2 = new HttpResponse(400, "Bad Request", new byte[0]);
        TwitterException exception2 = new TwitterException("Test Exception Message", response2);
        Assert.assertFalse(exception2.resourceNotFound());
    }

    @Test
    public void testGetRateLimitStatus() {
        HttpResponse response = new HttpResponse(420, "Rate Limit Exceeded", new byte[0]);
        TwitterException exception = new TwitterException("Test Exception Message", response);
        RateLimitStatus rateLimitStatus = new RateLimitStatus();
        rateLimitLimitStatus.setRemaining(10);
        rateLimitStatus.setLimit(20);
        exception.setRateLimitStatus(rateLimitStatus);
        Assert.assertNotNull(exception.getRateLimitStatus());
        Assert.assertEquals(rateLimitStatus, exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        HttpResponse response = new HttpResponse(420, "Rate Limit Exceeded", new byte[0]);
        TwitterException exception = new TwitterException("Test Exception Message", response);
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus();
        featureSpecificRateLimitStatus.setRemaining(10);
        featureSpecificRateLimitStatus.setLimit(20);
        exception.setFeatureSpecificRateLimitStatus(featureSpecificRateLimitStatus);
        Assert.assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        HttpResponse response = new HttpResponse(420, "Rate Limit Exceeded", new byte[0]);
        response.setResponseHeader("Retry-After", "30");
        TwitterException exception = new TwitterException("Test Exception Message", response);
        Assert.assertEquals(30, exception.getRetryAfter());
    }
}