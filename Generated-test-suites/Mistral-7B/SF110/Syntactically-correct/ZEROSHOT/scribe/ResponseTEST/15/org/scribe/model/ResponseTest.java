package org.scribe.model;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ResponseTest {

 private static final String EMPTY = "";
 private static final String KEY = "Content-Type";
 private static final String VALUE = "application/json";

 @Test
 public void testResponseWithSuccessfulStatusCode() throws IOException {
 Response response = new Response(createSuccessfulConnection("200 OK", "{}"));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertEquals(VALUE, response.getHeader(KEY));
 }

 @Test
 public void testResponseWithErrorStatusCode() throws IOException {
 Response response = new Response(createErrorConnection("404 Not Found", EMPTY));

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 Response response = new Response(createSuccessfulConnection("200 OK", EMPTY));

 assertEquals(200, response.getCode());
 assertNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testResponseWithNonEmptyBody() throws IOException {
 Response response = new Response(createSuccessfulConnection("200 OK", "{\"name\": \"John\", \"age\": 30}"));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertEquals(VALUE, response.getHeader(KEY));
 assertEquals("{\"name\": \"John\", \"age\": 30}", response.getBody());
 }

 @Test
 public void testResponseWithUnknownHostException() throws IOException {
 Response response = new Response(createErrorConnection("404 Not Found", EMPTY));

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testParseHeaders() throws IOException {
 HttpURLConnection connection = createSuccessfulConnection("200 OK", "{}");
 Response response = new Response(connection);

 Map<String, String> headers = response.getHeaders();

 assertEquals(VALUE, headers.get(KEY));
 }

 @Test
 public void testWasSuccessful() throws IOException {
 Response response = new Response(createSuccessfulConnection("200 OK", "{}"));

 assertTrue(response.wasSuccessful());
 }

 @Test
 public void testWasUnsuccessful() throws IOException {
 Response response = new Response(createErrorConnection("404 Not Found", EMPTY));

 assertFalse(response.wasSuccessful());
 }

 private HttpURLConnection createSuccessfulConnection(String status, String body) throws IOException {
 URL url = new URL("http://example.com");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setInstanceFollowRedirects(false);
 connection.setDoOutput(false);
 connection.setDoInput(true);
 connection.setRequestProperty("Accept", VALUE);
 connection.setRequestProperty("User-Agent", "Test/1.0");
 connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 connection.setRequestProperty("Connection", "keep-alive");
 connection.setRequestProperty("Host", "example.com");
 connection.setRequestProperty("Accept-Encoding", "gzip, deflate");

 if (body != null) {
 byte[] bodyBytes = body.getBytes("UTF-8");
 connection.setRequestProperty("Content-Length", String.valueOf(bodyBytes.length));
 OutputStream outputStream = connection.getOutputStream();
 outputStream.write(bodyBytes, 0, bodyBytes.length);
 outputStream.close();
 }
 connection.setResponseCode(Integer.parseInt(status.split(" ")[1]));
 connection.disconnect();
 return connection;
 }

 private HttpURLConnection createErrorConnection(String status, String body) throws IOException {
 URL url = new URL("http://example.com");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setInstanceFollowRedirects(false);
 connection.setDoOutput(false);
 connection.setDoInput(true);
 connection.setRequestProperty("Accept", VALUE);
 connection.setRequestProperty("User-Agent", "Test/1.0");
 connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 connection.setRequestProperty("Connection", "keep-alive");
 connection.setRequestProperty("Host", "example.com");
 connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
 connection.disconnect();
 throw new IOException(status);
 }
}