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
 private static final String TEST_HEADER_KEY = "Test-Header-Key";
 private static final String TEST_HEADER_VALUE = "Test-Header-Value";

 @Test
 public void testResponseWithSuccessfulCode() throws Exception {
 Response response = new Response(createMockConnection(200));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader(TEST_HEADER_KEY));
 }

 @Test
 public void testResponseWithErrorCode() throws Exception {
 Response response = new Response(createMockConnection(404));

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader(TEST_HEADER_KEY));
 }

 @Test
 public void testResponseWithEmptyBody() throws Exception {
 Response response = new Response(createMockConnection(204));

 assertEquals(204, response.getCode());
 assertNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader(TEST_HEADER_KEY));
 }

 @Test
 public void testResponseWithNonEmptyBody() throws Exception {
 String testBody = "Test body content";
 Response response = new Response(createMockConnection(200, testBody.getBytes()));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader(TEST_HEADER_KEY));
 assertEquals(testBody, response.getBody());
 }

 @Test
 public void testResponseWithHeaders() throws Exception {
 Response response = new Response(createMockConnectionWithHeaders(200, TEST_HEADER_KEY, TEST_HEADER_VALUE));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertEquals(TEST_HEADER_VALUE, response.getHeader(TEST_HEADER_KEY));
 }

 @Test(expected = UnknownHostException.class)
 public void testResponseWithUnknownHost() throws Exception {
 Response response = new Response(createMockConnectionWithUnknownHost());

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader(TEST_HEADER_KEY));
 }

 private HttpURLConnection createMockConnection(int code) throws IOException {
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenReturn(code);
 return connection;
 }

 private HttpURLConnection createMockConnection(int code, byte[] body) throws IOException {
 HttpURLConnection connection = createMockConnection(code);
 when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(body));
 return connection;
 }

 private HttpURLConnection createMockConnectionWithHeaders(int code, String headerKey, String headerValue) throws IOException {
 HttpURLConnection connection = createMockConnection(code);
 Map<String, List<String>> headerFields = new HashMap<>();
 List<String> headerValues = new ArrayList<>();
 headerValues.add(headerValue);
 headerFields.put(headerKey, headerValues);
 when(connection.getHeaderFields()).thenReturn(headerFields);
 return connection;
 }

 private HttpURLConnection createMockConnectionWithUnknownHost() throws IOException {
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenThrow(new UnknownHostException());
 return connection;
 }

}