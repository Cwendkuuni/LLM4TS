import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connection;

 @Before
 public void setup() {
 connection = mock(HttpURLConnection.class);
 response = new Response(connection);
 }

 @Test
 public void testResponseConstructor() throws IOException {
 // Test successful response
 when(connection.getResponseCode()).thenReturn(200);
 response.parseHeaders(connection);
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertEquals(200, response.getCode());

 // Test error response
 when(connection.getResponseCode()).thenThrow(new UnknownHostException());
 response = new Response(connection);
 assertEquals(404, response.getCode());
 assertEquals(EMPTY, response.getBody());
 }

 @Test
 public void testParseBodyContents() throws IOException {
 String expectedBody = "Expected body";
 ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedBody.getBytes());
 when(connection.getInputStream()).thenReturn(inputStream);
 response = new Response(connection);
 assertEquals(expectedBody, response.getBody());
 }

 @Test
 public void testParseHeaders() {
 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put("Content-Type", "application/json");
 when(connection.getHeaderFields()).thenReturn(expectedHeaders);
 response = new Response(connection);
 assertEquals(expectedHeaders, response.getHeaders());
 }

 @Test
 public void testWasSuccessful() {
 response.setCode(201);
 assertTrue(response.wasSuccessful());
 response.setCode(404);
 assertFalse(response.wasSuccessful());
 }

 @Test(expected = UnknownHostException.class)
 public void testUnknownHostException() throws IOException {
 response = new Response(connection);
 response.parseBodyContents();
 }

 @Test
 public void testGetBody_emptyBody() {
 response.setBody(null);
 assertEquals(EMPTY, response.getBody());
 }

 @Test
 public void testGetStream_emptyBody() {
 response.setBody(null);
 assertNull(response.getStream());
 }

 @Test
 public void testGetCode_successCode() {
 response.setCode(200);
 assertEquals(200, response.getCode());
 }

 @Test
 public void testGetCode_errorCode() {
 response.setCode(404);
 assertEquals(404, response.getCode());
 }

 @Test
 public void testGetHeaders() {
 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put("Content-Type", "application/json");
 response.setHeaders(expectedHeaders);
 assertEquals(expectedHeaders, response.getHeaders());
 }

 @Test
 public void testGetHeader_correctName() {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 response.setHeaders(headers);
 assertEquals("application/json", response.getHeader("Content-Type"));
 }

 @Test
 public void testGetHeader_incorrectName() {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 response.setHeaders(headers);
 assertNull(response.getHeader("IncorrectName"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetHeader_nullHeaders() {
 response.setHeaders(null);
 response.getHeader("Content-Type");
 }
}