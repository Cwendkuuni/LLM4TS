import org.junit.Assert;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

public class RequestTest {

 private Request request;

 @Test
 public void testRequestConstruction() {
 request = new Request(Verb.GET, "http://example.com");
 Assert.assertNotNull(request);
 Assert.assertEquals(Verb.GET, request.getVerb());
 Assert.assertEquals("http://example.com", request.getUrl());
 Assert.assertEquals(new HashMap<>(), request.getBodyParams());
 Assert.assertEquals(new HashMap<>(), request.getHeaders());
 Assert.assertEquals(null, request.getPayload());
 Assert.assertEquals(null, request.getBytePayload());
 }

 @Test
 public void testAddQuerystringParameter() {
 request = new Request(Verb.GET, "http://example.com");
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 Map<String, String> params = request.getQueryStringParams();
 Assert.assertEquals("param1=value1&param2=value2", params.toString());
 }

 @Test
 public void testAddBodyParameter() {
 request = new Request(Verb.POST, "http://example.com");
 request.addBodyParameter("param1", "value1");
 Map<String, String> bodyParams = request.getBodyParams();
 Assert.assertEquals("param1=value1", bodyParams.toString());
 }

 @Test
 public void testAddHeader() {
 request = new Request(Verb.GET, "http://example.com");
 request.addHeader("Header1", "Value1");
 Map<String, String> headers = request.getHeaders();
 Assert.assertEquals("Header1: Value1", headers.toString());
 }

 @Test
 public void testAddPayload() {
 request = new Request(Verb.POST, "http://example.com");
 request.addPayload("payload");
 String payload = request.getBodyContents();
 Assert.assertEquals("payload", payload);
 }

 @Test
 public void testAddPayloadByteArray() {
 request = new Request(Verb.POST, "http://example.com");
 request.addPayload(new byte[]{'a', 'b', 'c'});
 String payload = request.getBodyContents();
 Assert.assertEquals("abc", payload);
 }

 @Test
 public void testSetCharset() {
 request = new Request(Verb.GET, "http://example.com");
 request.setCharset("UTF-8");
 String charset = request.getCharset();
 Assert.assertEquals("UTF-8", charset);
 }

 @Test
 public void testSendNormalCase() throws IOException, OAuthException {
 request = new Request(Verb.GET, "http://example.com");
 request.addQuerystringParameter("param1", "value1");
 Response response = request.send();
 Assert.assertNotNull(response);
 }

 @Test(expected = OAuthException.class)
 public void testSendIOException() throws IOException, OAuthException {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnectionKeepAlive(true); // Force an IOException
 Response response = request.send();
 }

 @Test(expected = OAuthException.class)
 public void testSendMalformedURL() throws IOException, OAuthException {
 request = new Request(Verb.GET, "http://example.com:");
 request.addQuerystringParameter("param1", "value1");
 try {
 request.send();
 } catch (OAuthException e) {
 Assert.assertEquals("Malformed URL", e.getMessage());
 }
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetByteBodyContentsUnsupportedCharset() throws UnsupportedEncodingException, IOException {
 request = new Request(Verb.POST, "http://example.com");
 request.addPayload("payload");
 request.setCharset("UTF-16BE");
 byte[] bytePayload = request.getByteBodyContents();
 }

}