import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
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
 private HttpURLConnection connectionMock;

 @Before
 public void setUp() {
 connectionMock = Mockito.mock(HttpURLConnection.class);
 response = new Response(connectionMock);
 }

 @Test
 public void testResponseConstructor() throws IOException {
 // Test normal response construction
 URL url = new URL("http://example.com");
 Mockito.when(connectionMock.getResponseCode()).thenReturn(200);
 Mockito.when(connectionMock.getErrorStream()).thenReturn(null);
 response = new Response(connectionMock);

 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertEquals(200, response.getCode());
 assertNotNull(response.getHeaders());

 // Test UnknownHostException construction
 Mockito.when(connectionMock.getResponseCode()).thenThrow(new UnknownHostException("Unknown host: example.com"));
 response = new Response(connectionMock);

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 }

 @Test
 public void testParseBodyContents() throws IOException {
 String expectedBody = "Hello World";
 ByteArrayInputStream inputStreamMock = new ByteArrayInputStream(expectedBody.getBytes());
 Mockito.when(connectionMock.getInputStream()).thenReturn(inputStreamMock);

 response.parseBodyContents();

 assertEquals(expectedBody, response.getBody());
 }

 @Test
 public void testParseHeaders() {
 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put("Content-Type", "application/json");
 expectedHeaders.put("X-Custom-Header", "Test Value");

 Mockito.when(connectionMock.getHeaderFields()).thenReturn(expectedHeaders);

 Map<String, String> actualHeaders = response.getHeaders();

 assertEquals(expectedHeaders, actualHeaders);
 }

 @Test
 public void testWasSuccessful() {
 assertTrue(response.wasSuccessful());
 response.setCode(299);
 assertFalse(response.wasSuccessful());
 }

 @Test(expected = NullPointerException.class)
 public void testGetHeaderNullName() {
 response.setHeaders(null);
 response.getHeader(null);
 }

 @Test(expected = NullPointerException.class)
 public void testGetHeaderNullHeaderMap() {
 response.setHeaders(new HashMap<>());
 response.getHeader(null);
 }

 @Test
 public void testGetHeaderValidName() {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 response.setHeaders(headers);

 assertEquals("application/json", response.getHeader("Content-Type"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetHeaderInvalidName() {
 response.setHeaders(new HashMap<>());
 response.getHeader("Invalid Header Name");
 }

 @Test
 public void testGetStreamNullBody() {
 response.setBody(null);
 assertNull(response.getStream());
 }

 @Test
 public void testGetStreamNotNullBody() {
 InputStream inputStreamMock = Mockito.mock(InputStream.class);
 response.setBody("Test Body");
 Mockito.when(response.getStream()).thenReturn(inputStreamMock);

 assertNotNull(response.getStream());
 }

 @Test
 public void testGetCodeNegativeCode() {
 response.setCode(-1);
 assertEquals(-1, response.getCode());
 }

 @Test
 public void testGetCodeZeroCode() {
 response.setCode(0);
 assertEquals(0, response.getCode());
 }

 @Test
 public void testGetCodeNonSuccessCode() {
 response.setCode(401);
 assertEquals(401, response.getCode());
 }

 @Test
 public void testGetCodeSuccessCode() {
 response.setCode(200);
 assertEquals(200, response.getCode());
 }

 @Test
 public void testGetHeadersEmptyMap() {
 response.setHeaders(new HashMap<>());
 assertEquals(0, response.getHeaders().size());
 }

 @Test
 public void testGetHeadersNonEmptyMap() {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 headers.put("X-Custom-Header", "Test Value");
 response.setHeaders(headers);
 assertEquals(2, response.getHeaders().size());
 }
}