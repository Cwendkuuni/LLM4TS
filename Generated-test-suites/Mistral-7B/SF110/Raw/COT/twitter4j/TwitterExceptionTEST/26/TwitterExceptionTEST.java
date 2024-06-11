package test;

import static org.junit.Assert.*;

import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

import org.junit.Test;

public class TwitterExceptionTest {

	@Test
	public void testTwitterExceptionWithMessage() {
		TwitterException exception = new TwitterException("Test exception message");
		assertEquals("Test exception message", exception.getMessage());
		assertEquals(-1, exception.getStatusCode());
		assertNull(exception.getRateLimitStatus());
		assertNull(exception.getFeatureSpecificRateLimitStatus());
		assertFalse(exception.isNested());
	}

	@Test
	public void testTwitterExceptionWithCause() {
		Exception cause = new IOException("Test cause message");
		TwitterException exception = new TwitterException("Test exception message", cause);
		assertEquals("Test exception message", exception.getMessage());
		assertEquals(-1, exception.getStatusCode());
		assertNull(exception.getRateLimitStatus());
		assertNull(exception.getFeatureSpecificRateLimitStatus());
		assertTrue(exception.isNested());
		assertEquals(cause.getMessage(), exception.getCause().getMessage());
	}

	@Test
	public void testTwitterExceptionWithMessageAndHttpResponse() {
		HttpResponse response = new HttpResponse(420, "Retry-After: 60");
		TwitterException exception = new TwitterException("Test exception message", response);
		assertEquals("Test exception message", exception.getMessage());
		assertEquals(420, exception.getStatusCode());
		assertEquals(60, exception.getRetryAfter());
		assertNotNull(exception.getRateLimitStatus());
		assertNotNull(exception.getFeatureSpecificRateLimitStatus());
		assertFalse(exception.isNested());
	}

	@Test
	public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
		HttpResponse response = new HttpResponse(420, "Retry-After: 60");
		Exception cause = new IOException("Test cause message");
		TwitterException exception = new TwitterException("Test exception message", cause, response);
		assertEquals("Test exception message", exception.getMessage());
		assertEquals(420, exception.getStatusCode());
		assertEquals(60, exception.getRetryAfter());
		assertNotNull(exception.getRateLimitStatus());
		assertNotNull(exception.getFeatureSpecificRateLimitStatus());
		assertTrue(exception.isNested());
		assertEquals(cause.getMessage(), exception.getCause().getMessage());
	}

	@Test(expected = IllegalStateException.class)
	public void testGetRetryAfterWhenStatusCodeIsNot420() {
		TwitterException exception = new TwitterException("Test exception message");
		exception.setStatusCode(404);
		exception.getRetryAfter();
	}

	@Test
	public void testIsCausedByNetworkIssue() {
		TwitterException exception = new TwitterException("Test exception message");
		assertFalse(exception.isCausedByNetworkIssue());

		TwitterException exceptionWithNetworkIssue = new TwitterException("Test exception message", new IOException("Test cause message"));
		assertTrue(exceptionWithNetworkIssue.isCausedByNetworkIssue());
	}

	@Test
	public void testExceededRateLimitation() {
		HttpResponse response = new HttpResponse(400);
		TwitterException exception = new TwitterException("Test exception message", response);
		assertFalse(exception.exceededRateLimitation());

		TwitterException exceptionWithRateLimitStatus = new TwitterException("Test exception message", new HttpResponse(420, "Retry-After: 60"));
		assertTrue(exceptionWithRateLimitStatus.exceededRateLimitation());

		TwitterException exceptionWithRateLimitStatus400 = new TwitterException("Test exception message", new HttpResponse(400, ""));
		assertTrue(exceptionWithRateLimitStatus400.exceededRateLimitation());
	}

	@Test
	public void testResourceNotFound() {
		HttpResponse response = new HttpResponse(404);
		TwitterException exception = new TwitterException("Test exception message", response);
		assertTrue(exception.resourceNotFound());
	}

	@Test
	public void testGetExceptionCode() {
		TwitterException exception = new TwitterException("Test exception message", new IOException("Test cause message"));
		assertEquals("twitter4j:Test cause message", exception.getExceptionCode());
	}

	@Test
	public void testEquals() {
		TwitterException exception1 = new TwitterException("Test exception message 1", new HttpResponse(420, "Retry-After: 60"));
		TwitterException exception2 = new TwitterException("Test exception message 2", new HttpResponse(420, "Retry-After: 60"));
		TwitterException exception3 = new TwitterException("Test exception message 1", new HttpResponse(404));
		TwitterException exception4 = new TwitterException("Test exception message 1", new HttpResponse(420, "Retry-After: 60"), new IOException("Test cause message"));
		assertTrue(exception1.equals(exception1));
		assertTrue(exception1.equals(exception2));
		assertFalse(exception1.equals(exception3));
		assertFalse(exception1.equals(exception4));
	}

	@Test
	public void testHashCode() {
		TwitterException exception1 = new TwitterException("Test exception message 1", new HttpResponse(420, "Retry-After: 60"));
		TwitterException exception2 = new TwitterException("Test exception message 2", new HttpResponse(420, "Retry-After: 60"));
		TwitterException exception3 = new TwitterException("Test exception message 1", new HttpResponse(404));
		TwitterException exception4 = new TwitterException("Test exception message 1", new HttpResponse(420, "Retry-After: 60"), new IOException("Test cause message"));
		int hashCode1 = exception1.hashCode();
		int hashCode2 = exception2.hashCode();
		int hashCode3 = exception3.hashCode();
		int hashCode4 = exception4.hashCode();
		assertNotEquals(hashCode1, hashCode2);
		assertEquals(hashCode1, exception1.hashCode());
		assertEquals(hashCode2, exception2.hashCode());
		assertNotEquals(hashCode1, hashCode3);
		assertNotEquals(hashCode1, hashCode4);
	}
}