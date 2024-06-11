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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com/api?param1=value1&param2=value2");
 }

 @Test
 public void testRequestWithDefaults() {
 // test default values
 assertEquals(Verb.GET, request.getVerb());
 assertEquals("http://example.com/api?param1=value1&param2=value2", request.getUrl());
 assertNull(request.getPayload());
 assertNull(request.getBytePayload());
 assertFalse(request.isConnectionKeepAlive());
 assertNull(request.getCharset());
 assertEmpty(request.getHeaders());
 assertEmpty(request.getBodyParams());
 assertEmpty(request.getQuerystringParams());
 }

 @Test
 public void testAddHeaders() {
 request.addHeader("Content-Type", "application/json");
 Map<String, String> headers = request.getHeaders();
 assertEquals("application/json", headers.get("Content-Type"));
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("key", "value");
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals("value", bodyParams.get("key"));
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("key", "value");
 Map<String, String> queryStringParams = request.getQueryStringParams();
 assertEquals("value", queryStringParams.get("key"));
 }

 @Test
 public void testAddPayload() {
 request.addPayload("payload");
 String payload = request.getBodyContents();
 assertEquals("payload", payload);
 }

 @Test
 public void testAddPayloadByteArray() {
 byte[] payload = "payload".getBytes(Charset.forName("UTF-8"));
 request.addPayload(payload);
 byte[] bytePayload = request.getByteBodyContents();
 assertArrayEquals(payload, bytePayload);
 }

 @Test
 public void testSendRequest() throws IOException, OAuthException {
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
 request.setConnection(connection);
 Request requestWithPayload = new Request(Verb.POST, "http://example.com");
 requestWithPayload.addPayload("payload");
 Response response = requestWithPayload.send();
 assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
 }

 @Test(expected = OAuthException.class)
 public void testSendRequestWithIOException() throws IOException, OAuthException {
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenThrow(new IOException());
 request.setConnection(connection);
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testSendRequestWithMalformedURL() throws IOException, OAuthException {
 request.setUrl("http://example.com:/api?param1=value1&param2=value2");
 try {
 request.send();
 fail("Should have thrown MalformedURLException");
 } catch (OAuthException oae) {
 // expected
 }
 }
}