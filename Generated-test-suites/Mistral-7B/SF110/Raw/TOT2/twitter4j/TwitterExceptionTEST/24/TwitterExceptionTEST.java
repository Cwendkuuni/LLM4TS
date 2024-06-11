import static org.junit.Assert.*;
import twitter4j.*;
import twitter4j.internal.http.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionEmptyConstructor() {
        TwitterException exception = new TwitterException("");
        assertEquals("", exception.getMessage());
        assertNull(exception.getCause());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isNested());
        assertNull(exception.getRetryAfter());
        assertEquals(-1, exception.getStatusCode());
    }

    @Test
    public void testTwitterExceptionStringConstructor() {
        TwitterException exception = new TwitterException("Test Message");
        assertEquals("Test Message", exception.getMessage());
        assertNull(exception.getCause());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isNested());
        assertNull(exception.getRetryAfter());
        assertEquals(-1, exception.getStatusCode());
    }

    @Test
    public void testTwitterExceptionHttpResponseConstructorWithValidStatusCode() {
        HttpResponse res = new HttpResponse(200, "Ok", new ByteArrayInputStream("".getBytes()));
        TwitterException exception = new TwitterException("Test Message", res);
        assertEquals("Test Message:Ok:[]", exception.getMessage());
        assertNull(exception.getCause());
        assertNotNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isNested());
        assertNull(exception.getRetryAfter());
        assertEquals(200, exception.getStatusCode());
    }

    @Test
    public void testTwitterExceptionHttpResponseConstructorWithInvalidStatusCode() {
        HttpResponse res = new HttpResponse(-1, "Error", new ByteArrayInputStream("".getBytes()));
        TwitterException exception = new TwitterException("Test Message", res);
        assertEquals("Test Message:Error:[]", exception.getMessage());
        assertNull(exception.getCause());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isNested());
        assertNull(exception.getRetryAfter());
        assertEquals(-1, exception.getStatusCode());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        HttpResponse res = new HttpResponse(408, "Request Timeout", new ByteArrayInputStream("".getBytes()));
        TwitterException exception = new TwitterException("Test Message", res);
        assertTrue(exception.isCausedByNetworkIssue());

        IOException cause = new IOException("Test IOException");
        TwitterException nestedException = new TwitterException("Test Message", cause);
        nestedException.setNested();
        TwitterException exception2 = new TwitterException("Test Message", nestedException);
        assertFalse(exception2.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse res = new HttpResponse(429, "Too Many Requests", new ByteArrayInputStream("".getBytes()));
        TwitterException exception = new TwitterException("Test Message", res);
        assertTrue(exception.exceededRateLimitation());

        HttpResponse res2 = new HttpResponse(400, "Bad Request", new ByteArrayInputStream("".getBytes()));
        TwitterException exception2 = new TwitterException("Test Message", res2);
        assertFalse(exception2.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse res = new HttpResponse(404, "Not Found", new ByteArrayInputStream("".getBytes()));
        TwitterException exception = new TwitterException("Test Message", res);
        assertTrue(exception.resourceNotFound());

        HttpResponse res2 = new HttpResponse(401, "Unauthorized", new ByteArrayInputStream("".getBytes()));
        TwitterException exception2 = new TwitterException("Test Message", res2);
        assertFalse(exception2.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        HttpResponse res = new HttpResponse(429, "Too Many Requests", new ByteArrayInputStream("".getBytes()));
        TwitterException exception = new TwitterException("Test Message", res);
        assertEquals("429:Too Many Requests:[]", exception.getExceptionCode());
    }
}