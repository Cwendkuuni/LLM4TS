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

 private static final String TEST_URL = "http://test.com";
 private static final String TEST_BODY = "This is a test response body.";
 private static final String TEST_HEADER_NAME = "Content-Type";
 private static final String TEST_HEADER_VALUE = "text/plain";

 @Test
 public void testResponseWithSuccessfulStatus() throws IOException {
 Response response = new Response(createConnectionWithSuccessStatus(TEST_URL));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertEquals(TEST_BODY, response.getBody());
 assertNotNull(response.getStream());

 Map<String, String> headers = response.getHeaders();
 assertEquals(1, headers.size());
 assertEquals(TEST_HEADER_VALUE, headers.get(TEST_HEADER_NAME));
 }

 @Test
 public void testResponseWithErrorStatus() throws IOException {
 Response response = new Response(createConnectionWithErrorStatus(TEST_URL));

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 Response response = new Response(createConnectionWithEmptyBody(TEST_URL));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertEquals(EMPTY, response.getBody());
 }

 @Test
 public void testResponseWithCustomHeaders() throws IOException {
 Response response = new Response(createConnectionWithCustomHeaders(TEST_URL));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());

 Map<String, String> headers = response.getHeaders();
 assertEquals(2, headers.size());
 assertEquals(TEST_HEADER_VALUE, headers.get(TEST_HEADER_NAME));
 assertEquals("application/json", headers.get("X-Custom-Header"));
 }

 private HttpURLConnection createConnectionWithSuccessStatus(String urlString) throws IOException {
 URL url = new URL(urlString);
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setRequestProperty("Accept", "text/plain");
 connection.setDoOutput(false);
 connection.disconnect();
 return connection;
 }

 private HttpURLConnection createConnectionWithErrorStatus(String urlString) throws IOException {
 URL url = new URL(urlString);
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.disconnect();
 return connection;
 }

 private HttpURLConnection createConnectionWithEmptyBody(String urlString) throws IOException {
 HttpURLConnection connection = createConnectionWithSuccessStatus(urlString);
 OutputStream outputStream = connection.getOutputStream();
 outputStream.close();
 return connection;
 }

 private HttpURLConnection createConnectionWithCustomHeaders(String urlString) throws IOException {
 HttpURLConnection connection = createConnectionWithSuccessStatus(urlString);
 connection.setRequestProperty("X-Custom-Header", "application/json");
 connection.disconnect();
 return connection;
 }
}