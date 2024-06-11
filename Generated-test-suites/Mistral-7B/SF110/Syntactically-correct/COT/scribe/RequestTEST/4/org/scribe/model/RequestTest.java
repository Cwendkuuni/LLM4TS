import org.junit.Assert;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

public class RequestTest {

 private Request request;

 @Test
 public void testRequestConstructor() {
 request = new Request(Verb.GET, "http://example.com");
 Assert.assertNotNull(request);
 Assert.assertEquals(Verb.GET, request.getVerb());
 Assert.assertEquals("http://example.com", request.getUrl());
 Assert.assertEquals(0, request.getBytePayload().length);
 Assert.assertEquals(null, request.getPayload());
 Assert.assertEquals(0, request.getConnection().getInputStream().available());
 }

 @Test
 public void testAddQuerystringParameter() {
 request = new Request(Verb.GET, "http://example.com");
 request.addQuerystringParameter("param1", "value1");
 Map<String, String> params = request.getQueryStringParams();
 Assert.assertEquals("param1=value1", params.get("param1"));
 }

 @Test
 public void testAddBodyParameter() {
 request = new Request(Verb.POST, "http://example.com");
 request.addBodyParameter("param1", "value1");
 Map<String, String> bodyParams = request.getBodyParams();
 Assert.assertEquals("param1=value1", bodyParams.get("param1"));
 }

 @Test
 public void testAddPayload() {
 request = new Request(Verb.POST, "http://example.com");
 request.addPayload("payload1");
 Assert.assertEquals("payload1", request.getPayload());
 }

 @Test
 public void testAddPayloadBytes() {
 request = new Request(Verb.POST, "http://example.com");
 request.addPayload(new byte[]{"p", "a", "y", "l", "o", "a", "d", "1"});
 Assert.assertNotNull(request.getBytePayload());
 Assert.assertEquals(8, request.getBytePayload().length);
 }

 @Test
 public void testCreateConnection() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.createConnection();
 Assert.assertNotNull(request.getConnection());
 }

 @Test(expected = OAuthException.class)
 public void testCreateConnectionWithMalformedUrl() throws IOException {
 request = new Request(Verb.GET, "http://example.com:");
 request.createConnection();
 }

 @Test
 public void testDoSend() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.addQuerystringParameter("param1", "value1");
 Response response = request.send();
 Assert.assertNotNull(response);
 }

 @Test(expected = OAuthException.class)
 public void testSendWithIOException() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 throw new IOException("Test IOException");
 }

 @Test
 public void testSetConnectTimeout() {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnectTimeout(5, TimeUnit.SECONDS);
 Assert.assertEquals(5000, request.getConnection().getConnectTimeout());
 }

 @Test
 public void testSetReadTimeout() {
 request = new Request(Verb.GET, "http://example.com");
 request.setReadTimeout(5, TimeUnit.SECONDS);
 Assert.assertEquals(5000, request.getConnection().getReadTimeout());
 }

 @Test
 public void testSetCharset() {
 request = new Request(Verb.GET, "http://example.com");
 request.setCharset("UTF-8");
 Assert.assertEquals("UTF-8", request.getCharset());
 }

 @Test
 public void testSetConnectionKeepAlive() {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnectionKeepAlive(true);
 Assert.assertTrue(request.isConnectionKeepAlive());
 }

 @Test
 public void testGetBodyContentsWithURLUtilsFormURLEncodeMap() throws UnsupportedEncodingException {
 request = new Request(Verb.POST, "http://example.com");request.addBodyParameter("param1", "value1"); Map<String, String> bodyParams = request.getBodyParams(); String body = URLUtils.formURLEncodeMap(bodyParams); Assert.assertEquals("param1=value1", body); }

 @Test
 public void testGetBodyContentsWithPayload() { request = new Request(Verb.POST, "http://example.com"); request.addPayload("payload1"); String body = request.getBodyContents(); Assert.assertEquals("payload1", body); }

 @Test(expected = OAuthException.class)
 public void testGetBodyContentsWithUnsupportedCharset() throws UnsupportedEncodingException { request = new Request(Verb.POST, "http://example.com"); request.setCharset("X-UNSUPPORTED-CHARSET"); }

 @Test
 public void testAddHeaders() { request = new Request(Verb.GET, "http://example.com"); request.addHeader("Header1", "Value1"); Map<String, String> headers = request.getHeaders(); Assert.assertEquals("Value1", headers.get("Header1")); }

}