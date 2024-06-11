import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.exceptions.OAuthException;
import org.scribe.utils.URLUtils;
import java.net.HttpURLConnection;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testRequestWithDefaults() {
 // Test default initialization
 assertNull(request.getBytePayload());
 assertNull(request.getPayload());
 assertFalse(request.isConnectionKeepAlive());
 assertEquals(Verb.GET, request.getVerb());
 assertEquals("http://example.com", request.getUrl());
 assertEquals(Collections.emptyMap(), request.getBodyParams());
 assertEquals(Collections.emptyMap(), request.getHeaders());
 assertEquals(Collections.emptyMap(), request.getQuerystringParams());
 }

 @Test(expected = OAuthException.class)
 public void testInvalidUrl() {
 request = new Request(Verb.GET, "http://example.com:");
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("key", "value");
 Map<String, String> params = request.getQueryStringParams();
 assertEquals("value", params.get("key"));
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("key", "value");
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals("value", bodyParams.get("key"));
 }

 @Test
 public void testAddHeader() {
 request.addHeader("Key", "Value");
 Map<String, String> headers = request.getHeaders();
 assertEquals("Value", headers.get("Key"));
 }

 @Test
 public void testAddPayload() {
 request.addPayload("Payload");
 assertEquals("Payload", request.getPayload());
 }

 @Test
 public void testAddPayloadByteArray() {
 byte[] payload = "Payload".getBytes(StandardCharsets.UTF_8);
 request.addPayload(payload);
 assertEquals(payload, request.getBytePayload());
 }

 @Test
 public void testSendGetRequest() throws IOException {
 request.setCharset("UTF-8");
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
 when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("Response".getBytes(StandardCharsets.UTF_8)));
 request.setConnection(connection);
 Response response = request.send();
 assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
 assertEquals("Response", response.getBody());
 }

 @Test
 public void testSendPutRequest() throws IOException {
 request.setVerb(Verb.PUT);
 request.addHeader("Content-Type", "application/json");
 request.addBodyParameter("key", "value");
 request.setPayload("{\"key\":\"value\"}");
 request.setCharset("UTF-8");
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
 when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("{\"status\":\"success\"}".getBytes(StandardCharsets.UTF_8)));
 request.setConnection(connection);
 Response response = request.send();
 assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
 assertEquals("{\"status\":\"success\"}", response.getBody());
 }

 @Test(expected = IOException.class)
 public void testSendPostRequestWithoutBody() throws IOException {
 request.setVerb(Verb.POST);
 request.setCharset("UTF-8");
 request.setConnectionKeepAlive(true);
 Response response = request.send();
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testSendPostRequestWithEmptyBody() throws IOException {
 request.setVerb(Verb.POST);
 request.setCharset("UTF-8");
 request.setConnectionKeepAlive(true);
 request.setBytePayload(new byte[0]);
 Response response = request.send();
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testSendPostRequestWithoutHeaders() throws IOException {
 request.setVerb(Verb.POST);
 request.setCharset("UTF-8");
 request.setConnectionKeepAlive(true);
 request.setBytePayload("Payload".getBytes(StandardCharsets.UTF_8));
 Response response = request.send();
 fail("Expected IOException");
 }

 @Test
 public void testCreateConnection() throws IOException {
 request.setUrl("http://example.com?key=value");
 request.setQuerystringParameter("key", "value");
 request.setConnectionKeepAlive(true);
 request.createConnection();
 assertTrue(request.getConnection() != null);
 }

 @Test
 public void testDoSend() throws IOException {
 request.setVerb(Verb.GET);
 request.setUrl("http://example.com");
 request.createConnection();
 Response response = request.doSend();
 assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
 assertEquals("", response.getBody());
 }

 @Test
 public void testGetByteBodyContents() throws IOException {
 request.setPayload("Payload");
 byte[] bytePayload = "Payload".getBytes(StandardCharsets.UTF_8);
 request.setBytePayload(bytePayload);
 assertEquals(bytePayload, request.getByteBodyContents());
 }

 @Test
 public void testGetCharset() {
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.getCharset());
 }

 @Test
 public void testGetQuerystringParams() {
 request.setUrl("http://example.com?key=value");
 Map<String, String> params = request.getQueryStringParams();
 assertEquals("value", params.get("key"));
 }

 @Test
 public void testGetBodyParams() {
 request.addBodyParameter("key", "value");
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals("value", bodyParams.get("key"));
 }

 @Test
 public void testGetHeaders() {
 request.addHeader("Key", "Value");
 Map<String, String> headers = request.getHeaders();
 assertEquals("Value", headers.get("Key"));
 }

 @Test
 public void testGetUrl() {
 assertEquals("http://example.com", request.getUrl());
 }

 @Test
 public void testGetSanitizedUrl() {
 assertEquals("example.com", request.getSanitizedUrl());
 }

 @Test
 public void testGetBodyContents() throws IOException {
 request.setPayload("Payload");
 assertEquals("Payload", request.getBodyContents());
 }

 @Test
 public void testGetVerb() {
 assertEquals(Verb.GET, request.getVerb());
 }
}