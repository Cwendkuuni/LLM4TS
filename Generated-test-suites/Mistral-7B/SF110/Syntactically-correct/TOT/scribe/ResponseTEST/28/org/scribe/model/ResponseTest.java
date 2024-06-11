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
import static org.junit.Assert.assertSame;
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
 public void testInstance() {
 Response r = new Response(null);
 assertNull(r.getBody());
 assertNull(r.getStream());
 assertNull(r.getHeaders());
 }

 @Test
 public void testResponseCode() throws IOException {
 when(connection.getResponseCode()).thenReturn(200);
 response = new Response(connection);
 assertEquals(200, response.getCode());
 }

 @Test
 public void testErrorResponseCode() throws IOException {
 when(connection.getResponseCode()).thenThrow(new UnknownHostException());
 response = new Response(connection);
 assertEquals(404, response.getCode());
 }

 @Test
 public void testGetStreamWhenSuccess() throws IOException {
 when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("success".getBytes()));
 response = new Response(connection);
 assertEquals("success", response.getBody());
 }

 @Test
 public void testGetStreamWhenError() throws IOException {
 when(connection.getErrorStream()).thenReturn(new ByteArrayInputStream("error".getBytes()));
 response = new Response(connection);
 assertEquals("error", response.getBody());
 }

 @Test
 public void testParseHeaders() throws IOException {
 Map<String, String> headers = new HashMap<String, String>();
 headers.put("Content-Type", "application/json");
 when(connection.getHeaderFields()).thenReturn(headers);
 response = new Response(connection);
 assertEquals("application/json", response.getHeader("Content-Type"));
 }

 @Test
 public void testWasSuccessful() {
 response = new Response(null);
 assertFalse(response.wasSuccessful());
 response = new Response(mock(HttpURLConnection.class));
 when(connection.getResponseCode()).thenReturn(201);
 assertTrue(response.wasSuccessful());
 }

 @Test
 public void testGetBodyWhenNull() throws IOException {
 response = new Response(null);
 assertNull(response.getBody());
 }

 @Test
 public void testGetBodyWhenStreamIsPresent() throws IOException {
 response = new Response(connection);
 when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("success".getBytes()));
 assertEquals("success", response.getBody());
 }

 @Test
 public void testGetBodyWhenStreamIsEmpty() throws IOException {
 response = new Response(connection);
 when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
 assertNull(response.getBody());
 }

 @Test
 public void testGetBodyWhenErrorStreamIsPresent() throws IOException {
 response = new Response(connection);
 when(connection.getErrorStream()).thenReturn(new ByteArrayInputStream("error".getBytes()));
 assertEquals("error", response.getBody());
 }

 @Test
 public void testGetHeaders() {
 response = new Response(connection);
 Map<String, List<String>> headersMap = new HashMap<String, List<String>>();
 headersMap.put("Content-Type", Collections.singletonList("application/json"));
 when(connection.getHeaderFields()).thenReturn(headersMap);
 assertEquals(headersMap, response.getHeaders());
 }

 @Test
 public void testGetHeader() {
 response = new Response(connection);
 Map<String, List<String>> headersMap = new HashMap<String, List<String>>();
 headersMap.put("Content-Type", Collections.singletonList("application/json"));
 when(connection.getHeaderFields()).thenReturn(headersMap);
 assertEquals("application/json", response.getHeader("Content-Type"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetHeaderWhenNullHeaderMap() {
 response = new Response(connection);
 when(connection.getHeaderFields()).thenReturn(null);
 response.getHeader("Content-Type");
 }

 @Test
 public void testGetHeaderWhenEmptyList() {
 response = new Response(connection);
 Map<String, List<String>> headersMap = new HashMap<String, List<String>>();
 headersMap.put("Content-Type", Collections.emptyList());
 when(connection.getHeaderFields()).thenReturn(headersMap);
 assertNull(response.getHeader("Content-Type"));
 }
}