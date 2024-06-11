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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connectionMock;

 @Before
 public void setup() {
 connectionMock = mock(HttpURLConnection.class);
 response = new Response(connectionMock);
 }

 @Test
 public void testInstanciationWithSuccessfulResponse() throws IOException {
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getHeaderFields()).thenReturn(new HashMap<>());
 response.parseHeaders(connectionMock);
 assertEquals(200, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testInstanciationWithErrorResponse() throws IOException {
 when(connectionMock.getResponseCode()).thenThrow(new UnknownHostException());
 response.parseHeaders(connectionMock);
 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testInstanciationWithEmptyBody() throws IOException {
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getHeaderFields()).thenReturn(new HashMap<>());
 when(connectionMock.getErrorStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
 response.parseHeaders(connectionMock);
 assertNull(response.getBody());
 assertNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testInstanciationWithNonEmptyBody() throws IOException, UnsupportedEncodingException {
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getHeaderFields()).thenReturn(new HashMap<>());
 when(connectionMock.getInputStream()).thenReturn(StreamUtils.createStream("Test body"));
 response.parseHeaders(connectionMock);
 assertEquals("Test body", response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test
 public void testGetStatusCodeWithSuccessfulResponse() {
 response.parseHeaders(connectionMock);
 assertEquals(200, response.getCode());
 }

 @Test
 public void testGetStatusCodeWithErrorResponse() {
 response.parseHeaders(connectionMock);
 assertEquals(404, response.getCode());
 }

 @Test
 public void testGetBodyWithEmptyBody() {
 response.parseBodyContents();
 assertNull(response.getBody());
 }

 @Test
 public void testGetBodyWithNonEmptyBody() throws IOException {
 when(connectionMock.getInputStream()).thenReturn(StreamUtils.createStream("Test body"));
 response.parseHeaders(connectionMock);
 assertEquals("Test body", response.getBody());
 }

 @Test
 public void testGetHeadersWithEmptyHeaders() {
 response.parseHeaders(connectionMock);
 assertNotNull(response.getHeaders());
 assertEquals(0, response.getHeaders().size());
 }

 @Test
 public void testGetHeadersWithNonEmptyHeaders() {
 when(connectionMock.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
 put("Test-Header", new ArrayList<String>() {{ add("Test value"); }});
 }});
 response.parseHeaders(connectionMock);
 assertEquals("Test value", response.getHeader("Test-Header"));
 }
}