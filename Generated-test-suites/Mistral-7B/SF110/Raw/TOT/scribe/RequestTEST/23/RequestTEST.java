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
 request.addBodyParameter("param2", "value2");
 request.addHeader("Custom-Header", "Custom-Value");
 }

 @Test
 public void testSend_GET_Success() throws OAuthException {
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testSend_POST_Success() throws OAuthException {
 request.setPayload("payload1");
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 }

 @Test
 public void testSend_PUT_Success() throws OAuthException {
 request.setPayload("payload1");
 request.setVerb(Verb.PUT);
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 }

 @Test(expected = OAuthException.class)
 public void testSend_IOException() throws OAuthException {
 doReturn(null).when(request).createConnection(); // Mock createConnection to throw IOException
 Response response = request.send();
 fail("IOException expected");
 }

 @Test(expected = OAuthException.class)
 public void testSend_InvalidUrl() throws OAuthException {
 request.setUrl("invalid://example.com");
 try {
 request.send();
 fail("OAuthException expected");
 } catch (OAuthException e) {
 assertEquals("Malformed URL", e.getMessage());
 }
 }

 @Test(expected = OAuthException.class)
 public void testSend_UnsupportedCharset() throws OAuthException {
 request.setCharset("Unsupported-Charset");
 try {
 request.send();
 fail("OAuthException expected");
 } catch (OAuthException e) {
 assertEquals("Unsupported Charset: Unsupported-Charset", e.getMessage());
 }
 }

 @Test
 public void testAddHeader() {
 request.addHeader("Custom-Header", "Custom-Value");
 assertEquals("Custom-Value", request.getHeaders().get("Custom-Header"));
 }

 @Test
 public void testAddBodyParameter() {
 request.addBodyParameter("param1", "value1");
 assertEquals("value1", request.getBodyParams().get("param1"));
 }

 @Test
 public void testAddQuerystringParameter() {
 request.addQuerystringParameter("param1", "value1");
 Map<String, String> params = request.getQueryStringParams();
 assertEquals("param1=value1", params.get("param1"));
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
 request.addPayload("payload1");
 assertEquals("payload1", request.getBodyContents());
 }

 @Test
 public void testGetVerb() {
 assertEquals(Verb.GET, request.getVerb());
 }

 @Test
 public void testGetHeaders() {
 Map<String, String> headers = request.getHeaders();
 assertEquals("Custom-Value", headers.get("Custom-Header"));
 }

 @Test
 public void testGetCharset() {
 assertEquals(Charset.defaultCharset().name(), request.getCharset());
 }
}