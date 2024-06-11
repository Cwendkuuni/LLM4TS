import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.*;
import org.scribe.exceptions.*;
import org.scribe.utils.*;
import java.net.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.*;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testRequestWithNullVerbAndUrl() {
 Assert.assertNull(new Request(null, null));
 }

 @Test
 public void testRequestWithValidArguments() {
 Assert.assertNotNull(request);
 Assert.assertEquals(Verb.GET, request.getVerb());
 Assert.assertEquals("http://example.com", request.getUrl());
 Assert.assertEquals(Collections.emptyMap(), request.getQueryStringParams());
 Assert.assertEquals(Collections.emptyMap(), request.getBodyParams());
 Assert.assertEquals(Collections.emptyMap(), request.getHeaders());
 Assert.assertNull(request.getByteBodyContents());
 Assert.assertNull(request.getPayload());
 }

 @Test(expected = OAuthException.class)
 public void testSendThrowsIOException() throws Exception {
 request.setConnectionKeepAlive(true);
 request.send();
 }

 @Test
 public void testCreateConnection() throws Exception {
 request.setConnectionKeepAlive(true);
 request.createConnection();
 Assert.assertNotNull(request.getConnection());
 }

 @Test
 public void testDoSendReturnsResponse() throws Exception {
 request.setConnectionKeepAlive(true);
 Response response = request.doSend();
 Assert.assertNotNull(response);
 }

 @Test
 public void testAddHeaders() throws Exception {
 request.addHeader("key", "value");
 request.createConnection();
 request.addHeaders(request.getConnection());
 Map<String, String> headers = request.getHeaders();
 Assert.assertEquals("value", headers.get("key"));
 }

 @Test
 public void testAddBodyWithEmptyMap() throws Exception {
 request.addBodyParameter("key", "value");
 byte[] bytePayload = request.getByteBodyContents();
 request.addBody(request.getConnection(), bytePayload);
 }

 @Test
 public void testAddBodyWithStringPayload() throws Exception {
 request.addPayload("payload");
 byte[] bytePayload = request.getByteBodyContents();
 request.addBody(request.getConnection(), bytePayload);
 }

 @Test
 public void testAddBodyWithBytePayload() throws Exception {
 request.addPayload(new byte[]{"test".getBytes("UTF-8")});
 request.addBody(request.getConnection(), request.getByteBodyContents());
 }

 @Test(expected = OAuthException.class)
 public void testAddBodyWithInvalidCharset() throws Exception {
 request.addPayload("payload");
 request.setCharset("invalidCharset");
 request.addBody(request.getConnection(), request.getByteBodyContents());
 }

 @Test
 public void testGetQueryStringParams() throws Exception {
 request.addQuerystringParameter("key", "value");
 Map<String, String> params = request.getQueryStringParams();
 Assert.assertEquals("value", params.get("key"));
 }

 @Test
 public void testGetBodyParams() {
 request.addBodyParameter("key", "value");
 Map<String, String> params = request.getBodyParams();
 Assert.assertEquals("value", params.get("key"));
 }

 @Test
 public void testGetUrl() {
 Assert.assertEquals("http://example.com", request.getUrl());
 }

 @Test
 public void testGetSanitizedUrl() {
 Assert.assertEquals("example.com", request.getSanitizedUrl());
 }

 @Test
 public void testGetBodyContentsWithNullCharset() throws Exception {
 request.addPayload("payload");
 String bodyContents = request.getBodyContents();
 Assert.assertNotNull(bodyContents);
 }

 @Test(expected = OAuthException.class)
 public void testGetBodyContentsWithInvalidCharset() throws Exception {
 request.addPayload("payload");
 request.setCharset("invalidCharset");
 request.getBodyContents();
 }

 @Test
 public void testGetVerb() {
 Assert.assertEquals(Verb.GET, request.getVerb());
 }

 @Test
 public void testGetHeaders() {
 request.addHeader("key", "value");
 Map<String, String> headers = request.getHeaders();
 Assert.assertEquals("value", headers.get("key"));
 }

 @Test
 public void testGetCharset() {
 request.setCharset("charset");
 Assert.assertEquals("charset", request.getCharset());
 }

 @Test
 public void testSetConnectTimeout() {
 request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
 Assert.assertEquals(5000, request.getConnection().getConnectTimeout());
 }

 @Test
 public void testSetReadTimeout() {
 request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
 Assert.assertEquals(5000, request.getConnection().getReadTimeout());
 }

 @Test
 public void testSetCharset() {
 request.setCharset("charset");
 Assert.assertEquals("charset", request.getCharset());
 }

 @Test
 public void testSetConnectionKeepAlive() {
 request.setConnectionKeepAlive(true);
 Assert.assertTrue(request.isConnectionKeepAlive());
 }

 @Test
 public void testSetConnection() {
 request.setConnection(new URL("http://example.com").openConnection());
 Assert.assertEquals(request.getConnection(), request.getConnection());
 }

 @Test
 public void testToString() {
 Assert.assertEquals("@Request(GET http://example.com)", request.toString());
 }
}