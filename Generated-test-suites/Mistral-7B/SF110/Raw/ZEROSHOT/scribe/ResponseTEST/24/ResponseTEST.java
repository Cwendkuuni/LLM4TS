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

 private static final String TEST_URL = "http://test.com";
 private static final String TEST_BODY = "Test body";
 private static final String TEST_HEADER_NAME = "Test-Header";
 private static final String TEST_HEADER_VALUE = "Test-Value";

 @Test
 public void testResponseWithSuccessfulCode() throws IOException {
 Response response = new Response(createSuccessfulConnection(TEST_URL));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader(TEST_HEADER_NAME));
 }

 @Test
 public void testResponseWithErrorCode() throws IOException {
 Response response = new Response(createErrorConnection(TEST_URL, 404));

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 Response response = new Response(createEmptyConnection(TEST_URL));

 assertEquals(200, response.getCode());
 assertNull(response.getBody());
 assertNotNull(response.getStream());
 }

 @Test
 public void testResponseWithCustomBody() throws IOException {
 Response response = new Response(createCustomConnection(TEST_URL, TEST_BODY));

 assertEquals(200, response.getCode());
 assertEquals(TEST_BODY, response.getBody());
 assertNotNull(response.getStream());
 }

 @Test
 public void testResponseWithCustomHeaders() throws IOException {
 Response response = new Response(createCustomHeadersConnection(TEST_URL));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertEq("Test-Value", response.getHeader(TEST_HEADER_NAME));
 }

 @Test(expected = UnknownHostException.class)
 public void testResponseWithUnknownHost() throws IOException {
 Response response = new Response(createUnknownHostConnection("http://unknown.com"));

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 }

 private HttpURLConnection createSuccessfulConnection(String urlString) throws IOException {
 URL url = new URL(urlString);
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setDoOutput(false);
 connection.connect();
 return connection;
 }

 private HttpURLConnection createErrorConnection(String urlString, int errorCode) throws IOException {
 URL url = new URL(urlString);
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setDoOutput(false);
 connection.setInstanceFollowRedirects(false);
 connection.setErrorStream(System.err);
 connection.disconnect();
 connection.setResponseCode(errorCode);
 return connection;
 }

 private HttpURLConnection createEmptyConnection(String urlString) throws IOException {
 return createSuccessfulConnection(urlString);
 }

 private HttpURLConnection createCustomConnection(String urlString, String body) throws IOException {
 HttpURLConnection connection = createSuccessfulConnection(urlString);
 OutputStream os = connection.getOutputStream();
 os.write(body.getBytes("UTF-8"));
 os.flush();
 os.close();
 return connection;
 }

 private HttpURLConnection createCustomHeadersConnection(String urlString) throws IOException {
 HttpURLConnection connection = createSuccessfulConnection(urlString);
 connection.setRequestProperty(TEST_HEADER_NAME, TEST_HEADER_VALUE);
 return connection;
 }

}