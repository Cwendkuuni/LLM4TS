package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.exceptions.OAuthException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
 public void testRequestWithNoParameters() throws OAuthException, IOException {
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testRequestWithQuerystringParameters() throws OAuthException, IOException {
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 Map<String, String> querystringParams = request.getQueryStringParams();
 assertEquals(2, querystringParams.size());
 assertEquals("value1", querystringParams.get("param1"));
 assertEquals("value2", querystringParams.get("param2"));
 }

 @Test
 public void testRequestWithBodyParameters() throws OAuthException, IOException {
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] bytePayload = outputStream.toByteArray();
 request.addPayload(new String(bytePayload, "UTF-8"));
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals(2, bodyParams.size());
 assertEquals("value1", bodyParams.get("param1"));
 assertEquals("value2", bodyParams.get("param2"));
 }

 @Test
 public void testRequestWithHeaders() throws OAuthException, IOException {
 request.addHeader("Header1", "Value1");
 request.addHeader("Header2", "Value2");
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.setRequestProperty(eq("Header1"), anyString())).thenReturn(true);
 when(connection.setRequestProperty(eq("Header2"), anyString())).thenReturn(true);
 request.setConnection(connection);
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 Map<String, String> headers = request.getHeaders();
 assertEquals(2, headers.size());
 assertEquals("Value1", headers.get("Header1"));
 assertEquals("Value2", headers.get("Header2"));
 }

 @Test(expected = OAuthException.class)
 public void testRequestWithInvalidCharset() throws OAuthException, IOException {
 request.setCharset("InvalidCharset");
 request.addPayload("Test String");
 try {
 request.send();
 fail("Expected OAuthException");
 } catch (OAuthException e) {
 assertEquals("Unsupported Charset: InvalidCharset", e.getMessage());
 }
 }

 @Test
 public void testRequestWithPayload() throws OAuthException, IOException {
 String payload = "Test String";
 request.addPayload(payload);
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 String bodyContents = request.getBodyContents();
 assertEquals(payload, bodyContents);
 }

 @Test
 public void testRequestWithGetVerb() throws OAuthException, IOException {
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 assertEquals(Verb.GET, request.getVerb());
 }

 @Test
 public void testRequestWithPostVerb() throws OAuthException, IOException {
 request.setVerb(Verb.POST);
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 byte[] bytePayload = Arrays.toString(new byte[]{"1", "2"}).getBytes();
 request.addPayload(bytePayload);
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testRequestWithPutVerb() throws OAuthException, IOException {
 request.setVerb(Verb.PUT);
 Map<String, String> bodyParams = new HashMap<String, String>();
 bodyParams.put("param1", "value1");
 bodyParams.put("param2", "value2");
 String payload = "Test String";
 request.addBody(bodyParams.toString().getBytes(), "UTF-8");
 request.addPayload(payload);
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 }
}