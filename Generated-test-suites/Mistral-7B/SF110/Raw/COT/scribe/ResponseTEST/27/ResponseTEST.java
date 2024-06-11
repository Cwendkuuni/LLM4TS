import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

 private static final String EMPTY = "";
 private static final String TEST_HEADER_NAME = "Test-Header";
 private static final String TEST_HEADER_VALUE = "Test-Header-Value";

 @Test
 public void testResponseConstructor() throws IOException {
 Response response = new Response(createMockHttpURLConnection(200, EMPTY, new HashMap<>(), EMPTY));
 Assert.assertEquals(200, response.getCode());
 Assert.assertNull(response.getBody());
 Assert.assertNull(response.getStream());
 Assert.assertEquals(new HashMap<>(), response.getHeaders());
 }

 @Test(expected = UnknownHostException.class)
 public void testResponseConstructorWithUnknownHostException() throws IOException {
 Response response = new Response(createMockHttpURLConnection(0, EMPTY, new HashMap<>(), EMPTY));
 }

 @Test
 public void testParseBodyContents() throws IOException {
 String testBody = "Test Body";
 Response response = new Response(createMockHttpURLConnection(200, testBody, new HashMap<>(), EMPTY));
 Assert.assertEquals(testBody, response.getBody());
 }

 @Test
 public void testParseHeaders() {
 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put(TEST_HEADER_NAME, TEST_HEADER_VALUE);
 Response response = new Response(createMockHttpURLConnection(200, EMPTY, expectedHeaders, EMPTY));
 Map<String, String> actualHeaders = response.getHeaders();
 Assert.assertEquals(expectedHeaders, actualHeaders);
 }

 @Test
 public void testWasSuccessful() {
 Response response = new Response(createMockHttpURLConnection(200, EMPTY, new HashMap<>(), EMPTY));
 Assert.assertTrue(response.wasSuccessful());
 }

 @Test
 public void testWasSuccessfulWithInvalidStatusCode() {
 Response response = new Response(createMockHttpURLConnection(404, EMPTY, new HashMap<>(), EMPTY));
 Assert.assertFalse(response.wasSuccessful());
 }

 @Test
 public void testGetBodyWithEmptyBody() {
 Response response = new Response(createMockHttpURLConnection(200, EMPTY, new HashMap<>(), EMPTY));
 Assert.assertEquals(EMPTY, response.getBody());
 }

 @Test
 public void testGetBodyWithNonEmptyBody() throws IOException {
 String testBody = "Test Body";
 Response response = new Response(createMockHttpURLConnection(200, testBody, new HashMap<>(), EMPTY));
 Assert.assertEquals(testBody, response.getBody());
 }

 @Test
 public void testGetStreamWithEmptyBody() {
 Response response = new Response(createMockHttpURLConnection(200, EMPTY, new HashMap<>(), EMPTY));
 Assert.assertNull(response.getStream());
 }

 @Test
 public void testGetStreamWithNonEmptyBody() throws IOException {
 String testBody = "Test Body";
 ByteArrayInputStream testInputStream = new ByteArrayInputStream(testBody.getBytes());
 Response response = new Response(createMockHttpURLConnection(200, EMPTY, new HashMap<>(), testBody), testInputStream);
 Assert.assertEquals(testInputStream, response.getStream());
 }

 @Test
 public void testGetCode() {
 Response response = new Response(createMockHttpURLConnection(200, EMPTY, new HashMap<>(), EMPTY));
 Assert.assertEquals(200, response.getCode());
 }

 @Test
 public void testGetCodeWithInvalidStatusCode() {
 Response response = new Response(createMockHttpURLConnection(404, EMPTY, new HashMap<>(), EMPTY));
 Assert.assertEquals(404, response.getCode());
 }

 @Test
 public void testGetHeaders() {
 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put(TEST_HEADER_NAME, TEST_HEADER_VALUE);
 Response response = new Response(createMockHttpURLConnection(200, EMPTY, expectedHeaders, EMPTY));
 Map<String, String> actualHeaders = response.getHeaders();
 Assert.assertEquals(expectedHeaders, actualHeaders);
 }

 @Test
 public void testGetHeaderWithValidName() {
 String testHeaderName = TEST_HEADER_NAME;
 String testHeaderValue = TEST_HEADER_VALUE;
 Response response = new Response(createMockHttpURLConnection(200, EMPTY, Map.of(testHeaderName, testHeaderValue), EMPTY));
 Assert.assertEquals(testHeaderValue, response.getHeader(testHeaderName));
 }

 @Test(expected = NullPointerException.class)
 public void testGetHeaderWithNullName() {
 String nullName = null;
 Response response = new Response(createMockHttpURLConnection(200, EMPTY, new HashMap<>(), EMPTY));
 response.getHeader(nullName);
 }

 @Test
 public void testGetHeaderWithInvalidName() {
 String invalidHeaderName = "Invalid-Header";
 Response response = new Response(createMockHttpURLConnection(200, EMPTY, new HashMap<>(), EMPTY));
 Assert.assertNull(response.getHeader(invalidHeaderName));
 }

 private HttpURLConnection createMockHttpURLConnection(int statusCode, String body, Map<String, String> headers, String errorMessage) throws IOException {
 URL url = new URL("http://test.com");
 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
 conn.setInstanceFollowRedirects(false);
 conn.setRequestMethod("GET");
 conn.disconnect();
 MockHttpURLConnection mockConn = new MockHttpURLConnection(conn) {
 @Override public int getResponseCode() {
 return statusCode;
 }

 @Override public String getResponseMessage() {
 return errorMessage;
 }

 @Override public InputStream getInputStream() throws IOException {
 if (body != null) {
 return new ByteArrayInputStream(body.getBytes());
 }
 return null;
 }

 @Override public Map<String, List<String>> getHeaderFields() {
 if (headers != null) {
 return headers;
 }
 return new HashMap<>();
 }
 };
 return mockConn;
 }

}