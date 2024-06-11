import org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testResponseConstructor() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertNull(response.getBody());
        assertNotNull(response.getStream());
        Map<String, String> headers = response.getHeaders();
        assertNotNull(headers);
        assertEquals(2, headers.size());
    }

    @Test
    public void testResponseEmptyBody() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.getOutputStream().write(new byte[0]);
        connection.getOutputStream().close();
        connection.disconnect();
        Response response = new Response(connection);
        assertEquals(204, response.getCode());
        assertEquals(EMPTY, response.getBody());
        assertNull(response.getStream());
        Map<String, String> headers = response.getHeaders();
        assertNotNull(headers);
        assertEquals(2, headers.size());
    }

    @Test(expected = UnknownHostException.class)
    public void testResponseUnknownHost() throws IOException {
        URL url = new URL("http://nonexistenthost.com");
        Response response = new Response(url.openConnection());
        assertEquals(404, response.getCode());
        assertEquals(EMPTY, response.getBody());
    }

    @Test
    public void testParseBodyContents() throws IOException {
        String body = "Hello World";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(body.getBytes());
        Response response = new Response(null, 200, null, null, inputStream);
        String actualBody = response.parseBodyContents();
        assertEquals(body, actualBody);
    }

    @Test
    public void testParseHeaders() throws IOException {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "text/plain");
        expectedHeaders.put("Cache-Control", "max-age=0");

        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        Map<String, List<String>> headerFields = connection.getHeaderFields();

        for (String key : headerFields.keySet()) {
            List<String> values = headerFields.get(key);
            expectedHeaders.put(key, values.get(0));
        }

        Response response = new Response(connection);
        Map<String, String> headers = response.getHeaders();
        assertEquals(expectedHeaders, headers);
    }

    @Test
    public void testWasSuccessful() {
        Response response = new Response(null, 200, null, null, null);
        assertTrue(response.wasSuccessful());

        Response response2 = new Response(null, 401, null, null, null);
        assertFalse(response2.wasSuccessful());
    }

    @Test
    public void testGetBody() throws IOException {
        String body = "Hello World";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(body.getBytes());
        Response response = new Response(null, 200, null, null, inputStream);
        String actualBody = response.getBody();
        assertEquals(body, actualBody);
    }

    @Test
    public void testGetStream() throws IOException {
        String body = "Hello World";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(body.getBytes());
        Response response = new Response(null, 200, null, null, inputStream);
        InputStream actualStream = response.getStream();
        assertEquals(inputStream, actualStream);
    }

    @Test
    public void testGetCode() {
        Response response = new Response(null, 200, null, null, null);
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/plain");
        headers.put("Cache-Control", "max-age=0");

        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        Map<String, List<String>> headerFields = connection.getHeaderFields();

        for (String key : headerFields.keySet()) {
            List<String> values = headerFields.get(key);
            headers.put(key, values.get(0));
        }

        Response response = new Response(connection);
        Map<String, String> responseHeaders = response.getHeaders();
        assertEquals(headers, responseHeaders);
    }

    @Test
    public void testGetHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/plain");
        headers.put("Cache-Control", "max-age=0");

        Response response = new Response(null, 200, headers, null, null);
        String actualContentType = response.getHeader("Content-Type");
        assertEquals("text/plain", actualContentType);
    }
}