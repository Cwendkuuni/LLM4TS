import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ResponseTest {

 private HttpURLConnection connectionMock;
 private Response response;

 @Before
 public void setUp() {
 connectionMock = mock(HttpURLConnection.class);
 response = new Response(connectionMock);
 }

 @Test
 public void testSuccessfulResponse() throws IOException {
 // Given: A successful HTTP response
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
 when(connectionMock.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
 put("Content-Type", new ArrayList<String>() {{ add("text/plain"); }});
 put("X-Custom-Header", new ArrayList<String>() {{ add("Custom Value"); }});
 }});

 // When: Creating a new Response object
 Response createdResponse = new Response(connectionMock);

 // Then: The code, body, headers, and stream should be set correctly
 assertEquals(200, createdResponse.getCode());
 assertNull(createdResponse.getBody());
 assertEquals(2, createdResponse.getHeaders().size());
 assertEquals("text/plain", createdResponse.getHeaders().get("Content-Type"));
 assertEquals("Custom Value", createdResponse.getHeaders().get("X-Custom-Header"));
 assertEquals(0, createdResponse.getStream().available());
 }

 @Test
 public void testFailingResponse() {
 // Given: A failing HTTP response
 when(connectionMock.getResponseCode()).thenThrow(new IOException("Connection error"));

 // When: Creating a new Response object
 Response createdResponse = new Response(connectionMock);

 // Then: The code, body, headers, and stream should be set correctly
 assertEquals(404, createdResponse.getCode());
 assertNull(createdResponse.getBody());
 assertNull(createdResponse.getHeaders());
 assertNull(createdResponse.getStream());
 }

 @Test
 public void testEmptyResponseBody() throws IOException {
 // Given: An HTTP response with an empty body
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0][]));
 when(connectionMock.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
 put("Content-Type", new ArrayList<String>() {{ add("text/plain"); }});
 }});

 // When: Creating a new Response object
 Response createdResponse = new Response(connectionMock);

 // Then: The code, body, headers, and stream should be set correctly
 assertEquals(200, createdResponse.getCode());
 assertEquals(EMPTY, createdResponse.getBody());
 assertNotNull(createdResponse.getHeaders());
 assertEquals(1, createdResponse.getHeaders().size());
 assertEquals("text/plain", createdResponse.getHeaders().get("Content-Type"));
 assertNull(createdResponse.getStream());
 }

 @Test
 public void testResponseWithBody() throws IOException {
 // Given: An HTTP response with a body
 String testBody = "This is a test body";
 when(connectionMock.getResponseCode()).thenReturn(200);
 when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(testBody.getBytes()));
 when(connectionMock.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
 put("Content-Type", new ArrayList<String>() {{ add("text/plain"); }});
 }});

 // When: Creating a new Response object
 Response createdResponse = new Response(connectionMock);

 // Then: The code, body, headers, and stream should be set correctly
 assertEquals(200, createdResponse.getCode());
 assertEquals(testBody, createdResponse.getBody());
 assertNotNull(createdResponse.getHeaders());
 assertEquals(1, createdResponse.getHeaders().size());
 assertEquals("text/plain", createdResponse.getHeaders().get("Content-Type"));
 assertNotNull(createdResponse.getStream());
 }

 @Test
 public void testResponseWithInvalidStatusCode() {
 // Given: An HTTP response with an invalid status code
 when(connectionMock.getResponseCode()).thenReturn(500);

 // When: Creating a new Response object
 Response createdResponse = new Response(connectionMock);

 // Then: The code, body, headers, and stream should be set correctly
 assertEquals(500, createdResponse.getCode());
 assertNull(createdResponse.getBody());
 assertNull(createdResponse.getHeaders());
 assertNull(createdResponse.getStream());
 }
}