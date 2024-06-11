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
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testCreateRequest() {
 assertNotNull(request);
 assertEquals(Verb.GET, request.getVerb());
 assertEquals("http://example.com", request.getUrl());
 assertEquals(new HashMap<>(), request.getQueryStringParams());
 assertEquals(new HashMap<>(), request.getBodyParams());
 assertEquals(new HashMap<>(), request.getHeaders());
 assertNull(request.getPayload());
 assertNull(request.getBytePayload());
 assertFalse(request.isConnectionKeepAlive());
 assertNull(request.getConnection());
 assertNull(request.getCharset());
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("key", "value");
 Map<String, String> params = request.getQueryStringParams();
 assertEquals("value", params.get("key"));
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("key", "value");
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals("value", bodyParams.get("key"));
 }

 @Test
 public void testAddHeader() {
 request.addHeader("key", "value");
 Map<String, String> headers = request.getHeaders();
 assertEquals("value", headers.get("key"));
 }

 @Test
 public void testAddPayload() {
 request.addPayload("payload");
 String payload = request.getPayload();
 assertEquals("payload", payload);
 }

 @Test
 public void testAddPayloadByte() throws IOException {
 byte[] bytePayload = "payload".getBytes();
 request.addPayload(bytePayload);
 byte[] getBytePayload = request.getBytePayload();
 assertArrayEquals(bytePayload, getBytePayload);
 }

 @Test
 public void testSetConnectionKeepAlive() {
 request.setConnectionKeepAlive(true);
 assertTrue(request.isConnectionKeepAlive());
 }

 @Test
 public void testSetCharset() {
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.getCharset());
 }

 @Test
 public void testSetConnection() {
 HttpURLConnection connection = mock(HttpURLConnection.class);
 request.setConnection(connection);
 assertEquals(connection, request.getConnection());
 }

 @Test(expected = OAuthException.class)
 public void testMalformedURL() {
 request = new Request(Verb.GET, "example:://example.com");
 }

 @Test(expected = OAuthException.class)
 public void testNullUrl() {
 request = new Request(Verb.GET, null);
 }

 @Test
 public void testSendGETRequest() throws IOException, InterruptedException {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnectTimeout(5, TimeUnit.SECONDS);
 request.setReadTimeout(5, TimeUnit.SECONDS);
 InputStream in = new ByteArrayInputStream(new byte[]{"Test"[0]});
 URL url = new URL("http://example.com");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setDoOutput(true);
 connection.setRequestProperty("Accept-Charset", "UTF-8");
 connection.setRequestProperty("User-Agent", "Java");
 try {
 request.send();
 fail("Expected OAuthException");
 } catch (OAuthException e) {
 // expected
 }
 connection.disconnect();
 }

 @Test
 public void testSendGETRequestWithHeaders() throws IOException, InterruptedException {
 request = new Request(Verb.GET, "http://example.com");
 request.addHeader("Accept", "image/*");
 request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:11.0) Gecko/20100101 Firefox/11.0");
 request.addQuerystringParameter("param", "value");
 request.setConnectTimeout(5, TimeUnit.SECONDS);
 request.setReadTimeout(5, TimeUnit.SECONDS);
 InputStream in = new ByteArrayInputStream(new byte[]{"Test"[0]});
 URL url = new URL("http://example.com?param=value");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setDoOutput(true);
 connection.setRequestProperty("Accept", request.getHeaders().get("Accept"));
 connection.setRequestProperty("User-Agent", request.getHeaders().get("User-Agent"));
 try {
 request.send();
 fail("Expected OAuthException");
 } catch (OAuthException e) {
 // expected
 }
 connection.disconnect();
 }

 @Test
 public void testSendPOSTRequest() throws IOException, InterruptedException {
 request = new Request(Verb.POST, "http://example.com");
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 request.setConnectTimeout(5, TimeUnit.SECONDS);
 request.setReadTimeout(5, TimeUnit.SECONDS);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test".getBytes());
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 request.setPayload("Test");
 request.setBytePayload(outputStream.toByteArray());
 request.setCharset("UTF-8");
 URL url = new URL("http://example.com");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setDoOutput(true);
 connection.setRequestProperty("Accept", "application/json");
 connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
 connection.setRequestProperty("User-Agent", "Java");
 try {
 request.send();
 String response = URLUtils.streamToString(connection.getInputStream(), "UTF-8");
 assertEquals("Test", response);
 } finally {
 connection.disconnect();
 }
 }

 @Test
 public void testSendPUTRequest() throws IOException, InterruptedException {
 request = new Request(Verb.PUT, "http://example.com");
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 request.setConnectTimeout(5, TimeUnit.SECONDS);
 request.setReadTimeout(5, TimeUnit.SECONDS);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test".getBytes());
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 request.setPayload("Test");
 request.setBytePayload(outputStream.toByteArray());
 request.setCharset("UTF-8");
 URL url = new URL("http://example.com");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setDoOutput(true);
 connection.setRequestProperty("Accept", "application/json");
 connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
 connection.setRequestProperty("User-Agent", "Java");
 try {
 request.send();
 int responseCode = connection.getResponseCode();
 assertEquals(HttpURLConnection.HTTP_OK, responseCode);
 } finally {
 connection.disconnect();
 }
 }
}