import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connectionMock;

 @Before
 public void setUp() {
 connectionMock = mock(HttpURLConnection.class);
 response = new Response(connectionMock);
 }

 @Test
 public void testResponseWithSuccessStatusCodeAndValidBody() throws IOException {
 // Given
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("Success body".getBytes()));
 when(connectionMock.getHeaderFields()).thenReturn(new HashMap<>());

 // When
 String body = response.getBody();
 Map<String, String> headers = response.getHeaders();
 int code = response.getCode();

 // Then
 assertEquals(200, code);
 assertNotNull(body);
 assertNotNull(headers);
 assertEquals("Success body", body);
 }

 @Test
 public void testResponseWithErrorStatusCodeAndEmptyBody() throws IOException {
 // Given
 when(connectionMock.getResponseCode()).thenReturn(404);
 when(connectionMock.getInputStream()).thenReturn(null);
 when(connectionMock.getHeaderFields()).thenReturn(new HashMap<>());

 // When
 String body = response.getBody();
 Map<String, String> headers = response.getHeaders();
 int code = response.getCode();

 // Then
 assertEquals(404, code);
 assertNull(body);
 assertNotNull(headers);
 }

 @Test
 public void testResponseWithSuccessStatusCodeAndEmptyBodyFromStream() throws IOException {
 // Given
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
 when(connectionMock.getHeaderFields()).thenReturn(new HashMap<>());

 // When
 String body = response.getBody();
 Map<String, String> headers = response.getHeaders();
 int code = response.getCode();

 // Then
 assertEquals(200, code);
 assertNull(body);
 assertNotNull(headers);
 }

 @Test(expected = IOException.class)
 public void testResponseWithIOException() throws IOException {
 // Given
 when(connectionMock.getResponseCode()).thenThrow(new IOException());

 // When & Then, an IOException should be thrown
 response.getBody();
 }

 @Test
 public void testResponseWithValidHeaders() throws IOException {
 // Given
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("Success body".getBytes()));
 when(connectionMock.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
 put("Content-Type", Collections.singletonList("application/json"));
 put("Cache-Control", Collections.singletonList("no-cache"));
 }});

 // When
 Map<String, String> headers = response.getHeaders();
 String contentType = response.getHeader("Content-Type");
 boolean hasNoCacheHeader = response.getHeader("Cache-Control").contains("no-cache");

 // Then
 assertEquals("application/json", contentType);
 assertTrue(hasNoCacheHeader);
 }

}