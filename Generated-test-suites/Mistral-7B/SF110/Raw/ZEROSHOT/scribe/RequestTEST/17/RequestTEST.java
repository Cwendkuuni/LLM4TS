package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testRequestWithNoBodyOrHeaders() {
 Request request = new Request(Verb.GET, "http://example.com");
 Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals("HTTP/1.1 200 OK", response.getStatusLine().getProtocolVersion() + " " + response.getStatusLine().getReasonPhrase());
 }

 @Test
 public void testRequestWithQueryStringParams() {
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 Map<String, String> params = request.getQueryStringParams();
 assertTrue(params.containsKey("param1"));
 assertEquals("value1", params.get("param1"));
 assertTrue(params.containsKey("param2"));
 assertEquals("value2", params.get("param2"));
 }

 @Test
 public void testRequestWithBodyParams() {
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 Map<String, String> bodyParams = request.getBodyParams();
 assertTrue(bodyParams.containsKey("param1"));
 assertEquals("value1", bodyParams.get("param1"));
 assertTrue(bodyParams.containsKey("param2"));
 assertEquals("value2", bodyParams.get("param2"));
 }

 @Test
 public void testRequestWithPayload() {
 request.addPayload("Hello World");
 Response response = request.send();
 assertEquals("Hello World", response.getBodyAsString());
 }

 @Test
 public void testRequestWithPayloadBytes() throws UnsupportedEncodingException, IOException {
 String payload = "Hello World";
 request.addPayload(payload.getBytes("UTF-8"));
 Response response = request.send();
 InputStream responseStream = response.getEntity().getContent();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] buffer = new byte[1024];
 int bytesRead = 0;
 while ((bytesRead = responseStream.read(buffer, 0, buffer.length)) != -1) {
 outputStream.write(buffer, 0, bytesRead);
 }
 outputStream.close();
 byte[] responseBytes = outputStream.toByteArray();
 assertArrayEquals(payload.getBytes("UTF-8"), responseBytes);
 }

 @Test
 public void testRequestWithHeaders() {
 request.addHeader("Authorization", "Bearer token123");
 request.addHeader("Content-Type", "application/json");
 Map<String, String> headers = request.getHeaders();
 assertTrue(headers.containsKey("Authorization"));
 assertEquals("Bearer token123", headers.get("Authorization"));
 assertTrue(headers.containsKey("Content-Type"));
 assertEquals("application/json", headers.get("Content-Type"));
 }

 @Test(expected = OAuthException.class)
 public void testRequestWithInvalidUrl() {
 request = new Request(Verb.GET, "http://example.com:");
 try {
 request.send();
 fail("Should have thrown an OAuthException");
 } catch (OAuthException e) {
 // Expected exception
 }
 }

 @Test(expected = OAuthException.class)
 public void testRequestWithInvalidVerb() {
 request = new Request(null, "http://example.com");
 try {
 request.send();
 fail("Should have thrown an OAuthException");
 } catch (OAuthException e) {
 // Expected exception
 }
 }

 @Test(expected = OAuthException.class)
 public void testRequestWithInvalidCharset() {
 request.setCharset("invalidCharset");
 try {
 request.send();
 fail("Should have thrown an OAuthException");
 } catch (OAuthException e) {
 // Expected exception
 }
 }
}