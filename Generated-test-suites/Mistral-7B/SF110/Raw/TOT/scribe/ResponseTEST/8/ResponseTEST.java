import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseTest {

    private Response response;
    private HttpURLConnection connection;
    private Map<String, String> headers;

    @Before
    public void setup() {
        connection = mock(HttpURLConnection.class);
        headers = new HashMap<>();
    }

    @Test
    public void testResponseCreationWithSuccessfulConnection() throws IOException {
        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getHeaderFields()).thenReturn(headers);
        response = new Response(connection);

        assertEquals(200, response.getCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getStream());
        assertNotNull(response.getHeaders());
    }

    @Test
    public void testResponseCreationWithNotFoundConnection() throws IOException {
        when(connection.getResponseCode()).thenThrow(new UnknownHostException(""));
        response = new Response(connection);

        assertEquals(404, response.getCode());
        assertEquals(EMPTY, response.getBody());
    }

    @Test
    public void testParseBodyContents() throws IOException {
        byte[] bodyContent = "This is the body content".getBytes();
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(bodyContent));
        response = new Response(connection);

        assertEquals("This is the body content", response.getBody());
    }

    @Test
    public void testParseHeaders() {
        headers.put("Content-Type", "application/json");
        when(connection.getHeaderFields()).thenReturn(headers);
        response = new Response(connection);

        assertEquals("application/json", response.getHeader("Content-Type"));
    }
}