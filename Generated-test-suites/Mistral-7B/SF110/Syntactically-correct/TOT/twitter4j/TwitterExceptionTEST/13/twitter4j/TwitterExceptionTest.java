import org.junit.Assert;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j. RateLimitStatus;

public class TwitterExceptionTest {

 @Test
 public void testTwitterExceptionWithMessage() {
 TwitterException exception = new TwitterException("Test Exception Message");
 Assert.assertEquals("Test Exception Message", exception.getMessage());
 Assert.assertNull(exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertNull(exception.getRetryAfter());
 Assert.assertFalse(exception.isCausedByNetworkIssue());
 Assert.assertFalse(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithCause() {
 TwitterException exception = new TwitterException(new IOException("Test IOException Message"));
 Assert.assertEquals("Test IOException Message", exception.getCause().getMessage());
 Assert.assertNull(exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertNull(exception.getRetryAfter());
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 Assert.assertFalse(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithMessageAndHttpResponse() {
 HttpResponse httpResponse = new HttpResponse() {{
 setStatusCode(400);
 setStatusMessage("Bad Request");
 }};
 TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
 Assert.assertEquals("Test Exception Message", exception.getMessage());
 Assert.assertEquals(400, exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertNull(exception.getRetryAfter());
 Assert.assertFalse(exception.isCausedByNetworkIssue());
 Assert.assertTrue(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testTwitterExceptionWithAllParameters() {
 HttpResponse httpResponse = new HttpResponse() {{
 setStatusCode(420);
 setStatusMessage("Rate Limit Exceeded");
 setResponseHeader("Retry-After", "120");
 }};
 TwitterException exception = new TwitterException("Test Exception Message", new IOException("Test IOException Message"), 400, httpResponse);
 Assert.assertEquals("Test Exception Message: Test IOException Message", exception.getMessage());
 Assert.assertEquals(400, exception.getStatusCode());
 Assert.assertNull(exception.getRateLimitStatus());
 Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
 Assert.assertEquals(120, exception.getRetryAfter());
 Assert.assertTrue(exception.isCausedByNetworkIssue());
 Assert.assertTrue(exception.exceededRateLimitation());
 Assert.assertFalse(exception.resourceNotFound());
 }

 @Test
 public void testEquals() {
 TwitterException exception1 = new TwitterException("Test Exception Message 1");
 TwitterException exception2 = new TwitterException("Test Exception Message 2");
 Assert.assertFalse(exception1.equals(exception2));
 TwitterException exception3 = new TwitterException("Test Exception Message 1");
 Assert.assertTrue(exception1.equals(exception3));
 TwitterException exception4 = new TwitterException("Test Exception Message 1") {{
 setRetryAfter(5);
 }};
 Assert.assertFalse(exception1.equals(exception4));
 }

 @Test
 public void testHashCode() {
 TwitterException exception1 = new TwitterException("Test Exception Message 1");
 TwitterException exception2 = new TwitterException("Test Exception Message 2");
 Assert.assertNotEquals(exception1.hashCode(), exception2.hashCode());
 TwitterException exception3 = new TwitterException("Test Exception Message 1");
 Assert.assertEquals(exception1.hashCode(), exception3.hashCode());
 TwitterException exception4 = new TwitterException("Test Exception Message 1") {{
 setRetryAfter(5);
 }};
 Assert.assertNotEquals(exception1.hashCode(), exception4.hashCode());
 }

 @Test
 public void testToString() {
 TwitterException exception = new TwitterException("Test Exception Message");
 String exceptionString = "TwitterException{statusCode=420, retryAfter=-1, rateLimitStatus=null, version=0.0.5}";
 if (exception.getStatusCode() == 420) {
 exceptionString = "TwitterException{exceptionCode=[...], statusCode=420, retryAfter=120, rateLimitStatus=RateLimitStatus{limit=15, remaining=12, reset=1600309681000}, version=0.0.5}";
 }
 Assert.assertEquals(exceptionString, exception.toString());
 }

 @Test
 public void testRateLimitStatus() {
 RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 12, 1600309681000);
 TwitterException exception = new TwitterException("Test Exception Message", new IOException("Test IOException Message"), 420, null, rateLimitStatus, null);
 Assert.assertEquals(rateLimitStatus, exception.getRateLimitStatus());
 }

 @Test
 public void testFeatureSpecificRateLimitStatus() {
 RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus("search", 5, 12, 1600309681000);
 TwitterException exception = new TwitterException("Test Exception Message", new IOException("Test IOException Message"), 420, null, null, featureSpecificRateLimitStatus);
 Assert.assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
 }
}