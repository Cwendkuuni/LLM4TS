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
 private static final String TEST_RESPONSE_BODY = "Test Response Body";
 private static final String TEST_HEADER_KEY = "Content-Type";
 private static final String TEST_HEADER_VALUE = "application/json";

 @Test
 public void testResponse() throws IOException {
 Response response = new Response(createMockHttpURLConnection(200, TEST_RESPONSE_BODY));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertEquals(TEST_RESPONSE_BODY, response.getBody());
 assertEquals(EMPTY, new String(response.getStream().readAllBytes()));

 Map<String, String> headers = response.getHeaders();
 assertEquals(1, headers.size());
 assertEquals(TEST_HEADER_VALUE, headers.get(TEST_HEADER_KEY));

 response.parseBodyContents();
 assertEquals(TEST_RESPONSE_BODY, response.getBody());
 }

 @Test
 public void testResponseWithErrorStream() throws IOException {
 Response response = new Response(createMockHttpURLConnection(500, EMPTY));

 assertEquals(500, response.getCode());
 assertNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertEquals(EMPTY, new String(response.getStream().readAllBytes()));
 assertEquals(EMPTY, response.getBody());
 }

 @Test(expected = UnknownHostException.class)
 public void testResponseWithUnknownHost() throws IOException {
 Response response = new Response(createMockHttpURLConnection(404, EMPTY));

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 }

 @Test
 public void testParseHeaders() throws IOException {
 Map<String, List<String>> headerFields = new HashMap<>();
 headerFields.put(TEST_HEADER_KEY, Collections.singletonList(TEST_HEADER_VALUE));

 HttpURLConnection conn = createMockHttpURLConnection(200, TEST_RESPONSE_BODY, headerFields);
 Response response = new Response(conn);

 Map<String, String> headers = response.getHeaders();
 assertEquals(1, headers.size());
 assertEquals(TEST_HEADER_VALUE, headers.get(TEST_HEADER_KEY));
 }

 @Test
 public void testWasSuccessful() throws IOException {
 Response response = new Response(createMockHttpURLConnection(201, TEST_RESPONSE_BODY));
 assertTrue(response.wasSuccessful());

 response = new Response(createMockHttpURLConnection(401, TEST_RESPONSE_BODY));
 assertFalse(response.wasSuccessful());
 }

 private HttpURLConnection createMockHttpURLConnection(int responseCode, String responseBody) throws IOException {
 return createMockHttpURLConnection(responseCode, responseBody, null);
 }

 private HttpURLConnection createMockHttpURLConnection(int responseCode, String responseBody, Map<String, List<String>> headerFields) throws IOException {
 URL url = new URL("http://test.com");
 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 connection.setResponseCode(responseCode);
 connection.setDoOutput(false);
 connection.setDoInput(true);
 if (headerFields != null) {
 connection.setRequestProperties(headerFields);
 }
 if (responseBody != null) {
 OutputStream os = connection.getOutputStream();
 os.write(responseBody.getBytes());
 os.close();
 }
 return connection;
 }

}