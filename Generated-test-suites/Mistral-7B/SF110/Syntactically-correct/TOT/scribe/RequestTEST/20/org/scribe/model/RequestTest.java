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
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 request.addHeader("header1", "value1");
 request.addHeader("header2", "value2");
 request.setCharset("UTF-8");
 }

 @Test
 public void testCreateConnection() throws IOException {
 request.createConnection();
 assertNotNull(request.connection);
 }

 @Test
 public void testSendGetRequest() throws IOException {
 Response response = request.send();
 assertNotNull(response);
 assertEquals(200, response.statusCode);
 }

 @Test
 public void testSendPostRequestWithBody() throws IOException {
 request.addBodyParameter("param", "value");
 request.addHeader("Content-Type", "application/json");
 request.addHeader("Accept", "application/json");
 request.setPayload("{\"name\":\"John\", \"age\":30}");
 request.setConnectionKeepAlive(true);
 Response response = request.send();
 assertNotNull(response);
 assertEquals(200, response.statusCode);
 }

 @Test(expected = OAuthException.class)
 public void testSendRequestWithInvalidUrl() {
 request = new Request(Verb.GET, "http://example.com:");
 request.createConnection(); // expected exception
 }

 @Test(expected = OAuthException.class)
 public void testSendRequestWithInvalidCharset() {
 request = new Request(Verb.GET, "http://example.com");
 request.setCharset("invalidCharset");
 request.createConnection(); // expected exception
 }

 @Test(expected = IOException.class)
 public void testSendRequestWithEmptyBody() throws IOException {
 request = new Request(Verb.POST, "http://example.com");
 request.addBodyParameter("param", "");
 request.setBytePayload(new byte[]{});
 request.send(); // expected exception
 }
}