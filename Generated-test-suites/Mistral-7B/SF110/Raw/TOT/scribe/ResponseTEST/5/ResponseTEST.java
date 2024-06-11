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

import static org.junit.Assert.*;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connectionMock;

 @Before
 public void setup() throws IOException, UnsupportedEncodingException {
 connectionMock = Mockito.mock(HttpURLConnection.class);
 Mockito.when(connectionMock.getResponseCode()).thenReturn(200);
 Mockito.when(connectionMock.getHeaderFields()).thenReturn(getHeaderFields());
 Mockito.when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("Test body".getBytes()));
 response = new Response(connectionMock);
 }

 @Test
 public void testSuccessfulResponse() {
 assertTrue(response.wasSuccessful());
 assertEquals(200, response.getCode());
 assertEquals("Test body", response.getBody());
 Map<String, String> headers = response.getHeaders();
 assertTrue(headers.containsKey("Content-Type"));
 assertTrue(headers.containsKey("Content-Length"));
 }

 @Test
 public void testFailedConnection() {
 assertFalse(response.wasSuccessful());
 assertEquals(404, response.getCode());
 assertNull(response.getBody());
 }

 @Test
 public void testParseHeaders() {
 Map<String, String> headers = new HashMap<>();
 headers.put("Content-Type", "application/json");
 headers.put("Content-Length", "1234");
 Mockito.when(connectionMock.getHeaderFields()).thenReturn(headers);

 Response response = new Response(connectionMock);
 Map<String, String> parsedHeaders = response.getHeaders();
 assertEquals("application/json", parsedHeaders.get("Content-Type"));
 assertEquals("1234", parsedHeaders.get("Content-Length"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullHeader() {
 Response response = new Response(connectionMock);
 assertNull(response.getHeader(null));
 }

 @Test
 public void testUnknownHeader() {
 Response response = new Response(connectionMock);
 assertNull(response.getHeader("Unknown-Header"));
 }

 @Test
 public void testEmptyResponseBody() throws IOException {
 connectionMock = Mockito.mock(HttpURLConnection.class);
 Mockito.when(connectionMock.getResponseCode()).thenReturn(204);
 Response response = new Response(connectionMock);
 assertEquals("", response.getBody());
 }

 @Test
 public void testErrorResponseBody() throws IOException {
 connectionMock = Mockito.mock(HttpURLConnection.class);
 Mockito.when(connectionMock.getResponseCode()).thenReturn(500);
 response = new Response(connectionMock);
 assertEquals("", response.getBody());
 }

 private Map<String, List<String>> getHeaderFields() {
 Map<String, List<String>> headerFields = new HashMap<>();
 List<String> contentTypes = new ArrayList<>();
 contentTypes.add("application/json");
 headerFields.put("Content-Type", contentTypes);
 return headerFields;
 }
}