import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.exceptions.OAuthException;
import org.scribe.utils.URLUtils;
import org.mockito.MockitoAnnotations;
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
 private Verb verb;
 private String url;

 @Before
 public void setUp() {
 verb = Verb.GET;
 url = "http://example.com";
 request = new Request(verb, url);
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testRequestInstance() {
 Request request = new Request(Verb.GET, "http://example.com");
 assertNotNull(request);
 }

 @Test(expected = NullPointerException.class)
 public void testRequestNullUrl() {
 Request request = new Request(Verb.GET, null);
 }

 @Test
 public void testRequestUrl() {
 assertEquals("http://example.com", request.getUrl());
 }

 @Test
 public void testRequestSanitizedUrl() {
 assertEquals("", request.getSanitizedUrl());
 }

 @Test
 public void testRequestGetVerb() {
 assertEquals(Verb.GET, request.getVerb());
 }

 @Test
 public void testRequestAddHeaders() {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 request.addHeaders(headers);
 assertEquals(headers, request.getHeaders());
 }

 @Test
 public void testRequestAddBodyParameter() {
 request.addBodyParameter("key", "value");
 assertEquals("value", request.getBodyParams().get("key"));
 }

 @Test
 public void testRequestAddQuerystringParameter() {
 request.addQuerystringParameter("key", "value");
 assertEquals("value", request.getQueryStringParams().get("key"));
 }

 @Test
 public void testRequestAddPayload() {
 request.addPayload("payload");
 assertEquals("payload", request.getBodyContents());
 }

 @Test
 public void testRequestAddPayloadByteArray() {
 byte[] payload = "payload".getBytes();
 request.addPayload(payload);
 assertEquals(payload, request.getByteBodyContents());
 }

 @Test
 public void testRequestCreateConnection() throws IOException {
 URL url = new URL(request.getUrl());
 HttpURLConnection connection = mock(HttpURLConnection.class);
 doReturn(connection).when(url).openConnection();
 request.createConnection();
 verify(connection).setRequestProperty("Accept", "application/json;charset=UTF-8");
 verify(connection).setRequestProperty("User-Agent", "Scribe Java library");
 }

 @Test
 public void testRequestDoSend() throws IOException {
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
 request.setConnection(connection);
 Response response = request.send();
 assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
 }

 @Test(expected = OAuthException.class)
 public void testRequestDoSendWithIOException() throws IOException {
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenThrow(new IOException());
 request.setConnection(connection);
 request.send();
 }

 @Test
 public void testRequestGetResponse() throws IOException {
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
 when(connection.getInputStream()).thenReturn(mock(InputStream.class));
 request.setConnection(connection);
 Response response = request.send();
 assertEquals("Success", response.getBody());
 }

 @Test
 public void testRequestSetCharset() {
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.getCharset());
 }

 @Test
 public void testRequestSetConnectionKeepAlive() {
 request.setConnectionKeepAlive(true);
 assertTrue(request.isConnectionKeepAlive());
 }

 @Test
 public void testRequestSetConnectTimeout() throws IOException {
 request.setConnectTimeout(500, TimeUnit.MILLISECONDS);
 assertEquals(500, request.getConnection().getConnectTimeout());
 }

 @Test
 public void testRequestSetReadTimeout() throws IOException {
 request.setReadTimeout(500, TimeUnit.MILLISECONDS);
 assertEquals(500, request.getConnection().getReadTimeout());
 }

 @Test
 public void testRequestToString() {
 assertEquals("@Request(GET http://example.com)", request.toString());
 }
}