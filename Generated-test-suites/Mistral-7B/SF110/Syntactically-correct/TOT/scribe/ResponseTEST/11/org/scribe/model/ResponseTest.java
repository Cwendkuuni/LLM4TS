import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connection;

 @Before
 public void setup() {
 connection = mock(HttpURLConnection.class);
 response = new Response(connection);
 }

 @Test
 public void testInstance() {
 assertNotNull(response);
 }

 @Test
 public void testEmptyResponse() {
 when(connection.getResponseCode()).thenReturn(404);
 response = new Response(connection);

 assertEquals("", response.getBody());
 assertNull(response.getStream());
 assertEquals(404, response.getCode());
 assertEquals(0, response.getHeaders().size());
 }

 @Test
 public void testSuccessfulResponseWithBody() throws IOException {
 String body = "Test body";
 when(connection.getResponseCode()).thenReturn(200);
 when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(body.getBytes()));

 response = new Response(connection);

 assertEquals(body, response.getBody());
 assertNotNull(response.getStream());
 assertEquals(200, response.getCode());
 assertEquals(0, response.getHeaders().size());
 }

 @Test
 public void testSuccessfulResponseWithoutBody() throws IOException {
 when(connection.getResponseCode()).thenReturn(204);
 when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

 response = new Response(connection);

 assertNull(response.getBody());
 assertNotNull(response.getStream());
 assertEquals(204, response.getCode());
 assertEquals(0, response.getHeaders().size());
 }

 @Test
 public void testErrorResponse() throws IOException {
 when(connection.getResponseCode()).thenReturn(500);
 response = new Response(connection);

 assertNull(response.getBody());
 assertNull(response.getStream());
 assertEquals(500, response.getCode());
 assertEquals(0, response.getHeaders().size());
 }

 @Test
 public void testHeaders() throws IOException {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "text/plain");
 when(connection.getHeaderFields()).thenReturn(headers);

 response = new Response(connection);

 assertEquals("text/plain", response.getHeader("Content-Type"));
 }

 @Test(expected = IOException.class)
 public void testConnectionError() throws IOException {
 when(connection.connect()).thenThrow(new IOException());

 response = new Response(connection);
 }
}