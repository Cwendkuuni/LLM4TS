import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;
import static org.junit.Assert.*;

public class RequestTest {

 private Request request;

 @Before
 public void setup() {
 request = new Request(Verb.GET, "http://example.com/api?param1=value1&param2=value2");
 request.addQuerystringParameter("param1", "newValue1");
 request.addQuerystringParameter("param2", "newValue2");
 request.addHeader("Authorization", "Bearer token123");
 request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
 request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testSendGetRequest() throws OAuthException {
 Request expectedRequest = new Request(Verb.GET, "http://example.com/api?param1=newValue1&param2=newValue2");
 expectedRequest.addHeader("Authorization", "Bearer token123");
 Response response = request.send();
 Request actualRequest = new Request(response);
 assertEquals(expectedRequest, actualRequest);
 }

 @Test(expected = OAuthException.class)
 public void testSendGetRequestWithMalformedUrl() {
 request.setUrl("http://example.com:/api?param1=value1&param2=value2");
 request.send();
 // exception should be thrown
 }

 @Test(expected = OAuthException.class)
 public void testSendPostRequestWithNullPayload() {
 request = new Request(Verb.POST, "http://example.com/api");
 request.send();
 // exception should be thrown
 }

 @Test
 public void testSendPostRequestWithStringPayload() throws OAuthException {
 request = new Request(Verb.POST, "http://example.com/api");
 request.addPayload("payload=test");
 Response response = request.send();
 String actualPayload = response.getResponseBody();
 assertEquals("payload=test", actualPayload);
 }

 @Test
 public void testSendPostRequestWithBytePayload() throws OAuthException {
 request = new Request(Verb.POST, "http://example.com/api");
 request.addPayload("payload=test".getBytes());
 Response response = request.send();
 String actualPayload = new String(response.getResponseBody(), Charset.forName("UTF-8"));
 assertEquals("payload=test", actualPayload);
 }
}