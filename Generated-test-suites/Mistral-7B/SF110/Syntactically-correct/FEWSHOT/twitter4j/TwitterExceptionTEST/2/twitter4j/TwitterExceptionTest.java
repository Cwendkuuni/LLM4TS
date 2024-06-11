package twitter4j.test;

import static org.junit.Assert.*;
import static twitter4j.internal.http.StatusCodes.*;

import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertEquals("Test Exception Message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        IOException cause = new IOException("Cause Exception Message");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse response = new HttpResponse() {{
            setStatusCode(400);
            setStatusMessage("Bad Request");
            setResponseHeader("Retry-After", "60");
        }};
        TwitterException exception = new TwitterException("Test Exception Message", response);
        assertEquals(400, exception.getStatusCode());
        assertEquals("Bad Request", exception.getMessage());
        assertEquals(RateLimitStatus.BAD_REQUEST, exception.getRateLimitStatus());
        assertEquals(60, exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithHttpResponseAndCause() {
        IOException cause = new IOException("Cause Exception Message");
        HttpResponse response = new HttpResponse() {{
            setStatusCode(400);
            setStatusMessage("Bad Request");
            setResponseHeader("Retry-After", "60");
        }};
        TwitterException exception = new TwitterException("Test Exception Message", response, cause);
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(400, exception.getStatusCode());
        assertEquals("Bad Request", exception.getMessage());
        assertEquals(RateLimitStatus.BAD_REQUEST, exception.getRateLimitStatus());
        assertEquals(60, exception.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("Cause Exception Message");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        assertTrue(exception.isCausedByNetworkIssue());

        Exception nonNetworkException = new Exception("Test Exception Message");
        TwitterException exception2 = new TwitterException("Test Exception Message", nonNetworkException);
        assertFalse(exception2.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse response = new HttpResponse() {{
            setStatusCode(420);
            setStatusMessage("Rate Limit Exceeded");
            setResponseHeader("Retry-After", "60");
        }};
        TwitterException exception = new TwitterException("Test Exception Message", response);
        assertTrue(exception.exceededRateLimitation());

        HttpResponse response2 = new HttpResponse() {{
            setStatusCode(400);
            setStatusMessage("Bad Request");
            setResponseHeader("Retry-After", null);
        }};
        TwitterException exception2 = new TwitterException("Test Exception Message", response2);
        assertFalse(exception2.exceededRateLimitation());

        HttpResponse response3 = new HttpResponse() {{
            setStatusCode(404);
            setStatusMessage("Resource Not Found");
            setResponseHeader("Retry-After", null);
        }};
        TwitterException exception3 = new TwitterException("Test Exception Message", response3);
        assertFalse(exception3.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse response = new HttpResponse() {{
            setStatusCode(404);
            setStatusMessage("Resource Not Found");
        }};
        TwitterException exception = new TwitterException("Test Exception Message", response);
        assertTrue(exception.resourceNotFound());

        HttpResponse response2 = new HttpResponse() {{
            setStatusCode(400);
            setStatusMessage("Bad Request");
        }};
        TwitterException exception2 = new TwitterException("Test Exception Message", response2);
        assertFalse(exception2.resourceNotFound());

        HttpResponse response3 = new HttpResponse() {{
            setStatusCode(420);
            setStatusMessage("Rate Limit Exceeded");
        }};
        TwitterException exception3 = new TwitterException("Test Exception Message", response3);
        assertFalse(exception3.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        HttpResponse response = new HttpResponse() {{
            setStatusCode(400);
            setResponseHeader("X-Twitter-Rate-Limit-Limit", "180");
            setResponseHeader("X-Twitter-Rate-Limit-Remaining", "165");
            setResponseHeader("X-Twitter-Rate-Limit-Reset", "1401240545");
        }};
        TwitterException exception = new TwitterException("Test Exception Message", response);
        assertEquals("54014001801651401240545", exception.getExceptionCode());

        HttpResponse response2 = new HttpResponse() {{
            setStatusCode(420);
            setResponseHeader("X-Twitter-Rate-Limit-Limit", "180");
            setResponseHeader("X-Twitter-Rate-Limit-Remaining", "165");
            setResponseHeader("X-Twitter-Rate-Limit-Reset", "1401240545");
            setResponseHeader("X-Twitter-Rate-Limit-Burst-Remaining", "10");
        }};
        TwitterException exception2 = new TwitterException("Test Exception Message", response2);
        assertEquals("5401400180165140124054554014010", exception2.getExceptionCode());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception Message 1", new HttpResponse() {{
            setStatusCode(400);
        }});
        TwitterException exception2 = new TwitterException("Test Exception Message 2", new HttpResponse() {{
            setStatusCode(400);
        }});
        TwitterException exception3 = new TwitterException("Test Exception Message 1", new HttpResponse() {{
            setStatusCode(401);
        }});
        TwitterException exception4 = new TwitterException("Test Exception Message 1", new HttpResponse() {{
            setStatusCode(400);
            setResponseHeader("Retry-After", "60");
        }});

        Map<Integer, TwitterException> exceptionsMap = new HashMap<>();
        exceptionsMap.put(400, exception1);
        exceptionsMap.put(401, exception3);

        assertTrue(exception1.equals(exception1));
        assertTrue(exception1.equals(exception4));
        assertFalse(exception1.equals(exception2));
        assertFalse(exception1.equals(exception3));
        assertFalse(exception1.equals(new Object()));

        assertTrue(exception2.equals(exception2));
        assertFalse(exception2.equals(exception1));
        assertFalse(exception2.equals(exception3));
        assertFalse(exception2.equals(new Object()));

        assertTrue(exception3.equals(exception3));
        assertFalse(exception3.equals(exception1));
        assertFalse(exception3.equals(exception2));
        assertFalse(exception3.equals(new Object()));

        assertTrue(exceptionsMap.containsKey(400));
        assertTrue(exceptionsMap.get(400).equals(exception1));
        assertFalse(exceptionsMap.containsKey(401));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test Exception Message 1", new HttpResponse() {{
            setStatusCode(400);
        }});
        TwitterException exception2 = new TwitterException("Test Exception Message 2", new HttpResponse() {{
            setStatusCode(400);
        }});
        TwitterException exception3 = new TwitterException("Test Exception Message 1", new HttpResponse() {{
            setStatusCode(401);
        }});
        TwitterException exception4 = new TwitterException("Test Exception Message 1", new HttpResponse() {{
            setStatusCode(400);
            setResponseHeader("Retry-After", "60");
        }});

        assertTrue(exception1.hashCode() == exception4.hashCode());
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
        assertNotEquals(exception1.hashCode(), exception3.hashCode());
    }

    @Test
    public void testToString() {
        HttpResponse response = new HttpResponse() {{
            setStatusCode(400);
            setStatusMessage("Bad Request");
            setResponseHeader("Retry-After", "60");
        }};
        TwitterException exception = new TwitterException("Test Exception Message", response);
        String expected = "TwitterException{statusCode=400, retryAfter=60, rateLimitStatus=null, version=Twitter4J-3.0.8}";
        assertEquals(expected, exception.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        HttpResponse response = new HttpResponse() {{
            setStatusCode(400);
        }};
        TwitterException exception = new TwitterException("Test Exception Message", response);
        exception.getRetryAfter();
    }
}