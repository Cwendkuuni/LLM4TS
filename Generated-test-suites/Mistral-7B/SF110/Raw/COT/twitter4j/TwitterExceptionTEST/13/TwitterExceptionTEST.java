import org.junit.Assert;
import org.junit.Test;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test message");
        Assert.assertEquals("Test message", exception.getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertEquals(false, exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException();
        TwitterException exception = new TwitterException("Test message", cause);
        Assert.assertEquals("Test message", exception.getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertEquals(cause, exception.getCause());
        Assert.assertEquals(false, exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponse(200, new byte[0]);
        TwitterException exception = new TwitterException("Test message", httpResponse);
        Assert.assertEquals("Test message" + System.lineSeparator() + getCause(httpResponse), exception.getMessage());
        Assert.assertEquals(httpResponse.getStatusCode(), exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertEquals(false, exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        String message = "Test message";
        Exception cause = new IOException();
        int statusCode = 400;
        TwitterException exception = new TwitterException(message, cause, statusCode);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(statusCode, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertEquals(cause, exception.getCause());
        Assert.assertEquals(false, exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithNestedException() {
        TwitterException outerException = new TwitterException("Outer message");
        Exception innerException = new IOException();
        outerException.setNested();
        outerException.initCause(innerException);
        Assert.assertEquals(true, outerException.isNested());
        Assert.assertNull(outerException.getCause());
        Assert.assertEquals("Outer message", outerException.getMessage());
        Assert.assertEquals(-1, outerException.getStatusCode());
        Assert.assertNull(outerException.getRateLimitStatus());
        Assert.assertNull(outerException.getFeatureSpecificRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        TwitterException exception = new TwitterException("Test message", new HttpResponse(200, new byte[0]));
        exception.getRetryAfter();
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = new HttpResponse(400, new byte[0]);
        TwitterException exception = new TwitterException("Test message", httpResponse);
        Assert.assertTrue(exception.exceededRateLimitation());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());

        httpResponse.setStatusCode(420);
        exception = new TwitterException("Test message", httpResponse);
        Assert.assertTrue(exception.exceededRateLimitation());
        Assert.assertNotNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());

        RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 16);
        exception.setRateLimitStatus(rateLimitStatus);
        Assert.assertTrue(exception.exceededRateLimitation());
        Assert.assertNotNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());

        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus(15, 16, "test");
        exception.setFeatureSpecificRateLimitStatus(featureSpecificRateLimitStatus);
        Assert.assertTrue(exception.exceededRateLimitation());
        Assert.assertNotNull(exception.getRateLimitStatus());
        Assert.assertNotNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        HttpResponse httpResponse = new HttpResponse(404, new byte[0]);
        IOException ioException = new IOException();
        TwitterException exception = new TwitterException("Test message", httpResponse);
        Assert.assertFalse(exception.isCausedByNetworkIssue());

        ioException.initCause(exception);
        exception = new TwitterException("Test message", ioException);
        Assert.assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse httpResponse = new HttpResponse(404, new byte[0]);
        TwitterException exception = new TwitterException("Test message", httpResponse);
        Assert.assertTrue(exception.resourceNotFound());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetExceptionCode() {
        HttpResponse httpResponse = new HttpResponse(400, new byte[0]);
        TwitterException exception = new TwitterException("Test message", httpResponse);
        String exceptionCode = exception.getExceptionCode();
        Assert.assertNotNull(exceptionCode);
        Assert.assertEquals(exceptionCode.length(), 6);
    }

    private static String getCause(HttpResponse res) {
        int statusCode = res.getStatusCode();
        String cause = null;
        switch (statusCode) {
            case 304:
                cause = "There was no new data to return.";
                break;
            case 400:
                cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
                break;
            case 401:
                cause = "Authentication credentials were missing or incorrect.";
                break;
            case 403:
                cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
                break;
            case 404:
                cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
                break;
            case 406:
                cause = "Returned by the Search API when an invalid format is specified in the request.";
                break;
            case 420:
                cause = "The number of requests you have made exceeds the quota afforded by your assigned rate limit.";
                break;
            case 500:
                cause = "Something is broken.  Please post to the group so the Twitter team can investigate.";
                break;
            case 502:
                cause = "Twitter is down or being upgraded.";
                break;
            case 503:
                cause = "Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
                break;
            default:
                cause = "";
                break;
        }
        return statusCode + ":" + cause;
    }

}