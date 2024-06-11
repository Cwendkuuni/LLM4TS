package org.scribe.model;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ResponseTest {

 private static final String EMPTY = "";
 private static final String TEST_HEADER_KEY = "Test-Header-Key";
 private static final String TEST_HEADER_VALUE = "Test-Header-Value";

 @Test
 public void testResponseWithSuccessfulStatusCode() throws IOException {
 Response response = new Response(createMockConnection(200, EMPTY, new HashMap<>()));

 assertEquals(200, response.getCode());
 assertNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader(TEST_HEADER_KEY));
 }

 @Test
 public void testResponseWithErrorStatusCode() throws IOException {
 Response response = new Response(createMockConnection(404, EMPTY, new HashMap<>()));

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader(TEST_HEADER_KEY));
 }

 @Test
 public void testResponseWithBody() throws IOException {
 String testBody = "Test body";
 Response response = new Response(createMockConnection(200, testBody, new HashMap<>()));

 assertEquals(200, response.getCode());
 assertEquals(testBody, response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader(TEST_HEADER_KEY));
 }

 @Test
 public void testResponseWithHeaders() throws IOException {
 Response response = new Response(createMockConnection(200, EMPTY, createHeadersMap(TEST_HEADER_KEY, TEST_HEADER_VALUE)));

 assertEquals(200, response.getCode());
 assertNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertEquals(TEST_HEADER_VALUE, response.getHeader(TEST_HEADER_KEY));
 }

 @Test(expected = IOException.class)
 public void testResponseWithUnknownHostException() throws IOException {
 Response response = new Response(createMockConnectionWithException(new UnknownHostException("Test exception message")));

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader(TEST_HEADER_KEY));
 }

 private HttpURLConnection createMockConnection(int statusCode, String body, Map<String, String> headers) throws IOException {
 URL url = new URL("http://test.com");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setDoInput(true);
 connection.setRequestMethod("GET");
 connection.setInstanceFollowRedirects(false);
 connection.setConnectTimeout(1000);
 connection.setReadTimeout(1000);
 connection.setRequestProperty("Accept", "text/plain");
 connection.setRequestProperty("User-Agent", "Test/1.0");
 connection.setRequestProperty("Host", "test.com");
 connection.setResponseCode(statusCode);
 if (body != null) {
 connection.setDoOutput(true);
 OutputStream outputStream = connection.getOutputStream();
 outputStream.write(body.getBytes());
 outputStream.flush();
 outputStream.close();
 }
 if (headers != null) {
 for (Map.Entry<String, String> entry : headers.entrySet()) {
 connection.setRequestProperty(entry.getKey(), entry.getValue());
 }
 }
 return connection;
 }

 private HttpURLConnection createMockConnectionWithException(Exception exception) throws IOException {
 URL url = new URL("http://test.com");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setDoInput(true);
 connection.setRequestMethod("GET");
 connection.setInstanceFollowRedirects(false);
 connection.setConnectTimeout(1000);
 connection.setReadTimeout(1000);
 connection.setRequestProperty("Accept", "text/plain");
 connection.setRequestProperty("User-Agent", "Test/1.0");
 connection.setRequestProperty("Host", "test.com");
 connection.setDoOutput(false);
 connection.setThrows(exception);
 return connection;
 }

 private Map<String, String> createHeadersMap(String... headerKeyValues) {
 Map<String, String> headers = new HashMap<>();
 for (String headerKeyValue : headerKeyValues) {
 String[] keyValue = headerKeyValue.split("=");
 headers.put(keyValue[0], keyValue[1]);
 }
 return headers;
 }
}