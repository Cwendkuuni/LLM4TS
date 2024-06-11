package org.scribe.model.test;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connection;

 @Before
 public void setUp() throws IOException {
 connection = createMockConnection(200, "{'success': true}".getBytes());
 response = new Response(connection);
 }

 @Test
 public void testSuccessfulResponse() {
 // Test for successful response with body
 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());

 // Test for successful response without body
 connection = createMockConnection(204, null);
 response = new Response(connection);
 assertEquals(204, response.getCode());
 assertNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testErrorResponse() {
 connection = createMockConnection(404, null);
 response = new Response(connection);
 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testParseHeaders() {
 Map<String, String> headers = response.getHeaders();
 assertTrue(headers.containsKey("Content-Type"));
 assertEquals("application/json", headers.get("Content-Type"));
 }

 @Test
 public void testGetHeader() {
 response.setHeaders(new HashMap<String, String>() {{
 put("Content-Type", "application/xml");
 put("Content-Length", "100");
 }} );
 assertEquals("application/xml", response.getHeader("Content-Type"));
 assertEquals("100", response.getHeader("Content-Length"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullConnection() {
 new Response(null);
 }

 private HttpURLConnection createMockConnection(int statusCode, byte[] body) throws IOException {
 URL url = new URL("http://test.com");
 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
 conn.setDoInput(true);
 conn.setDoOutput(false);
 conn.setRequestMethod("GET");
 conn.setInstanceFollowRedirects(false);
 conn.setRequestProperty("Accept", "application/json");
 conn.setRequestProperty("User-Agent", "Test-Client/1.0");
 conn.setResponseCode(statusCode);
 if (body != null) {
 conn.setFixedLengthStreamingMode(body.length);
 OutputStream stream = conn.getOutputStream();
 stream.write(body);
 stream.close();
 }
 return conn;
 }

 @After
 public void tearDown() throws Exception {
 connection.disconnect();
 }

}