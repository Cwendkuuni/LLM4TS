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
 request = new Request(Verb.GET, "https://api.example.com/endpoint?param1=value1&param2=value2");
 request.addHeader("Authorization", "Bearer token");
 request.addQuerystringParameter("param3", "value3");
 request.addBodyParameter("param4", "value4");
 request.addPayload("{\"key\": \"value\"}");
 request.setCharset("UTF-8");
 request.setConnectionKeepAlive(true);
 }

 @Test
 public void testSend_GET_ValidRequest() {
 Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals(200, response.getStatusCode());
 }

 @Test(expected = OAuthException.class)
 public void testSend_GET_MalformedURL() {
 request.setUrl("https://api.example.com:");
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testSend_GET_IOException() throws Exception {
 doReturn(null).when(request).createConnection();
 request.send();
 }

 @Test
 public void testSend_POST_ValidRequest() throws Exception {
 request = new Request(Verb.POST, "https://api.example.com/endpoint");
 request.addHeader("Content-Type", "application/json");
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 request.addPayload("{\"key\": \"value\"}");
 request.setCharset("UTF-8");
 request.setConnectionKeepAlive(true);
 Response response = request.send();
 assertTrue(response.isSuccess());
 assertEquals(200, response.getStatusCode());
 }

 @Test(expected = OAuthException.class)
 public void testSend_POST_MalformedURL() {
 request.setUrl("https://api.example.com:");
 request.send();
 }

 @Test(expected = OAuthException.class)
 public void testSend_POST_IOException() throws Exception {
 doReturn(null).when(request).createConnection();
 request.send();
 }

 @Test
 public void testAddHeaders() {
 request.addHeader("X-Test", "header1");
 request.addHeader("X-Test", "header2");
 Map<String, String> headers = request.getHeaders();
 assertEquals(2, headers.size());
 assertEquals("header1", headers.get("X-Test"));
 assertEquals("header2", headers.get("X-Test"));
 }

 @Test
 public void testAddBodyParameters() {
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");
 Map<String, String> bodyParams = request.getBodyParams();
 assertEquals(2, bodyParams.size());
 assertEquals("value1", bodyParams.get("param1"));
 assertEquals("value2", bodyParams.get("param2"));
 }

 @Test
 public void testAddQuerystringParameters() {
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");
 Map<String, String> params = request.getQueryStringParams();
 assertEquals(4, params.size());
 assertEquals("value1", params.get("param1"));
 assertEquals("value2", params.get("param2"));
 }

 @Test
 public void testGetBodyContents() {
 String payload = request.getPayload();
 String bodyContents = request.getBodyContents();
 assertEquals(payload, bodyContents);
 }

 @Test
 public void testGetByteBodyContents() throws Exception {
 String payload = request.getPayload();
 byte[] bytePayload = request.getByteBodyContents();
 assertEquals(payload.getBytes().length, bytePayload.length);
 }

 @Test
 public void testGetVerb() {
 assertEquals(Verb.GET, request.getVerb());
 }
}