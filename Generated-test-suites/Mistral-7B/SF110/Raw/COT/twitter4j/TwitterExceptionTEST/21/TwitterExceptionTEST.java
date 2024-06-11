import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test Exception");
        Assert.assertEquals("Test Exception", exception.getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isCausedByNetworkIssue());
        Assert.assertFalse(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getExceptionCode());
        Assert.assertFalse(exception.isNested());
        Assert.assertNull(exception.toString());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException();
        TwitterException exception = new TwitterException("Test Exception", cause);
        Assert.assertEquals("Test Exception", exception.getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertTrue(exception.isCausedByNetworkIssue());
        Assert.assertFalse(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getExceptionCode());
        Assert.assertFalse(exception.isNested());
        Assert.assertEquals(cause.toString(), exception.getCause().toString());
        Assert.assertEquals(cause.getClass(), exception.getCause().getClass());
        Assert.assertEquals(exception.getMessage() + "\n" + cause.getMessage(), exception.toString());
    }

    @Test
    public void testTwitterExceptionWithMessageAndCause() {
        String msg = "Test Exception";
        Exception cause = new IOException();
        TwitterException exception = new TwitterException(msg, cause);
        Assert.assertEquals(msg, exception.getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertTrue(exception.isCausedByNetworkIssue());
        Assert.assertFalse(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getExceptionCode());
        Assert.assertFalse(exception.isNested());
        Assert.assertEquals(cause.toString(), exception.getCause().toString());
        Assert.assertEquals(cause.getClass(), exception.getCause().getClass());
        Assert.assertEquals(msg + "\n" + cause.getMessage(), exception.toString());
    }

    @Test
    public void testTwitterExceptionWithMessageStatusCode() {
        String msg = "Test Exception";
        int statusCode = 400;
        TwitterException exception = new TwitterException(msg, statusCode);
        Assert.assertEquals(msg, exception.getMessage());
        Assert.assertEquals(statusCode, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertFalse(exception.isCausedByNetworkIssue());
        Assert.assertFalse(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getExceptionCode());
        Assert.assertFalse(exception.isNested());
        Assert.assertEquals(msg, exception.toString());
    }

    @Test
    public void testTwitterExceptionWithMessageStatusCodeCause() {
        String msg = "Test Exception";
        int statusCode = 400;
        Exception cause = new IOException();
        TwitterException exception = new TwitterException(msg, statusCode, cause);
        Assert.assertEquals(msg, exception.getMessage());
        Assert.assertEquals(statusCode, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
        Assert.assertTrue(exception.isCausedByNetworkIssue());
        Assert.assertFalse(exception.exceededRateLimitation());
        Assert.assertFalse(exception.resourceNotFound());
        Assert.assertNull(exception.getExceptionCode());
        Assert.assertFalse(exception.isNested());
        Assert.assertEquals(cause.toString(), exception.getCause().toString());
        Assert.assertEquals(cause.getClass(), exception.getCause().getClass());
        Assert.assertEquals(msg + "\n" + cause.getMessage(), exception.toString());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test Exception");
        exception.setStatusCode(400);
        Assert.assertEquals(400, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        HttpResponse response = new HttpResponse(420, "[]", new byte[0]);
        TwitterException exception = new TwitterException("Test Exception", response);
        Assert.assertEquals(exception.getRateLimitStatus(), RateLimitStatus.READ);
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        HttpResponse response = new HttpResponse(420, "{\"limit\":100,\"remaining\":95,\"reset\":1595485224}", new byte[0]);
        TwitterException exception = new TwitterException("Test Exception", response);
        Assert.assertEquals(exception.getFeatureSpecificRateLimitStatus().getLimit(), 100);
        Assert.assertEquals(exception.getFeatureSpecificRateLimitStatus().getRemaining(), 95);
        Assert.assertEquals(exception.getFeatureSpecificRateLimitStatus().getReset(), 1595485224L);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        HttpResponse response = new HttpResponse(400, "[]", new byte[0]);
        TwitterException exception = new TwitterException("Test Exception", response);
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test Exception");
        Assert.assertFalse(exception.isCausedByNetworkIssue());

        IOException cause = new IOException();
        exception = new TwitterException("Test Exception", cause);
        Assert.assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse response = new HttpResponse(400, "{\"rate\":{\"limit\":100,\"remaining\":99,\"reset\":1595485224},\"status\":400}", new byte[0]);
        TwitterException exception = new TwitterException("Test Exception", response);
        Assert.assertFalse(exception.exceededRateLimitation());

        HttpResponse response2 = new HttpResponse(420, "[]", new byte[0]);
        TwitterException exception2 = new TwitterException("Test Exception", response2);
        Assert.assertTrue(exception2.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse response = new HttpResponse(404, "[]", new byte[0]);
        TwitterException exception = new TwitterException("Test Exception", response);
        Assert.assertTrue(exception.resourceNotFound());

        HttpResponse response2 = new HttpResponse(400, "[]", new byte[0]);
        TwitterException exception2 = new TwitterException("Test Exception", response2);
        Assert.assertFalse(exception2.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        HttpResponse response = new HttpResponse(400, "[]", new byte[0]);
        TwitterException exception = new TwitterException("Test Exception", response);
        Assert.assertNull(exception.getExceptionCode());

        HttpResponse response2 = new HttpResponse(420, "[]", new byte[0]);
        TwitterException exception2 = new TwitterException("Test Exception", response2);
        Assert.assertEquals(exception2.getExceptionCode(), "400:The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.");
    }

    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test Exception");
        exception.setNested();
        Assert.assertTrue(exception.isNested());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception");
        TwitterException exception2 = new TwitterException("Test Exception");
        Assert.assertTrue(exception1.equals(exception2));

        exception2.setStatusCode(401);
        Assert.assertFalse(exception1.equals(exception2));

        exception2.setStatusCode(400);
        Assert.assertTrue(exception1.equals(exception2));

        exception2.setRateLimitStatus(RateLimitStatus.WRITE);
        Assert.assertFalse(exception1.equals(exception2));

        TwitterException exception3 = new TwitterException("Test Exception", 400);
        Assert.assertTrue(exception1.equals(exception3));

        exception3.setStatusCode(401);
        Assert.assertFalse(exception1.equals(exception3));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test Exception");
        TwitterException exception2 = new TwitterException("Test Exception");
        Assert.assertEquals(exception1.hashCode(), exception2.hashCode());

        exception2.setStatusCode(401);
        Assert.assertNotEquals(exception1.hashCode(), exception2.hashCode());

        exception2.setStatusCode(400);
        Assert.assertEquals(exception1.hashCode(), exception2.hashCode());

        exception2.setRateLimitStatus(RateLimitStatus.WRITE);
        Assert.assertNotEquals(exception1.hashCode(), exception2.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Exception");
        Assert.assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=-1, rateLimitStatus=null, version=}", exception.toString());

        exception.setStatusCode(400);
        Assert.assertEquals("TwitterException{exceptionCode=[], statusCode=400, retryAfter=-1, rateLimitStatus=null, version=}", exception.toString());

        exception.setRateLimitStatus(RateLimitStatus.READ);
        Assert.assertEquals("TwitterException{exceptionCode=[], statusCode=400, retryAfter=-1, rateLimitStatus=READ, version=}", exception.toString());

        exception.setFeatureSpecificRateLimitStatus(new RateLimitStatus("write", "100", "99", 1595485224));
        Assert.assertEquals("TwitterException{exceptionCode=[], statusCode=400, retryAfter=-1, rateLimitStatus=READ, featureSpecificRateLimitStatus=write:limit=100,remaining=99,reset=1595485224, version=}", exception.toString());
    }

}