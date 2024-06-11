import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.*;
import org.scribe.exceptions.*;

public class RequestTest {

 private Request request;

 @Test
 public void testRequestWithValidArguments() {
 Request validRequest = new Request(Verb.GET, "http://www.example.com");
 Assert.assertNotNull(validRequest);
 }

 @Test
 public void testSendMethod() throws Exception {
 request = new Request(Verb.GET, "http://www.example.com");
 Response response = request.send();
 Assert.assertNotNull(response);
 }

 @Test
 public void testCreateConnectionMethod() throws Exception {
 request = new Request(Verb.GET, "http://www.example.com");
 request.createConnection();
 }

 @Test
 public void testDoSendMethod() throws Exception {
 request = new Request(Verb.GET, "http://www.example.com");
 Response response = request.send();
 response = request.doSend();
 Assert.assertNotNull(response);
 }

 @Test
 public void testAddHeadersMethod() throws Exception {
 request = new Request(Verb.GET, "http://www.example.com");
 request.addHeader("Key", "Value");
 }

 @Test
 public void testAddBodyParameterMethod() {
 request = new Request(Verb.GET, "http://www.example.com");
 request.addBodyParameter("Key", "Value");
 }

 @Test
 public void testAddQuerystringParameterMethod() {
 request = new Request(Verb.GET, "http://www.example.com");
 request.addQuerystringParameter("Key", "Value");
 }

 @Test
 public void testAddPayloadMethodWithString() {
 request = new Request(Verb.GET, "http://www.example.com");
 request.addPayload("Payload");
 }

 @Test
 public void testAddPayloadMethodWithByteArray() {
 request = new Request(Verb.GET, "http://www.example.com");
 request.addPayload(new byte[10]);
 }

 @Test
 public void testGetQueryStringParamsMethod() {
 request = new Request(Verb.GET, "http://www.example.com?key=value");
 Map<String, String> params = request.getQueryStringParams();
 Assert.assertEquals("value", params.get("key"));
 }

 @Test
 public void testGetBodyParamsMethod() {
 request = new Request(Verb.GET, "http://www.example.com");
 request.addBodyParameter("Key", "Value");
 Map<String, String> params = request.getBodyParams();
 Assert.assertEquals("Value", params.get("Key"));
 }

 @Test
 public void testGetUrlMethod() {
 request = new Request(Verb.GET, "http://www.example.com");
 String url = request.getUrl();
 Assert.assertEquals("http://www.example.com", url);
 }

 @Test
 public void testGetSanitizedUrlMethod() {
 request = new Request(Verb.GET, "http://www.example.com?key=value:1234");
 String sanitizedUrl = request.getSanitizedUrl();
 Assert.assertEquals("http://www.example.com", sanitizedUrl);
 }

 @Test
 public void testGetBodyContentsMethod() throws Exception {
 request = new Request(Verb.GET, "http://www.example.com");
 request.addPayload("Payload");
 String bodyContents = request.getBodyContents();
 Assert.assertEquals("Payload", bodyContents);
 }

 @Test
 public void testGetVerbMethod() {
 request = new Request(Verb.GET, "http://www.example.com");
 Verb verb = request.getVerb();
 Assert.assertEquals(Verb.GET, verb);
 }

 @Test
 public void testGetHeadersMethod() {
 request = new Request(Verb.GET, "http://www.example.com");
 request.addHeader("Key", "Value");
 Map<String, String> headers = request.getHeaders();
 Assert.assertEquals("Value", headers.get("Key"));
 }

 @Test
 public void testGetCharsetMethod() {
 request = new Request(Verb.GET, "http://www.example.com");
 request.setCharset("UTF-8");
 String charset = request.getCharset();
 Assert.assertEquals("UTF-8", charset);
 }

 @Test
 public void testSetConnectTimeoutMethod() {
 request = new Request(Verb.GET, "http://www.example.com");
 request.setConnectTimeout(5, TimeUnit.SECONDS);
 }

 @Test
 public void testSetReadTimeoutMethod() {
 request = new Request(Verb.GET, "http://www.example.com");request.setReadTimeout(5, TimeUnit.SECONDS); }

 @Test
 public void testSetCharsetMethod() {
 request = new Request(Verb.GET, "http://www.example.com");
 request.setCharset("UTF-8");
 String charset = request.getCharset();
 Assert.assertEquals("UTF-8", charset);
 }

 @Test
 public void testSetConnectionKeepAliveMethod() {
 request = new Request(Verb.GET, "http://www.example.com");
 request.setConnectionKeepAlive(true);
 }

 @Test
 public void testSetConnectionMethod() {
 request = new Request(Verb.GET, "http://www.example.com");
 request.setConnection(mock(HttpURLConnection.class));
 }

 @Test(expected = OAuthException.class)
 public void testSendWithIOException() throws Exception {
 request = new Request(Verb.GET, "http://www.example.com/fail");
 throw new IOException();
 }

 @Test(expected = MalformedURLException.class)
 public void testGetQueryStringParamsWithMalformedURL() {
 request = new Request(Verb.GET, "http://www.example.com%22");
 request.getQueryStringParams();
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetBodyContentsWithUnsupportedCharset() throws Exception {
 request = new Request(Verb.GET, "http://www.example.com");
 request.setCharset("InvalidCharset");
 request.addPayload("Payload");
 }
}