import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

 private Response response;
 private HttpURLConnection connection;

 @Before
 public void setUp() throws IOException {
 URL url = new URL("http://example.com");
 connection = (HttpURLConnection) url.openConnection();
 response = new Response(connection);
 }

 @Test
 public void testResponseConstructor() throws IOException {
 assertEquals(200, response.getCode());
 assertNotNull(response.getBody());
 assertNotNull(response.getStream());
 assertNotNull(response.getHeaders());
 }

 @Test(expected = UnknownHostException.class)
 public void testUnknownHostException() throws IOException {
 connection.setDoInput(false);
 connection.disconnect();
 response = new Response(connection);
 fail("UnknownHostException should be thrown but it wasnot");
 }

 @Test
 public void testParseBodyContents() {
 String expectedBody = "This is the expected body content";
 connection.setDoOutput(true);
 OutputStream os = connection.getOutputStream();
 os.write(expectedBody.getBytes());
 os.flush();
 response = new Response(connection);
 assertEquals(expectedBody, response.getBody());
 }

 @Test
 public void testParseHeaders() {
 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put("Content-Type", "application/json; charset=UTF-8");
 expectedHeaders.put("Date", "Mon, 27 Jul 2009 08:22:11 GMT");
 assertEquals(expectedHeaders, response.getHeaders());
 }

 @Test
 public void testWasSuccessful() {
 assertTrue(response.wasSuccessful());
 }

 @Test
 public void testGetBody() {
 String expectedBody = "This is the expected body content";
 connection.setDoOutput(true);
 OutputStream os = connection.getOutputStream();
 os.write(expectedBody.getBytes());
 os.flush();
 response.parseBodyContents();
 assertEquals(expectedBody, response.getBody());
 }

 @Test
 public void testGetStream() throws IOException {
 ByteArrayInputStream expectedStream = new ByteArrayInputStream("This is the expected stream content".getBytes());
 connection.setDoOutput(true);
 connection.setDoInput(true);
 response = new Response(connection);
 InputStream actualStream = response.getStream();
 Assert.assertSame(expectedStream, actualStream);
 }

 @Test
 public void testGetCode() {
 assertEquals(200, response.getCode());
 }

 @Test
 public void testGetHeaders() {
 assertEquals(response.getHeaders().size(), 2);
 }

 @Test
 public void testGetHeader() {
 String expectedHeaderValue = "application/json; charset=UTF-8";
 assertEquals(expectedHeaderValue, response.getHeader("Content-Type"));
 }

 @Test
 public void testGetEmptyBody() {
 assertEquals(EMPTY, response.getBody());
 }

 @Test
 public void testGetErrorStream() throws IOException {
 connection.setResponseCode(500);
 response = new Response(connection);
 assertNotNull(response.getStream());
 }
}