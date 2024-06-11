import org.junit.Assert;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;

public class TwitterExceptionTest {

    @org.junit.Test
    public void testTwitterExceptionConstructorWithMessage() {
        TwitterException exception = new TwitterException("Test Message");
        Assert.assertEquals("Test Message", exception.getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isNested());
    }

    @org.junit.Test
    public void testTwitterExceptionConstructorWithCause() {
        Exception cause = new IOException("Test IOException");
        TwitterException exception = new TwitterException(cause);
        Assert.assertEquals(cause.getMessage(), exception.getCause().getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isNested());
    }

    @org.junit.Test
    public void testTwitterExceptionConstructorWithMessageAndHttpResponse() {
        HttpResponse httpResponse = new DefaultHttpResponse(420, "Rate Limit Exceeded");
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        Assert.assertEquals("Test Message" + System.lineSeparator() + getCause(httpResponse), exception.getMessage());
        Assert.assertEquals(httpResponse.getStatusCode(), exception.getStatusCode());
        Assert.assertNotNull(exception.getRateLimitStatus());
        Assert.assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isNested());
    }

    @org.junit.Test
    public void testTwitterExceptionConstructorWithMessageCauseAndStatusCode() {
        Exception cause = new IOException("Test IOException");
        TwitterException exception = new TwitterException("Test Message", cause, 400);
        Assert.assertEquals("Test Message", exception.getMessage());
        Assert.assertEquals(400, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isNested());
        Assert.assertSame(cause, exception.getCause());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        HttpResponse httpResponse = new DefaultHttpResponse(400, "Test Response");
        TwitterException exception = new TwitterException("Test Message", httpResponse);
        exception.getRetryAfter();
    }

    @org.junit.Test
    public void testIsCausedByNetworkIssue() {
        IOException ioException = new IOException("Test IOException");
        TwitterException twitterException = new TwitterException("Error Message", ioException);
        Assert.assertTrue(twitterException.isCausedByNetworkIssue());
        twitterException = new TwitterException("Error Message");
        Assert.assertFalse(twitterException.isCausedByNetworkIssue());
        Exception nonIOExceptionException = new Exception("Test Exception");
        twitterException = new TwitterException("Error Message", nonIOExceptionException);
        Assert.assertFalse(twitterException.isCausedByNetworkIssue());
    }

    @org.junit.Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = new DefaultHttpResponse(400, "Test Response");
        TwitterException exception1 = new TwitterException("Error Message", httpResponse);
        Assert.assertFalse(exception1.exceededRateLimitation());
        HttpResponse httpResponseRateLimit = new DefaultHttpResponse(420, "Rate Limit Exceeded");
        TwitterException exception2 = new TwitterException("Error Message", httpResponseRateLimit);
        Assert.assertTrue(exception2.exceededRateLimitation());
        HttpResponse httpResponseNoRateLimit = new DefaultHttpResponse(200, "Test Response");
        TwitterException exception3 = new TwitterException("Error Message", httpResponseNoRateLimit);
        Assert.assertFalse(exception3.exceededRateLimitation());
    }

    @org.junit.Test
    public void testResourceNotFound() {
        HttpResponse httpResponse = new DefaultHttpResponse(404, "Test Response");
        TwitterException exception = new TwitterException("Error Message", httpResponse);
        Assert.assertTrue(exception.resourceNotFound());
        HttpResponse httpResponseNotNotFound = new DefaultHttpResponse(200, "Test Response");
        TwitterException exceptionNotNotFound = new TwitterException("Error Message", httpResponseNotNotFound);
        Assert.assertFalse(exceptionNotNotFound.resourceNotFound());
    }

    private static String getCause(HttpResponse httpResponse) {
        return httpResponse.getStatusLine().getReasonPhrase();
    }

}