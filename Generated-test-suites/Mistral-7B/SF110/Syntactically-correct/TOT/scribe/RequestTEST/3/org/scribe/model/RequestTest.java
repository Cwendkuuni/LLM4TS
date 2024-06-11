import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import static org.junit.Assert.*;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "https://api.example.com?param1=value1&param2=value2");
 request.addQuerystringParameter("param1", "newValue1");
 request.addBodyParameter("param3", "newValue3");
 request.addHeader("Content-Type", "application/json");
 request.addPayload("{\"key\":\"value\"}");
 }

 @Test
 public void testSend_ValidRequest() {
 try {
 Response response = request.send();
 assertEquals(200, response.getStatusCode());
 } catch (OAuthException e) {
 e.printStackTrace();
 fail("Unexpected exception");
 }
 }

 @Test
 public void testSend_InvalidUrl() {
 request.setUrl("http://:1234/invalidUrl");
 try {
 request.send();
 fail("Expected OAuthException not thrown");
 } catch (OAuthException e) {
 assertEquals("Malformed URL", e.getMessage());
 }
 }

 @Test
 public void testSend_MissingMethod() {
 request.setVerb(null);
 try {
 request.send();
 fail("Expected OAuthException not thrown");
 } catch (OAuthException e) {
 assertEquals("Invalid HTTP method", e.getMessage());
 }
 }

 @Test
 public void testSend_MissingUrl() {
 request.setUrl(null);
 try {
 request.send();
 fail("Expected OAuthException not thrown");
 } catch (OAuthException e) {
 assertEquals("URL is required", e.getMessage());
 }
 }

 @Test
 public void testSend_MissingHeaders() {
 request.setHeaders(null);
 try {
 request.send();
 fail("Expected OAuthException not thrown");
 } catch (OAuthException e) {
 assertEquals("Headers are required", e.getMessage());
 }
 }

 @Test
 public void testSend_MissingPayload() {
 request.setPayload(null);
 try {
 request.send();
 fail("Expected OAuthException not thrown");
 } catch (OAuthException e) {
 assertEquals("Payload is required for PUT and POST methods", e.getMessage());
 }
 }

 @Test
 public void testSend_InvalidContentLength() {
 request.setHeaders(Collections.singletonMap("Content-Length", "abc"));
 try {
 request.send();
 fail("Expected IOException not thrown");
 } catch (IOException e) {
 assertTrue(e.getMessage().contains("Invalid content length: abc"));
 }
 }

 @Test
 public void testSend_IOException() {
 try {
 request.setUrl("http://localhost:8080/notfound");
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
 Map<String, String> params = request.getBodyParams();
 assertEquals(1, params.size());
 assertEquals("newValue3", params.get("param3"));
 }

 @Test
 public void testGetUrl() {
 assertEquals("https://api.example.com?param1=newValue1&param2=value2", request.getUrl());
 }

 @Test
 public void testGetSanitizedUrl() {
 assertEquals("https://api.example.com", request.getSanitizedUrl());
 }

 @Test
 public void testGetBodyContents() {
 assertEquals("{\"key\":\"value\"}", request.getBodyContents());
 }

 @Test
 public void testGetVerb() {
 assertEquals(Verb.GET, request.getVerb());
 }

 @Test
 public void testGetHeaders() {
 Map<String, String> headers = request.getHeaders();
 assertEquals(1, headers.size());
 assertEquals("application/json", headers.get("Content-Type"));
 }

 @Test
 public void testGetCharset() {
 assertEquals("UTF-8", request.getCharset());
 }
}