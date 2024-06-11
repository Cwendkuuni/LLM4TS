import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.nio.charset.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class RequestTest {

 private Request request;
 private HttpURLConnection connection;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 connection = mock(HttpURLConnection.class);
 }

 @After
 public void tearDown() throws Exception {
 request = null;
 connection = null;
 }

 @Test
 public void testRequestWithValidArguments() {
 Request request = new Request(Verb.GET, "http://example.com");
 assertNotNull(request);
 assertEquals(Verb.GET, request.getVerb());
 assertEquals("http://example.com", request.getUrl());
Map<String, String> params = request.getQueryStringParams();
 assertTrue(params.isEmpty());
 }

 @Test
 public void testSend_200OK() throws Exception {
 request.addHeader("Content-Type", "application/json");
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 }

 @Test(expected = OAuthException.class)
 public void testSend_MalformedURL() throws Exception {
 request = new Request(Verb.GET, "http://example:/");
 request.send();
 fail("An OAuthException should be thrown for a malformed URL");
 }

 @Test(expected = OAuthException.class)
 public void testSend_IOError() throws Exception {
 doThrow(new IOException()).when(connection).setRequestProperty(anyString(), anyString());
 request.send();
 fail("An OAuthException should be thrown for an I/O error");
 }

 @Test(expected = OAuthException.class)
 public void testSend_InvalidVerb() throws Exception {
 request = new Request(null, "http://example.com");
 request.send();
 fail("An OAuthException should be thrown for an invalid verb");
 }

 @Test
 public void testAddHeader() {
 request.addHeader("Content-Type", "application/json");
 assertEquals("application/json", request.getHeaders().get("Content-Type"));
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("param1", "value1");
 assertEquals("value1", request.getBodyParams().get("param1"));
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("param1", "value1");
 Map<String, String> params = request.getQueryStringParams();
 assertEquals("value1", params.get("param1"));
 }

 @Test
 public void testAddPayload() {
 request.addPayload("payload");
 assertEquals("payload", request.getPayload());
 }

 @Test
 public void testAddPayloadByteArray() {
 byte[] bytePayload = "bytePayload".getBytes();
 request.addPayload(bytePayload);
 assertEquals(bytePayload, request.getBytePayload());
 }

 @Test(expected = OAuthException.class)
 public void testSend_ConnectionTimeout() throws Exception {
 doThrow(new SocketTimeoutException("Connection Timed Out")).when(connection).setDoOutput(anyBoolean());
 request.send();
 fail("An OAuthException should be thrown for a connection timeout");
 }

 @Test(expected = OAuthException.class)
 public void testSend_ReadTimeout() throws Exception {
 doThrow(new SocketTimeoutException("Read Timed Out")).when(connection).getInputStream();
 request.send();
 fail("An OAuthException should be thrown for a read timeout");
 }

 @Test(expected = OAuthException.class)
 public void testSend_InvalidCharset() throws Exception {
 request.setCharset("invalidCharset");
 request.send();
 fail("An OAuthException should be thrown for an invalid charset");
 }

 @Test
 public void testGetQueryStringParams() {
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 Map<String, String> params = request.getQueryStringParams();
 assertEquals(2, params.size());
 assertEquals("value1", params.get("param1"));
 assertEquals("value2", params.get("param2"));
 }

 @Test
 public void testGetBodyParams() {
 request.addBodyParameter("param1", "value1");
 Map<String, String> params = request.getBodyParams();
 assertEquals(1, params.size());
 assertEquals("value1", params.get("param1"));
 }

 @Test
 public void testGetUrl() {
 assertEquals("http://example.com", request.getUrl());
 }

 @Test
 public void testGetSanitizedUrl() {
 assertEquals("example.com", request.getSanitizedUrl());
 }

 @Test
 public void testGetBodyContents() throws UnsupportedEncodingException {
 request.addPayload("payload");
 assertEquals("payload", request.getBodyContents());
 }

 @Test
 public void testGetVerb() {
 assertEquals(Verb.GET, request.getVerb());
 }

 @Test
 public void testGetHeaders() {
 request.addHeader("Content-Type", "application/json");
 Map<String, String> headers = request.getHeaders();
 assertEquals(1, headers.size());
 assertEquals("application/json", headers.get("Content-Type"));
 }

 @Test
 public void testGetCharset() {
 assertEquals("UTF-8", request.getCharset());
 }

 @Test
 public void testSetConnectTimeout() {
 request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
 assertEquals(5000, request.getConnection().getConnectTimeout());
 }

 @Test
 public void testSetReadTimeout() {
 request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
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
 public void testSetConnection() {
 Request request = new Request(Verb.GET, "http://example.com");
 HttpURLConnection connection = mock(HttpURLConnection.class);
 request.setConnection(connection);
 assertEquals(connection, request.getConnection());
 }
}