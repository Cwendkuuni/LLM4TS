import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Response;
import java.net.HttpURLConnection;
import java.net.URL;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ResponseTest {

    private HttpURLConnection connection;

    @Before
    public void setup() {
        initMocks(this.connection);
    }

    @Test
    public void testResponseConstructor() throws Exception {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Response response = new Response(connection);
        // Add asserts here based on the expected behavior
    }

    // Test methods here (getBody(), getStream(), getCode(), getHeaders(), getHeader())

    // Test constructor with a non-existing URL
    @Test(expected = UnknownHostException.class)
    public void testResponseConstructorWithInvalidURL() throws Exception {
        URL url = new URL("http://nonexistenturl.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        new Response(connection);
    }

    // Test getBody() with an empty response
    @Test
    public void testGetBodyEmptyResponse() throws Exception {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Response response = new Response(connection);
        assertEquals(EMPTY, response.getBody());
    }

    // Test getBody() with a response body
    @Test
    public void testGetBody() throws Exception {
        URL url = new URL("http://example.com");
        String expectedBody = "This is the response body";
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(expectedBody.getBytes()));
        when(connection.getErrorStream()).thenReturn(null);
        when(connection.getResponseCode()).thenReturn(200);
        Response response = new Response(connection);
        assertEquals(expectedBody, response.getBody());
    }

    // Test getHeaders() with an empty response
    @Test
    public void testGetHeadersEmptyResponse() throws Exception {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Response response = new Response(connection);
        assertEquals(Collections.emptyMap(), response.getHeaders());
    }

    // Test getHeaders() with a response having headers
    @Test
    public void testGetHeaders() throws Exception {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(new BasicHeaderElementIterator(new Header[] {
                new BasicHeader("Content-Type", "application/json"),
                new BasicHeader("Content-Length", "1024")
        }));
        Response response = new Response(connection);
        Map<String, String> headers = response.getHeaders();
        assertEquals("application/json", headers.get("Content-Type"));
        assertEquals("1024", headers.get("Content-Length"));
    }

    // Test wasSuccessful() with a successful response
    @Test
    public void testWasSuccessfulWithSuccessResponse() throws Exception {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.disconnect();
        when(connection.getResponseCode()).thenReturn(200);
        Response response = new Response(connection);
        assertTrue(response.wasSuccessful());
    }

    // Test wasSuccessful() with an unsuccessful response
    @Test
    public void testWasSuccessfulWithUnsuccessfulResponse() throws Exception {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.disconnect();
        when(connection.getResponseCode()).thenReturn(500);
        Response response = new Response(connection);
        assertFalse(response.wasSuccessful());
    }

    // Test getCode() with a successful response
    @Test
    public void testGetCodeWithSuccessResponse() throws Exception {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.disconnect();
        when(connection.getResponseCode()).thenReturn(200);
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
    }

    // Test getCode() with an unsuccessful response
    @Test
    public void testGetCodeWithUnsuccessfulResponse() throws Exception {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.disconnect();
        when(connection.getResponseCode()).thenReturn(500);
        Response response = new Response(connection);
        assertEquals(500, response.getCode());
    }

    // Test getStream() with a successful response
    @Test
    public void testGetStreamWithSuccessResponse() throws Exception {
        URL url = new URL("http://example.com");
        String expectedBody = "This is the response body";
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(expectedBody.getBytes()));
        when(connection.getErrorStream()).thenReturn(null);
        when(connection.getResponseCode()).thenReturn(200);
        Response response = new Response(connection);
        InputStream stream = response.getStream();
        byte[] testData = new byte[expectedBody.length()];
        int read = 0;
        while ((read = stream.read(testData, 0, testData.length)) != -1) {
            assertEquals(expectedBody.charAt(read), (char) testData[read]);
        }
        stream.close();
    }

    // Test getStream() with an unsuccessful response
    @Test(expected = IOException.class)
    public void testGetStreamWithUnsuccessfulResponse() throws Exception {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.disconnect();
        when(connection.getResponseCode()).thenReturn(500);
        Response response = new Response(connection);
        response.getStream();
    }
}