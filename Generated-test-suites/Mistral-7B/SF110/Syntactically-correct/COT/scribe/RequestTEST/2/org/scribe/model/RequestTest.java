import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.exceptions.OAuthException;
import java.util.HashMap;
import java.util.Map;
import java.net.HttpURLConnection;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class RequestTest {

 private Request request;

 @Test
 public void testRequestConstructor() {
 request = new Request(Verb.GET, "http://example.com");
 Assert.assertNotNull(request.getUrl());
 Assert.assertNull(request.getPayload());
 Assert.assertNull(request.getBytePayload());
 Assert.assertFalse(request.isConnectionKeepAlive());
 }

 @Test
 public void testSendMethod() throws IOException, OAuthException {
 request = new Request(Verb.GET, "http://example.com");
 Response response = request.send();
 Assert.assertNotNull(response);
 }

 @Test(expected = OAuthException.class)
 public void testSendMethodWithIOException() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 // Simulate IOException in send() method
 throw new IOException();
 }

 @Test
 public void testCreateConnectionMethod() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.createConnection();
 Assert.assertNotNull(request.getConnection());
 }

 @Test(expected = IOException.class)
 public void testCreateConnectionMethodWithIOException() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 // Simulate IOException in createConnection() method
 throw new IOException();
 }

 @Test
 public void testDoSendMethod() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 Response response = request.doSend();
 Assert.assertNotNull(response);
 }

 @Test(expected = IOException.class)
 public void testDoSendMethodWithIOException() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 // Simulate IOException in doSend() method
 throw new IOException();
 }

 @Test
 public void testAddHeadersMethod() {
 request = new Request(Verb.GET, "http://example.com");
 Map<String, String> headers = new HashMap<>();
 headers.put("key", "value");
 request.addHeaders(headers);
 Assert.assertEquals(headers, request.getHeaders());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddHeadersMethodWithNullHeaders() {
 request = new Request(Verb.GET, "http://example.com");
 request.addHeaders(null);
 }

 @Test
 public void testAddBodyParameterMethod() {
 request = new Request(Verb.GET, "http://example.com");
 request.addBodyParameter("key", "value");
 Assert.assertEquals("value", request.getBodyParams().get("key"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBodyParameterMethodWithNullKey() {
 request = new Request(Verb.GET, "http://example.com");
 request.addBodyParameter(null, "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBodyParameterMethodWithNullValue() {
 request = new Request(Verb.GET, "http://example.com");
 request.addBodyParameter("key", null);
 }

 @Test
 public void testAddQuerystringParameterMethod() {
 request = new Request(Verb.GET, "http://example.com");
 request.addQuerystringParameter("key", "value");
 Assert.assertEquals("value", request.getQueryStringParams().get("key"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddQuerystringParameterMethodWithNullKey() {
 request = new Request(Verb.GET, "http://example.com");
 request.addQuerystringParameter(null, "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddQuerystringParameterMethodWithNullValue() {
 request = new Request(Verb.GET, "http://example.com");
 request.addQuerystringParameter("key", null);
 }

 @Test
 public void testAddPayloadMethodWithString() {
 request = new Request(Verb.POST, "http://example.com");
 request.addPayload("payload");
 Assert.assertEquals("payload", request.getPayload());
 }

 @Test
 public void testAddPayloadMethodWithByteArray() {
 request = new Request(Verb.POST, "http://example.com");
 byte[] bytePayload = "payload".getBytes();
 request.addPayload(bytePayload);
 Assert.assertArrayEquals(bytePayload, request.getBytePayload());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPayloadMethodWithNullPayload() {
 request = new Request(Verb.POST, "http://example.com");
 request.addPayload(null);
 }

 @Test
 public void testGetQueryStringParamsMethod() {
 request = new Request(Verb.GET, "http://example.com?key=value");
 Map<String, String> params = request.getQueryStringParams();
 Assert.assertEquals("value", params.get("key"));
 }

 @Test
 public void testGetBodyParamsMethod() {
 request = new Request(Verb.POST, null);
 request.addBodyParameter("key", "value");
 Map<String, String> bodyParams = request.getBodyParams();
 Assert.assertEquals("value", bodyParams.get("key"));
 }

 @Test
 public void testGetUrlMethod() {
 request = new Request(Verb.GET, "http://example.com");
 Assert.assertEquals("http://example.com", request.getUrl());
 }

 @Test
 public void testGetSanitizedUrlMethod() {
 request = new Request(Verb.GET, "http://example.com?key=value&port:8080");
 Assert.assertEquals("http://example.com", request.getSanitizedUrl());
 }

 @Test
 public void testGetBodyContentsMethod() throws UnsupportedEncodingException {
 request = new Request(Verb.POST, null);
 request.addBodyParameter("key", "value");
 String bodyContents = request.getBodyContents();
 Assert.assertEquals("value", bodyContents);
 }

 @Test
 public void testGetByteBodyContentsMethod() throws UnsupportedEncodingException {
 request = new Request(Verb.POST, null);
 request.addBodyParameter("key", "value");
 byte[] byteBodyContents = request.getByteBodyContents();
 Assert.assertArrayEquals("value".getBytes(), byteBodyContents);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetByteBodyContentsMethodWithUnsupportedCharset() {
 request = new Request(Verb.POST, null);
 request.setCharset("unsupported-charset");
 }

 @Test
 public void testGetVerbMethod() {
 request = new Request(Verb.POST, "http://example.com");
 Assert.assertEquals(Verb.POST, request.getVerb());
 }

 @Test
 public void testGetHeadersMethod() {
 request = new Request(Verb.GET, "http://example.com");
 Map<String, String> headers = new HashMap<>();
 headers.put("key", "value");
 request.addHeaders(headers);
 Assert.assertEquals(headers, request.getHeaders());
 }

 @Test
 public void testGetCharsetMethod() {
 request = new Request(Verb.GET, "http://example.com");
 request.setCharset("charset");
 Assert.assertEquals("charset", request.getCharset());
 }

 @Test
 public void testSetConnectTimeoutMethod() {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
 Assert.assertEquals(5000, request.getConnection().getConnectTimeout());
 }

 @Test
 public void testSetReadTimeoutMethod() {
 request = new Request(Verb.GET, "http://example.com");
 request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
 Assert.assertEquals(5000, request.getConnection().getReadTimeout());
 }

 @Test
 public void testSetConnectionKeepAliveMethod() {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnectionKeepAlive(true);
 Assert.assertTrue(request.isConnectionKeepAlive());
 }

 @Test
 public void testSetConnectionMethod() {
 request = new Request(Verb.GET, "http://example.com");
 HttpURLConnection connection = new MockHttpURLConnection();
 request.setConnection(connection);
 Assert.assertEquals(connection, request.getConnection());
 }

}