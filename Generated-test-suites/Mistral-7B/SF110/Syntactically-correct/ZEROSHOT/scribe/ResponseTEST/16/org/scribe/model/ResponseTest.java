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
 Response response = new Response(createConnectionWithSuccessfulStatusCode(200));

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
 Response response = new Response(createConnectionWithSuccessfulStatusCode(204));

 assertEquals("", response.getBody());
 }

 @Test
 public void testResponseWithHeaders() throws IOException {
 Response response = new Response(createConnectionWithHeaders());

 Map<String, String> headers = response.getHeaders();
 assertEquals("Content-Type: text/html;charset=UTF-8", headers.get("Content-Type"));
 assertEquals("Date: Thu, 01 Jan 2010 00:00:01 GMT", headers.get("Date"));
 // Add more assertions as needed
 }

 @Test(expected = UnknownHostException.class)
 public void testResponseWithUnknownHostException() throws IOException {
 Response response = new Response(createConnectionWithUnknownHostException());

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 private HttpURLConnection createConnectionWithSuccessfulStatusCode(int statusCode) throws IOException {
 URL url = new URL(TEST_URL);
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setDoOutput(false);
 connection.setInstanceFollowRedirects(false);
 connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
 connection.disconnect();
 connection.setResponseCode(statusCode);
 return connection;
 }

 private HttpURLConnection createConnectionWithErrorStatusCode(int statusCode) throws IOException {
 URL url = new URL(TEST_URL);
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setDoOutput(false);
 connection.setInstanceFollowRedirects(false);
 connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
 connection.disconnect();
 connection.setResponseCode(statusCode);
 return connection;
 }

 private HttpURLConnection createConnectionWithHeaders() throws IOException {
 URL url = new URL(TEST_URL);
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setDoOutput(false);
 connection.setInstanceFollowRedirects(false);
 connection.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
 connection.setRequestProperty("Date", "Thu, 01 Jan 2010 00:00:01 GMT");
 connection.disconnect();
 connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
 connection.setResponseCode(200);
 return connection;
 }

 private HttpURLConnection createConnectionWithUnknownHostException() throws IOException {
 URL url = new URL("http://non-existent.com");
 throw new UnknownHostException("Unknown host: non-existent.com");
 }
}