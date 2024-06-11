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
import java.util.HashMap;
import java.util.Map;

public class RequestTest {

 private Request request;

 @Before
 public void setup() {
 request = new Request(Verb.GET, "http://example.com");
 request.addQuerystringParameter("param1", "value1");
 request.addHeader("Content-Type", "application/json");
 }

 @Test
 public void testSend_ValidRequest() throws IOException, OAuthException {
 final Response response = request.send();
 assertNotNull(response.getResponseCode());
 }

 @Test
 public void testSend_InvalidURL() {
 try {
 request = new Request(Verb.GET, "http://example.com:");
 request.send();
 fail("Should have thrown MalformedURLException");
 } catch (MalformedURLException e) {
 // Expected
 }
 }

 @Test
 public void testSend_MissingVerb() {
 request = new Request(null, "http://example.com");
 try {
 request.send();
 fail("Should have thrown NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testSend_MissingURL() {
 request = new Request(Verb.GET, null);
 try {
 request.send();
 fail("Should have thrown NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("param1", "value2");
 assertEquals("value2", request.getBodyParams().get("param1"));
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("param2", "value2");
 Map<String, String> params = request.getQueryStringParams();
 assertEquals("value2", params.get("param2"));
 }

 @Test
 public void testAddPayload() {
 request.addPayload("payload");
 assertEquals("payload", request.getPayload());
 }

 @Test
 public void testAddPayload_ByteArray() throws UnsupportedEncodingException {
 final String payload = "payload";
 final byte[] bytes = payload.getBytes("UTF-8");
 request.addPayload(bytes);
 final InputStream inputStream = new ByteArrayInputStream(bytes);
 final byte[] expectedBytes = "expected payload".getBytes("UTF-8");
 request.setBytePayload(expectedBytes);
 final byte[] actualBytes = request.getByteBodyContents();
 assertArrayEquals(expectedBytes, actualBytes);
 }

 @Test
 public void testSetConnectTimeout() throws IOException, InterruptedException {
 request.setConnectTimeout(500, TimeUnit.MILLISECONDS);
 URL url = new URL("http://example.com");
 HttpURLConnection connection = (HttpURLConnection)url.openConnection();
 connection.setDoInput(true);
 connection.setDoOutput(true);
 request.setConnection(connection);
 try {
 Thread.sleep(600);
 request.send();
 fail("Should have thrown ConnectTimeoutException");
 } catch (ConnectException e) {
 // Expected
 }
 }

 @Test
 public void testSetReadTimeout() throws IOException, InterruptedException {
 request.setReadTimeout(500, TimeUnit.MILLISECONDS);
 URL url = new URL("http://example.com");
 HttpURLConnection connection = (HttpURLConnection)url.openConnection();
 connection.setDoInput(true);
 connection.setDoOutput(true);
 request.setConnection(connection);
 final String responseBody = "response body";
 final byte[] responseBytes = responseBody.getBytes("UTF-8");
 request.setBytePayload(responseBytes);
 final InputStream inputStream = new ByteArrayInputStream(responseBytes);
 connection.setInputStream(inputStream);
 connection.disconnect();
 final byte[] actualBytes = request.getByteBodyContents();
 assertArrayEquals(responseBytes, actualBytes);
 }
}