import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 request.addHeader("Authorization", "Bearer token");
 request.addBodyParameter("key1", "value3");
 request.addPayload("{\"key\":\"value\"}");
 }

 @Test
 public void testSendGETRequestWithQuerystringParameters() throws IOException, InterruptedException {
 final Response response = request.send();
 final Map<String, String> params = request.getQueryStringParams();
 assert response.isSuccess();
 assert response.getStatusCode() == 200;
 assert Arrays.equals(params.toArray(new String[0]), new String[]{"param1=value1", "param2=value2"});
 }

 @Test(expected = OAuthException.class)
 public void testSendGETRequestWithMalformedURL() {
 request = new Request(Verb.GET, "http://example.com:");
 request.send();
 }

 @Test(expected = IOException.class)
 public void testSendGETRequestWithNoConnection() throws OAuthException {
 request.setConnection(null);
 request.send();
 }

 @Test
 public void testSendGETRequestWithHeaders() throws IOException, InterruptedException {
 request = new Request(Verb.GET, "http://example.com");
 request.addHeader("Accept", "application/json");
 request.send();
 final Map<String, String> headers = request.getHeaders();
 assert headers.containsKey("Accept");
 assert headers.get("Accept").equals("application/json");
 }

 @Test
 public void testSendPOSTRequestWithBody() throws IOException, InterruptedException {
 request = new Request(Verb.POST, "http://example.com");
 request.addBodyParameter("key1", "value3");
 request.addPayload("{\"key\":\"value\"}");
 request.send();
 final Map<String, String> bodyParams = request.getBodyParams();
 assert bodyParams.size() == 1;
 assert bodyParams.get("key1").equals("value3");
 }

 @Test(expected = IOException.class)
 public void testSendPOSTRequestWithoutBody() throws IOException, InterruptedException {
 request = new Request(Verb.POST, "http://example.com");
 request.send();
 }

 @Test
 public void testSetConnectTimeout() throws IOException, InterruptedException {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnectTimeout(5, TimeUnit.SECONDS);
 try {
 request.send();
 fail();
 } catch (IOException e) {
 assert e.getMessage().contains("Connect timed out");
 }
 }

 @Test
 public void testSetReadTimeout() throws IOException, InterruptedException {
 request = new Request(Verb.GET, "http://example.com");
 request.setReadTimeout(5, TimeUnit.MILLISECONDS);
 final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 request.setInputStream(inputStream);
 request.send();
 try {
 request.getBodyContents();
 fail();
 } catch (IOException e) {
 assert e.getMessage().contains("Read timed out");
 }
 }

 @Test
 public void testSetCharset() throws IOException, InterruptedException {
 request = new Request(Verb.GET, "http://example.com");
 request.setCharset("UTF-8");
 final String bodyContents = request.getBodyContents();
 assert bodyContents.equals("");
 }

 @Test
 public void testSetConnectionKeepAlive() throws IOException, InterruptedException {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnectionKeepAlive(true);
 request.send();
 request.send();
 final Map<String, String> headers = request.getHeaders();
 assert headers.containsKey("Connection");
 assert headers.get("Connection").equals("keep-alive");
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetByteBodyContentsWithUnsupportedCharset() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.setCharset("UTF-16BE");
 request.addPayload("{\"key\":\"value\"}");
 request.send();
 }

 @Test
 public void testToString() {
 final String expected = "@Request(GET http://example.com)";
 assert request.toString().equals(expected);
 }
}