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
 public void testRequestWithGetVerbAndEmptyQueryString() {
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testRequestWithGetVerbAndQueryString() {
 request.addQuerystringParameter("key", "value");
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 }

 @Test(expected = OAuthException.class)
 public void testRequestWithInvalidVerb() {
 request = new Request(null, "http://example.com");
 assertThrows(OAuthException.class, request::send);
 }

 @Test
 public void testRequestWithPutVerbAndEmptyBody() {
 request = new Request(Verb.PUT, "http://example.com");
 Response response = request.send();
 assertEquals(405, response.getStatusCode());
 }

 @Test
 public void testRequestWithPutVerbAndBody() throws UnsupportedEncodingException {
 request.addPayload("test body");
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 }

 @Test(expected = OAuthException.class)
 public void testRequestWithNullUrl() {
 request = new Request(Verb.GET, null);
 assertThrows(OAuthException.class, request::send);
 }

 @Test(expected = MalformedURLException.class)
 public void testRequestWithMalformedUrl() {
 request = new Request(Verb.GET, "http://example.com:");
 assertThrows(MalformedURLException.class, request::send);
 }

 @Test
 public void testRequestWithHeaders() {
 request.addHeader("X-Test", "Test");
 Response response = request.send();
 String headerValue = response.getHeaders().get("X-Test");
 assertEquals("Test", headerValue);
 }

 @Test
 public void testRequestWithBodyParameters() {
 request.addBodyParameter("key", "value");
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals("value", bodyParams.get("key"));
 }

 @Test
 public void testRequestWithQueryStringParameters() {
 request.addQuerystringParameter("key", "value");
 Map<String, String> queryStringParams = request.getQueryStringParams();
 assertEquals("value", queryStringParams.get("key"));
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testRequestWithInvalidCharset() {
 request.setCharset("invalid_charset");
 request.send();
 }

 @Test
 public void testRequestWithConnectionKeepAliveTrue() {
 request.setConnectionKeepAlive(true);
 Response response1 = request.send();
 Response response2 = request.send();
 assertEquals(response1.getStatusCode(), response2.getStatusCode());
 }

 @Test
 public void testRequestWithConnectionKeepAliveFalse() {
 request.setConnectionKeepAlive(false);
 Response response1 = request.send();
 Response response2 = request.send();
 assertNotEquals(response1.getStatusCode(), response2.getStatusCode());
 }

}