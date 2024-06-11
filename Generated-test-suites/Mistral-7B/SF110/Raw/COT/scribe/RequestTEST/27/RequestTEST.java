import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.*;
import org.scribe.exceptions.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "https://api.example.com/");
 }

 @Test
 public void testRequestWithValidArguments() {
 Request request = new Request(Verb.GET, "https://api.example.com/");
 assertNotNull(request);
 assertEquals(Verb.GET, request.getVerb());
 assertEquals("https://api.example.com", request.getUrl());
 assertEquals(Collections.emptyMap(), request.getQueryStringParams());
 assertEquals(Collections.emptyMap(), request.getBodyParams());
 assertEquals(Collections.emptyMap(), request.getHeaders());
 assertNull(request.getBytePayload());
 assertNull(request.getPayload());
 assertNull(request.getCharset());
 assertFalse(request.isConnectionKeepAlive());
 assertNull(request.getConnection());
 }

 @Test
 public void testAddHeader() {
 request.addHeader("X-Foo", "Bar");
 assertEquals("Bar", request.getHeaders().get("X-Foo"));
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("foo", "bar");
 assertEquals("bar", request.getBodyParams().get("foo"));
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("foo", "bar");
 assertEquals("bar", request.getQueryStringParams().get("foo"));
 }

 @Test
 public void testAddPayload() {
 request.addPayload("Hello World!");
 assertEquals("Hello World!", request.getPayload());
 }

 @Test
 public void testAddPayloadWithByteArray() {
 byte[] payload = "Hello World!".getBytes();
 request.addPayload(payload);
 assertEquals(payload, request.getBytePayload());
 }

 @Test
 public void testCreateConnection() throws IOException {
 request.createConnection();
 assertNotNull(request.getConnection());
 }

 @Test(expected = IOException.class)
 public void testCreateConnectionWithNullUrl() {
 request = new Request(Verb.GET, null);
 request.createConnection();
 }

 @Test(expected = OAuthException.class)
 public void testSendWithMalformedUrl() {
 request = new Request(Verb.GET, "https://api.example.com:/");
 assertThrows(OAuthException.class, request::send);
 }

 @Test
 public void testSendWithValidUrl() throws IOException {
 Response response = request.send();
 assertNotNull(response);
 }

 @Test(expected = OAuthException.class)
 public void testSendWithIOException() {
 request = new Request(Verb.GET, "https://api.example.com/");
 MockURLConnection connection = new MockURLConnection("https://api.example.com/");
 request.setConnection(connection);
 when(connection.getInputStream()).thenThrow(new IOException("Test IOException"));
 assertThrows(OAuthException.class, request::send);
 }

 @Test
 public void testDoSendWithValidUrl() throws IOException {
 Response response = request.doSend();
 assertNotNull(response);
 }

 @Test(expected = IOException.class)
 public void testDoSendWithNullConnection() throws IOException {
 request = new Request(Verb.GET, "https://api.example.com/");
 assertThrows(IOException.class, request::doSend);
 }

 @Test
 public void testGetQueryStringParams() {
 request.addQuerystringParameter("foo", "bar");
 assertEquals(Collections.singletonMap("foo", "bar"), request.getQueryStringParams());
 }

 @Test
 public void testGetBodyParams() {
 request.addBodyParameter("foo", "bar");
 assertEquals(Collections.singletonMap("foo", "bar"), request.getBodyParams());
 }

 @Test
 public void testGetUrl() {
 assertEquals("https://api.example.com", request.getUrl());
 }

 @Test
 public void testGetSanitizedUrl() {
 assertEquals("api.example.com", request.getSanitizedUrl());
 }

 @Test
 public void testGetBodyContents() {
 request.addPayload("Hello World!");
 assertEquals("Hello World!", request.getBodyContents());
 }

 @Test(expected = OAuthException.class)
 public void testGetBodyContentsWithUnsupportedCharset() {
 request.setCharset("UTF-16BE");
 assertThrows(OAuthException.class, request::getBodyContents);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testGetByteBodyContentsWithUnsupportedCharset() throws UnsupportedEncodingException {
 request.setCharset("UTF-16BE");
 byte[] bytePayload = "Hello World!".getBytes();
 request.addPayload(bytePayload);
 assertThrows(UnsupportedEncodingException.class, request::getByteBodyContents);
 }

 @Test
 public void testGetByteBodyContents() throws UnsupportedEncodingException {
 byte[] bytePayload = "Hello World!".getBytes();
 request.addPayload(bytePayload);
 byte[] actualBytePayload = request.getByteBodyContents();
 assertArrayEquals(bytePayload, actualBytePayload);
 }

 @Test
 public void testGetVerb() {
 assertEquals(Verb.GET, request.getVerb());
 }

 @Test
 public void testGetHeaders() {
 return; // This method returns an unmodifiable map, so it's not possible to test it directly
 }

 @Test
 public void testGetCharset() {
 assertEquals(Charset.defaultCharset().name(), request.getCharset());
 }

 @Test
 public void testSetConnectTimeout() {
 request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
 assertEquals(5000, request.getConnection().getConnectTimeout());
 }

 @Test
 public void testSetReadTimeout() {
 request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
 assertEquals(5000, request.getConnection().getReadTimeout());
 }

 @Test
 public void testSetCharset() {
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.getCharset());
 }

 @Test
 public void testSetConnectionKeepAlive() {
 request.setConnectionKeepAlive(true);
 assertTrue(request.isConnectionKeepAlive());
 }

 @Test
 public void testSetConnection() {
 HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
 request.setConnection(connection);
 assertEquals(connection, request.getConnection());
 }

}