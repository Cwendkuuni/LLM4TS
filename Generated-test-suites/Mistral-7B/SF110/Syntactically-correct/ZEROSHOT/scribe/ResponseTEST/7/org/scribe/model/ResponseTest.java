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
 private static final String TEST_BODY = "Test body";
 private static final String TEST_HEADER_NAME = "Test-Header";
 private static final String TEST_HEADER_VALUE = "Test-Value";

 @Test
 public void testResponseConstruction() throws IOException {
 Response response = new Response(createConnection(TEST_URL, TEST_RESPONSE_CODE));
 assertEquals(TEST_RESPONSE_CODE, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertEquals(0, response.getHeaders().size());
 }

 @Test
 public void testResponseBodyParsing() throws IOException {
 Response response = new Response(createConnection(TEST_URL, TEST_RESPONSE_CODE));
 response.parseBodyContents();
 assertEquals(TEST_BODY, response.getBody());
 }

 @Test
 public void testResponseHeadersParsing() throws IOException {
 Response response = new Response(createConnectionWithHeaders(TEST_URL, TEST_RESPONSE_CODE, TEST_HEADER_NAME, TEST_HEADER_VALUE));
 Map<String, String> headers = response.getHeaders();
 assertEquals(1, headers.size());
 assertEquals(TEST_HEADER_VALUE, headers.get(TEST_HEADER_NAME));
 }

 @Test
 public void testResponseWasSuccessful() throws IOException {
 Response response = new Response(createConnection(TEST_URL, TEST_RESPONSE_CODE));
 assertTrue(response.wasSuccessful());
 }

 @Test
 public void testResponseWasNotSuccessful() throws IOException {
 Response response = new Response(createConnection(TEST_URL, 404));
 assertFalse(response.wasSuccessful());
 }

 @Test(expected = UnknownHostException.class)
 public void testResponseUnknownHostException() throws IOException {
 Response response = new Response(createConnectionWithException(TEST_URL));
 assertEquals(404, response.getCode());
 assertEquals(EMPTY, response.getBody());
 }

 @Test
 public void testGetStreamFromSuccessfulConnection() throws IOException {
 Response response = new Response(createConnection(TEST_URL, TEST_RESPONSE_CODE));
 byte[] testData = TEST_BODY.getBytes();
 ByteArrayInputStream expectedStream = new ByteArrayInputStream(testData);
 InputStream actualStream = response.getStream();
 byte[] testDataRead = new byte[testData.length];
 int actualBytesRead = actualStream.read(testDataRead, 0, testData.length);
 assertEquals(testData.length, actualBytesRead);
 assertArrayEquals(testData, testDataRead);
 }

 @Test
 public void testGetStreamFromErrorConnection() throws IOException {
 Response response = new Response(createConnectionWithError(TEST_URL));
 InputStream actualStream = response.getStream();
 assertNull(actualStream);
 }

 private HttpURLConnection createConnection(String urlString, int responseCode) throws IOException {
 HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
 connection.setRequestMethod("GET");
 connection.setDoOutput(false);
 connection.setDoInput(true);
 connection.disconnect();
 connection.setResponseCode(responseCode);
 return connection;
 }

 private HttpURLConnection createConnectionWithHeaders(String urlString, int responseCode, String headerName, String headerValue) throws IOException {
 HttpURLConnection connection = createConnection(urlString, responseCode);
 connection.setRequestProperty(headerName, headerValue);
 return connection;
 }

 private HttpURLConnection createConnectionWithException(String urlString) throws IOException {
 throw new UnknownHostException("Unknown host " + urlString);
 }

 private HttpURLConnection createConnectionWithError(String urlString) throws IOException {
 HttpURLConnection connection = createConnection(urlString, 500);
 connection.disconnect();
 return connection;
 }

}