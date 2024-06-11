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

 private static final String EMPTY = "";
 private static final String TEST_HEADER_NAME = "Test-Header-Name";
 private static final String TEST_HEADER_VALUE = "Test-Header-Value";
 private static final int TEST_RESPONSE_CODE = 200;
 private static final String TEST_BODY = "Test Body";

 @Test
 public void testResponseWithSuccessfulCodeAndBody() throws IOException {
 Response response = new Response(createMockConnection(TEST_RESPONSE_CODE, TEST_BODY.getBytes()));

 assertEquals(TEST_RESPONSE_CODE, response.getCode());
 assertEquals(TEST_BODY, response.getBody());
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 Response response = new Response(createMockConnection(TEST_RESPONSE_CODE, EMPTY.getBytes()));

 assertEquals(TEST_RESPONSE_CODE, response.getCode());
 assertNull(response.getBody());
 }

 @Test
 public void testResponseWithErrorStream() throws IOException {
 Response response = new Response(createMockConnection(404, null));

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 }

 @Test
 public void testResponseWithHeaders() throws IOException {
 Response response = new Response(createMockConnectionWithHeaders(TEST_RESPONSE_CODE, TEST_BODY.getBytes(), TEST_HEADER_NAME, TEST_HEADER_VALUE));

 assertEquals(TEST_RESPONSE_CODE, response.getCode());
 assertEquals(TEST_BODY, response.getBody());
 assertEquals(TEST_HEADER_VALUE, response.getHeader(TEST_HEADER_NAME));
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testParseHeadersWithInvalidEncoding() {
 Response response = new Response(createMockConnectionWithHeaders(TEST_RESPONSE_CODE, TEST_BODY.getBytes(), TEST_HEADER_NAME, "Invalid-Encoding"));
 }

 @Test
 public void testParseHeadersWithoutHeaders() throws IOException {
 Response response = new Response(createMockConnection(TEST_RESPONSE_CODE, TEST_BODY.getBytes()));

 assertNull(response.getHeaders());
 }

 @Test
 public void testWasSuccessfulWithValidStatusCode() {
 Response response = new Response(createMockConnection(200, null));

 assertTrue(response.wasSuccessful());
 }

 @Test
 public void testWasSuccessfulWithInvalidStatusCode() {
 Response response = new Response(createMockConnection(401, null));

 assertFalse(response.wasSuccessful());
 }

 private HttpURLConnection createMockConnection(int responseCode, byte[] body) throws IOException {
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenReturn(responseCode);
 when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(body));
 return connection;
 }

 private HttpURLConnection createMockConnectionWithHeaders(int responseCode, byte[] body, String headerName, String headerValue) throws IOException {
 HttpURLConnection connection = createMockConnection(responseCode, body);
 Map<String, List<String>> headers = mock(Map.class);
 List<String> list = Arrays.asList(headerValue);
 when(connection.getHeaderFields()).thenReturn(headers);
 when(headers.get(headerName)).thenReturn(list);
 return connection;
 }
}