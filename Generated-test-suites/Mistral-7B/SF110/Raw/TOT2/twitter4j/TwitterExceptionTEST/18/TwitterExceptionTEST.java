
import org.junit.Assert;
import org.junit.Test;
import twitter4j.*;

public class TwitterExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        TwitterException twitterException = new TwitterException("Test Error Message");
        assertEquals("Test Error Message", twitterException.getMessage());
    }

    @Test
    public void testConstructorWithCause() {
        IOException ioException = new IOException();
        TwitterException twitterException = new TwitterException(ioException);
        assertTrue(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testConstructorWithMessageAndHttpResponse() {
        HttpResponse httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        TwitterException twitterException = new TwitterException("Test Error Message", httpResponse);
        assertEquals(HttpResponseStatus.OK.code(), twitterException.getStatusCode());
    }

    @Test
    public void testConstructorWithMessageCauseStatusCode() {
        IOException ioException = new IOException();
        TwitterException twitterException = new TwitterException("Test Error Message", ioException, 400);
        assertTrue(twitterException.isCausedByNetworkIssue());
        assertEquals(400, twitterException.getStatusCode());
    }

    @Test
    public void testNegativeRetryAfterValue() {
        TwitterException twitterException = new TwitterException("Test Error Message", new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST), -1);
        assertEquals(-1, twitterException.getRetryAfter());
    }

    @Test
    public void testNullRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test Error Message", new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK));
        assertNull(twitterException.getRateLimitStatus());
    }

    @Test
    public void testNullFeatureSpecificRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test Error Message", new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK));
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithNullMsg() {
        new TwitterException(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithNullCause() {
        new TwitterException(null, new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK));
    }

    @Test(expected = NumberFormatException.class)
    public void testNumberFormatException() {
        TwitterException twitterException = new TwitterException("Test Error Message", new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK), "abc");
    }

    @Test
    public void testIllegalStateException() {
        TwitterException twitterException = new TwitterException("Test Error Message", new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK));
        try {
            twitterException.getRetryAfter();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // expected
        }
    }
}
