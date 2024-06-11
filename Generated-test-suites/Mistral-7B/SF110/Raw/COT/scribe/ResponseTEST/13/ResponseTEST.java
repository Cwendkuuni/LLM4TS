package org.scribe.model.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.scribe.model.Response;

public class ResponseTest {

 private static final String EMPTY = Response.EMPTY;

 @Test
 public void testResponseConstructionWithSuccessfulConnection() throws IOException {
 Response response = new Response(createSuccessfulConnection());
 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getHeaders());
 assertNotNull(response.getStream());
 }

 @Test(expected = UnknownHostException.class)
 public void testResponseConstructionWithFailedConnection() {
 Response response = new Response(createFailedConnection());
 assertEquals(404, response.getCode());
 assertEquals(EMPTY, response.getBody());
 assertNull(response.getHeaders());
 assertNull(response.getStream());
 }

 @Test
 public void testParseBodyContents() throws IOException {
 Response response = new Response(createSuccessfulConnection());
 response.parseBodyContents();
 assertEquals(Response.EMPTY, response.getBody()); // Initially empty
 String body = "test body";
 response.setBody(body);
 assertEquals(body, response.parseBodyContents());
 }

 @Test
 public void testParseHeaders() throws IOException {
 Response response = new Response(createSuccessfulConnection());
 Map<String, String> headers = response.parseHeaders(createSuccessfulConnection());
 assertEquals(2, headers.size());
 assertTrue(headers.containsKey("Content-Type"));
 assertTrue(headers.containsKey("Content-Length"));
 }

 @Test
 public void testWasSuccessful() {
 Response response = new Response();
 response.setCode(201);
 assertTrue(response.wasSuccessful());
 response.setCode(401);
 assertFalse(response.wasSuccessful());
 }

 @Test
 public void testGetBody() {
 String body = "test body";
 Response response = new Response();
 response.setBody(body);
 assertEquals(body, response.getBody());
 }

 @Test
 public void testGetStream() throws IOException {
 Response response = new Response();
 ByteArrayInputStream inputStream = new ByteArrayInputStream("test stream".getBytes());
 response.setStream(inputStream);
 InputStream actualStream = response.getStream();
 assertEquals(inputStream, actualStream);
 }

 @Test
 public void testGetCode() {
 Response response = new Response();
 response.setCode(200);
 assertEquals(200, response.getCode());
 }

 @Test
 public void testGetHeaders() {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 Response response = new Response();
 response.setHeaders(headers);
 Map<String, String> actualHeaders = response.getHeaders();
 assertEquals(headers, actualHeaders);
 }

 @Test
 public void testGetHeader() {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 headers.put("Content-Length", "10");
 Response response = new Response();
 response.setHeaders(headers);
 String actualHeader = response.getHeader("Content-Type");
 assertEquals("application/json", actualHeader);
 }

 @Test(expected = NullPointerException.class)
 public void testGetHeaderWithNullHeadersMap() {
 Response response = new Response();
 response.setHeaders(null);
 response.getHeader("Content-Type");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetHeaderWithNullOrEmptyHeaderName() {
 Response response = new Response();
 Map<String, String> headers = new HashMap<>();
 headers.put("", "test value");
 response.setHeaders(headers);
 response.getHeader(null);
 response.getHeader("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetHeaderWithNullOrEmptyHeaderValue() {
 Response response = new Response();
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", null);
 response.setHeaders(headers);
 response.getHeader("Content-Type");
 }

 @Test
 public void testResponseConstructionWithIOException() throws IOException {
 Response response = new Response(createIOExceptionConnection());
 assertEquals(500, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getHeaders());
 assertNull(response.getStream());
 }

 private HttpURLConnection createSuccessfulConnection() throws IOException {
 URL url = new URL("http://example.com/");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setInstanceFollowRedirects(false);
 connection.setDoOutput(false);
 connection.disconnect();
 return connection;
 }

 private HttpURLConnection createFailedConnection() throws IOException {
 URL url = new URL("http://example.com/notfound");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.disconnect();
 throw new UnknownHostException("Unknown host: example.com");
 }

 private HttpURLConnection createIOExceptionConnection() throws IOException {
 URL url = new URL("http://example.com/error");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.disconnect();
 throw new IOException("Error reading response");
 }
}