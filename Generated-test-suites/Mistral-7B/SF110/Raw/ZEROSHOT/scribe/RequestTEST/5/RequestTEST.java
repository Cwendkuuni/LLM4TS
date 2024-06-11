package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {

 private static final String BASE_URL = "http://example.com";
 private static final String QUERY_STRING = "param1=value1&param2=value2";
 private static final String PAYLOAD = "{\"key\":\"value\"}";
 private static final String CHARSET = "UTF-8";
 private static final Verb VERB_GET = Verb.GET;
 private static final Verb VERB_PUT = Verb.PUT;
 private static final Verb VERB_POST = Verb.POST;

 @Test
 public void testConstructor() {
 Request request = new Request(VERB_GET, BASE_URL);
 assertNull(request.getBody());
 assertNull(request.getByteBodyContents());
 assertFalse(request.isConnectionKeepAlive());
 assertEquals(VERB_GET, request.getVerb());
 assertEquals(BASE_URL, request.getUrl());
 assertEquals(new HashMap<String, String>(), request.getBodyParams());
 assertEquals(new HashMap<String, String>(), request.getHeaders());
 assertEquals(new HashMap<String, String>(), request.getQuerystringParams());
 }

 @Test
 public void testGetQueryStringParams() {
 Map<String, String> params = new HashMap<String, String>() {{
 put("param1", "value1");
 put("param2", "value2");
 }};

 Request request = new Request(VERB_GET, BASE_URL + "?" + QUERY_STRING);
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");

 Map<String, String> expectedParams = new HashMap<String, String>() {{
 put("param1", "value1");
 put("param2", "value2");
 putAll(params);
 }};

 Map<String, String> actualParams = request.getQueryStringParams();
 assertEquals(expectedParams, actualParams);
 }

 @Test
 public void testGetBodyParams() {
 Request request = new Request(VERB_POST, BASE_URL);
 request.addBodyParameter("key", "value");

 Map<String, String> expectedParams = new HashMap<String, String>() {{
 put("key", "value");
 }};

 Map<String, String> actualParams = request.getBodyParams();
 assertEquals(expectedParams, actualParams);
 }

 @Test
 public void testAddHeaders() throws IOException {
 Request request = new Request(VERB_GET, BASE_URL);
 request.addHeader("X-Custom-Header", "Custom Value");

 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
 put("X-Custom-Header", Collections.singletonList("Custom Value"));
 }});

 request.createConnection();
 request.addHeaders(connection);

 assertEquals("Custom Value", request.getHeaders().get("X-Custom-Header"));
 }

 @Test
 public void testAddBody() throws IOException {
 Request request = new Request(VERB_POST, BASE_URL);
 request.addPayload(PAYLOAD);

 HttpURLConnection connection = mock(HttpURLConnection.class);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(PAYLOAD.getBytes(CHARSET));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 when(connection.getOutputStream()).thenReturn(outputStream);
 when(connection.getInputStream()).thenReturn(inputStream);

 request.createConnection();
 request.addBody(connection, request.getByteBodyContents());

 ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
 byte[] actualBytes = request.getByteBodyContents();
 actualOutput.write(actualBytes, 0, actualBytes.length);
 String actualPayload = new String(actualOutput.toByteArray(), CHARSET);

 assertEquals(PAYLOAD, actualPayload);
 }

 @Test(expected = OAuthException.class)
 public void testSendWithInvalidCharset() throws IOException {
 Request request = new Request(VERB_GET, BASE_URL);
 request.setCharset("Invalid Charset");
 request.send();
 }

 @Test
 public void testSendWithTimeout() throws IOException, InterruptedException {
 Request request = new Request(VERB_GET, BASE_URL);
 URL url = new URL(request.getUrl());
 HttpURLConnection connection = (HttpURLConnection)url.openConnection();
 connection.setConnectTimeout(1, TimeUnit.SECONDS);
 connection.setReadTimeout(1, TimeUnit.SECONDS);
 request.setConnection(connection);
 request.send();
 assertFalse(request.isConnectionKeepAlive());
 }

 @Test
 public void testSendWithKeepAlive() throws IOException {
 Request request = new Request(VERB_GET, BASE_URL);
 request.setConnectionKeepAlive(true);
 request.send();
 assertTrue(request.isConnectionKeepAlive());
 }
}