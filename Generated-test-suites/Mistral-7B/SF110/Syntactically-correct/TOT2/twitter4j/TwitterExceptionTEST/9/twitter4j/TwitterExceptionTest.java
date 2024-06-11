package twitter4j.test;

import org.junit.*;
import static org.junit.Assert.*;
import twitter4j.*;
import static twitter4j.internal.http.DefaultHttpResponse.*;

public class TwitterExceptionTest {
  @Test
  public void testGetStatusCode() {
    TwitterException twitterException = new TwitterException("Test Exception");
    assertEquals(-1, twitterException.getStatusCode());

    HttpResponse response = new DefaultHttpResponse(400);
    TwitterException exceptionFromResponse = new TwitterException("Test Exception", response);
    assertEquals(400, exceptionFromResponse.getStatusCode());
  }

  @Test
  public void testGetStatusCodeWithNoArguments() {
    TwitterException twitterException = new TwitterException("Test Exception");
    assertEquals(-1, twitterException.getStatusCode());
  }

  @Test
  public void testGetStatusCodeFromHttpResponse() {
    HttpResponse response = new DefaultHttpResponse(400);
    TwitterException exceptionFromResponse = new TwitterException("Test Exception", response);
    assertEquals(400, exceptionFromResponse.getStatusCode());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetRetryAfterWithout420StatusCode() {
    TwitterException twitterException = new TwitterException("Test Exception");
    twitterException.getRetryAfter();
  }

  @Test
  public void testIsCausedByNetworkIssue() {
    IOException ioException = new IOException("Network error");
    TwitterException exceptionWithIoException = new TwitterException("Test Exception", ioException);
    assertTrue(exceptionWithIoException.isCausedByNetworkIssue());
  }

  @Test
  public void testExceededRateLimitation() {
    HttpResponse response = new DefaultHttpResponse(400);
    TwitterException exceptionFromResponse = new TwitterException("Test Exception", response);
    assertTrue(exceptionFromResponse.exceededRateLimitation());
  }

  @Test
  public void testResourceNotFound() {
    HttpResponse response = new DefaultHttpResponse(404);
    TwitterException exceptionFromResponse = new TwitterException("Test Exception", response);
    assertTrue(exceptionFromResponse.resourceNotFound());
  }
}