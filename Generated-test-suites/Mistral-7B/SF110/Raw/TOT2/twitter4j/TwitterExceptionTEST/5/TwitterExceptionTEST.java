package twitter4j.test;

import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.DefaultHttpResponse;
import twitter4j.internal.http.HttpResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        String message = "Test Message";
        TwitterException twitterException = new TwitterException(message);
        assertEquals(message, twitterException.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException();
        TwitterException twitterException = new TwitterException(cause);
        assertTrue(twitterException.getCause() instanceof IOException);
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        String message = "Test Message";
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        TwitterException twitterException = new TwitterException(message, response);
        assertEquals(message, twitterException.getMessage());
        assertEquals(HttpResponseStatus.OK, twitterException.getStatusCode());
    }

    @Test(expected = IllegalStateException.class)
    public void testTwitterExceptionWithInvalidStatusCode() {
        String message = "Test Message";
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
        TwitterException twitterException = new TwitterException(message, response);
        twitterException.setStatusCode(0);
    }

    @Test(expected = IllegalStateException.class)
    public void testTwitterExceptionWithoutRateLimitStatus() {
        String message = "Test Message";
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        TwitterException twitterException = new TwitterException(message, response);
        twitterException.setRateLimitStatus(null);
    }

    @Test
    public void testTwitterExceptionWithRateLimitExceeded() {
        String message = "Test Message";
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.TOO_MANY_REQUESTS);
        TwitterException twitterException = new TwitterException(message, response);
        assertTrue(twitterException.exceededRateLimitation());
    }

    @Test(expected = IllegalStateException.class)
    public void testTwitterExceptionWithInvalidRetryAfter() {
        String message = "Test Message";
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        TwitterException twitterException = new TwitterException(message, response);
        twitterException.setRetryAfter(-1);
    }

    @Test(expected = IllegalStateException.class)
    public void testTwitterExceptionWithoutFeatureSpecificRateLimitStatus() {
        String message = "Test Message";
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        TwitterException twitterException = new TwitterException(message, response);
        twitterException.setFeatureSpecificRateLimitStatus(null);
    }

    @Test
    public void testTwitterExceptionWithNetworkIssue() {
        String message = "Test Message";
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        TwitterException twitterException = new TwitterException(message, response);
        assertTrue(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testTwitterExceptionWithNestedException() {
        String message = "Test Message";
        Exception cause = new IOException();
        TwitterException twitterException = new TwitterException(message, cause);
        assertTrue(twitterException.isNested());
    }

    @Test
    public void testTwitterExceptionEquals() {
        String message1 = "Test Message 1";
        String message2 = "Test Message 2";
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        TwitterException twitterException1 = new TwitterException(message1, response);
        TwitterException twitterException2 = new TwitterException(message2, response);
        assertTrue(twitterException1.equals(twitterException2));
    }

    @Test
    public void testTwitterExceptionResourceNotFound() {
        String message = "Test Message";
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
        TwitterException twitterException = new TwitterException(message, response);
        assertTrue(twitterException.resourceNotFound());
    }

}