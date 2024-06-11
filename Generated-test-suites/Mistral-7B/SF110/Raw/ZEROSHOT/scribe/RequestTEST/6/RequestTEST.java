import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @After
 public void tearDown() throws Exception {
 request.getConnection().disconnect();
 }

 @Test
 public void testRequestCreation() {
 // Test default values
 assertEquals(Verb.GET, request.getVerb());
 assertEquals("http://example.com", request.getUrl());
 assertNull(request.getPayload());
 assertNull(request.getBytePayload());
 assertFalse(request.isConnectionKeepAlive());
 assertEquals(0, request.getConnection().getConnectTimeout());
 assertEquals(0, request.getConnection().getReadTimeout());
 assertNull(request.getCharset());
 assertNull(request.getHeaders());
 }

 @Test
 public void testSettingProperties() {
 // Test setting properties
 request.addHeader("Content-Type", "application/json");
 request.addBodyParameter("key", "value");
 request.addQuerystringParameter("param", "value");
 request.addPayload("payload");
 request.addPayload(new ByteArrayOutputStream(10).toByteArray());
 request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
 request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
 request.setCharset("UTF-8");
 request.setConnectionKeepAlive(true);

 // Test getting properties
 assertEquals(asList("Content-Type"), request.getHeaders().keySet());
 assertEquals("application/json", request.getHeaders().get("Content-Type"));
 assertEquals("value", request.getBodyParams().get("key"));
 assertEquals("param=value&", request.getQueryStringParams().toString());
 assertEquals("payload", request.getPayload());
 assertEquals(10, request.getConnection().getConnectTimeout());
 assertEquals(1000, request.getConnection().getReadTimeout());
 assertEquals("UTF-8", request.getCharset());
 assertTrue(request.isConnectionKeepAlive());
 }

 @Test(expected = OAuthException.class)
 public void testMalformedURLException() {
 request = new Request(Verb.GET, "http://example.com:");
 }

 @Test
 public void testGetByteBodyContents() throws IOException {
 // Test getting byte body contents from payload
 request.addPayload("payload");
 byte[] expectedByteBody = "payload".getBytes();
 byte[] actualByteBody = request.getByteBodyContents();
 assertArrayEquals(expectedByteBody, actualByteBody);

 // Test getting byte body contents from body params
 request.clearBodyParams();
 request.addBodyParameter("key", "value");
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 byteStream.write("value".getBytes());
 request.addPayload(byteStream.toByteArray());
 byte[] expectedByteBody2 = "value".getBytes();
 byte[] actualByteBody2 = request.getByteBodyContents();
 assertArrayEquals(expectedByteBody2, actualByteBody2);
 }

 @Test
 public void testGetBodyContents() throws IOException, UnsupportedEncodingException {
 // Test getting body contents from payload
 request.addPayload("payload");
 String expectedBody = "payload";
 String actualBody = new String(request.getByteBodyContents(), "UTF-8");
 assertEquals(expectedBody, actualBody);

 // Test getting body contents from body params
 request.clearBodyParams();
 request.addBodyParameter("key", "value");
 request.addPayload(new ByteArrayOutputStream(10).toByteArray());
 String charset = "UTF-16";
 String expectedBody2 = "value";
 String actualBody2 = new String(request.getByteBodyContents(), charset);
 assertEquals(expectedBody2, actualBody2);
 }

 @Test
 public void testSend() throws IOException {
 // Test sending a GET request
 Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testSendWithBody() throws IOException {
 // Test sending a POST request with a body
 String body = "{\"key\":\"value\"}";
 request.setVerb(Verb.POST);
 request.addPayload(body);
 request.addHeader("Content-Type", "application/json");
 Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals(200, response.getStatusCode());
 String actualBody = new String(request.getByteBodyContents(), "UTF-8");
 assertEquals(body, actualBody);
 }
}