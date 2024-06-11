package twitter4j;

import static org.junit.Assert.*;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException twitterException = new TwitterException("Test Exception Message");
        assertEquals("Test Exception Message", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isNested());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new Exception("Cause Exception Message");
        TwitterException twitterException = new TwitterException(cause);
        assertEquals(cause.getMessage(), twitterException.getCause().getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isNested());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(400);
        String retryAfterStr = "120";
        httpResponse.setResponseHeader("Retry-After", retryAfterStr);
        TwitterException twitterException = new TwitterException("Test Exception Message", httpResponse);
        assertEquals("Test Exception Message" + "\n400: The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.", twitterException.getMessage());
        assertEquals(400, twitterException.getStatusCode());
        assertEquals(Integer.valueOf(120), twitterException.getRetryAfter());
        assertNotNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isNested());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(400);
        String retryAfterStr = "120";
        httpResponse.setResponseHeader("Retry-After", retryAfterStr);
        Exception cause = new Exception("Cause Exception Message");
        TwitterException twitterException = new TwitterException("Test Exception Message", cause, 400, httpResponse);
        assertEquals("Test Exception Message" + "\n400: The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.", twitterException.getMessage());
        assertEquals(400, twitterException.getStatusCode());
        assertEquals(Integer.valueOf(120), twitterException.getRetryAfter());
        assertEquals(cause.getMessage(), twitterException.getCause().getMessage());
        assertNotNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isNested());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException ioException = new IOException("Network IOException");
        TwitterException twitterException1 = new TwitterException("Twitter Exception Message", ioException);
        assertTrue(twitterException1.isCausedByNetworkIssue());

        Exception exception = new Exception("Exception Message");
        TwitterException twitterException2 = new TwitterException("Twitter Exception Message", exception);
        assertFalse(twitterException2.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(420);
        TwitterException twitterException = new TwitterException("Test Exception Message", httpResponse);
        assertTrue(twitterException.exceededRateLimitation());

        HttpResponse httpResponse1 = new HttpResponse();
        httpResponse1.setStatusCode(404);
        TwitterException twitterException1 = new TwitterException("Test Exception Message", httpResponse1);
        assertFalse(twitterException1.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(404);
        TwitterException twitterException = new TwitterException("Test Exception Message", httpResponse);
        assertTrue(twitterException.resourceNotFound());

        HttpResponse httpResponse1 = new HttpResponse();
        httpResponse1.setStatusCode(400);
        TwitterException twitterException1 = new TwitterException("Test Exception Message", httpResponse1);
        assertFalse(twitterException1.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(400);
        TwitterException twitterException = new TwitterException("Test Exception Message", httpResponse);
        assertEquals("400:The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.", twitterException.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException twitterException = new TwitterException("Test Exception Message");
        assertFalse(twitterException.isNested());

        twitterException.setNested();
        assertTrue(twitterException.isNested());
    }

    @Test
    public void testEquals() {
        TwitterException twitterException1 = new TwitterException("Test Exception Message 1");
        TwitterException twitterException2 = new TwitterException("Test Exception Message 2");
        assertFalse(twitterException1.equals(twitterException2));

        TwitterException twitterException3 = new TwitterException("Test Exception Message 1");
        assertTrue(twitterException1.equals(twitterException3));

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(420);
        TwitterException twitterException4 = new TwitterException("Test Exception Message", httpResponse);
        TwitterException twitterException5 = new TwitterException("Test Exception Message", httpResponse);
        assertTrue(twitterException4.equals(twitterException5));
    }

    @Test
    public void testHashCode() {
        TwitterException twitterException1 = new TwitterException("Test Exception Message 1");
        TwitterException twitterException2 = new TwitterException("Test Exception Message 2");
        int hashCode1 = twitterException1.hashCode();
        int hashCode2 = twitterException2.hashCode();
        assertNotEquals(hashCode1, hashCode2);

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(420);
        TwitterException twitterException3 = new TwitterException("Test Exception Message", httpResponse);
        TwitterException twitterException4 = new TwitterException("Test Exception Message", httpResponse);
        int hashCode3 = twitterException3.hashCode();
        int hashCode4 = twitterException4.hashCode();
        assertEquals(hashCode3, hashCode4);
    }
}