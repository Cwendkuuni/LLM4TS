import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.*;
import org.scribe.exceptions.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RequestTest {

 private Request request;
 private Verb verb;
 private String url;
 private Map<String, String> querystringParams;
 private Map<String, String> bodyParams;
 private Map<String, String> headers;
 private HttpURLConnection connectionMock;

 @Before
 public void setUp() {
 verb = Verb.GET;
 url = "http://localhost:8080/api/test";
 querystringParams = new HashMap<>();
 bodyParams = new HashMap<>();
 headers = new HashMap<>();
 request = new Request(verb, url);
 connectionMock = mock(HttpURLConnection.class);
 }

 @After
 public void tearDown() throws Exception {
 request = null;
 connectionMock = null;
 }

 @Test
 public void testRequestWithDefaults() {
 Request request = new Request(Verb.GET, "http://example.com");
 assertEquals("@Request(GET http://example.com)", request.toString());
 }

 @Test
 public void testCreateConnection() throws IOException {
 when(connectionMock.openConnection()).thenReturn(connectionMock);
 request.createConnection();
 verify(connectionMock, times(1)).openConnection();
 }

 @Test(expected = OAuthException.class)
 public void testSendThrowsExceptionWhenIOException() throws Exception {
 when(connectionMock.getInputStream()).thenThrow(new IOException("Test Exception"));
 request.connection = connectionMock;
 request.send();
 fail("Should have thrown an OAuthException");
 }

 @Test
 public void testSendReturnsResponse() throws Exception {
 Response response = mock(Response.class);
 when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("Test Response".getBytes()));
 when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
 when(connectionMock.getContentType()).thenReturn("application/json");
 when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("{\"success\":true}".getBytes(StandardCharsets.UTF_8)));
 when(connectionMock.getHeaderFields()).thenReturn(headers);
 request.connection = connectionMock;
 request.addHeader("Content-Type", "application/json");
 request.addBodyParameter("param1", "value1");
 Response actualResponse = request.send();
 assertEquals(response, actualResponse);
 }

 @Test
 public void testSettingAndGettingVerb() {
 request.setVerb(Verb.PUT);
 assertEquals(Verb.PUT, request.getVerb());
 }

 @Test
 public void testSettingAndGettingUrl() {
 request.setUrl("http://localhost:8080/api/test2");
 assertEquals("http://localhost:8080/api/test2", request.getUrl());
 }

 @Test
 public void testSettingAndGettingHeaders() {
 request.addHeader("Authorization", "Bearer abc123");
 request.addHeader("Content-Type", "application/json");
 assertEquals("Authorization: Bearer abc123, Content-Type: application/json", request.getHeaders().toString());
 }

 @Test
 public void testSettingAndGettingQuerystringParameters() {
 request.addQuerystringParameter("param1", "value1");
 Map<String, String> params = request.getQueryStringParams();
 assertEquals("param1=value1", params.toString());
 }

 @Test
 public void testSettingAndGettingBodyParameters() {
 request.addBodyParameter("param1", "value1");
 assertEquals("param1=value1", request.getBodyParams().toString());
 }

 @Test
 public void testSettingAndGettingPayload() {
 request.addPayload("{\"param1\":\"value1\"}");
 assertEquals("{\"param1\":\"value1\"}", request.getPayload());
 }

 @Test
 public void testSettingAndGettingBytePayload() {
 request.addPayload("{\"param1\":\"value1\"}".getBytes());
 assertEquals("{\"param1\":\"value1\"}", new String(request.getBytePayload(), StandardCharsets.UTF_8));
 }

 @Test
 public void testGetCharset() {
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.getCharset());
 }

 @Test
 public void testSettingAndGettingConnectionKeepAlive() {
 request.setConnectionKeepAlive(true);
 assertEquals(true, request.isConnectionKeepAlive());
 }

 @Test
 public void testSettingAndGettingReadTimeout() throws Exception {
 request.setReadTimeout(5, TimeUnit.SECONDS);
 assertEquals(5000, request.getReadTimeout());
 }

 @Test
 public void testSettingAndGettingConnectTimeout() throws Exception {
 request.setConnectTimeout(3, TimeUnit.SECONDS);
 assertEquals(3000, request.getConnectTimeout());
 }
}