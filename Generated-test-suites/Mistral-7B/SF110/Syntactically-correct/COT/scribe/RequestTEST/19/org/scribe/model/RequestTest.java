import org.junit.Test;
import org.junit.Assert;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

public class RequestTest {

 private Request request;

 @Test
 public void testRequestWithValidArguments() {
 request = new Request(Verb.GET, "http://example.com");
 Assert.assertNotNull(request);
 }

 @Test
 public void testSendMethod() throws Exception {
 request = new Request(Verb.GET, "http://example.com");
 Response response = request.send();
 Assert.assertNotNull(response);
 }

 @Test
 public void testCreateConnectionMethod() throws Exception {
 request = new Request(Verb.GET, "http://example.com");
 request.createConnection();
 }

 @Test
 public void testDoSendMethod() throws Exception {
 request = new Request(Verb.GET, "http://example.com");
 Response response = request.doSend();
 Assert.assertNotNull(response);
 }

 @Test
 public void testAddHeadersMethod() throws Exception {
 request = new Request(Verb.GET, "http://example.com");
 request.addHeader("key", "value");
 }

 @Test
 public void testAddBodyParameterMethod() {
 request = new Request(Verb.GET, "http://example.com");
 request.addBodyParameter("key", "value");
 }

 @Test
 public void testAddQuerystringParameterMethod() {
 request = new Request(Verb.GET, "http://example.com?key=value");
 request.addQuerystringParameter("key", "value2");
 Map<String, String> params = request.getQueryStringParams();
 Assert.assertEquals("value2", params.get("key"));
 }

 @Test
 public void testAddPayloadMethodWithString() {
 request = new Request(Verb.GET, "http://example.com");
 request.addPayload("payload");
 }

 @Test
 public void testAddPayloadMethodWithByteArray() throws Exception {
 request = new Request(Verb.GET, "http://example.com");
 request.addPayload(new byte[10]);
 }

 @Test(expected = OAuthException.class)
 public void testSendMethodWithIOException() throws Exception {
 request = new Request(Verb.GET, "http://example.com");
 doReturn(null).when(request).createConnection();
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testSendMethodWithMalformedURLException() throws Exception {
 request = new Request(Verb.GET, "http://example.com:");
 doThrow(new MalformedURLException("test error")).when(request).createConnection();
 request.send();
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetByteBodyContentsWithUnsupportedCharset() throws Exception {
 request = new Request(Verb.GET, "http://example.com");
 request.setCharset("unsupported-charset");
 request.addPayload("payload");
 }

 @Test
 public void testGetQueryStringParamsMethod() {
 request = new Request(Verb.GET, "http://example.com?key=value");
 Map<String, String> params = request.getQueryStringParams();
 Assert.assertEquals("value", params.get("key"));
 }

 @Test
 public void testGetBodyParamsMethod() {
 request = new Request(Verb.GET, "http://example.com")
 .addBodyParameter("key", "value");
 Map<String, String> params = request.getBodyParams();
 Assert.assertEquals("value", params.get("key"));
 }

 @Test
 public void testGetUrlMethod() {
 request = new Request(Verb.GET, "http://example.com");
 Assert.assertEquals("http://example.com", request.getUrl());
 }

 @Test
 public void testGetSanitizedUrlMethod() {
 request = new Request(Verb.GET, "http://example.com?key=value");
 Assert.assertEquals("http://example.com", request.getSanitizedUrl());
 }

 @Test
 public void testGetBodyContentsMethod() throws Exception {
 request = new Request(Verb.GET, "http://example.com")
 .addPayload("payload");
 String body = request.getBodyContents();
 Assert.assertEquals("payload", body);
 }

 @Test
 public void testGetVerbMethod() {
 request = new Request(Verb.GET, "http://example.com");
 Assert.assertEquals(Verb.GET, request.getVerb());
 }

 @Test
 public void testGetHeadersMethod() {
 request = new Request(Verb.GET, "http://example.com")
 .addHeader("key", "value");
 Map<String, String> headers = request.getHeaders();
 Assert.assertEquals("value", headers.get("key"));
 }

 @Test
 public void testGetCharsetMethod() {
 request = new Request(Verb.GET, "http://example.com");
 Assert.assertEquals(Charset.defaultCharset().name(), request.getCharset());
 }

 @Test
 public void testSetConnectTimeoutMethod() {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnectTimeout(10, TimeUnit.SECONDS);
 }

 @Test
 public void testSetReadTimeoutMethod() {
 request = new Request(Verb.GET, "http://example.com");
 request.setReadTimeout(10, TimeUnit.SECONDS);
 }

 @Test
 public void testSetCharsetMethod() {
 request = new Request(Verb.GET, "http://example.com");
 request.setCharset("UTF-8");
 }

 @Test
 public void testSetConnectionKeepAliveMethod() {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnectionKeepAlive(true);
 }

 @Test
 public void testSetConnectionMethod() {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnection((HttpURLConnection)mock(HttpURLConnection.class));
 }
}