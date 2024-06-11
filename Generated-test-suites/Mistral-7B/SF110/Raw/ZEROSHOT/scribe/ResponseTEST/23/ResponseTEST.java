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

 private static final String EMPTY = "";
 private static final String TEST_CONTENT_TYPE = "text/plain";
 private static final String TEST_HEADER_KEY = "Content-Type";
 private static final String TEST_HEADER_VALUE = TEST_CONTENT_TYPE;
 private static final int TEST_RESPONSE_CODE = 200;
 private static final String TEST_BODY = "Test body";

 @Test
 public void testResponseWithSuccessfulConnection() throws IOException {
 Response response = new Response(createMockConnection(TEST_RESPONSE_CODE, TEST_HEADER_KEY, TEST_HEADER_VALUE, TEST_BODY));

 assertEquals(TEST_RESPONSE_CODE, response.getCode());
 assertEquals(TEST_BODY, response.getBody());
 assertEquals(TEST_HEADER_VALUE, response.getHeader(TEST_HEADER_KEY));
 assertNotNull(response.getStream());
 }

 @Test
 public void testResponseWithUnsuccessfulConnection() {
 Response response = new Response(createMockConnection(404, EMPTY, EMPTY, EMPTY));

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 }

 @Test
 public void testResponseWithNullConnection() {
 Response response = new Response(null);

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 }

 @Test
 public void testParseHeaders() throws IOException {
 HttpURLConnection connection = createMockConnection(200, TEST_HEADER_KEY, TEST_HEADER_VALUE, TEST_BODY);
 Response response = new Response(connection);

 Map<String, String> headers = new HashMap<String, String>();
 headers.put(TEST_HEADER_KEY, TEST_HEADER_VALUE);

 assertEquals(headers, response.getHeaders());
 }

 @Test
 public void testWasSuccessful() throws IOException {
 Response response1 = new Response(createMockConnection(200, TEST_HEADER_KEY, TEST_HEADER_VALUE, TEST_BODY));
 Response response2 = new Response(createMockConnection(401, TEST_HEADER_KEY, TEST_HEADER_VALUE, TEST_BODY));

 assertTrue(response1.wasSuccessful());
 assertFalse(response2.wasSuccessful());
 }

 @Test
 public void testParseBodyContents() throws IOException {
 Response response = new Response(createMockConnection(200, TEST_HEADER_KEY, TEST_HEADER_VALUE, null));

 assertEquals(TEST_BODY, response.parseBodyContents());
 }

 private HttpURLConnection createMockConnection(int code, String headerKey, String headerValue, String body) throws IOException {
 URL url = new URL("http://test.com");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setDoInput(true);
 connection.setRequestMethod("GET");
 connection.setDoOutput(false);
 connection.setInstanceFollowRedirects(false);
 connection.setRequestProperty("Accept-Charset", "UTF-8");
 connection.setRequestProperty("User-Agent", "Test/1.0");
 connection.setRequestProperty("Host", "test.com");
 connection.disconnect();

 if (body != null) {
 connection.setRequestProperty("Content-Length", Integer.toString(body.length()));
 connection.setFixedLengthStreamingMode(body.length());
 connection.setOutputStream(new ByteArrayOutputStream());
 connection.getOutputStream().write(body.getBytes());
 }

 connection.setResponseCode(code);
 connection.setRequestProperty("Content-Type", TEST_CONTENT_TYPE);
 connection.setHeaderFields(new HashMap<String, List<String>>() {{
 put(headerKey, Collections.singletonList(headerValue));
 }});

 return connection;
 }

}