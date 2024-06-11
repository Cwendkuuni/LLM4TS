package org.scribe.model;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ResponseTest {

 private static final String TEST_RESPONSE_URL = "http://test.com";
 private static final String TEST_HEADER_KEY = "Content-Type";
 private static final String TEST_HEADER_VALUE = "application/json";
 private static final String TEST_BODY = "{\"name\":\"Test\"}";

 @Test
 public void testResponseWithValidUrl() throws IOException {
 Response response = new Response(new URL(TEST_RESPONSE_URL));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNotNull(response.getHeader(TEST_HEADER_KEY));
 assertEquals(TEST_HEADER_VALUE, response.getHeader(TEST_HEADER_KEY));

 byte[] bodyBytes = TEST_BODY.getBytes();
 InputStream inputStream = new ByteArrayInputStream(bodyBytes);
 InputStream expectedStream = response.getStream();

 byte[] actualBytes = StreamUtils.getStreamContents(expectedStream);
 assertArrayEquals(bodyBytes, actualBytes);
 }

 @Test
 public void testResponseWithInvalidUrl() throws IOException {
 Response response = new Response(new URL("http://nonexistent.com"));

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader(TEST_HEADER_KEY));
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 Response response = new Response(new URL(TEST_RESPONSE_URL));
 response.parseBodyContents();

 assertNotNull(response.getBody());
 assertEquals(EMPTY, response.getBody());
 }

 @Test
 public void testResponseWithNonSuccessStatusCode() throws IOException {
 Response response = new Response(new URL(TEST_RESPONSE_URL + "/error"));

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader(TEST_HEADER_KEY));
 }

 @Test
 public void testResponseWithEmptyHeaders() throws IOException {
 Response response = new Response(new URL(TEST_RESPONSE_URL));

 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader(TEST_HEADER_KEY));
 }

 @Test
 public void testResponseWithHeaders() throws IOException {
 Response response = new Response(new URL(TEST_RESPONSE_URL + "?testHeader=" + TEST_HEADER_VALUE));

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNotNull(response.getHeader(TEST_HEADER_KEY));
 assertEquals(TEST_HEADER_VALUE, response.getHeader(TEST_HEADER_KEY));
 }
}