package org.scribe.model;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ResponseTest {

 private static final String TEST_URL = "http://example.com";

 @Test
 public void testResponseWithSuccessfulStatusCode() throws IOException {
 Response response = new Response(createConnection(TEST_URL, 200));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertFalse(response.wasSuccessful()); // since we explicitly checked the code above
 }

 @Test
 public void testResponseWithErrorStatusCode() throws IOException {
 Response response = new Response(createConnection(TEST_URL, 404));

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 Response response = new Response(createConnection(TEST_URL, 200));
 response.parseBodyContents(); // parse the body to make it nullable

 assertNotNull(response.getBody());
 assertTrue(response.getBody().isEmpty());
 }

 @Test
 public void testResponseWithHeaders() throws IOException {
 Response response = new Response(createConnectionWithHeaders(TEST_URL, 200, "Content-Type: text/plain; charset=UTF-8", "X-Custom-Header: Value"));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertEquals("Content-Type", response.getHeaders().keySet().iterator().next());
 assertEquals("text/plain; charset=UTF-8", response.getHeaders().get("Content-Type"));
 assertEquals("X-Custom-Header", response.getHeaders().keySet().iterator().next());
 assertEquals("Value", response.getHeaders().get("X-Custom-Header"));
 }

 @Test
 public void testResponseWithEmptyHeaders() throws IOException {
 Response response = new Response(createConnection(TEST_URL, 200));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertTrue(response.getHeaders().isEmpty());
 }

 @Test
 public void testResponseWithNoConnection() throws IOException {
 Response response = new Response(null);

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 private HttpURLConnection createConnection(String urlString, int statusCode) throws IOException {
 URL url = new URL(urlString);
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setInstanceFollowRedirects(false);
 connection.setRequestProperty("Accept", "application/json");
 connection.setDoOutput(false);
 connection.setConnectTimeout(1000);
 connection.disconnect(); // disconnect to simulate a successful response
 connection.setResponseCode(statusCode);
 return connection;
 }

 private HttpURLConnection createConnectionWithHeaders(String urlString, int statusCode, String... headerNamesAndValues) throws IOException {
 HttpURLConnection connection = createConnection(urlString, statusCode);
 for (String headerNameAndValue : headerNamesAndValues) {
 String[] parts = headerNameAndValue.split(":");
 connection.setRequestProperty(parts[0], parts[1]);
 }
 return connection;
 }
}