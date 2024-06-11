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
import static org.junit.Assert.assertTrue;

public class ResponseTest {

 private HttpURLConnection connection;
 private Response response;

 @Before
 public void setup() {
 connection = Mockito.mock(HttpURLConnection.class);
 response = new Response(connection);
 }

 @Test
 public void testEmptyResponse() throws IOException {
 Mockito.when(connection.getHeaderFields()).thenReturn(new HttpHeaders());
 Mockito.when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);

 response.parseBodyContents();

 assertEquals(HttpURLConnection.HTTP_NOT_FOUND, response.getCode());
 assertNull(response.getBody());
 }

 @Test
 public void testSuccessfulResponse() throws IOException {
 String body = "Successful response";
 byte[] bodyBytes = body.getBytes();
 Mockito.when(connection.getHeaderFields()).thenReturn(new HttpHeaders());
 Mockito.when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
 Mockito.when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(bodyBytes));

 response.parseBodyContents();

 assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
 assertEquals(body, response.getBody());
 }

 @Test
 public void testErrorResponse() throws IOException {
 String body = "Error response";
 byte[] bodyBytes = body.getBytes();
 Mockito.when(connection.getHeaderFields()).thenReturn(new HttpHeaders());
 Mockito.when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_INTERNAL_ERROR);
 Mockito.when(connection.getErrorStream()).thenReturn(new ByteArrayInputStream(bodyBytes));

 response.parseBodyContents();

 assertEquals(HttpURLConnection.HTTP_INTERNAL_ERROR, response.getCode());
 assertEquals(body, response.getBody());
 }

 @Test
 public void testGetHeaders() {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 Mockito.when(connection.getHeaderFields()).thenReturn(headers);

 assertEquals(headers, response.getHeaders());
 }

 @Test
 public void testGetHeader() {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 headers.put("Cache-Control", "max-age=0");
 Mockito.when(connection.getHeaderFields()).thenReturn(headers);

 assertEquals("application/json", response.getHeader("Content-Type"));
 assertEquals("max-age=0", response.getHeader("Cache-Control"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullHeaders() {
 Mockito.when(connection.getHeaderFields()).thenReturn(null);

 response.parseHeaders(connection);
 }
}