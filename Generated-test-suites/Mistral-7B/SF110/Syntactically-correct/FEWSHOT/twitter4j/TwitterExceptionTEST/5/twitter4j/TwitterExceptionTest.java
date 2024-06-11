package twitter4j;

import static org.junit.Assert.*;

import twitter4j.internal.http.HttpResponse;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusJSONImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TwitterExceptionTest {

    @Test
    public void testTwitterException() {
        TwitterException e = new TwitterException("Test Exception");
        assertEquals("Test Exception", e.getMessage());
        assertEquals(-1, e.getStatusCode());
        assertNull(e.getRateLimitStatus());
        assertNull(e.getFeatureSpecificRateLimitStatus());
        assertFalse(e.isCausedByNetworkIssue());
        assertFalse(e.exceededRateLimitation());
        assertFalse(e.resourceNotFound());
        assertNull(e.getExceptionCode());
        assertFalse(e.isNested());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        IOException ioException = new IOException("Test IOException");
        TwitterException e = new TwitterException("Test Exception", ioException);
        assertEquals("Test Exception", e.getMessage());
        assertEquals(-1, e.getStatusCode());
        assertNull(e.getRateLimitStatus());
        assertNull(e.getFeatureSpecificRateLimitStatus());
        assertTrue(e.isCausedByNetworkIssue());
        assertFalse(e.exceededRateLimitation());
        assertFalse(e.resourceNotFound());
        assertEquals("Test IOException", e.getCause().getMessage());
        assertNull(e.getExceptionCode());
        assertFalse(e.isNested());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse mockResponse = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 400;
            }

            @Override
            public String getResponseHeader(String name) {
                switch (name) {
                    case "Retry-After":
                        return "120";
                    default:
                        return null;
                }
            }
        };
        InputStream inputStream = new ByteArrayInputStream("{}".getBytes());
        RateLimitStatusJSONImpl mockRateLimitStatus = new RateLimitStatusJSONImpl();
        mockRateLimitStatus.parse(inputStream);
        TwitterException e = new TwitterException("Test Exception", mockResponse);
        assertEquals("Test Exception" + "\n" + "400: The request was invalid. An accompanying error message will explain why. This is the status code will be returned during rate limiting.", e.getMessage());
        assertEquals(400, e.getStatusCode());
        assertEquals(120, e.getRetryAfter());
        assertEquals(mockRateLimitStatus, e.getRateLimitStatus());
        assertNull(e.getFeatureSpecificRateLimitStatus());
        assertFalse(e.isCausedByNetworkIssue());
        assertTrue(e.exceededRateLimitation());
        assertFalse(e.resourceNotFound());
        assertNull(e.getExceptionCode());
        assertFalse(e.isNested());
    }

    @Test
    public void testTwitterExceptionWithHttpResponseAndCause() {
        HttpResponse mockResponse = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }

            @Override
            public String getResponseHeader(String name) {
                switch (name) {
                    case "Retry-After":
                        return "120";
                    default:
                        return null;
                }
            }
        };
        InputStream inputStream = new ByteArrayInputStream("{\"limit\":{\"remaining\":1,\"reset\":1593386256}}".getBytes());
        RateLimitStatusJSONImpl mockRateLimitStatus = new RateLimitStatusJSONImpl();
        mockRateLimitStatus.parse(inputStream);
        TwitterException e = new TwitterException("Test Exception", mockResponse);
        String cause = new IOException("Test IOException").getMessage();
        TwitterException nestedException = new TwitterException("Nested Test Exception");
        nestedException.setNested();
        nestedException.initCause(new IOException("Nested Test IOException"));
        e.initCause(nestedException);
        e.setNested();
        assertEquals("Nested Test Exception" + "\n" + "420: The number of requests you have made exceeds the quota afforded by your assigned rate limit.", e.getMessage());
        assertEquals(420, e.getStatusCode());
        assertEquals(120, e.getRetryAfter());
        assertEquals(mockRateLimitStatus, e.getRateLimitStatus());
        assertNotNull(e.getFeatureSpecificRateLimitStatus());
        assertTrue(e.isCausedByNetworkIssue());
        assertTrue(e.exceededRateLimitation());
        assertFalse(e.resourceNotFound());
        assertEquals(cause, e.getCause().getMessage());
        assertTrue(e.isNested());
    }
}