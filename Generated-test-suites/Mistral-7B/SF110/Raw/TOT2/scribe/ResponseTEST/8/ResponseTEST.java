import org.junit.Test;
import org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class ResponseTest {

    @Test
    public void testResponseConstructor() throws IOException {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(200);
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetBody() {
        Response response = new Response(mock(HttpURLConnection.class));
        assertEquals(EMPTY, response.getBody());

        String body = "Test Body";
        when(response.getStream()).thenReturn(new ByteArrayInputStream(body.getBytes()));
        assertEquals(body, response.getBody());
    }

    @Test
    public void testGetStream() {
        Response response = new Response(mock(HttpURLConnection.class));
        assertNull(response.getStream());

        InputStream stream = mock(InputStream.class);
        when(response.getStream()).thenReturn(stream);
        assertSame(stream, response.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = new Response(mock(HttpURLConnection.class));
        assertEquals(0, response.getCode());

        when(response.getStream()).thenThrow(new IOException());
        response.parseBodyContents();
        assertEquals(400, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response(mock(HttpURLConnection.class));
        assertEquals(Collections.emptyMap(), response.getHeaders());

        Map<String, String> headers = new HashMap<>();
        headers.put("Test-Header", "Test Value");
        when(response.getHeaders()).thenReturn(headers);
        assertEquals(headers, response.getHeaders());

        Map<String, String[]> headerFields = mock(Map.class);
        when(response.parseHeaders(Mockito.eq(mock(HttpURLConnection.class)))).thenReturn(headers);
        when(headerFields.keySet()).thenReturn(Arrays.asList("Test-Header"));
        assertEquals(headers, response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        Response response = new Response(mock(HttpURLConnection.class));
        assertEquals(EMPTY, response.getHeader("Test-Header"));

        Map<String, String> headers = new HashMap<>();
        headers.put("Test-Header", "Test Value");
        when(response.getHeaders()).thenReturn(headers);
        assertEquals("Test Value", response.getHeader("Test-Header"));

        when(response.getHeaders()).thenReturn(headerFields);
        when(headerFields.get("Test-Header")[0]).thenReturn("Test Value");
        assertEquals("Test Value", response.getHeader("Test-Header"));
    }

    @Test(expected = UnknownHostException.class)
    public void testUnknownHostException() throws IOException {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenThrow(new UnknownHostException());
        new Response(connection);
    }
}