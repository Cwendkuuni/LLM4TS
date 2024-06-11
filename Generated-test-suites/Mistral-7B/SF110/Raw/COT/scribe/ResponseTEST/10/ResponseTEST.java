import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connection;

 @Before
 public void setup() throws IOException {
 connection = (HttpURLConnection) new URL("http://example.com").openConnection();
 response = new Response(connection);
 }

 @Test
 public void testResponseConstructor() {
 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getHeaders());
 assertNotNull(response.getStream());
 }

 @Test(expected = UnknownHostException.class)
 public void testUnknownHostException() {
 connection.setInstanceFollowRedirects(false);
 connection.disconnect();
 response = new Response(connection);
 }

 @Test
 public void testParseBodyContents() {
 String expectedBody = "This is the expected body content.";
 connection.setDoOutput(true);
 OutputStream os = connection.getOutputStream();
 byte[] input = expectedBody.getBytes();
 os.write(input, 0, input.length);
 os.flush();
 response = new Response(connection);
 assertEquals(expectedBody, response.getBody());
 }

 @Test
 public void testParseHeaders() {
 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put("Content-Type", "application/json");
 expectedHeaders.put("Cache-Control", "max-age=0");
 assertEquals(expectedHeaders, response.getHeaders());
 }

 @Test
 public void testWasSuccessful() {
 assertTrue(response.wasSuccessful());
 connection.setResponseCode(201);
 assertFalse(response.wasSuccessful());
 }

 @Test
 public void testGetBody() {
 String expectedBody = "This is the expected body content.";
 response.parseBodyContents();
 assertEquals(expectedBody, response.getBody());
 }

 @Test
 public void testGetStream() {
 InputStream expectedStream = new ByteArrayInputStream("This is the expected stream content".getBytes());
 response.stream = expectedStream;
 InputStream stream = response.getStream();
 assertEquals(expectedStream, stream);
 }

 @Test
 public void testGetCode() {
 assertEquals(200, response.getCode());
 connection.setResponseCode(404);
 assertEquals(404, response.getCode());
 }

 @Test
 public void testGetHeaders() {
 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put("Content-Type", "application/json");
 expectedHeaders.put("Cache-Control", "max-age=0");
 assertEquals(expectedHeaders, response.getHeaders());
 }

 @Test
 public void testGetHeader() {
 String expectedHeaderValue = "This is the expected header value.";
 response.headers.put("Content-Type", expectedHeaderValue);
 assertEquals(expectedHeaderValue, response.getHeader("Content-Type"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetHeaderNullName() {
 response.headers = null;
 response.getHeader(null);
 }

 @Test
 public void testGetHeaderNullValue() {
 response.headers.put("Content-Type", null);
 response.getHeader("Content-Type");
 }
}