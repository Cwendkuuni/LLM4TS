import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scribe.Model.Request;
import org.scribe.Model.Response;
import org.scribe.exceptions.OAuthException;
import static org.junit.Assert.*;

public class RequestTest {

 private Request request;

 @Before
 public void setUp() {
 request = new Request(Verb.GET, "http://example.com");
 }

 @Test
 public void testRequestWithGetVerbAndEmptyUrl() {
 // Test constructor
 assertNotNull(request);
 assertEquals("GET", request.getVerb().name());
 assertEquals("http://example.com", request.getUrl());
 assertEquals(0, request.getByteBodyContents().length);
 assertNull(request.getBytePayload());
 assertNull(request.getPayload());
 assertNull(request.getCharset());
 assertNull(request.getConnection());
 assertNull(request.getHeaders());
 assertNull(request.getQuerystringParams());
 assertNull(request.getBodyParams());
 }

 @Test
 public void testRequestWithGetVerbAndUrlWithQuerystringParams() {
 request.addQuerystringParameter("param1", "value1");
 request.addQuerystringParameter("param2", "value2");

 Map<String, String> querystringParams = request.getQueryStringParams();
 assertTrue(querystringParams.size() == 2);
 assertEquals("param1", querystringParams.get("param1"));
 assertEquals("value1", querystringParams.get("param1"));
 assertEquals("param2", querystringParams.get("param2"));
 assertEquals("value2", querystringParams.get("param2"));
 }

 @Test(expected = OAuthException.class)
 public void testRequestWithGetVerbAndMalformedUrl() {
 request = new Request(Verb.GET, "http://example.com:1234");
 }

 @Test
 public void testRequestWithGetVerbAndUrlWithPayload() {
 request.addPayload("payload");
 assertEquals("payload", request.getPayload());
 assertEquals(0, request.getByteBodyContents().length);
 assertNull(request.getBytePayload());
 }

 @Test
 public void testRequestWithGetVerbAndUrlWithBytePayload() {
 request.addPayload(new byte[5]);
 assertNotNull(request.getBytePayload());
 assertEquals(5, request.getByteBodyContents().length);
 }

 @Test
 public void testRequestWithGetVerbAndUrlWithHeaders() {
 request.addHeader("Content-Type", "application/json");
 request.addHeader("Accept", "application/json");

 Map<String, String> headers = request.getHeaders();
 assertTrue(headers.size() == 2);
 assertEquals("Content-Type", headers.get("Content-Type"));
 assertEquals("application/json", headers.get("Content-Type"));
 assertEquals("Accept", headers.get("Accept"));
 assertEquals("application/json", headers.get("Accept"));
 }

 @Test
 public void testRequestWithGetVerbAndUrlWithBodyParams() {
 request.addBodyParameter("param1", "value1");
 request.addBodyParameter("param2", "value2");

 Map<String, String> bodyParams = request.getBodyParams();
 assertTrue(bodyParams.size() == 2);
 assertEquals("param1", bodyParams.get("param1"));
 assertEquals("value1", bodyParams.get("param1"));
 assertEquals("param2", bodyParams.get("param2"));
 assertEquals("value2", bodyParams.get("param2"));
 }

 @Test
 public void testRequestWithPostVerbAndUrlWithPayload() {
 request = new Request(Verb.POST, "http://example.com");
 request.addPayload("payload");

 Response response = request.send();
 assertNotNull(response);
 assertEquals(200, response.getResponseCode());
 }

 @Test(expected = IOException.class)
 public void testRequestWithPostVerbAndUrlWithPayloadAndIOException() throws IOException {
 request = new Request(Verb.POST, "http://example.com");
 request.addPayload("payload");
 throw new IOException();
 }

 @Test
 public void testRequestWithPutVerbAndUrlWithBytePayload() {
 request = new Request(Verb.PUT, "http://example.com");
 request.addPayload(new byte[5]);

 Response response = request.send();
 assertNotNull(response);
 assertEquals(200, response.getResponseCode());
 }

 @After
 public void tearDown() {
 request = null;
 }

}