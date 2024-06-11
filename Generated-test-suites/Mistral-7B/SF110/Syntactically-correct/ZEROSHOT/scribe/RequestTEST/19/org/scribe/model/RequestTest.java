package org.scribe; // assuming the test file is in the same package as the Request class

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() throws Exception {
 request = new Request(Verb.GET, "http://example.com/api?param1=value1&param2=value2");
 }

 @Test
 public void testRequestWithGetVerbAndDefaults() {
 Request request = new Request(Verb.GET, "http://example.com/api");
 assertEquals("@Request(GET http://example.com/api)", request.toString());
 }

 @Test
 public void testRequestWithGetVerbAndQuerystringParameters() {
 Map<String, String> queryStringParams = request.getQueryStringParams();
 assertEquals(2, queryStringParams.size());
 assertEquals("value1", queryStringParams.get("param1"));
 assertEquals("value2", queryStringParams.get("param2"));
 }

 @Test
 public void testRequestWithGetVerbAndAdditionalHeaders() {
 Map<String, String> headers = new HashMap<>();
 headers.put("X-Custom-Header", "custom-value");
 request.addHeaders(headers);
 assertEquals(1, request.getHeaders().size());
 assertEquals("custom-value", request.getHeaders().get("X-Custom-Header"));
 }

 @Test(expected = OAuthException.class)
 public void testRequestWithUnsupportedCharset() {
 request.setCharset("UTF-16BE");
 try {
 request.getByteBodyContents();
 fail("Expected OAuthException");
 } catch (OAuthException e) {
 // pass
 }
 }

 @Test
 public void testRequestWithPutVerbAndBody() throws IOException {
 String body = "{\"key\":\"value\"}";
 request = new Request(Verb.PUT, "http://example.com/api");
 request.addPayload(body);
 HttpURLConnection connection = request.send().getConnection();
 try {
 InputStream is = connection.getInputStream();
 String responseBody = convertInputStreamToString(is);
 assertEquals(body, responseBody);
 } finally {
 connection.disconnect();
 }
 }

 @Test
 public void testRequestWithPostVerbAndBody() throws IOException {
 String body = "{\"key\":\"value\"}";
 request = new Request(Verb.POST, "http://example.com/api");
 request.addPayload(body);
 request.setCharset(StandardCharsets.UTF_8.name());
 HttpURLConnection connection = request.send().getConnection();
 try {
 InputStream is = connection.getInputStream();
 String responseBody = convertInputStreamToString(is);
 assertEquals(body, responseBody);
 } finally {
 connection.disconnect();
 }
 }

 @Test
 public void testRequestWithPostVerbAndBodyStream() throws IOException {
 byte[] body = "{\"key\":\"value\"}".getBytes(StandardCharsets.UTF_8);
 request = new Request(Verb.POST, "http://example.com/api");
 request.addPayload(body);
 request.setCharset(StandardCharsets.UTF_8.name());
 request.setConnectionKeepAlive(true);
 HttpURLConnection connection = request.send().getConnection();
 try {
 connection.setDoOutput(true);
 connection.getOutputStream().write(body);
 connection.setRequestProperty("Content-Length", String.valueOf(body.length));
 connection.getOutputStream().close();
 InputStream is = connection.getInputStream();
 String responseBody = convertInputStreamToString(is);
 assertEquals(new String(body, StandardCharsets.UTF_8), responseBody);
 } finally {
 connection.disconnect();
 }
 }

 @Test
 public void testRequestWithAdditionalQuerystringParameters() {
 request.addQuerystringParameter("newParam", "newValue");
 Map<String, String> queryStringParams = request.getQueryStringParams();
 assertEquals(3, queryStringParams.size());
 assertEquals("newValue", queryStringParams.get("newParam"));
 }

 @Test
 public void testRequestWithAdditionalBodyParameters() {
 request.addBodyParameter("newKey", "newValue");
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals("newValue", bodyParams.get("newKey"));
 }

 @Test
 public void testRequestWithAdditionalHeader() {
 request.addHeader("X-Custom-Header", "custom-value");
 Map<String, String> headers = request.getHeaders();
 assertEquals("custom-value", headers.get("X-Custom-Header"));
 }

 @Test
 public void testRequestWithReadTimeout() throws IOException, InterruptedException {
 request.setReadTimeout(1, TimeUnit.SECONDS);
 try {
 request.send().getResponseCode();
 fail("Expected timeout exception");
 } catch (IOException e) {
 // pass
 }
 Thread.sleep(2000);
 }

 @Test
 public void testRequestWithConnectTimeout() throws IOException, InterruptedException {
 request.setConnectTimeout(1, TimeUnit.SECONDS);
 try {
 request.send().getResponseCode();
 fail("Expected timeout exception");
 } catch (IOException e) {
 // pass
 }
 Thread.sleep(2050);
 }

 @Test
 public void testRequestWithUrlSanitization() {
 String url = "http://example.com:8080/api?param1=value1&param2=value2";
 request = new Request(Verb.GET, url);
 assertEquals("@Request(GET http://example.com/api)", request.toString());
 }

 private String convertInputStreamToString(InputStream inputStream) throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] buffer = new byte[4096];
 int bytesRead;
 while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
 outputStream.write(buffer, 0, bytesRead);
 }
 return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
 }
}