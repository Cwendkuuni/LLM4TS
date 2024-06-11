package twitter4j; // Import necessary packages here if needed

import static org.junit.Assert.*;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionBasicConstruction() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionExceptionConstruction() {
        Exception cause = new IOException("IOException Test Message");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isNested());
        assertEquals("IOException Test Message", exception.getCause().getMessage());
    }

    @Test
    public void testTwitterExceptionHttpResponseConstruction() {
        HttpResponse res = new HttpResponse(400, "{}");
        TwitterException exception = new TwitterException("Test Exception Message", res);
        assertEquals("Test Exception Message" + System.lineSeparator() + getCause(res), exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionHttpResponseWithRateLimitInfoConstruction() {
        HttpResponse res = new HttpResponse(420, "{\"rate_limit_status\":{\"limit\":100,\"remaining\":95,\"reset_timestamp\":1597723179}}");
        TwitterException exception = new TwitterException("Test Exception Message", res);
        assertEquals("Test Exception Message" + System.lineSeparator() + getCause(res), exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertEquals(100, exception.getRateLimitStatus().getLimit());
        assertEquals(95, exception.getRateLimitStatus().getRemaining());
        assertEquals(1597723179, exception.getRateLimitStatus().getResetTimestamp());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionHttpResponseWithFeatureSpecificRateLimitInfoConstruction() {
        HttpResponse res = new HttpResponse(420, "{\"rate_limit_status\":{\"limit\":100,\"remaining\":95,\"reset_timestamp\":1597723179},\"feature_specific_rate_limit_status\":{\"limit\":50,\"remaining\":48,\"seconds_left\":20}}");
        TwitterException exception = new TwitterException("Test Exception Message", res);
        assertEquals("Test Exception Message" + System.lineSeparator() + getCause(res), exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertEquals(100, exception.getRateLimitStatus().getLimit());
        assertEquals(95, exception.getRateLimitStatus().getRemaining());
        assertEquals(1597723179, exception.getRateLimitStatus().getResetTimestamp());
        assertEquals(50, exception.getFeatureSpecificRateLimitStatus().getLimit());
        assertEquals(48, exception.getFeatureSpecificRateLimitStatus().getRemaining());
        assertEquals(20, exception.getFeatureSpecificRateLimitStatus().getSecondsLeft());
        assertFalse(exception.isNested());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("Network Error");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        assertTrue(exception.isCausedByNetworkIssue());

        exception = new TwitterException("Test Exception Message");
        assertFalse(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse res = new HttpResponse(400, "{}");
        TwitterException exception = new TwitterException("Test Exception Message", res);
        assertFalse(exception.exceededRateLimitation());

        res = new HttpResponse(420, "{}");
        exception = new TwitterException("Test Exception Message", res);
        assertTrue(exception.exceededRateLimitation());

        res = new HttpResponse(401, "{}");
        exception = new TwitterException("Test Exception Message", res);
        assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse res = new HttpResponse(404, "{}");
        TwitterException exception = new TwitterException("Test Exception Message", res);
        assertTrue(exception.resourceNotFound());

        res = new HttpResponse(400, "{}");
        exception = new TwitterException("Test Exception Message", res);
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        HttpResponse res = new HttpResponse(400, "{}");
        TwitterException exception = new TwitterException("Test Exception Message", res);
        String exceptionCode = exception.getExceptionCode();
        assertTrue(exceptionCode.length() > 0);
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception Message 1");
        TwitterException exception2 = new TwitterException("Test Exception Message 1");
        TwitterException exception3 = new TwitterException("Test Exception Message 2");

        assertTrue(exception1.equals(exception1));
        assertTrue(exception1.equals(exception2));
        assertFalse(exception1.equals(exception3));

        exception1.setNested();
        assertFalse(exception1.equals(exception2));
        assertTrue(exception1.equals(exception1));

        exception1.setStatusCode(420);
        exception1.setRetryAfter(5);
        exception1.setRateLimitStatus(new RateLimitStatus(10, 5, 1597723179));
        exception2.setStatusCode(420);
        exception2.setRetryAfter(5);
        exception2.setRateLimitStatus(new RateLimitStatus(10, 5, 1597723179));
        assertTrue(exception1.equals(exception2));

        exception1.setRateLimitStatus(new RateLimitStatus(10, 5, 1597723179));
        exception2.setRateLimitStatus(new RateLimitStatus(11, 5, 1597723179));
        assertFalse(exception1.equals(exception2));
    }

    @Test
    public void hashCodeTest() {
        TwitterException exception1 = new TwitterException("Test Exception Message 1");
        TwitterException exception2 = new TwitterException("Test Exception Message 1");
        TwitterException exception3 = new TwitterException("Test Exception Message 2");

        int hashCode1 = exception1.hashCode();
        int hashCode2 = exception2.hashCode();
        int hashCode3 = exception3.hashCode();

        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);

        exception1.setNested();
        hashCode1 = exception1.hashCode();
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);

        exception1.setStatusCode(420);
        exception1.setRetryAfter(5);
        exception1.setRateLimitStatus(new RateLimitStatus(10, 5, 1597723179));
        exception2.setStatusCode(420);
        exception2.setRetryAfter(5);
        exception2.setRateLimitStatus(new RateLimitStatus(10, 5, 1597723179));
        assertEquals(hashCode1, hashCode2);

        exception1.setRateLimitStatus(new RateLimitStatus(10, 5, 1597723179));
        exception2.setRateLimitStatus(new RateLimitStatus(11, 5, 1597723179));
        assertNotEquals(hashCode1, hashCode2);
    }

    private String getCause(HttpResponse res) {
        final int statusCode = res.getStatusCode();
        String cause = null;
        switch (statusCode) {
            case 304: {
                cause = "There was no new data to return.";
                break;
            }
            case 400: {
                cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
                break;
            }
            case 401: {
                cause = "Authentication credentials were missing or incorrect.";
                break;
            }
            case 403: {
                cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
                break;
            }
            case 404: {
                cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
                break;
            }
            case 406: {
                cause = "Returned by the Search API when an invalid format is specified in the request.";
                break;
            }
            case 420: {
                cause = "The number of requests you have made exceeds the quota afforded by your assigned rate limit.";
                break;
            }
            case 500: {
                cause = "Something is broken.  Please post to the group so the Twitter team can investigate.";
                break;
            }
            case 502: {
                cause = "Twitter is down or being upgraded.";
                break;
            }
            case 503: {
                cause = "Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
                break;
            }
            default: {
                cause = "";
                break;
            }
        }
        return statusCode + ":" + cause;
    }
}