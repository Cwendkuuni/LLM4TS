import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.HttpResponse;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.internal.http.DefaultHttpResponse;

public class TwitterExceptionTest {
    @Test
    public void testGetStatusCode() {
        TwitterException twitterException = new TwitterException("sample message");
        assertEquals(-1, twitterException.getStatusCode());

        HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN);
        TwitterException twitterExceptionWithStatusCode = new TwitterException("sample message", res);
        assertEquals(403, twitterExceptionWithStatusCode.getStatusCode());
    }

    // Test for various statusCode values
    @Test
    public void testGetStatusCodeWithDifferentStatusCode() {
        HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        TwitterException twitterExceptionWithStatusCode = new TwitterException("sample message", res);
        assertEquals(500, twitterExceptionWithStatusCode.getStatusCode());

        // Test for negative retryAfter value
        HttpResponse resWithNegativeRetryAfter = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.TOO_MANY_REQUESTS);
        twitterExceptionWithStatusCode = new TwitterException("sample message", resWithNegativeRetryAfter);
        twitterExceptionWithStatusCode.setRetryAfter(-1);
        assertEquals(-1, twitterExceptionWithStatusCode.getRetryAfter());

        // Test for null and invalid rateLimitStatus and featureSpecificRateLimitStatus
        TwitterException twitterExceptionWithNullRateLimitStatus = new TwitterException("sample message");
        assertNull(twitterExceptionWithNullRateLimitStatus.getRateLimitStatus());

        TwitterException twitterExceptionWithInvalidRateLimitStatus = new TwitterException("sample message", new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_GATEWAY));
        assertNotNull(twitterExceptionWithInvalidRateLimitStatus.getRateLimitStatus());
        assertEquals(RateLimitStatus.LIMIT_NOT_REACHED, twitterExceptionWithInvalidRateLimitStatus.getRateLimitStatus());
    }

    // Test for null and invalid HttpResponse objects
    @Test
    public void testGetStatusCodeWithNullAndInvalidHttpResponse() {
        TwitterException twitterExceptionWithNullHttpResponse = new TwitterException("sample message");
        assertNull(twitterExceptionWithNullHttpResponse.getRateLimitStatus());

        HttpResponse invalidHttpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, null);
        TwitterException twitterExceptionWithInvalidHttpResponse = new TwitterException("sample message", invalidHttpResponse);
        assertNull(twitterExceptionWithInvalidHttpResponse.getRateLimitStatus());
    }

    // Test for nested exceptions
    @Test
    public void testGetStatusCodeWithNestedException() {
        IOException exception = new IOException("network error");
        TwitterException twitterExceptionWithNestedException = new TwitterException("sample message", exception);
        twitterExceptionWithNestedException.setNested();
        assertEquals(true, twitterExceptionWithNestedException.isCausedByNetworkIssue());
    }
}