import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;
import org.scribe.utils.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connection;

 @Before
 public void setUp() throws IOException {
 connection = mock(HttpURLConnection.class);
 response = new Response(connection);
 }

 @Test
 public void testResponseWithSuccessStatusCode() throws IOException {
 // Given
 when(connection.getResponseCode()).thenReturn(200);
 when(connection.getHeaderFields()).thenReturn(StringUtils.createHeaderMap("Content-Type: text/plain"));
 when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("Test body".getBytes()));

 // When
 response.parseBodyContents();

 // Then
 assertEquals("Test body", response.getBody());
 }

 @Test
 public void testResponseWithErrorStatusCode() throws IOException {
 // Given
 when(connection.getResponseCode()).thenReturn(500);
 when(connection.getErrorStream()).thenReturn(Mockito.mock(InputStream.class));

 // When
 response.parseBodyContents();

 // Then
 assertNull(response.getBody());
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 // Given
 when(connection.getResponseCode()).thenReturn(200);
 when(connection.getInputStream()).thenReturn(StreamUtils.createEmptyInputStream());

 // When
 response.parseBodyContents();

 // Then
 assertNull(response.getBody());
 }

 @Test
 public void testResponseWithNullBody() throws IOException {
 // Given
 when(connection.getResponseCode()).thenReturn(200);
 when(connection.getInputStream()).thenReturn(null);

 // When
 response.parseBodyContents();

 // Then
 assertNull(response.getBody());
 }

 @Test(expected = IOException.class)
 public void testResponseWithException() throws IOException {
 // Given
 when(connection.getResponseCode()).thenThrow(new IOException("Test exception"));

 // When
 response.parseBodyContents();

 // Then
 // An IOException should be thrown
 }

 @Test
 public void testGetHeaders() {
 // Given
 Map<String, List<String>> headerFields = new HashMap<>();
 headerFields.put("Content-Type", Collections.singletonList("text/plain"));
 when(connection.getHeaderFields()).thenReturn(headerFields);

 // When
 Map<String, String> headers = response.getHeaders();

 // Then
 assertEquals("text/plain", headers.get("Content-Type"));
 }

 @Test
 public void testGetHeaderWithNullName() {
 // When
 String headerName = null;
 String headerValue = response.getHeader(headerName);

 // Then
 assertNull(headerValue);
 }
}