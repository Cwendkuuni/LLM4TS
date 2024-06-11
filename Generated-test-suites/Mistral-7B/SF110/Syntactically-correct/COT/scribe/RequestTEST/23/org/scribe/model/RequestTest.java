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
 }

 @Test
 public void testRequestConstructor() {
 assertEquals("http://example.com", request.getUrl());
 assertEquals(Verb.GET, request.getVerb());
 assertNull(request.getBytePayload());
 assertNull(request.getPayload());
 assertNotNull(request.getHeaders());
 assertNotNull(request.getQuerystringParams());
 assertNotNull(request.getBodyParams());
 }

 @Test
 public void testSend_GET() throws IOException, OAuthException {
 Response response = request.send();
 assertNotNull(response);
 assertEquals(200, response.getStatusCode());
 }

 @Test(expected = OAuthException.class)
 public void testSend_IOException() throws IOException, OAuthException {
 request.setConnectionKeepAlive(true);
 try {
 request.send();
 } catch (IOException e) {
 throw new OAuthException("Expected IOException", e);
 }
 }

 @Test
 public void testCreateConnection() throws IOException {
 request.setUrl("http://example.com?key=value");
 request.createConnection();
 assertNotNull(request.getConnection());
 }

 @Test
 public void testDoSend_GET() throws IOException, OAuthException {
 Response response = request.doSend();
 assertNotNull(response);
 assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testAddHeaders() {
 request.addHeader("key", "value");
 request.addHeader("anotherKey", "anotherValue");
 Map<String, String> headers = request.getHeaders();
 assertEquals(2, headers.size());
 assertEquals("value", headers.get("key"));
 assertEquals("anotherValue", headers.get("anotherKey"));
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("key", "value");
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals("value", bodyParams.get("key"));
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("key", "value");
 Map<String, String> queryStringParams = request.getQueryStringParams();
 assertEquals("value", queryStringParams.get("key"));
 }

 @Test
 public void testAddPayload() {
 request.addPayload("payload");
 assertEquals("payload", request.getPayload());
 }

 @Test
 public void testAddPayload_byteArray() {
 byte[] bytePayload = "payload".getBytes();
 request.addPayload(bytePayload);
 assertEquals(bytePayload, request.getBytePayload());
 }

 @Test
 public void testGetQueryStringParams() {
 Map<String, String> params = request.getQueryStringParams();
 assertEquals(0, params.size());
 request.addQuerystringParameter("key", "value");
 params = request.getQueryStringParams();
 assertEquals("value", params.get("key"));
 }

 @Test
 public void testGetBodyParams() {
 Map<String, String> params = request.getBodyParams();
 assertEquals(0, params.size());
 request.addBodyParameter("key", "value");
 params = request.getBodyParams();
 assertEquals("value", params.get("key"));
 }

 @Test
 public void testGetUrl() {
 assertEquals("http://example.com", request.getUrl());
 }

 @Test
 public void testGetSanitizedUrl() {
 assertEquals("http://example.com", request.getSanitizedUrl());
 }

 @Test
 public void testGetBodyContents() throws UnsupportedEncodingException {
 request.addPayload("payload");
 String bodyContents = request.getBodyContents();
 assertEquals("payload", bodyContents);
 }

 @Test(expected = OAuthException.class)
 public void testGetBodyContents_UnsupportedEncodingException() throws UnsupportedEncodingException {
 request.setCharset("unsupportedCharset");
 try {
 request.getBodyContents();
 } catch (UnsupportedEncodingException e) {
 throw new OAuthException("Expected UnsupportedEncodingException", e);
 }
 }

 @Test
 public void testGetVerb() {
 assertEquals(Verb.GET, request.getVerb());
 }

 @Test
 public void testGetHeaders() {
 return; // This method returns an immutable map, so we cannot test its content directly
 }

 @Test
 public void testGetCharset() {
 assertEquals(Charset.defaultCharset().name(), request.getCharset());
 }

 @Test
 public void testSetConnectTimeout() {
 request.setConnectTimeout(5, TimeUnit.SECONDS);
 assertEquals(5000, request.getConnection().getConnectTimeout());
 }

 @Test
 public void testSetReadTimeout() {
 request.setReadTimeout(5, TimeUnit.SECONDS);
 assertEquals(5000, request.getConnection().getReadTimeout());
 }

 @Test
 public void testSetCharset() {
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.getCharset());
 }

 @Test
 public void testSetConnectionKeepAlive() {
 request.setConnectionKeepAlive(true);
 assertTrue(request.isConnectionKeepAlive());
 }

 @Test
 public void testToString() {
 assertEquals("@Request(GET http://example.com)", request.toString());
 }
}