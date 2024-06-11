import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;

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
 connectionMock = Mockito.mock(HttpURLConnection.class);
 Map<String, List<String>> headersMap = new HashMap<>();
 headersMap.put("Content-Type", Arrays.asList("application/json"));
 Mockito.when(connectionMock.getHeaderFields()).thenReturn(headersMap);
 response = new Response(connectionMock);
 }

 @Test
 public void testInstanceCreation() {
 Response instance = new Response(connectionMock);
 assertNotNull(instance.getBody());
 assertNull(instance.getStream());
 assertEquals(0, instance.getCode());
 assertNull(instance.getHeaders());
 }

 @Test
 public void testParseBodyContents() throws IOException {
 String bodyContent = "{\"name\":\"John\",\"age\":30}";
 Mockito.when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(bodyContent.getBytes()));
 response = new Response(connectionMock);
 assertEquals(bodyContent, response.getBody());
 }

 @Test
 public void testParseHeaders() {
 Mockito.when(connectionMock.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
 put("Content-Type", Arrays.asList("application/json"));
 put("X-Custom-Header", Arrays.asList("Custom Value"));
 }});
 response = new Response(connectionMock);
 assertEquals("application/json", response.getHeader("Content-Type"));
 assertEquals("Custom Value", response.getHeader("X-Custom-Header"));
 }

 @Test
 public void testWasSuccessful() {
 response = new Response(connectionMock);
 Mockito.when(connectionMock.getResponseCode()).thenReturn(200);
 assertTrue(response.wasSuccessful());

 response = new Response(connectionMock);
 Mockito.when(connectionMock.getResponseCode()).thenReturn(404);
 assertFalse(response.wasSuccessful());
 }

 @Test
 public void testGetCode() {
 response = new Response(connectionMock);
 Mockito.when(connectionMock.getResponseCode()).thenReturn(200);
 assertEquals(200, response.getCode());

 response = new Response(connectionMock);
 Mockito.when(connectionMock.getResponseCode()).thenReturn(404);
 assertEquals(404, response.getCode());
 }

 @Test
 public void testGetHeaders() {
 response = new Response(connectionMock);
 Map<String, String> headers = response.getHeaders();
 assertNotNull(headers);
 assertEquals(2, headers.size());

 headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 headers.put("X-Custom-Header", "Custom Value");
 assertEquals(headers, response.getHeaders());
 }

 @Test
 public void testGetHeader() {
 response = new Response(connectionMock);
 Mockito.when(connectionMock.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
 put("Content-Type", Arrays.asList("application/json"));
 put("X-Custom-Header", Arrays.asList("Custom Value"));
 }});
 assertEquals("application/json", response.getHeader("Content-Type"));
 assertEquals("Custom Value", response.getHeader("X-Custom-Header"));

 response = new Response(connectionMock);
 Mockito.when(connectionMock.getHeaderFields()).thenReturn(new HashMap<String, List<String>>());
 assertNull(response.getHeader("Content-Type"));
 }
}