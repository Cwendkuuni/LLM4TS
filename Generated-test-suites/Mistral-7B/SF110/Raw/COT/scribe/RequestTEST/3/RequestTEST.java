import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.exceptions.OAuthException;
import java.util.HashMap;
import java.util.Map;

public class RequestTest {

 private Request request;

 @Test
 public void testRequestConstructor() {
 request = new Request(Verb.GET, "http://example.com");
 Assert.assertEquals(Verb.GET, request.getVerb());
 Assert.assertEquals("http://example.com", request.getUrl());
 Assert.assertNull(request.getBytePayload());
 Assert.assertNull(request.getPayload());
 Assert.assertEquals(0, request.getConnection().getInputStream().available());
 }

 @Test
 public void testAddHeaders() {
 request = new Request(Verb.GET, "http://example.com");
 Map<String, String> headers = new HashMap<>();
 headers.put("Test-Header", "Test-Value");
 request.addHeaders(headers);
 Assert.assertEquals("Test-Value", request.getHeaders().get("Test-Header"));
 }

 @Test
 public void testAddBodyParameter() {
 request = new Request(Verb.GET, "http://example.com");
 request.addBodyParameter("key", "value");
 Assert.assertEquals("value", request.getBodyParams().get("key"));
 }

 @Test
 public void testAddQuerystringParameter() {
 request = new Request(Verb.GET, "http://example.com");
 request.addQuerystringParameter("key", "value");
 Map<String, String> params = request.getQueryStringParams();
 Assert.assertEquals("value", params.get("key"));
 }

 @Test
 public void testAddPayload() {
 request = new Request(Verb.GET, "http://example.com");
 request.addPayload("payload");
 Assert.assertEquals("payload", request.getPayload());
 }

 @Test
 public void testAddPayloadByteArray() {
 request = new Request(Verb.GET, "http://example.com");
 byte[] payload = "payload".getBytes();
 request.addPayload(payload);
 Assert.assertArrayEquals(payload, request.getBytePayload());
 }

 @Test(expected = OAuthException.class)
 public void testMalformedUrl() {
 request = new Request(Verb.GET, "http://example.com:");
 }

 @Test
 public void testGetUrl() {
 request = new Request(Verb.GET, "http://example.com?key=value");
 Assert.assertEquals("http://example.com", request.getUrl());
 }

 @Test
 public void testGetSanitizedUrl() {
 request = new Request(Verb.GET, "http://example.com?key=value");
 Assert.assertEquals("example.com", request.getSanitizedUrl());
 }

 @Test
 public void testGetBodyContents() {
 request = new Request(Verb.GET, "http://example.com");
 request.addBodyParameter("key", "value");
 Assert.assertEquals("value", request.getBodyContents());
 }

 @Test
 public void testGetVerb() {
 request = new Request(Verb.GET, "http://example.com");
 Assert.assertEquals(Verb.GET, request.getVerb());
 }

 @Test
 public void testGetHeaders() {
 request = new Request(Verb.GET, "http://example.com");
 Map<String, String> headers = new HashMap<>();
 headers.put("Test-Header", "Test-Value");
 request.addHeaders(headers);
 Assert.assertEquals(headers, request.getHeaders());
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testUnsupportedCharset() {
 request = new Request(Verb.GET, "http://example.com");
 request.setCharset("Unsupported-Charset");
 }

 @Test
 public void testSetConnectTimeout() {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
 Assert.assertEquals(5000, request.getConnection().getConnectTimeout());
 }

 @Test
 public void testSetReadTimeout() {
 request = new Request(Verb.GET, "http://example.com");
 request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
 Assert.assertEquals(5000, request.getConnection().getReadTimeout());
 }

 @Test
 public void testSetConnectionKeepAlive() {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnectionKeepAlive(true);
 Assert.assertTrue(request.isConnectionKeepAlive());
 }

 @Test
 public void testCreateConnection() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.createConnection();
 Assert.assertNotNull(request.getConnection());
 }

 @Test(expected = IOException.class)
 public void testCreateConnectionFailure() throws IOException {
 request = new Request(Verb.GET, "");
 request.createConnection();
 }

 @Test
 public void testDoSend() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 Map<String, String> headers = new HashMap<>();
 headers.put("Test-Header", "Test-Value");
 request.addHeaders(headers);
 request.addBodyParameter("key", "value");
 Response response = request.send();
 Assert.assertNotNull(response);
 }

 @Test(expected = OAuthException.class)
 public void testSendFailure() throws IOException {
 request = new Request(Verb.GET, "");
 request.addHeaders(Collections.emptyMap());
 try {
 request.send();
 } catch (IOException e) {
 throw new OAuthException("Expected IOException", e);
 }
 }

 @Test
 public void testSendWithBody() throws IOException {
 request = new Request(Verb.POST, "http://example.com");
 Map<String, String> headers = new HashMap<>();
 headers.put("Test-Header", "Test-Value");
 request.addHeaders(headers);
 request.addBodyParameter("key", "value");
 request.addPayload("payload");
 Response response = request.send();
 Assert.assertNotNull(response);
 }

 @Test
 public void testSendWithByteBody() throws IOException {
 request = new Request(Verb.POST, "http://example.com");
 Map<String, String> headers = new HashMap<>();
 headers.put("Test-Header", "Test-Value");
 request.addHeaders(headers);
 byte[] payload = "payload".getBytes();
 request.addPayload(payload);
 Response response = request.send();
 Assert.assertNotNull(response);
 }

 @Test
 public void testSendWithEmptyBody() throws IOException {
 request = new Request(Verb.POST, "http://example.com");
 Map<String, String> headers = new HashMap<>();
 headers.put("Test-Header", "Test-Value");
 request.addHeaders(headers);
 Response response = request.send();
 Assert.assertNotNull(response);
 }

}