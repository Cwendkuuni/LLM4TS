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
 private static final String TEST_HEADER_KEY = "Test-Header";
 private static final String TEST_HEADER_VALUE = "Test-Header-Value";

 @Test
 public void testResponseWithSuccessfulCode() throws IOException {
 Response response = new Response(createSuccessfulConnection());

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader(null));
 assertNull(response.getHeader(""));

 Map<String, String> headers = response.getHeaders();
 assertEquals(TEST_HEADER_VALUE, headers.get(TEST_HEADER_KEY));
 }

 @Test
 public void testResponseWithErrorCode() throws IOException {
 Response response = new Response(createErrorConnection());

 assertEquals(404, response.getCode());
 assertEquals(EMPTY, response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader(null));
 assertNull(response.getHeader(""));
 }

 @Test
 public void testParseHeaders() throws IOException {
 URL url = new URL("http://example.com");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestProperty("Test-Header", TEST_HEADER_VALUE);
 connection.disconnect();

 Response response = new Response(connection);
 Map<String, String> headers = response.getHeaders();
 assertEquals(TEST_HEADER_VALUE, headers.get(TEST_HEADER_KEY));
 }

 @Test
 public void testWasSuccessful() throws IOException {
 Response response = new Response(createSuccessfulConnection());
 assertTrue(response.wasSuccessful());

 Response responseWithError = new Response(createErrorConnection());
 assertFalse(responseWithError.wasSuccessful());
 }

 @Test
 public void testGetBody() throws IOException {
 String testBody = "Test body";
 URL url = new URL("http://example.com");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setDoOutput(true);
 connection.setRequestMethod("POST");
 connection.getOutputStream().write(testBody.getBytes());
 connection.getOutputStream().flush();
 connection.getOutputStream().close();

 Response response = new Response(connection);
 assertEquals(testBody, response.getBody());
 }

 @Test
 public void testGetHeader() throws IOException {
 Response response = new Response(createSuccessfulConnection());
 response.setHeader(TEST_HEADER_KEY, TEST_HEADER_VALUE);

 assertEquals(TEST_HEADER_VALUE, response.getHeader(TEST_HEADER_KEY));
 }

 @Test
 public void testSetHeader() throws IOException {
 Response response = new Response(createSuccessfulConnection());

 ByteArrayInputStream testInputStream = new ByteArrayInputStream(new byte[0]);
 response.setStream(testInputStream);
 response.setHeader(TEST_HEADER_KEY, TEST_HEADER_VALUE);

 assertEquals(TEST_HEADER_VALUE, response.getHeader(TEST_HEADER_KEY));
 }

 private HttpURLConnection createSuccessfulConnection() throws IOException {
 HttpURLConnection connection = new URL("http://example.com").openConnection();
 connection.setInstanceFollowRedirects(false);
 assertEquals(200, connection.getResponseCode());
 return connection;
 }

 private HttpURLConnection createErrorConnection() throws IOException {
 HttpURLConnection connection = new URL("http://example.com/not-found").openConnection();
 connection.setInstanceFollowRedirects(false);
 assertEquals(404, connection.getResponseCode());
 return connection;
 }

}