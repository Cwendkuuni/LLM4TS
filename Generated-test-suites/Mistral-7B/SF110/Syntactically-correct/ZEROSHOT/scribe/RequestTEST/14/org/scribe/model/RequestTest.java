package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.*;
import org.scribe.exceptions.OAuthException;
import org.scribe.utils.URLUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testRequestWithDefaultValues() {
 // Test initial values
 assertEquals(Verb.GET, request.getVerb());
 assertEquals("http://example.com", request.getUrl());
 assertNull(request.getPayload());
 assertNull(request.getBytePayload());
 assertFalse(request.isConnectionKeepAlive());
 assertNull(request.getCharset());
 assertNotNull(request.getHeaders());
 assertEmpty(request.getHeaders());
 assertNull(request.getConnection());
 }

 @Test
 public void testAddingHeaders() {
 request.addHeader("Authorization", "Bearer token123");
 request.addHeader("Content-Type", "application/json");
 Map<String, String> headers = request.getHeaders();
 assertEquals("Bearer token123", headers.get("Authorization"));
 assertEquals("application/json", headers.get("Content-Type"));
 }

 @Test
 public void testAddingQuerystringParameters() {
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 Map<String, String> params = request.getQueryStringParams();
 assertEquals("param1=value1&param2=value2", params.toString());
 }

 @Test
 public void testAddingBodyParameters() {
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals("param1=value1&param2=value2", bodyParams.toString());
 }

 @Test
 public void testAddingPayload() {
 request.addPayload("This is a test payload");
 String payload = request.getBodyContents();
 assertEquals("This is a test payload", payload);
 }

 @Test
 public void testAddingBytePayload() throws IOException {
 String testData = "This is a test byte payload";
 byte[] bytePayload = testData.getBytes("UTF-8");
 request.addPayload(bytePayload);
 InputStream inputStream = new ByteArrayInputStream(bytePayload);
 byte[] receivedBytePayload = request.getByteBodyContents();
 assertArrayEquals(bytePayload, receivedBytePayload);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testAddingPayloadWithUnsupportedCharset() {
 request.addPayload("This is a test payload with unsupported charset XYZ");
 }

 @Test(expected = OAuthException.class)
 public void testFailureCreatingConnection() throws IOException {
 request.setConnectionKeepAlive(true);
 try {
 request.createConnection();
 fail("Expected exception when creating connection");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testSendingRequest() throws IOException {
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
 request.setConnection(connection);
 Response response = request.send();
 assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
 }

 @Test
 public void testSendingRequestWithBody() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String testData = "This is a test payload";
 OutputStream os = outputStream;
 byte[] bytePayload = testData.getBytes("UTF-8");
 request.addPayload(bytePayload);
 request.setConnectionKeepAlive(true);
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getDoOutput()).thenReturn(true);
 when(connection.getOutputStream()).thenReturn(os);
 when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
 request.setConnection(connection);
 request.send();
 String responseBody = asString(os.toByteArray());
 assertEquals(testData, responseBody);
 }

 @Test
 public void testSendingRequestWithKeepAlive() throws IOException {
 request.setConnectionKeepAlive(true);
 request.setConnectTimeout(10, TimeUnit.SECONDS);
 request.setReadTimeout(15, TimeUnit.SECONDS);
 Response response = request.send();
 assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
 }

 @Test
 public void testSendingRequestWith不支持的字符集() throws IOException {
 request.setCharset("XYZ");
 try {
 request.send();
 fail("Expected OAuthException with unsupported charset");
 } catch (OAuthException e) {
 assertEquals("Unsupported Charset: XYZ", e.getMessage());
 }
 }

 private String asString(byte[] bytes) throws IOException {
 InputStream is = new ByteArrayInputStream(bytes);
 String result = "";
 byte[] buffer = new byte[1024];
 int n = 0;
 while (-1 != (n = is.read(buffer, 0, buffer.length))) {
 result += new String(Arrays.copyOfRange(buffer, 0, n), "UTF-8");
 }
 return result;
 }
}