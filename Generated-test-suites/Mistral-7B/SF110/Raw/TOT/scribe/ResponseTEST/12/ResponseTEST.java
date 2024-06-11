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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connection;

 @Before
 public void setup() throws IOException {
 connection = mock(HttpURLConnection.class);
 response = new Response(connection);
 }

 @Test
 public void testResponseWithSuccessCode() throws IOException {
 when(connection.getResponseCode()).thenReturn(200);
 when(connection.getHeaderFields()).thenReturn(Mockito.mock(Map.class));

 response.parseBodyContents();

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testResponseWithErrorCode() throws IOException {
 when(connection.getResponseCode()).thenReturn(404);
 when(connection.getErrorStream()).thenReturn(Mockito.mock(InputStream.class));

 response.parseBodyContents();

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testResponseWithEmptyBody() throws IOException {
 when(connection.getResponseCode()).thenReturn(200);
 when(connection.getHeaderFields()).thenReturn(new HashMap<String, List<String>>());
 when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

 response.parseBodyContents();

 assertEquals(200, response.getCode());
 assertNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testResponseWithHeaders() throws IOException, UnsupportedEncodingException {
 when(connection.getResponseCode()).thenReturn(200);
 Map<String, List<String>> headerFields = new HashMap<>();
 headerFields.put("Content-Type", Collections.singletonList("application/json"));
 when(connection.getHeaderFields()).thenReturn(headerFields);

 response.parseBodyContents();

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertEquals("application/json", response.getHeader("Content-Type"));
 }
}