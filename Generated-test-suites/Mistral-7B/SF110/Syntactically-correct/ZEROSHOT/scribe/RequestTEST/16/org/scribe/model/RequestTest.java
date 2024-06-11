package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "https://api.example.com/resource?param=value");
 }

 @Test
 public void testRequestWithNoBodyOrHeaders() {
 Request request = new Request(Verb.GET, "https://api.example.com/resource?param=value");
 Response response = request.send();
 assertNotNull(response);
 }

 @Test(expected = OAuthException.class)
 public void testRequestWithNullUrl() {
 Request request = new Request(Verb.GET, null);
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testRequestWithNullVerb() {
 Request request = new Request(null, "https://api.example.com/resource?param=value");
 request.send();
 }

 @Test
 public void testGetUrl() {
 assertEquals("https://api.example.com/resource?param=value", request.getUrl());
 }

 @Test
 public void testGetSanitizedUrl() {
 assertEquals("api.example.com/resource", request.getSanitizedUrl());
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("test", "value");
 Map<String, String> params = request.getQueryStringParams();
 assertEquals("value", params.get("test"));
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("test", "value");
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals("value", bodyParams.get("test"));
 }

 @Test
 public void testAddHeader() {
 request.addHeader("Authorization", "Bearer token");
 Map<String, String> headers = request.getHeaders();
 assertEquals("Bearer token", headers.get("Authorization"));
 }

 @Test
 public void testAddPayload() {
 request.addPayload("test payload");
 String payload = request.getBodyContents();
 assertEquals("test payload", payload);
 }

 @Test
 public void testAddPayloadByteArray() {
 byte[] bytePayload = "test byte payload".getBytes();
 request.addPayload(bytePayload);
 InputStream inputStream = new ByteArrayInputStream(bytePayload);
 byte[] readData = new byte[1024];
 int read = 0;
 String payload = "";
 try {
 while ((read = inputStream.read(readData)) != -1) {
 payload += new String(readData, 0, read);
 }
 }
 catch (IOException e) {
 e.printStackTrace();
 }
 String payloadFromRequest = request.getBodyContents();
 assertEquals(payload, payloadFromRequest);
 }

 @Test
 public void testCreateConnection() throws IOException {
 request.createConnection();
 assertNotNull(request.getConnection());
 }

 @Test
 public void testDoSend() throws IOException {
 Response response = request.send();
 assertNotNull(response);
 }

 @Test
 public void testSendWithHeaders() throws IOException {
 request.addHeader("Content-Type", "application/json");
 request.addHeader("Accept", "application/json");
 Response response = request.send();
 Map<String, List<String>> headers = response.getHeaders();
 assertTrue(headers.containsKey("Content-Type"));
 assertTrue(headers.containsKey("Accept"));
 }

 @Test
 public void testSendWithBody() throws IOException {
 Map<String, String> bodyParams = new HashMap<>();
 bodyParams.put("test", "value");
 request.addBodyParameters(bodyParams);
 request.addHeader("Content-Type", "application/json");
 Response response = request.send();
 Map<String, String> headers = response.getHeaders();
 assertEquals("application/json", headers.get("Content-Type"));
 Map<String, String> body = response.getBody();
 assertEquals("value", body.get("test"));
 }

 @Test
 public void testSendWithBodyAndHeaders() throws IOException {
 Map<String, String> bodyParams = new HashMap<>();
 bodyParams.put("test", "value");
 request.addBodyParameters(bodyParams);
 request.addHeader("Content-Type", "application/json");
 request.addHeader("Accept", "application/json");
 Response response = request.send();
 Map<String, List<String>> headers = response.getHeaders();
 assertTrue(headers.containsKey("Content-Type"));
 assertTrue(headers.containsKey("Accept"));
 Map<String, String> body = response.getBody();
 assertEquals("value", body.get("test"));
 }

 @Test
 public void testSendWithBodyByteArray() throws IOException {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 request.addHeaders(headers);
 byte[] bytePayload = "{\"test\": \"value\"}".getBytes();
 request.setBytePayload(bytePayload);
 Response response = request.send();
 Map<String, List<String>> headers2 = response.getHeaders();
 assertTrue(headers2.containsKey("Content-Type"));
 String body = new String(response.getByteBody(), Charset.forName("UTF-8"));
 assertEquals("{\"test\": \"value\"}", body);
 }

 @Test
 public void testSetConnectTimeout() {
 request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
 assertEquals(1000, request.getConnection().getConnectTimeout());
 }

 @Test
 public void testSetReadTimeout() {
 request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
 assertEquals(1000, request.getConnection().getReadTimeout());
 }

 @Test
 public void testSetCharset() {
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.getCharset());
 }

 @Test
 public void testSetConnectionKeepAlive() {
 request.setConnectionKeepAlive(true);
 assertEquals(true, request.isConnectionKeepAlive());
 }

 @Test
 public void testSetConnection() {
 HttpURLConnection connection = (HttpURLConnection) new URL("https://api.example.com/resource").openConnection();
 request.setConnection(connection);
 assertEquals(connection, request.getConnection());
 }

 @Test
 public void testToString() {
 assertEquals("@Request(GET https://api.example.com/resource?param=value)", request.toString());
 }
}