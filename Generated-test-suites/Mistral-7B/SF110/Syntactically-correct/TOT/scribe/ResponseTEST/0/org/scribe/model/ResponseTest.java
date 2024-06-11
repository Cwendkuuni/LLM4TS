import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connection;

 @Before
 public void setUp() {
 connection = mock(HttpURLConnection.class);
 response = new Response(connection);
 }

 @Test
 public void testInitializationWithSuccessStatusCode() throws IOException {
 when(connection.getResponseCode()).thenReturn(200);
 when(connection.getHeaderFields()).thenReturn(getMockHeaderFields());
 response.parseBodyContents();

 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testInitializationWithFailureStatusCode() throws IOException {
 when(connection.getResponseCode()).thenThrow(new UnknownHostException());

 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testInitializationWithErrorStream() throws IOException {
 when(connection.getResponseCode()).thenReturn(400);
 when(connection.getErrorStream()).thenReturn(new ByteArrayInputStream("Error message".getBytes()));

 assertEquals(400, response.getCode());
 assertNotNull(response.getBody());
 assertEquals("Error message", response.getBody());
 assertNull(response.getStream());
 }

 @Test
 public void testGetHeader() {
 when(connection.getHeaderFields()).thenReturn(getMockHeaderFields());
 when(connection.getHeaderFields().get(ArgumentMatchers.eq("Content-Type"))).thenReturn(new ArrayList<String>(){{ add("text/plain"); }});

 assertEquals("text/plain", response.getHeader("Content-Type"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetHeaderWithNullHeaderName() {
 response.getHeader(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetHeaderWithEmptyHeaderName() {
 response.getHeader("");
 }

 private Map<String, List<String>> getMockHeaderFields() {
 final Map<String, List<String>> headerFields = new HashMap<>();
 headerFields.put("Content-Type", Collections.singletonList("text/plain"));
 return headerFields;
 }
}