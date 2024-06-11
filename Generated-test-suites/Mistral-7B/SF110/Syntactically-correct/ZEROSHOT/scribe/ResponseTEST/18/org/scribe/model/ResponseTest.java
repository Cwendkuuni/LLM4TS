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

 private static final String TEST_URL = "http://example.com";
 private static final String TEST_HEADER_KEY = "Content-Type";
 private static final String TEST_HEADER_VALUE = "application/json";

 @Test
 public void testResponseWithSuccessfulStatusCode() throws IOException {
 Response response = new Response(createConnectionWithSuccessfulStatusCode(200));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertEquals(TEST_HEADER_VALUE, response.getHeader(TEST_HEADER_KEY));
 }

 @Test
 public void testResponseWithErrorStatusCode() throws IOException {
 Response response = new Response(createConnectionWithErrorStatusCode(404));

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertEquals(EMPTY, response.getHeader(TEST_HEADER_KEY));
 }

 @Test
 public void testParseBodyContents() throws IOException {
 Response response = new Response(createConnectionWithSuccessfulStatusCode(200));
 response.parseBodyContents();

 assertNotNull(response.getBody());
 }

 @Test(expected = IOException.class)
 public void testParseBodyContentsWithErrorStream() throws IOException {
 Response response = new Response(createConnectionWithErrorStatusCode(500));
 response.parseBodyContents();
 }

 @Test
 public void testParseHeaders() throws UnsupportedEncodingException, IOException {
 HttpURLConnection connection = createConnectionWithSuccessfulHeaders();
 Response response = new Response(connection);

 Map<String, String> headers = response.getHeaders();
 assertEquals(2, headers.size());
 assertEquals(TEST_HEADER_VALUE, headers.get(TEST_HEADER_KEY));
 }

 @Test
 public void testWasSuccessful() throws IOException {
 Response response = new Response(createConnectionWithSuccessfulStatusCode(200));

 assertTrue(response.wasSuccessful());

 response = new Response(createConnectionWithErrorStatusCode(401));

 assertFalse(response.wasSuccessful());
 }

 private HttpURLConnection createConnectionWithSuccessfulStatusCode(int statusCode) throws IOException {
 URL url = new URL(TEST_URL);
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setInstanceFollowRedirects(false);
 connection.setRequestProperty("Accept-Encoding", "identity");
 connection.setDoOutput(false);
 connection.setConnectTimeout(1000);
 connection.setReadTimeout(1000);
 connection.setResponseCode(statusCode);
 return connection;
 }

 private HttpURLConnection createConnectionWithErrorStatusCode(int statusCode) throws IOException {
 URL url = new URL(TEST_URL);
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setInstanceFollowRedirects(false);
 connection.setDoOutput(false);
 connection.setConnectTimeout(1000);
 connection.setReadTimeout(1000);
 connection.setResponseCode(statusCode);
 return connection;
 }

 private HttpURLConnection createConnectionWithSuccessfulHeaders() throws IOException {
 URL url = new URL(TEST_URL);
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setInstanceFollowRedirects(false);
 connection.setDoOutput(false);
 connection.setConnectTimeout(1000);
 connection.setReadTimeout(1000);
 connection.setRequestProperty("Content-Type", TEST_HEADER_VALUE);
 connection.setRequestProperty("Accept-Encoding", "identity");
 connection.disconnect();
 return connection;
 }

}