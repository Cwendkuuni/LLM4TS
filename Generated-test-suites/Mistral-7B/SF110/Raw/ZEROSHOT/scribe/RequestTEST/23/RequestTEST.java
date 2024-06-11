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
import java.util.concurrent.TimeUnit;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testRequestWithNoParameters() throws IOException, OAuthException {
 Request request = new Request(Verb.GET, "http://example.com");
 Response response = request.send();
 assertTrue(response.isSuccess());
 }

 @Test(expected = OAuthException.class)
 public void testRequestWithMalformedUrl() throws IOException, OAuthException {
 Request request = new Request(Verb.GET, "http://example.com:");
 }

 @Test
 public void testRequestWithQuerystringParameters() throws IOException, OAuthException {
 Map<String, String> querystringParams = new HashMap<>();
 querystringParams.put("param1", "value1");
 querystringParams.put("param2", "value2");
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 Request request = new Request(Verb.GET, "http://example.com?" + URLUtils.mapToQueryString(querystringParams));
 Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals("value1", request.getQueryStringParams().get("param1"));
 assertEquals("value2", request.getQueryStringParams().get("param2"));
 }

 @Test
 public void testRequestWithBodyParameters() throws IOException, OAuthException {
 Map<String, String> bodyParams = new HashMap<>();
 bodyParams.put("param1", "value1");
 bodyParams.put("param2", "value2");
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 request.addPayload("payload=" + URLUtils.formURLEncodeMap(bodyParams).replace("+", "%20"));
 String expectedBody = "param1=value1&param2=value2";
 Request request = new Request(Verb.POST, "http://example.com");
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 byte[] byteBody = expectedBody.getBytes("UTF-8");
 request.setBytePayload(byteBody);
 Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals(expectedBody, new String(request.getByteBodyContents(), "UTF-8"));
 }

 @Test
 public void testRequestWithHeaders() throws IOException, OAuthException {
 Map<String, String> headers = new HashMap<>();
 headers.put("Header1", "Value1");
 headers.put("Header2", "Value2");
 request.addHeader("Header1", "Value1");
 request.addHeader("Header2", "Value2");
 Request request = new Request(Verb.GET, "http://example.com");
 request.addHeader("Header1", "Value1");
 request.addHeader("Header2", "Value2");
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getHeaderFields()).thenReturn(headers);
 Request testRequest = new Request(Verb.GET, "http://example.com") {
 @Override
 public Response doSend() throws IOException {
 return new Response(connection);
 }
 };
 Response response = testRequest.send();
 assertEquals(headers, testRequest.getHeaders());
 }

 @Test
 public void testRequestWithPayload() throws IOException, OAuthException {
 request.addPayload("payload");
 Request request = new Request(Verb.POST, "http://example.com");
 request.addPayload("payload");
 Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals("payload", request.getBodyContents());
 }

 @Test
 public void testRequestWithBytePayload() throws IOException, OAuthException {
 String payload = "payload";
 byte[] bytePayload = payload.getBytes("UTF-8");
 request.addPayload(bytePayload);
 Request request = new Request(Verb.POST, "http://example.com");
 request.addPayload(bytePayload);
 Response response = request.send();
 assertTrue(response.isSuccess());
 InputStream inputStream = new ByteArrayInputStream(bytePayload);
 assertEquals(inputStream, request.getConnection().getInputStream());
 }

 @Test
 public void testRequestWithSetters() throws IOException, OAuthException {
 request.setCharset("UTF-8");
 request.setConnectionKeepAlive(true);
 Request request = new Request(Verb.GET, "http://example.com");
 request.setCharset("UTF-8");
 request.setConnectionKeepAlive(true);
 assertEquals("UTF-8", request.getCharset());
 assertEquals(true, request.isConnectionKeepAlive());
 }

 @Test
 public void testSendWithException() throws IOException, OAuthException {
 Request request = new Request(Verb.GET, "http://example.com");
 doThrow(new IOException("Test exception")).when(request).createConnection();
 try {
 request.send();
 fail();
 } catch (OAuthException e) {
 assertEquals("Test exception", e.getMessage());
 }
 }
}