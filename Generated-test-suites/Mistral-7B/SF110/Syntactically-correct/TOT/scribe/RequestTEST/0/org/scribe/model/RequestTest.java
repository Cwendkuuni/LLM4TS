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
import java.io.OutputStreamWriter;
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
 public void testCreateAndSendRequest() throws IOException {
 Map<String, String> querystringParams = new HashMap<>();
 querystringParams.put("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 request.addHeader("Header1", "Value1");
 request.addBodyParameter("BodyParam1", "BodyValue1");
 request.addBodyParameter("BodyParam2", "BodyValue2");
 request.addPayload("Payload");
 request.setCharset("UTF-8");
 request.setConnectionKeepAlive(true);
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testIOExceptionOnCreateConnection() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 String effectiveUrl = URLUtils.appendParametersToQueryString(request.getUrl(), request.getQueryStringParams());
 URL url = new URL(effectiveUrl);
 try {
 System.setProperty("http.keepAlive", request.isConnectionKeepAlive() ? "true" : "false");
 HttpURLConnection connection = (HttpURLConnection)url.openConnection();
 request.setConnection(connection);
 } catch (IOException e) {
 throw new OAuthException("Problems while creating connection", e);
 }
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testMalformedURL() {
 request = new Request(Verb.GET, "http://example.com");
 request.addQuerystringParameter("param1", "value1");
 try {
 request.send();
 } catch (OAuthException e) {
 assertEquals("Malformed URL", e.getMessage());
 }
 }

 @Test(expected = OAuthException.class)
 public void testUnsupportedCharset() {
 request = new Request(Verb.GET, "http://example.com");
 request.addBodyParameter("BodyParam1", "BodyValue1");
 request.setCharset("X-Charset");
 try {
 request.send();
 } catch (OAuthException e) {
 assertEquals("Unsupported Charset: X-Charset", e.getMessage());
 }
 }

 @Test
 public void testSendGETRequest() throws IOException {
 Request requestGET = new Request(Verb.GET, "http://example.com");
 requestGET.send();
 }

 @Test
 public void testSendPOSTRequestWithBody() throws IOException {
 String body = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
 request = new Request(Verb.POST, "http://example.com");
 request.addBodyParameter("param1", "value1");
 request.addHeader("Content-Type", "application/json");
 request.addPayload(body);
 request.send();
 }

 @Test
 public void testSendPUTRequestWithBody() throws IOException {
 String body = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
 request = new Request(Verb.PUT, "http://example.com");
 request.addBodyParameter("param1", "value1");
 request.addHeader("Content-Type", "application/json");
 request.addPayload(body);
 request.send();
 }

 @Test
 public void testSendRequestWithHeaders() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.addHeader("Header1", "Value1");
 request.addHeader("Header2", "Value2");
 request.send();
 }

 @Test
 public void testSendRequestWithQueryStringParameters() throws IOException {
 request = new Request(Verb.GET, "http://example.com?param1=value1&param2=value2");
 request.addQuerystringParameter("param3", "value3");
 request.send();
 }

 @Test
 public void testSendRequestWithBodyParameters() throws IOException {
 request = new Request(Verb.POST, "http://example.com");
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 request.send();
 }

 @Test
 public void testSendRequestWithPayload() throws IOException {
 request = new Request(Verb.POST, "http://example.com");request.addPayload("Payload");
 request.send();
 }

 @Test
 public void testSendRequestWithBytePayload() throws IOException {
 byte[] bytePayload = "Payload".getBytes();
 request = new Request(Verb.POST, "http://example.com");
 request.addPayload(bytePayload);
 request.send();
 }

 @Test
 public void testSetConnectTimeout() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
 request.send();
 }

 @Test
 public void testSetReadTimeout() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
 request.send();
 }

 @Test
 public void testGetQueryStringParams() throws IOException {
 request = new Request(Verb.GET, "http://example.com?param1=value1&param2=value2");
 Map<String, String> params = request.getQueryStringParams();
 assertEquals(Arrays.asList("param1", "param2"), params.keySet());
 assertEquals("value1", params.get("param1"));
 assertEquals("value2", params.get("param2"));
 }

 @Test
 public void testGetBodyParams() throws IOException {
 request = new Request(Verb.POST, "http://example.com");
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 Map<String, String> params = request.getBodyParams();
 assertEquals(Arrays.asList("param1", "param2"), params.keySet());
 assertEquals("value1", params.get("param1"));
 assertEquals("value2", params.get("param2"));
 }

 @Test
 public void testGetUrl() throws IOException {
 request = new Request(Verb.GET, "http://example.com?param1=value1&param2=value2");
 assertEquals("http://example.com", request.getUrl());
 }

 @Test
 public void testGetSanitizedUrl() throws IOException {
 request = new Request(Verb.GET, "http://example.com?param1=value1&param2=value2");
 assertEquals("example.com", request.getSanitizedUrl());
 }

 @Test
 public void testGetBodyContents() throws IOException {
 request = new Request(Verb.POST, "http://example.com");
 request.addBodyParameter("param1", "value1");
 request.setCharset("UTF-8");
 String body = request.getBodyContents();
 assertEquals("param1=value1", body);
 }

 @Test
 public void testGetVerb() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 assertEquals(Verb.GET, request.getVerb());
 }

 @Test
 public void testGetHeaders() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.addHeader("Header1", "Value1");
 Map<String, String> headers = request.getHeaders();
 assertEquals(Arrays.asList("Header1"), headers.keySet());
 assertEquals("Value1", headers.get("Header1"));
 }

 @Test
 public void testGetCharset() throws IOException {
 request = new Request(Verb.GET, "http://example.com");
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.getCharset());
 }
}