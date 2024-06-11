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

 private static final String TEST_URL = "http://test.com";
 private static final int TEST_CODE = 200;
 private static final String TEST_BODY = "Test body";
 private static final Map<String, String> TEST_HEADERS = new HashMap<String, String>() {{
 put("Content-Type", "application/json");
 put("Content-Length", "12");
 put("Cache-Control", "no-cache");
 }};

 @Test
 public void testResponseWithSuccessfulConnection() throws IOException {
 Response response = new Response(new HttpURLConnection(new URL(TEST_URL)) {
 @Override
 public int getResponseCode() {
 return TEST_CODE;
 }

 @Override
 public Map<String, List<String>> getHeaderFields() {
 return TEST_HEADERS;
 }
 });

 assertEquals(TEST_CODE, response.getCode());
 assertEquals(TEST_BODY, response.getBody());
 assertSame(response.getStream(), response.getInputStream());
 assertEquals(TEST_HEADERS, response.getHeaders());
 assertEquals(TEST_HEADERS.get("Content-Type"), response.getHeader("Content-Type"));
 }

 @Test
 public void testResponseWithErrorConnection() throws IOException {
 Response response = new Response(new HttpURLConnection(new URL(TEST_URL)) {
 @Override
 public int getResponseCode() {
 throw new IOException("Unexpected error");
 }
 });

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 Response response = new Response(new HttpURLConnection(new URL(TEST_URL)) {
 @Override
 public int getResponseCode() {
 return 204;
 }

 @Override
 public Map<String, List<String>> getHeaderFields() {
 return TEST_HEADERS;
 }
 });

 assertEquals(204, response.getCode());
 assertEquals(EMPTY, response.getBody());
 assertSame(response.getStream(), response.getInputStream());
 assertEquals(TEST_HEADERS, response.getHeaders());
 }

 @Test
 public void testResponseWithStream() throws IOException {
 ByteArrayInputStream expectedStream = new ByteArrayInputStream("Test stream".getBytes());
 Response response = new Response(new HttpURLConnection(new URL(TEST_URL)) {
 @Override
 public int getResponseCode() {
 return 200;
 }

 @Override
 public Map<String, List<String>> getHeaderFields() {
 return TEST_HEADERS;
 }

 @Override
 public InputStream getInputStream() {
 return expectedStream;
 }
 });

 assertEquals(TEST_CODE, response.getCode());
 assertEquals("Test stream", response.getBody());
 assertSame(expectedStream, response.getStream());
 assertEquals(TEST_HEADERS, response.getHeaders());
 }
}