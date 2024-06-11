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
 private static final String TEST_HEADERS = "Content-Type: application/json;charset=UTF-8";
 private static final String TEST_BODY = "{\"status\":\"success\", \"data\":[1,2,3]}";

 @Test
 public void testResponseWithSuccessfulStatus() throws IOException {
 Response response = new Response(createSuccessfulConnection(TEST_URL));

 assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getHeaders());
 assertEquals(TEST_HEADERS, response.getHeaders().get("Content-Type"));
 assertNotNull(response.getStream());
 }

 @Test
 public void testResponseWithErrorStatus() throws IOException {
 Response response = new Response(createErrorConnection(TEST_URL));

 assertEquals(HttpURLConnection.HTTP_NOT_FOUND, response.getCode());
 assertNull(response.getBody());
 assertNotNull(response.getHeaders());
 assertEquals(EMPTY, response.getHeaders().get("Content-Type"));
 assertNull(response.getStream());
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 Response response = new Response(createSuccessfulConnection(TEST_URL + "/empty"));

 assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
 assertNotNull(response.getBody());
 assertEquals(EMPTY, response.getBody());
 assertNotNull(response.getHeaders());
 assertEquals(TEST_HEADERS, response.getHeaders().get("Content-Type"));
 assertNotNull(response.getStream());
 }

 @Test
 public void testResponseWithNonEmptyBody() throws IOException {
 Response response = new Response(createSuccessfulConnection(TEST_URL + "/data"));

 assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
 assertNotNull(response.getBody());
 assertEquals(TEST_BODY, response.getBody());
 assertNotNull(response.getHeaders());
 assertEquals(TEST_HEADERS, response.getHeaders().get("Content-Type"));
 assertNotNull(response.getStream());
 }

 @Test
 public void testResponseWithCustomHeaders() throws IOException {
 Response response = new Response(createSuccessfulConnection(TEST_URL + "/custom-headers"));

 assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getHeaders());
 assertEquals("application/xml", response.getHeaders().get("Content-Type"));
 assertNotNull(response.getStream());
 }

 @Test
 public void testResponseWithNullHeaders() throws IOException {
 Response response = new Response(createSuccessfulConnection(TEST_URL + "/null-headers"));

 assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getHeaders());
 assertEquals(EMPTY, response.getHeaders().get("Content-Type"));
 assertNotNull(response.getStream());
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testResponseWithUnsupportedEncoding() throws IOException {
 Response response = new Response(createSuccessfulConnection(TEST_URL + "/unsupported-encoding"));

 assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
 assertNotNull(response.getBody());
 fail("Unexpectedly did not throw UnsupportedEncodingException");
 }

 private HttpURLConnection createSuccessfulConnection(String urlString) throws IOException {
 URL url = new URL(urlString);
 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
 conn.setRequestMethod("GET");
 conn.setConnectTimeout(1000);
 conn.setReadTimeout(1000);
 conn.setDoOutput(false);
 conn.setDoInput(true);
 conn.connect();
 return conn;
 }

 private HttpURLConnection createErrorConnection(String urlString) throws IOException {
 URL url = new URL(urlString);
 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
 conn.setRequestMethod("GET");
 conn.setConnectTimeout(1000);
 conn.setReadTimeout(1000);
 conn.setDoOutput(false);
 conn.setDoInput(true);
 conn.connect();
 conn.disconnect();
 return conn;
 }

}