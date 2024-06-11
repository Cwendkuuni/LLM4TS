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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testRequestInstance() {
 // Test initial request instance
 assertEquals("@Request(GET http://example.com)", request.toString());
 assertNull(request.getPayload());
 assertNull(request.getBytePayload());
 assertFalse(request.isConnectionKeepAlive());
 assertNull(request.getCharset());
 assertNull(request.getConnection());
 }

 @Test
 public void testSetVerb() {
 request.setVerb(Verb.POST);
 assertEquals("@Request(POST http://example.com)", request.toString());
 }

 @Test
 public void testSetUrl() {
 request.setUrl("http://example.org");
 assertEquals("@Request(POST http://example.org)", request.toString());
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("key", "value");
 assertEquals("key=value", request.getQueryStringParams().get("key"));
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("key", "value");
 assertEquals("value", request.getBodyParams().get("key"));
 }

 @Test
 public void testAddPayload() {
 request.addPayload("payload");
 assertEquals("payload", request.getPayload());
 }

 @Test
 public void testAddPayloadByteArray() {
 byte[] payload = "payload".getBytes();
 request.addPayload(payload);
 assertEquals(payload, request.getBytePayload());
 }

 @Test
 public void testAddHeader() {
 request.addHeader("key", "value");
 assertEquals("value", request.getHeaders().get("key"));
 }

 @Test
 public void testSetConnectTimeout() {
 request.setConnectTimeout(5, TimeUnit.SECONDS);
 assertEquals(5000, request.getConnection().getConnectTimeout());
 }

 @Test
 public void testSetReadTimeout() {
 request.setReadTimeout(5, TimeUnit.SECONDS);
 assertEquals(5000, request.getConnection().getReadTimeout());
 }

 @Test
 public void testSendRequestWithGETVerb() throws OAuthException, IOException {
 request.setUrl("http://example.com?key=value");
 request.addQuerystringParameter("key", "value");
 request.addHeader("key", "value");
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
 when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
 request.setConnection(connection);
 Response response = request.send();
 assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
 }

 @Test(expected = OAuthException.class)
 public void testSendRequestWithInvalidUrl() {
 request.setUrl("http://example.com:");
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testSendRequestWithInvalidVerb() {
 request.setVerb(new Verb("INVALID"));
 request.send();
 }

 @Test(expected = IOException.class)
 public void testSendRequestWithConnectionError() throws IOException {
 request.setUrl("http://example.com");
 HttpURLConnection connection = mock(HttpURLConnection.class);
 when(connection.getErrorStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
 request.setConnection(connection);
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testSendRequestWithUnsupportedEncoding() throws OAuthException, UnsupportedEncodingException {
 request.addPayload("payload");
 request.setCharset("UNSUPPORTED_ENCODING");
 request.send();
 }

 @Test
 public void testGetCharset() {
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.getCharset());
 }

 @Test
 public void testGetBodyContents() throws UnsupportedEncodingException, IOException {
 request.addBodyParameter("key", "value");
 assertEquals("value\n", new String(request.getByteBodyContents(), request.getCharset()));
 }

 @Test
 public void testGetQueryStringParams() throws MalformedURLException {
 Map<String, String> queryStringParams = request.getQueryStringParams();
 assertEquals("key=value", queryStringParams.get("key"));
 }

 @Test
 public void testGetBodyParams() {
 assertEquals(Arrays.asList("key=value"), request.getBodyParams().entrySet().stream().map(Map.Entry::getKey).toList());
 }

 @Test
 public void testGetUrl() {
 assertEquals("http://example.com", request.getUrl());
 }

 @Test
 public void testGetSanitizedUrl() {
 assertEquals("example.com", request.getSanitizedUrl());
 }
}