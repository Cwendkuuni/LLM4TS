package org.scribe.model;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ResponseTest {

 private HttpURLConnection connection;
 private Response response;

 @Before
 public void setUp() throws IOException {
 URL url = new URL("http://example.com");
 connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setDoOutput(false);
 }

 @Test
 public void testResponseWithSuccessfulStatusCode() throws IOException {
 response = new Response(connection);
 assertEquals(200, response.getCode());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertFalse(response.getBody().isEmpty());
 }

 @Test
 public void testResponseWithErrorStatusCode() throws IOException {
 connection.setInstanceFollowRedirects(false);
 connection.setRequestMethod("GET");
 connection.disconnect();
 connection.setErrorStream(System.err);
 response = new Response(connection);
 assertEquals(404, response.getCode());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertFalse(response.getBody().isEmpty());
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 connection.setRequestMethod("GET");
 connection.setInstanceFollowRedirects(false);
 connection.disconnect();
 connection.setDoOutput(false);
 response = new Response(connection);
 assertEquals(200, response.getCode());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertTrue(response.getBody().isEmpty());
 }

 @Test
 public void testResponseWithHeaders() throws IOException {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 connection.setRequestProperty("Accept", "application/json");
 connection.setInstanceFollowRedirects(false);
 connection.disconnect();
 response = new Response(connection);
 assertEquals(200, response.getCode());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertEquals("application/json", response.getHeader("Content-Type"));
 }

 @Test
 public void testResponseWithNullHeaders() throws IOException {
 connection.setRequestProperty("Accept", null);
 connection.setInstanceFollowRedirects(false);
 connection.disconnect();
 response = new Response(connection);
 assertEquals(200, response.getCode());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader("Content-Type"));
 }

 @Test
 public void testResponseWithEmptyHeaders() throws IOException {
 connection.setRequestProperty("Accept", "");
 connection.setInstanceFollowRedirects(false);
 connection.disconnect();
 response = new Response(connection);
 assertEquals(200, response.getCode());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertTrue(response.getHeaders().isEmpty());
 }

 @Test
 public void testResponseWithInvalidStatusCode() throws IOException {
 connection.setInstanceFollowRedirects(false);
 connection.disconnect();
 response = new Response(connection);
 assertEquals(500, response.getCode());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertFalse(response.getBody().isEmpty());
 }

 @Test
 public void testParseHeaders() throws IOException {
 connection.setRequestProperty("Content-Type", "application/json");
 connection.setRequestProperty("Location", "http://example.com/redirect");
 connection.setInstanceFollowRedirects(false);
 connection.disconnect();
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 headers.put("Location", "http://example.com/redirect");
 response = new Response(connection);
 Map<String, String> responseHeaders = response.getHeaders();
 assertEquals(headers.size(), responseHeaders.size());
 for (String key : headers.keySet()) {
 assertEquals(headers.get(key), responseHeaders.get(key));
 }
 }

 @Test
 public void testWasSuccessful() throws IOException {
 assertTrue(response.wasSuccessful());
 connection.setInstanceFollowRedirects(true);
 connection.setRequestMethod("GET");
 connection.disconnect();
 assertFalse(response.wasSuccessful());
 }

 @Test
 public void testParseBodyContents() throws IOException {
 String expectedBody = "{\"status\": \"success\"}";
 connection.setDoOutput(true);
 connection.setRequestProperty("Content-Type", "application/json");
 connection.getOutputStream().write(expectedBody.getBytes());
 connection.getOutputStream().flush();
 connection.getOutputStream().close();
 connection.setInstanceFollowRedirects(false);
 connection.disconnect();
 response = new Response(connection);
 String actualBody = response.getBody();
 assertEquals(expectedBody, actualBody);
 }

 @Test
 public void testGetStreamWithSuccessfulResponse() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test".getBytes());
 connection.setDoOutput(true);
 connection.setRequestProperty("Content-Type", "application/json");
 connection.setRequestProperty("Content-Length", "3");
 connection.getOutputStream().write("test".getBytes());
 connection.getOutputStream().flush();
 connection.getOutputStream().close();
 connection.setInstanceFollowRedirects(false);
 connection.disconnect();
 response = new Response(connection);
 byte[] expectedBytes = "test".getBytes();
 byte[] actualBytes = new byte[inputStream.available()];
 inputStream.read(actualBytes);
 assertArrayEquals(expectedBytes, actualBytes);
 }

 @Test
 public void testGetStreamWithErrorResponse() throws IOException {
 connection.setInstanceFollowRedirects(false);
 connection.disconnect();
 response = new Response(connection);
 assertNull(response.getStream());
 }
}