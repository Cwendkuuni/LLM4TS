package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        String message = "Test TwitterException with message";
        TwitterException exception = new TwitterException(message);
        Assert.assertEquals(message, exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithException() {
        Exception causeException = new IOException("Test IOException exception");
        TwitterException exception = new TwitterException(causeException);
        Assert.assertSame(causeException, exception.getCause());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        String message = "Test TwitterException with message and HttpResponse";
        HttpResponse httpResponse = new HttpResponse(420, "Retry-After: 5", new byte[0]);
        TwitterException exception = new TwitterException(message, httpResponse);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(420, exception.getStatusCode());
        Assert.assertNotNull(exception.getRateLimitStatus());
        Assert.assertEquals(RateLimitStatus.LIMIT_RATE_LIMIT, exception.getRateLimitStatus().getLimit());
        Assert.assertEquals(RateLimitStatus.LIMIT_LIMIT_REMAINING, exception.getRateLimitStatus().getRemaining());
        Assert.assertEquals(5, exception.getRetryAfter());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionWithMessageExceptionAndStatusCode() {
        String message = "Test TwitterException with message, exception and status code";
        Exception causeException = new IOException("Test IOException exception");
        TwitterException exception = new TwitterException(message, causeException, 400);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertSame(causeException, exception.getCause());
        Assert.assertEquals(400, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isNested());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test TwitterException with status code");
        exception.setStatusCode(404);
        Assert.assertEquals(404, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        HttpResponse httpResponse = new HttpResponse(420, "Retry-After: 5", new byte[0]);
        TwitterException exception = new TwitterException("Test TwitterException with HttpResponse", httpResponse);
        Assert.assertEquals(RateLimitStatus.LIMIT_RATE_LIMIT, exception.getRateLimitStatus().getLimit());
        Assert.assertEquals(RateLimitStatus.LIMIT_LIMIT_REMAINING, exception.getRateLimitStatus().getRemaining());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        HttpResponse httpResponse = new HttpResponse(420, "Retry-After: 5, X-Rate-Limit-Remaining: 10, X-Rate-Limit-Reset: Sat, 15 Aug 2020 03:57:13 GMT", new byte[0]);
        TwitterException exception = new TwitterException("Test TwitterException with HttpResponse", httpResponse);
        Assert.assertEquals(RateLimitStatus.LIMIT_RATE_LIMIT, exception.getFeatureSpecificRateLimitStatus().getLimit());
        Assert.assertEquals(10, exception.getFeatureSpecificRateLimitStatus().getRemaining());
        Assert.assertEquals("Sat, 15 Aug 2020 03:57:13 GMT", exception.getFeatureSpecificRateLimitStatus().getReset());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exceptionWithIOException = new TwitterException("Test TwitterException with IOException cause", new IOException("Test IOException cause"));
        Assert.assertTrue(exceptionWithIOException.isCausedByNetworkIssue());

        TwitterException exceptionWithoutIOException = new TwitterException("Test TwitterException without IOException cause");
        Assert.assertFalse(exceptionWithoutIOException.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = new HttpResponse(400, "", new byte[0]);
        TwitterException exception = new TwitterException("Test TwitterException with HttpResponse", httpResponse);
        Assert.assertTrue(exception.exceededRateLimitation());

        HttpResponse httpResponse2 = new HttpResponse(200, "", new byte[0]);
        TwitterException exception2 = new TwitterException("Test TwitterException with HttpResponse", httpResponse2);
        Assert.assertFalse(exception2.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse httpResponse = new HttpResponse(404, "", new byte[0]);
        TwitterException exception = new TwitterException("Test TwitterException with HttpResponse", httpResponse);
        Assert.assertTrue(exception.resourceNotFound());

        HttpResponse httpResponse2 = new HttpResponse(200, "", new byte[0]);
        TwitterException exception2 = new TwitterException("Test TwitterException with HttpResponse", httpResponse2);
        Assert.assertFalse(exception2.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test TwitterException with HttpResponse", new IOException("Test IOException cause"));
        Assert.assertEquals("twitter4j:304:There was no new data to return.", exception.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test TwitterException with nested exception");
        TwitterException nestedException = new TwitterException("Test nested TwitterException");
        exception.setNested(nestedException);
        Assert.assertTrue(exception.isNested());
        Assert.assertSame(nestedException, exception.getCause());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWithInvalidStatusCode() {
        TwitterException exception = new TwitterException("Test TwitterException with HttpResponse", new HttpResponse(401, "", new byte[0]));
        exception.getRetryAfter();
    }
}