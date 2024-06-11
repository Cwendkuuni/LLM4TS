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
 private static final String TEST_BODY = "This is a test response body.";
 private static final int TEST_SUCCESSFUL_CODE = 200;
 private static final int TEST_ERROR_CODE = 404;
 private static final String TEST_HEADER_KEY = "Content-Type";
 private static final String TEST_HEADER_VALUE = "application/json";

 @Test
 public void testResponseWithSuccessfulStatusCode() throws IOException {
 Response response = new Response(createConnectionWithSuccessfulStatusCode(TEST_URL));

 assertEquals("Expected successful status code", TEST_SUCCESSFUL_CODE, response.getCode());
 assertNotNull("Expected response body", response.getBody());
 assertNotNull("Expected response stream", response.getStream());
 assertEquals("Expected empty map for headers when successful", Collections.emptyMap(), response.getHeaders());

 byte[] expectedBodyBytes = TEST_BODY.getBytes();
 ByteArrayInputStream expectedBodyStream = new ByteArrayInputStream(expectedBodyBytes);
 assertEquals("Expected response body to match expected", TEST_BODY, response.getBody());
 assertSame("Expected response stream to match expected", expectedBodyStream, response.getStream());
 }

 @Test
 public void testResponseWithErrorStatusCode() throws IOException {
 Response response = new Response(createConnectionWithErrorStatusCode(TEST_URL));

 assertEquals("Expected error status code", TEST_ERROR_CODE, response.getCode());
 assertNull("Expected empty response body for error", response.getBody());
 assertNull("Expected null response stream for error", response.getStream());

 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put(TEST_HEADER_KEY, TEST_HEADER_VALUE);
 assertEquals("Expected headers to match expected", expectedHeaders, response.getHeaders());
 }

 @Test
 public void testGetHeader() throws IOException {
 Response response = new Response(createConnectionWithSuccessfulStatusCode(TEST_URL));
 String expectedHeaderValue = TEST_HEADER_VALUE;

 assertEquals("Expected header value to match expected", expectedHeaderValue, response.getHeader(TEST_HEADER_KEY));
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetHeaderWithUnknownEncoding() throws IOException {
 Response response = new Response(createConnectionWithSuccessfulStatusCode(TEST_URL));
 String unknownHeaderKey = "X-Unknown-Encoding";

 response.getHeader(unknownHeaderKey);
 }

 @Test
 public void testParseHeaders() throws IOException {
 HttpURLConnection conn = createConnectionWithHeaders(TEST_URL);
 Response response = new Response(conn);

 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put(TEST_HEADER_KEY, TEST_HEADER_VALUE);
 assertEquals("Expected parsed headers to match expected", expectedHeaders, response.getHeaders());
 }

 private HttpURLConnection createConnectionWithSuccessfulStatusCode(String urlString) throws IOException {
 URL url = new URL(urlString);
 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
 conn.setRequestMethod("GET");
 conn.setDoInput(true);
 conn.setInstanceFollowRedirects(false);
 conn.setRequestProperty("Accept-Charset", "UTF-8");
 conn.setRequestProperty("User-Agent", "Mozilla/5.0");
 conn.disconnect();
 return conn;
 }

 private HttpURLConnection createConnectionWithErrorStatusCode(String urlString) throws IOException {
 URL url = new URL(urlString);
 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
 conn.setRequestMethod("GET");
 conn.setDoInput(true);
 conn.setInstanceFollowRedirects(false);
 conn.setRequestProperty("Accept-Charset", "UTF-8");
 conn.setRequestProperty("User-Agent", "Mozilla/5.0");
 conn.disconnect();
 return new ErrorResponse(TEST_ERROR_CODE, TEST_BODY.getBytes());
 }

 private HttpURLConnection createConnectionWithHeaders(String urlString) throws IOException {
 URL url = new URL(urlString);
 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
 conn.setRequestMethod("GET");
 conn.setDoInput(true);
 conn.setInstanceFollowRedirects(false);
 conn.addRequestProperty("Content-Type", TEST_HEADER_VALUE);
 conn.disconnect();
 return conn;
 }

 private static class ErrorResponse extends HttpURLConnection {
 private final int statusCode;
 private final byte[] body;

 public ErrorResponse(int statusCode, byte[] body) {
 this.statusCode = statusCode;
 this.body = body;
 }

 @Override
 public int getResponseCode() {
 return statusCode;
 }

 @Override
 public InputStream getInputStream() throws IOException {
 return new ByteArrayInputStream(body);
 }
 }
}