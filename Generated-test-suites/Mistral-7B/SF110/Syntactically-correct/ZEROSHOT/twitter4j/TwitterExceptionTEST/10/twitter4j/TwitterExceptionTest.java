package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.StatusLine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test Exception Message");
        String expectedMessage = "Test Exception Message";
        String actualMessage = exception.getMessage();
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testTwitterExceptionWithCause() {
        IOException cause = new IOException("Test IOException Message");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        String expectedIOExceptionMessage = "Test IOException Message";
        String actualIOExceptionMessage = cause.getMessage();
        String expectedMessage = "Test Exception Message";
        String actualMessage = exception.getMessage();
        Assert.assertEquals(expectedMessage, actualMessage);
        Assert.assertEquals(expectedIOExceptionMessage, actualIOExceptionMessage);
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponse();
        StatusLine statusLine = new StatusLine(new ByteArrayInputStream("HTTP/1.1 400 Bad Request\r\n".getBytes()), 400);
        httpResponse.setStatusLine(statusLine);

        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        int expectedStatusCode = 400;
        int actualStatusCode = exception.getStatusCode();
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponse();
        StatusLine statusLine = new StatusLine(new ByteArrayInputStream("HTTP/1.1 400 Bad Request\r\n".getBytes()), 400);
        httpResponse.setStatusLine(statusLine);

        IOException cause = new IOException("Test IOException Message");
        TwitterException exception = new TwitterException("Test Exception Message", cause, 400);
        String expectedIOExceptionMessage = "Test IOException Message";
        String actualIOExceptionMessage = cause.getMessage();
        int expectedStatusCode = 400;
        int actualStatusCode = exception.getStatusCode();
        String expectedMessage = "Test Exception Message";
        String actualMessage = exception.getMessage();

        Assert.assertEquals(expectedMessage, actualMessage);
        Assert.assertEquals(expectedIOExceptionMessage, actualIOExceptionMessage);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetRateLimitStatus() {
        HttpResponse httpResponse = new HttpResponse();
        StatusLine statusLine = new StatusLine(new ByteArrayInputStream("HTTP/1.1 420 Rate Limit Exceeded\r\n".getBytes()), 420);
        httpResponse.setStatusLine(statusLine);
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);

        RateLimitStatus rateLimitStatus = exception.getRateLimitStatus();
        Assert.assertNotNull(rateLimitStatus);
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        HttpResponse httpResponse = new HttpResponse();
        StatusLine statusLine = new StatusLine(new ByteArrayInputStream("HTTP/1.1 420 Rate Limit Exceeded\r\n".getBytes()), 420);
        httpResponse.setStatusLine(statusLine);
        httpResponse.setResponseHeader("X-Rate-Limit-Reset", "1527152338");
        httpResponse.setResponseHeader("X-Rate-Limit-Limit", "15");
        httpResponse.setResponseHeader("X-Twitter-Retry-After", "5");

        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);

        RateLimitStatus rateLimitStatus = exception.getRateLimitStatus();
        long expectedReset = 1527152338L;
        long actualReset = rateLimitStatus.getReset();
        int expectedLimit = 15;
        int actualLimit = rateLimitStatus.getLimit();
        int expectedRemaining = rateLimitStatus.getRemaining();
        int actualRemaining = rateLimitStatus.getRemaining();

        Assert.assertEquals(expectedReset, actualReset);
        Assert.assertEquals(expectedLimit, actualLimit);
        Assert.assertEquals(expectedRemaining, actualRemaining);

        RateLimitStatus featureSpecificRateLimitStatus = exception.getFeatureSpecificRateLimitStatus();
        long expectedFeatureReset = 1527152338L;
        long actualFeatureReset = featureSpecificRateLimitStatus.getReset();
        int expectedFeatureLimit = 15;
        int actualFeatureLimit = featureSpecificRateLimitStatus.getLimit();
        int expectedFeatureRemaining = 15;
        int actualFeatureRemaining = featureSpecificRateLimitStatus.getRemaining();

        Assert.assertEquals(expectedFeatureReset, actualFeatureReset);
        Assert.assertEquals(expectedFeatureLimit, actualFeatureLimit);
        Assert.assertEquals(expectedFeatureRemaining, actualFeatureRemaining);
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException ioException = new IOException("Test IOException Message");
        TwitterException exception = new TwitterException("Test Exception Message", ioException);
        boolean expectedResult = true;
        boolean actualResult = exception.isCausedByNetworkIssue();

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = new HttpResponse();
        StatusLine statusLine = new StatusLine(new ByteArrayInputStream("HTTP/1.1 400 Bad Request\r\n".getBytes()), 400);
        httpResponse.setStatusLine(statusLine);
        httpResponse.setResponseHeader("Retry-After", "5");

        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        boolean expectedResult = true;
        boolean actualResult = exception.exceededRateLimitation();

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse httpResponse = new HttpResponse();
        StatusLine statusLine = new StatusLine(new ByteArrayInputStream("HTTP/1.1 404 Not Found\r\n".getBytes()), 404);
        httpResponse.setStatusLine(statusLine);

        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        boolean expectedResult = true;
        boolean actualResult = exception.resourceNotFound();

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetRetryAfter() {
        HttpResponse httpResponse = new HttpResponse();
        StatusLine statusLine = new StatusLine(new ByteArrayInputStream("HTTP/1.1 420 Rate Limit Exceeded\r\n".getBytes()), 420);
        httpResponse.setStatusLine(statusLine);
        httpResponse.setResponseHeader("Retry-After", "5");

        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        int expectedRetryAfter = 5;
        int actualRetryAfter = exception.getRetryAfter();

        Assert.assertEquals(expectedRetryAfter, actualRetryAfter);
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception Message 1");
        TwitterException exception2 = new TwitterException("Test Exception Message 2");

        boolean expectedResult1 = false;
        boolean actualResult1 = exception1.equals(exception2);

        Assert.assertEquals(expectedResult1, actualResult1);

        TwitterException exception3 = new TwitterException("Test Exception Message 1");

        boolean expectedResult2 = true;
        boolean actualResult2 = exception1.equals(exception3);

        Assert.assertEquals(expectedResult2, actualResult2);
    }

    @Test
    public void testGetHashCode() {
        TwitterException exception1 = new TwitterException("Test Exception Message 1");
        TwitterException exception2 = new TwitterException("Test Exception Message 2");
        TwitterException exception3 = new TwitterException("Test Exception Message 1");

        int expectedHashCode1 = 2002137275;
        int actualHashCode1 = exception1.hashCode();

        int expectedHashCode2 = 1064801913;
        int actualHashCode2 = exception2.hashCode();

        int expectedHashCode3 = 2002137275;
        int actualHashCode3 = exception3.hashCode();

        Assert.assertEquals(expectedHashCode1, actualHashCode1);
        Assert.assertNotEquals(expectedHashCode1, actualHashCode2);
        Assert.assertEquals(expectedHashCode1, actualHashCode3);
    }

    @Test
    public void testToString() {
        HttpResponse httpResponse = new HttpResponse();
        StatusLine statusLine = new StatusLine(new ByteArrayInputStream("HTTP/1.1 420 Rate Limit Exceeded\r\n".getBytes()), 420);
        httpResponse.setStatusLine(statusLine);
        httpResponse.setResponseHeader("Retry-After", "5");

        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);

        String expectedToString = "TwitterException{statusCode=420, retryAfter=5, rateLimitStatus=null, version=unknown}";
        String actualToString = exception.toString();

        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testgetCause() {
        HttpResponse httpResponse = new HttpResponse();
        StatusLine statusLine = new StatusLine(new ByteArrayInputStream("HTTP/1.1 420 Rate Limit Exceeded\r\n".getBytes()), 420);
        httpResponse.setStatusLine(statusLine);

        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);

        String expectedCause = "420: The number of requests you have made exceeds the quota afforded by your assigned rate limit.";
        String actualCause = exception.getCause();

        Assert.assertEquals(expectedCause, actualCause);
    }
}