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
 public void testResponseWithSuccessCode() throws IOException, UnsupportedEncodingException {
 // Given
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("expected body".getBytes()));

 // When
 response = new Response(connectionMock);

 // Then
 assertNotNull(response.getBody());
 assertEquals("expected body", response.getBody());
 assertEquals(200, response.getCode());
 }

 @Test
 public void testResponseWithErrorCode() throws IOException {
 // Given
 when(connectionMock.getResponseCode()).thenReturn(404);
 when(connectionMock.getErrorStream()).thenReturn(Mockito.mock(InputStream.class));

 // When
 response = new Response(connectionMock);

 // Then
 assertNull(response.getBody());
 assertEquals(404, response.getCode());
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 // Given
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream( new byte[0] ));

 // When
 response = new Response(connectionMock);

 // Then
 assertNotNull(response.getBody());
 assertEquals(EMPTY, response.getBody());
 }

 @Test
 public void testResponseWithHeaders() throws IOException {
 // Given
 Map<String, List<String>> headerFields = new HashMap<>();
 headerFields.put("Content-Type", Collections.singletonList("text/plain"));
 when(connectionMock.getHeaderFields()).thenReturn(headerFields);

 // When
 response = new Response(connectionMock);

 // Then
 assertEquals("text/plain", response.getHeader("Content-Type"));
 assertNull(response.getHeader("Unknown-Header"));
 }
}