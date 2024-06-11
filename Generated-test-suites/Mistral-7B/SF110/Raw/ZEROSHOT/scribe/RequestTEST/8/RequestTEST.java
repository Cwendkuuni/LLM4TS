import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class RequestTest {

 private Request request;
 private URL url;

 @Before
 public void setUp() throws IOException, UnsupportedEncodingException {
 url = new URL("http://example.com/path?param1=value1&param2=value2");
 request = new Request(Verb.GET, url.toString());
 }

 @After
 public void tearDown() throws Exception {
 if (request.connection != null) {
 request.connection.disconnect();
 }
 }

 @Test
 public void testRequestWithDefaults() {
assertEquals("@Request(GET http://example.com/path)", request.toString());
 assertFalse(request.connectionKeepAlive);
 assertNull(request.bytePayload);
 assertNull(request.payload);
 assertNull(request.charset);
 assertNull(request.connection);
 assertEquals(0, request.connection.getConnectTimeout());
 assertEquals(0, request.connection.getReadTimeout());
 assertNull(request.headers);
 assertNull(request.querystringParams);
 assertNull(request.bodyParams);
 }

 @Test
 public void testRequestWithHeaders() {
 request.addHeader("Header1", "Value1");
 request.addHeader("Header2", "Value2");
 Map<String, String> headers = request.getHeaders();
 assertEquals(2, headers.size());
 assertEquals("Value1", headers.get("Header1"));
 assertEquals("Value2", headers.get("Header2"));
 }

 @Test
 public void testRequestWithBody() throws UnsupportedEncodingException {
 request.addPayload("Hello World");
 Map<String, String> params = request.getQueryStringParams();
 assertEquals(0, params.size());
 assertEquals("Hello World", request.getBodyContents());
 }

 @Test
 public void testRequestWithByteBody() throws UnsupportedEncodingException {
 byte[] byteBody = "Hello World".getBytes();
 request.addPayload(byteBody);
 Map<String, String> params = request.getQueryStringParams();
 assertEquals(0, params.size());
 assertEquals(new String(byteBody, Charset.defaultCharset()), request.getBodyContents());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRequestWithNullBody() {
 request.addPayload(null);
 }

 @Test
 public void testRequestWithBodyAndQuerystringParameters() throws UnsupportedEncodingException {
 Map<String, String> bodyParams = new HashMap<>();
 bodyParams.put("key1", "value1");
 request.addBodyParameter("key1", "value1");
 request.addQuerystringParameter("param1", "value3");

 Map<String, String> queryStringParams = request.getQueryStringParams();
 Map<String, String> body = request.getBodyParams();
 assertEquals(1, queryStringParams.size());
 assertEquals("value3", queryStringParams.get("param1"));
 assertEquals(1, body.size());
 assertEquals("value1", body.get("key1"));
 }

 @Test(expected = OAuthException.class)
 public void testMalformedUrl() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testSetConnectTimeout() {
 request.setConnectTimeout(5, TimeUnit.SECONDS);
 assertEquals(5000, request.connection.getConnectTimeout());
 }

 @Test
 public void testSetReadTimeout() {
 request.setReadTimeout(5, TimeUnit.SECONDS);
 assertEquals(5000, request.connection.getReadTimeout());
 }

 @Test
 public void testSetCharset() {
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.charset);
 }

 @Test
 public void testSetConnectionKeepAlive() {
 request.setConnectionKeepAlive(true);
 assertTrue(request.connectionKeepAlive);
 }

 @Test(expected = IOException.class)
 public void testSend() throws IOException {
 request.createConnection();
 request.send();
 }

 @Test
 public void testSanitizedUrl() {
 assertEquals("http://example.com/path", request.getSanitizedUrl());
 }
}