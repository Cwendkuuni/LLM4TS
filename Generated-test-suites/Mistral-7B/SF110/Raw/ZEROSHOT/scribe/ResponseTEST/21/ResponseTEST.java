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

 private static final String TEST_RESPONSE_BODY = "Test Response Body";
 private static final int TEST_RESPONSE_CODE = 200;
 private static final String TEST_RESPONSE_HEADER_KEY = "Test-Header-Key";
 private static final String TEST_RESPONSE_HEADER_VALUE = "Test-Header-Value";

 @Test
 public void testResponseWithSuccessfulStatusCode() throws IOException {
 // Given
 URL url = new URL("http://example.com/test");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.disconnect();

 // When
 Response response = new Response(connection);

 // Then
 assertEquals(TEST_RESPONSE_CODE, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertEquals(TEST_RESPONSE_HEADER_VALUE, response.getHeader(TEST_RESPONSE_HEADER_KEY));
 assertTrue(response.wasSuccessful());
 }

 @Test
 public void testResponseWithErrorStatusCode() throws IOException {
 // Given
 URL url = new URL("http://example.com/error");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.disconnect();

 // When
 Response response = new Response(connection);

 // Then
 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader(TEST_RESPONSE_HEADER_KEY));
 assertFalse(response.wasSuccessful());
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 // Given
 URL url = new URL("http://example.com/empty");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.disconnect();

 // When
 Response response = new Response(connection);

 // Then
 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertEquals(EMPTY, response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader(TEST_RESPONSE_HEADER_KEY));
 assertTrue(response.wasSuccessful());
 }

 @Test
 public void testResponseWithCustomHeader() throws IOException {
 // Given
 URL url = new URL("http://example.com/custom-header");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestProperty(TEST_RESPONSE_HEADER_KEY, TEST_RESPONSE_HEADER_VALUE);
 connection.setRequestMethod("GET");
 connection.disconnect();

 // When
 Response response = new Response(connection);

 // Then
 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertEquals(TEST_RESPONSE_HEADER_VALUE, response.getHeader(TEST_RESPONSE_HEADER_KEY));
 assertTrue(response.wasSuccessful());
 }

 @Test
 public void testParseBodyContents() throws IOException {
 // Given
 ByteArrayInputStream testStream = new ByteArrayInputStream(TEST_RESPONSE_BODY.getBytes());
 Response response = new Response(null, testStream, 200, null, null);

 // When
 String body = response.parseBodyContents();

 // Then
 assertEquals(TEST_RESPONSE_BODY, body);
 }

 @Test
 public void testParseHeaders() {
 // Given
 Map<String, List<String>> headerFields = new HashMap<>();
 headerFields.put(TEST_RESPONSE_HEADER_KEY, new ArrayList<>(){ { add(TEST_RESPONSE_HEADER_VALUE); } });
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getHeaderFields()).thenReturn(headerFields);

 // When
 Response response = new Response(connection);

 // Then
 Map<String, String> headers = response.getHeaders();
 assertEquals(TEST_RESPONSE_HEADER_VALUE, headers.get(TEST_RESPONSE_HEADER_KEY));
 }
}