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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connectionMock;

 @Before
 public void setUp() {
 connectionMock = Mockito.mock(HttpURLConnection.class);
 response = new Response(connectionMock);
 }

 @Test
 public void testResponseWithSuccessfulStatusAndBody() throws IOException {
 // Given
 String expectedBody = "Successful Response";
 ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedBody.getBytes());
 Mockito.when(connectionMock.getInputStream()).thenReturn(inputStream);
 Mockito.when(connectionMock.getResponseCode()).thenReturn(200);

 // When
 response.parseBodyContents();

 // Then
 assertEquals(expectedBody, response.getBody());
 }

 @Test
 public void testResponseWithEmptyBodyAndSuccessfulStatus() throws IOException {
 // Given
 Mockito.when(connectionMock.getInputStream()).thenReturn(StreamUtils.EMPTY_INPUT_STREAM);
 Mockito.when(connectionMock.getResponseCode()).thenReturn(200);

 // When
 response.parseBodyContents();

 // Then
 assertNull(response.getBody());
 }

 @Test
 public void testResponseWithErrorStatusAndNullBody() throws IOException {
 // Given
 Mockito.when(connectionMock.getErrorStream()).thenReturn(StreamUtils.EMPTY_INPUT_STREAM);
 Mockito.when(connectionMock.getResponseCode()).thenReturn(404);

 // When
 response.parseBodyContents();

 // Then
 assertNull(response.getBody());
 }

 @Test
 public void testResponseWithSuccessfulStatusAndNonEmptyHeaders() throws IOException {
 // Given
 Map<String, List<String>> headerFields = new HashMap<>();
 headerFields.put("Content-Type", Collections.singletonList("text/plain"));
 Mockito.when(connectionMock.getHeaderFields()).thenReturn(headerFields);
 Mockito.when(connectionMock.getResponseCode()).thenReturn(200);

 // When
 response.parseHeaders(connectionMock);

 // Then
 assertEquals(1, response.getHeaders().size());
 assertEquals("text/plain", response.getHeaders().get("Content-Type"));
 }

 @Test
 public void testResponseWithInvalidStatus() throws IOException {
 // Given
 Mockito.when(connectionMock.getResponseCode()).thenReturn(500);

 // When
 // Then
 assertNotNull(response);
 assertTrue(response.wasSuccessful());
 }
}