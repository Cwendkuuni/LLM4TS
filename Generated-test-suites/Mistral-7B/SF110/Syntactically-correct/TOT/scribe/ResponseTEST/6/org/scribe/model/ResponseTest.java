import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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
 public void setup() {
 connectionMock = mock(HttpURLConnection.class);
 response = new Response(connectionMock);
 }

 @Test
 public void testResponseWithSuccessfulStatusCode() throws IOException {
 // Given: mock successful response
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getHeaderFields()).thenReturn(new HashMap<>());
 when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("Successful response".getBytes()));

 // When: create Response object
 response = new Response(connectionMock);

 // Then: test getters for successful response
 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader("NonExistentHeader"));
 }

 @Test
 public void testResponseWithErrorStatusCode() throws IOException {
 // Given: mock error response
 when(connectionMock.getResponseCode()).thenReturn(500);
 when(connectionMock.getHeaderFields()).thenReturn(new HashMap<>());
 when(connectionMock.getErrorStream()).thenReturn(new ByteArrayInputStream("Error response".getBytes()));

 // When: create Response object
 response = new Response(connectionMock);

 // Then: test getters for error response
 assertEquals(500, response.getCode());
 assertNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 assertNull(response.getHeader("NonExistentHeader"));
 }

 @Test(expected = UnknownHostException.class)
 public void testResponseWithUnknownHostException() throws IOException {
 // Given: mock unknown host exception
 when(connectionMock.getResponseCode()).thenThrow(new UnknownHostException());

 // When: create Response object
 response = new Response(connectionMock);

 // Then: check if exception is thrown
 }

 @Test
 public void testParseHeaders() {
 // Given: mock response with headers
 when(connectionMock.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
 put("Content-Type", Collections.singletonList("application/json"));
 put("Cache-Control", Collections.singletonList("max-age=0"));
 put("ETag", Collections.singletonList("W/\"abc123\""));
 }});

 // When: create Response object and parse headers
 response = new Response(connectionMock);
 Map<String, String> headers = response.getHeaders();

 // Then: test headers getter returns expected headers
 assertEquals("application/json", headers.get("Content-Type"));
 assertEquals("max-age=0", headers.get("Cache-Control"));
 assertEquals("W/\"abc123\"", headers.get("ETag"));
 assertEquals(null, headers.get("NonExistentHeader"));
 }
}