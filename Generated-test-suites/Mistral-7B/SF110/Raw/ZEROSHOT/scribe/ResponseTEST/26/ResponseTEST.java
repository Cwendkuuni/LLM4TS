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

 private static final String TEST_URL = "http://test.com";

 @Test
 public void testResponseWithSuccessfulStatusCode() throws IOException {
 Response response = new Response(createConnectionWithSuccessStatusCode(200));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testResponseWithErrorStatusCode() throws IOException {
 Response response = new Response(createConnectionWithErrorStatusCode(404));

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 Response response = new Response(createConnectionWithSuccessStatusCode(204));

 assertEquals(204, response.getCode());
 assertNull(response.getBody());
 assertNotNull(response.getStream());
 }

 @Test
 public void testResponseWithBody() throws IOException {
 Response response = new Response(createConnectionWithSuccessStatusCode(200, "Test body"));;

 assertEquals(200, response.getCode());
 assertEquals("Test body", response.getBody());
 assertNotNull(response.getStream());
 }

 @Test
 public void testResponseWithHeaders() throws IOException {
 Response response = new Response(createConnectionWithHeaders());

 assertEquals(200, response.getCode());
 assertNotNull(response.getHeaders());
 assertEquals("Test-Header", response.getHeader("Test-Header"));
 }

 @Test
 public void testResponseWithNullHeaders() throws IOException {
 Response response = new Response(createConnectionWithNullHeaders());

 assertEquals(200, response.getCode());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader("Test-Header"));
 }

 @Test(expected = UnknownHostException.class)
 public void testResponseWithUnknownHostException() throws IOException {
 Response response = new Response(createConnectionWithUnknownHostException());

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 private HttpURLConnection createConnectionWithSuccessStatusCode(int code) throws IOException {
 return createConnectionWithStatusCode(code, "Test body".getBytes());
 }

 private HttpURLConnection createConnectionWithErrorStatusCode(int code) throws IOException {
 return createConnectionWithStatusCode(code, EMPTY.getBytes());
 }

 private HttpURLConnection createConnectionWithHeaders() throws IOException {
 HttpURLConnection connection = (HttpURLConnection) new URL(TEST_URL).openConnection();
 connection.setRequestProperty("Test-Header", "Test-Header Value");
 return connection;
 }

 private HttpURLConnection createConnectionWithNullHeaders() throws IOException {
 return (HttpURLConnection) new URL(TEST_URL).openConnection();
 }

 private HttpURLConnection createConnectionWithStatusCode(int code, byte[] body) throws IOException {
 HttpURLConnection connection = (HttpURLConnection) new URL(TEST_URL).openConnection();
 connection.setDoInput(true);
 connection.setInstanceFollowRedirects(false);
 connection.setRequestMethod("GET");
 connection.setRequestProperty("Accept", "application/json");
 connection.disconnect();
 connection.setResponseCode(code);
 connection.setDoOutput(false);
 if (body != null) {
 connection.setFixedLengthStreamingMode(body.length);
 OutputStream os = connection.getOutputStream();
 os.write(body);
 os.close();
 }
 return connection;
 }
}