import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.*;
import org.scribe.exceptions.*;
import java.net.HttpURLConnection;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestTest {

 private Request request;

 @Test
 public void testRequestConstructor() {
 request = new Request(Verb.GET, "http://example.com");
 Assert.assertNull(request.payload);
 Assert.assertNull(request.bytePayload);
 Assert.assertFalse(request.connectionKeepAlive);
 Assert.assertEquals(Verb.GET, request.getVerb());
 Assert.assertEquals("http://example.com", request.getUrl());
 Assert.assertEquals(new HashMap<>(), request.getBodyParams());
 Assert.assertEquals(new HashMap<>(), request.getHeaders());
 Assert.assertEquals(new HashMap<>(), request.getQueryStringParams());
 }

 @Test
 public void testSendMethodCallsCreateConnectionAndDoSend() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.createConnection();
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testSendThrowsIOException() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 when(mockHttpURLConnection.createConnection()).thenThrow(new IOException("Test error"));
 request.send();
 }

 @Test
 public void testCreateConnectionAppendsQueryStringParams() throws IOException {
 request = new Request(Verb.GET, "http://example.com?param1=value1&param2=value2");
 request.addQuerystringParameter("param1", "value3");
 request.addQuerystringParameter("param2", "value4");
 request.createConnection();
 String effectiveUrl = request.connection.getURL().toString();
 Map<String, String> params = request.getQueryStringParams();
 assertTrue(effectiveUrl.contains("param1=value3"));
 assertTrue(effectiveUrl.contains("param2=value4"));
 }

 @Test
 public void testCreateConnectionUsesDefaultConnectionKeepAliveSetting() {
 request = new Request(Verb.GET, "http://example.com");
 request.createConnection();
 Assert.assertEquals(false, request.connectionKeepAlive);
 }

 @Test
 public void testCreateConnectionUsesSpecifiedConnectionKeepAliveSetting() {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnectionKeepAlive(true);
 request.createConnection();
 Assert.assertEquals(true, request.connectionKeepAlive);
 }

 @Test
 public void testDoSendSetsRequestMethod() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.createConnection();
 request.send();
 assertEquals(Verb.GET.name(), request.connection.getRequestMethod());
 }

 @Test
 public void testDoSendAddsHeaders() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.createConnection();
 request.addHeader("HeaderKey", "HeaderValue");
 request.doSend();
 assertEquals("HeaderValue", request.connection.getRequestProperty("HeaderKey"));
 }

 @Test
 public void testDoSendAddsBodyWhenUsingPUTOrPOST() throws IOException {
 request = new Request(Verb.POST, "http://example.com");
 request.addBodyParameter("param1", "value1");
 request.createConnection();
 request.doSend();
 Assert.assertEquals("Content-Length:" + request.getByteBodyContents().length, request.connection.getRequestProperty("Content-Length"));
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetByteBodyContentsThrowsUnsupportedEncodingException() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.addPayload("test");
 request.setCharset("InvalidEncoding");
 request.getByteBodyContents();
 }

 @Test
 public void testGetByteBodyContentsReturnsByteArrayOfPayload() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.addPayload("test");
 request.setCharset("UTF-8");
 byte[] expected = "test".getBytes("UTF-8");
 byte[] actual = request.getByteBodyContents();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetByteBodyContentsReturnsByteArrayOfBodyParams() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 request.setCharset("UTF-8");
 Map<String, String> bodyParams = request.getBodyParams();
 String payload = "param1=" + URLEncoder.encode("value1", "UTF-8") + "&param2=" + URLEncoder.encode("value2", "UTF-8");
 byte[] expected = payload.getBytes("UTF-8");
 byte[] actual = request.getByteBodyContents();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetCharsetReturnsDefaultCharsetIfNull() {
 request = new Request(Verb.GET, "http://example.com");
 assertEquals(Charset.defaultCharset().name(), request.getCharset());
 }

 @Test
 public void testGetCharsetReturnsSpecifiedCharset() {
 request = new Request(Verb.GET, "http://example.com");
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.getCharset());
 }

 @Test
 public void testSetConnectTimeout() {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
 assertEquals(5000, request.connection.getConnectTimeout());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetConnectTimeoutWithNegativeDuration() {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnectTimeout(-1, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testSetReadTimeout() {
 request = new Request(Verb.GET, "http://example.com");
 request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
 assertEquals(5000, request.connection.getReadTimeout());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetReadTimeoutWithNegativeDuration() {
 request = new Request(Verb.GET, "http://example.com");
 request.setReadTimeout(-1, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testToStringMethodReturnsCorrectStringRepresentation() {
 request = new Request(Verb.GET, "http://example.com");
 assertEquals("@Request(GET http://example.com)", request.toString());
 }
}