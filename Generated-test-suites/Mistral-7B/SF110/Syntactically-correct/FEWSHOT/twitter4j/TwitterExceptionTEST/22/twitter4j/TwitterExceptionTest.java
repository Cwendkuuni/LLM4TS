package twitter4j;

import static org.junit.Assert.*;
import twitter4j.internal.http.HttpResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertEquals("Test Exception Message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new Exception("Test Cause Message");
        TwitterException exception = new TwitterException(cause);
        assertEquals("Test Cause Message", exception.getCause().getMessage());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(400);
        response.setResponseHeader("Retry-After", "120");

        TwitterException exception = new TwitterException("Test Exception Message", response);
        assertEquals(400, exception.getStatusCode());
        assertEquals(120, exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(400);
        response.setResponseHeader("Retry-After", "120");

        Exception cause = new Exception("Test Cause Message");
        TwitterException exception = new TwitterException("Test Exception Message", cause, response);
        assertEquals(400, exception.getStatusCode());
        assertEquals(120, exception.getRetryAfter());
        assertEquals("Test Cause Message", exception.getCause().getMessage());
    }

    @Test
    public void testTwitterExceptionWithMessageIntAndHttpResponse() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(400);

        TwitterException exception = new TwitterException("Test Exception Message", 420, response);
        assertEquals(400, exception.getStatusCode());
        assertEquals(420, exception.getStatusCode()); // should be equal to statusCode
        assertNull(exception.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("Test IOException Message");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(420);
        response.setResponseHeader("Retry-After", "120");

        TwitterException exception = new TwitterException("Test Exception Message", response);
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(404);

        TwitterException exception = new TwitterException("Test Exception Message", response);
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() throws Exception {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(400);
        response.setResponseHeader("Retry-After", "120");

        TwitterException exception = new TwitterException("Test Exception Message", response);
        String exceptionCode = exception.getExceptionCode();
        assertEquals("400:The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.", exceptionCode);
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception Message 1");
        TwitterException exception2 = new TwitterException("Test Exception Message 2");
        TwitterException exception3 = new TwitterException("Test Exception Message 1");

        assertFalse(exception1.equals(null));
        assertFalse(exception1.equals(""));
        assertFalse(exception1.equals(new Object()));
        assertFalse(exception1.equals(exception2));
        assertTrue(exception1.equals(exception3));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test Exception Message 1");
        TwitterException exception2 = new TwitterException("Test Exception Message 2");
        TwitterException exception3 = new TwitterException("Test Exception Message 1");

        int hashCode1 = exception1.hashCode();
        int hashCode2 = exception2.hashCode();
        int hashCode3 = exception3.hashCode();

        assertNotEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Exception Message");

        String output = "TwitterException{statusCode=[-1], retryAfter=-1, rateLimitStatus=null, version=unknown}";
        if (exception.getStatusCode() != -1) {
            output += ", statusCode=" + exception.getStatusCode();
        }
        if (exception.getRetryAfter() != -1) {
            output += ", retryAfter=" + exception.getRetryAfter();
        }
        if (exception.getRateLimitStatus() != null) {
            output += ", rateLimitStatus=" + exception.getRateLimitStatus().toString();
        }

        assertEquals(output, exception.toString());
    }

    @Test
    public void testGetRateLimitStatus() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(420);
        response.setResponseHeader("X-Rate-Limit-Limit", "100");
        response.setResponseHeader("X-Rate-Limit-Remaining", "50");
        response.setResponseHeader("X-Rate-Limit-Reset", "1234567890");

        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        rateLimitLimit = 100;
        rateLimitRemaining = 50;
        reset = 1234567890L;

        TwitterException exception = new TwitterException("Test Exception Message", response);
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(420);
        response.setResponseHeader("X-Rate-Limit-Limit-User", "100");
        response.setResponseHeader("X-Rate-Limit-Remaining-User", "50");
        response.setResponseHeader("X-Rate-Limit-Reset-User", "1234567890");

        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        rateLimitLimitUser = 100;
        rateLimitRemainingUser = 50;
        resetUser = 1234567890L;

        FeatureSpecificRateLimitStatus featureSpecificRateLimitStatus = new FeatureSpecificRateLimitStatusJSONImpl();
        featureSpecificRateLimitLimitUser = 50;
        featureSpecificRateLimitRemainingUser = 20;
        featureSpecificRateLimitResetUser = 1337L;

        TwitterException exception = new TwitterException("Test Exception Message", response);
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testExceptionCodeFilter() throws Exception {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(400);
        response.setResponseHeader("X-Twitter-Status", "321");

        TwitterException exception = new TwitterException("Test Exception Message", response);
        String exceptionCode = exception.getExceptionCode();
        assertEquals("321", exceptionCode);
    }

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertEquals(-1, exception.getStatusCode());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(400);

        TwitterException exception = new TwitterException("Test Exception Message", response);
        exception.getRetryAfter();
    }
}