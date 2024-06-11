import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connection;

 @Before
 public void setup() {
 connection = mock(HttpURLConnection.class);
 response = new Response(connection);
 }

 @Test
 public void testResponseWithSuccessfulStatusCode() throws IOException {
 when(connection.getResponseCode()).thenReturn(200);
 when(connection.getInputStream()).thenReturn(Mockito.mock(InputStream.class));

 response.parseBodyContents();
 assertEquals("", response.getBody());
 assertNotNull(response.getStream());
 assertEquals(200, response.getCode());
 assertEquals(16, response.getHeaders().size());
 }

 @Test
 public void testResponseWithErrorStatusCode() throws IOException {
 when(connection.getResponseCode()).thenReturn(404);
 when(connection.getErrorStream()).thenReturn(Mockito.mock(InputStream.class));

 response.parseBodyContents();
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertEquals(404, response.getCode());
 assertEquals(0, response.getHeaders().size());
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 when(connection.getResponseCode()).thenReturn(200);
 when(connection.getInputStream()).thenReturn(Mockito.mock(InputStream.class));
 when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

 response.parseBodyContents();
 assertEquals("", response.getBody());
 assertNotNull(response.getStream());
 assertEquals(200, response.getCode());
 assertEquals(16, response.getHeaders().size());
 }

 @Test
 public void testResponseWithHeaders() throws IOException {
 when(connection.getResponseCode()).thenReturn(200);
 Map<String, List<String>> headers = new HashMap<>();
 headers.put("Content-Type", Arrays.asList("text/plain"));
 when(connection.getHeaderFields()).thenReturn(headers);
 when(connection.getInputStream()).thenReturn(Mockito.mock(InputStream.class));

 response.parseBodyContents();
 assertEquals("", response.getBody());
 assertNotNull(response.getStream());
 assertEquals(200, response.getCode());
 assertEquals(2, response.getHeaders().size());
 assertEquals("text/plain", response.getHeader("Content-Type"));
 }
}