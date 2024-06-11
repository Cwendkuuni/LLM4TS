package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.*;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.RateLimitStatusJSONImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        Exception cause = new IOException("Test IOException Message");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse response = new HttpResponse(420, null, null);
        TwitterException exception = new TwitterException("Test Exception Message", response);
        assertEquals(420, exception.getStatusCode());
        assertEquals(RateLimitStatusJSONImpl.createFromResponseHeader(response), exception.getRateLimitStatus());
        assertEquals(RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(response), exception.getFeatureSpecificRateLimitStatus());
        assertEquals(-1, exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithHttpResponseAndCause() {
        HttpResponse response = new HttpResponse(420, null, null);
        IOException cause = new IOException("Test IOException Message");
        TwitterException exception = new TwitterException("Test Exception Message", response, cause);
        assertEquals(420, exception.getStatusCode());
        assertEquals(RateLimitStatusJSONImpl.createFromResponseHeader(response), exception.getRateLimitStatus());
        assertEquals(RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(response), exception.getFeatureSpecificRateLimitStatus());
        assertEquals(-1, exception.getRetryAfter());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithHttpResponseStatusCode() {
        HttpResponse response = new HttpResponse(404, null, null);
        TwitterException exception = new TwitterException("Test Exception Message", null, 404);
        assertEquals(404, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getCause());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("Test IOException Message");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        assertTrue(exception.isCausedByNetworkIssue());

        exception = new TwitterException("Test Exception Message");
        assertFalse(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse response = new HttpResponse(420, null, null);
        TwitterException exception = new TwitterException("Test Exception Message", response);
        assertTrue(exception.exceededRateLimitation());

        response = new HttpResponse(400, null, null);
        exception = new TwitterException("Test Exception Message", response);
        assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse response = new HttpResponse(404, null, null);
        TwitterException exception = new TwitterException("Test Exception Message", response);
        assertTrue(exception.resourceNotFound());

        response = new HttpResponse(400, null, null);
        exception = new TwitterException("Test Exception Message", response);
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        HttpResponse response = new HttpResponse(420, null, null);
        TwitterException exception = new TwitterException("Test Exception Message", response);
        String exceptionCode = exception.getExceptionCode();
        // The actual hex value may vary, this is just a test example
        assertEquals("40:01:02:03:04:05:06:07:08:09:0a:0b:0c:0d:0e:0f:10:11:12:13:14:15:16:17:18:19:1a:1b:1c:1d:1e:1f:20:21:22:23:24:25:26:27:28:29:2a:2b:2c:2d:2e:2f:30:31:32:33:34:35:36:37:38:39:3a:3b:3c:3d:3e:3f:40:41:42:43:44:45:46:47:48:49:4a:4b:4c:4d:4e:4f:50:51:52:53:54:55:56:57:58:59:5a:5b:5c:5d:5e:5f:60:61:62:63:64:65:66:67:68:69:6a:6b:6c:6d:6e:6f:70:71:72:73:74:75:76:77:78:79:7a:7b:7c:7d:7e:7f", exceptionCode);
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception Message 1", new HttpResponse(420, null, null));
        TwitterException exception2 = new TwitterException("Test Exception Message 2", new HttpResponse(420, null, null));
        TwitterException exception3 = new TwitterException("Test Exception Message 1", new HttpResponse(404, null, null));

        assertEquals(exception1, exception1);
        assertNotEquals(exception1, exception2);
        assertNotEquals(exception1, exception3);

        exception1.setNested();
        exception2.setNested();
        assertEquals(exception1, exception2);
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test Exception Message 1", new HttpResponse(420, null, null));
        TwitterException exception2 = new TwitterException("Test Exception Message 2", new HttpResponse(420, null, null));
        TwitterException exception3 = new TwitterException("Test Exception Message 1", new HttpResponse(404, null, null));

        int hashCode1 = exception1.hashCode();
        int hashCode2 = exception2.hashCode();
        int hashCode3 = exception3.hashCode();

        // The actual hash codes may vary, this is just a test example
        assertEquals(hashCode1, hashCode1);
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);

        exception1.setNested();
        exception2.setNested();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Exception Message", new HttpResponse(420, null, null));
        String toString = exception.toString();
        // The actual output may vary, this is just a test example
        assertTrue(toString.contains("TwitterException{statusCode=" + 420 + ", retryAfter=" + (-1) + ", rateLimitStatus=" + null + ", version="));
    }

    @Test
    public void testGetCauseFromHttpResponse() {
        HttpResponse response = new HttpResponse(420, null, null);
        String causeMessage = "There was an error: ";
        InputStream inputStream = new ByteArrayInputStream(causeMessage.getBytes());
        response.setEntity(inputStream);
        TwitterException exception = new TwitterException("Test Exception Message", response);
        assertEquals(causeMessage, exception.getCause().getMessage());

        response = new HttpResponse(400, null, null);
        inputStream = new ByteArrayInputStream(new byte[0]);
        response.setEntity(inputStream);
        exception = new TwitterException("Test Exception Message", response);
        assertNull(exception.getCause());
    }

    @Test
    public void testCreateFromResponseHeader() throws IOException {
        HttpResponse response = new HttpResponse(420, null, null);
        response.setResponseHeader("Retry-After", "120");
        RateLimitStatus rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(response);
        assertEquals(120, rateLimitStatus.getSeconds());

        response = new HttpResponse(420, null, null);
        response.setResponseHeader("Retry-After", "120, 30");
        rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(response);
        assertEquals(120, rateLimitStatus.getSeconds());
        assertEquals(30, rateLimitStatus.getSecondsRemaining());

        response = new HttpResponse(420, null, null);
        response.setResponseHeader("Retry-After", "120, 30, 60");
        rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(response);
        assertEquals(120, rateLimitStatus.getSeconds());
        assertEquals(30, rateLimitStatus.getSecondsRemaining());
        assertEquals(60, rateLimitStatus.getSecondsUntilReset());

        response = new HttpResponse(420, null, null);
        response.setResponseHeader("Retry-After", "120, 30, 60, 120");
        rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(response);
        assertEquals(120, rateLimitStatus.getSeconds());
        assertEquals(30, rateLimitStatus.getSecondsRemaining());
        assertEquals(60, rateLimitStatus.getSecondsUntilReset());
        assertEquals(120, rateLimitStatus.getSecondsUntilResetNext());
    }

    @Test
    public void testCreateFeatureSpecificRateLimitStatusFromResponseHeader() throws IOException {
        HttpResponse response = new HttpResponse(420, null, null);
        response.setResponseHeader("X-Rate-Limit-Bucket-Name", "testBucket");
        response.setResponseHeader("X-Rate-Limit-Limit-Remaining", "100");
        response.setResponseHeader("X-Rate-Limit-Reset", "2022-12-31T23:59:59Z");
        FeatureSpecificRateLimitStatus featureSpecificRateLimitStatus = RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(response);
        assertEquals("testBucket", featureSpecificRateLimitStatus.getBucket());
        assertEquals(100, featureSpecificRateLimitStatus.getLimitRemaining());
        assertEquals("2022-12-31T23:59:59Z", featureSpecificRateLimitStatus.getReset());

        response = new HttpResponse(420, null, null);
        response.setResponseHeader("X-Rate-Limit-Bucket-Name", "testBucket");
        response.setResponseHeader("X-Rate-Limit-Limit-Remaining", "100");
        response.setResponseHeader("X-Rate-Limit-Reset", "2022-12-31T23:59:59Z, 2023-01-01T00:00:00Z");
        featureSpecificRateLimitStatus = RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(response);
        assertEquals("testBucket", featureSpecificRateLimitStatus.getBucket());
        assertEquals(100, featureSpecificRateLimitStatus.getLimitRemaining());
        assertEquals("2022-12-31T23:59:59Z", featureSpecificRateLimitStatus.getReset());
        assertEquals("2023-01-01T00:00:00Z", featureSpecificRateLimitStatus.getResetNext());
    }
}