package org.scribe.model.test;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connection;

 @Before
 public void setup() throws IOException {
 connection = (HttpURLConnection) new URL("http://example.com").openConnection();
 response = new Response(connection);
 }

 @Test
 public void testResponseConstructorWithSuccessfulConnection() {
 // Basic test case for constructor with successful connection
 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test(expected = UnknownHostException.class)
 public void testResponseConstructorWithUnknownHostException() {
 // Test case for constructor with UnknownHostException
 connection.disconnect();
 connection.setInstanceFollowRedirects(false);
 connection.setDoInput(true);
 connection.setRequestMethod("GET");
 connection.setDoOutput(false);
 connection.connect();
 connection.disconnect();
 connection.setInstanceFollowRedirects(true);
 connection.setDoInput(false);
 connection.connect();
 response = new Response(connection);
 }

 @Test
 public void testParseBodyContents() {
 // Test case for parseBodyContents method
 String expectedBody = "Hello World";
 connection.setDoOutput(true);
 connection.setRequestProperty("Content-Type", "text/plain");
 OutputStream os = connection.getOutputStream();
 os.write(expectedBody.getBytes());
 os.close();
 response = new Response(connection);
 String actualBody = response.getBody();
 assertEquals(expectedBody, actualBody);
 }

 @Test
 public void testParseHeaders() {
 // Test case for parseHeaders method
 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put("Content-Type", "text/plain");
 expectedHeaders.put("Cache-Control", "max-age=0");
 connection.setDoOutput(true);
 connection.setRequestProperty("Content-Type", "text/plain");
 connection.setRequestProperty("Cache-Control", "max-age=0");
 connection.disconnect();
 response = new Response(connection);
 Map<String, String> actualHeaders = response.getHeaders();
 assertEquals(expectedHeaders, actualHeaders);
 }

 @Test
 public void testWasSuccessful() {
 // Test case for wasSuccessful method
 response = new Response(connection);
 assertTrue(response.wasSuccessful());
 connection.setResponseCode(404);
 assertFalse(response.wasSuccessful());
 }

 @Test
 public void testGetBodyWithNullBody() {
 // Test case for getBody method with null body
 response.body = null;
 assertEquals(EMPTY, response.getBody());
 }

 @Test
 public void testGetBodyWithEmptyBody() {
 // Test case for getBody method with empty body
 response.body = EMPTY;
 assertEquals(EMPTY, response.getBody());
 }

 @Test
 public void testGetStreamWithNullStream() {
 // Test case for getStream method with null stream
 response.stream = null;
 assertNull(response.getStream());
 }

 @Test
 public void testGetStreamWithEmptyInputStream() throws IOException {
 // Test case for getStream method with empty InputStream
 connection.setDoOutput(false);
 connection.connect();
 response = new Response(connection);
 ByteArrayInputStream expectedStream = new ByteArrayInputStream(new byte[0]);
 response.stream = expectedStream;
 InputStream actualStream = response.getStream();
 assertEquals(expectedStream, actualStream);
 }

 @Test
 public void testGetCodeWithCodeGreaterThan400() {
 // Test case for getCode method with code greater than 400
 connection.setResponseCode(500);
 assertEquals(500, response.getCode());
 }

 @Test
 public void testGetCodeWithCodeLessThan200() {
 // Test case for getCode method with code less than 200
 connection.setResponseCode(199);
 assertEquals(199, response.getCode());
 }

 @Test
 public void testGetHeadersWithNullHeaders() {
 // Test case for getHeaders method with null headers
 response.headers = null;
 assertNull(response.getHeaders());
 }

 @Test
 public void testGetHeaderWithNullHeaderName() {
 // Test case for getHeader method with null header name
 response.headers = new HashMap<>();
 assertNull(response.getHeader(null));
 }

 @Test
 public void testGetHeaderWithEmptyHeaderName() {
 // Test case for getHeader method with empty header name
 response.headers = new HashMap<>();
 assertNull(response.getHeader(EMPTY));
 }

 @Test
 public void testGetHeaderWithValidHeaderName() {
 // Test case for getHeader method with valid header name
 response.headers = new HashMap<>();
 response.headers.put("Content-Type", "text/plain");
 assertEquals("text/plain", response.getHeader("Content-Type"));
 }

 @Test
 public void testGetHeaderWithInvalidHeaderName() {
 // Test case for getHeader method with invalid header name
 response.headers = new HashMap<>();
 assertEquals(null, response.getHeader("Invalid-Header"));
 }
}