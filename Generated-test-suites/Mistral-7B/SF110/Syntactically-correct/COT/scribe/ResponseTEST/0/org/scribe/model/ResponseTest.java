import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connection;

 @Before
 public void setUp() throws IOException {
 connection = (HttpURLConnection) new URL("http://example.com").openConnection();
 response = new Response(connection);
 }

 @Test
 public void testResponseConstructorWithSuccess() throws IOException {
 // Basic functionality test
 response = new Response(connection);
 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getHeaders());
 assertNotNull(response.getStream());
 }

 @Test(expected = UnknownHostException.class)
 public void testResponseConstructorWithUnknownHost() {
 // Exception handling test
 response = new Response(connection);
 connection.disconnect();
 connection.setInstanceFollowRedirects(false);
 connection.setDoInput(true);
 connection.setDoOutput(true);
 connection.connect();
 connection.disconnect();
 connection.setRequestMethod("GET");
 connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
 connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3094.110 Safari/56.0");
 connection.setRequestProperty("Connection", "keep-alive");
 connection.setUseCookies(false);
 connection.setFollowRedirects(false);
 connection.setInstanceFollowRedirects(false);
 connection.connect();
 response = new Response(connection);
 }

 @Test
 public void testParseBodyContents() throws IOException {
 String expectedBody = "This is a test body.";
 connection.setDoOutput(true);
 try (OutputStream os = connection.getOutputStream()) {
 os.write(expectedBody.getBytes("UTF-8"));
 os.flush();
 }
 response = new Response(connection);
 assertEquals(expectedBody, response.getBody());
 }

 @Test
 public void testParseHeaders() {
 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put("Content-Type", "text/html; charset=UTF-8");
 expectedHeaders.put("Content-Length", "23");
 expectedHeaders.put("Cache-Control", "max-age=0");
 response = new Response(connection);
 Map<String, String> actualHeaders = response.getHeaders();
 assertEquals(expectedHeaders, actualHeaders);
 }

 @Test
 public void testWasSuccessful() {
 // Edge case test
 response = new Response(connection);
 response.setCode(299);
 assertFalse(response.wasSuccessful());

 // Edge case test
 response = new Response(connection);
 response.setCode(199);
 assertFalse(response.wasSuccessful());

 // Edge case test
 response = new Response(connection);
 response.setCode(398);
 assertFalse(response.wasSuccessful());
 }

 @Test
 public void testGetHeader() {
 String expectedHeaderValue = "Some header value";
 Map<String, String> headers = new HashMap<>();
 headers.put("HeaderKey", expectedHeaderValue);
 response = new Response(connection);
 response.setHeaders(headers);
 String actualHeaderValue = response.getHeader("HeaderKey");
 assertEquals(expectedHeaderValue, actualHeaderValue);
 }

 @Test
 public void testGetEmptyResponseBody() {
 response = new Response(connection);
 assertNull(response.getBody());
 }

 @Test
 public void testGetNullResponseStream() {
 response = new Response(connection);
 response.setStream(null);
 assertNull(response.getStream());
 }

 @Test
 public void testGetEmptyResponseHeaders() {
 response = new Response(connection);
 response.setHeaders(new HashMap<>());
 assertNotNull(response.getHeaders());
 assertEmpty(response.getHeaders().entrySet());
 }

 @Test
 public void testGetNonExistentResponseHeader() {
 response = new Response(connection);
 String nonExistentHeaderName = "NonExistentHeaderName";
 assertNull(response.getHeader(nonExistentHeaderName));
 }

 @Test
 public void testGetResponseWithInputStream() throws IOException {
 String expectedBody = "This is a test body.";
 connection.setDoOutput(true);
 try (OutputStream os = connection.getOutputStream()) {
 os.write(expectedBody.getBytes("UTF-8"));
 os.flush();
 }
 response = new Response(connection);
 response.setStream(new ByteArrayInputStream(expectedBody.getBytes("UTF-8")));
 assertEquals(expectedBody, response.getBody());
 }

 @Test
 public void testGetResponseWithErrorStream() throws IOException {
 String expectedErrorBody = "This is a test error body.";
 connection.setDoOutput(true);
 connection.setRequestMethod("GET");
 connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
 connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3094.110 Safari/56.0");
 connection.setRequestProperty("Connection", "keep-alive");
 connection.setUseCookies(false);
 connection.setFollowRedirects(false);
 connection.connect();
 response = new Response(connection);
 assertEquals(500, response.getCode());
 assertEquals(expectedErrorBody, StreamUtils.getStreamContents(response.getStream()));
 }

 @Test
 public void testGetResponseWithNullInputStream() {
 response = new Response(connection);
 response.setStream(null);
 assertNull(response.getBody());
 }
}