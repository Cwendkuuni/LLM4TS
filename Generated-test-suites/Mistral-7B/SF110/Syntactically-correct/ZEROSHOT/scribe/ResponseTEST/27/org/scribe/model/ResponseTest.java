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
 private static final int TEST_RESPONSE_CODE = 200;
 private static final String TEST_BODY = "This is a test body";
 private static final String TEST_HEADER_NAME = "Content-Type";
 private static final String TEST_HEADER_VALUE = "text/plain";

 @Test
 public void testResponseConstructor() throws IOException {
 Response response = new Response(createConnection(TEST_URL, TEST_RESPONSE_CODE));
 assertEquals(TEST_RESPONSE_CODE, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testResponseParseBodyContents() throws IOException {
 Response response = new Response(createConnection(TEST_URL, TEST_RESPONSE_CODE));
 response.parseBodyContents();
 assertEquals(TEST_BODY, response.getBody());
 }

 @Test
 public void testResponseParseHeaders() {
 Response response = new Response(createConnection(TEST_URL, TEST_RESPONSE_CODE));
 Map<String, String> headers = response.getHeaders();
 assertEquals(TEST_HEADER_VALUE, headers.get(TEST_HEADER_NAME));
 }

 @Test
 public void testResponseWasSuccessful() throws IOException {
 Response response = new Response(createConnection(TEST_URL, TEST_RESPONSE_CODE));
 assertTrue(response.wasSuccessful());

 Response responseError = new Response(createConnection(TEST_URL, 404));
 assertFalse(responseError.wasSuccessful());
 }

 @Test
 public void testResponseGetBodyNull() throws IOException {
 Response response = new Response(createConnection(TEST_URL, TEST_RESPONSE_CODE));
 response.body = null;
 assertNull(response.getBody());
 }

 @Test
 public void testResponseGetStreamNull() throws IOException {
 Response response = new Response(createConnection(TEST_URL, TEST_RESPONSE_CODE));
 response.stream = null;
 assertNull(response.getStream());
 }

 @Test
 public void testResponseGetCodeNegative() throws IOException {
 Response response = new Response(createConnection(TEST_URL, -1));
 assertEquals(404, response.getCode()); // Default error code if connection fails
 }

 @Test
 public void testResponseGetCodeZero() throws IOException {
 Response response = new Response(createConnection(TEST_URL, 0));
 assertEquals(404, response.getCode()); // Default error code if connection fails
 }

 @Test
 public void testResponseGetHeadersEmpty() throws IOException {
 Response response = new Response(createConnection(TEST_URL, TEST_RESPONSE_CODE));
 Map<String, String> headers = response.getHeaders();
 assertEquals(0, headers.size());
 }

 @Test
 public void testResponseGetHeaderNull() throws IOException {
 Response response = new Response(createConnection(TEST_URL, TEST_RESPONSE_CODE));
 assertNull(response.getHeader(null));
 }

 @Test
 public void testResponseGetHeaderEmpty() throws IOException {
 Response response = new Response(createConnection(TEST_URL, TEST_RESPONSE_CODE));
 Map<String, String> headers = response.getHeaders();
 assertNull(headers.get(TEST_HEADER_NAME));
 }

 @Test
 public void testResponseGetHeaderWrongName() throws IOException {
 Response response = new Response(createConnection(TEST_URL, TEST_RESPONSE_CODE));
 Map<String, String> headers = response.getHeaders();
 assertNull(headers.get("WrongName"));
 }

 @Test
 public void testResponseGetHeaderWrongValue() throws IOException {
 Response response = new Response(createConnection(TEST_URL, TEST_RESPONSE_CODE));
 Map<String, String> headers = response.getHeaders();
 assertNotEquals(TEST_HEADER_VALUE, headers.get(TEST_HEADER_NAME));
 headers.put(TEST_HEADER_NAME, TEST_HEADER_VALUE);
 assertEquals(TEST_HEADER_VALUE, response.getHeader(TEST_HEADER_NAME));
 }

 private HttpURLConnection createConnection(String urlString, int responseCode) throws IOException {
 URL url = new URL(urlString);
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setRequestMethod("GET");
 connection.setConnectTimeout(5000);
 connection.setReadTimeout(5000);
 connection.setInstanceFollowRedirects(false);
 connection.setDoOutput(false);
 connection.setRequestProperty("Accept", "*/*");
 connection.setRequestProperty("User-Agent", "Test/1.0");
 connection.setResponseCode(responseCode);
 return connection;
 }

 @Test
 public void testResponseStreamContents() throws IOException, UnsupportedEncodingException {
 Response response = new Response(createConnection(TEST_URL, TEST_RESPONSE_CODE));
 String expectedBody = TEST_BODY;
 String actualBody = StreamUtils.getStreamContents(response.getStream());
 assertEquals(expectedBody, actualBody);
 }

 @Test
 public void testResponseHeadersSize() throws IOException {
 Response response = new Response(createConnection(TEST_URL, TEST_RESPONSE_CODE));
 Map<String, String> headers = response.getHeaders();
 int expectedSize = 1;
 int actualSize = headers.size();
 assertEquals(expectedSize, actualSize);
 }

}