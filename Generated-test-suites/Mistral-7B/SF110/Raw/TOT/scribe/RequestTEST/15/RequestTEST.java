import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;
import static org.junit.Assert.*;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 request.addBodyParameter("param3", "value3");
 request.addHeader("Header1", "value1");
 request.addHeader("Header2", "value2");
 }

 @Test
 public void testSend() throws OAuthException {
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 }

 @Test(expected = OAuthException.class)
 public void testSendWithInvalidUrl() {
 request.setUrl("http://example.com:");
 try {
 request.send();
 fail("Expected OAuthException not thrown");
 } catch (OAuthException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testSendWithIOException() throws OAuthException {
 request.setConnectionKeepAlive(true);
 doReturn(null).when(request).createConnection();
 try {
 request.send();
 fail("Expected IOException not thrown");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testGetQueryStringParams() {
 Map<String, String> params = request.getQueryStringParams();
 assertEquals(2, params.size());
 assertEquals("value1", params.get("param1"));
 assertEquals("value2", params.get("param2"));
 }

 @Test
 public void testGetBodyParams() {
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals(1, bodyParams.size());
 assertEquals("value3", bodyParams.get("param3"));
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
 public void testGetBodyContents() {
 String bodyContents = request.getBodyContents();
 assertNotNull(bodyContents);
 }

 @Test(expected = OAuthException.class)
 public void testGetBodyContentsWithUnsupportedCharset() {
 request.setCharset("invalidCharset");
 try {
 request.getBodyContents();
 fail("Expected OAuthException not thrown");
 } catch (OAuthException e) {
 // expected
 }
 }

 @Test
 public void testGetVerb() {
 assertEquals(Verb.GET, request.getVerb());
 }

 @Test
 public void testGetHeaders() {
 Map<String, String> headers = request.getHeaders();
 assertEquals(2, headers.size());
 assertEquals("value1", headers.get("Header1"));
 assertEquals("value2", headers.get("Header2"));
 }

 @Test
 public void testGetCharset() {
 assertEquals(Charset.defaultCharset().name(), request.getCharset());
 }

 @Test
 public void testSetConnectTimeout() {
 request.setConnectTimeout(5, TimeUnit.SECONDS);
 assertEquals(5000, request.connection.getConnectTimeout());
 }

 @Test
 public void testSetReadTimeout() {
 request.setReadTimeout(10, TimeUnit.SECONDS);
 assertEquals(10000, request.connection.getReadTimeout());
 }

 @Test
 public void testSetCharset() {
 request.setCharset("UTF-8");
 assertEquals("UTF-8", request.getCharset());
 }

 @Test
 public void testSetConnectionKeepAlive() {
 request.setConnectionKeepAlive(true);
 assertEquals(true, request.connectionKeepAlive);
 }

 @Test
 public void testToString() {
 assertEquals("@Request(GET http://example.com)", request.toString());
 }
}