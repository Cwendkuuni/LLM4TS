import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Response;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testResponse() throws IOException {
        Response response = new Response(createMockConnection(200, "OK", "{}"));
        Assert.assertEquals(200, response.getCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertNotNull(response.getHeaders());
        Assert.assertEquals("OK", response.getHeader("Status"));
    }

    @Test(expected = IOException.class)
    public void testConnectionFailure() {
        new Response(createMockConnection(0, null, null));
    }

    @Test
    public void testEmptyBody() throws IOException {
        Response response = new Response(createMockConnection(204, "No Content", null));
        Assert.assertNull(response.getBody());
    }

    @Test
    public void testErrorStream() throws IOException {
        Response response = new Response(createMockConnection(500, "Internal Server Error", null));
        Assert.assertNotNull(response.getStream());
        response.parseBodyContents(); // should not throw an exception
        Assert.assertEquals("", response.getBody());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNameHeader() {
        Response response = new Response(createMockConnection(200, "OK", new HashMap<String, String>() {{ put(null, "Value"); }}));
    }

    @Test
    public void testParseHeaders() throws IOException {
        Map<String, String> headers = new HashMap<String, String>() {{
            put("Status", "OK");
            put("Content-Type", "application/json");
        }};
        Response response = new Response(createMockConnection(200, null, headers));
        Assert.assertEquals("OK", response.getHeader("Status"));
        Assert.assertEquals("application/json", response.getHeader("Content-Type"));
        Assert.assertNull(response.getHeader(null));
    }

    @Test
    public void testWasSuccessful() throws IOException {
        Response response = new Response(createMockConnection(202, "Accepted", null));
        Assert.assertTrue(response.wasSuccessful());
        response = new Response(createMockConnection(401, "Unauthorized", null));
        Assert.assertFalse(response.wasSuccessful());
    }

    private HttpURLConnection createMockConnection(int statusCode, String reasonPhrase, Map<String, String> headers) throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(statusCode);
        when(connection.getResponseMessage()).thenReturn(reasonPhrase);
        when(connection.getHeaderFields()).thenReturn(new BasicHeaderElement[] { new BasicHeaderElement("Status", reasonPhrase), new BasicHeaderElement("Content-Type", "application/json") });
        return connection;
    }
}